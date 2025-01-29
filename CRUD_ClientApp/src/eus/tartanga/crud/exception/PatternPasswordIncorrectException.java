/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.exception;

/**
 * Custom exception to validate the format of a password. Ensures that the
 * password meets specific format requirements: - Minimum 8 characters - At
 * least one uppercase letter - At least one lowercase letter - At least one
 * digit - At least one special character
 *
 * This exception is thrown if any of these conditions are not met.
 *
 * @author Meylin
 */
public class PatternPasswordIncorrectException extends Exception {

    /**
     * Default constructor for PatternPasswordIncorrectException without an
     * error message.
     */
    public PatternPasswordIncorrectException() {
    }

    /**
     * Constructs a PatternPasswordIncorrectException with the specified error
     * message.
     *
     * @param msg the detail message
     */
    public PatternPasswordIncorrectException(String msg) {
        super(msg);
    }
}
