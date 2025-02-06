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
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 * Jersey REST client generated for the REST resource: ArtistFacadeREST
 * [eus.tartanga.crud.entities.artist]
 *
 * <p>
 * This class provides methods to interact with the Artist API, allowing
 * operations such as retrieving, creating, updating, and deleting artist
 * records.</p>
 *
 * <p>
 * <b>Usage Example:</b></p>
 * <pre>
 *     ArtistClientRest client = new ArtistClientRest();
 *     Object response = client.findAllArtist(...);
 *     // Process the response
 *     client.close();
 * </pre>
 *
 * @author olaia
 */
public class ArtistClientRest implements ArtistManager {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/CRUD_ServerApp/api";

    /**
     * Constructor that initializes the REST client and sets up the base URI for
     * API requests.
     */
    public ArtistClientRest() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("eus.tartanga.crud.entities.artist");
    }

    /**
     * Retrieves a list of all artists from the API.
     *
     * @param <T> The expected response type.
     * @param responseType The class type of the response.
     * @return A list of artists in the specified response type.
     * @throws ReadException If an error occurs while retrieving the data.
     */
    @Override
    public <T> T findAllArtist(GenericType<T> responseType) throws ReadException {
        try {
            WebTarget resource = webTarget;
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    /**
     * Updates an existing artist in the database.
     *
     * @param requestEntity The updated artist entity.
     * @param id The ID of the artist to update.
     * @throws UpdateException If an error occurs during the update.
     */
    @Override
    public void updateArtist(Object requestEntity, String id) throws UpdateException {
        try {
            webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                    .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                    .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        } catch (WebApplicationException e) {
            throw new UpdateException(e.getMessage());
        }
    }

    /**
     * Creates a new artist in the database.
     *
     * @param requestEntity The artist entity to create.
     * @throws AddException If an error occurs during the creation process.
     */
    @Override
    public void createArtist(Object requestEntity) throws AddException {
        try {
            webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                    .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        } catch (WebApplicationException e) {
            throw new AddException(e.getMessage());
        }
    }

    /**
     * Deletes an artist from the database.
     *
     * @param id The ID of the artist to delete.
     * @throws DeleteException If an error occurs during deletion.
     */
    @Override
    public void removeArtist(String id) throws DeleteException {
        try {
            webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                    .request()
                    .delete();
        } catch (WebApplicationException e) {
            throw new DeleteException(e.getMessage());
        }
    }

    /**
     * Searches for artists based on a given term.
     *
     * @param <T> The expected response type.
     * @param responseType The class type of the response.
     * @param searchTerm The term to search for.
     * @return A list of matching artists.
     * @throws ReadException If an error occurs while searching.
     */
    @Override
    public <T> T searchByTerm_XML(GenericType<T> responseType, String searchTerm) throws ReadException {
        WebTarget resource = webTarget.path(java.text.MessageFormat.format("search/{0}", new Object[]{searchTerm}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Retrieves a specific artist by their ID.
     *
     * @param <T> The expected response type.
     * @param responseType The class type of the response.
     * @param id The ID of the artist to retrieve.
     * @return The requested artist entity.
     * @throws ReadException If an error occurs while retrieving the artist.
     */
    @Override
    public <T> T findArtist(Class<T> responseType, String id) throws ReadException {
        try {
            WebTarget resource = webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    /**
     * Retrieves artists within a specific date range.
     *
     * @param <T> The expected response type.
     * @param responseType The class type of the response.
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of artists who fall within the specified date range.
     * @throws ReadException If an error occurs while retrieving the data.
     */
    @Override
    public <T> T ArtistFindBetweenDates_XML(Class<T> responseType, String startDate, String endDate) throws ReadException {
        WebTarget resource = webTarget.path(java.text.MessageFormat.format("betweenDates/{0}/{1}", new Object[]{startDate, endDate}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Closes the client connection to free resources.
     */
    public void close() {
        client.close();
    }
}
