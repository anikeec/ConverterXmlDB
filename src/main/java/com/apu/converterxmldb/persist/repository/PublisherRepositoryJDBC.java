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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apu
 */
public class PublisherRepositoryJDBC implements PublisherRepository {
    
    private static JDBCPool dbPool = JDBCPool.getInstance();
    private static final Log log = Log.getInstance();
    private static final Class classname = PublisherRepositoryJDBC.class;
    
    private final String INSERT_STRING = 
        "INSERT INTO publisher(publisher_id, title) "
            + "SELECT * FROM "
            + "(SELECT COALESCE((SELECT (MAX(publisher_id)+1) FROM publisher publ),0), ?) "
            + "AS tmp WHERE NOT EXISTS (SELECT * FROM publisher WHERE title = ?);"; 
    
    private final String FIND_BY_NAME_STRING =
        "SELECT * FROM publisher WHERE title = ?"; 
    
    private final String REMOVE_STRING =
        "DELETE FROM publisher WHERE title = ?";


    @Override
    public Publisher getPublisherByTitle(String name) throws RepositoryException {
        Connection con = null;
        PreparedStatement findStatement = null;
        Publisher publisher = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                findStatement = con.prepareStatement(FIND_BY_NAME_STRING);
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
    public void deletePublisherByTitle(String name) throws RepositoryException {
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
    public void savePublisher(Publisher publisher) throws RepositoryException {
        Connection con = null;
        PreparedStatement insertStatement = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                insertStatement = con.prepareStatement(INSERT_STRING);
                insertStatement.setString(1, publisher.getTitle());
                insertStatement.setString(2, publisher.getTitle());
                insertStatement.executeUpdate();
                con.commit();
            } catch (SQLException ex ) {
                if (con != null) {
                    log.debug(classname, "Transaction is being rolled back");
                    con.rollback();
                }
                throw ex;
            } finally {
                if (insertStatement != null) {
                    insertStatement.close();
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
    }
    
}
