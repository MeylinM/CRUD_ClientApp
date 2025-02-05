/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.DeleteException;
import eus.tartanga.crud.exception.MaxCharacterException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.exception.TextEmptyException;
import eus.tartanga.crud.exception.UpdateException;
import eus.tartanga.crud.logic.ArtistFactory;
import eus.tartanga.crud.logic.ArtistManager;
import eus.tartanga.crud.logic.ConcertFactory;
import eus.tartanga.crud.logic.ConcertManager;
import eus.tartanga.crud.logic.FanetixClientFactory;
import eus.tartanga.crud.logic.FanetixClientManager;
import eus.tartanga.crud.model.Artist;
import eus.tartanga.crud.model.Concert;
import eus.tartanga.crud.model.FanetixClient;
import eus.tartanga.crud.model.FanetixUser;
import eus.tartanga.crud.userInterface.factories.ConcertDateEditingCell;
import eus.tartanga.crud.userInterface.factories.ConcertTimeEditingCell;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author 2dam
 */
public class ConcertViewController {

    @FXML
    private TableView<Concert> concertTable;

    @FXML
    private TableColumn<Concert, byte[]> billboardColumn;

    @FXML
    private TableColumn<Concert, String> concertColumn;

    @FXML
    private TableColumn<Concert, String> artistColumn;

    @FXML
    private TableColumn<Concert, String> locationColumn;

    @FXML
    private TableColumn<Concert, String> cityColumn;

    @FXML
    private TableColumn<Concert, Date> dateColumn;

    @FXML
    private TableColumn<Concert, Date> timeColumn;

    @FXML
    private AnchorPane concertAnchorPane;

    @FXML
    private CheckBox cbxComingSoon;

    @FXML
    private Button btnAddConcert;

    @FXML
    private Button btnDeleteConcert;

    @FXML
    private TextField tfSearch;

    @FXML
    private Button btnInfo;

    @FXML
    private DatePicker dpFrom;

    @FXML
    private DatePicker dpTo;

