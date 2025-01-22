/*
 * To change this license header, choose License Headers in Project Properties.
 * To change thsis template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.logic;

/**
 *
 * @author Meylin
 */
public class CartFactory {
    
    public static CartManager getCartManager() {
        return new CartClientRest();
    }
}
