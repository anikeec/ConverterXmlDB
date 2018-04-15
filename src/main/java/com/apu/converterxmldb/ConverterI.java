/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb;

import com.apu.converterxmldb.exception.ConverterException;

/**
 *
 * @author apu
 */
public interface ConverterI {
    
    void save(String libraryStr) throws ConverterException;
    
    String read() throws ConverterException;
    
    void update(String libraryStr) throws ConverterException;
    
    void delete(String libraryStr) throws ConverterException;
    
}
