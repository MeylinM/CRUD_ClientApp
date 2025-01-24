/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.logic.ArtistFactory;
import eus.tartanga.crud.logic.ArtistManager;
import eus.tartanga.crud.model.Artist;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.ws.rs.core.GenericType;

/**
 * FXML Controller class
 *
 * @author meyli
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
    private TextField searchField;

    private Stage stage;
    private Logger logger = Logger.getLogger(ArtistViewController.class.getName());
    private ObservableList<Artist> artistList = FXCollections.observableArrayList();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root) {
        
        try {
            logger.info("Initializing Artist stage");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            // The window title is "Sign In".
            stage.setTitle("Artist");
            // The window is not resizable.
            stage.setResizable(false);
            stage.show();
            
            // Configurar columnas de tabla
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            companyColumn.setCellValueFactory(new PropertyValueFactory<>("company"));
            lastAlbumColumn.setCellValueFactory(new PropertyValueFactory<>("lastAlbum"));
            debutColumn.setCellValueFactory(new PropertyValueFactory<>("debut"));
            artistColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
            
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
            artistColumn.setCellFactory(column -> new TableCell<Artist, byte[]>() {
                private final ImageView imageView = new ImageView();

                {
                    imageView.setFitHeight(90);
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
            // Cargar datos desde el servidor
            artistList.addAll(findAllArtists());
            artistTable.setItems(artistList);
            // Configurar filtro de búsqueda
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

            // Establecer filas con estilos alternos
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

    private List<Artist> findAllArtists() throws ReadException {
        ArtistManager artistManager = ArtistFactory.getArtistManager();
        return artistManager.findAllArtist(new GenericType<List<Artist>>() {
        });
    }

}
