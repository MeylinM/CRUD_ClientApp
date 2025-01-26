/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.logic.FanetixClientFactory;
import eus.tartanga.crud.logic.FanetixClientManager;
import eus.tartanga.crud.model.FanetixClient;
import eus.tartanga.crud.model.Product;
import java.util.List;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;

/**
 * FXML Controller class
 *
 * @author 2dam
 */
public class ProfileViewController {

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField streetField;

    @FXML
    private TextField postalCodeField;

    @FXML
    private Button changePasswordButton;

    @FXML
    private Label statusLabel;

    private Stage stage;
    private Logger logger = Logger.getLogger(ProfileViewController.class.getName());
    private ContextMenu contextMenuInside;
    private ContextMenu contextMenuOutside;

    private FanetixClientManager clientManager;
    private FanetixClient client;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller class.
     *
     * @param root
     * @param user
     */
    public void initStage(Parent root, FanetixClient user) {
        try {
            logger.info("Initializizng profile stage");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Profile");
            stage.setResizable(false);
            fullNameField.setEditable(false);
            emailField.setEditable(false);
            passwordField.setEditable(false);
            phoneField.setEditable(false);
            cityField.setEditable(false);
            streetField.setEditable(false);
            postalCodeField.setEditable(false);
            stage.show();

            String email = user.getEmail();
            System.out.println(email);

            findClient(email);

        } catch (Exception e) {
            String errorMsg = "Error" + e.getMessage();
            logger.severe(errorMsg);
        }
    }

    private FanetixClient findClient(String email) {
        clientManager = FanetixClientFactory.getFanetixClientManager();
        client = clientManager.findClient_XML(new GenericType<FanetixClient>() {
        }, email); //esto no funciona RESIVAR EL SERVIDOR NECESITO LA ULTIMA VERSION!!
        loadClientData(client);
        return client;

    }

    private void loadClientData(FanetixClient client) {
        fullNameField.setText(client.getFullName());
        emailField.setText(client.getEmail());
        phoneField.setText(String.valueOf(client.getMobile()));
        cityField.setText(client.getCity());
        streetField.setText(client.getStreet());
        postalCodeField.setText(String.valueOf(client.getZip()));

    }
}
