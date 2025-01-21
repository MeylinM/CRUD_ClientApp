/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;

import eus.tartanga.crud.model.FanetixUser;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    public void openProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ProfileView.fxml"));
            Parent profileRoot = loader.load();

            ProfileViewController controller = (ProfileViewController) loader.getController();
            //controller.setStage(stageMenu);
            //controller.initStage(root, loggedUser); Creo que todas tenemos que tener nuestros controladores para probar esto luego
        } catch (IOException e) {
            e.printStackTrace();
            Logger.getLogger(MenuBarViewController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void openMyOrders() {

    }

    public void logOut() {

    }

    public void openArtist() {

    }

    public void openMyCart() {

    }

    private void showHelpArtist() {

    }

    private void showHelpConcert() {
    }

    private void showHelpProduct() {
    }

    private void showHelpCart() {
    }

    private void showHelpOrder() {
    }

}
