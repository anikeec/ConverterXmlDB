/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist.repository;

import com.apu.converterxmldb.entity.Library;
import com.apu.converterxmldb.exception.RepositoryException;

/**
 *
 * @author apu
 */
public interface LibraryRepository {
    
    boolean save(Library library) throws RepositoryException;
    
    Library read() throws RepositoryException;
    
    boolean update(Library library) throws RepositoryException;
    
    boolean delete(Library library) throws RepositoryException;
    
}
