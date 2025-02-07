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
 * Clase controladora de FXML encargada de gestionar la interfaz de usuario
 * relacionada con los conciertos. Esta clase maneja la lógica detrás de la
 * vista donde se listan, agregan, eliminan y filtran los conciertos. Gestiona
 * la tabla de conciertos, proporciona acciones para agregar y eliminar
 * conciertos, e incluye filtros por texto de búsqueda, fechas y estado de los
 * conciertos (si son próximos).
 *
 * El controlador interactúa con los gestores `ConcertManager`, `ArtistManager`
 * y `FanetixClientManager` para la obtención y manipulación de datos. También
 * maneja la visualización de alertas para mensajes de error y menús
 * contextuales para interacciones con clic derecho en la tabla de conciertos.
 *
 * La clase está vinculada al archivo FXML `ConcertView.fxml` para el diseño y
 * las interacciones de la interfaz de usuario.
 *
 * @author Irati
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

    /**
     * Establece el escenario (ventana) asociado a este controlador.
     *
     * @param stage El escenario que se asociará a este controlador.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Inicializa la ventana de conciertos y configura todos los elementos
     * necesarios para su funcionamiento. Este método establece la escena
     * principal de la aplicación, configura las columnas de la tabla de
     * conciertos, y ajusta el comportamiento de edición y visualización de los
     * conciertos. También maneja la visibilidad de botones y la creación de
     * menús contextuales para interacción con la interfaz.
     *
     * @param root El nodo raíz de la interfaz de usuario en el formato FXML.
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     * al cargar los datos.
     */
    public void initStage(Parent root) {
        try {
            logger.info("Initializizng Concert stage");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Concerts");
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
                            throw new TextEmptyException("The concert name cannot be empty.");
                        } else if (concert.getConcertName().length() > 50) {
                            throw new MaxCharacterException("The concert name cannot be longer than 50 characters.");
                        }
                        updateConcert(concert);
                    } catch (TextEmptyException | MaxCharacterException e) {
                        logger.severe(e.getMessage());
                        concert.setConcertName(concertName);
                        concertTable.refresh();
                        alert.setTitle("Error");
                        alert.setHeaderText("Validation error");
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
                            throw new TextEmptyException("The concert location cannot be empty.");
                        } else if (concert.getLocation().length() > 50) {
                            throw new MaxCharacterException("The concert location cannot be more than 50 characters.");
                        }
                        updateConcert(concert);
                    } catch (TextEmptyException | MaxCharacterException e) {
                        logger.severe(e.getMessage());
                        concert.setLocation(concertLocation);
                        concertTable.refresh();
                        alert.setTitle("Error");
                        alert.setHeaderText("Validation error");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                    concert.setLocation(event.getNewValue());
                });

                cityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                cityColumn.setOnEditCommit(event -> {
                    Concert concert = event.getRowValue();
                    String concertCity = concert.getCity();
                    try {
                        concert.setCity(event.getNewValue());
                        if (concert.getCity() == null || concert.getCity().isEmpty()) {
                            throw new TextEmptyException("The concert city cannot be empty.");
                        } else if (concert.getCity().length() > 50) {
                            throw new MaxCharacterException("The concert city cannot have more than 50 characters.");
                        }
                        updateConcert(concert);
                    } catch (TextEmptyException | MaxCharacterException e) {
                        logger.severe(e.getMessage());
                        concert.setCity(concertCity);
                        concertTable.refresh();
                        alert.setTitle("Error");
                        alert.setHeaderText("Validation error");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                    concert.setCity(event.getNewValue());
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

    /**
     * Maneja el evento de añadir un nuevo concierto. Crea una nueva instancia
     * de `Concert`, asigna la lista de artistas al concierto y luego lo añade a
     * la base de datos a través del `concertManager`. Después de añadir el
     * concierto, se actualiza la lista de conciertos mostrados en la tabla.
     *
     * Si ocurre un error al intentar añadir el concierto, se captura una
     * `AddException` y se muestra una alerta al usuario con el mensaje de
     * error.
     *
     * @param event El evento de acción que se dispara cuando el usuario hace
     * clic en el botón para añadir un concierto.
     */
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

    /**
     * Refresca la lista de conciertos mostrados en la tabla. Obtiene la lista
     * más reciente de conciertos desde la base de datos mediante el método
     * `findAllConcerts`, actualiza la lista interna de conciertos
     * (`concertList`) y luego actualiza la vista de la tabla para reflejar los
     * cambios.
     */
    private void refreshConcertList() {
        List<Concert> updatedConcerts = findAllConcerts();
        concertList.setAll(updatedConcerts);
        concertTable.refresh();
    }

    /**
     * Maneja la acción de eliminar uno o más conciertos seleccionados en la
     * tabla. Si no se ha seleccionado ningún concierto, muestra una alerta de
     * error. Si se ha seleccionado uno o más conciertos, muestra una
     * confirmación de eliminación. Si el usuario confirma la eliminación, los
     * conciertos seleccionados son eliminados de la base de datos y de la lista
     * de la tabla.
     *
     * @param event El evento de acción que se desencadena al hacer clic en el
     * botón de eliminación.
     */
    private void handleDeleteConcert(ActionEvent event) {
        ObservableList<Concert> selectedConcerts = concertTable.getSelectionModel().getSelectedItems();

        if (selectedConcerts.isEmpty()) {
            showAlert("Selection error", "No concert has been selected.");
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
                    //Elimina el concierto de la lista observable
                    concertList.remove(concert);
                    logger.log(Level.INFO, "Concert successfully deleted {0}", concert.getConcertName());
                }
            } catch (DeleteException ex) {
                logger.log(Level.SEVERE, "Error deleting the concert {0}", ex.getMessage());
                showAlert("Server error", "Error deleting the concert");
            }
        }
    }

    /**
     * Maneja la acción del botón de información. Al hacer clic en el botón,
     * carga y muestra una ventana de ayuda con información relacionada con la
     * gestión de conciertos. La ventana de ayuda varía según si el usuario es
     * un cliente o un administrador.
     *
     * Si el usuario es un cliente, se muestra una vista de ayuda para clientes.
     * Si es un administrador, se muestra una vista de ayuda para
     * administradores.
     *
     * Si ocurre un error al cargar la vista de ayuda, muestra una alerta de
     * error.
     *
     * @param event El evento de acción que se desencadena al hacer clic en el
     * botón de información.
     */
    private void handleInfoButton(ActionEvent event) {
        try {
            FXMLLoader loader
                    = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ConcertHelpView.fxml"));
            Parent root = (Parent) loader.load();
            ConcertHelpController helpController
                    = ((ConcertHelpController) loader.getController());
            // Inicializa y muestra la ventana de ayuda
            if (client != null) {
                helpController.initAndShowStageClient(root);
            } else {
                helpController.initAndShowStageAdmin(root);
            }
        } catch (IOException ex) {
            logger.warning("Unable to load the help");
            showAlert("Help error", "Unable to load the help");
        }
    }

    /**
     * Recupera la lista de todos los conciertos desde el servidor. Realiza una
     * llamada al servicio para obtener los conciertos en formato XML y los
     * devuelve como una lista de objetos de tipo {@link Concert}.
     *
     * Si ocurre un error al recuperar los conciertos, muestra una alerta de
     * error.
     *
     * @return Una lista de conciertos. Si ocurre un error, devuelve
     * {@code null}.
     */
    private List<Concert> findAllConcerts() {
        List<Concert> concerts = null;
        try {
            concerts = concertManager.findAllConcerts_XML(new GenericType<List<Concert>>() {
            });
            return concerts;
        } catch (ReadException ex) {
            logger.severe(ex.getMessage());
            showAlert("Server error", "An error occurred while loading the concerts");
        }
        return concerts;
    }

    /**
     * Actualiza los datos de un concierto en el servidor. Realiza una llamada
     * al servicio para actualizar un concierto específico en el servidor
     * utilizando su ID como identificador.
     *
     * Si ocurre un error durante la actualización, muestra una alerta de error.
     *
     * @param concert El objeto {@link Concert} con los datos actualizados a
     * enviar al servidor.
     */
    private void updateConcert(Concert concert) {
        try {
            concertManager.updateConcert_XML(concert, concert.getConcertId().toString());
        } catch (UpdateException e) {
            logger.severe(e.getMessage());
            showAlert("Server error", "An error occurred while updating the concerts");
        }
    }

    /**
     * Crea y configura los menús contextuales para las interacciones dentro y
     * fuera de la tabla de conciertos. El menú contextual varía dependiendo de
     * si el usuario es un cliente o un administrador.
     *
     * - Si el usuario es cliente, solo se habilita la opción de imprimir. - Si
     * el usuario es administrador, se habilitan las opciones para agregar,
     * eliminar conciertos, seleccionar artistas e imprimir.
     *
     * También configura las acciones asociadas a cada opción del menú
     * contextual: - La opción "Add new concert" llama al método
     * {@link #handleAddConcert}. - La opción "Delete" llama al método
     * {@link #handleDeleteConcert}. - La opción "Select artist/s" abre un
     * diálogo de selección de artistas. - La opción "Print" llama al método
     * {@link #printItems}.
     */
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

    /**
     * Muestra un cuadro de diálogo que permite seleccionar artistas para un
     * concierto.
     *
     * Este método crea una ventana emergente donde se muestra una lista de
     * artistas disponibles. El usuario puede seleccionar o deseleccionar
     * artistas de la lista. Los artistas seleccionados se agregarán al
     * concierto y se actualizarán en la base de datos. La ventana también tiene
     * un botón de confirmación para guardar los cambios.
     *
     * @param concert El concierto al que se le agregarán los artistas
     * seleccionados.
     */
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
                            setText(item.getName()); // Mostrar el nombre del artista
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
                        logger.severe(ex.getMessage());
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
            showAlert("Selection error", "An error occurred while selecting the artists");
        }
    }

    /**
     * Imprime los datos de los conciertos actuales en la tabla en formato de
     * informe.
     *
     * Este método compila y llena un informe de JasperReports con los datos de
     * los conciertos que están actualmente en la tabla. El informe se genera a
     * partir del archivo JRXML especificado y se visualiza utilizando
     * JasperViewer.
     *
     * Si ocurre un error durante la generación o visualización del informe, se
     * registra un mensaje de error.
     *
     * @param event El evento de acción que dispara la generación del informe
     * (por ejemplo, un clic en un botón).
     */
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

    /**
     * Maneja el clic derecho en la tabla de conciertos para mostrar el menú
     * contextual correspondiente.
     *
     * Este método detecta si el usuario hace clic derecho sobre la tabla de
     * conciertos y, en función de ello, muestra el menú contextual adecuado. Si
     * el menú contextual fuera visible fuera de la tabla, lo oculta antes de
     * mostrar el menú contextual dentro de la tabla. Si se hace clic con un
     * botón distinto al derecho, el menú contextual dentro de la tabla se
     * oculta.
     *
     * @param event El evento de mouse que se dispara cuando el usuario
     * interactúa con la tabla.
     */
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

    /**
     * Maneja el clic derecho fuera de la tabla para mostrar el menú contextual
     * correspondiente.
     *
     * Este método detecta si el usuario hace clic derecho fuera de la tabla de
     * conciertos. Si es así, muestra el menú contextual fuera de la tabla, y
     * oculta el menú contextual dentro de la tabla si está visible. Si se hace
     * clic con un botón distinto al derecho, el menú contextual fuera de la
     * tabla se oculta.
     *
     * @param event El evento de mouse que se dispara cuando el usuario
     * interactúa fuera de la tabla.
     */
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

    /**
     * Configura los filtros para la tabla de conciertos en base a la entrada
     * del usuario.
     *
     * Este método establece varios oyentes de eventos para aplicar filtros a la
     * tabla de conciertos según los valores introducidos por el usuario en los
     * siguientes elementos de la interfaz:
     * <ul>
     * <li>El campo de texto para la búsqueda de conciertos (tfSearch), donde el
     * filtro se aplica mientras el usuario escribe.</li>
     * <li>El combo box (cbxComingSoon), que filtra los conciertos según si
     * están próximos a realizarse.</li>
     * <li>Los dos campos de selección de fechas (dpFrom y dpTo), que filtran
     * los conciertos según un rango de fechas seleccionado por el usuario.</li>
     * </ul>
     * Cada vez que cualquiera de estos valores cambie, se aplica el filtro para
     * actualizar los conciertos mostrados en la tabla.
     */
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

    /**
     * Aplica los filtros de búsqueda y fechas a la tabla de conciertos.
     *
     * Este método obtiene los valores introducidos por el usuario para la
     * búsqueda de conciertos (campo de texto), la selección de conciertos
     * próximos (check box), y las fechas de inicio y fin (DatePicker). Luego,
     * filtra los conciertos según estos parámetros y actualiza la tabla para
     * mostrar solo los conciertos que cumplen con los criterios establecidos.
     *
     * Los filtros aplicados son:
     * <ul>
     * <li>Texto de búsqueda (campo de texto): Filtra los conciertos según el
     * nombre o cualquier otro criterio basado en el texto.</li>
     * <li>Conciertos próximos (check box): Filtra los conciertos para mostrar
     * solo aquellos que están próximos a realizarse.</li>
     * <li>Fechas (rango de fechas): Filtra los conciertos según un rango de
     * fechas seleccionado por el usuario.</li>
     * </ul>
     *
     * Después de aplicar los filtros, se actualiza la tabla con los conciertos
     * filtrados y ordenados.
     */
    private void applyFilter() {
        // Obtener el texto de búsqueda y las fechas seleccionadas
        String searchText = tfSearch.getText();
        boolean comingSoon = cbxComingSoon.isSelected();
        LocalDate fromDate = dpFrom.getValue();
        LocalDate toDate = dpTo.getValue();

        // Aplicar el filtrado de conciertos
        FilteredList<Concert> filteredConcert = getConcertsBySearchField(searchText, comingSoon, fromDate, toDate);

        // Actualizar la tabla con los conciertos filtrados
        SortedList<Concert> sortedData = new SortedList<>(filteredConcert);
        sortedData.comparatorProperty().bind(concertTable.comparatorProperty());
        concertTable.setItems(sortedData);
    }

    /**
     * Filtra la lista de conciertos según los criterios de búsqueda, selección
     * de conciertos próximos y rango de fechas.
     *
     * Este método recibe los parámetros de búsqueda, la selección de conciertos
     * próximos y un rango de fechas, y devuelve una lista filtrada de
     * conciertos que cumplen con todos los criterios especificados.
     *
     * Los filtros aplicados son:
     * <ul>
     * <li><strong>Búsqueda por texto</strong>: Filtra conciertos cuyo nombre,
     * ciudad, ubicación o nombre de algún artista coincidan con el texto
     * introducido.</li>
     * <li><strong>Conciertos próximos</strong>: Filtra conciertos que aún no
     * han ocurrido si el parámetro 'comingSoon' está activado.</li>
     * <li><strong>Rango de fechas</strong>: Filtra conciertos que ocurran
     * dentro del rango de fechas especificado.</li>
     * </ul>
     *
     * @param searchText El texto de búsqueda para filtrar conciertos por
     * nombre, ciudad, ubicación o artistas.
     * @param comingSoon Si se deben filtrar solo los conciertos que aún no han
     * ocurrido.
     * @param fromDate La fecha de inicio del rango de fechas para filtrar los
     * conciertos.
     * @param toDate La fecha de finalización del rango de fechas para filtrar
     * los conciertos.
     * @return Un objeto {@link FilteredList} que contiene los conciertos que
     * cumplen con los filtros especificados.
     */
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

    /**
     * Configura el estilo de las filas de la tabla {@link concertTable} para
     * alternar el color de fondo de cada fila.
     *
     * Este método aplica un color de fondo alterno a las filas de la tabla. Las
     * filas en índices pares tendrán un fondo de color rosa (#ffb6c1), y las
     * filas en índices impares tendrán un fondo de color rosa claro (#fdd3e1).
     * Esta alternancia de colores facilita la lectura y mejora la experiencia
     * visual en la tabla.
     *
     * El color de fondo se aplica de manera dinámica cada vez que cambia el
     * contenido de una fila.
     */
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

    /**
     * Muestra una alerta de error con un mensaje personalizado.
     *
     * Este método crea y muestra una alerta de tipo
     * {@link Alert.AlertType#ERROR} para informar al usuario sobre un error. La
     * alerta contiene un título, un encabezado y un mensaje con el detalle del
     * error. La alerta se mostrará de forma modal, bloqueando la interacción
     * con la interfaz hasta que el usuario cierre la ventana de alerta.
     *
     * @param header El encabezado que describe el tipo de error (por ejemplo,
     * "Error de servidor").
     * @param message El mensaje que proporciona detalles sobre el error
     * ocurrido (por ejemplo, "Ha ocurrido un error al procesar la solicitud").
     */
    private void showAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Recupera un cliente de la base de datos utilizando su dirección de correo
     * electrónico.
     *
     * Este método utiliza el {@link clientManager} para realizar una búsqueda
     * del cliente asociado a un correo electrónico en la base de datos. Si la
     * búsqueda es exitosa, devuelve el objeto {@link FanetixClient}
     * correspondiente. En caso de error, se captura la excepción
     * {@link ReadException} y se registra el error en los logs.
     *
     * @param email La dirección de correo electrónico del cliente que se busca.
     * @return El objeto {@link FanetixClient} que corresponde al cliente con el
     * correo electrónico proporcionado. Si no se encuentra un cliente con el
     * correo especificado, se devuelve null.
     */
    private FanetixClient getFanetixClient(String email) {
        try {
            client = clientManager.findClient_XML(new GenericType<FanetixClient>() {
            }, email);
        } catch (ReadException ex) {
            logger.severe(ex.getMessage());
        }
        return client;
    }
}
