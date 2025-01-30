/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.logic.FanetixClientFactory;
import eus.tartanga.crud.model.FanetixClient;
import eus.tartanga.crud.model.FanetixUser;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;

/**
 * FXML Controller class
 *
 * @author 2dam
 */
public class ChangePasswdViewController {
    
    @FXML
    PasswordField oldPasswd;
    
    @FXML
    PasswordField newPasswd;
    
    @FXML
    PasswordField repeatPasswd;
    
    @FXML
    Button btnSave;
    
    private Stage stage;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    private Logger logger = Logger.getLogger(ChangePasswdViewController.class.getName());
     public void initStage(Parent root) {
        try {
            logger.info("Initializizng profile stage");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Profile");
            stage.setResizable(false);
            stage.show();
            FanetixUser user = MenuBarViewController.getLoggedUser();
            
        } catch (Exception e) {
            String errorMsg = "Error" + e.getMessage();
            logger.severe(errorMsg);
        }
    }
    
  
    
}
