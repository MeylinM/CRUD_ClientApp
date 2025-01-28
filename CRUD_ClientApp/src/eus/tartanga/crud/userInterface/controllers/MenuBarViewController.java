package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.model.FanetixUser;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * FXML Controller class for MenuBarView.fxml.
 *
 * @author
 */
public class MenuBarViewController {

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
    private MenuItem itemMyCart;

    @FXML
    private MenuItem itemHelpArtist;

    @FXML
    private MenuItem itemHelpConcert;

    @FXML
    private MenuItem itemHelpProduct;

    @FXML
    private MenuItem itemHelpCart;

    @FXML
    private MenuItem itemHelpOrder;

    private static FanetixUser loggedUser;
    private static Stage stageMenu;
    private static final Logger LOGGER = Logger.getLogger("package view");

    @FXML
    public void openProfile(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ProfileView.fxml"));
            Parent root = (Parent) loader.load();

            ProfileViewController controller = (ProfileViewController) loader.getController();
            controller.setStage(stageMenu);
            controller.initStage(root, loggedUser);
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
            // Cargar el archivo FXML de SignInView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/SignInView.fxml"));
            Parent root = loader.load();

            // Crear una nueva ventana para SignIn
            Stage signInStage = new Stage();
            signInStage.setScene(new Scene(root));
            signInStage.setTitle("Sign In");

            // Obtener el Stage actual (la ventana que contiene el menú)
            Stage currentStage = (Stage) menu.getScene().getWindow();

            // Cerrar la ventana actual
            currentStage.close();

            // Mostrar la ventana de inicio de sesión
            signInStage.show();

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
            controller.setStage(stageMenu); // Asegúrate de que stageMenu esté inicializado
            controller.initStage(root);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "Error loading ArtistView.fxml", e);
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
    private void showHelpArtist() {

    }

    @FXML
    private void showHelpConcert() {
    }

    @FXML
    private void showHelpProduct() {
    }

    @FXML
    private void showHelpCart() {
    }

    @FXML
    private void showHelpOrder() {
    }

}
