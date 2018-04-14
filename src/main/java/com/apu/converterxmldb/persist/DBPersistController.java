/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist;

import com.apu.converterxmldb.entity.Library;
import com.apu.converterxmldb.exception.PersistException;
import com.apu.converterxmldb.exception.RepositoryException;
import com.apu.converterxmldb.persist.repository.LibraryRepository;
import com.apu.converterxmldb.persist.repository.LibraryRepositoryJDBC;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apu
 */
public class DBPersistController implements PersistController {
    
    private final LibraryRepository libraryRepository;

    public DBPersistController() {
        this.libraryRepository = new LibraryRepositoryJDBC();
    }

    @Override
    public boolean save(Library library) throws PersistException {
        try {
            return libraryRepository.save(library);
        } catch (RepositoryException ex) {
            throw new PersistException(ex);
        }
    }

    @Override
    public Library read() throws PersistException {
        try {
            return libraryRepository.read();
        } catch (RepositoryException ex) {
            throw new PersistException(ex);
        }
    }

    @Override
    public boolean update(Library library) throws PersistException {
        try {
            return libraryRepository.update(library);
        } catch (RepositoryException ex) {
            throw new PersistException(ex);
        }
    }

    @Override
    public boolean delete(Library library) throws PersistException {
        try {
            return libraryRepository.delete(library);
        } catch (RepositoryException ex) {
            throw new PersistException(ex);
        }
    }
    
}
