/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.exception.SignInException;
import eus.tartanga.crud.logic.AdministratorFactory;
import eus.tartanga.crud.logic.AdministratorManager;
import eus.tartanga.crud.logic.FanetixClientFactory;
import eus.tartanga.crud.logic.FanetixClientManager;
import eus.tartanga.crud.model.Administrator;
import eus.tartanga.crud.model.FanetixClient;
import eus.tartanga.crud.model.FanetixUser;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;

/**
 * @author Elbire
 */
public class SignInViewController {

    // Elementos de la vista
    @FXML
    private AnchorPane signIn;
    @FXML
    private TextField tfEmail; // Campo de texto para el usuario (email o username)
    @FXML
    private PasswordField pfPassword; // Campo para la contraseña
    @FXML
    private TextField tfPassword;
    @FXML
    private ToggleButton tgbtnEyeIcon; // Botón para mostrar/ocultar contraseña
    @FXML
    private Button btnAccept; // Botón de "Accept"
    @FXML
    private Hyperlink hypSignUp; // Hipervínculo para "Sign Up"
    @FXML
    private Hyperlink hypForgotPassword; // Hipervínculo para "Did you forget your password?"
    private Stage stage;
    private Logger logger = Logger.getLogger(SignInViewController.class.getName());
    private AdministratorManager adminManager;
    private FanetixClientManager clientManager;
    private boolean isPasswordVisible;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root) {
        try {
            logger.info("Initializing SignIn stage");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("SignIn");
            //Añadir a la ventana el ícono “FanetixLogo.png”.
            stage.getIcons().add(new Image("eus/tartanga/crud/app/resources/logo.png"));
            stage.setResizable(false);
            clientManager = FanetixClientFactory.getFanetixClientManager();
            adminManager = AdministratorFactory.getAdministratorManager();
            tfPassword.setVisible(false);
            pfPassword.textProperty().addListener(this::textPropertyChange);
            tfPassword.textProperty().addListener(this::textPropertyChange);
            // Manejo de eventos
            // stage.setOnCloseRequest(event -> handleOnActionExit(event));
            // tgbtnEyeIcon.setOnAction(this::togglePasswordVisibility);
            // hypSignUp.setOnAction(this::handlerHyperlink);
            // hypForgotPassword.setOnAction(this::handlerHyperlink);
            stage.show();
            logger.info("Showing the SignIn window.");
        } catch (Exception e) {
            String errorMsg = "Error opening window:\n" + e.getMessage();
            logger.log(Level.SEVERE, errorMsg);
        }
    }

    @FXML
    public void handelAcceptButtonAction(ActionEvent event) {
        FanetixClient user;
        Administrator admin = null;
        String email = this.tfEmail.getText();
        String passwrd = this.pfPassword.getText();
        try {
            logger.info("Handeling the accept button.");
            user = clientManager.signIn_XML(new GenericType<FanetixClient>() {
            }, email, passwrd);
            //Sign in falseado
            if (user == null) {
                admin = adminManager.signIn_XML(new GenericType<Administrator>() {
                }, email, passwrd);
            }
            //Sign in falseado
            if (user != null || admin != null) {
                /*FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ProfileView.fxml"));
                    FanetixUser userGeneral = new FanetixUser(email, passwrd);
                    MenuBarViewController.setUser(userGeneral);
                    Parent root = (Parent) loader.load();
                    ProfileViewController controller = (ProfileViewController) loader.getController();
                    controller.setStage(stage);
                    controller.initStage(root);*/
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ArtistView.fxml"));
                FanetixUser userGeneral = new FanetixUser(email, passwrd);
                MenuBarViewController.setStageMenu(stage);
                MenuBarViewController.setUser(userGeneral);
                Parent root = (Parent) loader.load();
                ArtistViewController controller = (ArtistViewController) loader.getController();
                controller.setStage(stage);
                controller.initStage(root);
                /*FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ProductView.fxml"));
                FanetixUser userGeneral = new FanetixUser(email, passwrd);
                MenuBarViewController.setStageMenu(stage);
                MenuBarViewController.setUser(userGeneral);
                Parent root = (Parent) loader.load();
                ProductViewController controller = (ProductViewController) loader.getController();
                controller.setStage(stage);
                controller.initStage(root);*/

            }
        } catch (ReadException e) {
            new Alert(Alert.AlertType.ERROR, "At this moment server is not available. Please try later.", ButtonType.OK).showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(SignInViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

// Mostrar/ocultar la contraseña
    private void textPropertyChange(ObservableValue observable, String oldValue, String newValue) {
        if (pfPassword.isVisible()) {
            tfPassword.setText(pfPassword.getText());
        } else {
            pfPassword.setText(tfPassword.getText());
        }
    }
    /*
    // Manejo del evento de cierre de ventana
    private void handleOnActionExit(javafx.stage.WindowEvent event) {
        logger.info("Closing SignIn window...");
        stage.close();
    }
    // Manejo de los hipervínculos
    private void handlerHyperlink(ActionEvent event) {
        if (event.getSource() == hypSignUp) {
            logger.info("Redirecting to Sign Up...");
            //navigateToSignUp();
        } else if (event.getSource() == hypForgotPassword) {
            logger.info("Redirecting to Forgot Password...");
            // navigateToForgotPassword();
        }
    }
    // Mostrar una alerta
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }*/
}
