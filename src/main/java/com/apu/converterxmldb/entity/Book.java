/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.entity;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author apu
 */
@XmlRootElement
public class Book {
    
    private List<Author> authors;    
    private Publisher publisher;

    public Book() {
        this.authors = new ArrayList<>();
    }     
    
    public List<Author> getAuthors() {
        return authors;
    }
    
    @XmlElement
    @XmlElementWrapper
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
    
    public void addAuthor(Author author) {
        if(!authors.contains(author)) 
            authors.add(author);
    }

    public Publisher getPublisher() {
        return publisher;
    }

    @XmlElement
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
    
    
}
