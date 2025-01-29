/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.app;

import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.logic.CartFactory;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import eus.tartanga.crud.logic.CartManager;
import eus.tartanga.crud.model.Cart;
import eus.tartanga.crud.userInterface.controllers.ArtistViewController;
import eus.tartanga.crud.userInterface.controllers.CartOrdersViewController;
import eus.tartanga.crud.userInterface.controllers.ProductViewController;
import eus.tartanga.crud.userInterface.controllers.ProfileViewController;
import eus.tartanga.crud.userInterface.controllers.SignInViewController;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author 2dam
 */
public class CRUD_ClientApp extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        
        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/SignInView.fxml"));
        Parent root = (Parent) loader.load();
        //Scene scene = new Scene(root);
        SignInViewController controller = ((SignInViewController) loader.getController());
        controller.setStage(stage);
        controller.initStage(root);
        */
        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ProductView.fxml"));

        Parent root = (Parent) loader.load();
        //Scene scene = new Scene(root);
        ProductViewController controller = ((ProductViewController) loader.getController());
        controller.setStage(stage);
        controller.initStage(root); */
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eus/tartanga/crud/userInterface/views/ProfileView.fxml"));
        Parent root = (Parent) loader.load();
        //Scene scene = new Scene(root);
        ProfileViewController controller = ((ProfileViewController) loader.getController());
        controller.setStage(stage);
        controller.initStage(root);
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ReadException {
        launch(args);
        
        
        /*List<Cart> carts = CartFactory.getCartManager().findAllBoughtProducts_XML(new GenericType<List<Cart>>() {});
        for(Cart c : carts){
            System.out.println("------------------------------");
            System.out.println(c.getClient().getFullName());
            System.out.println(c.getOrderDate());
            System.out.println(c.getProduct().getTitle());
        }*/
        
    }
    
}
