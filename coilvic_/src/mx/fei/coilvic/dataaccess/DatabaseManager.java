/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.fei.coilvic.dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ivanr
 */
public class DatabaseManager {
    private Connection connection;
    private static final String DATABASE_URL = "jdbc:mysql://127.0.0.1/coilvicdb";
    private static final String DATABASE_USER = "vic";
    private static final String DATABASE_PASSWORD = "vic123";
    
    public DatabaseManager() {
        
    }
    
    public Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = connect();
            }
        } catch (SQLException e) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return connection;
    }
    
    public Connection connect() throws SQLException {
        Connection newConnection = null;
        newConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        return newConnection;
    }
    
    public boolean closeConnection() {
        boolean isClosed = false;
        
        try {
            if (connection != null) {
                connection.close();
            }
            isClosed = true;
        } catch (SQLException e) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return isClosed;
    }
    
    
}
