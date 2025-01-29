/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eus.tartanga.crud.exception;

/**
 * Custom exception for mismatched passwords.
 *
 * @author Elbire, Meylin
 */
public class PasswdsDontMatchException extends Exception {

    // Constructor without error message
    public PasswdsDontMatchException() {
    }

    // Constructor with a specific error message
    public PasswdsDontMatchException(String msg) {
        super(msg);
    }
}