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
public class Library {
    
    private List<Book> books;

    public Library() {
        this.books = new ArrayList<>();
    }

    public List<Book> getBooks() {
        return books;
    }

    @XmlElement
    @XmlElementWrapper
    public void setBooks(List<Book> books) {
        this.books = books;
    }
    
    public void addBook(Book book) {
        if(!books.contains(book))
            books.add(book);
    }
    
}
