/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist.JDBC;

import com.apu.converterxmldb.exception.JDBCPoolException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Deque;
import java.util.LinkedList;

/**
 *
 * @author apu
 */
public class JDBCPool {
    private static final int MAX_CONNECTIONS = 5;
    private static final Deque<Connection> connectionsStack = new LinkedList<>();
    private static final JDBCService dbService = JDBCService.getInstance();
    private static JDBCPool instance;    
    
    private JDBCPool() {
    }
    
    public static JDBCPool getInstance() {
        if(instance == null) {
            synchronized(JDBCPool.class) {
                for(int i=0;i<MAX_CONNECTIONS;i++) {
                    try {
                        connectionsStack.add(dbService.dbConnect());
                    } catch (IOException | ClassNotFoundException | SQLException ex) {
                        throw new JDBCPoolException(ex);
                    }
                }
                instance = new JDBCPool();
            }
        }
        return instance;
    }
    
    public synchronized Connection getConnection() {
        while(connectionsStack.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                //maybe it would be better to do something here....
            }
        }
        Connection con = connectionsStack.pollFirst();
        return con;
    }
    
    public synchronized void putConnection(Connection con) {
        connectionsStack.addFirst(con);
        notifyAll();
    }    
    
}
