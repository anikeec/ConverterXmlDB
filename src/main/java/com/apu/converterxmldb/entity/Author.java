/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author apu
 */
@XmlRootElement
public class Author {
    
    private Integer id;
    private String name;

    public Author() {
        this(null);
    }  

    public Author(String name) {
        this(null, name);
    }

    public Author(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }
    
    @XmlTransient
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    @XmlElement
    public void setName(String name) {
        this.name = name;
    }
    
    
}
