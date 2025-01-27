/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.logic;

import eus.tartanga.crud.userInterface.controllers.ProfileViewController;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import javax.xml.bind.*;
import java.io.StringReader;
import java.util.logging.*;

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
 * @author meyli
 */
public class FanetixClientRest implements FanetixClientManager {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/CRUD_ServerApp/api";

    public FanetixClientRest() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("eus.tartanga.crud.entities.fanetixclient");
    }

    @Override
    public void createClient_XML(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    @Override
    public void createClient_JSON(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    @Override
    public void removeClient(String email) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{email})).request().delete();
    }

    @Override
    public void updateClient_XML(Object requestEntity, String email) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{email})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    @Override
    public void updateClient_JSON(Object requestEntity, String email) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{email})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    @Override
    public <T> T signIn_XML(Class<T> responseType, String email, String passwd) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("signIn/{0}/{1}", new Object[]{email, passwd}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public <T> T signIn_JSON(Class<T> responseType, String email, String passwd) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("signIn/{0}/{1}", new Object[]{email, passwd}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    public <T> T findClient_XML(GenericType<T> responseType, String email) throws ClientErrorException {
        Logger logger = Logger.getLogger(ProfileViewController.class.getName());

        // Define el WebTarget para la solicitud.
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{email}));

        // Log para ver la URL y el tipo de respuesta
        logger.info("Request URL: " + resource.getUri());

        // Realizar la solicitud GET
        Response response = resource.request(MediaType.APPLICATION_XML).get();
        String xmlResponse = response.readEntity(String.class);  // Obtener el XML como String

        logger.info("XML Response: " + xmlResponse);
        logger.info("Response status: " + response.getStatus());

        // Verificar el tipo de contenido que recibimos
        String contentType = response.getHeaderString("Content-Type");
        logger.info("Content-Type: " + contentType);

        // Si la respuesta es exitosa y el contenido es XML
        if (response.getStatus() == 200 && contentType != null && contentType.contains("application/xml")) {
            try {
                // Crear un contexto JAXB para deserializar el XML a la clase generica
                JAXBContext context = JAXBContext.newInstance(responseType.getClass());
                Unmarshaller unmarshaller = context.createUnmarshaller();

                // Convertir el XML a un objeto Java utilizando el unmarshaller
                StringReader reader = new StringReader(xmlResponse);
                T result = (T) unmarshaller.unmarshal(reader);

                // Retornar el objeto deserializado
                return result;
            } catch (JAXBException e) {
                // Si ocurre un error durante la deserialización, logueamos el error
                logger.severe("Error deserializing the XML response: " + e.getMessage());
                throw new ClientErrorException("Error deserializing the XML response", response);
            }
        } else {
            // Si la respuesta no es exitosa, logueamos el error
            logger.severe("Failed to fetch client data. Response: " + response.readEntity(String.class));
            throw new ClientErrorException("Error fetching client data", response);
        }
    }

    @Override
    public <T> T findClient_JSON(Class<T> responseType, String email) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{email}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    public <T> T findAllClients_XML(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public <T> T findAllClients_JSON(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    public void close() {
        client.close();
    }

}
