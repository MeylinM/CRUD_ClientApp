package eus.tartanga.crud.exception;

/**
 * Custom exception for incorrect ZIP code format.
 * @author Elbire, Meylin
 */
public class PatternZipIncorrectException extends Exception {

    // Constructor without error message
    public PatternZipIncorrectException() {
    }

    // Constructor with a specific error message
    public PatternZipIncorrectException(String msg) {
        super(msg);
    }
}
