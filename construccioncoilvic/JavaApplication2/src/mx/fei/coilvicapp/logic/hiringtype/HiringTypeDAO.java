package mx.fei.coilvicapp.logic.hiringtype;

import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import java.sql.SQLException;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.implementations.DAOException;

public class HiringTypeDAO implements IHiringType{
    
    @Override
    public int insertHiringType(HiringType hiringType) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "INSERT INTO tipocontratación(nombre) VALUES(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, hiringType.getName());
            
            return preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(HiringTypeDAO.class.getName()).log(Level.SEVERE, null, exception);
            return -1;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Logger.getLogger(HiringTypeDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }
    
    @Override
    public int updateHiringType(String newHiringType, String hiringTypeName) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE tipocontratación SET nombre = ? WHERE nombre = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, newHiringType);
            preparedStatement.setString(2, hiringTypeName);
            
            return preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(HiringTypeDAO.class.getName()).log(Level.SEVERE, null, exception);
            return -1;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Logger.getLogger(HiringTypeDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }        
    }
    
    @Override
    public int deleteHiringType(String hiringTypeName) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "DELETE FROM tipocontratación WHERE nombre = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, hiringTypeName);
            
            return preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(HiringTypeDAO.class.getName()).log(Level.SEVERE, null, exception);
            return -1;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Logger.getLogger(HiringTypeDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }         
    }
    
    @Override
    public List<String> getHiringTypes() throws DAOException {
        List<String> hiringTypes = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;        
        ResultSet resultSet = null;
        String statement = "SELECT * FROM tipocontratación";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            resultSet = preparedStatement.executeQuery();

            while (resultSet != null && resultSet.next()) {
                hiringTypes.add(resultSet.getString("nombre"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(HiringTypeDAO.class.getName()).log(Level.SEVERE, null, exception);
            return null;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(HiringTypeDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return hiringTypes;
    }
    
}
