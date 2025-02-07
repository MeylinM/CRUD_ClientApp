package eus.tartanga.crud.exception;

/**
 * Custom exception for server error adding
 * @author Everyone
 */
public class DeleteException extends Exception {

    public DeleteException() {
    }

    public DeleteException(String string) {
        super(string);
    }
    
}
