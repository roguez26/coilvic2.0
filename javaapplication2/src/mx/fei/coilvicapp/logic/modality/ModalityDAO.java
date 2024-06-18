package mx.fei.coilvicapp.logic.modality;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;

public class ModalityDAO implements IModality {
    
    @Override
    public int registerModality(Modality modality) throws DAOException {
        int result = 0;
        if (!checkNameDuplicate(modality)) {
            result = insertModality(modality);
        }
        return result;        
    }
    
    @Override
    public int updateModality(Modality modality) throws DAOException {
        int result = 0;

        if (!checkNameDuplicate(modality)) {
            result = updateModalityPrivate(modality);
        }
        return result;          
    }    
    
    @Override
    public int deleteModality(int idModality) throws DAOException {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "DELETE FROM Modalidad WHERE idModalidad = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setInt(1, idModality);
            
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Log.getLogger(ModalityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar la modalidad", Status.ERROR);
        } finally {            
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Log.getLogger(ModalityDAO.class).error(exception.getMessage(), exception);
            }
        } 
        return result;
    }
    
    @Override
    public Modality getModalityByName(String modalityName) throws DAOException {
        Modality modality = new Modality();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;        
        ResultSet resultSet = null;        
        String statement = "SELECT * FROM Modalidad WHERE nombre = ?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setString(1, modalityName);
            
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                modality.setIdModality(resultSet.getInt("idModalidad"));
                modality.setName(resultSet.getString("nombre"));
            }
        } catch (SQLException exception) {
            Log.getLogger(ModalityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener la modalidad", Status.ERROR);
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
                Log.getLogger(ModalityDAO.class).error(exception.getMessage(), exception);
            }
        }
        return modality;
    }
    
    @Override
    public Modality getModalityByIdModality(int idModality) throws DAOException {        
        Modality modality = new Modality();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;        
        ResultSet resultSet = null;        
        String statement = "SELECT * FROM Modalidad WHERE idModalidad = ?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setInt(1, idModality);
            
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                modality.setIdModality(resultSet.getInt("idModalidad"));
                modality.setName(resultSet.getString("nombre"));
            }
        } catch (SQLException exception) {
            Log.getLogger(ModalityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener la modalidad", Status.ERROR);
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
                Log.getLogger(ModalityDAO.class).error(exception.getMessage(), exception);
            }
        }
        return modality;
    }    
    
    @Override
    public ArrayList<Modality> getModalities() throws DAOException {
        ArrayList<Modality> modalities = new ArrayList<>();
        Modality modality = new Modality();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;        
        ResultSet resultSet = null;        
        String statement = "SELECT * FROM Modalidad";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                modality.setIdModality(resultSet.getInt("idModalidad"));
                modality.setName(resultSet.getString("nombre"));
                modalities.add(modality);
            }
        } catch (SQLException exception) {
            Log.getLogger(ModalityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener las modalidades", Status.ERROR);
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
                Log.getLogger(ModalityDAO.class).error(exception.getMessage(), exception);
            }
        }
        return modalities;
    }
    
    private boolean checkNameDuplicate(Modality modality) throws DAOException {
        Modality auxModality;
        int idModality = 0;

        try {
            auxModality = getModalityByName(modality.getName());
            idModality = auxModality.getIdModality();
        } catch (DAOException exception) {
            throw new DAOException
            ("No fue posible realizar la validacion, intente registrar mas tarde", Status.WARNING);
        }
        if (idModality != modality.getIdModality() && idModality > 0) {
            throw new DAOException("La modalidad ya se encuentra registrado", Status.WARNING);
        }
        return false;
    }
    
    private int insertModality(Modality modality) throws DAOException {
        int result = -1;
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "INSERT INTO Modalidad (nombre) VALUES (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);
            
            preparedStatement.setString(1, modality.getName());
            
            preparedStatement.executeUpdate();   
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Log.getLogger(ModalityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar la modalidad", Status.ERROR);
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
                Log.getLogger(ModalityDAO.class).error(exception.getMessage(), exception);
            }
        }
        return result;
    }
    
    private int updateModalityPrivate(Modality modality) throws DAOException {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE Modalidad SET nombre = ? WHERE idModalidad = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setString(1, modality.getName());
            preparedStatement.setInt(2, modality.getIdModality());
            
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Log.getLogger(ModalityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible actualizar la modalidad", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Log.getLogger(ModalityDAO.class).error(exception.getMessage(), exception);
            }
        }
        return result;
    }    
}
