package unit.test.DatabaseManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class DatabaseManagerTest {

    private static final DatabaseManager DATABASE_MANAGER = new DatabaseManager();

    @After
    public void tearDown() {
        DATABASE_MANAGER.closeConnection();
    }

    @Test
    public void testGetConnectionSuccess() throws DAOException {
        Connection connection = null;

        connection = DATABASE_MANAGER.getConnection();

        System.out.println(connection);
        Assert.assertNotNull(connection);
    }

    @Test
    public void testConnectSuccess() throws DAOException {
        Connection connection = null;

        connection = DATABASE_MANAGER.connect();
        System.out.println(connection);
        Assert.assertNotNull(connection);
    }

    @Test
    public void testCloseConnectionSuccess() throws DAOException {
        boolean isClosed = false;

        isClosed = DATABASE_MANAGER.closeConnection();
        System.out.println(isClosed);
        Assert.assertTrue(isClosed);
    }

    @Test(expected = DAOException.class)
    public void testGetConnectionFailByNotFoundPropertiesFile() throws DAOException {
        File originalFile = new File("resources/database/database.properties");
        File auxFile = new File("resources/database/database2.properties");

        originalFile.renameTo(auxFile);
        try {
            DATABASE_MANAGER.getConnection();
        } finally {
            DATABASE_MANAGER.closeConnection();
            auxFile.renameTo(originalFile);
        }
    }

    @Test(expected = DAOException.class)
    public void testGetConnectionFailByIncorrectFile() throws IOException, DAOException {
        File originalFile = new File("resources/database/database.properties");
        File auxFile = new File("resources/database/database2.properties");

        originalFile.renameTo(auxFile);
        File temporaryFile = new File("resources/database/database.properties");
        temporaryFile.createNewFile();
        try {
            DATABASE_MANAGER.getConnection();
        } finally {
            temporaryFile.delete();
            auxFile.renameTo(originalFile);
        }
    }

    @Test(expected = DAOException.class)
    public void testGetConnectionFailByIncorrectPermissionsFile() throws IOException, DAOException {
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
        try {
            DATABASE_MANAGER.getConnection();
        } finally {
            temporaryFile.delete();
            auxFile.renameTo(originalFile);
        }
    }

}
