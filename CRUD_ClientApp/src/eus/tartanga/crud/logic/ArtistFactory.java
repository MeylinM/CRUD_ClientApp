/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.logic;

/**
 * Factory class for managing the {@link ArtistManager} instance.
 * 
 * <p>This class follows the Singleton pattern to ensure that only one 
 * instance of {ArtistManager} is created and used throughout the application.</p>
 * 
 * <p><b>Usage Example:</b></p>
 * <pre>
 *     ArtistManager manager = ArtistFactory.getArtistManager();
 *     manager.createArtist(artist);
 * </pre>
 * 
 * @author olaia
 */
public class ArtistFactory {
    
    /** Singleton instance of {@link ArtistManager}. */
    private static ArtistManager artistManager;

    /**
     * Returns the singleton instance of {ArtistManager}.
     * If it does not exist, a new instance of {ArtistClientRest} is created.
     *
     * @return The {ArtistManager} instance.
     */
    public static ArtistManager getArtistManager() {
        if (artistManager == null) {
            artistManager = new ArtistClientRest();
        }
        return artistManager;
    }
}
