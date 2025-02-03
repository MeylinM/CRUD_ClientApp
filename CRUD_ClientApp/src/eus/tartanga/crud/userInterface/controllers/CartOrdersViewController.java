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
import eus.tartanga.crud.userInterface.factories.ProductDateEditingCell;
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
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

/**
 *
 * @author meylin
 */
public class CartOrdersViewController {

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
    private Label totalPriceLabel;

    @FXML
    private Button btnBuy;

    @FXML
    private Button btnClearCart;

    @FXML
    private Button btnPrint;

    // true para "Cart", false para "My Orders"
    private CartManager cartManager;
    private ProductManager productManager;
    private ArtistManager artistManager;
    private ObservableList<Cart> cartList = FXCollections.observableArrayList();
    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private FanetixClient client;
    private FanetixClientManager clientManager;
    private Stage stage;
    private static final Logger LOGGER = Logger.getLogger(ArtistViewController.class.getName());
    boolean isCartView;
    ContextMenu contextMenu;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root, boolean isCartView) {

        Scene scene = new Scene(root);
        //General para ambas vistas de la ventana
        //Añadir a la ventana el ícono “FanetixLogo.png”.
        stage.getIcons().add(new Image("eus/tartanga/crud/app/resources/logo.png"));
        //Ventana no redimensionable
        stage.setResizable(false);
        this.isCartView = isCartView;
        clientManager = FanetixClientFactory.getFanetixClientManager();
        FanetixUser user = MenuBarViewController.getLoggedUser();
        client = getFanetixClient(user.getEmail());
        btnInfo.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("eus/tartanga/crud/app/resources/btnInfo.png"))));
        cartManager = CartFactory.getCartManager();
        artistManager = ArtistFactory.getArtistManager();
        productManager = ProductFactory.getProductManager();
        configureRowStyling();
        //COMO VA A MOSTRAR LA TABLA LOS DATOS
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
                -> new SimpleStringProperty(cellData.getValue().getProduct().getTitle())
        );

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
        // Fecha de orden del producto
        tbcOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        // - Lacolumna“Order Date” deberá estar formateada con el patrón “dd/mm/yyyy”
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
        //Cantidad del prodcuto
        tbcQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        /* -Sub total es una columna con un valor calculado con la cantidad y el precio*/
        tbcSubTotal.setCellValueFactory(cellData -> {
            Cart cart = cellData.getValue();
            float subtotal = cart.getQuantity() * Float.valueOf(cart.getProduct().getPrice());
            return new SimpleObjectProperty<>(subtotal);
        });

        btnInfo.setOnAction(this::handleInfoButton);

        // Si se accede desde el botón My Cart situado en el menubar
        if (isCartView) {
            // Configuración para "My Carts"
            LOGGER.info("Initializing Cart stage");
            // Establecer el título de la ventana con el valor "My Cart".
            stage.setTitle("My Cart");
            //Imagen con el titulo de la ventana
            titleImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("eus/tartanga/crud/app/resources/MyCart.png")));
            //La tabla será editable
            tbCart.setEditable(true);
            //Context menu d
            contextMenu = new ContextMenu();
            // Crear las opciones del menú contextual
            MenuItem mIAddProduct = new MenuItem("Add Product");
            MenuItem mIDelete = new MenuItem("Delete Product");
            // Acción para añadir producto (siempre habilitado)
            mIAddProduct.setOnAction(this::handleAddProduct);
            // Acción para eliminar producto (solo si hay selección)
            mIDelete.setOnAction(event -> handleDeleteProduct());
            mIDelete.setDisable(true);

            // Habilitar "Delete" solo cuando hay un producto seleccionado
            tbCart.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldSelection, newSelection) -> mIDelete.setDisable(newSelection == null)
            );

            // Agregar items al ContextMenu y asignarlo a la tabla
            contextMenu.getItems().addAll(mIAddProduct, mIDelete);
            tbCart.setContextMenu(contextMenu);

            //Columna"Order Date": Convertir en editable usando un DatePicker.
            tbcOrderDate.setEditable(true);
            final Callback<TableColumn<Cart, Date>, TableCell<Cart, Date>> dateCell
                    = (TableColumn<Cart, Date> param) -> new CartDateEditingCell();
            tbcOrderDate.setCellFactory(dateCell);

            tbcOrderDate.setOnEditCommit(event -> {
                Cart cart = event.getRowValue();
                cart.setOrderDate(event.getNewValue());
                updateCart(cart);
            });

            //Columna Quantity: Configurar como editable con un Spinner
            /*
            *donde el valor mínimo será 1 y el máximo será el stock 
            * disponible (Product.getStock()). 
             */
            final Callback<TableColumn<Cart, Integer>, TableCell<Cart, Integer>> quantityCellFactory = (TableColumn<Cart, Integer> param) -> new CartSpinnerEditingCell();
            tbcQuantity.setCellFactory(quantityCellFactory);

            tbcQuantity.setEditable(true);

            // Manejar el evento cuando se edita el valor de la cantidad
            tbcQuantity.setOnEditCommit(event -> {
                Cart cart = event.getRowValue();
                cart.setQuantity(event.getNewValue()); // Actualiza la cantidad en el modelo de datos
                updateCart(cart); // Actualiza el carrito, puedes personalizar esta lógica
            });
            //"btnBuy". (Botón por defecto)
            btnBuy.setDefaultButton(
                    true);
            btnBuy.setOnAction(
                    this::handleBuyProduct);
            btnClearCart.setOnAction(
                    this::handleDeleteAllProducts);
            updateTotal();
            // Los filtros por artista y fecha estarán ocultos.

            cbxArtist.setVisible(
                    false);
            dpFrom.setVisible(
                    false);
            dpTo.setVisible(
                    false);

            // El botón“btnPrint” estará oculto.
            btnPrint.setVisible(
                    false);
            cartList.addAll(findAllNotBoughtCartProducts());
        } else {
            // Configuración para "My Orders"
            LOGGER.info("Initializing Orders stage");
            //Establecer el título de la ventana con el valor "My Orders"
            stage.setTitle("My Orders");
            //Imagen con el titulo de la ventana
            titleImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream("eus/tartanga/crud/app/resources/MyOrders.png")));
            //Cargar los nombres de los artistas en el choice box
            loadArtists();
            // Agregar listener para filtrar productos según el artista seleccionado
            cbxArtist.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> applyFilters());
            // Filtra por rango de fechas cuando cambian los DatePickers
            dpFrom.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
            dpTo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
            //Genera un informe con la información disponible en la tabla my orders
            btnPrint.setOnAction(this::handlePrintOrders);
            //Ocultar botones btnClearCart, btnBuy y el footer con el total.
            btnBuy.setVisible(false);
            btnClearCart.setVisible(false);
            footer.setVisible(false);
            cartList.addAll(findAllBoughtCartProducts());
        }
        //Cargar la tabla Products con la información de los productos

        tbCart.setItems(cartList);

        stage.setScene(scene);

        stage.show();

    }

    private void handleBuyProduct(ActionEvent event) {
        List<Cart> buy = findAllNotBoughtCartProducts();
        try {

            // Iterar sobre cada producto en la lista del carrito
            for (Cart cart : buy) {
                cart.setBought(true);
                cartManager.updateCart_XML(cart, cart.getId().getEmail(), cart.getId().getProductId().toString());

                // Obtener el producto relacionado con el carrito
                Product product = cart.getProduct();

                // Actualizar el stock del producto
                int currentStock = Integer.parseInt(product.getStock());
                int quantityInCart = cart.getQuantity();
                int updatedStock = currentStock - quantityInCart;

                // Convertir el nuevo stock de int a String
                String updatedStockStr = String.valueOf(updatedStock);

                // Actualizar el stock del producto
                product.setStock(updatedStockStr);
                productManager.edit_XML(product, product.getProductId().toString());

            }
            tbCart.refresh();
        } catch (UpdateException e) {
            showErrorAlert("Problem with buying products");
        }
    }

    private void handleDeleteAllProducts(ActionEvent event) {
        // Mostrar el alert de confirmación antes de eliminar todos los productos
        boolean confirmDelete = showConfirmationAlert(
                "Are you sure you want to delete all products??",
                "This action cannot be undone."
        );
        // Si el usuario confirma, proceder con la eliminación
        if (confirmDelete) {
            try {
                List<Cart> delete = findAllNotBoughtCartProducts();
                for (Cart carts : delete) {
                    cartManager.removeCart(carts.getId().getEmail(), carts.getId().getProductId().toString());
                    cartList.remove(carts);
                }
                tbCart.refresh();
            } catch (DeleteException e) {
                showErrorAlert("Problem with deleting products from the cart");
            }
        }
    }

    private List<Cart> findAllNotBoughtCartProducts() {
        List<Cart> allCarts;
        List<Cart> carts = null;
        try {
            allCarts = cartManager.findAllNotBoughtProducts_XML(new GenericType<List<Cart>>() {
            });

            carts = allCarts.stream()
                    .filter(cart -> cart.getId().getEmail().equals(client.getEmail()))
                    .collect(Collectors.toList());
        } catch (ReadException e) {
            showErrorAlert("Problem with reading not bought products");
        }
        return carts;
    }

    private List<Cart> findAllBoughtCartProducts() {
        List<Cart> allCarts;
        List<Cart> carts = null;
        try {
            allCarts = cartManager.findAllBoughtProducts_XML(new GenericType<List<Cart>>() {
            });

            carts = allCarts.stream()
                    .filter(cart -> cart.getId().getEmail().equals(client.getEmail()))
                    .collect(Collectors.toList());

        } catch (ReadException e) {
            showErrorAlert("Problem with reading bought products");
        }
        return carts;
    }

    private FanetixClient getFanetixClient(String email) {
        FanetixClient fanetixClient = null;
        try {
            fanetixClient = clientManager.findClient_XML(new GenericType<FanetixClient>() {
            }, email);

        } catch (ReadException ex) {
            Logger.getLogger(ProductViewController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return fanetixClient;
    }

    private void handleAddProduct(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ProductView.fxml"));
            Parent root = (Parent) loader.load();
            //Scene scene = new Scene(root);
            ProductViewController controller = ((ProductViewController) loader.getController());
            controller.setStage(stage);
            controller.initStage(root);

        } catch (IOException ex) {
            Logger.getLogger(ArtistViewController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleDeleteProduct() {
        Cart selectedProduct = tbCart.getSelectionModel().getSelectedItem();
        // Mostrar el alert de confirmación antes de eliminar
        boolean confirmDelete = showConfirmationAlert(
                "Are you sure you want to delete this product?",
                "This action cannot be undone."
        );
        if (confirmDelete) {
            String email = selectedProduct.getId().getEmail();
            String productId = selectedProduct.getProduct().getProductId().toString();
            try {
                // Elimina el producto de la base de datos
                cartManager.removeCart(email, productId);
                // Elimina el producto de la lista observable
                cartList.remove(selectedProduct);

            } catch (DeleteException e) {
                showErrorAlert("An error occurred while deleting the product(s) from the cart");
            }
        }
    }

    private void updateTotal() {
        // Sumar todos los subtotales
        float total = 0.00f;
        List<Cart> carts = findAllNotBoughtCartProducts();
        // Iterar sobre cada producto en el carrito
        for (Cart cart : carts) {
            total += cart.getQuantity() * Float.valueOf(cart.getProduct().getPrice());
        }

        // Formatear el total con el patrón “###.###,##”
        String formattedTotal = NumberFormat.getInstance(Locale.ROOT).format(total);

        // Actualizar el Label con el total formateado
        totalPriceLabel.setText(formattedTotal);
    }

    private void configureRowStyling() {
        tbCart.setRowFactory(tv -> {
            TableRow<Cart> row = new TableRow<>();

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

    private void loadArtists() {
        List<Artist> artistList;
        try {
            artistList = artistManager.findAllArtist(new GenericType<List<Artist>>() {
            });

            List<String> artistName = new ArrayList<>();
            for (Artist artists : artistList) {
                artistName.add(artists.getName());
            }
            cbxArtist.getItems().clear(); // Limpiar opciones previas
            cbxArtist.getItems().add("All");
            cbxArtist.getItems().addAll(artistName); // Agregar artistas
            cbxArtist.setValue("All");
        } catch (ReadException e) {
            showErrorAlert("Error loading artists from the database.");
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

    private void handlePrintOrders(ActionEvent event) {
        try {
            // Cargar el reporte JRXML
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/eus/tartanga/crud/userInterface/report/myOrdersReport.jrxml"));

            // Crear la fuente de datos para el reporte
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<Cart>) this.tbCart.getItems());

            // Crear un mapa de parámetros para el reporte
            Map<String, Object> parameters = new HashMap<>();

            // Rellenar el reporte con los parámetros y los datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);

            // Visualizar el reporte
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (JRException ex) {
            showAlertPrint("Print Error:\n" + ex.getMessage());
            LOGGER.severe("UI GestionUsuariosController: Error printing report: {0}" + ex.getMessage());
        }
    }

    private void handleInfoButton(ActionEvent event) {
        boolean cart = this.isCartView;
        try {
            LOGGER.info("Loading help view...");
            //Load node graph from fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/HelpMyCartOrdersView.fxml"));
            Parent root = (Parent) loader.load();
            HelpMyCartOrdersViewController helpController = ((HelpMyCartOrdersViewController) loader.getController());
            if (cart) {

                //Initializes and shows help stage
                helpController.initAndShowStageCart(root);
            } else {
                helpController.initAndShowStageOrder(root);
            }
        } catch (Exception ex) {
            //If there is an error show message and
            //log it.
            System.out.println(ex.getMessage());

        }
    }
    //ALERTS

    private boolean showConfirmationAlert(String headerText, String contentText) {
        // Crear el alert de confirmación
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Confirmation");
        confirmationAlert.setHeaderText(headerText);
        confirmationAlert.setContentText(contentText);

        // Mostrar el alert y esperar la respuesta
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        // Retornar true si el usuario hace clic en "Aceptar" (ButtonType.OK)
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error de servidor");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInformationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlertPrint(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Print Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateCart(Cart cart) {
        try {
            cartManager.updateCart_XML(cart, cart.getId().getEmail(), cart.getId().getProductId().toString());
            tbCart.refresh();
        } catch (UpdateException e) {
            showErrorAlert("An error occurred while updating the product");
        }
    }
}
