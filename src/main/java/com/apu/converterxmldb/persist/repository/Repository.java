/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist.repository;

import com.apu.converterxmldb.exception.RepositoryException;
import java.util.List;

/**
 *
 * @author apu
 */
public interface Repository<T> {
    
    public List<T> getAll() throws RepositoryException;
    
    public T get() throws RepositoryException;

    public T get(String str) throws RepositoryException;
    
    public T get(List<String> strs) throws RepositoryException;

    public void save(T obj) throws RepositoryException;

    public void delete(String str) throws RepositoryException;    
    
    public void delete(T obj) throws RepositoryException;
    
}
