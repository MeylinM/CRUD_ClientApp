package eus.tartanga.crud.exception;

/**
 * Custom exception for encryption error
 * @author Everyone
 */
public class EncryptException extends Exception {

    /**
     * Creates a new instance of <code>EncryptException</code> without detail
     * message.
     */
    public EncryptException() {
    }

    /**
     * Constructs an instance of <code>EncryptException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EncryptException(String msg) {
        super(msg);
    }
}
