/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist.repository;

import com.apu.converterxmldb.exception.RepositoryException;
import com.apu.converterxmldb.entity.Author;
import com.apu.converterxmldb.persist.JDBC.JDBCPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;


/**
 *
 * @author apu
 */
//TODO each class must have oun instance of logger
@Log
public class AuthorRepositoryJDBC extends AbstractRepository<Author,Integer> {
    
    private final String INSERT_STRING = 
        "INSERT INTO author(name) VALUES(?); ";
    
    private final String FIND_BY_NAME_STRING =
        "SELECT * FROM author WHERE name = ?";
    
    private final String REMOVE_STRING =
        "DELETE FROM author WHERE name = ?";

    @Override
    public List<Author> getAll() throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Integer get(Author obj, Connection con) throws SQLException {
        if(obj == null)
            throw new IllegalArgumentException();
        PreparedStatement findStatement = null;
        try {        
            findStatement = con.prepareStatement(FIND_BY_NAME_STRING);
            findStatement.setString(1, obj.getName());
            ResultSet rs = findStatement.executeQuery();
            Author author = null;
            while(rs.next()) {
                author = new Author();
                author.setId(rs.getInt("author_id"));
                author.setName(rs.getString("name"));
                obj.setId(author.getId());
                return author.getId();
            }
            return null;
        } finally {
            if (findStatement != null)
                findStatement.close();
        }
    }

    @Override
    public void delete(Author author, Connection con) throws SQLException {
        PreparedStatement removeStatement = null;
        try {        
            removeStatement = con.prepareStatement(REMOVE_STRING);
            removeStatement.setString(1, author.getName());
            removeStatement.executeUpdate();
        } finally {
            if (removeStatement != null)
                    removeStatement.close();
        }
    }
    
    public void save(Author author, Connection con) throws SQLException {
        PreparedStatement insertStatement = null;
        try { 
            Integer id = this.get(author, con);
            if(id == null) {
                insertStatement = con.prepareStatement(INSERT_STRING,
                                                        Statement.RETURN_GENERATED_KEYS);
                insertStatement.setString(1, author.getName());
                //insertStatement.setString(2, author.getName());
                insertStatement.executeUpdate();
                try (ResultSet keys = insertStatement.getGeneratedKeys()) {
                    if(keys.next()) {                        
                        Integer key = keys.getInt(1);
                        author.setId(key);
                    }
                }
            }
        } finally {
            if (insertStatement != null)
                    insertStatement.close();
        }
    }

    public void update(Author srcObj, Author resObj, Connection con) throws SQLException {
        PreparedStatement findStatement = null;
        try {
            findStatement = con.prepareStatement(FIND_BY_NAME_STRING,
                                                ResultSet.TYPE_FORWARD_ONLY,
                                                ResultSet.CONCUR_UPDATABLE,
                                                ResultSet.HOLD_CURSORS_OVER_COMMIT);
            findStatement.setString(1, srcObj.getName());
            ResultSet rs = findStatement.executeQuery();
            if(rs.next()) {
                rs.updateString("name", resObj.getName());
                resObj.setId(rs.getInt("author_id"));
                rs.updateRow();
            }
        } finally {
            if (findStatement != null)
                findStatement.close();
        }
    }
    
}
