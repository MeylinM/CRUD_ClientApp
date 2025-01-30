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
public class ConcertFactory {
    private static ConcertManager concertManager;
    
    public static ConcertManager getConcertManager() {
        if (concertManager == null) {
            concertManager = new ConcertClientRest();
        }
        return concertManager;
    }
}
