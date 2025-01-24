/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.exception;

/**
 *
 * @author Elbire
 */
public class WrongDateException extends Exception {

    /**
     * Creates a new instance of <code>WrongDateException</code> without detail
     * message.
     */
    public WrongDateException() {
    }

    /**
     * Constructs an instance of <code>WrongDateException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public WrongDateException(String msg) {
        super(msg);
    }
}
