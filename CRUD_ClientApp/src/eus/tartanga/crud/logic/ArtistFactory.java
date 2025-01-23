/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.logic;

/**
 *
 * @author olaia
 */
public class ArtistFactory {
    private static ArtistManager artistManager;
    
    public static ArtistManager getArtistManager(){
        if (artistManager == null){
            artistManager = new ArtistClientRest();
        }
            return artistManager;
    }
    
}
