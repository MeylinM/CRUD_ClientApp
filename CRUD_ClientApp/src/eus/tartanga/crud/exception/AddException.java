package eus.tartanga.crud.exception;

/**
 * Custom exception for server error adding
 * @author Everyone
 */
public class AddException extends Exception {

    public AddException() {
    }

    public AddException(String string) {
        super(string);
    }
    
}
