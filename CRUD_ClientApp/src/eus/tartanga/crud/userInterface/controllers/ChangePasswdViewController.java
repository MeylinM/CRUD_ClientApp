package eus.tartanga.crud.userInterface.controllers;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
import eus.tartanga.crud.encrypt.AsymmetricalClient;
import eus.tartanga.crud.exception.EncryptException;
import eus.tartanga.crud.exception.SignInException;
import eus.tartanga.crud.exception.UpdateException;
import eus.tartanga.crud.logic.FanetixClientFactory;
import eus.tartanga.crud.logic.FanetixClientManager;
import eus.tartanga.crud.model.FanetixClient;
import eus.tartanga.crud.model.FanetixUser;
import java.util.Base64;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javax.ws.rs.core.GenericType;

public class ChangePasswdViewController {

    @FXML
    private PasswordField oldPasswd;

    @FXML
    private PasswordField newPasswd;

    @FXML
    private PasswordField repeatPasswd;

    @FXML
    private Button btnSave;

    private Stage stage;
    private Logger logger = Logger.getLogger(ChangePasswdViewController.class.getName());
    private AsymmetricalClient asymmetricalClient = new AsymmetricalClient();
    private FanetixUser loggedUser = MenuBarViewController.getLoggedUser();
    private FanetixClientManager clientManager;
    private FanetixClient user;

    // Método para establecer el escenario de la ventana
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root) {
        try {
            logger.info("Initializizng profile stage");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Change password");
            stage.setResizable(false);
            stage.show();
            btnSave.setOnAction(this::handleSave);
        } catch (Exception e) {
            String errorMsg = "Error" + e.getMessage();
            logger.severe(errorMsg);
        }
    }

    public void handleSave(ActionEvent event) {
        String oldPassword = oldPasswd.getText();
        String newPassword = newPasswd.getText();
        String repeatPassword = repeatPasswd.getText();
        byte[] encryptedPassword;
        String encryptedPasswordBase64;

        try {
            // Verificar que las contraseñas nuevas coinciden
            if (!newPassword.equals(repeatPassword)) {
                showAlert(AlertType.ERROR, "Error", "Las contraseñas nuevas no coinciden.");
                return;
            }

            try {
                encryptedPassword = asymmetricalClient.encryptedData(oldPassword);
            } catch (EncryptException ex) {
                LOGGER.severe("Encryption error");
                showAlert(AlertType.ERROR, "Error", "Error encrypting the password. Please try again.");
                return; // Detiene el proceso si la encriptación falla
            }

            // Convertimos la contraseña encriptada a Base64
            encryptedPasswordBase64 = Base64.getEncoder().encodeToString(encryptedPassword);

            // Busca si el usuario existe con la contraseña actual
            clientManager = FanetixClientFactory.getFanetixClientManager();
            FanetixClient authenticatedUser = clientManager.signIn_XML(
                    new GenericType<FanetixClient>() {
            }, loggedUser.getEmail(), encryptedPasswordBase64);

            if (authenticatedUser == null) {
                showAlert(AlertType.ERROR, "Error", "La contraseña antigua es incorrecta.");
                return;
            }

            // Encriptamos la nueva contraseña
            encryptedPassword = asymmetricalClient.encryptedData(newPassword);
            encryptedPasswordBase64 = Base64.getEncoder().encodeToString(encryptedPassword);

            // Creamos el objeto usuario con la nueva contraseña
            user = new FanetixClient(loggedUser.getEmail(), encryptedPasswordBase64);

            // Intentamos actualizar la contraseña
            boolean passwordChanged = false;
            try {
                clientManager.updateClient_XML(user, loggedUser.getEmail());
                passwordChanged = true;
            } catch (UpdateException ex) {
                LOGGER.log(Level.SEVERE, "Hubo un problema al cambiar la contraseña.", ex);
            }

            // Verificamos si la actualización fue exitosa
            if (passwordChanged) {
                showAlert(AlertType.INFORMATION, "Éxito", "Contraseña cambiada con éxito.");
                stage.close();
            } else {
                showAlert(AlertType.ERROR, "Error", "Hubo un problema al cambiar la contraseña.");
            }

        } catch (SignInException ex) {
            LOGGER.log(Level.SEVERE, "Error al validar el usuario", ex);
            showAlert(AlertType.ERROR, "Error", "Error al validar el usuario.");
        } catch (EncryptException ex) {
            LOGGER.log(Level.SEVERE, "Error al encriptar la contraseña", ex);
            showAlert(AlertType.ERROR, "Error", "Error al encriptar la contraseña.");
        }
    }

// Método para mostrar alertas
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
