/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist.repository;

import com.apu.converterxmldb.exception.RepositoryException;
import com.apu.converterxmldb.entity.Author;
import com.apu.converterxmldb.persist.JDBC.JDBCPool;
import com.apu.converterxmldb.utils.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author apu
 */
public class AuthorRepositoryJDBC implements AuthorRepository {
    
    private static JDBCPool dbPool = JDBCPool.getInstance();
    private static final Log log = Log.getInstance();
    private static final Class classname = AuthorRepositoryJDBC.class;
    
    private final String INSERT_STRING = 
        "INSERT INTO author(name) SELECT * FROM (SELECT ?) AS tmp "
            + "WHERE NOT EXISTS (SELECT * FROM author WHERE name = ?);"; 
    
    private final String FIND_BY_NAME_STRING =
        "SELECT * FROM author WHERE name = ?"; 
    
    private final String REMOVE_STRING =
        "DELETE FROM author WHERE name = ?";


    @Override
    public Author getAuthorByName(String name) throws RepositoryException {
        Connection con = null;
        PreparedStatement findStatement = null;
        Author author = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                findStatement = con.prepareStatement(FIND_BY_NAME_STRING);
                findStatement.setString(1, name);
                ResultSet rs = findStatement.executeQuery();
                while(rs.next()) {
                    author = new Author();
                    author.setId(rs.getInt("author_id"));
                    author.setName(rs.getString("name"));
                }
            } finally {
                if (findStatement != null) {
                    findStatement.close();
                }
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(SQLException ex) {
            throw new RepositoryException(ex);
        } finally {
            dbPool.putConnection(con);
        }
        return author;

    }

    @Override
    public void deleteAuthorByName(String name) throws RepositoryException {
        Connection con = null;
        PreparedStatement removeStatement = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                removeStatement = con.prepareStatement(REMOVE_STRING);
                removeStatement.setString(1, name);
                removeStatement.executeUpdate();
                con.commit();
            } catch (SQLException ex ) {
                if (con != null) {
                    log.debug(classname, "Transaction is being rolled back");
                    con.rollback();
                }
                throw ex;
            } finally {
                if (removeStatement != null)
                    removeStatement.close();
                if(con != null)
                    con.setAutoCommit(true);
            }
        } catch(SQLException ex) {
            throw new RepositoryException(ex);
        } finally {
            dbPool.putConnection(con);
        }
    }

    @Override
    public void saveAuthor(Author author) throws RepositoryException {
        Connection con = null;
        PreparedStatement insertStatement = null;
        PreparedStatement selectStatement = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                
                insertStatement = con.prepareStatement(INSERT_STRING, 
                                            Statement.RETURN_GENERATED_KEYS);
                insertStatement.setString(1, author.getName());
                insertStatement.setString(2, author.getName());
                
                selectStatement = con.prepareStatement(FIND_BY_NAME_STRING);                
                selectStatement.setString(1, author.getName());
                ResultSet res = selectStatement.executeQuery();
                if(res.next()) {
                    Integer key = res.getInt("author_id");
                    author.setId(key);
                    con.commit();
                } else {                
                    insertStatement.executeUpdate();
                    con.commit();
                    try (ResultSet keys = insertStatement.getGeneratedKeys()) {
                        if(keys.next()) {                        
                            Integer key = keys.getInt(1);
                            author.setId(key);
                        }
                    }
                }
            } catch (SQLException ex ) {
                if (con != null) {
                    log.debug(classname, "Transaction is being rolled back");
                    con.rollback();
                }
                throw ex;
            } finally {
                if (insertStatement != null)
                    insertStatement.close();
                if(selectStatement != null)
                    selectStatement.close();
                if(con != null)
                    con.setAutoCommit(true);
            }
        } catch(SQLException ex) {
            throw new RepositoryException(ex);
        } finally {
            dbPool.putConnection(con);
        }
    }
    
}
