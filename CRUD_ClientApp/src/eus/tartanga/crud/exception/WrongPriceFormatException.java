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
public class WrongPriceFormatException extends Exception {

    /**
     * Creates a new instance of <code>WrongPriceFormatException</code> without
     * detail message.
     */
    public WrongPriceFormatException() {
    }

    /**
     * Constructs an instance of <code>WrongPriceFormatException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public WrongPriceFormatException(String msg) {
        super(msg);
    }
}
