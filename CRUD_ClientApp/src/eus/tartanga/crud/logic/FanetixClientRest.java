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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 * Jersey REST client generated for REST resource:FanetixClientFacadeREST
 * [eus.tartanga.crud.entities.fanetixclient]<br>
 * USAGE:
 * <pre>
 *        FanetixClientRest client = new FanetixClientRest();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Elbire and Meylin
 */
public class FanetixClientRest implements FanetixClientManager{

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/CRUD_ServerApp/api";

    public FanetixClientRest() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("eus.tartanga.crud.entities.fanetixclient");
    }

    /**
     *
     * @param requestEntity
     * @throws AddException
     */
    @Override
    public void createClient_XML(Object requestEntity) throws AddException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    /**
     *
     * @param requestEntity
     * @throws AddException
     */
    @Override
    public void createClient_JSON(Object requestEntity) throws AddException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    /**
     *
     * @param email
     * @throws DeleteException
     */
    @Override
    public void removeClient(String email) throws DeleteException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{email})).request().delete();
    }

    /**
     *
     * @param requestEntity
     * @param email
     * @throws UpdateException
     */
    @Override
    public void updateClient_XML(Object requestEntity, String email) throws UpdateException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{email})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    /**
     *
     * @param requestEntity
     * @param email
     * @throws UpdateException
     */
    @Override
    public void updateClient_JSON(Object requestEntity, String email) throws UpdateException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{email})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    /**
     *
     * @param <T>
     * @param responseType
     * @param email
     * @param passwd
     * @return
     * @throws ReadException
     */
    @Override
    public <T> T signIn_XML(GenericType<T> responseType, String email, String passwd) throws ReadException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("signIn/{0}/{1}", new Object[]{email, passwd}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     *
     * @param <T>
     * @param responseType
     * @param email
     * @param passwd
     * @return
     * @throws ReadException
     */
    @Override
    public <T> T signIn_JSON(GenericType<T> responseType, String email, String passwd) throws ReadException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("signIn/{0}/{1}", new Object[]{email, passwd}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     *
     * @param <T>
     * @param responseType
     * @param email
     * @return
     * @throws ReadException
     */
    @Override
    public <T> T findClient_XML(GenericType<T> responseType, String email) throws ReadException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{email}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     *
     * @param <T>
     * @param responseType
     * @param email
     * @return
     * @throws ReadException
     */
    @Override
    public <T> T findClient_JSON(Class<T> responseType, String email) throws ReadException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{email}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     *
     * @param <T>
     * @param responseType
     * @return
     * @throws ReadException
     */
    @Override
    public <T> T findAllClients_XML(Class<T> responseType) throws ReadException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     *
     * @param <T>
     * @param responseType
     * @return
     * @throws ReadException
     */
    @Override
    public <T> T findAllClients_JSON(Class<T> responseType) throws ReadException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     *
     */
    @Override
    public void close() {
        client.close();
    }

}
