/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.logic;

import eus.tartanga.crud.exception.AddException;
import eus.tartanga.crud.exception.DeleteException;
import eus.tartanga.crud.exception.ReadException;
import eus.tartanga.crud.exception.SignInException;
import eus.tartanga.crud.exception.UpdateException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 *
 * @author meyli
 */
public interface AdministratorManager {

    public <T> T find(GenericType<T> responseType, String email) throws WebApplicationException;

    public <T> T signIn_XML(GenericType<T> responseType, String email, String passwd) throws WebApplicationException;

    public <T> T signIn_JSON(GenericType<T> responseType, String email, String passwd) throws WebApplicationException;

    public Response create(Object requestEntity) throws WebApplicationException;

    public Response update(Object requestEntity) throws WebApplicationException;

    public <T> T findAll(GenericType<T> responseType) throws WebApplicationException;

    public Response remove(String email) throws WebApplicationException;

    public void close();
}
