package unit.test.DatabaseManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import log.Log;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertThrows;
import org.junit.Test;

public class DatabaseManagerTest {

    private static final DatabaseManager DATABASE_MANAGER = new DatabaseManager();

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
        System.out.println(connection);
        Assert.assertNotNull(connection);
        DATABASE_MANAGER.closeConnection();
    }

    @Test
    public void testConnectSuccess() {
        Connection connection = null;
        try {
            connection = DATABASE_MANAGER.connect();
        } catch (DAOException exception) {
            Log.getLogger(DatabaseManagerTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(connection);
        Assert.assertNotNull(connection);
        DATABASE_MANAGER.closeConnection();
    }

    @Test
    public void testCloseConnectionSuccess() {
        boolean isClosed = false;
        try {
            isClosed = DATABASE_MANAGER.closeConnection();
        } catch (Exception exception) {
            Log.getLogger(DatabaseManagerTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(isClosed);
        Assert.assertTrue(isClosed);
    }

    @Test
    public void testGetConnectionFailByNotFoundPropertiesFile() throws DAOException {
        File originalFile = new File("resources/database/database.properties");
        File auxFile = new File("resources/database/database2.properties");

        originalFile.renameTo(auxFile);
        DAOException exception = assertThrows(DAOException.class, () -> DATABASE_MANAGER.getConnection());

        System.out.println(exception.getMessage());
        DATABASE_MANAGER.closeConnection();
        auxFile.renameTo(originalFile);
    }
    
    @Test
    public void testGetConnectionFailByIncorrectFile() throws IOException {
        File originalFile = new File("resources/database/database.properties");
        File auxFile = new File("resources/database/database2.properties");
        
        originalFile.renameTo(auxFile);
        File temporaryFile = new File("resources/database/database.properties");
        temporaryFile.createNewFile();
        DAOException exception = assertThrows(DAOException.class, () -> DATABASE_MANAGER.getConnection());
        temporaryFile.delete();
        System.out.println(exception.getMessage());
        auxFile.renameTo(originalFile);
    }
    
    @Test
    public void testGetConnectionFailByIncorrectPermissionsFile() throws IOException {
        File originalFile = new File("resources/database/database.properties");
        File auxFile = new File("resources/database/database2.properties");

        originalFile.renameTo(auxFile);

        File temporaryFile = new File("resources/database/database.properties");
        temporaryFile.createNewFile();
        try (FileWriter writer = new FileWriter(temporaryFile)) {
            writer.write("mysql.db.user=user\n");
            writer.write("mysql.db.password=password\n");
            writer.write("mysql.db.url=jdbc:mysql://127.0.0.1/db\n"); 
        }

        DAOException exception = assertThrows(DAOException.class, () -> DATABASE_MANAGER.getConnection());
        System.out.println(exception.getMessage());
        temporaryFile.delete();
        auxFile.renameTo(originalFile);
    }

}
