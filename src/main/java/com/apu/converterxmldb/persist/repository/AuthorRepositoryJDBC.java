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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apu
 */
public class AuthorRepositoryJDBC implements Repository<Author> {
    
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
    public Author get(String name) throws RepositoryException {
        Connection con = null;
        Author author = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);                
                author = this.get(name, con);
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
        return author;
    }

    @Override
    public void delete(String name) throws RepositoryException {
        Connection con = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                this.delete(name, con);
                con.commit();
            } catch (SQLException ex ) {
                if (con != null) {
                    log.debug(classname, "Transaction is being rolled back");
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
    public void save(Author author) throws RepositoryException {
        Connection con = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);                
                this.save(author, con);
                con.commit();
            } catch (SQLException ex ) {
                if (con != null) {
                    log.debug(classname, "Transaction is being rolled back");
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
    public List<Author> getAll() throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Author get() throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void delete(Author obj) throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    public Author get(String name, Connection con) throws SQLException {
        PreparedStatement findStatement = null;
        try {        
            findStatement = con.prepareStatement(FIND_BY_NAME_STRING);
            findStatement.setString(1, name);
            ResultSet rs = findStatement.executeQuery();
            Author author = null;
            while(rs.next()) {
                author = new Author();
                author.setId(rs.getInt("author_id"));
                author.setName(rs.getString("name"));
            }
            return author;
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
    
    public void save(Author author, Connection con) throws SQLException {
        PreparedStatement insertStatement = null;
        try { 
            Author auth = this.get(author.getName(), con);
            if(auth != null) {
                author.setId(auth.getId());
            } else {            
                insertStatement = con.prepareStatement(INSERT_STRING, 
                                            Statement.RETURN_GENERATED_KEYS);
                insertStatement.setString(1, author.getName());
                insertStatement.setString(2, author.getName());
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

    @Override
    public Author get(List<String> str) throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
