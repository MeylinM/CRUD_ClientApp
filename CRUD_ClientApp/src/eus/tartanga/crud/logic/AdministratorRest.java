/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.logic;

import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.DeleteException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.exception.SignInException;
import eus.tartanga.crud.exception.UpdateException;
import java.util.ResourceBundle;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:AdministratorFacadeREST
 * [eus.tartanga.crud.entities.administrator]<br>
 * USAGE:
 * <pre>
 *        AdministratorRest client = new AdministratorRest();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author 2dam
 */
public class AdministratorRest implements AdministratorManager {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/CRUD_ServerApp/api";
    //private static final String BASE_URI = ResourceBundle.getBundle("config.config").getString("BASE_URI");

    public AdministratorRest() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("eus.tartanga.crud.entities.administrator");
    }

    @Override
    public <T> T find(GenericType<T> responseType, String email) throws ReadException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{email}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }

    }

    @Override
    public <T> T signIn_XML(GenericType<T> responseType, String email, String passwd) throws SignInException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("signIn/{0}/{1}", new Object[]{email, passwd}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        } catch (WebApplicationException e) {
            throw new SignInException(e.getMessage());
        }
    }

    @Override
    public <T> T signIn_JSON(GenericType<T> responseType, String email, String passwd) throws SignInException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("signIn/{0}/{1}", new Object[]{email, passwd}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        } catch (WebApplicationException e) {
            throw new SignInException(e.getMessage());
        }
    }

    @Override
    public Response create(Object requestEntity) throws AddException {
        try {
            return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Response.class);
        } catch (WebApplicationException e) {
            throw new AddException(e.getMessage());
        }
    }

    @Override
    public Response update(Object requestEntity) throws UpdateException {
        try {
            return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Response.class);
        } catch (WebApplicationException e) {
            throw new UpdateException(e.getMessage());
        }
    }

    @Override
    public <T> T findAll(GenericType<T> responseType) throws ReadException {
        try {
            WebTarget resource = webTarget;
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    @Override
    public Response remove(String email) throws DeleteException {
        try {
            return webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{email})).request().delete(Response.class);
        } catch (WebApplicationException e) {
            throw new DeleteException(e.getMessage());
        }
    }

    @Override
    public void close() {
        client.close();
    }

}
