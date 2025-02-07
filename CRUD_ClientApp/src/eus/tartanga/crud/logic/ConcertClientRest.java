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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 * Cliente REST generado para interactuar con el recurso RESTful de conciertos.
 *
 * Esta clase permite realizar operaciones sobre los conciertos a través de un
 * cliente REST utilizando la librería Jersey. Está diseñada para comunicarse
 * con el servidor a través de su API y realizar operaciones como la búsqueda,
 * creación, y actualización de conciertos.
 *
 * La URL base del servidor es definida por {@code BASE_URI}, que actualmente
 * apunta a un servidor local. El cliente REST está configurado para enviar y
 * recibir datos en formato XML y JSON según el método utilizado.
 *
 * @author Irati
 */
public class ConcertClientRest implements ConcertManager {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/CRUD_ServerApp/api";
    //private static final String BASE_URI = ResourceBundle.getBundle("config.config").getString("BASE_URI");

    /**
     * Constructor que inicializa el cliente REST y configura la URL base para
     * acceder al recurso de conciertos.
     *
     * Este constructor crea un nuevo cliente REST y lo configura para
     * interactuar con el servidor RESTful en la dirección especificada por
     * {@code BASE_URI}. También establece el objetivo (target) en la ruta
     * correspondiente para interactuar con el recurso de conciertos.
     */
    public ConcertClientRest() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("eus.tartanga.crud.entities.concert");
    }

    /**
     * Elimina un concierto identificado por su ID a través de una petición
     * DELETE al servicio REST.
     *
     * @param id Identificador del concierto a eliminar.
     * @throws DeleteException Si ocurre un error en la eliminación, como una
     * respuesta de error del servidor.
     */
    @Override
    public void removeConcert(String id) throws DeleteException {
        try {
            webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete();
        } catch (WebApplicationException e) {
            throw new DeleteException(e.getMessage());
        }
    }

    /**
     * Obtiene una lista de conciertos próximos en formato XML mediante una
     * petición GET al servicio REST.
     *
     * @param responseType Clase del tipo de respuesta esperada.
     * @param <T> Tipo genérico de la respuesta.
     * @return Lista de conciertos en formato XML.
     * @throws ReadException Si ocurre un error al recuperar los datos del
     * servidor.
     */
    @Override
    public <T> T findComingSoon_XML(Class<T> responseType) throws ReadException {
        WebTarget resource = webTarget;
        resource = resource.path("comingSoon");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Obtiene una lista de conciertos próximos en formato JSON mediante una
     * petición GET al servicio REST.
     *
     * @param responseType Clase del tipo de respuesta esperada.
     * @param <T> Tipo genérico de la respuesta.
     * @return Lista de conciertos en formato JSON.
     * @throws ReadException Si ocurre un error al recuperar los datos del
     * servidor.
     */
    @Override
    public <T> T findComingSoon_JSON(Class<T> responseType) throws ReadException {
        WebTarget resource = webTarget;
        resource = resource.path("comingSoon");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Obtiene una lista de conciertos próximos en formato XML mediante una
     * petición GET al servicio REST.
     *
     * @param responseType Clase del tipo de respuesta esperada.
     * @param <T> Tipo genérico de la respuesta.
     * @return Lista de conciertos en formato XML.
     * @throws ReadException Si ocurre un error al recuperar los datos del
     * servidor.
     */
    @Override
    public <T> T findBetweenDates_XML(Class<T> responseType, String startDate, String endDate) throws ReadException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("betweenDates/{0}/{1}", new Object[]{startDate, endDate}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Realiza una solicitud GET a la API para obtener datos entre dos fechas
     * especificadas.
     *
     * Este método realiza una llamada a un recurso RESTful utilizando el
     * formato JSON y devuelve la respuesta en el tipo de clase especificado
     * como parámetro de tipo genérico. Las fechas de inicio y fin se incluyen
     * en la URL como parámetros de ruta.
     *
     * @param <T> El tipo de objeto que se espera como respuesta (debe ser una
     * clase que represente la estructura de los datos).
     * @param responseType La clase de tipo que se desea obtener como respuesta.
     * Este parámetro indica el tipo de objeto en el que se deserializará la
     * respuesta JSON.
     * @param startDate La fecha de inicio en formato adecuado (por ejemplo,
     * "yyyy-MM-dd").
     * @param endDate La fecha de finalización en formato adecuado (por ejemplo,
     * "yyyy-MM-dd").
     * @return Un objeto del tipo {@code T} que contiene los datos obtenidos en
     * formato JSON.
     * @throws ReadException Si ocurre un error al leer los datos de la
     * respuesta.
     */
    @Override
    public <T> T findBetweenDates_JSON(Class<T> responseType, String startDate, String endDate) throws ReadException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("betweenDates/{0}/{1}", new Object[]{startDate, endDate}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Actualiza un concierto en el servidor utilizando el formato XML.
     *
     * Este método realiza una solicitud PUT a un recurso RESTful para
     * actualizar la información de un concierto en el servidor. Los datos a
     * actualizar se pasan como un objeto que se convierte a formato XML y se
     * envía en la solicitud. El identificador del concierto es proporcionado
     * como un parámetro en la URL.
     *
     * @param requestEntity El objeto que contiene los datos actualizados del
     * concierto. Este objeto se convertirá a formato XML antes de ser enviado.
     * @param id El identificador único del concierto que se desea actualizar.
     * @throws UpdateException Si ocurre un error al intentar actualizar el
     * concierto en el servidor.
     */
    @Override
    public void updateConcert_XML(Object requestEntity, String id) throws UpdateException {
        try {
            webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        } catch (WebApplicationException e) {
            throw new UpdateException(e.getMessage());
        }
    }

    /**
     * Actualiza un concierto en el servidor utilizando el formato JSON.
     *
     * Este método realiza una solicitud PUT a un recurso RESTful para
     * actualizar la información de un concierto en el servidor. Los datos a
     * actualizar se pasan como un objeto que se convierte a formato JSON y se
     * envía en la solicitud. El identificador del concierto es proporcionado
     * como un parámetro en la URL.
     *
     * @param requestEntity El objeto que contiene los datos actualizados del
     * concierto. Este objeto se convertirá a formato JSON antes de ser enviado.
     * @param id El identificador único del concierto que se desea actualizar.
     * @throws UpdateException Si ocurre un error al intentar actualizar el
     * concierto en el servidor.
     */
    @Override
    public void updateConcert_JSON(Object requestEntity, String id) throws UpdateException {
        try {
            webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        } catch (WebApplicationException e) {
            throw new UpdateException(e.getMessage());
        }
    }

    /**
     * Realiza una búsqueda de datos en el servidor utilizando un término de
     * búsqueda y el formato XML.
     *
     * Este método realiza una solicitud GET a un recurso RESTful para buscar
     * datos relacionados con el término de búsqueda proporcionado. Los
     * resultados se devuelven en formato XML y se deserializan en un objeto del
     * tipo especificado por el parámetro {@code responseType}.
     *
     * @param <T> El tipo de objeto que se espera como respuesta (debe ser una
     * clase que represente la estructura de los datos).
     * @param responseType La clase de tipo que se desea obtener como respuesta.
     * Este parámetro indica el tipo de objeto en el que se deserializará la
     * respuesta XML.
     * @param searchTerm El término de búsqueda que se utilizará para filtrar
     * los datos en el servidor.
     * @return Un objeto del tipo {@code T} que contiene los resultados de la
     * búsqueda en formato XML.
     * @throws ReadException Si ocurre un error al leer los datos de la
     * respuesta.
     */
    @Override
    public <T> T searchByTerm_XML(Class<T> responseType, String searchTerm) throws ReadException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("search/{0}", new Object[]{searchTerm}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Realiza una búsqueda de datos en el servidor utilizando un término de
     * búsqueda y el formato JSON.
     *
     * Este método realiza una solicitud GET a un recurso RESTful para buscar
     * datos relacionados con el término de búsqueda proporcionado. Los
     * resultados se devuelven en formato JSON y se deserializan en un objeto
     * del tipo especificado por el parámetro {@code responseType}.
     *
     * @param <T> El tipo de objeto que se espera como respuesta (debe ser una
     * clase que represente la estructura de los datos).
     * @param responseType La clase de tipo que se desea obtener como respuesta.
     * Este parámetro indica el tipo de objeto en el que se deserializará la
     * respuesta JSON.
     * @param searchTerm El término de búsqueda que se utilizará para filtrar
     * los datos en el servidor.
     * @return Un objeto del tipo {@code T} que contiene los resultados de la
     * búsqueda en formato JSON.
     * @throws ReadException Si ocurre un error al leer los datos de la
     * respuesta.
     */
    @Override
    public <T> T searchByTerm_JSON(Class<T> responseType, String searchTerm) throws ReadException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("search/{0}", new Object[]{searchTerm}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Crea un nuevo concierto en el servidor utilizando el formato XML.
     *
     * Este método realiza una solicitud POST a un recurso RESTful para crear un
     * nuevo concierto. Los datos del concierto se pasan como un objeto que se
     * convierte a formato XML y se envía en la solicitud.
     *
     * @param requestEntity El objeto que contiene los datos del nuevo
     * concierto. Este objeto se convertirá a formato XML antes de ser enviado.
     * @throws AddException Si ocurre un error al intentar crear el concierto en
     * el servidor.
     */
    @Override
    public void createConcert_XML(Object requestEntity) throws AddException {
        try {
            webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        } catch (WebApplicationException e) {
            throw new AddException(e.getMessage());
        }
    }

    /**
     * Crea un nuevo concierto en el servidor utilizando el formato JSON.
     *
     * Este método realiza una solicitud POST a un recurso RESTful para crear un
     * nuevo concierto. Los datos del concierto se pasan como un objeto que se
     * convierte a formato JSON y se envía en la solicitud.
     *
     * @param requestEntity El objeto que contiene los datos del nuevo
     * concierto. Este objeto se convertirá a formato JSON antes de ser enviado.
     * @throws AddException Si ocurre un error al intentar crear el concierto en
     * el servidor.
     */
    @Override
    public void createConcert_JSON(Object requestEntity) throws AddException {
        try {
            webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        } catch (WebApplicationException e) {
            throw new AddException(e.getMessage());
        }
    }

    /**
     * Obtiene un concierto específico del servidor en formato XML, dado su
     * identificador.
     *
     * Este método realiza una solicitud GET a un recurso RESTful para obtener
     * un concierto específico utilizando su identificador. Los datos se
     * deserializan en el tipo de respuesta especificado por el parámetro
     * {@code responseType}.
     *
     * @param <T> El tipo de objeto que se espera como respuesta (debe ser una
     * clase que represente los datos del concierto).
     * @param responseType El tipo de respuesta que se espera recibir. Este
     * parámetro indica el tipo de objeto en el que se deserializarán los datos
     * del concierto en formato XML.
     * @param id El identificador único del concierto que se desea obtener.
     * @return Un objeto del tipo {@code T} que contiene los datos del concierto
     * obtenido en formato XML.
     * @throws ReadException Si ocurre un error al leer los datos de la
     * respuesta.
     */
    @Override
    public <T> T findConcert_XML(Class<T> responseType, String id) throws ReadException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    /**
     * Obtiene un concierto específico del servidor en formato JSON, dado su
     * identificador.
     *
     * Este método realiza una solicitud GET a un recurso RESTful para obtener
     * un concierto específico utilizando su identificador. Los datos se
     * deserializan en el tipo de respuesta especificado por el parámetro
     * {@code responseType}.
     *
     * @param <T> El tipo de objeto que se espera como respuesta (debe ser una
     * clase que represente los datos del concierto).
     * @param responseType El tipo de respuesta que se espera recibir. Este
     * parámetro indica el tipo de objeto en el que se deserializarán los datos
     * del concierto en formato JSON.
     * @param id El identificador único del concierto que se desea obtener.
     * @return Un objeto del tipo {@code T} que contiene los datos del concierto
     * obtenido en formato JSON.
     * @throws ReadException Si ocurre un error al leer los datos de la
     * respuesta.
     */
    @Override
    public <T> T findConcert_JSON(Class<T> responseType, String id) throws ReadException {
        try {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    /**
     * Obtiene todos los conciertos disponibles en el servidor en formato XML.
     *
     * Este método realiza una solicitud GET a un recurso RESTful para obtener
     * todos los conciertos en formato XML. Los datos se deserializan en el tipo
     * de respuesta especificado por el parámetro {@code responseType}.
     *
     * @param <T> El tipo de objeto que se espera como respuesta (debe ser una
     * clase que represente una colección de conciertos).
     * @param responseType El tipo de respuesta que se espera recibir. Este
     * parámetro indica el tipo de objeto en el que se deserializarán los datos
     * en formato XML.
     * @return Un objeto del tipo {@code T} que contiene todos los conciertos
     * obtenidos en formato XML.
     * @throws ReadException Si ocurre un error al leer los datos de la
     * respuesta.
     */
    @Override
    public <T> T findAllConcerts_XML(GenericType<T> responseType) throws ReadException {
        try {
            WebTarget resource = webTarget;
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    /**
     * Obtiene todos los conciertos disponibles en el servidor en formato XML.
     *
     * Este método realiza una solicitud GET a un recurso RESTful para obtener
     * todos los conciertos en formato XML. Los datos se deserializan en el tipo
     * de respuesta especificado por el parámetro {@code responseType}.
     *
     * @param <T> El tipo de objeto que se espera como respuesta (debe ser una
     * clase que represente una colección de conciertos).
     * @param responseType El tipo de respuesta que se espera recibir. Este
     * parámetro indica el tipo de objeto en el que se deserializarán los datos
     * en formato XML.
     * @return Un objeto del tipo {@code T} que contiene todos los conciertos
     * obtenidos en formato XML.
     * @throws ReadException Si ocurre un error al leer los datos de la
     * respuesta.
     */
    @Override
    public <T> T findAllConcerts_JSON(Class<T> responseType) throws ReadException {
        try {
            WebTarget resource = webTarget;
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
        } catch (WebApplicationException e) {
            throw new ReadException(e.getMessage());
        }
    }

    /**
     * Cierra la conexión del cliente.
     *
     * Este método cierra el cliente de la API, liberando los recursos asociados
     * con él. Después de llamar a este método, el cliente ya no puede ser
     * utilizado para realizar más solicitudes.
     */
    @Override
    public void close() {
        client.close();
    }
}
