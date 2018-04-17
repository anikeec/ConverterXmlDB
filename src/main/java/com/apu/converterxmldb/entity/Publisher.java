/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.*;

/**
 *
 * @author apu
 */
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class Publisher  extends AbstractPersistable<Integer> {

    @Getter private String title;
    
    @XmlElement
    public void setTitle(String title) {
        this.title = title;
    }
    
}
