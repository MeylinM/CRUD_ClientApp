/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.exception;

/**
 *
 * @author meyli
 */
public class SignInException extends Exception {

    /**
     * Creates a new instance of <code>SignInException</code> without detail
     * message.
     */
    public SignInException() {
    }

    /**
     * Constructs an instance of <code>SignInException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public SignInException(String msg) {
        super(msg);
    }
}
