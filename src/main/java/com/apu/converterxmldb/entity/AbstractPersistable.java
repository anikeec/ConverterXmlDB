package com.apu.converterxmldb.entity;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by vova on 15.04.18.
 */
public class AbstractPersistable<PK> {

    protected PK id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractPersistable)) return false;

        AbstractPersistable<?> that = (AbstractPersistable<?>) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    public PK getId() {
        return id;
    }

    @XmlTransient
    public void setId(PK id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AbstractPersistable{" +
                "id=" + id +
                '}';
    }
}
