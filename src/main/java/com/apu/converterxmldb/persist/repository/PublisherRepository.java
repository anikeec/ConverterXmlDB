/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist.repository;

import com.apu.converterxmldb.exception.RepositoryException;
import com.apu.converterxmldb.entity.Publisher;

/**
 *
 * @author apu
 */
public interface PublisherRepository {
    
//    List<Publisher> getPublishers();

    Publisher getPublisherByTitle(String title) throws RepositoryException;
    
    void savePublisher(Publisher publisher) throws RepositoryException;

    void deletePublisherByTitle(String title) throws RepositoryException;
    
}
