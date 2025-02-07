package eus.tartanga.crud.exception;

/**
 * Custom exception for price format error
 * @author Elbire
 */
public class WrongStockFormatException extends Exception {

    /**
     * Creates a new instance of <code>WrongStockFormatException</code> without
     * detail message.
     */
    public WrongStockFormatException() {
    }

    /**
     * Constructs an instance of <code>WrongStockFormatException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public WrongStockFormatException(String msg) {
        super(msg);
    }
}
