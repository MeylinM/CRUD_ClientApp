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
 * Controlador para la ventana de ayuda.
 * Esta clase maneja la visualización de una página de ayuda que explica cómo usar la ventana de gestión de conciertos del usuario.
 * La vista se define en el archivo Help.fxml y la página de ayuda se encuentra en help.html.
 * 
 * @author Irati
 */
public class ConcertHelpController {
    
    /**
     * El control que muestra la página de ayuda.
     */
    @FXML
    private WebView webView;
    
    /**
     * Inicializa y muestra la ventana de ayuda para el administrador.
     * @param root La jerarquía raíz del documento FXML. 
     */
    public void initAndShowStageAdmin(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Establece que la ventana será modal
        stage.setScene(scene);
        stage.setTitle("Ayuda para la Gestión de Conciertos");
        stage.setResizable(false);  // La ventana no es redimensionable
        stage.setMinWidth(800);  // Establece el tamaño mínimo de la ventana
        stage.setMinHeight(600);
        stage.setOnShowing(this::handleWindowShowingAdmin); // Define el comportamiento cuando la ventana se muestra
        stage.show();
    }
    
    /**
     * Inicializa y muestra la ventana de ayuda para el cliente.
     * @param root La jerarquía raíz del documento FXML. 
     */
    public void initAndShowStageClient(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Establece que la ventana será modal
        stage.setScene(scene);
        stage.setTitle("Ayuda para la Gestión de Conciertos");
        stage.setResizable(false);  // La ventana no es redimensionable
        stage.setMinWidth(800);  // Establece el tamaño mínimo de la ventana
        stage.setMinHeight(600);
        stage.setOnShowing(this::handleWindowShowingClient); // Define el comportamiento cuando la ventana se muestra
        stage.show();
    }
    
    /**
     * Inicializa el estado de la ventana. Implementa el comportamiento para el evento de tipo WINDOW_SHOWING.
     * Este método se llama cuando la ventana de ayuda para el administrador se está mostrando.
     * @param event El evento de la ventana
     */
    private void handleWindowShowingAdmin(WindowEvent event){
        WebEngine webEngine = webView.getEngine();
        // Carga la página de ayuda para el administrador
        webEngine.load(getClass()
                .getResource("/eus/tartanga/crud/userInterface/html/helpConcertAdmin.html").toExternalForm());
    }
    
    /**
     * Inicializa el estado de la ventana. Implementa el comportamiento para el evento de tipo WINDOW_SHOWING.
     * Este método se llama cuando la ventana de ayuda para el cliente se está mostrando.
     * @param event El evento de la ventana
     */
    private void handleWindowShowingClient(WindowEvent event) {
        WebEngine webEngine = webView.getEngine();
        // Carga la página de ayuda para el cliente
        webEngine.load(getClass()
                .getResource("/eus/tartanga/crud/userInterface/html/helpConcertClient.html").toExternalForm());
    }
}
