/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author apu
 */
@XmlRootElement
public class Publisher {
    
    private String title;

    public Publisher() {
        this(null);
    }

    public Publisher(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    
    @XmlElement
    public void setTitle(String title) {
        this.title = title;
    }
    
    
}
