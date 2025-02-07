/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.logic;

/**
 * Clase {@code FanetixClientFactory} que proporciona una instancia única de {@link FanetixClientManager}.
 * Si no existe, crea una nueva instancia de {@link FanetixClientRest}.
 * @author meyli
 */
public class FanetixClientFactory {
    
    private static FanetixClientManager fanetixClientManager;
    public static FanetixClientManager getFanetixClientManager() {
        if (fanetixClientManager == null) {
            fanetixClientManager = new FanetixClientRest();
        }
        return fanetixClientManager;
    }
}
