/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb;

import com.apu.converterxmldb.convert.ConverterException;

/**
 *
 * @author apu
 */
public interface ConverterI {
    
    boolean save(String libraryStr) throws ConverterException;
    
    String read() throws ConverterException;
    
    boolean update(String libraryStr) throws ConverterException;
    
    boolean delete(String libraryStr) throws ConverterException;
    
}
