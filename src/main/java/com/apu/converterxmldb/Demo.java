/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb;

import com.apu.converterxmldb.convert.XmlConvertController;
import com.apu.converterxmldb.exception.ConverterException;
import com.apu.converterxmldb.persist.DBPersistController;
import com.apu.converterxmldb.utils.Log;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class Demo {
    
    private static final Log log = Log.getInstance();
    private static final Class classname = Demo.class;
    
    public static void main(String[] args) {
        ConverterI converter = new Converter(new XmlConvertController(),
                                            new DBPersistController());
        try {
            String libraryStr = converter.read();            
            System.out.println(libraryStr);
            
            //here we can change libraryStr and put it to save method
            converter.save(libraryStr);
        } catch (ConverterException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        }
    }
    
}
