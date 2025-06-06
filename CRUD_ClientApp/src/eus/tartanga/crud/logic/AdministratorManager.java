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
 * Interface that defines the operations for managing administrators.
 * @author meyli
 */
public interface AdministratorManager {

    public <T> T find(GenericType<T> responseType, String email) throws ReadException;

    public <T> T signIn_XML(GenericType<T> responseType, String email, String passwd) throws SignInException;

    public <T> T signIn_JSON(GenericType<T> responseType, String email, String passwd) throws SignInException;

    public Response create(Object requestEntity) throws AddException;

    public Response update(Object requestEntity) throws UpdateException;

    public <T> T findAll(GenericType<T> responseType) throws ReadException;

    public Response remove(String email) throws DeleteException;

    public void close();
}
