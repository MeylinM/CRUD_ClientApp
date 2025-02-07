package eus.tartanga.crud.logic;

import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.DeleteException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.exception.UpdateException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;

/**
 * Interfaz que define los métodos para gestionar los productos en un sistema CRUD.
 * Proporciona operaciones para crear, leer, actualizar y eliminar productos,
 * así como buscar productos por término o por fechas.
 * 
 * @author Elbire
 */
public interface ProductManager {
    
    /**
     * Obtiene el stock de productos en formato XML.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param <T> Tipo de datos de la respuesta.
     * @return El stock de productos en el formato solicitado.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    public <T> T findStock_XML(GenericType<T> responseType) throws WebApplicationException;

    /**
     * Obtiene el stock de productos en formato JSON.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param <T> Tipo de datos de la respuesta.
     * @return El stock de productos en el formato solicitado.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    public <T> T findStock_JSON(GenericType<T> responseType) throws WebApplicationException;

    /**
     * Actualiza un producto en formato XML.
     * 
     * @param requestEntity Objeto que contiene los datos del producto a actualizar.
     * @param id Identificador del producto a actualizar.
     * @throws UpdateException Si ocurre un error al actualizar el producto.
     */
    public void edit_XML(Object requestEntity, String id) throws UpdateException;

    /**
     * Actualiza un producto en formato JSON.
     * 
     * @param requestEntity Objeto que contiene los datos del producto a actualizar.
     * @param id Identificador del producto a actualizar.
     * @throws UpdateException Si ocurre un error al actualizar el producto.
     */
    public void edit_JSON(Object requestEntity, String id) throws UpdateException;

    /**
     * Busca productos entre dos fechas en formato XML.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param startDate Fecha de inicio del rango de búsqueda.
     * @param endDate Fecha de fin del rango de búsqueda.
     * @param <T> Tipo de datos de la respuesta.
     * @return Los productos encontrados en el rango de fechas.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    public <T> T findBetweenDates_XML(GenericType<T> responseType, String startDate, String endDate) throws WebApplicationException;

    /**
     * Busca productos entre dos fechas en formato JSON.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param startDate Fecha de inicio del rango de búsqueda.
     * @param endDate Fecha de fin del rango de búsqueda.
     * @param <T> Tipo de datos de la respuesta.
     * @return Los productos encontrados en el rango de fechas.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    public <T> T findBetweenDates_JSON(GenericType<T> responseType, String startDate, String endDate) throws WebApplicationException;

    /**
     * Busca un producto por su ID en formato XML.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param id Identificador del producto a buscar.
     * @param <T> Tipo de datos de la respuesta.
     * @return El producto encontrado con el ID especificado.
     * @throws ReadException Si ocurre un error al leer el producto.
     */
    public <T> T find_XML(GenericType<T> responseType, String id) throws ReadException;

    /**
     * Busca un producto por su ID en formato JSON.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param id Identificador del producto a buscar.
     * @param <T> Tipo de datos de la respuesta.
     * @return El producto encontrado con el ID especificado.
     * @throws ReadException Si ocurre un error al leer el producto.
     */
    public <T> T find_JSON(GenericType<T> responseType, String id) throws ReadException;

    /**
     * Crea un nuevo producto en formato XML.
     * 
     * @param requestEntity Objeto que contiene los datos del producto a crear.
     * @throws AddException Si ocurre un error al añadir el producto.
     */
    public void create_XML(Object requestEntity) throws AddException;

    /**
     * Crea un nuevo producto en formato JSON.
     * 
     * @param requestEntity Objeto que contiene los datos del producto a crear.
     * @throws AddException Si ocurre un error al añadir el producto.
     */
    public void create_JSON(Object requestEntity) throws AddException;

    /**
     * Busca productos por un término de búsqueda en formato XML.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param searchTerm Término de búsqueda para los productos.
     * @param <T> Tipo de datos de la respuesta.
     * @return Los productos que coinciden con el término de búsqueda.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    public <T> T searchByTerm_XML(GenericType<T> responseType, String searchTerm) throws WebApplicationException;

    /**
     * Busca productos por un término de búsqueda en formato JSON.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param searchTerm Término de búsqueda para los productos.
     * @param <T> Tipo de datos de la respuesta.
     * @return Los productos que coinciden con el término de búsqueda.
     * @throws WebApplicationException Si ocurre un error en la solicitud.
     */
    public <T> T searchByTerm_JSON(GenericType<T> responseType, String searchTerm) throws WebApplicationException;

    /**
     * Obtiene todos los productos en formato XML.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param <T> Tipo de datos de la respuesta.
     * @return Todos los productos.
     * @throws ReadException Si ocurre un error al leer los productos.
     */
    public <T> T findAll_XML(GenericType<T> responseType) throws ReadException;

    /**
     * Obtiene todos los productos en formato JSON.
     * 
     * @param responseType Tipo genérico de la respuesta esperada.
     * @param <T> Tipo de datos de la respuesta.
     * @return Todos los productos.
     * @throws ReadException Si ocurre un error al leer los productos.
     */
    public <T> T findAll_JSON(GenericType<T> responseType) throws ReadException;

    /**
     * Elimina un producto por su ID.
     * 
     * @param id Identificador del producto a eliminar.
     * @throws DeleteException Si ocurre un error al eliminar el producto.
     */
    public void remove(String id) throws DeleteException;
}