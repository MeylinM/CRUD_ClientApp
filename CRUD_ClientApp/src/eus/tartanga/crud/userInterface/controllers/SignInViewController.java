package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.logic.AdministratorFactory;
import eus.tartanga.crud.logic.AdministratorManager;
import eus.tartanga.crud.logic.FanetixClientFactory;
import eus.tartanga.crud.logic.FanetixClientManager;
import eus.tartanga.crud.model.FanetixClient;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
            stage.setResizable(false);
            tfPassword.setVisible(false);

            pfPassword.textProperty().addListener(this::textPropertyChange);
            tfPassword.textProperty().addListener(this::textPropertyChange);

            // Manejo de eventos
            // stage.setOnCloseRequest(event -> handleOnActionExit(event));
            // tgbtnEyeIcon.setOnAction(this::togglePasswordVisibility);
            // hypSignUp.setOnAction(this::handlerHyperlink);
            // hypForgotPassword.setOnAction(this::handlerHyperlink);
            //  adminManager = AdministratorFactory.getAdministratorManager();
            // clientManager = FanetixClientFactory.getFanetixClientManager();
            stage.show();
            logger.info("Showing the SignIn window.");
        } catch (Exception e) {
            String errorMsg = "Error opening window:\n" + e.getMessage();
            logger.log(Level.SEVERE, errorMsg);
        }
    }

    @FXML
    public void handelAcceptButtonAction(ActionEvent event) {
        String email = this.tfEmail.getText().trim();
        String passwrd = this.pfPassword.getText().trim();

        try {
            logger.info("Handeling the accept button.");
            FanetixClient user = new FanetixClient(email, passwrd);
            //Sign in falseado
            if (email.equals("client@gmail.com") && passwrd.equals("abcd*1234")) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ProfileView.fxml"));

                Parent root = (Parent) loader.load();
                ProfileViewController controller = (ProfileViewController) loader.getController();

                controller.setStage(stage);
                controller.initStage(root, user);
            }
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
