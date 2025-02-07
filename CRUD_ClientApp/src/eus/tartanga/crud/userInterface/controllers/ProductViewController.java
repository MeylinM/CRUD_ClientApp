package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.DeleteException;
import eus.tartanga.crud.exception.MaxCharacterException;
import eus.tartanga.crud.exception.NoStockException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.exception.TextEmptyException;
import eus.tartanga.crud.exception.UpdateException;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import javax.ws.rs.core.GenericType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Controlador para la vista de productos en la aplicación Fanetix. Gestiona la
 * visualización, edición y manipulación de productos en la tabla. Dependiendo
 * del rol del usuario, permite la adición, eliminación y modificación de
 * productos.
 *
 * @author Elbire
 */
public class ProductViewController {

    /**
     * Tabla para mostrar los productos disponibles.
     */
    @FXML
    private TableView<Product> productTable;

    /**
     * Columna para mostrar los títulos de los productos.
     */
    @FXML
    private TableColumn<Product, String> titleColumn;

    /**
     * Columna para mostrar los artistas asociados a los productos.
     */
    @FXML
    private TableColumn<Product, Artist> artistColumn;

    /**
     * Columna para mostrar las descripciones de los productos.
     */
    @FXML
    private TableColumn<Product, String> descriptionColumn;

    /**
     * Columna para mostrar las imágenes de los productos.
     */
    @FXML
    private TableColumn<Product, byte[]> imageColumn;

    /**
     * Columna para mostrar la fecha de lanzamiento de los productos.
     */
    @FXML
    private TableColumn<Product, Date> releaseDateColumn;

    /**
     * Columna para mostrar el stock de los productos.
     */
    @FXML
    private TableColumn<Product, String> stockColumn;

    /**
     * Columna para mostrar el precio de los productos.
     */
    @FXML
    private TableColumn<Product, String> priceColumn;

    /**
     * Panel principal de la vista de productos.
     */
    @FXML
    private AnchorPane productAnchorPane;

    /**
     * Campo de texto para la búsqueda de productos.
     */
    @FXML
    private TextField tfSearch;

    /**
     * Casilla de verificación para filtrar productos con stock disponible.
     */
    @FXML
    private CheckBox cbxStock;

    /**
     * Botón para añadir un nuevo producto.
     */
    @FXML
    private Button btnAddProduct;

    /**
     * Botón para añadir un producto al carrito de compras.
     */
    @FXML
    private Button btnAddToCart;

    /**
     * Botón para eliminar un producto seleccionado.
     */
    @FXML
    private Button btnDeleteProduct;

    /**
     * Botón para ver más información sobre un producto.
     */
    @FXML
    private Button btnInfo;

    /**
     * Selector de fecha para filtrar productos desde una fecha específica.
     */
    @FXML
    private DatePicker dpFrom;

    /**
     * Selector de fecha para filtrar productos hasta una fecha específica.
     */
    @FXML
    private DatePicker dpTo;
    /**
     * Ventana principal de la vista de productos.
     */
    private Stage stage;
    /**
     * Logger para registrar eventos y errores en el controlador.
     */
    private Logger logger = Logger.getLogger(ProductViewController.class.getName());

    /**
     * Menú contextual para acciones dentro de la tabla de productos.
     */
    private ContextMenu contextMenuInside;

    /**
     * Menú contextual para acciones fuera de la tabla de productos.
     */
    private ContextMenu contextMenuOutside;

    /**
     * Gestor de productos para manejar las operaciones de negocio relacionadas
     * con productos.
     */
    private ProductManager productManager;

    /**
     * Lista observable de productos para la tabla de visualización.
     */
    private ObservableList<Product> productList = FXCollections.observableArrayList();

    /**
     * Gestor de artistas para manejar la información de artistas en los
     * productos.
     */
    private ArtistManager artistManager;

    /**
     * Lista observable de artistas disponibles.
     */
    private ObservableList<Artist> artistList = FXCollections.observableArrayList();

    /**
     * Gestor de carritos de compra.
     */
    private CartManager cartManager;

    /**
     * Gestor de clientes de Fanetix.
     */
    private FanetixClientManager clientManager;

    /**
     * Cliente actual logueado en el sistema.
     */
    private FanetixClient client;

