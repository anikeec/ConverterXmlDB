/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist.repository;

import com.apu.converterxmldb.exception.RepositoryException;
import com.apu.converterxmldb.persist.JDBC.JDBCPool;
import com.apu.converterxmldb.entity.Publisher;
import lombok.extern.java.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author apu
 */
@Log
public class PublisherRepositoryJDBC extends AbstractRepository<Publisher, Integer> {
    
    private final String INSERT_STRING = 
        "INSERT INTO publisher(title) VALUES(?);";
    
    private final String FIND_BY_TITLE_STRING =
        "SELECT * FROM publisher WHERE title = ?"; 
    
    private final String REMOVE_STRING =
        "DELETE FROM publisher WHERE title = ?";

    @Override
    public Integer get(Publisher obj, Connection con) throws SQLException {
        if((obj == null) || (con == null))
            throw new NullPointerException();
        PreparedStatement findStatement = null;
        try {        
            findStatement = con.prepareStatement(FIND_BY_TITLE_STRING);
            findStatement.setString(1, obj.getTitle());
            ResultSet rs = findStatement.executeQuery();
            Publisher publisher = null;
            while(rs.next()) {
                publisher = new Publisher();
                publisher.setId(rs.getInt("publisher_id"));
                publisher.setTitle(rs.getString("title"));
                obj.setId(publisher.getId());
                return publisher.getId();
            }
            return null;
        } finally {
            if (findStatement != null)
                findStatement.close();
        }
    }

    @Override
    public void delete(Publisher obj, Connection con) throws SQLException {
        if((obj == null) || (con == null))
                throw new NullPointerException();
        PreparedStatement removeStatement = null;
        try {        
            removeStatement = con.prepareStatement(REMOVE_STRING);
            removeStatement.setString(1, obj.getTitle());
            removeStatement.executeUpdate();
        } finally {
            if (removeStatement != null)
                    removeStatement.close();
        }
    }

    @Override
    public void save(Publisher publisher, Connection con) throws SQLException {
        if((publisher == null) || (con == null))
            throw new NullPointerException();
        PreparedStatement insertStatement = null;
        try { 
            Integer id = get(publisher, con);
            if(id == null) {
                insertStatement = con.prepareStatement(INSERT_STRING, 
                                                Statement.RETURN_GENERATED_KEYS);
                insertStatement.setString(1, publisher.getTitle());
                insertStatement.executeUpdate();
                try (ResultSet keys = insertStatement.getGeneratedKeys()) {
                    if(keys.next()) {                        
                        Integer key = keys.getInt(1);
                        publisher.setId(key);
                    }
                }
            }
        } finally {
            if (insertStatement != null)
                    insertStatement.close();
        }
    }

    @Override
    public void update(Publisher srcObj, Publisher resObj, Connection con) throws SQLException {
        PreparedStatement findStatement = null;
        try {
            findStatement = con.prepareStatement(FIND_BY_TITLE_STRING,
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_UPDATABLE,
                    ResultSet.HOLD_CURSORS_OVER_COMMIT);
            findStatement.setString(1, srcObj.getTitle());
            ResultSet rs = findStatement.executeQuery();
            if(rs.next()) {
                rs.updateString("title", resObj.getTitle());
                resObj.setId(rs.getInt("publisher_id"));
                rs.updateRow();
            }
        } finally {
            if (findStatement != null)
                findStatement.close();
        }
    }
    
}
