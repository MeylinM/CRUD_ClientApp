/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.exception.DeleteException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.exception.SignInException;
import eus.tartanga.crud.logic.AdministratorFactory;
import eus.tartanga.crud.logic.AdministratorManager;
import eus.tartanga.crud.logic.FanetixClientFactory;
import eus.tartanga.crud.logic.FanetixClientManager;
import eus.tartanga.crud.model.Administrator;
import eus.tartanga.crud.model.FanetixClient;
import eus.tartanga.crud.model.FanetixUser;
import eus.tartanga.crud.model.Product;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;

/**
 * FXML Controller class
 *
 * @author Elbire
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
    private Button changePasswd;

    @FXML
    private TextField dateField;

    @FXML
    private Label lbDate;

    @FXML
    private Label lbName;

    @FXML
    private Label lbPhone;

    @FXML
    private Label lbCity;

    @FXML
    private Label lbStreet;

    @FXML
    private Label lbZIP;

    @FXML
    private Button btnInfo;

    private Stage stage;
    private Logger logger = Logger.getLogger(ProfileViewController.class.getName());
    private ContextMenu contextMenuInside;
    private ContextMenu contextMenuOutside;

    private FanetixClientManager clientManager;
    private FanetixClient client;
    private AdministratorManager adminManager;
    private Administrator admin;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller class.
     *
     * @param root El nodo raíz de la escena.
     */
    public void initStage(Parent root) {
        try {
            logger.info("Initializizng profile stage");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Profile");
            //Añadir a la ventana el ícono “FanetixLogo.png”.
            stage.getIcons().add(new Image("eus/tartanga/crud/app/resources/logo.png"));
            stage.setResizable(false);
            stage.show();
            fullNameField.setEditable(false);
            emailField.setEditable(false);
            passwordField.setEditable(false);
            phoneField.setEditable(false);
            cityField.setEditable(false);
            streetField.setEditable(false);
            postalCodeField.setEditable(false);
            dateField.setEditable(false);
            FanetixUser user = MenuBarViewController.getLoggedUser();
            clientManager = FanetixClientFactory.getFanetixClientManager();
            adminManager = AdministratorFactory.getAdministratorManager();
            findClient(user.getEmail());
            changePasswd.setOnAction(this::handleChangePasswd);
            btnInfo.setOnAction(this::handleInfoButton);
        } catch (Exception e) {
            String errorMsg = "Error" + e.getMessage();
            logger.severe(errorMsg);
        }
    }

    private void handleChangePasswd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ChangePasswdView.fxml"));
            Parent root = (Parent) loader.load();
            ChangePasswdViewController controller = (ChangePasswdViewController) loader.getController();
            controller.setStage(stage);
            //  controller.initStage(root);
        } catch (IOException ex) {
            Logger.getLogger(ProfileViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void findClient(String email) {
        try {
            client = clientManager.findClient_XML(new GenericType<FanetixClient>() {
            }, email);
            if (client != null) {
                loadClientData(client);
            } else {
                admin = adminManager.find(new GenericType<Administrator>() {
                }, email);
                loadAdminData(admin);
            }
        } catch (ReadException ex) {
            Logger.getLogger(ProfileViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadAdminData(Administrator admin) {
        // Mostrar solo email, password e incorporation date
        emailField.setText(admin.getEmail());
        passwordField.setText(admin.getPasswd());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateField.setText(sdf.format(admin.getIncorporationDate()));

        // Ocultar todos los demás campos excepto los requeridos
        fullNameField.setVisible(false);
        phoneField.setVisible(false);
        cityField.setVisible(false);
        streetField.setVisible(false);
        postalCodeField.setVisible(false);

        lbName.setVisible(false);
        lbPhone.setVisible(false);
        lbCity.setVisible(false);
        lbStreet.setVisible(false);
        lbZIP.setVisible(false);

    }

    private void loadClientData(FanetixClient client) {
        if (client != null) {
            dateField.setVisible(false);
            lbDate.setVisible(false);
            System.out.println(client.getFullName());
            fullNameField.setText(client.getFullName());
            emailField.setText(client.getEmail());
            phoneField.setText(String.valueOf(client.getMobile()));
            cityField.setText(client.getCity());
            streetField.setText(client.getStreet());
            postalCodeField.setText(String.valueOf(client.getZip()));
            passwordField.setText(client.getPasswd());
        } else {
            logger.severe("El cliente es nulo");
        }

    }

    private void handleInfoButton(ActionEvent event) {
        try {
            FXMLLoader loader
                    = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/HelpProfileView.fxml"));
            Parent root = (Parent) loader.load();
            HelpProfileViewController helpController
                    = ((HelpProfileViewController) loader.getController());
            // Inicializa y muestra la ventana de ayuda
            helpController.initAndShowStage(root);
        } catch (IOException ex) {
            logger.warning("Unable to load the help");
        }
    }
}
