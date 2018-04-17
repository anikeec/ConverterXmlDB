/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb;

import com.apu.converterxmldb.convert.XmlConvertController;
import com.apu.converterxmldb.entity.Author;
import com.apu.converterxmldb.entity.Book;
import com.apu.converterxmldb.entity.Library;
import com.apu.converterxmldb.entity.Publisher;
import com.apu.converterxmldb.exception.ConverterException;
import com.apu.converterxmldb.exception.PersistException;
import com.apu.converterxmldb.persist.DBPersistController;
import lombok.extern.java.Log;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Level;

/**
 *
 * @author apu
 */
@Log
public class Demo {

    private static final Class classname = Demo.class;
    
    public static void main(String[] args) {
        XmlConvertController xmlConvertController =
                new XmlConvertController();
        DBPersistController persistController =
                new DBPersistController();
        ConverterI converter = new Converter(xmlConvertController,
                                                persistController);
        try {
            //work with xml
            String libraryStr = converter.read();            
            System.out.println(libraryStr);

            //work with objects
            try {
                Library library = persistController.read();
                Book book = new Book();
                book.setTitle("Java7");
                book.addAuthor(new Author("Horstmann"));
                book.setPublisher(new Publisher("Harkiv"));
                library.addBook(book);
                persistController.save(library);
            } catch (PersistException e) {
                logger.info(e.toString());
            }

            //again work with xml
            libraryStr = converter.read();
            System.out.println(libraryStr);

        } catch (ConverterException ex) {
            logger.info(ExceptionUtils.getStackTrace(ex));
        }
    }
    
}
