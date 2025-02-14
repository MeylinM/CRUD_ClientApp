package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.exception.DeleteException;
import eus.tartanga.crud.exception.ReadException;
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
import eus.tartanga.crud.model.FanetixClient;
import eus.tartanga.crud.model.FanetixUser;
import eus.tartanga.crud.model.Product;
import eus.tartanga.crud.userInterface.factories.CartDateEditingCell;
import eus.tartanga.crud.userInterface.factories.CartSpinnerEditingCell;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.SelectionMode;

/**
 * Controller class for the Cart and Orders view in the application. It manages
 * the display and interaction of the shopping cart and past orders.
 *
 * If {@code isCartView} is {@code true}, the view displays the user's cart. If
 * {@code isCartView} is {@code false}, the view displays the user's past
 * orders.
 *
 * @author Meylin
 */
public class CartOrdersViewController {

    /**
     * ImageView for displaying the title image of the view.
     */
    @FXML
    private ImageView titleImage;

    /**
     * Button to display additional information.
     */
    @FXML
    private Button btnInfo;

    /**
     * ChoiceBox for selecting an artist (used in order filtering).
     */
    @FXML
    private ChoiceBox cbxArtist;

    /**
     * DatePicker for selecting the start date in the order filter.
     */
    @FXML
    private DatePicker dpFrom;

    /**
     * DatePicker for selecting the end date in the order filter.
     */
    @FXML
    private DatePicker dpTo;

    /**
     * TableView displaying the cart or order items.
     */
    @FXML
    private TableView<Cart> tbCart;

    /**
     * TableColumn for displaying the product image.
     */
    @FXML
    private TableColumn<Cart, byte[]> tbcProduct;

    /**
     * TableColumn for displaying the artist's name.
     */
    @FXML
    private TableColumn<Cart, String> tbcArtist;

    /**
     * TableColumn for displaying the product's name.
     */
    @FXML
    private TableColumn<Cart, String> tbcName;

    /**
     * TableColumn for displaying the product's description.
     */
    @FXML
    private TableColumn<Cart, String> tbcDescription;

    /**
     * TableColumn for displaying the order date.
     */
    @FXML
    private TableColumn<Cart, Date> tbcOrderDate;

    /**
     * TableColumn for displaying the product price.
     */
    @FXML
    private TableColumn<Cart, String> tbcPrice;

    /**
     * TableColumn for displaying the product quantity.
     */
    @FXML
    private TableColumn<Cart, Integer> tbcQuantity;

    /**
     * TableColumn for displaying the subtotal (price * quantity).
     */
    @FXML
    private TableColumn<Cart, Float> tbcSubTotal;

    /**
     * Footer section containing the total price label.
     */
    @FXML
    private HBox footer;

    /**
     * Label displaying the total price of the cart.
     */
    @FXML
    private Label totalPriceLabel;

    /**
     * Button to confirm the purchase of items in the cart.
     */
    @FXML
    private Button btnBuy;

    /**
     * Button to clear all items in the cart.
     */
    @FXML
    private Button btnClearCart;

    /**
     * Button to print the order details.
     */
    @FXML
    private Button btnPrint;

    /**
     * Manages cart operations such as adding and removing products.
     */
    private CartManager cartManager;

    /**
     * Manages product-related operations.
     */
    private ProductManager productManager;

    /**
     * Manages artist-related operations.
     */
    private ArtistManager artistManager;

    /**
     * Observable list of cart items (either for the cart or past orders).
     */
    private ObservableList<Cart> cartList = FXCollections.observableArrayList();

    /**
     * Observable list of available products.
     */
    private ObservableList<Product> productList = FXCollections.observableArrayList();

    /**
     * The client (user) who is currently logged in.
     */
    private FanetixClient client;

    /**
     * Manages client-related operations.
     */
    private FanetixClientManager clientManager;

    /**
     * Reference to the current application stage.
     */
    private Stage stage;

    /**
     * Logger for logging application events and errors.
     */
    private static final Logger LOGGER = Logger.getLogger(ArtistViewController.class.getName());

    /**
     * Determines whether the view is displaying the cart or past orders.
     */
    boolean isCartView;

    /**
     * Context menu for additional options (used in cart view).
     */
    ContextMenu contextMenu;

