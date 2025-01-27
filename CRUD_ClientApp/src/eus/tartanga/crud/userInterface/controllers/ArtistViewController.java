package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.MaxCharacterException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.exception.TextEmptyException;
import eus.tartanga.crud.exception.UpdateException;
import eus.tartanga.crud.exception.WrongStockFormatException;
import eus.tartanga.crud.logic.ArtistFactory;
import eus.tartanga.crud.logic.ArtistManager;
import eus.tartanga.crud.model.Artist;
import eus.tartanga.crud.userInterface.factories.ArtistDateEditingCell;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javax.ws.rs.core.GenericType;

public class ArtistViewController {

    @FXML
    private TableView<Artist> artistTable;

    @FXML
    private TableColumn<Artist, byte[]> artistColumn;

    @FXML
    private TableColumn<Artist, String> nameColumn;

    @FXML
    private TableColumn<Artist, String> companyColumn;

    @FXML
    private TableColumn<Artist, String> lastAlbumColumn;

    @FXML
    private TableColumn<Artist, Date> debutColumn;

    @FXML
    private HBox detailsBox;

    @FXML
    private Label artistNameLabel;

    @FXML
    private ImageView artistImageView;

    @FXML
    private Label companyLabel;

    @FXML
    private Label lastAlbumLabel;

    @FXML
    private TextField searchField;

