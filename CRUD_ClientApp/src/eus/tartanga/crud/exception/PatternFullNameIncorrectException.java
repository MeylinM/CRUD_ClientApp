/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.exception;

/**
 * Custom exception for invalid full name patterns.
 * @author Elbire y Meylin
 */

public class PatternFullNameIncorrectException extends Exception {

    // Constructor without error message
    public PatternFullNameIncorrectException() {
    }

    // Constructor with a specific error message
    public PatternFullNameIncorrectException(String msg) {
        super(msg);
    }
}
