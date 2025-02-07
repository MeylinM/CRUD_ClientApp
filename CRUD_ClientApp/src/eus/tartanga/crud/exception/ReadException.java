package eus.tartanga.crud.exception;

/**
 * Custom exception for read error 
 * @author Everyone
 */
public class ReadException extends Exception {

    public ReadException() {
    }

    public ReadException(String string) {
        super(string);
    }
    
}
