package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.DeleteException;
import eus.tartanga.crud.exception.MaxCharacterException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.exception.TextEmptyException;
import eus.tartanga.crud.exception.UpdateException;
import eus.tartanga.crud.exception.WrongStockFormatException;
import eus.tartanga.crud.logic.ArtistFactory;
import eus.tartanga.crud.logic.ArtistManager;
import eus.tartanga.crud.logic.ConcertFactory;
import eus.tartanga.crud.logic.ConcertManager;
import eus.tartanga.crud.logic.FanetixClientFactory;
import eus.tartanga.crud.logic.FanetixClientManager;
import eus.tartanga.crud.logic.ProductManager;
import eus.tartanga.crud.model.Artist;
import eus.tartanga.crud.model.Concert;
import eus.tartanga.crud.model.FanetixClient;
import eus.tartanga.crud.model.FanetixUser;
import eus.tartanga.crud.model.Product;
import eus.tartanga.crud.userInterface.factories.ArtistDateEditingCell;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
 * Controller for managing the Artist view in the user interface. This class is
 * responsible for displaying and interacting with the list of artists,
 * including adding, deleting, searching, and viewing detailed information about
 * them. It also handles filtering the artist list, showing the artist's
 * details, and modifying the view depending on whether the user is an
 * administrator or a client.
 * <p>
 * The class also provides functionality for managing concerts, interacting with
 * the product manager, and viewing additional information such as concert
 * details. It supports the loading of a report using the JasperReports library
 * for generating artist-related reports.
 * </p>
 *
 * @author olaia
 */
public class ArtistViewController {

    /**
     * Table view to display the list of artists.
     */
    @FXML
    private TableView<Artist> artistTable;

    /**
     * Table column for displaying the artist's image.
     */
    @FXML
    private TableColumn<Artist, byte[]> artistColumn;

    /**
     * Table column for displaying the artist's name.
     */
    @FXML
    private TableColumn<Artist, String> nameColumn;

    /**
     * Table column for displaying the artist's company.
     */
    @FXML
    private TableColumn<Artist, String> companyColumn;

    /**
     * Table column for displaying the artist's last album.
     */
    @FXML
    private TableColumn<Artist, String> lastAlbumColumn;

    /**
     * Table column for displaying the artist's debut date.
     */
    @FXML
    private TableColumn<Artist, Date> debutColumn;

    /**
     * HBox container for displaying artist details.
     */
    @FXML
    private HBox detailsBox;

    /**
     * Label to display the artist's name.
     */
    @FXML
    private Label artistNameLabel;

    /**
     * Image view to display the artist's image.
     */
    @FXML
    private ImageView artistImageView;

    /**
     * Label to display the artist's company.
     */
    @FXML
    private Label companyLabel;

    /**
     * Label to display the artist's last album.
     */
    @FXML
    private Label lastAlbumLabel;

    /**
     * Anchor pane containing the artist view layout.
     */
    @FXML
    private AnchorPane artistAnchorPane;

    /**
     * Text field for searching artists by name.
     */
    @FXML
    private TextField searchField;

    /**
     * Button for adding a new artist.
     */
    @FXML
    private Button btnAddArtist;

    /**
     * Button for deleting an artist.
     */
    @FXML
    private Button btnDeleteArtist;

    /**
     * Button for viewing additional artist information.
     */
    @FXML
    private Button btnInfo;

    /**
     * Button for navigating to the shop view.
     */
    @FXML
    private Button shopButton;

    /**
     * Button for navigating to the concerts view.
     */
    @FXML
    private Button concertsButton;

    /**
     * Date picker for filtering artists based on the debut date range (from
     * date).
     */
    @FXML
    private DatePicker dpFrom;

    /**
     * Date picker for filtering artists based on the debut date range (to
     * date).
     */
    @FXML
    private DatePicker dpTo;

    /**
     * Stage for managing the artist view window.
     */
    private Stage stage;
    /**
     * Logger for logging events and errors.
     */
    private Logger logger = Logger.getLogger(ArtistViewController.class.getName());
    /**
     * Observable list for storing the list of artists.
     */
    private ObservableList<Artist> artistList = FXCollections.observableArrayList();
    /**
     * Observable list for storing the list of concerts.
     */
    private ObservableList<Concert> concertList = FXCollections.observableArrayList();
    /**
     * Context menu for artist-related actions inside the artist table.
     */
    private ContextMenu contextMenuInside;
    /**
     * Context menu for artist-related actions outside the artist table.
     */
    private ContextMenu contextMenuOutside;
    /**
     * Manager for handling artist data.
     */
    private ArtistManager artistManager;
    /**
     * Manager for handling concert data.
     */
    private ConcertManager concertManager;
    /**
     * Manager for handling product data.
     */
    private ProductManager productManager;
    /**
     * Manager for handling client data.
     */
    private FanetixClientManager clientManager;
    /**
     * Client object representing the logged-in user.
     */
    FanetixClient client;

