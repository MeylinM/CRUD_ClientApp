/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.logic;

import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.DeleteException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.exception.UpdateException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author olaia
 */
public interface ArtistManager {
    
  public <T> T findAllArtist(GenericType<T> responseType) throws ReadException ;

    public void updateArtist(Object requestEntity, String id) throws UpdateException ;

    public void createArtist(Object requestEntity) throws AddException ;

    public void removeArtist(String id) throws DeleteException ;

    public <T> T searchByTerm_XML(Class<T> responseType, String searchTerm) throws ReadException ;

    public <T> T findArtist(Class<T> responseType, String id) throws ReadException ;

    public <T> T ArtistFindBetweenDates_XML(Class<T> responseType, String startDate, String endDate) throws ReadException ;
}
