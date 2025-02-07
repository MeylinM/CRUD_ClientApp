package eus.tartanga.crud.exception;

/**
 * Custom exception for signIn error
 * @author Everyone
 */
public class SignInException extends Exception {

    /**
     * Creates a new instance of <code>SignInException</code> without detail
     * message.
     */
    public SignInException() {
    }

    /**
     * Constructs an instance of <code>SignInException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public SignInException(String msg) {
        super(msg);
    }
}
