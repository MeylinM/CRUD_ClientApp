package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.DeleteException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.exception.UpdateException;
import eus.tartanga.crud.logic.CartFactory;
import eus.tartanga.crud.logic.CartManager;
import eus.tartanga.crud.logic.ProductFactory;
import eus.tartanga.crud.logic.ProductManager;
import eus.tartanga.crud.model.Artist;
import eus.tartanga.crud.model.Cart;
import eus.tartanga.crud.model.Product;
import eus.tartanga.crud.userInterface.factories.CartDateEditingCell;
import eus.tartanga.crud.userInterface.factories.CartSpinnerEditingCell;
import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;
import javafx.util.Callback;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

/**
 *
 * @author Meylin
 */
public class CartOrdersViewController extends Application {

    @FXML
    private ImageView titleImage;

    @FXML
    private Button btnInfo;

    @FXML
    private ChoiceBox cbxArtist;

    @FXML
    private DatePicker dpFrom;

    @FXML
    private DatePicker dpTo;

    @FXML
    private TableView<Cart> tbCart;

    @FXML
    private TableColumn<Cart, byte[]> tbcProduct;

    @FXML
    private TableColumn<Cart, String> tbcArtist;

    @FXML
    private TableColumn<Cart, String> tbcName;

    @FXML
    private TableColumn<Cart, String> tbcDescription;

    @FXML
    private TableColumn<Cart, Date> tbcOrderDate;

    @FXML
    private TableColumn<Cart, String> tbcPrice;

    @FXML
    private TableColumn<Cart, Integer> tbcQuantity;

    @FXML
    private TableColumn<Cart, Float> tbcSubTotal;

    @FXML
    private HBox footer;

    @FXML
    private Button btnBuy;

    @FXML
    private Button btnClearCart;

    @FXML
    private Button btnPrint;

