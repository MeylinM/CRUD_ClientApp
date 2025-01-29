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
public class WrongStockFormatException extends Exception {

    /**
     * Creates a new instance of <code>WrongStockFormatException</code> without
     * detail message.
     */
    public WrongStockFormatException() {
    }

    /**
     * Constructs an instance of <code>WrongStockFormatException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public WrongStockFormatException(String msg) {
        super(msg);
    }
}
