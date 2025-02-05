/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.exception;

/**
 * Custom exception for incorrect mobile number format.
 *
 * @author Elbire, Meylin
 */
public class PatternMobileIncorrectException extends Exception {

    // Constructor without error message
    public PatternMobileIncorrectException() {
    }

    // Constructor with a specific error message
    public PatternMobileIncorrectException(String msg) {
        super(msg);
    }
}