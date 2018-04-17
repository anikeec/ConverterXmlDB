/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author apu
 */
@Getter
@Setter
@RequiredArgsConstructor
@XmlRootElement
public class Book extends AbstractPersistable<Integer> {

    private String title;
    private List<Author> authors = new ArrayList<>();
    private Publisher publisher;

    @XmlElement
    public void setTitle(String title) {
        this.title = title;
    }
    
    @XmlElementWrapper(name = "Authors")
    @XmlElement(name = "Author")    
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
    
    public void addAuthor(Author author) {
        if(!authors.contains(author)) 
            authors.add(author);
    }

    @XmlElement
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
    
}
