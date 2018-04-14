/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist.JDBC;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author apu
 */
public class JDBCService {
        
    private static JDBCService instance;
    
    private JDBCService() {
    }
    
    public static JDBCService getInstance() {
        if(instance == null)
            instance = new JDBCService();
        return instance;
    }
    
    private static String loadProperty(String propertyName) throws IOException {
        Properties props = new Properties();
        InputStream fins = JDBCService.class.getResourceAsStream("/config.properties");
        if(fins!=null)
            props.load(fins);
        return props.getProperty(propertyName, "");        
    }
    
    public synchronized Connection dbConnect() 
                    throws IOException, ClassNotFoundException, SQLException  {
        
            String dbDriver = loadProperty("DB_DRIVER");
            String dbConnectionUrl = loadProperty("DB_CONNECTION_URL");
            String dbUser = loadProperty("DB_USER");
            String dbPassword = loadProperty("DB_PASSWORD");
            String dbName = loadProperty("DB_NAME");
            
            Class.forName(dbDriver);
            
            Connection connection = DriverManager.getConnection(
                    dbConnectionUrl + dbName, dbUser, dbPassword);        
        return connection;
    }
    
    public synchronized void dbDisconnect(Connection connection) throws SQLException {
        if(connection != null) 
                connection.close();
    }
}
