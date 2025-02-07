package eus.tartanga.crud.exception;

/**
 * Custom exception for user exists
 * @author Everyone
 */
public class UserExistErrorException extends Exception {

    /**
     * Creates a new instance of <code>UserExistErrorException</code> without
     * detail message.
     */
    public UserExistErrorException() {
    }

    /**
     * Constructs an instance of <code>UserExistErrorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UserExistErrorException(String msg) {
        super(msg);
    }
}
