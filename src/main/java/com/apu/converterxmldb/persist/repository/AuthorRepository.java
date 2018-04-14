/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist.repository;

import com.apu.converterxmldb.exception.RepositoryException;
import com.apu.converterxmldb.entity.Author;

/**
 *
 * @author apu
 */
public interface AuthorRepository {
    
//    List<Author> getAuthors();

    Author getAuthorByName(String name) throws RepositoryException;

    void saveAuthor(Author author) throws RepositoryException;

    void deleteAuthorByName(String name) throws RepositoryException;    
    
}
