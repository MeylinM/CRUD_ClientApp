package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.logic.FanetixClientFactory;
import eus.tartanga.crud.logic.FanetixClientManager;
import eus.tartanga.crud.model.FanetixClient;
import eus.tartanga.crud.model.FanetixUser;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;

/**
 * FXML Controller class for MenuBarView.fxml.
 *
 * @author Elbire and Meylin
 */
public class MenuBarViewController implements Initializable {

    @FXML
    private MenuBar menu;

    @FXML
    private MenuItem itemProfile;

    @FXML
    private MenuItem itemMyOrders;

    @FXML
    private MenuItem itemLogOut;

    @FXML
    private MenuItem itemArtist;

    @FXML
    private Menu menuArtist;

    @FXML
    private Menu menuMyCart;

    @FXML
    private Menu menuHelp;
    
    @FXML
    private Menu menuWindows;

    private static FanetixUser loggedUser;
    private static Stage stageMenu;
    private static final Logger LOGGER = Logger.getLogger("package view");
    private FanetixClientManager clientManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientManager = FanetixClientFactory.getFanetixClientManager();
        try {
            FanetixClient user = clientManager.findClient_XML(new GenericType<FanetixClient>() {
            }, loggedUser.getEmail());
            if (user == null) {
                menuMyCart.setVisible(false);
                itemMyOrders.setVisible(false);
            }else{
                menuWindows.setVisible(false);
            }
        } catch (ReadException ex) {
            Logger.getLogger(MenuBarViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        menuArtist.showingProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (newValue) {
                        menuArtist.getItems().get(0).fire();
                    }
                }
        );
        menuMyCart.showingProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (newValue) {
                        menuMyCart.getItems().get(0).fire();
                    }
                }
        );
        menuHelp.showingProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (newValue) {
                        menuHelp.getItems().get(0).fire();
                    }
                }
        );
    }

    @FXML
    public void openProfile(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ProfileView.fxml"));
            Parent root = (Parent) loader.load();
            ProfileViewController controller = (ProfileViewController) loader.getController();
            controller.setStage(stageMenu);
            controller.initStage(root);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.getLogger(MenuBarViewController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    public void openMyOrders(Event event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/CartOrdersView.fxml"));
            // Cargar el archivo FXML de la vista CartOrdersView
            Parent root = (Parent) loader.load();
            // Obtain the Sign In window controller
            CartOrdersViewController controller = (CartOrdersViewController) loader.getController();
            controller.setStage(stageMenu);
            controller.initStage(root, false);
        } catch (IOException ex) {

            LOGGER.log(Level.SEVERE, "Error loading CartOrdersView.fxml for My Orders", ex);
        }
    }

    @FXML
    public void logOut() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/SignInView.fxml"));
            Parent root = (Parent) loader.load();
            SignInViewController controller = ((SignInViewController) loader.getController());
            controller.setStage(stageMenu);
            controller.initStage(root);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading SignInView.fxml", e);
        }

    }

    @FXML
    public void openArtist(Event event) {
        try {
            // Cargar el archivo FXML de la vista ArtistView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ArtistView.fxml"));
            Parent root = loader.load();
            ArtistViewController controller = loader.getController();
            controller.setStage(stageMenu);
            controller.initStage(root);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "Error loading ArtistView.fxml", e);
        }
    }
    
    @FXML
    public void openConcert(Event event) {
        try {
            // Cargar el archivo FXML de la vista Concert
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ConcertView.fxml"));
            Parent root = loader.load();
            ConcertViewController controller = loader.getController();
            controller.setStage(stageMenu);
            controller.initStage(root);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "Error loading ConcertView.fxml", e);
        }
    }
    
    @FXML
    public void openProduct(Event event) {
        try {
            // Cargar el archivo FXML de la vista Product
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ProductView.fxml"));
            Parent root = loader.load();
            ProductViewController controller = loader.getController();
            controller.setStage(stageMenu);
            controller.initStage(root);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "Error loading ProductView.fxml", e);
        }
    }
    
    @FXML
    public void openMyCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/CartOrdersView.fxml"));
            // Cargar el archivo FXML de la vista CartOrdersView
            Parent root = (Parent) loader.load();
            // Obtain the Sign In window controller
            CartOrdersViewController controller = (CartOrdersViewController) loader.getController();
            controller.setStage(stageMenu);
            controller.initStage(root, true);
        } catch (IOException ex) {

            LOGGER.log(Level.SEVERE, "Error loading CartOrdersView.fxml for My Orders", ex);
        }
    }

    @FXML
    private void showHelp() {
        try {
            //LOGGER.info("Loading help view...");
            //Load node graph from fxml file
            FXMLLoader loader
                    = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/HelpGeneral.fxml"));
            Parent root = (Parent) loader.load();
            HelpGeneralController helpController
                    = ((HelpGeneralController) loader.getController());
            helpController.initAndShowStage(root);
            //Initializes and shows help stage
        } catch (Exception ex) {
            //If there is an error show message and
            //log it.
            System.out.println(ex.getMessage());

        }
    }

    public static void setUser(FanetixUser user) {
        loggedUser = user;
    }

    public static FanetixUser getLoggedUser() {
        return loggedUser;
    }

    public static void setStageMenu(Stage stageMenu) {
        MenuBarViewController.stageMenu = stageMenu;
    }

}
