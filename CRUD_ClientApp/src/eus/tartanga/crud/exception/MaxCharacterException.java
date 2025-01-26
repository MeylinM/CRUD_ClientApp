/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.exception;

/**
 *
 * @author Irati
 */
public class MaxCharacterException extends Exception {

    /**
     * Creates a new instance of <code>MaxCharacterException</code> without
     * detail message.
     */
    public MaxCharacterException() {
    }

    /**
     * Constructs an instance of <code>MaxCharacterException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public MaxCharacterException(String msg) {
        super(msg);
    }
}
