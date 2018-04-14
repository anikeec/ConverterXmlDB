/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb;

/**
 *
 * @author apu
 */
public interface ConverterI {
    
    boolean save(String libraryStr);
    
    String read();
    
    boolean update(String libraryStr);
    
    boolean delete(String libraryStr);
    
}
