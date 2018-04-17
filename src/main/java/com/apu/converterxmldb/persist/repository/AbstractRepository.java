/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist.repository;

import com.apu.converterxmldb.entity.AbstractPersistable;
import com.apu.converterxmldb.exception.RepositoryException;
import com.apu.converterxmldb.persist.JDBC.JDBCPool;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apu
 */
@Log
public abstract class AbstractRepository<T extends AbstractPersistable<PK>, PK extends Serializable>
        implements Repository<T,PK> {

    protected JDBCPool dbPool = JDBCPool.getInstance();

    @Override
    public List<T> getAll() throws RepositoryException {
        Connection con = null;
        List<T> books = new ArrayList<>();
        try {
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                books = this.getAll(con);
            } finally {
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(SQLException ex) {
            throw new RepositoryException(ex);
        } finally {
            if(con != null)
                try {
                    con.close();
                } catch (SQLException ex) {}

        }
        return books;
    }

    @Override
    public T get() throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet.");
    };

    @Override
    public PK get(T obj) throws RepositoryException {
        if(obj == null)
            throw new IllegalArgumentException();
        Connection con = null;
        PK id = null;
        try {
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                id = this.get(obj, con);
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
        return id;
    }

    @Override
    public T get(PK id) throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet.");
    };

    @Override
    public T get(List<PK> id) throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet.");
    };

    @Override
    public void save(T author) throws RepositoryException {
        Connection con = null;
        try {
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                this.save(author, con);
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
    public void update(T srcObj, T resObj) throws RepositoryException {
        Connection con = null;
        try {
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                this.update(srcObj, resObj, con);
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
    public void delete(PK id) throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet.");
    };

    @Override
    public void delete(T obj) throws RepositoryException {
        if(obj == null)
            throw new IllegalArgumentException();
        Connection con = null;
        try {
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                this.delete(obj, con);
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
    };

    public List<T> getAll(Connection con) throws SQLException{
        throw new UnsupportedOperationException("Not supported yet.");
    };

    abstract public PK get(T obj, Connection con) throws SQLException;

    abstract public void delete(T obj, Connection con) throws SQLException;

    abstract public void save(T author, Connection con) throws SQLException;

    public void update(T srcObj, T resObj, Connection con) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    };
    
}
