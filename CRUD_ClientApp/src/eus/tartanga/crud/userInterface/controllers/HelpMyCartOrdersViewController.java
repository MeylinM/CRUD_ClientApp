/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Controller class for help window . It shows a help page that explains how to
 * use the user's data management window. The view is defined in Help.fmxl file
 * and the help page is help.html.
 *
 * @author javi
 */
public class HelpMyCartOrdersViewController {

    /**
     * The control that shows the help page.
     */
    @FXML
    private WebView webView;

    /**
     * Initializes and show the help window.
     *
     * @param root The FXML document hierarchy root.
     */
    public void initAndShowStageCart(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Cart Help");
        //Añadir a la ventana el ícono “FanetixLogo.png”.
        stage.getIcons().add(new Image("eus/tartanga/crud/app/resources/logo.png"));
        stage.setResizable(false);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setOnShowing(this::handleWindowShowingCart);
        stage.show();
    }

    public void initAndShowStageOrder(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Orders Help");
        //Añadir a la ventana el ícono “FanetixLogo.png”.
        stage.getIcons().add(new Image("eus/tartanga/crud/app/resources/logo.png"));
        stage.setResizable(false);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setOnShowing(this::handleWindowShowingOrder);
        stage.show();
    }

    /**
     * Initializes window state. It implements behavior for WINDOW_SHOWING type
     * event.
     *
     * @param event The window event
     */
    private void handleWindowShowingCart(WindowEvent event) {
        WebEngine webEngine = webView.getEngine();
        // Verificación antes de cargar la página
        System.out.println("Loading help page for cart...");
        webEngine.load(getClass()
                .getResource("/eus/tartanga/crud/userInterface/html/helpMyCart.html").toExternalForm());
    }

    private void handleWindowShowingOrder(WindowEvent event) {
        WebEngine webEngine = webView.getEngine();
        // Verificación antes de cargar la página
        System.out.println("Loading help page for orders...");
        webEngine.load(getClass()
                .getResource("/eus/tartanga/crud/userInterface/html/helpMyOrders.html").toExternalForm());
    }

}
