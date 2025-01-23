/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.logic;

/**
 *
 * @author 2dam
 */
public class ProductFactory {
    
    private static ProductManager productManager;

    public static ProductManager getProductManager() {
        if (productManager == null) {
            productManager = new ProductClientRest();
        }
        return productManager;
    }
}