    private Stage stage;
    private Logger logger = Logger.getLogger(ArtistViewController.class.getName());
    private ObservableList<Artist> artistList = FXCollections.observableArrayList();
    private ContextMenu contextMenuInside;
    private ContextMenu contextMenuOutside;
    private ArtistManager artistManager;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root) {
        try {
            logger.info("Initializing Artist stage");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Artist");
            stage.setResizable(false);
            stage.show();

            artistManager = ArtistFactory.getArtistManager();

            artistTable.setEditable(true);

            // Configurar columnas de tabla
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            companyColumn.setCellValueFactory(new PropertyValueFactory<>("company"));
            lastAlbumColumn.setCellValueFactory(new PropertyValueFactory<>("lastAlbum"));
            debutColumn.setCellValueFactory(new PropertyValueFactory<>("debut"));
            artistColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

            // Hacer la columna 'debutColumn' editable
           final Callback<TableColumn<Artist, Date>, TableCell<Artist, Date>> dateCell
                    = (TableColumn<Artist, Date> param) -> new ArtistDateEditingCell();
            debutColumn.setCellFactory(dateCell);


            // Hacer la columna 'nameColumn' editable
            nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            nameColumn.setOnEditCommit(event -> {
                Artist artist = event.getRowValue();
                String originalName = artist.getName();

                try {
                    artist.setName(event.getNewValue());

                    if (artist.getName().isEmpty()) {
                        throw new TextEmptyException("El campo de nombre no puede estar vacío.");
                    } else if (artist.getName().length() > 20) {
                        throw new MaxCharacterException("El nombre no puede tener más de 20 caracteres.");
                    }

                    updateArtist(artist);
                } catch (TextEmptyException | MaxCharacterException e) {
                    logger.log(Level.SEVERE, null, e);
                    artist.setName(originalName);
                    artistTable.refresh();

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error de validación");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            });

            // Hacer la columna 'companyColumn' editable
            companyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            companyColumn.setOnEditCommit(event -> {
                Artist artist = event.getRowValue();
                String originalCompany = artist.getCompany();
                try {
                    artist.setCompany(event.getNewValue());
                    if (artist.getCompany() == null || artist.getCompany().isEmpty()) {
                        throw new TextEmptyException("El campo de compañia no puede estar vacío");
                    } else if (artist.getCompany().length() > 20) {
                        throw new MaxCharacterException("La compañia no puede tener más de 20 caracteres");
                    }
                    updateArtist(artist);
                } catch (TextEmptyException | MaxCharacterException e) {
                    Logger.getLogger(ArtistViewController.class.getName()).log(Level.SEVERE, null, e);
                    artist.setCompany(originalCompany);
                    artistTable.refresh();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error de validación");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
                artist.setCompany(event.getNewValue());
            });

            // Hacer la columna 'companyColumn' editable
            lastAlbumColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            lastAlbumColumn.setOnEditCommit(event -> {
                Artist artist = event.getRowValue();
                String originalAlbum = artist.getLastAlbum();
                try {
                    artist.setLastAlbum(event.getNewValue());
                    if (artist.getLastAlbum() == null || artist.getLastAlbum().isEmpty()) {
                        throw new TextEmptyException("El campo de Last Album no puede estar vacío");
                    } else if (artist.getLastAlbum().length() > 50) {
                        throw new MaxCharacterException("La Last Album no puede tener más de 50 caracteres");
                    }
                    updateArtist(artist);
                } catch (TextEmptyException | MaxCharacterException e) {
                    Logger.getLogger(ArtistViewController.class.getName()).log(Level.SEVERE, null, e);
                    artist.setCompany(originalAlbum);
                    artistTable.refresh();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error de validación");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
                artist.setLastAlbum(event.getNewValue());
            });

            // Formato para la columna de fecha 'debutColumn'
            debutColumn.setCellFactory(column -> new TableCell<Artist, Date>() {
                @Override
                protected void updateItem(Date date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || date == null) {
                        setText(null);
                    } else {
                        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        setText(localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    }
                }
            });

            // Configuración para la columna de imagen
            artistColumn.setCellFactory(column -> new TableCell<Artist, byte[]>() {
                private final ImageView imageView = new ImageView();

                {
                    imageView.setFitHeight(90);
                    imageView.setPreserveRatio(true);
                }

                @Override
                protected void updateItem(byte[] imageBytes, boolean empty) {
                    super.updateItem(imageBytes, empty);
                    if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                        setGraphic(null);
                        return;
                    }
                    if (imageBytes == null) {
                        Image noImage = new Image(getClass().getClassLoader().getResourceAsStream("eus/tartanga/crud/app/resources/noImage.png")); // Ruta de la imagen predeterminada
                        imageView.setImage(noImage);
                        setGraphic(imageView);
                    } else {
                        // Convertir byte[] a Image
                        Image image = new Image(new ByteArrayInputStream(imageBytes));
                        imageView.setImage(image);
                        setGraphic(imageView);

                    }
                }
            });

            artistList.addAll(findAllArtists());
            artistTable.setItems(artistList);

            // Filtro de búsqueda
            FilteredList<Artist> filteredData = new FilteredList<>(artistList, p -> true);
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(artist -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    return artist.getName().toLowerCase().contains(lowerCaseFilter)
                            || artist.getCompany().toLowerCase().contains(lowerCaseFilter);
                });
            });

            SortedList<Artist> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(artistTable.comparatorProperty());
            artistTable.setItems(sortedData);

            // Estilo alternado de filas
            artistTable.setRowFactory(tv -> {
                TableRow<Artist> row = new TableRow<>();
                row.itemProperty().addListener((obs, oldItem, newItem) -> {
                    if (newItem == null) {
                        row.setStyle("");
                    } else {
                        row.setStyle(row.getIndex() % 2 == 0 ? "-fx-background-color: #ffb6c1;" : "-fx-background-color: #fdd3e1;");
                    }
                });
                return row;
            });
        } catch (Exception e) {
            logger.severe("Error initializing Artist stage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<Artist> findAllArtists() {
        try {
            return artistManager.findAllArtist(new GenericType<List<Artist>>() {
            });
        } catch (ReadException e) {
            logger.severe("Error al obtener artistas: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
    }

    private void updateArtist(Artist artist) {
        try {
            System.out.println("Intento de update");
            artistManager.updateArtist(artist, artist.getArtistId().toString());
        } catch (UpdateException e) {
            System.out.println(e);
        }
    }

    /*createContextMenu();
    // Mostrará el Menú contextual con las opciones mIAddToCart, 
    //mIDeleteProduct, mIAddToProduct y mIPrint en el caso de hacer
    //click derecho en la tabla

    artistAnchorPane.setOnMousePressed (this::handleRightClickTable);
    // Mostrará el Menú contextual con las opciones mIAddToProduct y mIPrint 
    //en el caso de hacer click derecho fuera de la tabla
    artistAnchorPane.setOnMouseClicked (this::handleRightClick);
    
    private void handleRightClickTable(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            // Ocultar el otro ContextMenu si está visible
            if (contextMenuOutside.isShowing()) {
                contextMenuOutside.hide();
            }
            // Mostrar el ContextMenu dentro de la tabla
            contextMenuInside.show(artistTable, event.getScreenX(), event.getScreenY());
        } else {
            contextMenuInside.hide();
        }
    }

    private void handleRightClick(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) { // Detectar clic derecho
            // Ocultar el otro ContextMenu si está visible
            if (contextMenuInside.isShowing()) {
                contextMenuInside.hide();
            }
            // Mostrar el ContextMenu fuera de la tabla
            contextMenuOutside.show(artistAnchorPane, event.getScreenX(), event.getScreenY());
        } else {
            contextMenuOutside.hide();
        }
    }
    
      private void createContextMenu() {
        contextMenuInside = new ContextMenu();
        MenuItem addItem = new MenuItem("Add new product");
        addItem.setOnAction(this::handleAddProduct);
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(this::handleDeleteProduct);
        MenuItem printItemInside = new MenuItem("Print");
        printItemInside.setOnAction(this::printItems);
        contextMenuInside.getItems().addAll(addItem, deleteItem, printItemInside);

        // Crear menú contextual para clics fuera de la tabla
        contextMenuOutside = new ContextMenu();
        MenuItem printItemOutside = new MenuItem("Print");
        printItemOutside.setOnAction(this::printItems);
        MenuItem addItemOutside = new MenuItem("Add new product");
        addItemOutside.setOnAction(this::handleAddProduct);
        contextMenuOutside.getItems().addAll(printItemOutside, addItemOutside);
    }*/
}
