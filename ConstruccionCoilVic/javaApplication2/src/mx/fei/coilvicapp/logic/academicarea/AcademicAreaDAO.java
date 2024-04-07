package mx.fei.coilvicapp.logic.academicarea;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;

public class AcademicAreaDAO implements IAcademicArea{
    
    @Override
    public int insertAcademicArea(AcademicArea academicArea) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "INSERT INTO areaacademica(nombre) VALUES(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        int result = -1;
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, academicArea.getName());
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(AcademicAreaDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible registrar el area academica", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Logger.getLogger(AcademicAreaDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return result;
    }
    
    @Override
    public int updateAcademicArea(String newAcademicArea, String academicAreaName) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE areaacademica SET nombre = ? WHERE nombre = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, newAcademicArea);
            preparedStatement.setString(2, academicAreaName);
            
            return preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(AcademicAreaDAO.class.getName()).log(Level.SEVERE, null, exception);
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
                Logger.getLogger(AcademicAreaDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }
    
    @Override
    public int deleteAcademicArea(String academicAreaName) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "DELETE FROM areaacademica WHERE nombre = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, academicAreaName);
            
            return preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(AcademicAreaDAO.class.getName()).log(Level.SEVERE, null, exception);
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
                Logger.getLogger(AcademicAreaDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        } 
    }
    
    @Override
    public List<String> getAcademicAreas() throws DAOException {
        List<String> academicArea = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;        
        ResultSet resultSet = null;
        String statement = "SELECT * FROM AreaAcademica";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            resultSet = preparedStatement.executeQuery();

            while (resultSet != null && resultSet.next()) {
                
                academicArea.add(resultSet.getString("nombre"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(AcademicAreaDAO.class.getName()).log(Level.SEVERE, null, exception);
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
                Logger.getLogger(AcademicAreaDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return academicArea;
    }   
}
