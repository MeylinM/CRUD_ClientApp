/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.exception;

/**
 * Custom exception for exceeding maximum street length.
 *
 * @author Elbire,Meylin
 */
public class MaxStreetCharacterException extends Exception {

    // Constructor without error message
    public MaxStreetCharacterException() {
    }

    // Constructor with a specific error message
    public MaxStreetCharacterException(String msg) {
        super(msg);
    }
}