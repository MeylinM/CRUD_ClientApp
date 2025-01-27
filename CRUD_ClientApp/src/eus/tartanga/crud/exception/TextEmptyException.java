/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.exception;

/**
 *
 * @author 2dam
 */
public class TextEmptyException extends Exception {

    /**
     * Creates a new instance of <code>TextEmptyException</code> without detail
     * message.
     */
    public TextEmptyException() {
    }

    /**
     * Constructs an instance of <code>TextEmptyException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TextEmptyException(String msg) {
        super(msg);
    }
}
