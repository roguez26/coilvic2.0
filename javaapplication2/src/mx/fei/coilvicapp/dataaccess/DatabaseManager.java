package mx.fei.coilvicapp.dataaccess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import log.Log;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;

public class DatabaseManager {

    private Connection connection;
    private static final String DATABASE_URL = "mysql.db.url";
    private static final String DATABASE_USER = "mysql.db.user";
    private static final String DATABASE_PASSWORD = "mysql.db.password";

    public DatabaseManager() {

    }

    public Connection getConnection() throws DAOException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = connect();
            }
        } catch (SQLException exception) {
            Log.getLogger(DatabaseManager.class).error(exception.getMessage(), exception);
            throw new DAOException("No se pudo realizar la conexión a la base de datos", Status.FATAL);
        }
        return connection;
    }

    public Connection connect() throws SQLException {
        Connection newConnection = null;
        Properties properties = getPropertiesFile();
        if (properties != null) {
            newConnection = DriverManager.getConnection(
                    properties.getProperty(DATABASE_URL),
                    properties.getProperty(DATABASE_USER),
                    properties.getProperty(DATABASE_PASSWORD));

        } else {
            throw new SQLException("No fue posible encontrar las credenciales de la base de datos");
        }
        return newConnection;
    }

    public boolean closeConnection() {
        boolean isClosed = false;

        try {
            if (connection != null) {
                connection.close();
            }
            isClosed = true;
        } catch (SQLException exception) {
            Log.getLogger(DatabaseManager.class).error(exception.getMessage(), exception);
        }
        return isClosed;
    }

    private Properties getPropertiesFile() {
        Properties properties = null;
        try {
            InputStream file = new FileInputStream("resources/database/database.properties");
            if (file != null) {
                properties = new Properties();
                properties.load(file);
            }
            file.close();
        } catch (FileNotFoundException exception) {
            Log.getLogger(DatabaseManager.class).error(exception.getMessage(), exception);
        } catch (IOException exception) {
            Log.getLogger(DatabaseManager.class).error(exception.getMessage(), exception);
        }
        return properties;
    }

}
