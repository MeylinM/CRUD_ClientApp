package eus.tartanga.crud.logic;

import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.DeleteException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.exception.UpdateException;
import javax.ws.rs.core.GenericType;

/**
 * Interface that defines the operations for managing artists.
 * 
 * <p>This interface provides methods for retrieving, adding, updating, 
 * and deleting artists in the system.</p>
 * 
 * <p>Implementations of this interface interact with a RESTful API to 
 * perform these operations.</p>
 * 
 * @author olaia
 */
public interface ArtistManager {

    /**
     * Retrieves all artists from the system.
     *
     * @param <T> The expected response type.
     * @param responseType The class type of the response.
     * @return A list of all artists in the specified response type.
     * @throws ReadException If an error occurs while retrieving the artists.
     */
    public <T> T findAllArtist(GenericType<T> responseType) throws ReadException;

    /**
     * Updates an existing artist in the system.
     *
     * @param requestEntity The updated artist entity.
     * @param id The ID of the artist to update.
     * @throws UpdateException If an error occurs during the update process.
     */
    public void updateArtist(Object requestEntity, String id) throws UpdateException;

    /**
     * Creates a new artist in the system.
     *
     * @param requestEntity The artist entity to be created.
     * @throws AddException If an error occurs during the creation process.
     */
    public void createArtist(Object requestEntity) throws AddException;

    /**
     * Deletes an artist from the system.
     *
     * @param id The ID of the artist to be removed.
     * @throws DeleteException If an error occurs during deletion.
     */
    public void removeArtist(String id) throws DeleteException;

    /**
     * Searches for artists that match the given search term.
     *
     * @param <T> The expected response type.
     * @param responseType The class type of the response.
     * @param searchTerm The term to search for.
     * @return A list of matching artists in the specified response type.
     * @throws ReadException If an error occurs while searching.
     */
    public <T> T searchByTerm_XML(GenericType<T> responseType, String searchTerm) throws ReadException;

    /**
     * Finds an artist by their ID.
     *
     * @param <T> The expected response type.
     * @param responseType The class type of the response.
     * @param id The ID of the artist to retrieve.
     * @return The artist entity corresponding to the given ID.
     * @throws ReadException If an error occurs while retrieving the artist.
     */
    public <T> T findArtist(Class<T> responseType, String id) throws ReadException;

    /**
     * Retrieves artists who were active between the specified dates.
     *
     * @param <T> The expected response type.
     * @param responseType The class type of the response.
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of artists active within the specified date range.
     * @throws ReadException If an error occurs while retrieving the data.
     */
    public <T> T ArtistFindBetweenDates_XML(Class<T> responseType, String startDate, String endDate) throws ReadException;
}
