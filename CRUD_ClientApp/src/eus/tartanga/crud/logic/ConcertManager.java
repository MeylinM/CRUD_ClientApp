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
import javax.ws.rs.core.GenericType;

/**
 *
 * @author 2dam
 */
public interface ConcertManager {

    public void removeConcert(String id) throws DeleteException;

    public <T> T findComingSoon_XML(Class<T> responseType) throws ReadException;

    public <T> T findComingSoon_JSON(Class<T> responseType) throws ReadException;

    public <T> T findBetweenDates_XML(Class<T> responseType, String startDate, String endDate) throws ReadException;

    public <T> T findBetweenDates_JSON(Class<T> responseType, String startDate, String endDate) throws ReadException;

    public void updateConcert_XML(Object requestEntity, String id) throws UpdateException;

    public void updateConcert_JSON(Object requestEntity, String id) throws UpdateException;

    public <T> T searchByTerm_XML(Class<T> responseType, String searchTerm) throws ReadException;

    public <T> T searchByTerm_JSON(Class<T> responseType, String searchTerm) throws ReadException;

    public void createConcert_XML(Object requestEntity) throws AddException;

    public void createConcert_JSON(Object requestEntity) throws AddException;

    public <T> T findConcert_XML(Class<T> responseType, String id) throws ReadException;

    public <T> T findConcert_JSON(Class<T> responseType, String id) throws ReadException;

    public <T> T findAllConcerts_XML(GenericType<T> responseType) throws ReadException;

    public <T> T findAllConcerts_JSON(Class<T> responseType) throws ReadException;

    public void close();
}
