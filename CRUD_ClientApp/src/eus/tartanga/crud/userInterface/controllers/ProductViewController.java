/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.logic.ArtistFactory;
import eus.tartanga.crud.logic.ArtistManager;
import eus.tartanga.crud.logic.ProductFactory;
import eus.tartanga.crud.logic.ProductManager;
import eus.tartanga.crud.model.Artist;
import eus.tartanga.crud.model.Product;
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
import java.util.stream.Collectors;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javax.ws.rs.core.GenericType;

/**
 * FXML Controller class
 *
 * @author meyli
 */
public class ProductViewController {

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> titleColumn;

    @FXML
    private TableColumn<Product, Artist> artistColumn;

    @FXML
    private TableColumn<Product, String> descriptionColumn;

    @FXML
    private TableColumn<Product, byte[]> imageColumn;

    @FXML
    private TableColumn<Product, Date> releaseDateColumn;

    @FXML
    private TableColumn<Product, Integer> stockColumn;

    @FXML
    private TableColumn<Product, Float> priceColumn;

    @FXML
    private AnchorPane productAnchorPane;

    @FXML
    private TextField searchField;

    @FXML
    private CheckBox cbxStock;

    @FXML
    private Button btnAddProduct;

    @FXML
    private Button btnDeleteProduct;

