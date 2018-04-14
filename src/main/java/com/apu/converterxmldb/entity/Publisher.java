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
public class Publisher {
    
    private Integer id;
    private String title;

    public Publisher() {
        this(null);
    }

    public Publisher(String title) {
        this(null, title);
    }
    
    public Publisher(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }
    
    @XmlTransient
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    
    @XmlElement
    public void setTitle(String title) {
        this.title = title;
    }
    
    
}
