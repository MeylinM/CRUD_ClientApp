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
import java.util.List;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author 2dam
 */
public class CRUD_ClientApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ReadException {
        //launch(args); ayudajjjs
        
        
        List<Cart> carts = CartFactory.getCartManager().findAllBoughtProducts_XML(new GenericType<List<Cart>>() {});
        for(Cart c : carts){
            System.out.println("------------------------------");
            System.out.println(c.getClient().getFullName());
            System.out.println(c.getOrderDate());
            System.out.println(c.getProduct().getTitle());
        }
        
    }
    
}
