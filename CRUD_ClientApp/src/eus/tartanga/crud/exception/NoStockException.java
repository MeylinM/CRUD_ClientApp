/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.exception;

/**
 * Custom exception for no stock on product
 * @author Elbire
 */
public class NoStockException extends Exception {

    /**
     * Creates a new instance of <code>NoStockException</code> without detail
     * message.
     */
    public NoStockException() {
    }

    /**
     * Constructs an instance of <code>NoStockException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoStockException(String msg) {
        super(msg);
    }
}
