/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.app;

import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.userInterface.controllers.ConcertViewController;
import eus.tartanga.crud.userInterface.controllers.SignUpViewController;
import eus.tartanga.crud.userInterface.controllers.ProductViewController;
import eus.tartanga.crud.userInterface.controllers.SignInViewController;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Main class for the CRUD Client application. 
 * Extends {javafx.application.Application} to launch the graphical user interface.
 * 
 * <p>This class loads the sign-in view and manages the initialization
 * of the application.</p>
 * 
 * @author Meylin, Elbire, Olaia and Irati
 */
public class CRUD_ClientApp extends Application {

    /**
     * The main entry point of the application. 
     * Loads the sign-in view from the FXML file and sets up the controller.
     *
     * @param stage The primary stage of the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/SignInView.fxml"));
        Parent root = (Parent) loader.load();
        
        // Get the sign-in view controller and initialize it
        SignInViewController controller = ((SignInViewController) loader.getController());
        controller.setStage(stage);
        controller.initStage(root);
    }

    /**
     * The main method of the application. 
     * Launches the JavaFX application.
     *
     * @param args Command-line arguments.
     * @throws ReadException If an error occurs while reading the initial application data.
     */
    public static void main(String[] args) throws ReadException {
        launch(args);
    }
}
