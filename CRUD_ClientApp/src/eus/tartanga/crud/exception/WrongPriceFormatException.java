package eus.tartanga.crud.exception;

/**
 * Custom exception for price format error
 * @author Elbire
 */
public class WrongPriceFormatException extends Exception {

    /**
     * Creates a new instance of <code>WrongPriceFormatException</code> without
     * detail message.
     */
    public WrongPriceFormatException() {
    }

    /**
     * Constructs an instance of <code>WrongPriceFormatException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public WrongPriceFormatException(String msg) {
        super(msg);
    }
}
