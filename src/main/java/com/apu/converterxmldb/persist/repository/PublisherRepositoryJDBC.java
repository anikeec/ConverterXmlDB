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
public class PublisherRepositoryJDBC implements Repository<Publisher, Integer> {
    
    private static JDBCPool dbPool = JDBCPool.getInstance();
    
    private final String INSERT_STRING = 
        "INSERT INTO publisher(title) SELECT * FROM (SELECT ?) AS tmp "
            + "WHERE NOT EXISTS (SELECT * FROM publisher WHERE title = ?);"; 
    
    private final String FIND_BY_TITLE_STRING =
        "SELECT * FROM publisher WHERE title = ?"; 
    
    private final String REMOVE_STRING =
        "DELETE FROM publisher WHERE title = ?";

    @Override
    public Publisher get(Publisher obj) throws RepositoryException {
        if(obj == null)
            throw new NullPointerException();
        Connection con = null;
        Publisher publisher = null;
        try {
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                publisher = this.get(obj.getTitle(), con);
            } finally {
                if(con != null)
                    con.setAutoCommit(true);
            }
        } catch(SQLException ex) {
            throw new RepositoryException(ex);
        } finally {
            if(con != null)
                try {
                    con.close();
                } catch (SQLException ex) {}

        }
        return publisher;
    }

    @Override
    public void save(Publisher publisher) throws RepositoryException {
        Connection con = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);                
                this.save(publisher, con);
                con.commit();
            } catch (SQLException ex ) {
                if (con != null) {
                    logger.info("Transaction is being rolled back");
                    con.rollback();
                }
                throw ex;
            } finally {
                if(con != null)
                    con.setAutoCommit(true);
            }
        } catch(SQLException ex) {
            throw new RepositoryException(ex);
        } finally {
            if(con != null)
                try {
                    con.close();
                } catch (SQLException ex) {}

        }
    }

    @Override
    public void delete(Publisher obj) throws RepositoryException {
        if(obj == null)
            throw new NullPointerException();
        Connection con = null;
        try {
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                this.delete(obj.getTitle(), con);
                con.commit();
            } catch (SQLException ex ) {
                if (con != null) {
                    logger.info("Transaction is being rolled back");
                    con.rollback();
                }
                throw ex;
            } finally {
                if(con != null)
                    con.setAutoCommit(true);
            }
        } catch(SQLException ex) {
            throw new RepositoryException(ex);
        } finally {
            if(con != null)
                try {
                    con.close();
                } catch (SQLException ex) {}

        }
    }

    @Override
    public void delete(Integer id) throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public Publisher get(Integer id) throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Publisher get(List<Integer> id) throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Publisher get(String name, Connection con) throws SQLException {
        PreparedStatement findStatement = null;
        try {        
            findStatement = con.prepareStatement(FIND_BY_TITLE_STRING);
            findStatement.setString(1, name);
            ResultSet rs = findStatement.executeQuery();
            Publisher publisher = null;
            while(rs.next()) {
                publisher = new Publisher();
                publisher.setId(rs.getInt("publisher_id"));
                publisher.setTitle(rs.getString("title"));
            }
            return publisher;
        } finally {
            if (findStatement != null)
                findStatement.close();
        }
    }
    
    public void delete(String name, Connection con) throws SQLException {
        PreparedStatement removeStatement = null;
        try {        
            removeStatement = con.prepareStatement(REMOVE_STRING);
            removeStatement.setString(1, name);
            removeStatement.executeUpdate();
        } finally {
            if (removeStatement != null)
                    removeStatement.close();
        }
    }
    
    public void save(Publisher publisher, Connection con) throws SQLException {
        PreparedStatement insertStatement = null;
        try { 
            Publisher publ = this.get(publisher.getTitle(), con);
            if(publ != null) {
                publisher.setId(publ.getId());
            } else {            
                insertStatement = con.prepareStatement(INSERT_STRING, 
                                                Statement.RETURN_GENERATED_KEYS);
                insertStatement.setString(1, publisher.getTitle());
                insertStatement.setString(2, publisher.getTitle());
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


    
}
