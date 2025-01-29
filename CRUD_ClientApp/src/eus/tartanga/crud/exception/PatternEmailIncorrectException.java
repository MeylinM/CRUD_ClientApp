/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.exception;

/**
 * Custom exception for invalid email patterns.
 * @author  Elbire, Meylin 
 */
public class PatternEmailIncorrectException extends Exception {

    // Constructor without error message
    public PatternEmailIncorrectException() {
    }

    // Constructor with a specific error message
    public PatternEmailIncorrectException(String msg) {
        super(msg);
    }
}
