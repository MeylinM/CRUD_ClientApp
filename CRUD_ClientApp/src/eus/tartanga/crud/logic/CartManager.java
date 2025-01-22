/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open tshe template in the editor.
 */
package eus.tartanga.crud.logic;

import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.DeleteException;
import eus.tartanga.crud.exception.UpdateException;
import eus.tartanga.crud.exception.ReadException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author 2dam
 */
 public interface CartManager {

    public <T> T findByArtist_XML(Class<T> responseType, String artistName) throws ReadException;

    public <T> T findByArtist_JSON(Class<T> responseType, String artistName) throws ReadException;

    public <T> T findAllNotBoughtProducts_XML(Class<T> responseType) throws ReadException;

    public <T> T findAllNotBoughtProducts_JSON(Class<T> responseType) throws ReadException;

    public <T> T findCart_XML(Class<T> responseType, String email, String productId) throws ReadException;

    public <T> T findCart_JSON(Class<T> responseType, String email, String productId) throws ReadException;

    public <T> T findAllBoughtProducts_XML(GenericType<T> responseType) throws ReadException;

    public <T> T findAllBoughtProducts_JSON(GenericType<T> responseType) throws ReadException;

    public void removeCart(String email, String productId) throws DeleteException;

    public void addToCart(Object requestEntity) throws AddException;

    public void updateCart_XML(Object requestEntity, String email, String productId) throws UpdateException;

    public void updateCart_JSON(Object requestEntity, String email, String productId) throws UpdateException;

    public <T> T findAllCartProducts_XML(Class<T> responseType) throws ReadException;

    public <T> T findAllCartProducts_JSON(Class<T> responseType) throws ReadException;

    public <T> T findBetweenDate_XML(Class<T> responseType, String startDate, String endDate) throws ReadException;

    public <T> T findBetweenDate_JSON(Class<T> responseType, String startDate, String endDate) throws ReadException;

    public void close();

}