    private Stage stage;
    private Logger logger = Logger.getLogger(ProductViewController.class.getName());
    private ContextMenu contextMenuInside;
    private ContextMenu contextMenuOutside;
    ProductManager productManager;
    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private ArtistManager artistInterface;
    private ObservableList<Artist> artistList = FXCollections.observableArrayList();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    //excepcion de longitud de entrada(más de lo que cabe en la base de datos(256)). Mirar el diseño 
    //todo lo que pide, que nosek tiene k estar activado,.. Mirar test primero de casos de usos, test 
    //cuando se crea un dato en la tabla que se crea de verdad, otro cuando se borra ese dato, cuando 
    //se modificand todas y cada una de las columnas se han modificado, otro test de consulta de datos
    //read,write,update,view
    //comprobar los datos añadidos en los items de la tabla
    //Antes de borrar  mirar consistencia en el persistance
    public void initStage(Parent root) {
        try {
            logger.info("Initializing Product stage");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Shop");
            stage.setResizable(false);
            stage.show();

            productManager = ProductFactory.getProductManager();
            artistInterface = ArtistFactory.getArtistManager();

            //Hacer la tabla editable
            productTable.setEditable(true);

            //Configurar columnas básicas
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
            releaseDateColumn.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
            stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

            //Para obtener la lista de artistas se usará el método de lógica findAllArtist
            artistList = FXCollections.observableArrayList(artistInterface.findAllArtist(new GenericType<List<Artist>>() {
            }));

            // Hacer las columnas editables
            titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            titleColumn.setOnEditCommit(event -> {
                Product product = event.getRowValue();
                product.setTitle(event.getNewValue());
            });

            descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            descriptionColumn.setOnEditCommit(event -> {
                Product product = event.getRowValue();
                product.setDescription(event.getNewValue());
            });

            stockColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            stockColumn.setOnEditCommit(event -> {
                Product product = event.getRowValue();
                product.setStock(event.getNewValue());
            });

            priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
            priceColumn.setOnEditCommit(event -> {
                Product product = event.getRowValue();
                product.setPrice(event.getNewValue());
            });

            artistColumn.setCellFactory(ComboBoxTableCell.forTableColumn(artistList));
            artistColumn.setOnEditCommit((TableColumn.CellEditEvent<Product, Artist> t) -> {
                ((Product) t.getTableView().getItems()
                        .get(t.getTablePosition().getRow()))
                        .setArtist(t.getNewValue());
                Product product = (Product) t.getTableView().getItems().get(t.getTablePosition().getRow());
                Artist originalValueLevel = product.getArtist();
            });

            imageColumn.setCellFactory(column -> new TableCell<Product, byte[]>() {
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
            releaseDateColumn.setCellFactory(column -> new TableCell<Product, Date>() {
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

            priceColumn.setCellFactory(column -> new TableCell<Product, Float>() {
                @Override
                protected void updateItem(Float price, boolean empty) {
                    super.updateItem(price, empty);
                    if (empty || price == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.2f €", price)); // Formato con dos decimales
                    }
                }
            });
            btnAddProduct.setOnAction(this::handleAddProduct);
            btnDeleteProduct.setOnAction(this::handleDeleteProduct);
            //Inicializar los menu contextuales
            createContextMenu();
            // Mostrará el Menú contextual con las opciones mIAddToCart, 
            //mIDeleteProduct, mIAddToProduct y mIPrint en el caso de hacer
            //click derecho en la tabla
            productTable.setOnMousePressed(this::handleRightClickTable);
            // Mostrará el Menú contextual con las opciones mIAddToProduct y mIPrint 
            //en el caso de hacer click derecho fuera de la tabla
            productAnchorPane.setOnMouseClicked(this::handleRightClick);
            //Obtener una lista de todos los productos de mi base de datos
            productList.addAll(findAllProducts());
            //Cargar la tabla Products con la información de los productos
            productTable.setItems(productList);

            //Gestion de los diferentes filtrados con barra de busqueda, stock y por fechas
            filterProducts();
            //Configurar estilos de cada fila de la tabla
            configureRowStyling();

        } catch (Exception e) {
            String errorMsg = "Error opening window:\n" + e.getMessage();
            logger.severe(errorMsg);
        }
    }

    private List<Product> findAllProducts() {
        List<Product> products = null;
        try {
            products = productManager.findAll_XML(new GenericType<List<Product>>() {
            });
            return products;
        } catch (ReadException e) {

        }
        return products;
    }

    private void handleRightClickTable(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            // Ocultar el otro ContextMenu si está visible
            if (contextMenuOutside.isShowing()) {
                contextMenuOutside.hide();
            }
            // Mostrar el ContextMenu dentro de la tabla
            contextMenuInside.show(productTable, event.getScreenX(), event.getScreenY());
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
            contextMenuOutside.show(productAnchorPane, event.getScreenX(), event.getScreenY());
        } else {
            contextMenuOutside.hide();
        }
    }

    public FilteredList<Product> getProductsBySearchField(String searchText, boolean inStock) {
        // Crea un FilteredList con la lista de productos original
        FilteredList<Product> filteredData = new FilteredList<>(productList, p -> true);

        // Si el texto de búsqueda no es nulo o vacío, filtra los productos
        if (searchText != null && !searchText.isEmpty()) {
            // Convierte el texto de búsqueda a minúsculas para hacer la comparación insensible a mayúsculas/minúsculas
            String lowerCaseFilter = searchText.toLowerCase();

            // Aplica el predicado de filtrado en función de los atributos de cada producto
            filteredData.setPredicate(product -> {
                boolean matchesSearch = product.getTitle().toLowerCase().contains(lowerCaseFilter)
                        || product.getDescription().toLowerCase().contains(lowerCaseFilter)
                        || product.getArtist().getName().toLowerCase().contains(lowerCaseFilter);

                // Además, filtra por stock si se requiere
                boolean matchesStock = !inStock || product.getStock() > 0;

                // Solo se incluye el producto si coincide con ambos filtros (búsqueda y stock)
                return matchesSearch && matchesStock;
            });
        } else {
            // Si no hay texto de búsqueda, filtra solo por el stock
            filteredData.setPredicate(product -> !inStock || product.getStock() > 0);
        }

        return filteredData; // Devuelve el filtro aplicado
    }


    /*private void createDatePickerField(){
        DatePicker datePicker = new DatePicker();
        datePicker.valueProperty.addListener(LocalDate,LocalDate,LocalDate);
    }*/
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
    }

    private void handleAddProduct(ActionEvent event) {
        Product product = new Product();
        product.setArtist(artistList.get(0));
        try {
            productManager.create_XML(product);
        } catch (AddException e) {
            System.out.println(e);
        }
        productTable.getItems().add(product);
    }

    private void handleDeleteProduct(ActionEvent event) {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            try {
                // Elimina el producto de la base de datos
                productManager.remove(selectedProduct.getProductId().toString());

                // Elimina el producto de la lista observable
                productList.remove(selectedProduct);

                // Muestra un mensaje de confirmación
                logger.info("Producto eliminado con éxito: " + selectedProduct.getTitle());
            } catch (Exception e) {
                // Maneja errores, como problemas de conexión o restricciones de la base de datos
                logger.severe("Error al eliminar el producto: " + e.getMessage());
            }
        } else {
            // Maneja el caso en el que no se ha seleccionado ningún producto
            logger.warning("No se ha seleccionado ningún producto para eliminar.");
        }
    }

    private void printItems(ActionEvent event) {
        System.out.println("Table Items: ");
    }

    private void filterProducts() {
        //Mientras el usuario está escribiendo ese valor se usará para filtrar los productos de la tabla
        //que filtrara ese valor en base a los atributos tittle, artist y description de todos los productos.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Mientras el usuario está escribiendo ese valor se usará para filtrar los productos de la tabla
            FilteredList<Product> filteredProducts = getProductsBySearchField(newValue, cbxStock.isSelected());

            // Actualiza la tabla con los productos filtrados
            SortedList<Product> sortedData = new SortedList<>(filteredProducts);
            sortedData.comparatorProperty().bind(productTable.comparatorProperty());
            productTable.setItems(sortedData);
        });

        // Filtra los elementos de la tabla en base de si hay stock o no.
        cbxStock.setOnAction(event -> {
            FilteredList<Product> filteredProducts = getProductsBySearchField(searchField.getText(), cbxStock.isSelected());
            //Actualiza la tabla con los productos filtrados
            SortedList<Product> sortedData = new SortedList<>(filteredProducts);
            sortedData.comparatorProperty().bind(productTable.comparatorProperty());
            productTable.setItems(sortedData);
        });
    }

    private void configureRowStyling() {
        productTable.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();

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
