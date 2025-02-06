package eus.tartanga.crud.logic;

import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.DeleteException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.exception.UpdateException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 * Cliente REST de Jersey para interactuar con el servicio REST de productos.
 * Esta clase implementa la interfaz ProductManager y proporciona métodos para
 * realizar operaciones CRUD sobre recursos de productos en un servidor REST.
 *
 * La clase utiliza la biblioteca Jersey para enviar solicitudes HTTP al
 * servidor y manejar las respuestas en formato XML o JSON.
 *
 * @author Elbire
 */
public class ProductClientRest implements ProductManager {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/CRUD_ServerApp/api";

    /**
     * Constructor de ProductClientRest. Inicializa un cliente HTTP de Jersey y
     * un WebTarget para interactuar con el servicio REST de productos.
     */
    public ProductClientRest() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("eus.tartanga.crud.entities.product");
    }

    /**
     * Realiza una solicitud GET al servicio REST para obtener el stock en
     * formato XML.
     *
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param <T> Tipo de la respuesta.
     * @return La respuesta deserializada en el tipo proporcionado.
     * @throws WebApplicationException Si ocurre un error durante la solicitud.
     */
    @Override
    public <T> T findStock_XML(GenericType<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path("stock");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Realiza una solicitud GET al servicio REST para obtener el stock en
     * formato JSON.
     *
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param <T> Tipo de la respuesta.
     * @return La respuesta deserializada en el tipo proporcionado.
     * @throws WebApplicationException Si ocurre un error durante la solicitud.
     */
    @Override
    public <T> T findStock_JSON(GenericType<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path("stock");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Actualiza un producto en formato XML en el servidor.
     *
     * @param requestEntity El objeto con los datos a actualizar.
     * @param id El identificador del producto a actualizar.
     * @throws UpdateException Si ocurre un error al intentar actualizar el
     * producto.
     */
    @Override
    public void edit_XML(Object requestEntity, String id) throws UpdateException {
        try {
            webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        } catch (WebApplicationException e) {
            throw new UpdateException(e.getMessage());
        }
    }

    /**
     * Actualiza un producto en formato JSON en el servidor.
     *
     * @param requestEntity El objeto con los datos a actualizar.
     * @param id El identificador del producto a actualizar.
     * @throws UpdateException Si ocurre un error al intentar actualizar el
     * producto.
     */
    @Override
    public void edit_JSON(Object requestEntity, String id) throws UpdateException {
        try {
            webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        } catch (WebApplicationException e) {
            throw new UpdateException(e.getMessage());
        }
    }

    /**
     * Realiza una solicitud GET al servicio REST para obtener productos entre
     * dos fechas en formato XML.
     *
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param startDate Fecha de inicio en formato yyyy-MM-dd.
     * @param endDate Fecha de fin en formato yyyy-MM-dd.
     * @param <T> Tipo de la respuesta.
     * @return La respuesta deserializada en el tipo proporcionado.
     * @throws WebApplicationException Si ocurre un error durante la solicitud.
     */
    @Override
    public <T> T findBetweenDates_XML(GenericType<T> responseType, String startDate, String endDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("betweenDates/{0}/{1}", new Object[]{startDate, endDate}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Realiza una solicitud GET al servicio REST para obtener productos entre
     * dos fechas en formato JSON.
     *
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param startDate Fecha de inicio en formato yyyy-MM-dd.
     * @param endDate Fecha de fin en formato yyyy-MM-dd.
     * @param <T> Tipo de la respuesta.
     * @return La respuesta deserializada en el tipo proporcionado.
     * @throws WebApplicationException Si ocurre un error durante la solicitud.
     */
    @Override
    public <T> T findBetweenDates_JSON(GenericType<T> responseType, String startDate, String endDate) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("betweenDates/{0}/{1}", new Object[]{startDate, endDate}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Realiza una solicitud GET al servicio REST para obtener un producto por
     * su identificador en formato XML.
     *
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param id El identificador del producto.
     * @param <T> Tipo de la respuesta.
     * @return La respuesta deserializada en el tipo proporcionado.
     * @throws ReadException Si ocurre un error al leer el producto.
     */
    @Override
    public <T> T find_XML(GenericType<T> responseType, String id) throws ReadException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }

    }

    /**
     * Realiza una solicitud GET al servicio REST para obtener un producto por
     * su identificador en formato JSON.
     *
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param id El identificador del producto.
     * @param <T> Tipo de la respuesta.
     * @return La respuesta deserializada en el tipo proporcionado.
     * @throws ReadException Si ocurre un error al leer el producto.
     */
    @Override
    public <T> T find_JSON(GenericType<T> responseType, String id) throws ReadException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    /**
     * Crea un nuevo producto en el servidor en formato XML.
     *
     * @param requestEntity El objeto con los datos del producto a crear.
     * @throws AddException Si ocurre un error al intentar agregar el producto.
     */
    @Override
    public void create_XML(Object requestEntity) throws AddException {
        try {
            webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        } catch (WebApplicationException e) {
            throw new AddException(e.getMessage());
        }
    }

    /**
     * Crea un nuevo producto en el servidor en formato JSON.
     *
     * @param requestEntity El objeto con los datos del producto a crear.
     * @throws AddException Si ocurre un error al intentar agregar el producto.
     */
    @Override
    public void create_JSON(Object requestEntity) throws AddException {
        try {
            webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        } catch (WebApplicationException e) {
            throw new AddException(e.getMessage());
        }
    }

    /**
     * Realiza una solicitud GET al servicio REST para buscar productos por
     * término en formato XML.
     *
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param searchTerm El término de búsqueda.
     * @param <T> Tipo de la respuesta.
     * @return La respuesta deserializada en el tipo proporcionado.
     * @throws WebApplicationException Si ocurre un error durante la solicitud.
     */
    @Override
    public <T> T searchByTerm_XML(GenericType<T> responseType, String searchTerm) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("search/{0}", new Object[]{searchTerm}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Realiza una solicitud GET al servicio REST para buscar productos por
     * término en formato JSON.
     *
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param searchTerm El término de búsqueda.
     * @param <T> Tipo de la respuesta.
     * @return La respuesta deserializada en el tipo proporcionado.
     * @throws WebApplicationException Si ocurre un error durante la solicitud.
     */
    @Override
    public <T> T searchByTerm_JSON(GenericType<T> responseType, String searchTerm) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("search/{0}", new Object[]{searchTerm}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Realiza una solicitud GET al servicio REST para obtener todos los
     * productos en formato XML.
     *
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param <T> Tipo de la respuesta.
     * @return La respuesta deserializada en el tipo proporcionado.
     * @throws ReadException Si ocurre un error al intentar leer los productos.
     */
    @Override
    public <T> T findAll_XML(GenericType<T> responseType) throws ReadException {
        try {
            WebTarget resource = webTarget;
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    /**
     * Realiza una solicitud GET al servicio REST para obtener todos los
     * productos en formato JSON.
     *
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param <T> Tipo de la respuesta.
     * @return La respuesta deserializada en el tipo proporcionado.
     * @throws ReadException Si ocurre un error al intentar leer los productos.
     */
    @Override
    public <T> T findAll_JSON(GenericType<T> responseType) throws ReadException {
        try {
            WebTarget resource = webTarget;
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    /**
     * Elimina un producto del servidor según su identificador.
     *
     * @param id El identificador del producto a eliminar.
     * @throws DeleteException Si ocurre un error al intentar eliminar el
     * producto.
     */
    @Override
    public void remove(String id) throws DeleteException {
        try {
            webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete();
        } catch (WebApplicationException e) {
            throw new DeleteException(e.getMessage());
        }
    }

    /**
     * Cierra la conexión del cliente REST.
     */
    public void close() {
        client.close();
    }

}