    /**
     * Sets the stage for the artist view window.
     *
     * @param stage The stage for the artist view.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the artist view stage by setting up the scene, stage
     * properties, loading the logged-in user's information, and configuring the
     * artist table columns.
     *
     * @param root The root layout for the scene.
     */
    public void initStage(Parent root) {
        try {
            logger.info("Initializing Artist stage");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Artist");
            // Set the window icon
            stage.getIcons().add(new Image("eus/tartanga/crud/app/resources/logo.png"));
            stage.setResizable(false);
            stage.show();

            artistManager = ArtistFactory.getArtistManager();
            concertManager = ConcertFactory.getConcertManager();
            clientManager = FanetixClientFactory.getFanetixClientManager();

            // Get the logged-in user information
            FanetixUser user = MenuBarViewController.getLoggedUser();
            client = getFanetixClient(user.getEmail());

            // Configure table columns
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            companyColumn.setCellValueFactory(new PropertyValueFactory<>("company"));
            lastAlbumColumn.setCellValueFactory(new PropertyValueFactory<>("lastAlbum"));
            debutColumn.setCellValueFactory(new PropertyValueFactory<>("debut"));
            artistColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

            // Display the window differently for clients
            if (client != null) {
                btnAddArtist.setVisible(false);
                btnDeleteArtist.setVisible(false);
                // Apply date format for client view
                final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                debutColumn.setCellFactory(column -> new TableCell<Artist, Date>() {
                    @Override
                    protected void updateItem(Date date, boolean empty) {
                        super.updateItem(date, empty);
                        if (empty || date == null) {
                            setText(null);
                        } else {
                            setText(dateFormat.format(date)); // Format the date correctly
                        }
                    }
                });
            } else {

                /**
                 * Initializes the artist table for editing, with different
                 * behavior for administrators. It allows columns for the
                 * artist's debut date, name, company, and last album to be
                 * edited. Validation is applied to ensure that the name,
                 * company, and last album fields are not empty and do not
                 * exceed the allowed length. In case of validation errors, an
                 * error message is shown in an alert.
                 *
                 * <p>
                 * The following columns are made editable:
                 * <ul>
                 * <li>debutColumn: Editable using a custom cell factory that
                 * handles the date input.</li>
                 * <li>nameColumn: Editable, with validation to ensure the name
                 * is not empty or too long.</li>
                 * <li>companyColumn: Editable, with validation to ensure the
                 * company field is not empty or too long.</li>
                 * <li>lastAlbumColumn: Editable, with validation to ensure the
                 * last album field is not empty or too long.</li>
                 * </ul>
                 * </p>
                 *
                 * <p>
                 * The method also sets the action handlers for the "Add Artist"
                 * and "Delete Artist" buttons. The "Add Artist" button opens a
                 * new artist creation process, while the "Delete Artist" button
                 * removes the selected artist from the list.
                 * </p>
                 *
                 * Additionally, the image column for artists is configured with
                 * a custom cell factory that displays the artist's image within
                 * the table.
                 */
                artistTable.setEditable(true);
                debutColumn.setEditable(true);

                // In case of an administrator, show the data differently
                // Make the 'debutColumn' editable
                final Callback<TableColumn<Artist, Date>, TableCell<Artist, Date>> dateCell
                        = (TableColumn<Artist, Date> param) -> new ArtistDateEditingCell();
                debutColumn.setCellFactory(dateCell);
                debutColumn.setOnEditCommit(event -> {

                    Artist artist = event.getRowValue();
                    artist.setDebut(event.getNewValue());

                    try {
                        artistManager.updateArtist(artist, String.valueOf(artist.getArtistId()));
                    } catch (UpdateException ex) {
                        Logger.getLogger(ArtistViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

                // Make the 'nameColumn' editable
                nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                nameColumn.setOnEditCommit(event -> {
                    Artist artist = event.getRowValue();
                    String originalName = artist.getName();

                    try {
                        artist.setName(event.getNewValue());

                        if (artist.getName().isEmpty()) {
                            throw new TextEmptyException("El campo de nombre no puede estar vacío.");
                        } else if (artist.getName().length() > 20) {
                            throw new MaxCharacterException("El nombre no puede tener más de 20 caracteres.");
                        }

                        updateArtist(artist);
                    } catch (TextEmptyException | MaxCharacterException e) {
                        logger.log(Level.SEVERE, null, e);
                        artist.setName(originalName);
                        artistTable.refresh();

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error de validación");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                });

                // Make the 'companyColumn' editable
                companyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                companyColumn.setOnEditCommit(event -> {
                    Artist artist = event.getRowValue();
                    String originalCompany = artist.getCompany();
                    try {
                        artist.setCompany(event.getNewValue());
                        if (artist.getCompany() == null || artist.getCompany().isEmpty()) {
                            throw new TextEmptyException("El campo de compañia no puede estar vacío");
                        } else if (artist.getCompany().length() > 20) {
                            throw new MaxCharacterException("La compañia no puede tener más de 20 caracteres");
                        }
                        updateArtist(artist);
                    } catch (TextEmptyException | MaxCharacterException e) {
                        Logger.getLogger(ArtistViewController.class.getName()).log(Level.SEVERE, null, e);
                        artist.setCompany(originalCompany);
                        artistTable.refresh();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error de validación");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                    artist.setCompany(event.getNewValue());
                });

                // Make the 'lastAlbumColumn' editabl
                lastAlbumColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                lastAlbumColumn.setOnEditCommit(event -> {
                    Artist artist = event.getRowValue();
                    String originalAlbum = artist.getLastAlbum();
                    try {
                        artist.setLastAlbum(event.getNewValue());
                        if (artist.getLastAlbum() == null || artist.getLastAlbum().isEmpty()) {
                            throw new TextEmptyException("El campo de Last Album no puede estar vacío");
                        } else if (artist.getLastAlbum().length() > 50) {
                            throw new MaxCharacterException("La Last Album no puede tener más de 50 caracteres");
                        }
                        updateArtist(artist);
                    } catch (TextEmptyException | MaxCharacterException e) {
                        Logger.getLogger(ArtistViewController.class.getName()).log(Level.SEVERE, null, e);
                        artist.setLastAlbum(originalAlbum);
                        artistTable.refresh();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error de validación");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                });
                // Set action handlers for the buttons
                btnAddArtist.setOnAction(this::handleAddArtist);
                btnDeleteArtist.setOnAction(this::handleDeleteArtist);
            }
            // Configuration for the image column
            artistColumn.setCellFactory(column -> new TableCell<Artist, byte[]>() {
                private final ImageView imageView = new ImageView();

                {
                    imageView.setFitHeight(90);
                    imageView.setPreserveRatio(true);
                }

                /**
                 * Updates the image item of the table cell by setting the image
                 * as a graphic.
                 *
                 * If the image is empty or the row item is null, it sets no
                 * graphic. If the image bytes are provided, it either sets a
                 * default image if the byte array is null or converts the byte
                 * array to an `Image` object and sets it as the graphic for the
                 * table cell.
                 *
                 * @param imageBytes The byte array representing the image. Can
                 * be null.
                 * @param empty Indicates if the cell is empty (true) or
                 * contains data (false).
                 */
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

            /**
             * Initializes the artist view, including setting up the table and
             * buttons, and configuring context menus.
             *
             * <p>
             * Performs the following tasks:</p>
             * <ul>
             * <li>Sets the artist table items from the artist list.</li>
             * <li>Assigns actions to the "Info", "Go to Shop", and "Go to
             * Concert" buttons.</li>
             * <li>Initializes and shows context menus for different
             * actions.</li>
             * <li>Fetches the list of all artists and loads the artist data
             * into the table.</li>
             * <li>Configures row styling for the table.</li>
             * </ul>
             */
            artistTable.setItems(artistList);
            btnInfo.setOnAction(this::handleInfoButton);
            shopButton.setOnAction(this::handleGoToShop);
            concertsButton.setOnAction(this::handleGoToConcert);

            // Initialize the context menus
            createContextMenu();
            // Will show the context menu with the options mIAddToCart,
            // mIDeleteProduct, mIAddToProduct, and mIPrint when right-clicking on the table
            artistTable.setOnMousePressed(this::handleRightClickTable);
            // Will show the context menu with the options mIAddToProduct and mIPrint
            // when right-clicking outside of the table
            artistAnchorPane.setOnMouseClicked(this::handleRightClick);
            // Get a list of all products from the database
            artistList.addAll(findAllArtists());
            // Load the Products table with the information of the products
            artistTable.setItems(artistList);
            // Configure the styles of each row in the table
            configureRowStyling();

        } catch (Exception e) {
            logger.severe("Error initializing Artist stage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the list of all artists from the database.
     *
     * This method interacts with the `artistManager` to fetch all artists. If
     * an error occurs during the retrieval, it logs the error and returns an
     * empty list.
     *
     * @return A list of `Artist` objects, or an empty list if an error occurs
     * during retrieval.
     */
    private List<Artist> findAllArtists() {
        try {
            return artistManager.findAllArtist(new GenericType<List<Artist>>() {
            });
        } catch (ReadException e) {
            logger.severe("Error al obtener artistas: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
    }

    /**
     * Handles the "Go to Shop" button action.
     *
     * This method loads the "ProductView" scene and initializes the
     * corresponding controller. It also sets the current stage and initializes
     * the product view.
     *
     * @param event The action event triggered by the button click.
     */
    private void handleGoToShop(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ProductView.fxml"));
            Parent root = (Parent) loader.load();
            ProductViewController controller = ((ProductViewController) loader.getController());
            controller.setStage(stage);
            controller.initStage(root);
        } catch (IOException ex) {
            Logger.getLogger(ArtistViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the "Go to Concert" button action.
     *
     * This method loads the "ConcertView" scene and initializes the
     * corresponding controller. It also sets the current stage and initializes
     * the concert view.
     *
     * @param event The action event triggered by the button click.
     */
    private void handleGoToConcert(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ConcertView.fxml"));
            Parent root = (Parent) loader.load();
            ConcertViewController controller = ((ConcertViewController) loader.getController());
            controller.setStage(stage);
            controller.initStage(root);
        } catch (IOException ex) {
            Logger.getLogger(ArtistViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the right-click mouse event on the artist table.
     *
     * If a right-click is detected on the table, it displays the context menu
     * within the table. If another context menu (outside the table) is visible,
     * it hides it.
     *
     * @param event The mouse event triggered by the right-click action.
     */
    private void handleRightClickTable(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            if (contextMenuOutside.isShowing()) {
                contextMenuOutside.hide();
            }
            contextMenuInside.show(artistTable, event.getScreenX(), event.getScreenY());
        } else {
            contextMenuInside.hide();
        }
    }

    /**
     * Handles the right-click mouse event outside the artist table.
     *
     * If a right-click is detected outside the table, it displays the context
     * menu outside of the table. If the context menu inside the table is
     * visible, it hides it.
     *
     * @param event The mouse event triggered by the right-click action.
     */
    private void handleRightClick(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            if (contextMenuInside.isShowing()) {
                contextMenuInside.hide();
            }
            contextMenuOutside.show(artistAnchorPane, event.getScreenX(), event.getScreenY());
        } else {
            contextMenuOutside.hide();
        }
    }

    /**
     * Filters a list of artists based on the search text and date range.
     *
     * The method filters the artists by their name or company, as well as by
     * their debut date. It returns a filtered list of artists that match the
     * search criteria. If no search text is provided, it only filters by the
     * date range.
     *
     * @param searchText The text used to search for artists by name or company.
     * Can be null or empty.
     * @param fromDate The start date for filtering artists by debut date. Can
     * be null.
     * @param toDate The end date for filtering artists by debut date. Can be
     * null.
     *
     * @return A filtered list of artists based on the search criteria.
     */
    public FilteredList<Artist> getArtistsBySearchField(String searchText, LocalDate fromDate, LocalDate toDate) {
        // Create a FilteredList with the original list of artists
        FilteredList<Artist> filteredData = new FilteredList<>(artistList, a -> true);

        // If the search text is not null or empty, filter the artists
        if (searchText != null && !searchText.isEmpty()) {
            // Convert the search text to lowercase to make the comparison case-insensitive
            String lowerCaseFilter = searchText.toLowerCase();

            // Apply the filtering predicate based on each artist's attributes
            filteredData.setPredicate(artist -> {
                // Filter only by name and company
                boolean matchesSearch = artist.getName().toLowerCase().contains(lowerCaseFilter)
                        || artist.getCompany().toLowerCase().contains(lowerCaseFilter);

                // Filter by dates (if the dates are defined)
                boolean matchesDate = true;

                if (fromDate != null && toDate != null) {
                    Date artistReleaseDate = artist.getDebut();  // This is Date (e.g., java.util.Date)

                    // Convert LocalDate to java.sql.Date for the comparison
                    java.sql.Date fromSQLDate = java.sql.Date.valueOf(fromDate);
                    java.sql.Date toSQLDate = java.sql.Date.valueOf(toDate);

                    // Compare the dates
                    matchesDate = !artistReleaseDate.before(fromSQLDate) && !artistReleaseDate.after(toSQLDate);
                }

                // Return true only if the artist matches all filters
                return matchesSearch && matchesDate;
            });
        } else {
            // If there is no search text, filter only by dates
            filteredData.setPredicate(artist -> {
                boolean matchesDate = true;

                if (fromDate != null && toDate != null) {
                    Date artistReleaseDate = artist.getDebut();  // This is Date (e.g., java.util.Date)

                    // Convert LocalDate to java.sql.Date for the comparison
                    java.sql.Date fromSQLDate = java.sql.Date.valueOf(fromDate);
                    java.sql.Date toSQLDate = java.sql.Date.valueOf(toDate);

                    // Compare the dates
                    matchesDate = !artistReleaseDate.before(fromSQLDate) && !artistReleaseDate.after(toSQLDate);
                }

                // If there is no search text, just return the result of the dates filter
                return matchesDate;
            });
        }

        return filteredData; // Return the applied filter
    }

    /**
     * This method initializes the context menus used in the artist table. It
     * creates two context menus: one for inside the table and one for outside
     * the table. The menu items include options to add a new artist, delete an
     * artist, and print.
     */
    private void createContextMenu() {
        contextMenuInside = new ContextMenu();
        MenuItem addItem = new MenuItem("Add new artist");
        addItem.setOnAction(this::handleAddArtist);
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(this::handleDeleteArtist);
        MenuItem printItemInside = new MenuItem("Print");
        printItemInside.setOnAction(this::printItems);
        contextMenuInside.getItems().addAll(addItem, deleteItem, printItemInside);

        contextMenuOutside = new ContextMenu();
        MenuItem printItemOutside = new MenuItem("Print");
        printItemOutside.setOnAction(this::printItems);
        MenuItem addItemOutside = new MenuItem("Add new artist");
        addItemOutside.setOnAction(this::handleAddArtist);
        contextMenuOutside.getItems().addAll(printItemOutside, addItemOutside);
    }

    /**
     * Handles adding a new artist to the artist table. Creates a new empty
     * artist, adds it to the table and refreshes the table view. Displays an
     * alert informing the user that the artist has been added successfully.
     *
     * @param event the event triggered by clicking the "Add new artist" menu
     * item
     */
    private void handleAddArtist(ActionEvent event) {
        Artist artist = new Artist(); /// Creates a new empty artist
        try {
            artistManager.createArtist(artist); // Calls the method to add the artist
            artistTable.refresh();
        } catch (AddException e) {
            System.out.println(e); // Prints the exception in case of error
        }

        artistTable.getItems().add(artist); // Adds the artist to the table
        artistTable.refresh();

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText("Artista añadido correctamente");
        alert.setContentText("Si desea editar el artista recién añadido, por favor, refresque la página seleccionando 'Artist' en el menú.");
        alert.showAndWait();
    }

    /**
     * Handles the action of the info button, which opens a help view for
     * artists. Depending on the user's role (client or admin), it initializes
     * and shows the appropriate help stage.
     *
     * @param event the event triggered by clicking the info button
     */
    private void handleInfoButton(ActionEvent event) {
        try {
            FXMLLoader loader
                    = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/HelpArtistView.fxml"));
            Parent root = (Parent) loader.load();
            HelpArtistController helpController
                    = ((HelpArtistController) loader.getController());
            // Initializes and shows help stage
            if (client != null) {
                helpController.initAndShowStageClient(root);
            } else {
                helpController.initAndShowStageAdmin(root);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        }
    }

    /**
     * Handles the action of deleting an artist. First checks if the artist has
     * associated concerts. If so, an alert is shown and the deletion process is
     * stopped. If not, the artist is deleted from the database and the table.
     *
     * @param event the event triggered by clicking the "Delete" menu item
     */
    private void handleDeleteArtist(ActionEvent event) {
        // Get the selected artist from the table
        Artist selectedArtist = artistTable.getSelectionModel().getSelectedItem();

        if (selectedArtist == null) {
            logger.warning("No se ha seleccionado ningún artista para eliminar.");
            return;
        }

        try {
            // 1. Check if the artist has associated concerts
            boolean hasConcerts = eliminarConciertosAsociados(selectedArtist);

            if (hasConcerts) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No se puede eliminar el artista");
                alert.setHeaderText("El artista " + selectedArtist.getName() + " tiene conciertos asociados.");
                alert.setContentText("Elimine los conciertos asociados antes de borrar el artista.");
                alert.showAndWait();
                return;
            }

            // 2. If no concerts are associated, proceed with deletion
            logger.info("Intentando eliminar artista con ID: " + selectedArtist.getArtistId());
            artistManager.removeArtist(selectedArtist.getArtistId().toString());
            artistList.remove(selectedArtist);
            logger.info("Artista eliminado con éxito: " + selectedArtist.getName());

        } catch (DeleteException e) {
            logger.severe("Error al eliminar el artista y sus relaciones: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This method checks if the selected artist has any associated concerts. If
     * associated concerts are found, it returns true, otherwise false.
     *
     * @param selectedArtist the artist to check for associated concerts
     * @return true if there are associated concerts, false otherwise
     */
    private boolean eliminarConciertosAsociados(Artist selectedArtist) {

        // Get the list of all concerts
        List<Concert> concertList;
        try {
            concertList = concertManager.findAllConcerts_XML(new GenericType<List<Concert>>() {
            });

            if (concertList == null) {
                logger.severe("No se pudo obtener la lista de conciertos.");
                return false;
            }

            // Filter the concerts that contain the selected artist
            List<Concert> concertsToDelete = concertList.stream()
                    .filter(concert -> concert.getArtistList() != null && concert.getArtistList().contains(selectedArtist))
                    .collect(Collectors.toList());

            if (!concertsToDelete.isEmpty()) {
                // If concerts are found, return 'true' to indicate the artist should not be deleted
                return true;
            }

            // If no concerts were found, return 'false'
            logger.info("No se encontraron conciertos asociados al artista " + selectedArtist.getName());

        } catch (ReadException ex) {
            Logger.getLogger(ArtistViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Handles printing the items (artists). Compiles a Jasper report and
     * displays it in a JasperViewer.
     *
     * @param event the event triggered by clicking the "Print" menu item
     */
    private void printItems(ActionEvent event) {
        try {
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/eus/tartanga/crud/userInterface/report/artistReport.jrxml"));
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<Artist>) this.artistTable.getItems());
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (JRException ex) {
            logger.severe("Error printing report: {0}" + ex.getMessage());
        }
    }

    /**
     * This method filters the artists in the table based on the text typed by
     * the user. Also filters the artists by the selected date range from the
     * DatePickers.
     */
    private void filterArtists() {
        // While the user is typing, the value will be used to filter the artists in the table
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter();
        });
        // Filters by date range when the DatePickers change
        dpFrom.valueProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter();
        });

        dpTo.valueProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter();
        });

    }

    /**
     * Applies the filter based on the search text and date range. Updates the
     * table with the filtered artists.
     */
    private void applyFilter() {
        // Get the search text and the selected dates
        String searchText = searchField.getText();
        LocalDate fromDate = dpFrom.getValue();
        LocalDate toDate = dpTo.getValue();

        // Apply the filtering to the artists
        FilteredList<Artist> filteredArtist = getArtistsBySearchField(searchText, fromDate, toDate);

        // Update the table with the filtered artists
        SortedList<Artist> sortedData = new SortedList<>(filteredArtist);
        sortedData.comparatorProperty().bind(artistTable.comparatorProperty());
        artistTable.setItems(sortedData);
    }

    /**
     * Retrieves a FanetixClient based on the provided email.
     *
     * @param email the email of the client
     * @return the corresponding FanetixClient object
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
     * Configures the row styling for the artist table. Alternates the row
     * background color based on the row index.
     */
    private void configureRowStyling() {
        artistTable.setRowFactory(tv -> {
            TableRow<Artist> row = new TableRow<>();

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
     * Updates the details of an artist.
     *
     * @param artist the artist to update
     */
    private void updateArtist(Artist artist) {
        try {
            artistManager.updateArtist(artist, artist.getArtistId().toString());
        } catch (UpdateException e) {
            System.out.println(e);
        }
    }

}
