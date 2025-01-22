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
 * @author 2dam
 */
public interface ProductManager {
    
    public <T> T findStock_XML(GenericType<T> responseType) throws WebApplicationException ;

    public void edit_XML(Object requestEntity, String id) throws WebApplicationException ;

    public <T> T findBetweenDates_XML(Class<T> responseType, String startDate, String endDate) throws WebApplicationException ;

    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException ;

    public void create_XML(Object requestEntity) throws WebApplicationException ;

    public <T> T searchByTerm_XML(Class<T> responseType, String searchTerm) throws WebApplicationException ;

    public <T> T findAll_XML(GenericType<T> responseType) throws WebApplicationException ;

    public void remove(String id) throws WebApplicationException ;
}
