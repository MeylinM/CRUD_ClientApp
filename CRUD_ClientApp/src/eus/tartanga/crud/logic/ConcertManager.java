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
import javax.ws.rs.core.GenericType;

/**
 * Interfaz para gestionar las operaciones relacionadas con los conciertos a través de un cliente REST.
 * 
 * Esta interfaz define los métodos necesarios para interactuar con el servidor RESTful en cuanto a la gestión
 * de conciertos, como crear, actualizar, eliminar y consultar conciertos. Los métodos permiten la comunicación
 * en diferentes formatos como XML y JSON.
 * 
 * @author Irati
 */
public interface ConcertManager {

    /**
     * Elimina un concierto con el identificador especificado.
     * 
     * @param id El identificador único del concierto a eliminar.
     * @throws DeleteException Si ocurre un error al eliminar el concierto.
     */
    public void removeConcert(String id) throws DeleteException;

    /**
     * Obtiene los conciertos programados para el futuro en formato XML.
     * 
     * @param <T> El tipo de objeto que se espera como respuesta (una lista de conciertos).
     * @param responseType El tipo de respuesta que se espera recibir.
     * @return Una lista de conciertos en formato XML.
     * @throws ReadException Si ocurre un error al leer los datos de la respuesta.
     */
    public <T> T findComingSoon_XML(Class<T> responseType) throws ReadException;

    /**
     * Obtiene los conciertos programados para el futuro en formato JSON.
     * 
     * @param <T> El tipo de objeto que se espera como respuesta (una lista de conciertos).
     * @param responseType El tipo de respuesta que se espera recibir.
     * @return Una lista de conciertos en formato JSON.
     * @throws ReadException Si ocurre un error al leer los datos de la respuesta.
     */
    public <T> T findComingSoon_JSON(Class<T> responseType) throws ReadException;

    /**
     * Obtiene los conciertos que ocurren entre dos fechas en formato XML.
     * 
     * @param <T> El tipo de objeto que se espera como respuesta (una lista de conciertos).
     * @param responseType El tipo de respuesta que se espera recibir.
     * @param startDate La fecha de inicio del intervalo.
     * @param endDate La fecha de fin del intervalo.
     * @return Una lista de conciertos en formato XML que ocurren entre las fechas especificadas.
     * @throws ReadException Si ocurre un error al leer los datos de la respuesta.
     */
    public <T> T findBetweenDates_XML(Class<T> responseType, String startDate, String endDate) throws ReadException;

    /**
     * Obtiene los conciertos que ocurren entre dos fechas en formato JSON.
     * 
     * @param <T> El tipo de objeto que se espera como respuesta (una lista de conciertos).
     * @param responseType El tipo de respuesta que se espera recibir.
     * @param startDate La fecha de inicio del intervalo.
     * @param endDate La fecha de fin del intervalo.
     * @return Una lista de conciertos en formato JSON que ocurren entre las fechas especificadas.
     * @throws ReadException Si ocurre un error al leer los datos de la respuesta.
     */
    public <T> T findBetweenDates_JSON(Class<T> responseType, String startDate, String endDate) throws ReadException;

    /**
     * Actualiza un concierto en formato XML.
     * 
     * @param requestEntity El objeto que contiene los datos a actualizar.
     * @param id El identificador único del concierto a actualizar.
     * @throws UpdateException Si ocurre un error al actualizar el concierto.
     */
    public void updateConcert_XML(Object requestEntity, String id) throws UpdateException;

    /**
     * Actualiza un concierto en formato JSON.
     * 
     * @param requestEntity El objeto que contiene los datos a actualizar.
     * @param id El identificador único del concierto a actualizar.
     * @throws UpdateException Si ocurre un error al actualizar el concierto.
     */
    public void updateConcert_JSON(Object requestEntity, String id) throws UpdateException;

    /**
     * Busca conciertos por un término de búsqueda en formato XML.
     * 
     * @param <T> El tipo de objeto que se espera como respuesta (una lista de conciertos).
     * @param responseType El tipo de respuesta que se espera recibir.
     * @param searchTerm El término de búsqueda.
     * @return Una lista de conciertos que coinciden con el término de búsqueda, en formato XML.
     * @throws ReadException Si ocurre un error al leer los datos de la respuesta.
     */
    public <T> T searchByTerm_XML(Class<T> responseType, String searchTerm) throws ReadException;

    /**
     * Busca conciertos por un término de búsqueda en formato JSON.
     * 
     * @param <T> El tipo de objeto que se espera como respuesta (una lista de conciertos).
     * @param responseType El tipo de respuesta que se espera recibir.
     * @param searchTerm El término de búsqueda.
     * @return Una lista de conciertos que coinciden con el término de búsqueda, en formato JSON.
     * @throws ReadException Si ocurre un error al leer los datos de la respuesta.
     */
    public <T> T searchByTerm_JSON(Class<T> responseType, String searchTerm) throws ReadException;

    /**
     * Crea un nuevo concierto en formato XML.
     * 
     * @param requestEntity El objeto que contiene los datos del nuevo concierto.
     * @throws AddException Si ocurre un error al crear el concierto.
     */
    public void createConcert_XML(Object requestEntity) throws AddException;

    /**
     * Crea un nuevo concierto en formato JSON.
     * 
     * @param requestEntity El objeto que contiene los datos del nuevo concierto.
     * @throws AddException Si ocurre un error al crear el concierto.
     */
    public void createConcert_JSON(Object requestEntity) throws AddException;

    /**
     * Obtiene un concierto específico en formato XML, dado su identificador.
     * 
     * @param <T> El tipo de objeto que se espera como respuesta (el concierto).
     * @param responseType El tipo de respuesta que se espera recibir.
     * @param id El identificador único del concierto.
     * @return El concierto en formato XML.
     * @throws ReadException Si ocurre un error al leer los datos de la respuesta.
     */
    public <T> T findConcert_XML(Class<T> responseType, String id) throws ReadException;

    /**
     * Obtiene un concierto específico en formato JSON, dado su identificador.
     * 
     * @param <T> El tipo de objeto que se espera como respuesta (el concierto).
     * @param responseType El tipo de respuesta que se espera recibir.
     * @param id El identificador único del concierto.
     * @return El concierto en formato JSON.
     * @throws ReadException Si ocurre un error al leer los datos de la respuesta.
     */
    public <T> T findConcert_JSON(Class<T> responseType, String id) throws ReadException;

    /**
     * Obtiene todos los conciertos en formato XML.
     * 
     * @param <T> El tipo de objeto que se espera como respuesta (una lista de conciertos).
     * @param responseType El tipo de respuesta que se espera recibir.
     * @return Una lista de todos los conciertos en formato XML.
     * @throws ReadException Si ocurre un error al leer los datos de la respuesta.
     */
    public <T> T findAllConcerts_XML(GenericType<T> responseType) throws ReadException;

    /**
     * Obtiene todos los conciertos en formato JSON.
     * 
     * @param <T> El tipo de objeto que se espera como respuesta (una lista de conciertos).
     * @param responseType El tipo de respuesta que se espera recibir.
     * @return Una lista de todos los conciertos en formato JSON.
     * @throws ReadException Si ocurre un error al leer los datos de la respuesta.
     */
    public <T> T findAllConcerts_JSON(Class<T> responseType) throws ReadException;

    /**
     * Cierra la conexión con el cliente REST.
     * 
     * Este método cierra la conexión con el cliente REST y libera los recursos asociados.
     */
    public void close();
}