    /**
     * Establece la ventana principal de la vista de productos.
     *
     * @param stage La ventana a establecer.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Inicializa la ventana de la vista de productos.
     *
     * @param root El nodo raíz de la escena.
     */
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

                //Formatear el precio de manera que se vea con el €
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
                //Formatear la fecha
                releaseDateColumn.setCellFactory(column -> new TableCell<Product, Date>() {
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
                btnAddToCart.setOnAction(this::handleAddToCart);
                //Ocultar los botones de añadir producto y borrar producto al cliente
                btnAddProduct.setVisible(false);
                btnDeleteProduct.setVisible(false);
            } else {
                //En caso de ser un administrador mostrar de manera diferente
                //Selección múltiple en tabla para el uso del botón delete.
                productTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                //Hacer la tabla editable
                productTable.setEditable(true);

                //Para obtener la lista de artistas se usará el método de lógica findAllArtist
                artistList.addAll(findAllArtist());

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

                //Establecer la factoria de celda de tittle
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
                        showAlertWarning("Error de  validacíon de titulo", e.getMessage());
                        productTable.refresh();
                    } catch (Exception e) {
                        Logger.getLogger(ProductViewController.class.getName()).log(Level.SEVERE, null, e);
                    }
                });
                //Establecer la factoria de celda de description
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
                        showAlertWarning("Error de validacion de descripcion", e.getMessage());
                        productTable.refresh();
                    } catch (Exception e) {
                        //Logger.getLogger(ProductViewController.class.getName()).log(Level.SEVERE, null, e);
                    }
                });
                //Establecer la factoria de celda de stock
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
                        showAlertWarning("Error de validacíon de stock", "El valor del stock debe ser un número válido y mayor o igual a 0");
                        productTable.refresh();
                    } catch (Exception e) {
                        Logger.getLogger(ProductViewController.class.getName()).log(Level.SEVERE, null, e);
                    }
                });
                //Establecer la factoria de celda de price
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
                        showAlertWarning("Error de validacíon de precio", "El valor del precio debe ser un número válido y mayor a 0");
                        productTable.refresh();
                    } catch (Exception e) {
                        Logger.getLogger(ProductViewController.class.getName()).log(Level.SEVERE, null, e);
                    }
                });
                //Establecer la factoria de celda del producto que es una combobox
                artistColumn.setCellFactory(ComboBoxTableCell.forTableColumn(artistList));
                artistColumn.setOnEditCommit(event -> {
                    try {
                        Product product = event.getRowValue();
                        Product productCopy = product.clone();
                        productCopy.setArtist(event.getNewValue());
                        updateProduct(productCopy);
                        product.setArtist(event.getNewValue());
                    } catch (Exception e) {
                        Logger.getLogger(ProductViewController.class.getName()).log(Level.SEVERE, null, e);
                        showAlertWarning("Error al actualizar artista", "No se pudo actualizar el artista en la base de datos.");
                        productTable.refresh();
                    }
                });

                btnAddProduct.setOnAction(this::handleAddProduct);
                btnDeleteProduct.setOnAction(this::handleDeleteProduct);
                //Ocultar el botón de añadir producto al carrito para el administrador
                btnAddToCart.setVisible(false);
            }
            //Establecer la factoria de celda para la imagen
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
                        //En caso de que el producto no tenga una imagen como tal colocarle una imagen predeterminada
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
            showAlert("Error en la ventana", "Ha ocurrido un error al inicializar la ventana");
        }
    }

    /**
     * Obtiene la lista de todos los productos disponibles.
     *
     * @return Lista de productos o null en caso de error. ReadException Si
     * ocurre un error al conseguir la lista de productos.
     */
    private List<Product> findAllProducts() {
        List<Product> products = null;
        try {
            products = productManager.findAll_XML(new GenericType<List<Product>>() {
            });
            return products;
        } catch (ReadException e) {
            logger.severe("Error al buscar productos: " + e.getMessage());
            showAlert("Error del servidor", "An error occurred while getting the product list");
        }
        return products;
    }

    /**
     * Obtiene la lista de todos los artistas disponibles.
     *
     * @return Lista de artistas o null en caso de error. ReadException Si
     * ocurre un error al conseguir la lista de artistas.
     */
    private List<Artist> findAllArtist() {
        List<Artist> artists = null;
        try {
            artists = artistManager.findAllArtist(new GenericType<List<Artist>>() {
            });
        } catch (ReadException e) {
            logger.severe("Error al buscar la lista de artistas disponible: " + e.getMessage());
            showAlert("Error del servidor", "An error occurred while getting the artist list");
        }
        return artists;
    }

    /**
     * Agrega un nuevo producto con valores por defecto y lo muestra en la
     * tabla.
     *
     * @param event Evento de acción al hacer clic en "Add Product".
     * AddException Si ocurre un error al añadir el producto.
     */
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
        } catch (AddException e) {
            showAlert("Error del servidor", "An error occurred while creating the product(s)");
        }
    }

    /**
     * Agrega el producto seleccionado al carrito si hay stock disponible.
     *
     * @param event Evento de acción al hacer clic en "Add to cart".
     * AddException Si ocurre un error al añadir el producto al carrito.
     * NoStockException Si el producto no tiene stock.
     */
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
                        cart.setOrderDate(new Date());
                        cart.setQuantity(quantityToAdd);
                        cart.setBought(false);
                        cartManager.addToCart(cart);
                    }
                } else {
                    throw new NoStockException("No queda stock de ese producto, no podrá ser añadido a su carrito");
                }
            } else {
                showAlertWarning("Error de seleccion", "No se ha seleccionado ningún producto para eliminar.");
            }
        } catch (AddException e) {
            showAlert("Error de servidor", "An error occurred while creating the product(s)");
        } catch (NoStockException e) {
            showAlertWarning("Stock no disponible", e.getMessage());
        }
    }

    /**
     * Maneja la eliminación de productos seleccionados en la tabla. Muestra una
     * alerta de confirmación antes de proceder con la eliminación.
     *
     * @param event El evento que activa la eliminación. DeleteException Si
     * ocurre un error al eliminar los productos.
     */
    private void handleDeleteProduct(ActionEvent event) {
        ObservableList<Product> selectedProducts = productTable.getSelectionModel().getSelectedItems();
        List<Cart> carts = null;
        if (selectedProducts.isEmpty()) {
            showAlertWarning("Error de seleccion", "Please select at least one product to delete");
            return;
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Do you want to delete the selected product(s)?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                carts = cartManager.findAllCartProducts_XML(new GenericType<List<Cart>>() {
                });
                // Elimina cada producto seleccionado
                for (Product product : new ArrayList<>(selectedProducts)) {
                    System.out.println("Producto: " + product.getTitle());
                    for (Cart cartsList : carts) {
                        System.out.println("Carrito: " + cartsList.getId().getEmail() + cartsList.getId().getProductId());
                        try {
                            if (product.getProductId() == cartsList.getId().getProductId()) {
                                System.out.println("EL PRODUCTO TIENE ESTE CARRITO: " + product.getProductId() + cartsList.getId().getProductId());
                                cartManager.removeCart(cartsList.getId().getEmail(), cartsList.getId().getProductId().toString());
                            }
                        } catch (DeleteException ex) {
                            Logger.getLogger(ProductViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    productManager.remove(product.getProductId().toString());
                    productList.remove(product); // Actualizar la tabla
                }
                productTable.getSelectionModel().clearSelection();

            } catch (DeleteException e) {
                showAlert("Error de servidor", "An error occurred while deleting the product(s)");
            }
        }
    }

    /**
     * Actualiza los datos de un producto.
     *
     * @param product El producto con los datos actualizados. UpdateException Si
     * ocurre un error al actualizar el producto.
     */
    private void updateProduct(Product product) {
        try {
            productManager.edit_XML(product, product.getProductId().toString());
        } catch (UpdateException e) {
            showAlert("Error de servidor", "An error occurred while updating the product");
        }
    }

    /**
     * Refresca la lista de productos en la vista. Obtiene los productos
     * actualizados y los muestra en la tabla.
     */
    private void refreshProductList() {
        List<Product> updatedProducts = findAllProducts();
        productList.setAll(updatedProducts);
        productTable.refresh();
    }

    /**
     * Imprime un informe con los datos de la tabla de productos. Muestra una
     * ventana con el informe generado.
     *
     * @param event El evento que activa la impresión.
     */
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

    /**
     * Maneja la acción del botón de información. Carga y muestra la vista de
     * ayuda para productos, dependiendo del tipo de usuario (cliente o
     * administrador).
     *
     * @param event El evento que activa la carga de la vista de ayuda.
     */
    private void handleInfoButton(ActionEvent event) {
        try {
            logger.info("Loading help view...");
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

    /**
     * Filtra los productos de la tabla en función de la búsqueda de texto, la
     * disponibilidad de stock y las fechas seleccionadas. Actualiza la tabla en
     * tiempo real mientras el usuario interactúa con los filtros.
     */
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

    /**
     * Aplica los filtros combinados (búsqueda de texto, stock y fechas) a los
     * productos y actualiza la tabla con los resultados.
     */
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

    /**
     * Filtra los productos según el texto de búsqueda, la disponibilidad de
     * stock y las fechas seleccionadas.
     *
     * @param searchText El texto de búsqueda para filtrar los productos.
     * @param inStock Un valor booleano que indica si se deben mostrar productos
     * con stock.
     * @param fromDate La fecha de inicio para el filtro de fechas.
     * @param toDate La fecha de fin para el filtro de fechas.
     * @return Un FilteredList que contiene los productos filtrados.
     */
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
            Date productReleaseDate = product.getReleaseDate();
            if (fromDate != null && toDate != null) {
                java.sql.Date fromSQLDate = java.sql.Date.valueOf(fromDate);
                java.sql.Date toSQLDate = java.sql.Date.valueOf(toDate);
                matchesDate = !productReleaseDate.before(fromSQLDate) && !productReleaseDate.after(toSQLDate);
            } else if (fromDate != null) {
                java.sql.Date fromSQLDate = java.sql.Date.valueOf(fromDate);
                matchesDate = !productReleaseDate.before(fromSQLDate);
            } else if (toDate != null) {
                java.sql.Date toSQLDate = java.sql.Date.valueOf(toDate);
                matchesDate = !productReleaseDate.after(toSQLDate);
            }

            // Devuelve true solo si el producto cumple con todos los filtros
            return matchesSearch && matchesStock && matchesDate;
        });

        return filteredData;
    }

    /**
     * Crea los menús contextuales para la tabla y el área fuera de la tabla. El
     * menú cambia según si el usuario es un cliente o no.
     */
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

    /**
     * Maneja el clic derecho en la tabla. Muestra el menú contextual dentro de
     * la tabla si se hace clic derecho.
     *
     * @param event El evento de clic derecho que activa el menú contextual.
     */
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

    /**
     * Maneja el clic derecho fuera de la tabla. Muestra el menú contextual
     * fuera de la tabla si se hace clic derecho.
     *
     * @param event El evento de clic derecho que activa el menú contextual.
     */
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

    /**
     * Obtiene un objeto FanetixClient basado en el correo electrónico
     * proporcionado. Realiza una llamada a la API para encontrar al cliente.
     *
     * @param email El correo electrónico del cliente que se desea obtener.
     * @return El objeto FanetixClient correspondiente al correo electrónico
     * proporcionado. ReadException Si ocurre un error al leer los datos del
     * cliente.
     */
    private FanetixClient getFanetixClient(String email) {
        FanetixClient client = null;
        try {
            client = clientManager.findClient_XML(new GenericType<FanetixClient>() {
            }, email);
        } catch (ReadException ex) {
            Logger.getLogger(ProductViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return client;
    }

    /**
     * Configura el estilo de las filas en la tabla de productos, alternando el
     * color de fondo para cada fila. El color de fondo cambia dependiendo de si
     * el índice de la fila es par o impar.
     */
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

    /**
     * Muestra una alerta de tipo ERROR con el encabezado y mensaje
     * proporcionados.
     *
     * @param header El encabezado del mensaje de error.
     * @param message El contenido del mensaje de error.
     */
    private void showAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Muestra una alerta de tipo WARNING con el encabezado y mensaje
     * proporcionados.
     *
     * @param header El encabezado del mensaje de advertencia.
     * @param message El contenido del mensaje de advertencia.
     */
    private void showAlertWarning(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Muestra un diálogo para seleccionar la cantidad de un producto a añadir
     * al carrito. El valor máximo del Spinner será el stock disponible.
     *
     * @param stock La cantidad de stock disponible para el producto.
     * @return La cantidad seleccionada por el usuario, o 0 si se cancela el
     * diálogo.
     */
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
