package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.DeleteException;
import eus.tartanga.crud.exception.MaxCharacterException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.exception.TextEmptyException;
import eus.tartanga.crud.exception.UpdateException;
import eus.tartanga.crud.exception.WrongStockFormatException;
import eus.tartanga.crud.logic.ArtistFactory;
import eus.tartanga.crud.logic.ArtistManager;
import eus.tartanga.crud.logic.ConcertFactory;
import eus.tartanga.crud.logic.ConcertManager;
import eus.tartanga.crud.logic.ProductManager;
import eus.tartanga.crud.model.Artist;
import eus.tartanga.crud.model.Concert;
import eus.tartanga.crud.model.Product;
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
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author olaia
 */
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
    private AnchorPane artistAnchorPane;

    @FXML
    private TextField searchField;

    @FXML
    private Button btnAddArtist;

    @FXML
    private Button btnDeleteArtist;

    @FXML
    private Button btnInfo;

    @FXML
    private DatePicker dpFrom;

    @FXML
    private DatePicker dpTo;

    private Stage stage;
    private Logger logger = Logger.getLogger(ArtistViewController.class.getName());
    private ObservableList<Artist> artistList = FXCollections.observableArrayList();
    private ObservableList<Concert> concertList = FXCollections.observableArrayList();
    private ContextMenu contextMenuInside;
    private ContextMenu contextMenuOutside;
    private ArtistManager artistManager;
    private ConcertManager concertManager;
    private ProductManager productManager;

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
            concertManager = ConcertFactory.getConcertManager();

            // concertList = FXCollections.observableArrayList(concertManager.findAllConcerts_XML(new GenericType<List<Concert>>(){
            //   }));
            artistTable.setEditable(true);
            debutColumn.setEditable(true);

            filterArtists();

            // Configurar columnas de tabla
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            companyColumn.setCellValueFactory(new PropertyValueFactory<>("company"));
            lastAlbumColumn.setCellValueFactory(new PropertyValueFactory<>("lastAlbum"));
            debutColumn.setCellValueFactory(new PropertyValueFactory<>("debut"));
            artistColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

            // Hacer la columna 'debutColumn' editable
            //NO HACE UPDATE
            final Callback<TableColumn<Artist, Date>, TableCell<Artist, Date>> dateCell
                    = (TableColumn<Artist, Date> param) -> new ArtistDateEditingCell();
            debutColumn.setCellFactory(dateCell);
            debutColumn.setOnEditCommit(event -> {
                Artist artist = event.getRowValue();
                artist.setDebut(event.getNewValue());
                try {
                    artistManager.updateArtist(artist, String.valueOf(artist.getArtistId()));
                } catch (UpdateException ex) {
                    Logger.getLogger(ArtistViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

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
                    artist.setLastAlbum(originalAlbum);
                    artistTable.refresh();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error de validación");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
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

            artistTable.setItems(artistList);
            btnAddArtist.setOnAction(this::handleAddArtist);
            //ARREGLAR DELETE DEL SERVIDOR
            btnDeleteArtist.setOnAction(this::handleDeleteArtist);
            btnInfo.setOnAction(this::handleInfoButton);
            //Inicializar los menu contextuales
            createContextMenu();
            // Mostrará el Menú contextual con las opciones mIAddToCart, 
            //mIDeleteProduct, mIAddToProduct y mIPrint en el caso de hacer
            //click derecho en la tabla
            artistTable.setOnMousePressed(this::handleRightClickTable);
            // Mostrará el Menú contextual con las opciones mIAddToProduct y mIPrint 
            //en el caso de hacer click derecho fuera de la tabla
            artistAnchorPane.setOnMouseClicked(this::handleRightClick);
            //Obtener una lista de todos los productos de mi base de datos
            artistList.addAll(findAllArtists());
            //Cargar la tabla Products con la información de los productos
            artistTable.setItems(artistList);
            //Configurar estilos de cada fila de la tabla
            configureRowStyling();

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

    public FilteredList<Artist> getArtistsBySearchField(String searchText, LocalDate fromDate, LocalDate toDate) {
        // Crea un FilteredList con la lista de artistas original
        FilteredList<Artist> filteredData = new FilteredList<>(artistList, a -> true);

        // Si el texto de búsqueda no es nulo o vacío, filtra los artistas
        if (searchText != null && !searchText.isEmpty()) {
            // Convierte el texto de búsqueda a minúsculas para hacer la comparación insensible a mayúsculas/minúsculas
            String lowerCaseFilter = searchText.toLowerCase();

            // Aplica el predicado de filtrado en función de los atributos de cada artista
            filteredData.setPredicate(artist -> {
                // Filtra solo por name y company
                boolean matchesSearch = artist.getName().toLowerCase().contains(lowerCaseFilter)
                        || artist.getCompany().toLowerCase().contains(lowerCaseFilter);

                // Filtra por fechas (si las fechas están definidas)
                boolean matchesDate = true;

                if (fromDate != null && toDate != null) {
                    Date artistReleaseDate = artist.getDebut();  // Esto es Date (por ejemplo, java.util.Date)

                    // Convertimos LocalDate a java.sql.Date para la comparación
                    java.sql.Date fromSQLDate = java.sql.Date.valueOf(fromDate);
                    java.sql.Date toSQLDate = java.sql.Date.valueOf(toDate);

                    // Comparamos las fechas
                    matchesDate = !artistReleaseDate.before(fromSQLDate) && !artistReleaseDate.after(toSQLDate);
                }

                // Devuelve true solo si el artista cumple con todos los filtros
                return matchesSearch && matchesDate;
            });
        } else {
            // Si no hay texto de búsqueda, solo filtra por fechas
            filteredData.setPredicate(artist -> {
                boolean matchesDate = true;

                if (fromDate != null && toDate != null) {
                    Date artistReleaseDate = artist.getDebut();  // Esto es Date (por ejemplo, java.util.Date)

                    // Convertimos LocalDate a java.sql.Date para la comparación
                    java.sql.Date fromSQLDate = java.sql.Date.valueOf(fromDate);
                    java.sql.Date toSQLDate = java.sql.Date.valueOf(toDate);

                    // Comparamos las fechas
                    matchesDate = !artistReleaseDate.before(fromSQLDate) && !artistReleaseDate.after(toSQLDate);
                }

                // Si no hay texto de búsqueda, solo devuelve el resultado de las fechas
                return matchesDate;
            });
        }

        return filteredData; // Devuelve el filtro aplicado
    }

    private void createContextMenu() {
        contextMenuInside = new ContextMenu();
        MenuItem addItem = new MenuItem("Add new artist");
        addItem.setOnAction(this::handleAddArtist);
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(this::handleDeleteArtist);
        MenuItem printItemInside = new MenuItem("Print");
        printItemInside.setOnAction(this::printItems);
        contextMenuInside.getItems().addAll(addItem, deleteItem, printItemInside);

        // Crear menú contextual para clics fuera de la tabla
        contextMenuOutside = new ContextMenu();
        MenuItem printItemOutside = new MenuItem("Print");
        printItemOutside.setOnAction(this::printItems);
        MenuItem addItemOutside = new MenuItem("Add new artist");
        addItemOutside.setOnAction(this::handleAddArtist);
        contextMenuOutside.getItems().addAll(printItemOutside, addItemOutside);
    }

    private void handleAddArtist(ActionEvent event) {
        Artist artist = new Artist(); // Crea un nuevo artista vacío
        try {
            artistManager.createArtist(artist); // Llama al método para añadir el artista
        } catch (AddException e) {
            System.out.println(e); // Imprime la excepción en caso de error
        }
        artistTable.getItems().add(artist); // Agrega el artista a la tabla
    }

    private void handleInfoButton(ActionEvent event) {
        try {
            //LOGGER.info("Loading help view...");
            //Load node graph from fxml file
            FXMLLoader loader
                    = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/HelpArtistView.fxml"));
            Parent root = (Parent) loader.load();
            HelpController helpController
                    = ((HelpController) loader.getController());
            //Initializes and shows help stage
            helpController.initAndShowStage(root);
        } catch (Exception ex) {
            //If there is an error show message and
            //log it.
            System.out.println(ex.getMessage());

        }
    }

    private void handleDeleteArtist(ActionEvent event) {
        // Obtener el artista seleccionado en la tabla
        Artist selectedArtist = artistTable.getSelectionModel().getSelectedItem();

        if (selectedArtist == null) {
            logger.warning("No se ha seleccionado ningún artista para eliminar.");
            return;
        }

        try {
            // 1. ELIMINAR CONCIERTOS RELACIONADOS CON EL ARTISTA
            eliminarConciertosAsociados(selectedArtist);

            // 2. ELIMINAR PRODUCTOS RELACIONADOS CON EL ARTISTA
            eliminarProductosAsociados(selectedArtist);

            // 3. ELIMINAR EL ARTISTA
            logger.info("Intentando eliminar artista: " + selectedArtist.getName() + " con ID: " + selectedArtist.getArtistId());
            artistManager.removeArtist(selectedArtist.getArtistId().toString());
            artistList.remove(selectedArtist);
            logger.info("Artista eliminado con éxito: " + selectedArtist.getName());

        } catch (Exception e) {
            logger.severe("Error al eliminar el artista y sus relaciones: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para eliminar los conciertos asociados a un artista
    private void eliminarConciertosAsociados(Artist selectedArtist) {
        // Obtener la lista de todos los conciertos
        List<Concert> concertList = concertManager.findAllConcerts_XML(new GenericType<List<Concert>>() {
        });

        if (concertList == null) {
            logger.severe("No se pudo obtener la lista de conciertos.");
            return;
        }

        // Filtrar los conciertos que contienen al artista seleccionado
        List<Concert> concertsToDelete = concertList.stream()
                .filter(concert -> {
                    if (concert.getArtistList() == null) {
                        logger.warning("El concierto " + concert.getConcertName() + " no tiene artistas asociados.");
                        return false;
                    }
                    return concert.getArtistList().contains(selectedArtist);
                })
                .collect(Collectors.toList());

        logger.info("Se encontraron " + concertsToDelete.size() + " conciertos asociados al artista " + selectedArtist.getName());

        // Eliminar los conciertos relacionados con el artista
        for (Concert concert : concertsToDelete) {
            logger.info("Intentando eliminar concierto: " + concert.getConcertName() + " con ID: " + concert.getConcertId());
            concertManager.removeConcert(concert.getConcertId().toString());
            logger.info("Concierto eliminado con éxito: " + concert.getConcertName());
        }
    }

// Método para eliminar los productos asociados a un artista
    private void eliminarProductosAsociados(Artist selectedArtist) {
        try {
            // Obtener la lista de todos los productos
            List<Product> productList = productManager.findAll_XML(new GenericType<List<Product>>() {
            });
            
            if (productList == null) {
                logger.severe("No se pudo obtener la lista de productos.");
                return;
            }
            
            // Filtrar los productos que pertenecen al artista seleccionado
            List<Product> productsToDelete = productList.stream()
                    .filter(product -> product.getArtist() != null && product.getArtist().equals(selectedArtist))
                    .collect(Collectors.toList());
            
            logger.info("Se encontraron " + productsToDelete.size() + " productos asociados al artista " + selectedArtist.getName());
            
            // Eliminar los productos relacionados con el artista
            for (Product product : productsToDelete) {
                try {
                    logger.info("Intentando eliminar producto: " + product.getTitle() + " con ID: " + product.getProductId());
                    productManager.remove(product.getProductId().toString());
                    logger.info("Producto eliminado con éxito: " + product.getTitle());
                } catch (DeleteException ex) {
                    Logger.getLogger(ArtistViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (ReadException ex) {
            Logger.getLogger(ArtistViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void printItems(ActionEvent event) {
        /*  try {
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/eus/tartanga/crud/userInterface/report/productReport.jrxml"));
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<Product>) this.productTable.getItems());
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (JRException ex) {
            //EXCEPCIONES DE ESAS
        }*/
    }

    // Filtro de búsqueda
    private void filterArtists() {
        // Mientras el usuario está escribiendo ese valor se usará para filtrar los artistas de la tabla
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Mientras el usuario está escribiendo ese valor se usará para filtrar los artistas de la tabla
            applyFilter();
        });
        // Filtra por rango de fechas cuando cambian los DatePickers
        dpFrom.valueProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter();
        });

        dpTo.valueProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter();
        });

    }

    private void applyFilter() {
        // Obtener el texto de búsqueda y las fechas seleccionadas
        String searchText = searchField.getText();
        LocalDate fromDate = dpFrom.getValue();
        LocalDate toDate = dpTo.getValue();

        // Aplicar el filtrado de productos
        FilteredList<Artist> filteredArtist = getArtistsBySearchField(searchText, fromDate, toDate);

        // Actualizar la tabla con los productos filtrados
        SortedList<Artist> sortedData = new SortedList<>(filteredArtist);
        sortedData.comparatorProperty().bind(artistTable.comparatorProperty());
        artistTable.setItems(sortedData);
    }

    private void configureRowStyling() {
        artistTable.setRowFactory(tv -> {
            TableRow<Artist> row = new TableRow<>();

            // Establecer color de fondo alterno según el índice
            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem == null) {
                    row.setStyle("");  // Resetear el color si no hay datos
                } else {
                    int index = row.getIndex();
                    if (index % 2 == 0) {
                        row.setStyle("-fx-background-color: #ffb6c1;"); // Rosa
                    } else {
                        row.setStyle("-fx-background-color: #fdd3e1;"); // Rosa claro
                    }
                }
            });

            return row;
        });
    }

    private void updateArtist(Artist artist) {
        try {
            System.out.println("Intento de update");
            artistManager.updateArtist(artist, artist.getArtistId().toString());
        } catch (UpdateException e) {
            System.out.println(e);
        }
    }

}
