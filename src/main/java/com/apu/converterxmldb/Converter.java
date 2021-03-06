/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb;

import com.apu.converterxmldb.convert.ConvertController;
import com.apu.converterxmldb.exception.ConvertException;
import com.apu.converterxmldb.exception.ConverterException;
import com.apu.converterxmldb.exception.ParseException;
import com.apu.converterxmldb.entity.Library;
import com.apu.converterxmldb.exception.PersistException;
import com.apu.converterxmldb.persist.PersistController;

/**
 *
 * @author apu
 */
public class Converter implements ConverterI {
    
    private final ConvertController convertController;
    private final PersistController persistController;

    public Converter(ConvertController convertController, 
                        PersistController persistController) {
        this.convertController = convertController;
        this.persistController = persistController;
    }

    @Override
    public void save(String libraryStr) throws ConverterException {
        try {
            Library library = convertController.parse(libraryStr);
            persistController.save(library);
        } catch (ParseException | PersistException ex) {
            throw new ConverterException(ex);
        }
    }

    @Override
    public String read() throws ConverterException {
        try {
            Library library = persistController.read();
            return convertController.convert(library);
        } catch (ConvertException | PersistException ex) {
            throw new ConverterException(ex);
        }
    }

    @Override
    public void update(String libraryStr) throws ConverterException {
        try {
            Library library = convertController.parse(libraryStr);
            persistController.update(library);
        } catch (ParseException | PersistException ex) {
            throw new ConverterException(ex);
        }
    }

    @Override
    public void delete(String libraryStr) throws ConverterException {
        try {
            Library library = convertController.parse(libraryStr);
            persistController.delete(library);
        } catch (ParseException | PersistException ex) {
            throw new ConverterException(ex);
        }
    }
    
    
}
