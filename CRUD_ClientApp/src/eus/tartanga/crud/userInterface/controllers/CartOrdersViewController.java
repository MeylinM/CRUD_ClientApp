package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.logic.ArtistFactory;
import eus.tartanga.crud.logic.CartFactory;
import eus.tartanga.crud.logic.CartManager;
import eus.tartanga.crud.logic.ProductFactory;
import eus.tartanga.crud.logic.ProductManager;
import eus.tartanga.crud.model.Artist;
import eus.tartanga.crud.model.Cart;
import eus.tartanga.crud.model.Product;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import java.util.Date;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableRow;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;

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
    private TableColumn<Cart, Product> tbcProduct;

    @FXML
    private TableColumn<Cart, Product> tbcArtist;

    @FXML
    private TableColumn<Cart, Product> tbcName;

    @FXML
    private TableColumn<Cart, Product> tbcDescription;

    @FXML
    private TableColumn<Cart, Date> tbcOrderDate;

    @FXML
    private TableColumn<Cart, Product> tbcPrice;

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
        //stage.getIcons().add(new Image("/resources/logo.png"));
        //Ventana no redimensionable
        stage.setResizable(false);
        /*La tabla mostrará las propiedades Image,ArtistName,Description,
         *Price obtenidos de cada producto
         */
        tbcProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        tbcArtist.setCellValueFactory(new PropertyValueFactory<>("product"));
        tbcDescription.setCellValueFactory(new PropertyValueFactory<>("product"));
        tbcPrice.setCellValueFactory(new PropertyValueFactory<>("product"));
        /*Mostrará además las propiedades OrderDate.Quantity y 
         *subToral obtenidas del carrito
         */
        tbcOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        tbcQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        /*
 


 ● Latabla mostrará las propiedades Image, Artist,
 Name, Description,price obtenidos de cada
 producto. Además de las propiedades
 OrderDate,Quantity y subTotal del carrito.
 ● Utilizar el método calculateSubtotal() de la clase
 CartManager para calcular el subtotal de cada
 producto.
 ● Tablaeditable:
 ○ Columna"Order Date": Convertir en editable
 usando un DatePicker. Validar que el formato
 sea dd/MM/yyyy y que la fecha seleccionada
 no sea anterior a la actual.
 ○ Columna"Quantity": Configurar como
 editable con un Spinner, donde el valor
 mínimo será 1 y el máximo será el stock
 disponible (Product.getStock()).
 Estas ediciones estarán habilitadas únicamente si el
 atributo bought del producto es false.
 ● Lacolumna“Order Date” deberá estar formateada
 con el patrón “dd/mm/yyyy”
 ● Lascolumnas price, subtotal y total deberán estar
 formateadas con el patrón “###.###,##”.
        
         */
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
            titleImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("eus/tartanga/crud/app/resources/MyCart.png")));
            //"btnBuy". (Botón por defecto)
            btnBuy.setDefaultButton(true);
            // Los filtros por artista y fecha estarán ocultos.
            cbxArtist.setVisible(false);
            dpFrom.setVisible(false);
            dpTo.setVisible(false);
            // El botón“btnPrint” estará oculto.
            btnPrint.setVisible(false);
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
        }
        stage.setScene(scene);
        stage.show();
        
        cartList.addAll(findAllCartProducts());
            //Cargar la tabla Products con la información de los productos
        tbCart.setItems(cartList);
    }

    private void configureView() {
        if (isCartView) {
            // Configuración para "Cart"
            /*titleLabel.setText("My Cart");
            btnClearCart.setVisible(true);
            btnBuy.setVisible(true);
            btnPrint.setVisible(false);
            artistFilterComboBox.setVisible(false);
            dateFilterFrom.setVisible(false);
            dateFilterTo.setVisible(false);
            // Mostrar solo productos no comprados*/
            loadCartData(false);
        } else {
            // Configuración para "My Orders"
            /*titleLabel.setText("My Orders");
            btnClearCart.setVisible(false);
            btnBuy.setVisible(false);
            btnPrint.setVisible(true);
            artistFilterComboBox.setVisible(true);
            dateFilterFrom.setVisible(true);
            dateFilterTo.setVisible(true);
            // Mostrar solo productos comprados*/
            loadCartData(true);
        }
    }

    private void loadCartData(boolean showBought) {
        // Lógica para cargar datos en la tabla según el atributo "bought"
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

    public static void main(String[] args) {
        launch(args);
    }

    private List<Cart> findAllCartProducts() {
        List<Cart> carts = null;
        try {
            carts = cartManager.findAllNotBoughtProducts_XML(new GenericType<List<Cart>>() {
            });
            return carts;
        } catch (ReadException e) {
            System.out.println("Problemo");
        }
        return carts;
        //findAllNotBoughtProducts_XML
    }
}
