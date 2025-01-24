/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.logic;

import eus.tartanga.crud.exception.SignInException;
import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.DeleteException;
import eus.tartanga.crud.exception.UpdateException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author 2dam
 */
public interface FanetixClientManager {

    public void createClient_XML(Object requestEntity) throws AddException;

    public void createClient_JSON(Object requestEntity) throws AddException; //SignUp

    public void removeClient(String email) throws DeleteException;

    public void updateClient_XML(Object requestEntity, String email) throws UpdateException;

    public void updateClient_JSON(Object requestEntity, String email) throws UpdateException;

    public <T> T signIn_XML(Class<T> responseType, String email, String passwd) throws SignInException;

    public <T> T signIn_JSON(Class<T> responseType, String email, String passwd) throws SignInException;

    public <T> T findClient_XML(GenericType<T> responseType, String email) throws ClientErrorException;

    public <T> T findClient_JSON(Class<T> responseType, String email) throws ClientErrorException;

    public <T> T findAllClients_XML(Class<T> responseType) throws ClientErrorException;

    public <T> T findAllClients_JSON(Class<T> responseType) throws ClientErrorException;

    public void close();
}
