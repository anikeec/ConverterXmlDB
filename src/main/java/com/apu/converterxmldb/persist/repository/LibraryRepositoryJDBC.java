/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist.repository;

import com.apu.converterxmldb.entity.Book;
import com.apu.converterxmldb.persist.JDBC.JDBCPool;
import com.apu.converterxmldb.entity.Library;
import com.apu.converterxmldb.exception.RepositoryException;
import lombok.extern.java.Log;

import java.util.List;

/**
 *
 * @author apu
 */
@Log
public class LibraryRepositoryJDBC implements Repository<Library, Integer> {
    
    private static final JDBCPool dbPool = JDBCPool.getInstance();
    private final BookRepositoryJDBC bookRepository;

    public LibraryRepositoryJDBC() {
        this.bookRepository = new BookRepositoryJDBC();       
    }

    @Override
    public Library get() throws RepositoryException {
        Library library = new Library();
        library.addBooks(bookRepository.getAll());
        return library;
    }

    @Override
    public void save(Library library) throws RepositoryException {
        if(library == null)
            throw new NullPointerException();
        for(Book book:library.getBooks()) {
            bookRepository.save(book);
        }
    }

    @Override
    public void delete(Library library) throws RepositoryException {
        if(library == null)
            throw new NullPointerException();
        for(Book book:library.getBooks()) {
            bookRepository.delete(book);
        }
    }

    @Override
    public List<Library> getAll() throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Integer get(Library obj) throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Library get(Integer id) throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Integer id) throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Library get(List<Integer> id) throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