    // true para "Cart", false para "My Orders"
    private boolean isCartView;
    private ContextMenu contextMenu;
    private CartManager cartManager;
    private ProductManager productManager;
    private ObservableList<Cart> cartList = FXCollections.observableArrayList();
    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private ObservableList<Artist> artistList = FXCollections.observableArrayList();
    private Stage stage;
    private Logger logger = Logger.getLogger(ArtistViewController.class.getName());

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root, boolean isCartView) {

        //Añadir a la ventana el ícono “FanetixLogo.png”.
        stage.getIcons().add(new Image("eus/tartanga/crud/app/resources/logo.png"));
        //Ventana no redimensionable
        stage.setResizable(false);
        /*La tabla mostrará las propiedades Image,ArtistName,Description,
         *Price obtenidos de cada producto
         */
        //Imagen del producto
        tbcProduct.setCellValueFactory(cellData
                -> new SimpleObjectProperty<>(cellData.getValue().getProduct().getImage())
        );

        tbcProduct.setCellFactory(column -> new TableCell<Cart, byte[]>() {
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
                    // Mostrar una imagen predeterminada si no hay datos
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

        //Nombre del Artista
        tbcArtist.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getProduct().getArtist().getName())
        );

        //Nombre del producto
        tbcName.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getProduct().getTitle()));

        //Descripcion del producto
        tbcDescription.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getProduct().getDescription())
        );
        /*Lascolumnas price, subtotal y total deberán estar formateadas 
         *con el patrón “###.###,##”.
         */

        // Precio del producto
        tbcPrice.setCellValueFactory(cellData -> {
            Float price = Float.valueOf(cellData.getValue().getProduct().getPrice());  // Obtener el precio
            String formatPrice = NumberFormat.getInstance(Locale.ROOT).format(price); // Formatear el precio
            return new SimpleStringProperty(formatPrice); // Devolver el precio formateado como SimpleStringProperty
        });

        /*Mostraremos además las propiedades OrderDate.Quantity obtenidas del carrito*/
        //FECHA DE ORDEN DEL PRODUCTO
        tbcOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        /* - Lacolumna“Order Date” deberá estar formateada con el patrón “dd/mm/yyyy”*/
        tbcOrderDate.setCellFactory(column -> new TableCell<Cart, Date>() {
            private final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            @Override
            protected void updateItem(Date date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null); // Deja la celda vacía si no hay datos
                } else {
                    setText(formatter.format(date)); // Formatea la fecha
                }
            }
        });
        tbcQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        /* -Sub total es una columna con un valor calculado con la cantidad y el precio*/
        tbcSubTotal.setCellValueFactory(cellData -> {
            Cart cart = cellData.getValue();
            float subtotal = cart.getQuantity() * Float.valueOf(cart.getProduct().getPrice());
            return new SimpleObjectProperty<>(subtotal);
        });
        this.isCartView = isCartView;
        Scene scene = new Scene(root);
        cartManager = CartFactory.getCartManager();
        productManager = ProductFactory.getProductManager();
        stage.setResizable(false);
        btnInfo.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("eus/tartanga/crud/app/resources/btnInfo.png"))));

        // Si se accede desde el botón My Cart situado en el menubar
        if (isCartView) {
            logger.info("Initializing Cart stage");
            // Establecer el título de la ventana con el valor "My Cart".
            stage.setTitle("My Cart");
            //La tabla será editable
            tbCart.setEditable(true);
            //Columna"Order Date": Convertir en editableusando un DatePicker.
            //Validar que el formato sea dd/MM/yyyy
            //COLUMNA ORDER DATE EDITABLE
            tbcOrderDate.setEditable(true);
            final Callback<TableColumn<Cart, Date>, TableCell<Cart, Date>> dateCell
                    = (TableColumn<Cart, Date> param) -> new CartDateEditingCell();
            tbcOrderDate.setCellFactory(dateCell);

            /*Validar que la fecha seleccionada no sea anterior a la actual.*/
            /*○ Columna"Quantity": 
             * Configurar como editable con un Spinner, 
             * donde el valor mínimo será 1 y el máximo será el stock 
             * disponible (Product.getStock()).
             */
            // Columna Quantity: Configurar como editable con un Spinner
            final Callback<TableColumn<Cart, Integer>, TableCell<Cart, Integer>> quantityCellFactory = (TableColumn<Cart, Integer> param) -> new CartSpinnerEditingCell();
            tbcQuantity.setCellFactory(quantityCellFactory);
            tbcQuantity.setEditable(true);
            titleImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("eus/tartanga/crud/app/resources/MyCart.png")));
            //"btnBuy". (Botón por defecto)
            btnBuy.setDefaultButton(true);
            btnBuy.setOnAction(this::handleAddProduct);
            btnClearCart.setOnAction(this::handleClearAllProducts);
            // Los filtros por artista y fecha estarán ocultos.
            cbxArtist.setVisible(false);
            dpFrom.setVisible(false);
            dpTo.setVisible(false);
            // El botón“btnPrint” estará oculto.
            btnPrint.setVisible(false);
            cartList.addAll(findAllNotBoughtCartProducts());
        } else {
            // Configuración para "My Orders"
            logger.info("Initializing Orders stage");
            //Establecer el título de la ventana con el valor "My Orders"
            stage.setTitle("My Orders");
            titleImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("eus/tartanga/crud/app/resources/MyOrders.png")));
            //Ocultar botones btnClearCart, btnBuy y el footer con el total.
            btnBuy.setVisible(false);
            btnClearCart.setVisible(false);
            footer.setVisible(false);
            cartList.addAll(findAllBoughtCartProducts());
        }
        stage.setScene(scene);
        stage.show();
        
        
        //Cargar la tabla Products con la información de los productos
        tbCart.setItems(cartList);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el archivo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/CartOrdersView.fxml"));
        Parent root = loader.load();

        // Obtener el controlador
        CartOrdersViewController controller = loader.getController();

        // Iniciar la ventana
        controller.initStage(root, true);  // Pasa 'true' si es para "Cart" o 'false' si es para "My Orders"

        // Establecer la escena y mostrar la ventana
        Scene scene = new Scene(root);
        primaryStage.setTitle("Cart Orders View");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleAddProduct(ActionEvent event) {
        try {
            //LOGICA DE CONSEGUIR EL PRODUCTO/CARRITO ESPECIFICO?
            Cart cart = new Cart();
            cart.setBought(true);
            cartManager.updateCart_XML(cart, "email?", "product1");
            /*HABRIA QUE REFRESCAR LA TABLA PARA QUE DESAPAREZCA DE LA TABLA
            refreshCartList();*/
        } catch (UpdateException e) {
            showAlert("Problem with buying products");
        }

    }
    
    private void handleClearAllProducts(ActionEvent event) {
        try {
            //LOGICA DE QUE SEA DE EL CLIENTE ESPECIFICOS
            for (Product product : productList) {
            cartManager.removeCart("email?", product.getProductId().toString());
            }
            /*HABRIA QUE REFRESCAR LA TABLA PARA QUE DESAPAREZCA DE LA TABLA?
            refreshCartList();*/
        } catch (DeleteException e) {
            showAlert("Problem with deleting products from the cart");
        }

    }

    private List<Cart> findAllNotBoughtCartProducts() {
        List<Cart> carts = null;
        try {
            carts = cartManager.findAllNotBoughtProducts_XML(new GenericType<List<Cart>>() {
            });
            return carts;
        } catch (ReadException e) {
            showAlert("Problem with reading not bought products");
        }
        return carts;
    }
    
    private List<Cart> findAllBoughtCartProducts() {
        List<Cart> carts = null;
        try {
            carts = cartManager.findAllBoughtProducts_XML(new GenericType<List<Cart>>() {});
        } catch (ReadException e) {
            showAlert("Problem with reading bought products");
        }
        return carts;
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error de servidor");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlertWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Error de validación");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
