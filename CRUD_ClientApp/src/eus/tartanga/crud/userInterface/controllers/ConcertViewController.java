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
import eus.tartanga.crud.model.Artist;
import eus.tartanga.crud.model.Concert;
import eus.tartanga.crud.userInterface.factories.ConcertDateEditingCell;
import eus.tartanga.crud.userInterface.factories.ConcertTimeEditingCell;

import java.io.ByteArrayInputStream;
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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
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
import javafx.stage.Modality;
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

    //me falta ponerlo en la vista
    @FXML
    private Button btnInfo;

    private Stage stage;
    private Logger logger = Logger.getLogger(ConcertViewController.class.getName());
    private ContextMenu contextMenuInside;
    private ContextMenu contextMenuOutside;
    private ConcertManager concertManager;
    private ObservableList<Concert> concertList = FXCollections.observableArrayList();
    private ArtistManager artistManager;
    private ObservableList<Artist> artistList = FXCollections.observableArrayList();

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

            concertManager = ConcertFactory.getConcertManager();
            artistManager = ArtistFactory.getArtistManager();

            // Hacer la tabla editable
            concertTable.setEditable(true);

            // Configurar columnas básicas
            billboardColumn.setCellValueFactory(new PropertyValueFactory<>("billboard"));
            concertColumn.setCellValueFactory(new PropertyValueFactory<>("concertName"));
            artistColumn.setCellValueFactory(new PropertyValueFactory<>("artistList"));
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("concertDate"));
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("concertTime"));

            //Obtener lista de artistas
            artistList = FXCollections.observableArrayList(artistManager.findAllArtist(new GenericType<List<Artist>>() {
            }));

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

            //Hacer las columnas editables REVISAR EL DISEÑO DEL CANVA
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
                    //HABRIA QUE DECLARAR EL ALERT ARRIBA COMO EL LOGER O QUE
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error de validación");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            });
            //artistColumn

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
                    //HABRIA QUE DECLARAR EL ALERT ARRIBA COMO EL LOGER O QUE
                    Alert alert = new Alert(Alert.AlertType.ERROR);
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
                    //HABRIA QUE DECLARAR EL ALERT ARRIBA COMO EL LOGER O QUE
                    Alert alert = new Alert(Alert.AlertType.ERROR);
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

            //ME FALTA CAMBIAR LA FACTORY PARA QUE ME DEJE METER UN CAMPO DE TEXTO Y ME LO FORMATE A 00:00
            final Callback<TableColumn<Concert, Date>, TableCell<Concert, Date>> timeCell
                    = (TableColumn<Concert, Date> param) -> new ConcertTimeEditingCell();
            timeColumn.setCellFactory(timeCell);

            //REVISAR BIEN QUE TIENE QUE HACER ESTE METODO Y QUE NECESITO
            btnAddConcert.setOnAction(this::handleAddConcert);
            //REVISAR QUE EL METODO BORRA DE LA BASE DE DATOS, QUE LA QUERY NO IBA ( SE SUPONE)
            btnDeleteConcert.setOnAction(this::handleDeleteConcert);

            //REVISAR EL DOCU DEL DISEÑO
            //Inicializar los menus contextuales
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
        Concert concert = new Concert();
        concert.setArtistList(artistList);
        try {
            concertManager.createConcert_XML(concert);
        } catch (AddException e) {
            logger.severe(e.getMessage());
        }
        concertTable.getItems().add(concert);
        refreshConcertList();
    }

    private void refreshConcertList() {
        List<Concert> updatedConcerts = findAllConcerts();
        concertList.setAll(updatedConcerts);
        concertTable.refresh();
    }

    private void handleDeleteConcert(ActionEvent event) {
        Concert selectedConcert = concertTable.getSelectionModel().getSelectedItem();

        if (selectedConcert != null) {
            try {
                //Elimina el concierto de la base de datos
                concertManager.removeConcert(selectedConcert.getConcertId().toString());
                //Elimina el producto de la lista observable
                concertList.remove(selectedConcert);
                logger.log(Level.INFO, "Concierto eliminado con exito{0}", selectedConcert.getConcertName());
            } catch (DeleteException e) {
                logger.log(Level.SEVERE, "Error al eliminar el concierto{0}", e.getMessage());
            }
        } else {
            logger.warning("No se ha seleccionado ningun concieto para eliminar");
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
        }
        return concerts;
    }

    private void updateConcert(Concert concert) {
        try {
            concertManager.updateConcert_XML(concert, concert.getConcertId().toString());
        } catch (UpdateException e) {
            logger.severe(e.getMessage());
        }
    }

    private void createContextMenu() {
        contextMenuInside = new ContextMenu();
        MenuItem addItem = new MenuItem("Add new concert");
        addItem.setOnAction(this::handleAddConcert);
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(this::handleDeleteConcert);
        MenuItem printItemInside = new MenuItem("Print");
        printItemInside.setOnAction(this::printItems);
        MenuItem selectArtistItem = new MenuItem("Select artist/s");
        selectArtistItem.setOnAction(event -> {
            Concert selectedArtist = concertTable.getSelectionModel().getSelectedItem();
            if (selectedArtist != null) {
                showArtistSelectionDialog(selectedArtist);
            }
        });
        contextMenuInside.getItems().addAll(addItem, deleteItem, printItemInside, selectArtistItem);

        // Crear menú contextual para clics fuera de la tabla
        contextMenuOutside = new ContextMenu();
        MenuItem printItemOutside = new MenuItem("Print");
        printItemOutside.setOnAction(this::printItems);
        MenuItem addItemOutside = new MenuItem("Add new concert");
        addItemOutside.setOnAction(this::handleAddConcert);
        contextMenuOutside.getItems().addAll(printItemOutside, addItemOutside);
    }

    private void showArtistSelectionDialog(Concert concert) {
        Stage artistStage = new Stage();
        artistStage.setTitle("Select Artists");
        ListView<Artist> artistListView = new ListView<>();
        ObservableList<Artist> selectedArtist = FXCollections.observableArrayList(); // Lista de elementos seleccionados manualmente

        try {
            List<Artist> artists = artistList;
            // Agregar las categorías a la lista de categorías disponibles
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
                        concertManager.updateConcert_XML(concert, String.valueOf(concert.getConcertId()));
                        artistStage.close();
                        concertTable.refresh();
                        logger.log(Level.INFO, "Selected Artist: {0}", concert.getConcertName());
                    } catch (UpdateException ex) {
                        logger.log(Level.SEVERE, "Error", ex);
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
        }
    }

    private void printItems(ActionEvent event) {
        try {
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/eus/tartanga/crud/userInterface/report/concertReport.jrxml"));
            JRBeanCollectionDataSource dataItems
                    = new JRBeanCollectionDataSource((Collection<Concert>) this.concertTable.getItems());
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint);
            jasperViewer.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(ConcertViewController.class.getName()).log(Level.SEVERE, null, ex);
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
        //PERO ESTAN MEZCLADOS EL TEMA DEL SEARCH Y EL COMING SOON, NO TENDRIAN QUE SER INDEPENDIENTES?

        //Mientras el usuario está escribiendo ese valor se usará para filtrar los conciertos de la tabla.
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<Concert> filteredConcerts = getConcertsBySearchField(newValue, cbxComingSoon.isSelected());

            // Actualiza la tabla con los conciertos filtrados
            SortedList<Concert> sortedData = new SortedList<>(filteredConcerts);
            sortedData.comparatorProperty().bind(concertTable.comparatorProperty());
            concertTable.setItems(sortedData);
        });

        // Filtra los conciertos de la tabla dependiendo de si ya han ocurrido o no.
        cbxComingSoon.setOnAction(event -> {
            FilteredList<Concert> filteredConcerts = getConcertsBySearchField(tfSearch.getText(), cbxComingSoon.isSelected());
            //Actualiza la tabla con los productos filtrados
            SortedList<Concert> sortedData = new SortedList<>(filteredConcerts);
            sortedData.comparatorProperty().bind(concertTable.comparatorProperty());
            concertTable.setItems(sortedData);
        });
    }

    private FilteredList<Concert> getConcertsBySearchField(String searchText, boolean comingSoon) {
        // Crea un FilteredList con la lista de conciertos original
        FilteredList<Concert> filteredData = new FilteredList<>(concertList, p -> true);

        // Si el texto de búsqueda no es nulo o vacío, filtra los conciertos
        if (searchText != null && !searchText.isEmpty()) {
            // Convierte el texto de búsqueda a minúsculas para hacer la comparación insensible a mayúsculas/minúsculas
            String lowerCaseFilter = searchText.toLowerCase();

            // Aplica el predicado de filtrado en función de los atributos de cada concierto
            filteredData.setPredicate(concert -> {
                boolean matchesSearch = concert.getConcertName().toLowerCase().contains(lowerCaseFilter)
                        || concert.getCity().toLowerCase().contains(lowerCaseFilter)
                        || concert.getLocation().toLowerCase().contains(lowerCaseFilter);
                //TENGO QUE RECORRER LA LISTA DE ARTISTAS Y BUSCAR POR SUS NOMBRES
                //  || concert.getArtistList().getName().toLowerCase().contains(lowerCaseFilter);

                // TENGO QUE COMPARAR CON LA FECHA ACTUAL -- Y ME IMPRIMA LOS CONCIERTOS CUYA FECHA ES MAYOR A LA ACTUAL
                // boolean matchesComingSoon = !comingSoon || concert.getConcertDate()
                // Solo se incluye el producto si coincide con ambos filtros (búsqueda y stock) PERO SERIA SI EL CHECK BOX ESTA SELECCIONADO O NO, HAY QUE FIJARSE EN ESO DIGO SHO
                return matchesSearch; // && matchesComingSoon;
            });
        } else {
            // Si no hay texto de búsqueda, filtra solo por la fecha EL MISMO ROYO, TENGO QUE COMPARAR CON LA FECHA ACTUAL
            //  filteredData.setPredicate(concert -> !comingSoon || concert.concert.getConcertDate()
        }

        return filteredData; // Devuelve el filtro aplicado
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
}
