/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.convert;

import com.apu.converterxmldb.Main;
import com.apu.converterxmldb.entity.Library;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author apu
 */
public class XmlConvertController implements ConvertController {

    @Override
    public Library parse(String libraryStr) throws ParseException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Library.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//            jaxbUnmarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringReader stringReader = new StringReader(libraryStr);
            Library library = (Library)jaxbUnmarshaller.unmarshal(stringReader);            
            return library;
        } catch (PropertyException ex) {
            throw new ParseException(ex);
        } catch (JAXBException ex) {
            throw new ParseException(ex);
        }
    }

    @Override
    public String convert(Library library) throws ConvertException {
        StringWriter stringWriter = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Library.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(library, stringWriter);
            return stringWriter.toString();            
        } catch (JAXBException ex) {
            throw new ConvertException(ex);
        }
    }
    
}
