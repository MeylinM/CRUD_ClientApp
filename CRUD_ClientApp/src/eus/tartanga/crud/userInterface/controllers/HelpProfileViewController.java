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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Irati
 */
public class HelpProfileViewController{

   
    /**
     * El control que muestra la página de ayuda.
     */
    @FXML
    private WebView webView;
    
    /**
     * Inicializa y muestra la ventana de ayuda.
     * @param root La jerarquía raíz del documento FXML. 
     */
    public void initAndShowStage(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Establece que la ventana será modal
        stage.setScene(scene);
        stage.setTitle("Ayuda para el perfil de usuarios de Fanetix");
        stage.setResizable(false);  // La ventana no es redimensionable
        stage.setMinWidth(800);  // Establece el tamaño mínimo de la ventana
        stage.setMinHeight(600);
        stage.setOnShowing(this::handleWindowShowing); // Define el comportamiento cuando la ventana se muestra
        stage.show();
    }
    
    /**
     * Inicializa el estado de la ventana. Implementa el comportamiento para el evento de tipo WINDOW_SHOWING.
     * Este método se llama cuando la ventana de ayuda para el administrador se está mostrando.
     * @param event El evento de la ventana
     */
    private void handleWindowShowing(WindowEvent event){
        WebEngine webEngine = webView.getEngine();
        // Carga la página de ayuda para el administrador
        webEngine.load(getClass()
                .getResource("/eus/tartanga/crud/userInterface/html/helpProfile.html").toExternalForm());
    }
}
