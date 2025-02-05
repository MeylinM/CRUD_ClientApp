package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.encrypt.AsymmetricalClient;
import eus.tartanga.crud.exception.EncryptException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.logic.FanetixClientFactory;
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
    private FanetixUser loggedUser = MenuBarViewController.getLoggedUser();  // Suponiendo que tienes el usuario logueado

    // Método para establecer el escenario de la ventana
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Método para inicializar la ventana de perfil
    public void initStage() {
        try {
            logger.info("Initializing profile stage");
            // Aquí puedes agregar cualquier lógica adicional si es necesario
        } catch (Exception e) {
            String errorMsg = "Error: " + e.getMessage();
            logger.severe(errorMsg);
        }
    }
/*
    // Método para manejar la acción de guardar
    @FXML
    public void handleSave(ActionEvent event) {

        try {
            String oldPassword = oldPasswd.getText();
            String newPassword = newPasswd.getText();
            String repeatPassword = repeatPasswd.getText();
            
            // Verificar que las contraseñas nuevas coinciden
            if (!newPassword.equals(repeatPassword)) {
                showAlert(AlertType.ERROR, "Error", "Las contraseñas nuevas no coinciden.");
                return;
            }
            
            // Encriptar la contraseña antigua
            byte[] encryptedOldPasswordBytes = asymmetricalClient.encyptedData(oldPassword);
            
            // Convertir el arreglo de bytes a una cadena Base64
            String encryptedOldPassword = Base64.getEncoder().encodeToString(encryptedOldPasswordBytes);
            
            // Verificar que la contraseña antigua sea correcta
            // = asymmetricalClient.encyptedData(encryptedOldPassword);  // Encriptamos la contraseña antigua
            /*
            try {
                // Suponiendo que FanetixClient tiene un método para validar la contraseña
                boolean isOldPasswordCorrect = FanetixClientFactory.getFanetixClientManager().findClient_XML(loggedUser.getEmail());
                if (!isOldPasswordCorrect) {
                    showAlert(AlertType.ERROR, "Error", "La contraseña antigua es incorrecta.");
                    return;
                }
                
                // Encriptamos la nueva contraseña
                String encryptedNewPassword = asymmetricalClient.encyptedData(newPassword);
                
                // Actualizar la contraseña en la base de datos
                boolean isPasswordUpdated = FanetixClientFactory.getFanetixClientManager().updatePassword(loggedUser.getEmail(), encryptedNewPassword);
                if (isPasswordUpdated) {
                    showAlert(AlertType.INFORMATION, "Éxito", "Contraseña cambiada con éxito.");
                    // Cerrar la ventana o hacer algún otro proceso si es necesario
                    stage.close();
                } else {
                    showAlert(AlertType.ERROR, "Error", "Hubo un problema al cambiar la contraseña.");
                }
                
            } catch (ReadException e) {
                logger.log(Level.SEVERE, "Error al validar o actualizar la contraseña", e);
                showAlert(AlertType.ERROR, "Error", "Error en la comunicación con la base de datos.");
            }
        } catch (EncryptException ex) {
            Logger.getLogger(ChangePasswdViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Método para mostrar alertas
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }*/
}
