/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist.JDBC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 *
 * @author apu
 */
public class JDBCPool {
    private static JDBCPool instance;
    private GenericObjectPool connectionPool = null; 
    private DataSource dataSource = null;
    
    private final int DB_POOL_SIZE = 10;
    
    private JDBCPool() {
    }
    
    public static JDBCPool getInstance() {
        if(instance == null) {
            synchronized(JDBCPool.class) {
                try {
                    instance = new JDBCPool();
                    instance.dataSource = instance.dbConnect();
                    instance.loadTemporaryDatabase();
                } catch (IOException | ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(JDBCPool.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return instance;
    }
    
    public synchronized Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }   
    
    private static String loadProperty(String propertyName) throws IOException {
        Properties props = new Properties();
        InputStream fins = JDBCPool.class.getResourceAsStream("/config.properties");
        if(fins!=null)
            props.load(fins);
        return props.getProperty(propertyName, "");        
    }
    
    public synchronized DataSource dbConnect() 
                    throws IOException, ClassNotFoundException, SQLException  {
        
        String dbDriver = loadProperty("DB_DRIVER");
        String dbConnectionUrl = loadProperty("DB_CONNECTION_URL");
        String dbUser = loadProperty("DB_USER");
        String dbPassword = loadProperty("DB_PASSWORD");
        String dbName = loadProperty("DB_NAME");

        Class.forName(dbDriver);

        DriverManagerConnectionFactory connectionFactory = 
            new DriverManagerConnectionFactory(dbConnectionUrl + dbName, dbUser, dbPassword);

        PoolableConnectionFactory poolableConnectionFactory =
            new PoolableConnectionFactory(connectionFactory, null);

        connectionPool = new GenericObjectPool(poolableConnectionFactory);
        connectionPool.setMaxTotal(DB_POOL_SIZE);
        poolableConnectionFactory.setPool(connectionPool);
        
        return new PoolingDataSource(connectionPool);
    
    }

    private void loadTemporaryDatabase() throws IOException, SQLException {
        Connection con = getConnection();
        con.createStatement().execute(
                    "CREATE TABLE AUTHOR(\n" +
                    " author_id  INT NOT NULL AUTO_INCREMENT,\n" +
                    " name VARCHAR(20) DEFAULT '',\n" +
                    " PRIMARY KEY (author_id) \n" +
                    ");\n" +
                    "CREATE TABLE PUBLISHER(\n" +
                    " publisher_id  INT NOT NULL AUTO_INCREMENT,\n" +
                    " title VARCHAR(20) DEFAULT '',\n" +
                    " PRIMARY KEY (publisher_id) \n" +
                    ");\n" +
                    "CREATE TABLE BOOK(\n" +
                    " book_id  INT NOT NULL AUTO_INCREMENT,\n" +
                    " title VARCHAR(30) DEFAULT '',\n" +
                    " publisher INT, \n" +
                    " PRIMARY KEY (book_id),\n" +
                    " FOREIGN KEY (publisher) REFERENCES PUBLISHER (publisher_id)\n" +
                    ");\n" +
                    "CREATE TABLE BOOK_AUTHOR(\n" +
                    " id  INT NOT NULL AUTO_INCREMENT,\n" +
                    " book_id  INT NOT NULL,\n" +
                    " author_id  INT NOT NULL,\n" +
                    " PRIMARY KEY (id),\n" +
                    " FOREIGN KEY (book_id) REFERENCES BOOK (book_id),\n" +
                    " FOREIGN KEY (author_id) REFERENCES AUTHOR (author_id)\n" +
                    ");");
        con.commit();
        con.close();
    }
    
}
