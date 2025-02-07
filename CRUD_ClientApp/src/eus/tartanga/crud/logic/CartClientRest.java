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
 * Jersey REST client generated for REST resource: CartFacadeREST
 * [eus.tartanga.crud.entities.cart]
 * 
 * This class provides the client-side functionality to interact with the Cart
 * resource of the CRUD server. It handles operations such as adding, removing,
 * updating, and retrieving cart items. The client communicates with the server 
 * using both XML and JSON formats.
 * 
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
    //private static final String BASE_URI = ResourceBundle.getBundle("config.config").getString("BASE_URI");

    /**
     * Constructor initializes the client and WebTarget for the Cart REST resource.
     */
    public CartClientRest() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("eus.tartanga.crud.entities.cart");
    }

    /**
     * Finds all products in the cart by a specific artist in XML format.
     * 
     * @param responseType The response type class.
     * @param artistName The name of the artist to filter by.
     * @param <T> The type of response.
     * @return A list of products in XML format.
     * @throws ReadException If an error occurs during reading the data.
     */
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

    /**
     * Finds all products in the cart by a specific artist in JSON format.
     * 
     * @param responseType The response type class.
     * @param artistName The name of the artist to filter by.
     * @param <T> The type of response.
     * @return A list of products in JSON format.
     * @throws ReadException If an error occurs during reading the data.
     */
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

    /**
     * Finds all products that have not been bought in XML format.
     * 
     * @param responseType The response type class.
     * @param <T> The type of response.
     * @return A list of products that have not been bought.
     * @throws ReadException If an error occurs during reading the data.
     */
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

    /**
     * Finds all products that have not been bought in JSON format.
     * 
     * @param responseType The response type class.
     * @param <T> The type of response.
     * @return A list of products that have not been bought.
     * @throws ReadException If an error occurs during reading the data.
     */
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

    /**
     * Finds the specific cart for a user and product in XML format.
     * 
     * @param responseType The response type class.
     * @param email The user's email.
     * @param productId The product ID.
     * @param <T> The type of response.
     * @return The cart information in XML format.
     * @throws ReadException If an error occurs during reading the data.
     */
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

    /**
     * Finds the specific cart for a user and product in JSON format.
     * 
     * @param responseType The response type class.
     * @param email The user's email.
     * @param productId The product ID.
     * @param <T> The type of response.
     * @return The cart information in JSON format.
     * @throws ReadException If an error occurs during reading the data.
     */
    public <T> T findCart_JSON(GenericType<T> responseType, String email, String productId) throws ReadException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{email, productId}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    /**
     * Finds all products in the cart that have been bought in XML format.
     * 
     * @param responseType The response type class.
     * @param <T> The type of response.
     * @return A list of products that have been bought.
     * @throws ReadException If an error occurs during reading the data.
     */
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

    /**
     * Finds all products in the cart that have been bought in JSON format.
     * 
     * @param responseType The response type class.
     * @param <T> The type of response.
     * @return A list of products that have been bought.
     * @throws ReadException If an error occurs during reading the data.
     */
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

    /**
     * Removes a product from the cart.
     * 
     * @param email The user's email.
     * @param productId The product ID.
     * @throws ClientErrorException If the client request fails.
     */
    @Override
    public void removeCart(String email, String productId) throws ClientErrorException {

        webTarget.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{email, productId})).request().delete();

    }

    /**
     * Adds a product to the cart.
     * 
     * @param requestEntity The product to be added.
     * @throws AddException If an error occurs while adding the product.
     */
    @Override
    public void addToCart(Object requestEntity) throws AddException {
        try {
            webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        } catch (WebApplicationException e) {
            throw new AddException(e.getMessage());
        }
    }

    /**
     * Updates a cart in XML format.
     * 
     * @param requestEntity The updated cart information.
     * @param email The user's email.
     * @param productId The product ID.
     * @throws UpdateException If an error occurs while updating the cart.
     */
    @Override
    public void updateCart_XML(Object requestEntity, String email, String productId) throws UpdateException {
        try {
            webTarget.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{email, productId})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        } catch (WebApplicationException e) {
            throw new UpdateException(e.getMessage());
        }
    }

    /**
     * Updates a cart in JSON format.
     * 
     * @param requestEntity The updated cart information.
     * @param email The user's email.
     * @param productId The product ID.
     * @throws UpdateException If an error occurs while updating the cart.
     */
    @Override
    public void updateCart_JSON(Object requestEntity, String email, String productId) throws UpdateException {
        try {
            webTarget.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{email, productId})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        } catch (WebApplicationException e) {
            throw new UpdateException(e.getMessage());
        }
    }

    /**
     * Finds all products in the cart in XML format.
     * 
     * @param responseType The response type class.
     * @param <T> The type of response.
     * @return A list of all products in the cart.
     * @throws WebServiceException If an error occurs while retrieving the data.
     */
    @Override
    public <T> T findAllCartProducts_XML(GenericType<T> responseType) throws WebServiceException {

        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Finds all products in the cart in JSON format.
     * 
     * @param responseType The response type class.
     * @param <T> The type of response.
     * @return A list of all products in the cart.
     * @throws WebServiceException If an error occurs while retrieving the data.
     */
    @Override
    public <T> T findAllCartProducts_JSON(GenericType<T> responseType) throws WebServiceException {

        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Finds all products purchased between two dates in XML format.
     * 
     * @param responseType The response type class.
     * @param startDate The start date.
     * @param endDate The end date.
     * @param <T> The type of response.
     * @return A list of products purchased between the dates.
     * @throws ReadException If an error occurs during reading the data.
     */
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

    /**
     * Finds all products purchased between two dates in JSON format.
     * 
     * @param responseType The response type class.
     * @param startDate The start date.
     * @param endDate The end date.
     * @param <T> The type of response.
     * @return A list of products purchased between the dates.
     * @throws ReadException If an error occurs during reading the data.
     */
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

    /**
     * Closes the client connection.
     */
    @Override
    public void close() {
        client.close();
    }
}
