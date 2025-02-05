/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.app;

import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.userInterface.controllers.ConcertViewController;
import eus.tartanga.crud.userInterface.controllers.SignInViewController;
import eus.tartanga.crud.userInterface.controllers.SignUpViewController;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 *
 * @author 2dam
 */
public class CRUD_ClientApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/SignInView.fxml"));
        Parent root = (Parent) loader.load();
        //Scene scene = new Scene(root);
        SignInViewController controller = ((SignInViewController) loader.getController());
        controller.setStage(stage);
        controller.initStage(root);*/

 /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ConcertView.fxml"));
        Parent root = (Parent) loader.load();
        //Scene scene = new Scene(root);
        ConcertViewController controller = ((ConcertViewController) loader.getController());
        controller.setStage(stage);
        controller.initStage(root); */
 /*FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ProductView.fxml"));
        Parent root = (Parent) loader.load();
        //Scene scene = new Scene(root);
        ProductViewController controller = ((ProductViewController) loader.getController());
        controller.setStage(stage);
        controller.initStage(root); */
 /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ArtistView.fxml"));
        Parent root = (Parent) loader.load();
        //Scene scene = new Scene(root);
        ArtistViewController controller = ((ArtistViewController) loader.getController());
        controller.setStage(stage);
        controller.initStage(root);*/
 
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/SignUpView.fxml"));
        Parent root = (Parent) loader.load();
        //Scene scene = new Scene(root);
        SignUpViewController controller = ((SignUpViewController) loader.getController());
        controller.setStage(stage);
        controller.initStage(root);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ReadException {
        launch(args);
    }

}
