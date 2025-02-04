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
 * @author 2dam
 */
public interface ProductManager {
    
    public <T> T findStock_XML(GenericType<T> responseType) throws WebApplicationException;

    public <T> T findStock_JSON(GenericType<T> responseType) throws WebApplicationException ;

    public void edit_XML(Object requestEntity, String id) throws UpdateException ;

    public void edit_JSON(Object requestEntity, String id) throws UpdateException ;

    public <T> T findBetweenDates_XML(GenericType<T> responseType, String startDate, String endDate) throws WebApplicationException;

    public <T> T findBetweenDates_JSON(GenericType<T> responseType, String startDate, String endDate) throws WebApplicationException;

    public <T> T find_XML(GenericType<T> responseType, String id) throws ReadException ;
    
    public <T> T find_JSON(GenericType<T> responseType, String id) throws ReadException;

    public void create_XML(Object requestEntity) throws AddException ;

    public void create_JSON(Object requestEntity) throws AddException;

    public <T> T searchByTerm_XML(GenericType<T> responseType, String searchTerm) throws WebApplicationException;

    public <T> T searchByTerm_JSON(GenericType<T> responseType, String searchTerm) throws WebApplicationException ;

    public <T> T findAll_XML(GenericType<T> responseType) throws ReadException ;

    public <T> T findAll_JSON(GenericType<T> responseType) throws ReadException ;

    public void remove(String id) throws DeleteException;
}
