package eus.tartanga.crud.exception;

/**
 * Custom exception for text empty
 * @author Elbire
 */
public class TextEmptyException extends Exception {

    /**
     * Creates a new instance of <code>TextEmptyException</code> without detail
     * message.
     */
    public TextEmptyException() {
    }

    /**
     * Constructs an instance of <code>TextEmptyException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TextEmptyException(String msg) {
        super(msg);
    }
}
