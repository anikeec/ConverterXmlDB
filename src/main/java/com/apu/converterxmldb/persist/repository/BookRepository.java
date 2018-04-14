/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist.repository;

import com.apu.converterxmldb.exception.RepositoryException;
import com.apu.converterxmldb.entity.Book;
import java.util.List;

/**
 *
 * @author apu
 */
public interface BookRepository {
    
    List<Book> getBooks() throws RepositoryException;

//    List<Book> getBooksByTitle(String title) throws RepositoryException;    

    void deleteBook(Book book) throws RepositoryException;

    void saveBook(Book book) throws RepositoryException;
    
}
