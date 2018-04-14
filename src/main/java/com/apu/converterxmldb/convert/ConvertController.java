/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.convert;

import com.apu.converterxmldb.exception.ParseException;
import com.apu.converterxmldb.exception.ConvertException;
import com.apu.converterxmldb.entity.Library;

/**
 *
 * @author apu
 */
public interface ConvertController {
    
    Library parse(String library) throws ParseException;
    
    String convert(Library library) throws ConvertException;
    
}
