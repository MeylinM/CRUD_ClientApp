/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.logic;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author 2dam
 */
public interface ConcertManager {

    public void removeConcert(String id) throws ClientErrorException;

    public <T> T findComingSoon_XML(Class<T> responseType) throws ClientErrorException;

    public <T> T findComingSoon_JSON(Class<T> responseType) throws ClientErrorException;

    public <T> T findBetweenDates_XML(Class<T> responseType, String startDate, String endDate) throws ClientErrorException;

    public <T> T findBetweenDates_JSON(Class<T> responseType, String startDate, String endDate) throws ClientErrorException;

    public void updateConcert_XML(Object requestEntity, String id) throws ClientErrorException;

    public void updateConcert_JSON(Object requestEntity, String id) throws ClientErrorException;

    public <T> T searchByTerm_XML(Class<T> responseType, String searchTerm) throws ClientErrorException;

    public <T> T searchByTerm_JSON(Class<T> responseType, String searchTerm) throws ClientErrorException;

    public void createConcert_XML(Object requestEntity) throws ClientErrorException;

    public void createConcert_JSON(Object requestEntity) throws ClientErrorException;

    public <T> T findConcert_XML(Class<T> responseType, String id) throws ClientErrorException;

    public <T> T findConcert_JSON(Class<T> responseType, String id) throws ClientErrorException;

    public <T> T findAllConcerts_XML(GenericType<T> responseType) throws ClientErrorException;

    public <T> T findAllConcerts_JSON(Class<T> responseType) throws ClientErrorException;

    public void close();
}
