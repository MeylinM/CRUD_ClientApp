package eus.tartanga.crud.userInterface.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This class manages the help window functionality for displaying help content
 * related to artists in the store's interface. It provides different help pages
 * based on whether the user is an administrator or a client.
 * <p>
 * It is responsible for initializing and showing the help window, loading the
 * appropriate help page in a WebView, and setting up the window's appearance and behavior.
 * </p>
 * 
 * @author olaia
 */
public class HelpArtistController {

    /**
     * The WebView control that displays the help page.
     */
    @FXML
    private WebView webView;

    /**
     * Initializes and displays the help window for administrators.
     * <p>
     * This method sets up the scene, the window's properties, and displays a 
     * modal window with the relevant help page for the admin.
     * </p>
     *
     * @param root The root of the FXML document hierarchy.
     */
    public void initAndShowStageAdmin(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Help for Product Management in the Store");
        stage.getIcons().add(new Image("eus/tartanga/crud/app/resources/logo.png"));
        stage.setResizable(false);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setOnShowing(this::handleWindowShowingAdmin);
        stage.show();
    }

    /**
     * Initializes and displays the help window for clients.
     * <p>
     * This method sets up the scene, the window's properties, and displays a 
     * modal window with the relevant help page for the client.
     * </p>
     *
     * @param root The root of the FXML document hierarchy.
     */
    public void initAndShowStageClient(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Help for Artist Management in the Store");
        stage.getIcons().add(new Image("eus/tartanga/crud/app/resources/logo.png"));
        stage.setResizable(false);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setOnShowing(this::handleWindowShowingClient);
        stage.show();
    }

    /**
     * Initializes the window state and handles the "WINDOW_SHOWING" event for administrators.
     * <p>
     * This method loads the help page for administrators when the window is about to be shown.
     * </p>
     *
     * @param event The window event triggered when the window is about to be shown.
     */
    private void handleWindowShowingAdmin(WindowEvent event) {
        WebEngine webEngine = webView.getEngine();
        // Load the help page for administrators
        webEngine.load(getClass()
                .getResource("/eus/tartanga/crud/userInterface/html/helpArtistAdmin.html").toExternalForm());
    }

    /**
     * Initializes the window state and handles the "WINDOW_SHOWING" event for clients.
     * <p>
     * This method loads the help page for clients when the window is about to be shown.
     * </p>
     *
     * @param event The window event triggered when the window is about to be shown.
     */
    private void handleWindowShowingClient(WindowEvent event) {
        WebEngine webEngine = webView.getEngine();
        // Load the help page for clients
        webEngine.load(getClass()
                .getResource("/eus/tartanga/crud/userInterface/html/helpArtistClient.html").toExternalForm());
    }
}
