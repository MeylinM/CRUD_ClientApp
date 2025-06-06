/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.app;

import eus.tartanga.crud.exception.ReadException;
import javafx.application.Application;
import eus.tartanga.crud.userInterface.controllers.ConcertController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 *
 * @author 2dam
 */
public class CRUD_ClientApp extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ConcertView.fxml"));
        Parent root = (Parent) loader.load();
        //Scene scene = new Scene(root);
        ConcertController controller = ((ConcertController) loader.getController());
        controller.setStage(stage);
        controller.initStage(root);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ReadException, IOException{
        launch(args);
        
        
    }
    
}
