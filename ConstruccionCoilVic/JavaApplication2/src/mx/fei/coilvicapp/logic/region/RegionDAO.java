package mx.fei.coilvicapp.logic.region;

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

public class RegionDAO implements IRegion {
    
    @Override
    public int insertRegion(Region region) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "INSERT INTO region(nombre) VALUES(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, region.getName());
            
            return preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(RegionDAO.class.getName()).log(Level.SEVERE, null, exception);
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
                Logger.getLogger(RegionDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }
    
    @Override
    public int updateRegion(String newRegion, String regionName) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE region SET nombre = ? WHERE nombre = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, newRegion);
            preparedStatement.setString(2, regionName);
            
            return preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(RegionDAO.class.getName()).log(Level.SEVERE, null, exception);
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
                Logger.getLogger(RegionDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }
    
    @Override
    public int deleteRegion(String regionName) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "DELETE FROM region WHERE nombre = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, regionName);
            
            return preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(RegionDAO.class.getName()).log(Level.SEVERE, null, exception);
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
                Logger.getLogger(RegionDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }
    
    
    @Override
    public List<String> getRegions() throws DAOException {
        List<String> regions = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;        
        ResultSet resultSet = null;
        String statement = "SELECT * FROM region";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            resultSet = preparedStatement.executeQuery();

            while (resultSet != null && resultSet.next()) {
                regions.add(resultSet.getString("nombre"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(RegionDAO.class.getName()).log(Level.SEVERE, null, exception);
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
                Logger.getLogger(RegionDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return regions;
    }
    
}
