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
    
    /**
     * Este método se utiliza para poder registrar nuevas modalidades dentro
     * de la base de datos
     * @param modality Ésta será la nueva modalidad que se desea será registrada, 
     * modality contiene los nuevos datos que serán registrados
     * @return 0 en caso de que el registro no resulte, de otro modo retornará
     * el id generado automáticamente por la base de datos
     * @throws DAOException Puede lanzar DAOException en caso de que el nombre 
     * ya se encuentre registrao o en caso de que ocurra una excepción del tipo
     * SQL
     */
    @Override
    public int registerModality(Modality modality) throws DAOException {
        int result = 0;
        if (!checkNameDuplicate(modality)) {
            result = insertModality(modality);
        }
        return result;        
    }
    
    /**
     * Este método se utiliza para poder actualizar alguna de las modalidades dentro
     * de la base de datos
     * @param modality Éste contiene los nuevos datos de la modalidad que se desean
     * serán los nuevos
     * @return 0 en caso de que no pueda ser actualizado, 1 en caso de que la actualización
     * sea exitosa
     * @throws DAOException Puede lanzar DAOException en caso de que el nombre 
     * ya se encuentre registrao o en caso de que ocurra una excepción del tipo
     * SQL 
     */
    
    @Override
    public int updateModality(Modality modality) throws DAOException {
        int result = 0;

        if (!checkNameDuplicate(modality)) {
            result = updateModalityPrivate(modality);
        }
        return result;          
    }    
    
    /**
     * Este método se utiliza para poder eliminar modalidades que se encuentren
     * dentro de la base de datos
     * @param idModality Éste sera el id de la modalidad que se desea será eliminada
     * @return -1 en caso de que no pueda ser eliminada, 1 en caso de que la eliminación
     * result exitosa
     * @throws DAOException Puede lanzar DAOException en caso de que el nombre 
     * ya se encuentre registrao o en caso de que ocurra una excepción del tipo
     * SQL  
     */
    
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
    
    /**
     * Este método se utiliza para recupera una modalidad que se encuentre en la base
     * de datos con base en su nombre
     * @param modalityName Éste serpa el nombre por el que se realiza la consulta
     * @return Retornará un objeto modalidad, en caso de que la consulta no resulte
     * el objeto retornará con datos vacíos
     * @throws DAOException Puede lanzar DAOException en caso de que el nombre 
     * ya se encuentre registrao o en caso de que ocurra una excepción del tipo
     * SQL   
     */
    
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
    
    /**
     * Este método se utiliza para poder recuperar una modalidad con base en su id
     * @param idModality Éste será el id de la modalidad que se desea recuperar
     * de la base de datos
     * @return Retornará un objeto modalidad, en caso de que la consulta no resulte
     * el objeto retornará con datos vacíos
     * @throws DAOException Puede lanzar DAOException en caso de que el nombre 
     * ya se encuentre registrao o en caso de que ocurra una excepción del tipo
     * SQL 
     */
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
    
    /**
     * Este método se utiliza para poder recuperar todas la modalidades que se encuentran en la base de 
     * datos
     * @return Un arreglo de modalidades con los datos de todos los registros, de otro modo
     * en caso de no resultar la consulta retornará un arreglo vacío
     * @throws DAOException Puede lanzar DAOException en caso de que el nombre 
     * ya se encuentre registrao o en caso de que ocurra una excepción del tipo
     * SQL  
     */
    
    @Override
    public ArrayList<Modality> getModalities() throws DAOException {
        ArrayList<Modality> modalities = new ArrayList<>();
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
                Modality modality = new Modality();
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
