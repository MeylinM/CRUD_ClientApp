/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.logic.ConcertFactory;
import eus.tartanga.crud.logic.ConcertManager;
import eus.tartanga.crud.model.Concert;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
import java.util.List;
import java.util.logging.Logger;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;

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
    private HBox detailsBox;

    @FXML
    private Label concertTitleLabel;

    @FXML
    private ImageView concertImageView;

    @FXML
    private Label concertDescriptionLabel;

    @FXML
    private TextField searchField;

    private Stage stage;
    private Logger logger = Logger.getLogger(ConcertViewController.class.getName());
    private ObservableList<Concert> concertList = FXCollections.observableArrayList();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root) {
        try {
            logger.info("Initializizng Concert stage");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Concert");
            stage.setResizable(false);
            stage.show();

            // Configurar columna de imágenes
            billboardColumn.setCellValueFactory(new PropertyValueFactory<>("Billboard"));
            billboardColumn.setCellFactory(column -> new TableCell<Concert, byte[]>() {
                private final ImageView imageView = new ImageView();

                {
                    imageView.setFitHeight(180);
                    imageView.setPreserveRatio(true);
                }

                @Override
                protected void updateItem(byte[] imageBytes, boolean empty) {
                    super.updateItem(imageBytes, empty);
                    if (empty || imageBytes == null) {
                        setGraphic(null);
                    } else {
                        // Convertir byte[] a Image
                        Image image = new Image(new ByteArrayInputStream(imageBytes));
                        imageView.setImage(image);
                        setGraphic(imageView);
                    }
                }
            });
            // Configurar columnas básicas
            concertColumn.setCellValueFactory(new PropertyValueFactory<>("concertName"));
            artistColumn.setCellValueFactory(new PropertyValueFactory<>("artistList"));
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("concertDate"));
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("concertTime"));

            // Formateao de la fecha
            dateColumn.setCellFactory(column -> new TableCell<Concert, Date>() {
                @Override
                protected void updateItem(Date date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || date == null) {
                        setText(null);
                    } else {
                        // Convertir la fecha
                        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        // Formatear la fecha
                        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        setText(formattedDate);
                    }
                }
            });

            // Formateao de la fecha
            dateColumn.setCellFactory(column -> new TableCell<Concert, Date>() {
                @Override
                protected void updateItem(Date date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || date == null) {
                        setText(null);
                    } else {
                        // Convertir la fecha
                        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        // Formatear la fecha
                        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        setText(formattedDate);
                    }
                }
            });
            // Formateo de la hora
            timeColumn.setCellFactory(column -> new TableCell<Concert, Date>() {
                @Override
                protected void updateItem(Date date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || date == null) {
                        setText(null);
                    } else {
                        // Si el valor es un Date y la columna es de tipo Time
                        java.sql.Time time = new java.sql.Time(date.getTime());  // Convierte Date a Time

                        // Extraer hora y minutos
                        int hours = time.getHours();
                        int minutes = time.getMinutes();

                        // Formatear la hora (por ejemplo, "HH:mm")
                        String formattedTime = String.format("%02d:%02d", hours, minutes);
                        setText(formattedTime);
                    }
                }
            });

            concertList.addAll(findAllConcerts());

            concertTable.setItems(concertList);

            FilteredList<Concert> filteredData = new FilteredList<>(concertList, p -> true);
            /**
             * // Actualiza el filtro cuando cambia el texto en el campo de
             * búsqueda searchField.textProperty().addListener((observable,
             * oldValue, newValue) -> { filteredData.setPredicate(product -> {
             * if (newValue == null || newValue.isEmpty()) { return true; // Si
             * no hay texto en el campo de búsqueda, mostrar todo } String
             * lowerCaseFilter = newValue.toLowerCase();
             *
             * // Compara si alguna de las columnas contiene el texto buscado
             * if (product.getTitle().toLowerCase().contains(lowerCaseFilter)) {
             * return true; } else if
             * (product.getDescription().toLowerCase().contains(lowerCaseFilter))
             * { return true; } return false; }); });
             *
             */

            // Ordena los datos filtrados
            SortedList<Concert> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(concertTable.comparatorProperty());
            concertTable.setItems(sortedData);

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
                            row.setStyle("-fx-background-color: #fdd3e1"); // Rosa claro
                        }
                    }
                });

                return row;
            });
        } catch (Exception e) {
            String errorMsg = "Error" + e.getMessage();
            logger.severe(errorMsg);
        }
    }

    private List<Concert> findAllConcerts() {
        ConcertManager concertManager = ConcertFactory.getConcertManager();
        List<Concert> concerts = concertManager.findAllConcerts_XML(new GenericType<List<Concert>>() {
        });

        return concerts;
    }

    /*private void createDatePickerField(){
        DatePicker datePicker = new DatePicker();
        datePicker.valueProperty.addListener(LocalDate,LocalDate,LocalDate);
    }*/
}