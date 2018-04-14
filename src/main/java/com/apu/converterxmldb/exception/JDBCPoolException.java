/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.exception;

/**
 *
 * @author apu
 */
public class JDBCPoolException extends RuntimeException {

    public JDBCPoolException(String message, Throwable cause) {
        super(message, cause);
    }

    public JDBCPoolException(Throwable cause) {
        super(cause);
    }
    
}
