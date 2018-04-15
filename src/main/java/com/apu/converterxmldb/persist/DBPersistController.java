/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist;

import com.apu.converterxmldb.entity.Library;
import com.apu.converterxmldb.exception.PersistException;
import com.apu.converterxmldb.exception.RepositoryException;
import com.apu.converterxmldb.persist.repository.Repository;
import com.apu.converterxmldb.persist.repository.LibraryRepositoryJDBC;

/**
 *
 * @author apu
 */
public class DBPersistController implements PersistController {
    
    private final Repository<Library> libraryRepository;

    public DBPersistController() {
        this.libraryRepository = new LibraryRepositoryJDBC();
    }

    @Override
    public void save(Library library) throws PersistException {
        try {
            libraryRepository.save(library);
        } catch (RepositoryException ex) {
            throw new PersistException(ex);
        }
    }

    @Override
    public Library read() throws PersistException {
        try {
            return libraryRepository.get();
        } catch (RepositoryException ex) {
            throw new PersistException(ex);
        }
    }

    @Override
    public void update(Library library) throws PersistException {
        try {
            libraryRepository.save(library);
        } catch (RepositoryException ex) {
            throw new PersistException(ex);
        }
    }

    @Override
    public void delete(Library library) throws PersistException {
        try {
            libraryRepository.delete(library);
        } catch (RepositoryException ex) {
            throw new PersistException(ex);
        }
    }
    
}
