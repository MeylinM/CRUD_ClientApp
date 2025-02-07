/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.logic;

/**
 * Clase {@code AdministratorFactory} que proporciona una instancia única de {@link AdministratorManager}.
 * Si no existe, crea una nueva instancia de {@link AdministratorRest}.
 * @author meyli
 */
public class AdministratorFactory {

    private static AdministratorManager administratorManager;

    public static AdministratorManager getAdministratorManager() {
        if (administratorManager == null) {
            administratorManager = new AdministratorRest();
        }
        return administratorManager;
    }
}
