/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.logic;

import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.exception.UpdateException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.xml.ws.WebServiceException;

/**
 * Jersey REST client generated for REST resource:CartFacadeREST
 * [eus.tartanga.crud.entities.cart]<br>
 * USAGE:
 * <pre>
 * CartClientRest client = new CartClientRest();
 * Object response = client.XXX(...);
 * // do whatever with response
 * client.close();
 * </pre>
 *
 * @author meylin
 */
public class CartClientRest implements CartManager {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/CRUD_ServerApp/api";
    // private static final String BASE_URI = ResourceBundle.getBundle("config.config").getString("BASE_URI");

    public CartClientRest() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("eus.tartanga.crud.entities.cart");
    }

    @Override
    public <T> T findByArtist_XML(Class<T> responseType, String artistName) throws ReadException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("byArtist/{0}", new Object[]{artistName}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    @Override
    public <T> T findByArtist_JSON(Class<T> responseType, String artistName) throws ReadException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("byArtist/{0}", new Object[]{artistName}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    @Override
    public <T> T findAllNotBoughtProducts_XML(GenericType<T> responseType) throws ReadException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path("notbought");
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    @Override
    public <T> T findAllNotBoughtProducts_JSON(GenericType<T> responseType) throws ReadException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path("notbought");
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    @Override
    public <T> T findCart_XML(GenericType<T> responseType, String email, String productId) throws ReadException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{email, productId}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    public <T> T findCart_JSON(GenericType<T> responseType, String email, String productId) throws ReadException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{email, productId}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    @Override
    public <T> T findAllBoughtProducts_XML(GenericType<T> responseType) throws ReadException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path("bought");
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    @Override
    public <T> T findAllBoughtProducts_JSON(GenericType<T> responseType) throws ReadException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path("bought");
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    @Override
    public void removeCart(String email, String productId) throws ClientErrorException {

        webTarget.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{email, productId})).request().delete();

    }

    @Override
    public void addToCart(Object requestEntity) throws AddException {
        try {
            webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        } catch (WebApplicationException e) {
            throw new AddException(e.getMessage());
        }
    }

    @Override
    public void updateCart_XML(Object requestEntity, String email, String productId) throws UpdateException {
        try {
            webTarget.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{email, productId})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        } catch (WebApplicationException e) {
            throw new UpdateException(e.getMessage());
        }
    }

    @Override
    public void updateCart_JSON(Object requestEntity, String email, String productId) throws UpdateException {
        try {
            webTarget.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{email, productId})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        } catch (WebApplicationException e) {
            throw new UpdateException(e.getMessage());
        }
    }

    @Override
    public <T> T findAllCartProducts_XML(GenericType<T> responseType) throws WebServiceException {

        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public <T> T findAllCartProducts_JSON(GenericType<T> responseType) throws WebServiceException {

        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    public <T> T findBetweenDate_XML(Class<T> responseType, String startDate, String endDate) throws ReadException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("betweenDates/{0}/{1}", new Object[]{startDate, endDate}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    @Override
    public <T> T findBetweenDate_JSON(Class<T> responseType, String startDate, String endDate) throws ReadException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("betweenDates/{0}/{1}", new Object[]{startDate, endDate}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    @Override
    public void close() {
        client.close();
    }
}