    private Stage stage;
    private Logger logger = Logger.getLogger(ConcertViewController.class.getName());
    private ContextMenu contextMenuInside;
    private ContextMenu contextMenuOutside;
    private ConcertManager concertManager;
    private ObservableList<Concert> concertList = FXCollections.observableArrayList();
    private ArtistManager artistManager;
    private ObservableList<Artist> artistList = FXCollections.observableArrayList();
    private Alert alert;
    private FanetixClient client;
    private FanetixClientManager clientManager;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root) {
        try {
            logger.info("Initializizng Concert stage");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Concert");
            //Añadir a la ventana el ícono “FanetixLogo.png”.
            stage.getIcons().add(new Image("eus/tartanga/crud/app/resources/logo.png"));
            stage.setResizable(false);
            stage.show();

            //Inicializar las factorias
            concertManager = ConcertFactory.getConcertManager();
            artistManager = ArtistFactory.getArtistManager();
            clientManager = FanetixClientFactory.getFanetixClientManager();

            //Conseguir la información del usuario loggeado
            FanetixUser user = MenuBarViewController.getLoggedUser();
            client = getFanetixClient(user.getEmail());

            // Configurar columnas básicas
            billboardColumn.setCellValueFactory(new PropertyValueFactory<>("billboard"));
            concertColumn.setCellValueFactory(new PropertyValueFactory<>("concertName"));
            artistColumn.setCellValueFactory(new PropertyValueFactory<>("artistList"));
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("concertDate"));
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("concertTime"));

            //Esta columna no es editable
            billboardColumn.setCellFactory(column -> new TableCell<Concert, byte[]>() {
                private final ImageView imageView = new ImageView();

                {
                    imageView.setFitHeight(180);
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
                        Image noImage = new Image(getClass().getClassLoader().getResourceAsStream("eus/tartanga/crud/app/resources/noImage.png"));
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

            if (client != null) {
                dateColumn.setCellFactory(column -> new TableCell<Concert, Date>() {
                    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(dateFormat.format(item));
                        }
                    }
                });
                timeColumn.setCellFactory(column -> new TableCell<Concert, Date>() {
                    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(timeFormat.format(item));
                        }
                    }
                });
                btnAddConcert.setVisible(false);
                btnDeleteConcert.setVisible(false);
            } else {
                //Selección múltiple en tabla para el uso del botón delete.
                concertTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                //Hacer la tabla editable
                concertTable.setEditable(true);
                //Obtener lista de artistas
                artistList = FXCollections.observableArrayList(artistManager.findAllArtist(new GenericType<List<Artist>>() {
                }));
                //Hacer las columnas editables
                concertColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                concertColumn.setOnEditCommit(event -> {
                    Concert concert = event.getRowValue();
                    String concertName = concert.getConcertName();
                    try {
                        concert.setConcertName(event.getNewValue());
                        if (concert.getConcertName() == null || concert.getConcertName().isEmpty()) {
                            throw new TextEmptyException("El nombre del concierto o tour no puede estar vacío");
                        } else if (concert.getConcertName().length() > 50) {
                            throw new MaxCharacterException("El nombre del concierto no puede tener mas de 50 caracteres");
                        }
                        updateConcert(concert);
                    } catch (TextEmptyException | MaxCharacterException e) {
                        logger.severe(e.getMessage());
                        concert.setConcertName(concertName);
                        concertTable.refresh();
                        alert.setTitle("Error");
                        alert.setHeaderText("Error de validación");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                });

                locationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                locationColumn.setOnEditCommit(event -> {
                    Concert concert = event.getRowValue();
                    String concertLocation = concert.getLocation();
                    try {
                        concert.setLocation(event.getNewValue());
                        if (concert.getLocation() == null || concert.getLocation().isEmpty()) {
                            throw new TextEmptyException("La ubicación del concierto o tour no puede estar vacío");
                        } else if (concert.getLocation().length() > 50) {
                            throw new MaxCharacterException("El nombre del concierto no puede tener mas de 50 caracteres");
                        }
                        updateConcert(concert);
                    } catch (TextEmptyException | MaxCharacterException e) {
                        logger.severe(e.getMessage());
                        concert.setLocation(concertLocation);
                        concertTable.refresh();
                        alert.setTitle("Error");
                        alert.setHeaderText("Error de validación");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                    concert.setLocation(event.getNewValue());//ESTO PORQUE AQUI OTRA VEZ SI EN EL OTRO NO TA
                });

                cityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                cityColumn.setOnEditCommit(event -> {
                    Concert concert = event.getRowValue();
                    String concertCity = concert.getCity();
                    try {
                        concert.setCity(event.getNewValue());
                        if (concert.getCity() == null || concert.getCity().isEmpty()) {
                            throw new TextEmptyException("La ubicación del concierto o tour no puede estar vacío");
                        } else if (concert.getCity().length() > 50) {
                            throw new MaxCharacterException("El nombre del concierto no puede tener mas de 50 caracteres");
                        }
                        updateConcert(concert);
                    } catch (TextEmptyException | MaxCharacterException e) {
                        logger.severe(e.getMessage());
                        concert.setCity(concertCity);
                        concertTable.refresh();
                        alert.setTitle("Error");
                        alert.setHeaderText("Error de validación");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                    concert.setCity(event.getNewValue());//ESTO PORQUE AQUI OTRA VEZ SI EN EL OTRO NO TA
                });

                final Callback<TableColumn<Concert, Date>, TableCell<Concert, Date>> dateCell
                        = (TableColumn<Concert, Date> param) -> new ConcertDateEditingCell();
                dateColumn.setCellFactory(dateCell);
                dateColumn.setOnEditCommit(event -> {
                    Concert concert = event.getRowValue();
                    concert.setConcertDate(event.getNewValue());
                    updateConcert(concert);
                });

                final Callback<TableColumn<Concert, Date>, TableCell<Concert, Date>> timeCell
                        = (TableColumn<Concert, Date> param) -> new ConcertTimeEditingCell();
                timeColumn.setCellFactory(timeCell);
                timeColumn.setOnEditCommit(event -> {
                    Concert concert = event.getRowValue();
                    concert.setConcertTime(event.getNewValue());
                    updateConcert(concert);
                });
                btnAddConcert.setOnAction(this::handleAddConcert);
                btnDeleteConcert.setOnAction(this::handleDeleteConcert);

            }
            btnInfo.setOnAction(this::handleInfoButton);
            //Inicializar los menu contextuales
            createContextMenu();
            //Menu contextual de dentro de la tabla
            concertTable.setOnMousePressed(this::handleRightClickTable);
            //Menu contextual de fuera de la tabla
            concertAnchorPane.setOnMouseClicked(this::handleRightClick);
            //Obtener una lista de todos los concierto de la base de datos
            concertList.addAll(findAllConcerts());
            //Cargar la tabla con todos los conciertos
            concertTable.setItems(concertList);
            //Gestion de los filtrados
            filterConcerts();
            //Configurar estilos de cada fila de la tabla
            configureRowStyling();

        } catch (WebApplicationException e) {
            String errorMsg = "Error" + e.getMessage();
            logger.severe(errorMsg);
        } catch (ReadException ex) {
            logger.severe(ex.getMessage());
        }
    }

    private void handleAddConcert(ActionEvent event) {
        try {

            Concert concert = new Concert();
            concert.setArtistList(artistList);
            concertManager.createConcert_XML(concert);
            concertTable.getItems().add(concert);
            refreshConcertList();
        } catch (AddException e) {
            logger.severe(e.getMessage());
            showAlert("Server error", "An error occurred while creating the concert");
        }
    }

    private void refreshConcertList() {
        List<Concert> updatedConcerts = findAllConcerts();
        concertList.setAll(updatedConcerts);
        concertTable.refresh();
    }

    private void handleDeleteConcert(ActionEvent event) {
        ObservableList<Concert> selectedConcerts = concertTable.getSelectionModel().getSelectedItems();

        if (selectedConcerts.isEmpty()) {
            showAlert("Error de selección", "No se ha seleccionado ningún concierto");
            return;
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Do you want to delete the selected concert(s)?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                for (Concert concert : new ArrayList<>(selectedConcerts)) {
                    concertManager.removeConcert(concert.getConcertId().toString());
                    //Elimina el producto de la lista observable
                    concertList.remove(concert);
                    logger.log(Level.INFO, "Concierto eliminado con exito{0}", concert.getConcertName());
                }
            } catch (DeleteException ex) {
                logger.log(Level.SEVERE, "Error al eliminar el concierto{0}", ex.getMessage());
                showAlert("Server error", "Error al eliminar el concierto");
            }
        }
    }

    private void handleInfoButton(ActionEvent event) {
        try {
            FXMLLoader loader
                    = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ConcertHelpView.fxml"));
            Parent root = (Parent) loader.load();
            ConcertHelpController helpController
                    = ((ConcertHelpController) loader.getController());
            //Initializes and shows help stage
            if (client != null) {
                helpController.initAndShowStageClient(root);
            } else {
                helpController.initAndShowStageAdmin(root);
            }
        } catch (IOException ex) {
            logger.warning("No se ha podido cargar la ayuda");
            showAlert("Error en el help", "No se ha podido cargar la ayuda");
        }
    }

    private List<Concert> findAllConcerts() {
        List<Concert> concerts = null;
        try {
            concerts = concertManager.findAllConcerts_XML(new GenericType<List<Concert>>() {
            });
            return concerts;
        } catch (ReadException ex) {
            Logger.getLogger(ConcertViewController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error de servidor", "Ha ocurrido un error al cargar los conciertos");
        }
        return concerts;
    }

    private void updateConcert(Concert concert) {
        try {
            concertManager.updateConcert_XML(concert, concert.getConcertId().toString());
        } catch (UpdateException e) {
            logger.severe(e.getMessage());
            showAlert("Error de servior", "Ha ocurrido un error al actualizar los conciertos");
        }
    }

    private void createContextMenu() {
        //Crear menú contextual dentro de la tabla
        contextMenuInside = new ContextMenu();
        MenuItem printItemInside = new MenuItem("Print");
        printItemInside.setOnAction(this::printItems);
        if (client != null) {
            contextMenuInside.getItems().addAll(printItemInside);
        } else {
            MenuItem addItem = new MenuItem("Add new concert");
            addItem.setOnAction(this::handleAddConcert);
            MenuItem deleteItem = new MenuItem("Delete");
            deleteItem.setOnAction(this::handleDeleteConcert);
            MenuItem selectArtistItem = new MenuItem("Select artist/s");
            selectArtistItem.setOnAction(event -> {
                Concert selectedArtist = concertTable.getSelectionModel().getSelectedItem();
                if (selectedArtist != null) {
                    showArtistSelectionDialog(selectedArtist);
                }
            });
            contextMenuInside.getItems().addAll(addItem, deleteItem, printItemInside, selectArtistItem);
        }
        // Crear menú contextual para clics fuera de la tabla
        contextMenuOutside = new ContextMenu();
        MenuItem printItemOutside = new MenuItem("Print");
        printItemOutside.setOnAction(this::printItems);
        if (client != null) {
            contextMenuOutside.getItems().addAll(printItemOutside);
        } else {
            MenuItem addItemOutside = new MenuItem("Add new concert");
            addItemOutside.setOnAction(this::handleAddConcert);
            contextMenuOutside.getItems().addAll(printItemOutside, addItemOutside);
        }
    }

    private void showArtistSelectionDialog(Concert concert) {
        Stage artistStage = new Stage();
        artistStage.setTitle("Select Artists");
        ListView<Artist> artistListView = new ListView<>();
        ObservableList<Artist> selectedArtist = FXCollections.observableArrayList(); // Lista de elementos seleccionados manualmente

        try {
            List<Artist> artists = artistList;
            // Agregar los artistas a la lista de artistas
            artistListView.setItems(FXCollections.observableArrayList(artists));
            // Agregar manejador de clics personalizados
            artistListView.setCellFactory(lv -> {
                ListCell<Artist> cell = new ListCell<Artist>() {
                    @Override
                    protected void updateItem(Artist item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getName()); // Mostrar el nombre de la categoría
                        } else {
                            setText(null);
                        }
                    }
                };
                cell.setOnMouseClicked(event -> {
                    if (cell.getItem() != null) {
                        Artist clickedCategory = cell.getItem();
                        if (selectedArtist.contains(clickedCategory)) {
                            selectedArtist.remove(clickedCategory); // Deseleccionar si ya está seleccionado
                            cell.setStyle(""); // Restablecer estilo
                        } else {
                            selectedArtist.add(clickedCategory); // Seleccionar si no está seleccionado
                            cell.setStyle("-fx-background-color: #fdd3e1;");
                        }
                    }
                });
                return cell;
            });

            Button confirmButton = new Button("Save");
            confirmButton.setStyle("-fx-background-color: #f9eccf");
            confirmButton.setOnAction(e -> {
                if (!selectedArtist.isEmpty()) {
                    try {
                        concert.setArtistList(selectedArtist);
                        concertManager.updateConcert_XML(concert, concert.getConcertId().toString());
                        artistStage.close();
                        concertTable.refresh();
                        logger.log(Level.INFO, "Selected Artist: {0}", concert.getConcertName());
                    } catch (UpdateException ex) {
                        Logger.getLogger(ConcertViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            VBox layout = new VBox(10);
            layout.setPadding(new javafx.geometry.Insets(10));
            layout.getChildren().addAll(artistListView, confirmButton);
            Scene scene = new Scene(layout);
            artistStage.setScene(scene);
            artistStage.setWidth(400);
            artistStage.setHeight(400);

            artistStage.show();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error", ex);
            showAlert("Error de selección", "Ha ocurrido un error al seleccionar los artistas");
        }
    }

    private void printItems(ActionEvent event) {
        try {
            //Se abrirá una ventana donde se puedan imprimir los datos del informe de la tabla.
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/eus/tartanga/crud/userInterface/report/concertReport.jrxml"));
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<Concert>) this.concertTable.getItems());
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (JRException ex) {
            logger.severe("Error printing report: {0}" + ex.getMessage());

        }

    }

    private void handleRightClickTable(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            // Ocultar el otro ContextMenu si está visible
            if (contextMenuOutside.isShowing()) {
                contextMenuOutside.hide();
            }
            // Mostrar el ContextMenu dentro de la tabla
            contextMenuInside.show(concertTable, event.getScreenX(), event.getScreenY());
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
            contextMenuOutside.show(concertAnchorPane, event.getScreenX(), event.getScreenY());
        } else {
            contextMenuOutside.hide();
        }
    }

    private void filterConcerts() {
        // Mientras el usuario está escribiendo ese valor se usará para filtrar de la tabla
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter();
        });

        // Filtra los elementos de la tabla en base de si hay stock o no.
        cbxComingSoon.setOnAction(event -> {
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
        String searchText = tfSearch.getText();
        boolean comingSoon = cbxComingSoon.isSelected();
        LocalDate fromDate = dpFrom.getValue();
        LocalDate toDate = dpTo.getValue();

        // Aplicar el filtrado de productos
        FilteredList<Concert> filteredConcert = getConcertsBySearchField(searchText, comingSoon, fromDate, toDate);

        // Actualizar la tabla con los productos filtrados
        SortedList<Concert> sortedData = new SortedList<>(filteredConcert);
        sortedData.comparatorProperty().bind(concertTable.comparatorProperty());
        concertTable.setItems(sortedData);
    }

    private FilteredList<Concert> getConcertsBySearchField(String searchText, boolean comingSoon, LocalDate fromDate, LocalDate toDate) {
        // Crea un FilteredList con la lista de conciertos original
        FilteredList<Concert> filteredData = new FilteredList<>(concertList, p -> true);

        // Aplica los filtros combinados (búsqueda, conmingSoon y fecha)
        filteredData.setPredicate(concert -> {
            // Filtra por el texto de búsqueda
            boolean matchesSearch = true;
            if (searchText != null && !searchText.isEmpty()) {
                String lowerCaseFilter = searchText.toLowerCase();

                // Verifica si el nombre, ciudad o ubicación coinciden
                matchesSearch = concert.getConcertName().toLowerCase().contains(lowerCaseFilter)
                        || concert.getCity().toLowerCase().contains(lowerCaseFilter)
                        || concert.getLocation().toLowerCase().contains(lowerCaseFilter)
                        || concert.getArtistList().stream()
                                .anyMatch(artist -> artist.getName().toLowerCase().contains(lowerCaseFilter));
            }

            // Filtra por conciertos que aún no han ocurrido si comingSoon está marcado
            boolean matchesComingSoon = true;
            if (comingSoon) {
                LocalDate today = LocalDate.now();
                matchesComingSoon = concert.getConcertDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .isAfter(today);
            }

            // Filtra por fechas (si las fechas están definidas)
            boolean matchesDate = true;
            if (fromDate != null && toDate != null) {
                Date concertDate = concert.getConcertDate();
                java.sql.Date fromSQLDate = java.sql.Date.valueOf(fromDate);
                java.sql.Date toSQLDate = java.sql.Date.valueOf(toDate);

                matchesDate = !concertDate.before(fromSQLDate) && !concertDate.after(toSQLDate);
            }

            // Devuelve true solo si el concierto cumple con todos los filtros
            return matchesSearch && matchesComingSoon && matchesDate;
        });

        return filteredData;
    }

    private void configureRowStyling() {
        concertTable.setRowFactory(tv -> {
            TableRow<Concert> row = new TableRow<>();

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

    private void showAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private FanetixClient getFanetixClient(String email) {
        try {
            client = clientManager.findClient_XML(new GenericType<FanetixClient>() {
            }, email);
        } catch (ReadException ex) {
            Logger.getLogger(ProductViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return client;
    }
}
