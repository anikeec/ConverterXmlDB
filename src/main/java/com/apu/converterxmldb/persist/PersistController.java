/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist;

import com.apu.converterxmldb.entity.Library;
import com.apu.converterxmldb.exception.PersistException;

/**
 *
 * @author apu
 */
public interface PersistController {
    
    boolean save(Library library) throws PersistException;
    
    Library read() throws PersistException;
    
    boolean update(Library library) throws PersistException;
    
    boolean delete(Library library) throws PersistException;
    
}
