/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;

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
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private TextField tfEmail;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private TextField tfPassword;
    @FXML
    private ToggleButton tgbtnEyeIcon;
    @FXML
    private ImageView ivEyeIcon;
    @FXML
    private Button btnAccept;
    @FXML
    private Hyperlink hypSignUp;
    @FXML
    private Hyperlink hypForgotPassword;
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
            tgbtnEyeIcon.setOnAction(this::togglePasswordVisibility);

            hypSignUp.setOnAction(this::handlerSignUpHyperlink);
            hypForgotPassword.setOnAction(this::handlerForgotPasswdHyperlink);

            stage.setOnCloseRequest(event -> handleOnActionExit(event));
            System.out.println("aqui llega 1");
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
            if (user != null || admin != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ArtistView.fxml"));
                FanetixUser userGeneral = new FanetixUser(email, passwrd);
                MenuBarViewController.setStageMenu(stage);
                MenuBarViewController.setUser(userGeneral);
                Parent root = (Parent) loader.load();
                ArtistViewController controller = (ArtistViewController) loader.getController();
                controller.setStage(stage);
                controller.initStage(root);

            }
        } catch (SignInException e) {
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

    @FXML
    private void togglePasswordVisibility(ActionEvent event) {
        if (tgbtnEyeIcon.isSelected()) {
            pfPassword.setVisible(false); // Hide the PasswordField
            tfPassword.setVisible(true);  // Show the TextField (password visible)
            // Change icon to "password visible"
            ivEyeIcon.setImage(new Image("/eus/tartanga/crud/app/resources/HidePasswd.png"));
        } else {
            tfPassword.setVisible(false); // Show the PasswordField
            pfPassword.setVisible(true);  //Hide the TextField (password visible)
            // Change icon to "password hidden"
            ivEyeIcon.setImage(new Image("/eus/tartanga/crud/app/resources/ShowPasswd.png"));
        }
    }

    @FXML
    private void handlerSignUpHyperlink(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/SignUpView.fxml"));
            Parent root = (Parent) loader.load();
            SignUpViewController controller = (SignUpViewController) loader.getController();
            //Stage modalStage = new Stage();
            controller.setStage(stage);
            controller.initStage(root);
        } catch (IOException ex) {
            Logger.getLogger(SignInViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void handlerForgotPasswdHyperlink(ActionEvent event) {

        showAlert("Server error", "At this moment this opction is not available", INFORMATION);
        /*
            TextInputDialog tidResetPass = new TextInputDialog();
            tidResetPass.setHeaderText("Enter your email.");
            Optional<String> result = tidResetPass.showAndWait();
            if (result.isPresent()) {
               clientManager.resetPasswd(result.get());
            }
            showAlert("Email", "An email with the recovery password was sent to: " + result.get(), INFORMATION);*/

    }

    // Manejo del evento de cierre de ventana
    private void handleOnActionExit(javafx.stage.WindowEvent event) {
        logger.info("Closing SignIn window...");
        stage.close();
    }

    // Mostrar una alerta
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
