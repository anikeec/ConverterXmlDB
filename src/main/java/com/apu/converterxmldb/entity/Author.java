/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.entity;

import lombok.*;
import lombok.extern.java.Log;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author apu
 */
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
@Log
//TODO use lombok
//TODO extends AbstractPersistabkle for all entities that you want to store
public class Author extends AbstractPersistable<Integer>{

    @Getter private String name;

    @XmlTransient
    public void setId(Integer id) {
        this.id = id;
    }


    @XmlElement
    public void setName(String name) {
        this.name = name;
    }
    
    
}
