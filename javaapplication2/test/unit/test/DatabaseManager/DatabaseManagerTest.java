package unit.test.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;
import log.Log;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DatabaseManagerTest {

    private static final DatabaseManager DATABASE_MANAGER = new DatabaseManager();
    
    @Before
    public void setUp() {
        try {
            DATABASE_MANAGER.getConnection();
        } catch (DAOException exception) {
            Log.getLogger(DatabaseManagerTest.class).error(exception.getMessage(), exception);
        }
    }
    
    @After
    public void tearDown() {
        try {
            DATABASE_MANAGER.closeConnection();
        } catch (Exception exception) {
            Log.getLogger(DatabaseManagerTest.class).error(exception.getMessage(), exception);
        }
    }
    
    @Test
    public void testGetConnectionSuccess() {
        Connection connection = null;
        try {
            connection = DATABASE_MANAGER.getConnection();
        } catch (DAOException exception) {
            Log.getLogger(DatabaseManagerTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertNotNull(connection);
    } 
    
    @Test
    public void testConnectSuccess() {
        Connection connection = null;
        try {
            connection = DATABASE_MANAGER.connect();
        } catch (DAOException exception) {
            Log.getLogger(DatabaseManagerTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertNotNull(connection);
    }

    @Test
    public void testCloseConnectionSuccess() {
        boolean isClosed = false;
        try {
            isClosed = DATABASE_MANAGER.closeConnection();
        } catch (Exception exception) {
            Log.getLogger(DatabaseManagerTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(isClosed);
    } 

}
