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
import javax.ws.rs.core.Response;

/**
 *
 * @author 2dam
 */
public interface AdministratorManager {

    public <T> T find(Class<T> responseType, String email) throws ReadException;

    public <T> T signIn_XML(Class<T> responseType, String email, String passwd) throws SignInException;

    public <T> T signIn_JSON(Class<T> responseType, String email, String passwd) throws SignInException;

    public Response create(Object requestEntity) throws AddException;

    public Response update(Object requestEntity) throws UpdateException;

    public <T> T findAll(Class<T> responseType) throws ReadException;

    public Response remove(String email) throws DeleteException;

    public void close();
}
