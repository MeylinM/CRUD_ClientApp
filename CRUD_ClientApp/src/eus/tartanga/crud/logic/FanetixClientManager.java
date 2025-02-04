/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.logic;

import eus.tartanga.crud.exception.SignInException;
import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.DeleteException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.exception.UpdateException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
/**
 *
 * @author Elbire and Meylin
 */
public interface FanetixClientManager {

    public void createClient_XML(Object requestEntity) throws WebApplicationException;

    public void createClient_JSON(Object requestEntity) throws WebApplicationException; //SignUp

    public void removeClient(String email) throws WebApplicationException;

    public void updateClient_XML(Object requestEntity, String email) throws WebApplicationException;

    public void updateClient_JSON(Object requestEntity, String email) throws WebApplicationException;

    public <T> T signIn_XML(GenericType<T> responseType, String email, String passwd) throws WebApplicationException;

    public <T> T signIn_JSON(GenericType<T> responseType, String email, String passwd) throws WebApplicationException;

    public <T> T findClient_XML(GenericType<T> responseType, String email) throws WebApplicationException;

    public <T> T findClient_JSON(Class<T> responseType, String email) throws WebApplicationException;

    public <T> T findAllClients_XML(Class<T> responseType) throws WebApplicationException;

    public <T> T findAllClients_JSON(Class<T> responseType) throws WebApplicationException;

    public void close();
}
