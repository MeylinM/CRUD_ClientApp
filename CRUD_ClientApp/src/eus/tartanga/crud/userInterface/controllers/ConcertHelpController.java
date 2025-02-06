/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.userInterface.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Controller class for help window . 
 * It shows a help page that explains how to use the user's data management window.
 * The view is defined in Help.fmxl file and the help page is help.html.
 *
 * @author javi
 */
public class ConcertHelpController {
    /**
     * The control that shows the help page.
     */
    @FXML
    private WebView webView;
    /**
     * Initializes and show the help window.
     * @param root The FXML document hierarchy root. 
     */
    public void initAndShowStageAdmin(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Ayuda para la Gestion de Conicertos");
        stage.setResizable(false);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setOnShowing(this::handleWindowShowingAdmin);
        stage.show();
    }
    
    public void initAndShowStageClient(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Ayuda para la Gestion de Conicertos");
        stage.setResizable(false);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setOnShowing(this::handleWindowShowingClient);
        stage.show();
    }
    /**
     * Initializes window state. It implements behavior for WINDOW_SHOWING type 
     * event.
     * @param event  The window event 
     */
    private void handleWindowShowingAdmin(WindowEvent event){
        WebEngine webEngine = webView.getEngine();
        //Load help page.eus.tartanga.crud.userInterface.html
        webEngine.load(getClass()
                .getResource("/eus/tartanga/crud/userInterface/html/helpConcertAdmin.html").toExternalForm());
    }
    
    private void handleWindowShowingClient(WindowEvent event){
        WebEngine webEngine = webView.getEngine();
        //Load help page.eus.tartanga.crud.userInterface.html
        webEngine.load(getClass()
                .getResource("/eus/tartanga/crud/userInterface/html/helpConcertClient.html").toExternalForm());
    }
}