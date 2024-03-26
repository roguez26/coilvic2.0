/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test.databaseManagerTest;
import java.sql.Connection;
import java.sql.SQLException;
import mx.fei.coilvic.dataaccess.DatabaseManager;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ivanr
 */
public class DatabaseManagerTest {
    
    public DatabaseManagerTest() {
        
    }
    
    @Test
    public void testGetConnectionSucces() throws SQLException {
        DatabaseManager instance = new DatabaseManager();       
        Connection result = instance.getConnection();
        assertNotNull(result);
    }
    
}
