/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist.repository;

import com.apu.converterxmldb.exception.RepositoryException;
import com.apu.converterxmldb.persist.JDBC.JDBCPool;
import com.apu.converterxmldb.entity.Publisher;
import com.apu.converterxmldb.utils.Log;
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
public class PublisherRepositoryJDBC implements Repository<Publisher> {
    
    private static JDBCPool dbPool = JDBCPool.getInstance();
    private static final Log log = Log.getInstance();
    private static final Class classname = PublisherRepositoryJDBC.class;
    
    private final String INSERT_STRING = 
        "INSERT INTO publisher(title) SELECT * FROM (SELECT ?) AS tmp "
            + "WHERE NOT EXISTS (SELECT * FROM publisher WHERE title = ?);"; 
    
    private final String FIND_BY_TITLE_STRING =
        "SELECT * FROM publisher WHERE title = ?"; 
    
    private final String REMOVE_STRING =
        "DELETE FROM publisher WHERE title = ?";


    @Override
    public Publisher get(String name) throws RepositoryException {
        Connection con = null;
        PreparedStatement findStatement = null;
        Publisher publisher = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                findStatement = con.prepareStatement(FIND_BY_TITLE_STRING);
                findStatement.setString(1, name);
                ResultSet rs = findStatement.executeQuery();
                while(rs.next()) {
                    publisher = new Publisher();
                    publisher.setId(rs.getInt("publisher_id"));
                    publisher.setTitle(rs.getString("title"));
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
        return publisher;

    }

    @Override
    public void delete(String name) throws RepositoryException {
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
    public void save(Publisher publisher) throws RepositoryException {
        Connection con = null;
        PreparedStatement insertStatement = null;
        PreparedStatement selectStatement = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                
                insertStatement = con.prepareStatement(INSERT_STRING, 
                                            Statement.RETURN_GENERATED_KEYS);
                insertStatement.setString(1, publisher.getTitle());
                insertStatement.setString(2, publisher.getTitle());
                
                selectStatement = con.prepareStatement(FIND_BY_TITLE_STRING);                
                selectStatement.setString(1, publisher.getTitle());
                ResultSet res = selectStatement.executeQuery();
                if(res.next()) {
                    Integer key = res.getInt("publisher_id");
                    publisher.setId(key);
                    con.commit();
                } else {                
                    insertStatement.executeUpdate();
                    con.commit();
                    try (ResultSet keys = insertStatement.getGeneratedKeys()) {
                        if(keys.next()) {                        
                            Integer key = keys.getInt(1);
                            publisher.setId(key);
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

    @Override
    public List<Publisher> getAll() throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Publisher get() throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void delete(Publisher obj) throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
