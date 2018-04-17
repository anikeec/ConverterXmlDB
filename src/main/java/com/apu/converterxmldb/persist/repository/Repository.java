/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist.repository;

import com.apu.converterxmldb.entity.AbstractPersistable;
import com.apu.converterxmldb.exception.RepositoryException;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author apu
 */
public interface Repository<T extends AbstractPersistable<PK>,PK extends Serializable> {
    
    public List<T> getAll() throws RepositoryException;
    
    public T get() throws RepositoryException;

    public PK get(T obj) throws RepositoryException;

    public T get(PK id) throws RepositoryException;
    
    public T get(List<PK> id) throws RepositoryException;

    public void save(T obj) throws RepositoryException;

    public void update(T srcObj, T resObj) throws RepositoryException;

    public void delete(PK id) throws RepositoryException;
    
    public void delete(T obj) throws RepositoryException;
    
}
