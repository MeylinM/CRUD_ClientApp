/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.logic;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author olaia
 */
public interface ArtistManager {
    
  public <T> T findAllArtist(GenericType<T> responseType) throws WebApplicationException ;

    public void updateArtist(Object requestEntity) throws WebApplicationException ;

    public void createArtist(Object requestEntity) throws WebApplicationException ;

    public void removeArtist(String id) throws WebApplicationException ;

    public <T> T searchByTerm_XML(Class<T> responseType, String searchTerm) throws WebApplicationException ;

    public <T> T findArtist(Class<T> responseType, String id) throws WebApplicationException ;

    public <T> T ArtistFindBetweenDates_XML(Class<T> responseType, String startDate, String endDate) throws WebApplicationException ;
}
