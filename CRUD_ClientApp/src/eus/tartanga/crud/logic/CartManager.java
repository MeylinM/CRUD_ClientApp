package eus.tartanga.crud.logic;

import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.DeleteException;
import eus.tartanga.crud.exception.UpdateException;
import eus.tartanga.crud.exception.ReadException;
import javax.ws.rs.core.GenericType;
import javax.xml.ws.WebServiceException;

/**
 * Interface that defines the operations for managing cart-related data.
 * <p>
 * This interface provides methods for interacting with the cart system, including fetching cart items,
 * adding and removing items, updating cart information, and filtering items by artist or purchase status.
 * </p>
 * <p>
 * Each method may throw specific exceptions like ReadException, AddException, UpdateException,
 * or DeleteException based on the operation performed.
 * </p>
 * 
 * @author 2dam
 */
public interface CartManager {

    /**
     * Retrieves all products associated with a specific artist in XML format.
     * 
     * @param responseType The class type to map the response to.
     * @param artistName The name of the artist.
     * @param <T> The response type.
     * @return A list of products associated with the specified artist.
     * @throws ReadException If an error occurs while reading the data.
     */
    public <T> T findByArtist_XML(Class<T> responseType, String artistName) throws ReadException;

    /**
     * Retrieves all products associated with a specific artist in JSON format.
     * 
     * @param responseType The class type to map the response to.
     * @param artistName The name of the artist.
     * @param <T> The response type.
     * @return A list of products associated with the specified artist.
     * @throws ReadException If an error occurs while reading the data.
     */
    public <T> T findByArtist_JSON(Class<T> responseType, String artistName) throws ReadException;

    /**
     * Retrieves all products that have not been bought yet in XML format.
     * 
     * @param responseType The class type to map the response to.
     * @param <T> The response type.
     * @return A list of unbought products.
     * @throws ReadException If an error occurs while reading the data.
     */
    public <T> T findAllNotBoughtProducts_XML(GenericType<T> responseType) throws ReadException;

    /**
     * Retrieves all products that have not been bought yet in JSON format.
     * 
     * @param responseType The class type to map the response to.
     * @param <T> The response type.
     * @return A list of unbought products.
     * @throws ReadException If an error occurs while reading the data.
     */
    public <T> T findAllNotBoughtProducts_JSON(GenericType<T> responseType) throws ReadException;

    /**
     * Retrieves a specific product in the cart by email and product ID in XML format.
     * 
     * @param responseType The class type to map the response to.
     * @param email The user's email.
     * @param productId The product ID.
     * @param <T> The response type.
     * @return The cart information for the specified email and product.
     * @throws ReadException If an error occurs while reading the data.
     */
    public <T> T findCart_XML(GenericType<T> responseType, String email, String productId) throws ReadException;

    /**
     * Retrieves a specific product in the cart by email and product ID in JSON format.
     * 
     * @param responseType The class type to map the response to.
     * @param email The user's email.
     * @param productId The product ID.
     * @param <T> The response type.
     * @return The cart information for the specified email and product.
     * @throws ReadException If an error occurs while reading the data.
     */
    public <T> T findCart_JSON(GenericType<T> responseType, String email, String productId) throws ReadException;

    /**
     * Retrieves all products that have been bought in XML format.
     * 
     * @param responseType The class type to map the response to.
     * @param <T> The response type.
     * @return A list of bought products.
     * @throws ReadException If an error occurs while reading the data.
     */
    public <T> T findAllBoughtProducts_XML(GenericType<T> responseType) throws ReadException;

    /**
     * Retrieves all products that have been bought in JSON format.
     * 
     * @param responseType The class type to map the response to.
     * @param <T> The response type.
     * @return A list of bought products.
     * @throws ReadException If an error occurs while reading the data.
     */
    public <T> T findAllBoughtProducts_JSON(GenericType<T> responseType) throws ReadException;

    /**
     * Removes a product from the cart based on email and product ID.
     * 
     * @param email The user's email.
     * @param productId The product ID to remove.
     * @throws DeleteException If an error occurs during the deletion.
     */
    public void removeCart(String email, String productId) throws DeleteException;

    /**
     * Adds a product to the cart.
     * 
     * @param requestEntity The product data to add to the cart.
     * @throws AddException If an error occurs while adding the product.
     */
    public void addToCart(Object requestEntity) throws AddException;

    /**
     * Updates a product's information in the cart in XML format.
     * 
     * @param requestEntity The updated product data.
     * @param email The user's email.
     * @param productId The product ID to update.
     * @throws UpdateException If an error occurs while updating the cart.
     */
    public void updateCart_XML(Object requestEntity, String email, String productId) throws UpdateException;

    /**
     * Updates a product's information in the cart in JSON format.
     * 
     * @param requestEntity The updated product data.
     * @param email The user's email.
     * @param productId The product ID to update.
     * @throws UpdateException If an error occurs while updating the cart.
     */
    public void updateCart_JSON(Object requestEntity, String email, String productId) throws UpdateException;

    /**
     * Retrieves all products in the cart in XML format.
     * 
     * @param responseType The class type to map the response to.
     * @param <T> The response type.
     * @return A list of all products in the cart.
     * @throws WebServiceException If an error occurs while retrieving the data.
     */
    public <T> T findAllCartProducts_XML(GenericType<T> responseType) throws WebServiceException;

    /**
     * Retrieves all products in the cart in JSON format.
     * 
     * @param responseType The class type to map the response to.
     * @param <T> The response type.
     * @return A list of all products in the cart.
     * @throws WebServiceException If an error occurs while retrieving the data.
     */
    public <T> T findAllCartProducts_JSON(GenericType<T> responseType) throws WebServiceException;

    /**
     * Retrieves products in the cart between two dates in XML format.
     * 
     * @param responseType The class type to map the response to.
     * @param startDate The start date for the range.
     * @param endDate The end date for the range.
     * @param <T> The response type.
     * @return A list of products purchased between the specified dates.
     * @throws ReadException If an error occurs while reading the data.
     */
    public <T> T findBetweenDate_XML(Class<T> responseType, String startDate, String endDate) throws ReadException;

    /**
     * Retrieves products in the cart between two dates in JSON format.
     * 
     * @param responseType The class type to map the response to.
     * @param startDate The start date for the range.
     * @param endDate The end date for the range.
     * @param <T> The response type.
     * @return A list of products purchased between the specified dates.
     * @throws ReadException If an error occurs while reading the data.
     */
    public <T> T findBetweenDate_JSON(Class<T> responseType, String startDate, String endDate) throws ReadException;

    /**
     * Closes the CartManager, releasing any resources if necessary.
     */
    public void close();
}
