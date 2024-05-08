package mx.fei.coilvicapp.logic.academicarea;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;

public class AcademicAreaDAO implements IAcademicArea{
        
    @Override
    public int registerAcademicArea(AcademicArea academicArea) throws DAOException {
        int result = 0;

        if (!checkNameDuplication(academicArea)) {
            result = insertAcademicAreaTransaction(academicArea);
        }
        return result;        
    }
    
    @Override
    public int updateAcademicArea(AcademicArea newAcademicAreaInformation) throws DAOException {   
        int result = 0;

        if (!checkNameDuplication(newAcademicAreaInformation)) {
            result = updateAcademicAreaTransaction(newAcademicAreaInformation);
        }
        return result;          
    }    
    
    @Override
    public int deleteAcademicArea(int idAcademicArea) throws DAOException {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "DELETE FROM areaacademica WHERE idAreaAcademica = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, idAcademicArea);            
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(AcademicAreaDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible eliminar el area academica", Status.ERROR);
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
    public AcademicArea getAcademicAreaByName(String academicAreaName) throws DAOException {
        AcademicArea academicArea = new AcademicArea();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;        
        ResultSet resultSet = null;        
        String statement = "SELECT * FROM AreaAcademica WHERE nombre = ?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, academicAreaName);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                academicArea.setIdAreaAcademica(resultSet.getInt("idAreaAcademica"));
                academicArea.setName(resultSet.getString("nombre"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(AcademicAreaDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener el area academica", Status.ERROR);
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
    
    @Override
    public AcademicArea getAcademicAreaById(int idAcademicArea) throws DAOException {
        AcademicArea academicArea = new AcademicArea();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;        
        ResultSet resultSet = null;        
        String statement = "SELECT * FROM AreaAcademica WHERE idAreaAcademica = ?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, idAcademicArea);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                academicArea.setIdAreaAcademica(resultSet.getInt("idAreaAcademica"));
                academicArea.setName(resultSet.getString("nombre"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(AcademicAreaDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener el area academica", Status.ERROR);
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
    
    @Override
    public ArrayList<AcademicArea> getAcademicAreas() throws DAOException {
        ArrayList<AcademicArea> academicAreas = new ArrayList<>();
        AcademicArea academicArea = new AcademicArea();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;        
        ResultSet resultSet = null;        
        String statement = "SELECT * FROM AreaAcademica";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                academicArea.setIdAreaAcademica(resultSet.getInt("idAreaAcademica"));
                academicArea.setName(resultSet.getString("nombre"));
                academicAreas.add(academicArea);
            }
        } catch (SQLException exception) {
            Logger.getLogger(AcademicAreaDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener las areas academicas", Status.ERROR);
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
        return academicAreas;
    }
    
    private boolean checkNameDuplication(AcademicArea academicArea) throws DAOException {
        AcademicArea academicAreaAux;
        int idAcademicArea = 0;

        try {
            academicAreaAux = getAcademicAreaByName(academicArea.getName());
            idAcademicArea = academicAreaAux.getIdAreaAcademica();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde", Status.ERROR);
        }
        if (idAcademicArea != academicArea.getIdAreaAcademica() && idAcademicArea > 0) {
            throw new DAOException("El area academica ya se encuentra registrada", Status.WARNING);
        }
        return false;
    }
    
    private int insertAcademicAreaTransaction(AcademicArea academicArea) throws DAOException {
        int result = -1;
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "INSERT INTO areaacademica(nombre) VALUES(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);
            preparedStatement.setString(1, academicArea.getName());
            preparedStatement.executeUpdate();   
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Logger.getLogger(AcademicAreaDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible registrar el area academica", Status.ERROR);
        } finally {
            try {
                if(resultSet != null) {
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
        return result;
    }
    
    private int updateAcademicAreaTransaction(AcademicArea newAcademicAreaInformation) throws DAOException {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE areaacademica SET nombre = ? WHERE idAreaAcademica = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, newAcademicAreaInformation.getName());
            preparedStatement.setInt(2, newAcademicAreaInformation.getIdAreaAcademica());         
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(AcademicAreaDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible actualizar la area academica", Status.ERROR);
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
        
}
