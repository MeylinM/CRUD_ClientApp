/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.exception;

/**
 * Custom exception for exceeding maximum city name length.
 *
 * @author Elbire, Meylin
 */
public class MaxCityCharacterException extends Exception {

    // Constructor without error message
    public MaxCityCharacterException() {
    }

    // Constructor with a specific error message
    public MaxCityCharacterException(String msg) {
        super(msg);
    }
}
