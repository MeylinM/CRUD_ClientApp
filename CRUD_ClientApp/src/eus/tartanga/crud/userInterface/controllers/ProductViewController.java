/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.DeleteException;
import eus.tartanga.crud.exception.MaxCharacterException;
import eus.tartanga.crud.exception.NoStockException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.exception.TextEmptyException;
import eus.tartanga.crud.exception.UpdateException;
import eus.tartanga.crud.exception.WrongStockFormatException;
import eus.tartanga.crud.logic.ArtistFactory;
import eus.tartanga.crud.logic.ArtistManager;
import eus.tartanga.crud.logic.CartFactory;
import eus.tartanga.crud.logic.CartManager;
import eus.tartanga.crud.logic.FanetixClientFactory;
import eus.tartanga.crud.logic.FanetixClientManager;
import eus.tartanga.crud.logic.ProductFactory;
import eus.tartanga.crud.logic.ProductManager;
import eus.tartanga.crud.model.Artist;
import eus.tartanga.crud.model.Cart;
import eus.tartanga.crud.model.CartId;
import eus.tartanga.crud.model.FanetixClient;
import eus.tartanga.crud.model.FanetixUser;
import eus.tartanga.crud.model.Product;
import eus.tartanga.crud.userInterface.factories.ProductDateEditingCell;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
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
 * @author Elbire
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
    private TableColumn<Product, String> stockColumn;

    @FXML
    private TableColumn<Product, String> priceColumn;

    @FXML
    private AnchorPane productAnchorPane;

    @FXML
    private TextField tfSearch;

    @FXML
    private CheckBox cbxStock;

    @FXML
    private Button btnAddProduct;

    @FXML
    private Button btnAddToCart;

    @FXML
    private Button btnDeleteProduct;

    @FXML
    private Button btnInfo;

    @FXML
    private DatePicker dpFrom;

    @FXML
    private DatePicker dpTo;

    private Stage stage;
    private Logger logger = Logger.getLogger(ProductViewController.class.getName());
    private ContextMenu contextMenuInside;
    private ContextMenu contextMenuOutside;
    private ProductManager productManager;
    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private ArtistManager artistManager;
    private ObservableList<Artist> artistList = FXCollections.observableArrayList();
    private CartManager cartManager;
    private FanetixClientManager clientManager;
    FanetixClient client;

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
            //Añadir a la ventana el ícono “FanetixLogo.png”.
            stage.getIcons().add(new Image("eus/tartanga/crud/app/resources/logo.png"));
            stage.setResizable(false);
            stage.show();

            //Inicializar las diferentes factorias que vamos a usar
            productManager = ProductFactory.getProductManager();
            artistManager = ArtistFactory.getArtistManager();
            cartManager = CartFactory.getCartManager();
            clientManager = FanetixClientFactory.getFanetixClientManager();

            //Selección múltiple en tabla.
            productTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            //Conseguir la informacíon del usuario loggeado
            FanetixUser user = MenuBarViewController.getLoggedUser();
            client = getFanetixClient(user.getEmail());

            //Configurar las columnas básicas
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
            releaseDateColumn.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
            stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

            //Mostrar la ventana de manera diferente en caso de ser un Cliente
            if (client != null) {

                priceColumn.setCellFactory(column -> new TableCell<Product, String>() {
                    @Override
                    protected void updateItem(String price, boolean empty) {
                        super.updateItem(price, empty);
                        if (empty || price == null) {
                            setText(null);
                        } else {
                            setText(price + " €");
                        }
                    }
                });
                btnAddToCart.setOnAction(this::handleAddToCart);
                btnAddProduct.setVisible(false);
                btnDeleteProduct.setVisible(false);
            } else {
                //En caso de ser un administrador mostrar de manera diferente

                //Hacer la tabla editable
                productTable.setEditable(true);

                //Para obtener la lista de artistas se usará el método de lógica findAllArtist
                artistList = FXCollections.observableArrayList(artistManager.findAllArtist(new GenericType<List<Artist>>() {
                }));

                //Crear la factoria de celda para releaseDate usando un DatePicker
                final Callback<TableColumn<Product, Date>, TableCell<Product, Date>> dateCell
                        = (TableColumn<Product, Date> param) -> new ProductDateEditingCell();
                releaseDateColumn.setCellFactory(dateCell);
                releaseDateColumn.setOnEditCommit(event -> {
                    Product product = event.getRowValue();
                    //Establecer la propiedad releaseDate del objeto Product correspondiente a la la editada.
                    product.setReleaseDate(event.getNewValue());
                    //Llamar al método de lógica updateProduct pasándole el objeto Product.
                    updateProduct(product);
                });

                // Hacer las columnas editables
                titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                titleColumn.setOnEditCommit(event -> {
                    try {
                        Product product = event.getRowValue();
                        Product productCopy = product.clone();
                        productCopy.setTitle(event.getNewValue());
                        //Validar primero que no esté vacío el valor
                        if (productCopy.getTitle() == null || productCopy.getTitle().isEmpty()) {
                            throw new TextEmptyException("El campo de Titulo no puede estar vacío");
                        }//Validar que no supere los 50 caracteres 
                        else if (productCopy.getTitle().length() > 50) {
                            throw new MaxCharacterException("El título no puede tener más de 50 caracteres");
                        }
                        //Llamar al método de lógica updateProduct pasándole el objeto Product.
                        updateProduct(productCopy);
                        //Establecer la propiedad tittle del objeto Product correspondiente a la la editada.
                        product.setTitle(event.getNewValue());
                    } catch (TextEmptyException | MaxCharacterException e) {
                        Logger.getLogger(ProductViewController.class.getName()).log(Level.SEVERE, null, e);
                        showAlertWarning("Error de  validacíon de titulo",e.getMessage());
                        productTable.refresh();
                    } catch (Exception e) {
                        Logger.getLogger(ProductViewController.class.getName()).log(Level.SEVERE, null, e);
                    }
                });

                descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                descriptionColumn.setOnEditCommit(event -> {
                    try {
                        Product product = event.getRowValue();
                        Product productCopy = product.clone();
                        productCopy.setDescription(event.getNewValue());
                        System.out.println(productCopy.getDescription());
                        //Validar primero que no esté vacío el valor
                        if (productCopy.getDescription() == null || productCopy.getDescription().isEmpty()) {
                            throw new TextEmptyException("El campo de description no puede estar vacío");
                        }//Validar que el valor escrito no supere los 250 caracteres 
                        else if (productCopy.getDescription().length() > 250) {
                            throw new MaxCharacterException("La descripcion no puede tener más de 250 caracteres");
                        }
                        //Establecer la propiedad description del objeto Product correspondiente a la la editada.
                        updateProduct(productCopy);
                        //Establecer la propiedad description del objeto Product correspondiente a la la editada.
                        product.setDescription(event.getNewValue());
                    } catch (TextEmptyException | MaxCharacterException e) {
                        Logger.getLogger(ProductViewController.class.getName()).log(Level.SEVERE, null, e);
                        showAlertWarning("Error de validacion de descripcion",e.getMessage());
                        productTable.refresh();
                    } catch (Exception e) {
                        //Logger.getLogger(ProductViewController.class.getName()).log(Level.SEVERE, null, e);
                    }
                });

                stockColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                stockColumn.setOnEditCommit(event -> {
                    try {
                        Product product = event.getRowValue();
                        Product productCopy = product.clone();
                        //Validar primero el valor escrito para que solo pueda ser numérico y mayor o igual a 0
                        if (Integer.parseInt(event.getNewValue()) >= 0) {
                            productCopy.setStock(event.getNewValue());
                        } else {
                            throw new NumberFormatException();
                        }
                        updateProduct(productCopy);
                        product.setStock(event.getNewValue());
                    } catch (NumberFormatException ex) {
                        showAlertWarning("Error de validacíon de stock","El valor del stock debe ser un número válido y mayor o igual a 0");
                        productTable.refresh();
                    } catch (Exception e) {
                        Logger.getLogger(ProductViewController.class.getName()).log(Level.SEVERE, null, e);
                    }
                });

                priceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                priceColumn.setOnEditCommit(event -> {
                    try {
                        Product product = event.getRowValue();
                        Product productCopy = product.clone();
                        //Validar que solo pueda ser un valor numérico que acepte decimales y mayor que 0.
                        if (Float.parseFloat(event.getNewValue()) > 0 && event.getNewValue().matches("^[1-9][0-9]*(\\.[0-9]{1,2})?$")) {
                            productCopy.setPrice(event.getNewValue());
                        } else {
                            throw new NumberFormatException();
                        }
                        //Llamar al método de lógica updateProduct pasándole el objeto Product.
                        updateProduct(productCopy);
                        //Establecer la propiedad price del objeto Product correspondiente a la la editada.
                        product.setPrice(event.getNewValue());
                    } catch (NumberFormatException ex) {
                        showAlertWarning("Error de validacíon de precio","El valor del precio debe ser un número válido y mayor a 0");
                        productTable.refresh();
                    } catch (Exception e) {
                        Logger.getLogger(ProductViewController.class.getName()).log(Level.SEVERE, null, e);
                    }
                });

                artistColumn.setCellFactory(ComboBoxTableCell.forTableColumn(artistList));
                artistColumn.setOnEditCommit((TableColumn.CellEditEvent<Product, Artist> t) -> {
                    ((Product) t.getTableView().getItems()
                            .get(t.getTablePosition().getRow()))
                            .setArtist(t.getNewValue());
                    Product product = (Product) t.getTableView().getItems().get(t.getTablePosition().getRow());
                    Artist originalValueLevel = product.getArtist();
                });

                btnAddProduct.setOnAction(this::handleAddProduct);

                btnDeleteProduct.setOnAction(this::handleDeleteProduct);
                btnAddToCart.setVisible(false);
            }

            imageColumn.setCellFactory(column -> new TableCell<Product, byte[]>() {
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
            //Se muestra una ventana modal con una pequeña guia de uso para la ventana.
            btnInfo.setOnAction(this::handleInfoButton);
            //Inicializar los menu contextuales
            createContextMenu();
            // Mostrará el Menú contextual con las opciones mIAddToCart, 
            //mIDeleteProduct, mIAddToProduct y mIPrint en el caso de hacer
            //click derecho en la tabla
            productTable.setOnMousePressed(this::handleRightClickTable);
            /* Mostrará el Menú contextual con las opciones mIAddToProduct y mIPrint 
            en el caso de hacer click derecho fuera de la tabla*/
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
        } catch (WebApplicationException e) {
            showAlert("Error del servidor","An error occurred while getting the product list");
        }
        return products;
    }

    private void handleAddProduct(ActionEvent event) {
        try {
            /*Se creará una nuevo objeto con un constructor por defecto del tipo Producto. El campo image
            se fijará en una imagen por defecto, tittle se fijará como “Tittle of the product”,
            description se jará como “Description of the product”, la fecha releaseDate tomará la fecha
            del día de creación, stock tomará el valor de base 1, price tomará el valor de base 0,00.*/
            Product product = new Product();
            //artist se fijará en el primer artista obtenido por defecto,
            product.setArtist(artistList.get(0));
            //Se usará el método de lógica createProduct, pasando como parámetro un objeto Product.
            productManager.create_XML(product);
            //Si la operación se lleva a cabo sin errores, la fila recién creada se mostrará en la tabla.
            productTable.getItems().add(product);
            refreshProductList();
        } catch (WebApplicationException e) {
            showAlert("Error del servidor","An error occurred while creating the product(s)");
        }
    }

    private void handleAddToCart(ActionEvent event) {
        try {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                if (Integer.parseInt(selectedProduct.getStock()) > 0) {
                    int quantityToAdd = showQuantityDialog(Integer.parseInt(selectedProduct.getStock()));

                    if (quantityToAdd > 0) {
                        Cart cart = new Cart();
                        CartId cartId = new CartId();
                        cartId.setEmail(client.getEmail());
                        cartId.setProductId(selectedProduct.getProductId());
                        cart.setId(cartId);
                        cart.setProduct(selectedProduct);
                        //System.out.println(client.getEmail());
                        //cart.setClient(client);
                        cart.setOrderDate(new Date());
                        cart.setQuantity(quantityToAdd);
                        cart.setBought(false);
                        cartManager.addToCart(cart);
                    }
                } else {
                    throw new NoStockException("No queda stock de ese producto, no podrá ser añadido a su carrito");
                }
            } else {
                showAlertWarning("Error de seleccion","No se ha seleccionado ningún producto para eliminar.");
            }
        } catch (AddException e) {
            showAlert("Error de servidor","An error occurred while creating the product(s)");
        } catch (NoStockException e) {
            showAlertWarning("Stock no disponible",e.getMessage());
        }
    }

    private void handleDeleteProduct(ActionEvent event) {
        ObservableList<Product> selectedProducts = productTable.getSelectionModel().getSelectedItems();

        if (selectedProducts.isEmpty()) {
            showAlertWarning("Error de seleccion","Please select at least one product to delete");
            return;
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Do you want to delete the selected product(s)?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Elimina cada producto seleccionado
                for (Product product : new ArrayList<>(selectedProducts)) {
                    productManager.remove(product.getProductId().toString());
                    productList.remove(product); // Actualizar la tabla
                }
                productTable.getSelectionModel().clearSelection();

            } catch (WebApplicationException e) {
                showAlert("Error de servidor","An error occurred while deleting the product(s)");
            }
        }
    }

    private void updateProduct(Product product) {
        try {
            productManager.edit_XML(product, product.getProductId().toString());
        } catch (WebApplicationException e) {
            showAlert("Error de servidor","An error occurred while updating the product");
        }
    }

    private void refreshProductList() {
        List<Product> updatedProducts = findAllProducts();
        productList.setAll(updatedProducts);
        productTable.refresh();
    }

    private void printItems(ActionEvent event) {
        try {
            //Se abrirá una ventana donde se puedan imprimir los datos del informe de la tabla de Productos.
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/eus/tartanga/crud/userInterface/report/productReport.jrxml"));
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<Product>) this.productTable.getItems());
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (JRException ex) {
            showAlert("Error al imprimir", "Ha ocurrido un error al imprimir la tabla de Productos");
            logger.severe("UI GestionUsuariosController: Error printing report: {0}"
                    + ex.getMessage());

        }
    }

    private void handleInfoButton(ActionEvent event) {
        try {
            //LOGGER.info("Loading help view...");
            //Load node graph from fxml file
            FXMLLoader loader
                    = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/HelpProductView.fxml"));
            Parent root = (Parent) loader.load();
            HelpProductController helpController
                    = ((HelpProductController) loader.getController());
            //Initializes and shows help stage
            if (client != null) {
                helpController.initAndShowStageClient(root);
            } else {
                helpController.initAndShowStageAdmin(root);
            }
        } catch (Exception ex) {
            //If there is an error show message and
            //log it.
            System.out.println(ex.getMessage());

        }
    }

    private void filterProducts() {
        // Mientras el usuario está escribiendo ese valor se usará para filtrar los productos de la tabla
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            // Se filtra aplicando todos los filtros (búsqueda, stock, fechas)
            applyFilter();
        });

        // Filtra los elementos de la tabla en base de si hay stock o no.
        cbxStock.setOnAction(event -> {
            applyFilter();
        });

        //Filtra los productos para que aparezcan a partir de la fecha seleccionada
        dpFrom.valueProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter();
        });
        //Filtra los productos para que aparezcan los productos de antes de la fecha seleccionada
        dpTo.valueProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter();
        });
    }

    private void applyFilter() {
        // Obtener el texto de búsqueda y las fechas seleccionadas
        String searchText = tfSearch.getText();
        boolean inStock = cbxStock.isSelected();
        LocalDate fromDate = dpFrom.getValue();
        LocalDate toDate = dpTo.getValue();

        // Aplicar el filtrado de productos
        FilteredList<Product> filteredProducts = getProductsBySearchField(searchText, inStock, fromDate, toDate);

        // Actualizar la tabla con los productos filtrados
        SortedList<Product> sortedData = new SortedList<>(filteredProducts);
        sortedData.comparatorProperty().bind(productTable.comparatorProperty());
        productTable.setItems(sortedData);
    }

    public FilteredList<Product> getProductsBySearchField(String searchText, boolean inStock, LocalDate fromDate, LocalDate toDate) {
        // Crea un FilteredList con la lista de productos original
        FilteredList<Product> filteredData = new FilteredList<>(productList, p -> true);

        // Aplica los filtros combinados (búsqueda, stock y fecha)
        filteredData.setPredicate(product -> {
            // Filtra por el texto de búsqueda
            boolean matchesSearch = true;
            if (searchText != null && !searchText.isEmpty()) {
                String lowerCaseFilter = searchText.toLowerCase();
                matchesSearch = product.getTitle().toLowerCase().contains(lowerCaseFilter)
                        || product.getDescription().toLowerCase().contains(lowerCaseFilter)
                        || product.getArtist().getName().toLowerCase().contains(lowerCaseFilter);
            }

            // Filtra por stock
            boolean matchesStock = !inStock || Integer.parseInt(product.getStock()) > 0;

            // Filtra por fechas (si las fechas están definidas)
            boolean matchesDate = true;
            if (fromDate != null && toDate != null) {
                Date productReleaseDate = product.getReleaseDate();  // Esto es Date
                // Convertimos LocalDate a Date para la comparación
                java.sql.Date fromSQLDate = java.sql.Date.valueOf(fromDate);
                java.sql.Date toSQLDate = java.sql.Date.valueOf(toDate);

                // Comparamos las fechas
                matchesDate = !productReleaseDate.before(fromSQLDate) && !productReleaseDate.after(toSQLDate);
            }

            // Devuelve true solo si el producto cumple con todos los filtros
            return matchesSearch && matchesStock && matchesDate;
        });

        return filteredData;
    }

    private void createContextMenu() {
        contextMenuInside = new ContextMenu();
        MenuItem printItemInside = new MenuItem("Print");
        printItemInside.setOnAction(this::printItems);
        if (client != null) {
            MenuItem addItemToCart = new MenuItem("Add to cart");
            addItemToCart.setOnAction(this::handleAddToCart);
            contextMenuInside.getItems().addAll(addItemToCart, printItemInside);
        } else {
            MenuItem addItem = new MenuItem("Add new product");
            addItem.setOnAction(this::handleAddProduct);
            MenuItem deleteItem = new MenuItem("Delete");
            deleteItem.setOnAction(this::handleDeleteProduct);
            contextMenuInside.getItems().addAll(addItem, deleteItem, printItemInside);
        }

        // Crear menú contextual para clics fuera de la tabla
        contextMenuOutside = new ContextMenu();
        MenuItem printItemOutside = new MenuItem("Print");
        printItemOutside.setOnAction(this::printItems);
        if (client == null) {
            MenuItem addItemOutside = new MenuItem("Add new product");
            addItemOutside.setOnAction(this::handleAddProduct);
            contextMenuOutside.getItems().addAll(printItemOutside, addItemOutside);
        } else {
            contextMenuOutside.getItems().addAll(printItemOutside);
        }

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

    private FanetixClient getFanetixClient(String email) {
        FanetixClient client = null;
        try {
            client = clientManager.findClient_XML(new GenericType<FanetixClient>() {
            }, email);
        } catch (WebApplicationException ex) {
            Logger.getLogger(ProductViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return client;
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

    private void showAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showAlertWarning(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private int showQuantityDialog(int stock) {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Select Quantity");
        dialog.setHeaderText("Choose the quantity to add to your cart:");

        // Crear el Spinner con valores desde 1 hasta el stock disponible
        Spinner<Integer> spinner = new Spinner<>(1, stock, 1);
        spinner.setEditable(true);

        // Botón de confirmación
        ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);

        // Agregar el Spinner al contenido del diálogo
        VBox content = new VBox(10, spinner);
        content.setAlignment(Pos.CENTER);
        dialog.getDialogPane().setContent(content);

        // Obtener el valor seleccionado cuando se presione "Confirm"
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButton) {
                return spinner.getValue();
            }
            return 0; // Si se cancela, devuelve 0
        });

        Optional<Integer> result = dialog.showAndWait();
        return result.orElse(0); // Si no se selecciona nada, devuelve 0
    }

}
