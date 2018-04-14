/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb;

import com.apu.converterxmldb.convert.ConvertController;
import com.apu.converterxmldb.entity.Library;
import com.apu.converterxmldb.persist.PersistController;

/**
 *
 * @author apu
 */
public class Converter implements ConverterI{
    
    private final ConvertController convertController;
    private final PersistController persistController;

    public Converter(ConvertController convertController, 
                        PersistController persistController) {
        this.convertController = convertController;
        this.persistController = persistController;
    }

    @Override
    public boolean save(String libraryStr) {
        Library library = convertController.parse(libraryStr);
        return persistController.delete(library);
    }

    @Override
    public String read() {
        Library library = persistController.read();
        return convertController.convert(library);
    }

    @Override
    public boolean update(String libraryStr) {
        Library library = convertController.parse(libraryStr);
        return persistController.update(library);
    }

    @Override
    public boolean delete(String libraryStr) {
        Library library = convertController.parse(libraryStr);
        return persistController.delete(library);
    }
    
    
}