    /**
     * Sets the stage for this controller.
     *
     * @param stage The primary stage of the application.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the stage and configures all UI elements for either the cart
     * view or the orders view.
     *
     * @param root The root element of the scene.
     * @param isCartView Boolean flag indicating if the view is for the cart
     * (true) or for the orders (false).
     */
    public void initStage(Parent root, boolean isCartView) {

        // Create the scene and set the window icon.
        Scene scene = new Scene(root);
        //Añadir a la ventana el ícono “FanetixLogo.png”.
        stage.getIcons().add(new Image("eus/tartanga/crud/app/resources/logo.png"));

        // Save whether the view is for the cart or orders.
        this.isCartView = isCartView;

        // Initialize managers using their respective factories.
        clientManager = FanetixClientFactory.getFanetixClientManager();
        cartManager = CartFactory.getCartManager();
        artistManager = ArtistFactory.getArtistManager();
        productManager = ProductFactory.getProductManager();

        // Retrieve the logged-in user.
        FanetixUser user = MenuBarViewController.getLoggedUser();
        // Retrieve the corresponding client information.
        client = getFanetixClient(user.getEmail());

        // Configure the info button image.
        btnInfo.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("eus/tartanga/crud/app/resources/btnInfo.png"))));
        btnInfo.setOnAction(this::handleInfoButton);

        // Configure row styling in the table.
        configureRowStyling();

        /**
         * Configure table columns and define how data should be displayed.
         */
        // Product image column (converting byte[] to Image)
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
                    // Show a default image if no image data is available
                    Image noImage = new Image(getClass().getClassLoader().getResourceAsStream("eus/tartanga/crud/app/resources/noImage.png"));
                    imageView.setImage(noImage);
                    setGraphic(imageView);
                } else {
                    // Convert byte[] to Image
                    Image image = new Image(new ByteArrayInputStream(imageBytes));
                    imageView.setImage(image);
                    setGraphic(imageView);
                }
            }
        });

        // Artist name column
        tbcArtist.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getProduct().getArtist().getName())
        );

        // Product name column
        tbcName.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getProduct().getTitle())
        );

        // Product description column
        tbcDescription.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getProduct().getDescription())
        );

        // Price column (formatted as "###,###.##")
        tbcPrice.setCellValueFactory(cellData -> {
            Float price = Float.valueOf(cellData.getValue().getProduct().getPrice());  // Obtener el precio
            String formatPrice = NumberFormat.getInstance(Locale.ROOT).format(price); // Formatear el precio
            return new SimpleStringProperty(formatPrice); // Devolver el precio formateado como SimpleStringProperty
        });

        // Order date column 
        tbcOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        //formatted as dd/MM/yyyy.
        tbcOrderDate.setCellFactory(column -> new TableCell<Cart, Date>() {
            private final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            @Override
            protected void updateItem(Date date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(formatter.format(date));
                }
            }
        });
        // Quantity column
        tbcQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Subtotal column (calculated from price and quantity)
        tbcSubTotal.setCellValueFactory(cellData -> {
            Cart cart = cellData.getValue();
            float subtotal = cart.getQuantity() * Float.valueOf(cart.getProduct().getPrice());
            return new SimpleObjectProperty<>(subtotal);
        });

        /**
         * Initializes the stage when accessed from the "My Cart" button in the
         * menu bar. Configures the table for displaying cart products, enables
         * editing, and sets up the context menu and event handlers.
         */
        if (isCartView) {
            LOGGER.info("Initializing Cart stage");
            // Set the window title to "My Cart".            
            stage.setTitle("My Cart");
            // Set the title image for the window.
            titleImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("eus/tartanga/crud/app/resources/MyCart.png")));
            // Make the table editable
            tbCart.setEditable(true);
            // Configure the context menu.
            contextMenu = new ContextMenu();
            // Create context menu items.
            MenuItem mIAddProduct = new MenuItem("Add Product");
            MenuItem mIDelete = new MenuItem("Delete Product");
            // Set action for adding a product (always enabled).
            mIAddProduct.setOnAction(this::handleAddProduct);
            // Set action for deleting a product (enabled only if a product is selected).
            mIDelete.setOnAction(event -> handleDeleteProduct());
            mIDelete.setDisable(true);
            //Desabilitar el boton de normal
            btnBuy.setDisable(true);
            //Activar la multiseleccion 
            tbCart.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            //El boton de buy solo estará active cuando haya algun item seleccionado
            tbCart.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldSelection, newSelection) -> btnBuy.setDisable(newSelection == null)
            );
            // Enable "Delete" only when a product is selected.
            tbCart.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldSelection, newSelection) -> mIDelete.setDisable(newSelection == null)
            );
            // Add menu items to the context menu and assign it to the table.
            contextMenu.getItems().addAll(mIAddProduct, mIDelete);
            tbCart.setContextMenu(contextMenu);

            // Configure editable columns.
            // Make "Order Date" column editable using a DatePicker.
            tbcOrderDate.setEditable(true);
            final Callback<TableColumn<Cart, Date>, TableCell<Cart, Date>> dateCell
                    = (TableColumn<Cart, Date> param) -> new CartDateEditingCell();
            tbcOrderDate.setCellFactory(dateCell);

            tbcOrderDate.setOnEditCommit(event -> {
                Cart cart = event.getRowValue();
                cart.setOrderDate(event.getNewValue());
                updateCart(cart);
            });

            // Make "Quantity" column editable with a Spinner.
            // The minimum value is 1 and the maximum value is the available stock .
            tbcQuantity.setEditable(true);

            final Callback<TableColumn<Cart, Integer>, TableCell<Cart, Integer>> quantityCellFactory = (TableColumn<Cart, Integer> param) -> new CartSpinnerEditingCell();
            tbcQuantity.setCellFactory(quantityCellFactory);

            // Handle event when the quantity value is edited.
            tbcQuantity.setOnEditCommit(event -> {
                Cart cart = event.getRowValue();
                int newQuantity = event.getNewValue();

                // Validate that the quantity does not exceed the available stock.
                if (newQuantity > Integer.parseInt(cart.getProduct().getStock())) {
                    cart.setQuantity(event.getOldValue()); // Restore previous value.
                    tbCart.refresh();

                    // Show an alert asynchronously.
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Stock insuficiente");
                        alert.setHeaderText(null);
                        alert.setContentText("Solo hay " + cart.getProduct().getStock() + " unidades disponibles.");
                        alert.showAndWait();
                    });

                } else {
                    // Update the quantity in the cart.
                    cart.setQuantity(newQuantity);
                    // Call method to update the cart.
                    updateCart(cart);
                }
            });

            // Configure "btnBuy" as the default button.
            btnBuy.setDefaultButton(true);
            btnBuy.setOnAction(this::handleBuyProduct);
            // Configure the button to clear all products from the cart.
            btnClearCart.setOnAction(this::handleDeleteAllProducts);
            // Calculate the total amount of the cart.
            updateTotal();

            // Hide artist and date filters.
            cbxArtist.setVisible(false);
            dpFrom.setVisible(false);
            dpTo.setVisible(false);
            // Hide the "btnPrint" button.
            btnPrint.setVisible(false);
            // Load all cart products that have not been purchased yet.
            cartList.addAll(findAllNotBoughtCartProducts());
        } else {
            /**
             * Initializes the "My Orders" window with relevant configurations,
             * including setting the window title, loading the artist names into
             * a choice box, setting up listeners for filtering products, and
             * managing the display of buttons. Also sets up the printing
             * functionality and populates the product table with data.
             */
            LOGGER.info("Initializing Orders stage");
            // Set the title of the window to "My Orders"
            stage.setTitle("My Orders");

            // Set the image for the window title
            titleImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("eus/tartanga/crud/app/resources/MyOrders.png")));

            // Load the names of the artists into the choice box
            loadArtists();

            // Add listener to filter products based on the selected artist
            cbxArtist.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> applyFilters());

            // Filter by date range when DatePickers change
            dpFrom.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
            dpTo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());

            // Generate a report with the available information in the "My Orders" table
            btnPrint.setOnAction(this::handlePrintOrders);

            // Hide the buttons btnClearCart, btnBuy, and the footer with the total
            btnBuy.setVisible(false);
            btnClearCart.setVisible(false);
            footer.setVisible(false);

            // Populate the cart list with bought products
            cartList.addAll(findAllBoughtCartProducts());
        }

        // Load the "Products" table with the available product information
        tbCart.setItems(cartList);

        // Set the scene for the stage
        stage.setScene(scene);

        // Display the stage (window)
        stage.show();

    }

    /**
     * Retrieves a FanetixClient object using the provided email. This method
     * queries the clientManager to find the client data in XML format. In case
     * of an exception during the retrieval process, it logs the error.
     *
     * @param email The email address used to find the FanetixClient.
     * @return The FanetixClient object corresponding to the provided email, or
     * null if an error occurs.
     */
    private FanetixClient getFanetixClient(String email) {
        FanetixClient fanetixClient = null;
        try {
            // Attempt to retrieve the FanetixClient object using the clientManager and the provided email
            fanetixClient = clientManager.findClient_XML(new GenericType<FanetixClient>() {
            }, email);

        } catch (ReadException ex) {
            // Log the exception in case of an error during the retrieval process
            LOGGER.getLogger(ProductViewController.class.getName()).log(Level.SEVERE, null, ex);
            // Show an error alert to the user
            showErrorAlert("An error occurred while retrieving client information. Please try again later.");
        }
        // Return the FanetixClient object, or null if an error occurred
        return fanetixClient;
    }

    /**
     * Handles the action event when the info button is clicked. It loads the
     * help view based on the current context (either "My Cart" or "My Orders")
     * and displays the corresponding help window. If an error occurs during the
     * process, it logs the exception.
     *
     * @param event The action event triggered by the info button click.
     */
    private void handleInfoButton(ActionEvent event) {
        boolean cart = this.isCartView;  // Check if the current view is the cart view
        try {
            LOGGER.info("Loading help view...");

            // Load the FXML file for the help view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/HelpMyCartOrdersView.fxml"));
            Parent root = (Parent) loader.load();  // Load the FXML file and create the view's root node

            // Get the controller for the loaded FXML file
            HelpMyCartOrdersViewController helpController = ((HelpMyCartOrdersViewController) loader.getController());

            if (cart) {
                // If the current view is the cart view, initialize and show the help stage for the cart
                helpController.initAndShowStageCart(root);
            } else {
                // If the current view is the order view, initialize and show the help stage for the order
                helpController.initAndShowStageOrder(root);
            }
        } catch (Exception ex) {
            // Log the exception
            LOGGER.severe("Error loading help view: " + ex.getMessage());

            // Show an error alert to the user
            showErrorAlert("An error occurred while loading the help view. Please try again.");
        }
    }

    /**
     * Configures the row styling for the cart table. This method alternates the
     * background color of each row based on its index (even rows get one color,
     * odd rows get another). It listens for changes in the row's item and
     * adjusts the style accordingly.
     */
    private void configureRowStyling() {
        // Set the row factory for the cart table to customize row styling
        tbCart.setRowFactory(tv -> {
            // Create a new row for the table
            TableRow<Cart> row = new TableRow<>();

            // Listen for changes in the row's item (data)
            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem == null) {
                    // If there is no data in the row, reset the style to default
                    row.setStyle("");
                } else {
                    // Get the index of the row
                    int index = row.getIndex();
                    if (index % 2 == 0) {
                        // Set the background color to pink for even-indexed rows
                        row.setStyle("-fx-background-color: #ffb6c1;"); // Pink
                    } else {
                        // Set the background color to light pink for odd-indexed rows
                        row.setStyle("-fx-background-color: #fdd3e1;"); // Light Pink
                    }
                }
            });

            // Return the row with the configured styling
            return row;
        });
    }

    /**
     * Handles the action event when the "Add Product" button is clicked. This
     * method loads the `ProductView.fxml` file, initializes the corresponding
     * controller, and sets the stage and the view for the product page. If an
     * error occurs during the process, it logs the exception.
     *
     * @param event The action event triggered by the "Add Product" button
     * click.
     */
    private void handleAddProduct(ActionEvent event) {
        try {
            // Load the FXML file for the product view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ProductView.fxml"));
            Parent root = (Parent) loader.load(); // Load the FXML file and create the root node for the scene

            // Get the controller for the loaded FXML file
            ProductViewController controller = ((ProductViewController) loader.getController());

            // Set the stage for the product view controller
            controller.setStage(stage);

            // Initialize the stage and display the product view
            controller.initStage(root);

        } catch (IOException ex) {
            // If there is an error during loading or displaying the product view, log the exception
            LOGGER.getLogger(ArtistViewController.class.getName()).log(Level.SEVERE, null, ex);
            // Show an error alert to the user
            showErrorAlert("An error occurred while loading the product view. Please try again.");
        }
    }

    /**
     * Handles the deletion of a selected product from the cart. This method
     * first displays a confirmation alert asking the user if they are sure
     * about deleting the product. If the user confirms, it removes the product
     * from both the database and the observable list. If an error occurs during
     * the deletion process, it shows an error alert.
     */
    private void handleDeleteProduct() {
        // Get the selected product from the table view
        Cart selectedProduct = tbCart.getSelectionModel().getSelectedItem();

        // Show a confirmation alert before proceeding with deletion
        boolean confirmDelete = showConfirmationAlert(
                "Are you sure you want to delete this product?", // Message in the alert
                "This action cannot be undone." // Additional information in the alert
        );

        if (confirmDelete) {
            // Get the email of the client and the product ID from the selected product
            String email = selectedProduct.getId().getEmail();
            String productId = selectedProduct.getProduct().getProductId().toString();

            try {
                // Remove the product from the database using the cart manager
                cartManager.removeCart(email, productId);

                // Remove the product from the observable list
                cartList.remove(selectedProduct);

            } catch (DeleteException e) {
                // If an error occurs during the deletion process, show an error alert
                showErrorAlert("An error occurred while deleting the product(s) from the cart");
                // Log the error for further analysis
                LOGGER.getLogger(getClass().getName()).log(Level.SEVERE, "Error deleting product with ID: " + productId, e);
            }
        }
    }

    /**
     * Updates the details of a product in the cart. This method sends the
     * updated cart information to the server using the cart manager. After
     * updating, it refreshes the table view to reflect the changes. If an error
     * occurs during the update process, it shows an error alert.
     *
     * @param cart The cart object containing the updated information of the
     * product.
     */
    private void updateCart(Cart cart) {
        try {
            // Update the cart information on the server using the cart manager
            cartManager.updateCart_XML(cart, cart.getId().getEmail(), cart.getId().getProductId().toString());
            // Calculate the total amount of the cart.
            updateTotal();
            // Refresh the table view to reflect the updated cart information
            tbCart.refresh();

        } catch (UpdateException e) {
            // If an error occurs during the update, show an error alert
            showErrorAlert("An error occurred while updating the product");
            // Log the error for further analysis
            LOGGER.getLogger(getClass().getName()).log(Level.SEVERE, "Error updating cart for product ID: " + cart.getId().getProductId(), e);
        }
    }

    /**
     * Handles the process of buying products in the cart. This method iterates
     * over the products in the cart, marks them as bought, updates the cart in
     * the database, and updates the stock of the corresponding products. If an
     * error occurs during the buying process, it shows an error alert.
     *
     * @param event The action event that triggered this method (usually from a
     * button click).
     */
    private void handleBuyProduct(ActionEvent event) {
        // Get the list of products that the user wants to buy
        ObservableList<Cart> selectedProducts = tbCart.getSelectionModel().getSelectedItems();
        // Get the selected product from the table view
        // Show a confirmation alert before proceeding with purchase
        boolean confirmPurchase = showConfirmationAlert(
                "Are you sure you want to buy the selected products?", // Message in the alert
                "" // Additional information in the alert
        );
        if (confirmPurchase) {
            try {
                // Iterate over each product in the cart that hasn't been bought
                for (Cart cart : selectedProducts) {
                    // Mark the product as bought
                    cart.setBought(true);

                    // Update the cart information in the database
                    cartManager.updateCart_XML(cart, cart.getId().getEmail(), cart.getId().getProductId().toString());

                    // Get the product related to the cart item
                    Product product = cart.getProduct();

                    // Get the current stock of the product
                    int currentStock = Integer.parseInt(product.getStock());
                    int quantityInCart = cart.getQuantity();
                    int updatedStock = currentStock - quantityInCart;

                    // Convert the updated stock to String
                    String updatedStockStr = String.valueOf(updatedStock);

                    // Update the stock of the product in the database
                    product.setStock(updatedStockStr);
                    productManager.edit_XML(product, product.getProductId().toString());
                }

                // Inform the user that the purchase was successful
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Purchase successful");
                alert.setHeaderText(null);
                alert.setContentText("Your products have been successfully purchased.");
                alert.showAndWait();
                //Update the database after buy products
                //Get the updated list of carts
                List<Cart> updatedCartList = findAllNotBoughtCartProducts();

                // Update the table with the new list
                tbCart.setItems(FXCollections.observableArrayList(updatedCartList));

                // Refresh the table view to reflect the updated cart information
                tbCart.refresh();

            } catch (UpdateException e) {
                // If an error occurs during the process, show an error alert
                showErrorAlert("Problem with buying products");
                // Log the error for further analysis
                LOGGER.getLogger(getClass().getName()).log(Level.SEVERE, "Error during product purchase", e);
            }
        }
    }

    /**
     * Retrieves a list of cart products that have not been bought yet, filtered
     * by the current client's email. The method fetches all the carts from the
     * database and then filters them by the current client's email. It also
     * attaches the corresponding products to each cart. If an error occurs
     * during the process, it shows an error alert.
     *
     * @return A list of Cart objects representing the not bought products for
     * the current client, or null if an error occurs.
     */
    private List<Cart> findAllNotBoughtCartProducts() {
        List<Cart> allCarts;  // List to store all cart items fetched from the database
        List<Cart> carts = null;  // List to store the filtered cart items for the current client
        List<Product> products = findAllProducts();  // Retrieve the list of all products from the database

        try {
            // Fetch all carts that have not been bsought yet
            allCarts = cartManager.findAllNotBoughtProducts_XML(new GenericType<List<Cart>>() {
            });

            // Filter the carts by the current client's email
            carts = allCarts.stream()
                    .filter(cart -> cart.getId().getEmail().equals(client.getEmail())) // Match email
                    .collect(Collectors.toList());  // Collect the filtered carts into a new list

            // Attach the products to the filtered carts
            attachProductsToCarts(carts, products);

        } catch (ReadException e) {
            // Show an error alert if an exception occurs while fetching cart data
            showErrorAlert("Problem with reading cart products");
            // Log the error for further analysis
            LOGGER.getLogger(getClass().getName()).log(Level.SEVERE, "Error fetching cart products", e);
        }

        // Return the list of carts for the current client, or null if an error occurred
        return carts;
    }

    /**
     * Retrieves a list of cart products that have been bought, filtered by the
     * current client's email. The method fetches all the carts from the
     * database and then filters them by the current client's email. It also
     * attaches the corresponding products to each cart. If an error occurs
     * during the process, it shows an error alert.
     *
     * @return A list of Cart objects representing the bought products for the
     * current client, or null if an error occurs.
     */
    private List<Cart> findAllBoughtCartProducts() {
        List<Cart> allCarts;  // List to store all cart items fetched from the database
        List<Cart> carts = null;  // List to store the filtered cart items for the current client
        List<Product> products = findAllProducts();  // Retrieve the list of all products from the database

        try {
            // Fetch all carts that have been bought
            allCarts = cartManager.findAllBoughtProducts_XML(new GenericType<List<Cart>>() {
            });

            // Filter the carts by the current client's email
            carts = allCarts.stream()
                    .filter(cart -> cart.getId().getEmail().equals(client.getEmail())) // Match email
                    .collect(Collectors.toList());  // Collect the filtered carts into a new list

            // Attach the products to the filtered carts
            attachProductsToCarts(carts, products);

        } catch (ReadException e) {
            // Show an error alert if an exception occurs while fetching cart data
            showErrorAlert("Problem with reading order products");
            // Log the error for further analysis
            LOGGER.getLogger(getClass().getName()).log(Level.SEVERE, "Error fetching orders", e);
        }

        // Return the list of carts for the current client, or null if an error occurred
        return carts;
    }

    /**
     * Retrieves a list of all products from the database. This method interacts
     * with the product manager to fetch all available products. If an error
     * occurs while fetching the product list, an error alert is displayed.
     *
     * @return A list of Product objects representing all the products, or null
     * if an error occurs.
     */
    private List<Product> findAllProducts() {
        List<Product> products = null;  // List to store all the fetched products

        try {
            // Fetch all products from the database
            products = productManager.findAll_XML(new GenericType<List<Product>>() {
            });

            // Return the list of products
            return products;
        } catch (ReadException e) {
            // Show an error alert if an exception occurs while fetching product data
            showErrorAlert("An error occurred while getting the product list");
            // Log the error for further analysis
            LOGGER.getLogger(getClass().getName()).log(Level.SEVERE, "Error fetching product list", e);
        }

        // Return null if an error occurred while fetching the products
        return products;
    }

    /**
     * Attaches the corresponding product to each cart in the provided list of
     * carts. This method uses the list of products to map product IDs to
     * Product objects and assigns the correct Product to each Cart based on the
     * product ID in the Cart.
     *
     * @param carts The list of Cart objects to which the products will be
     * attached.
     * @param products The list of Product objects used to map product IDs to
     * products.
     */
    private void attachProductsToCarts(List<Cart> carts, List<Product> products) {
        // Check if both carts and products are not null
        if (carts != null && products != null) {
            // Create a map that associates product IDs to corresponding Product objects
            Map<Integer, Product> productMap = products.stream()
                    .collect(Collectors.toMap(Product::getProductId, product -> product)); // Map of productId -> Product

            // Iterate through each cart and assign the corresponding product based on the productId
            for (Cart cart : carts) {
                // Set the product for the current cart
                cart.setProduct(productMap.get(cart.getId().getProductId())); // Assigns the correct product
            }
        }
    }

    /**
     * Handles the action of deleting all products from the cart. This method
     * shows a confirmation alert to ensure that the user intends to delete all
     * the products. If the user confirms the action, all the products in the
     * cart are removed, both from the database and from the observable list.
     * After deletion, the cart table is refreshed to reflect the changes.
     *
     * @param event The action event that triggers the deletion process.
     */
    private void handleDeleteAllProducts(ActionEvent event) {
        // Display a confirmation alert to the user before deleting all products
        boolean confirmDelete = showConfirmationAlert(
                "Are you sure you want to delete all products??",
                "This action cannot be undone."
        );

        // If the user confirms the deletion, proceed with the removal of all products
        if (confirmDelete) {
            try {
                // Retrieve the list of all products in the cart that have not been bought
                List<Cart> delete = findAllNotBoughtCartProducts();

                // Iterate over each cart in the list and remove it from the cart manager and the observable list
                for (Cart carts : delete) {
                    // Remove the cart product from the database using the cart's email and product ID
                    cartManager.removeCart(carts.getId().getEmail(), carts.getId().getProductId().toString());
                    // Remove the cart from the observable list to update the UI
                    cartList.remove(carts);
                }

                // Refresh the table view to reflect the changes made to the cart list
                tbCart.refresh();
            } catch (DeleteException e) {
                // Show an error alert if an exception occurs during the deletion process
                showErrorAlert("Problem with deleting products from the cart");
                // Log the error for further analysis
                LOGGER.getLogger(getClass().getName()).log(Level.SEVERE, "Error deleting products from cart", e);
            }
        }
    }

    /**
     * Updates the total price of all the products in the cart. It sums up the
     * subtotals of each product (quantity * price), formats the result, and
     * updates the label displaying the total price. The price is formatted
     * using the "###.###,##" pattern.
     *
     * @see Cart
     * @see Product
     */
    private void updateTotal() {
        // Initialize the total price to 0.00
        float total = 0.00f;

        // Retrieve the list of all products in the cart that have not been bought
        List<Cart> carts = findAllNotBoughtCartProducts();

        // Iterate over each product in the cart
        for (Cart cart : carts) {
            // Calculate the subtotal for each product (quantity * price)
            total += cart.getQuantity() * Float.valueOf(cart.getProduct().getPrice());
        }

        // Format the total price using the pattern “###.###,##”
        String formattedTotal = NumberFormat.getInstance(Locale.ROOT).format(total);

        // Update the total price label with the formatted total
        totalPriceLabel.setText(formattedTotal);
    }

    /**
     * Loads the list of artists from the database and populates the ComboBox
     * (`cbxArtist`) with their names. It also adds an option for "All" artists
     * at the top of the list, allowing the user to view all artists or select
     * one.
     *
     * @see Artist
     * @see ArtistManager
     */
    private void loadArtists() {
        // Declare the list to hold artists
        List<Artist> artistList;

        try {
            // Retrieve all artists from the database using the artist manager
            artistList = artistManager.findAllArtist(new GenericType<List<Artist>>() {
            });

            // Create a list to hold the names of the artists
            List<String> artistName = new ArrayList<>();

            // Iterate through the list of artists and add their names to the list
            for (Artist artists : artistList) {
                artistName.add(artists.getName());
            }

            // Clear any previous items in the ComboBox
            cbxArtist.getItems().clear();

            // Add an option to select "All" artists
            cbxArtist.getItems().add("All");

            // Add the names of the artists to the ComboBox
            cbxArtist.getItems().addAll(artistName);

            // Set the default value of the ComboBox to "All"
            cbxArtist.setValue("All");

        } catch (ReadException e) {
            // If there is an error reading the artists from the database, show an error alert
            showErrorAlert("Error loading artists from the database.");
            // Log the error for further analysis
            LOGGER.getLogger(getClass().getName()).log(Level.SEVERE, "Error loading artists from database", e);
        }
    }

    private void applyFilters() {
        List<Cart> allBoughtCarts = findAllBoughtCartProducts();

        // Obtener valores seleccionados del ChoiceBox y los DatePickers
        String selectedArtist = (String) cbxArtist.getValue();
        LocalDate startDate = dpFrom.getValue();
        LocalDate endDate = dpTo.getValue();

        // Aplicar filtro por artista
        List<Cart> filteredBoughtCarts = allBoughtCarts.stream()
                .filter(cart -> "All".equals(selectedArtist) || cart.getProduct().getArtist().getName().equals(selectedArtist))
                .collect(Collectors.toList());

        // Aplicar filtro por fecha
        filteredBoughtCarts = filteredBoughtCarts.stream()
                .filter(cart -> {
                    LocalDate orderDate = cart.getOrderDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    boolean afterStart = (startDate == null) || orderDate.isEqual(startDate) || orderDate.isAfter(startDate);
                    boolean beforeEnd = (endDate == null) || orderDate.isEqual(endDate) || orderDate.isBefore(endDate);

                    return afterStart && beforeEnd;
                })
                .collect(Collectors.toList());

        // Validación de fechas
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            showErrorAlert("The 'to' date cannot be earlier than the 'from' date.");
            return;
        }

        // Si no hay productos después de los filtros, mostrar un mensaje
        if (filteredBoughtCarts.isEmpty()) {
            showInformationAlert("No products match the selected filters.");
        }

        // Actualizar la tabla
        tbCart.getItems().clear();
        tbCart.getItems().addAll(filteredBoughtCarts);
    }

    /**
     * Handles the event to generate and display the order report. It compiles
     * and fills a JasperReports report with data from the cart and shows it in
     * a viewer.
     *
     * @param event The action event that triggers the printing process.
     */
    private void handlePrintOrders(ActionEvent event) {
        try {
            // Cargar el reporte JRXML (archivo de definición de reporte)
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/eus/tartanga/crud/userInterface/report/myOrdersReport.jrxml"));

            // Crear la fuente de datos para el reporte a partir de los elementos en el carrito
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<Cart>) this.tbCart.getItems());

            // Crear un mapa de parámetros para el reporte (en este caso, vacío)
            Map<String, Object> parameters = new HashMap<>();

            // Rellenar el reporte con los parámetros y los datos obtenidos
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);

            // Visualizar el reporte en un visualizador de JasperReports
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (JRException ex) {
            // Si ocurre un error, mostrar una alerta de error y registrar el problema
            showAlertPrint("Print Error:\n" + ex.getMessage());
            LOGGER.severe("UI GestionUsuariosController: Error printing report: {0}" + ex.getMessage());
        }
    }

    /**
     * Displays a confirmation alert to the user with the given header and
     * content text. The user can confirm the action by pressing "OK" or cancel
     * it by pressing "Cancel".
     *
     * @param headerText The text to be displayed in the header of the alert.
     * @param contentText The content text to be displayed in the alert.
     * @return boolean Returns true if the user clicks "OK", otherwise false if
     * "Cancel" is clicked.
     */
    private boolean showConfirmationAlert(String headerText, String contentText) {
        // Create the confirmation alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText(headerText);
        confirmationAlert.setContentText(contentText);

        // Display the alert and wait for the user's response
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        // Return true if the user clicks "OK" (ButtonType.OK)
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Displays an error alert with the given error message. The alert shows an
     * "Error" title and a header with "Server Error".
     *
     * @param message The error message to be displayed in the content of the
     * alert.
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Server Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays an informational alert with the given message. The alert shows
     * an "Information" title and the provided message in the content.
     *
     * @param message The informational message to be displayed in the content
     * of the alert.
     */
    private void showInformationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays an error alert specific to print-related issues with the
     * provided message. The alert shows an "Error" title and a header with
     * "Print Error".
     *
     * @param message The print-related error message to be displayed in the
     * content of the alert.
     */
    private void showAlertPrint(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Print Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
