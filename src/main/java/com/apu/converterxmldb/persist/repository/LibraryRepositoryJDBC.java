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

/**
 *
 * @author apu
 */
public class LibraryRepositoryJDBC implements LibraryRepository {
    
    private static final JDBCPool dbPool = JDBCPool.getInstance();
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;

    public LibraryRepositoryJDBC() {
        this.bookRepository = new BookRepositoryJDBC();
        this.authorRepository = new AuthorRepositoryJDBC();
        this.publisherRepository = new PublisherRepositoryJDBC();       
    }

    @Override
    public boolean save(Library library) throws RepositoryException {
        if(library == null)
            throw new NullPointerException();
        for(Book book:library.getBooks()) {
            bookRepository.saveBook(book);
        }
        return true;
    }

    @Override
    public Library read() throws RepositoryException {
        Library library = new Library();
        library.addBooks(bookRepository.getBooks());
        return library;
    }

    @Override
    public boolean update(Library library) throws RepositoryException {
        return this.save(library);
    }

    @Override
    public boolean delete(Library library) throws RepositoryException {
        if(library == null)
            throw new NullPointerException();
        for(Book book:library.getBooks()) {
            bookRepository.deleteBook(book);
        }
        return true;
    }
    
}
