package mx.fei.coilvicapp.dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import log.Log;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;

public class DatabaseManager {

    private Connection connection;
    private static final String DATABASE_URL = "jdbc:mysql://127.0.0.1/coilvicdb";
    private static final String DATABASE_USER = "vic";
    private static final String DATABASE_PASSWORD = "vic123";

    public DatabaseManager() {

    }

    public Connection getConnection() throws DAOException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = connect();
            }
        } catch (SQLException exception) {
            Log.getLogger(DatabaseManager.class).error(exception.getMessage(), exception);
            throw new DAOException("No se pudo realizar la conexion a la base de datos", Status.FATAL);
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
        } catch (SQLException exception) {
            Log.getLogger(DatabaseManager.class).error(exception.getMessage(), exception);
        }
        return isClosed;
    }
    
}
