package eus.tartanga.crud.exception;

/**
 * Custom exception for wrong date format
 * @author Elbire
 */
public class WrongDateException extends Exception {

    /**
     * Creates a new instance of <code>WrongDateException</code> without detail
     * message.
     */
    public WrongDateException() {
    }

    /**
     * Constructs an instance of <code>WrongDateException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public WrongDateException(String msg) {
        super(msg);
    }
}
