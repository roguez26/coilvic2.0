package mx.fei.coilvicapp.logic.academicarea;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;

public class AcademicAreaDAO implements IAcademicArea{
        
    /**
     * Verifica si hay al menos un área académica registrada en la base de datos.
     * 
     * @return true si hay al menos un área académica registrada, false si no hay ninguna
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public boolean isThereAtLeastOneAcademicArea() throws DAOException {
        boolean result = false;
        String statement = "SELECT EXISTS(SELECT 1 FROM areaacademica LIMIT 1) AS hay_registros;";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                result = resultSet.getBoolean("hay_registros");
            }
        } catch (SQLException exception) {
            Log.getLogger(AcademicAreaDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible verificar que haya areas academicas", Status.ERROR);
        }
        return result;
    }
    
    /**
     * Registra una nueva área académica en la base de datos.
     * 
     * @param academicArea El objeto AcademicArea que representa el área académica a registrar
     * @return El número de registros afectados en la base de datos (debería ser 1)
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */    
    @Override
    public int registerAcademicArea(AcademicArea academicArea) throws DAOException {
        int result = 0;

        if (!checkNameDuplication(academicArea)) {
            result = insertAcademicAreaTransaction(academicArea);
        }
        return result;        
    }
    
    /**
     * Actualiza la información de un área académica en la base de datos.
     * 
     * @param newAcademicAreaInformation El objeto AcademicArea con la nueva información
     * @return El número de registros actualizados en la base de datos (debería ser 1)
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */    
    @Override
    public int updateAcademicArea(AcademicArea newAcademicAreaInformation) throws DAOException {   
        int result = 0;

        if (!checkNameDuplication(newAcademicAreaInformation)) {
            result = updateAcademicAreaTransaction(newAcademicAreaInformation);
        }
        return result;          
    }    
    
    /**
     * Elimina un área académica de la base de datos por su ID.
     * 
     * @param idAcademicArea El ID del área académica a eliminar
     * @return El número de registros eliminados en la base de datos (debería ser 1)
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */    
    @Override
    public int deleteAcademicArea(int idAcademicArea) throws DAOException {
        int result = -1;
        String statement = "DELETE FROM areaacademica WHERE idAreaAcademica = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement)) {            
            preparedStatement.setInt(1, idAcademicArea);            
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Log.getLogger(AcademicAreaDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar el area academica", Status.ERROR);
        }
        return result;
    }
    
    /**
     * Obtiene un área académica por su nombre desde la base de datos.
     * 
     * @param academicAreaName El nombre del área académica a buscar
     * @return El objeto AcademicArea si se encuentra, null si no se encuentra
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */    
    @Override
    public AcademicArea getAcademicAreaByName(String academicAreaName) throws DAOException {
        AcademicArea academicArea = new AcademicArea();
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM AreaAcademica WHERE nombre = ?";

        try (Connection connection = databaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, academicAreaName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    academicArea.setIdAreaAcademica(resultSet.getInt("idAreaAcademica"));
                    academicArea.setName(resultSet.getString("nombre"));
                }
            }          
        } catch (SQLException exception) {
            Log.getLogger(AcademicAreaDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener el area academica", Status.ERROR);
        }
        return academicArea;
    }
    
    /**
     * Obtiene un área académica por su ID desde la base de datos.
     * 
     * @param idAcademicArea El ID del área académica a buscar
     * @return El objeto AcademicArea si se encuentra, null si no se encuentra
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */    
    @Override
    public AcademicArea getAcademicAreaById(int idAcademicArea) throws DAOException {
        AcademicArea academicArea = new AcademicArea();
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM AreaAcademica WHERE idAreaAcademica = ?";
        
        try (Connection connection = databaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement)) {            
            preparedStatement.setInt(1, idAcademicArea);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    academicArea.setIdAreaAcademica(resultSet.getInt("idAreaAcademica"));
                    academicArea.setName(resultSet.getString("nombre"));
                }
            } 
        } catch (SQLException exception) {
            Log.getLogger(AcademicAreaDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener el area academica", Status.ERROR);
        }
        return academicArea;
    }    
    
    /**
     * Obtiene todas las áreas académicas registradas en la base de datos.
     * 
     * @return Una lista de objetos AcademicArea con todas las áreas académicas encontradas
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */    
    @Override
    public ArrayList<AcademicArea> getAcademicAreas() throws DAOException {
        ArrayList<AcademicArea> academicAreas = new ArrayList<>();        
        DatabaseManager databaseManager = new DatabaseManager();      
        String statement = "SELECT * FROM AreaAcademica";
        
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    AcademicArea academicArea = new AcademicArea();
                    academicArea.setIdAreaAcademica(resultSet.getInt("idAreaAcademica"));
                    academicArea.setName(resultSet.getString("nombre"));
                    academicAreas.add(academicArea);
                }
            } 
        } catch (SQLException exception) {
            Log.getLogger(AcademicAreaDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener las areas academicas", Status.ERROR);
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
        String statement = "INSERT INTO areaacademica(nombre) VALUES(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, academicArea.getName());
            preparedStatement.executeUpdate();   
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(AcademicAreaDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar el area academica", Status.ERROR);
        }
        return result;
    }
    
    private int updateAcademicAreaTransaction(AcademicArea newAcademicAreaInformation) throws DAOException {
        int result = -1;
        String statement = "UPDATE areaacademica SET nombre = ? WHERE idAreaAcademica = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            
            preparedStatement.setString(1, newAcademicAreaInformation.getName());
            preparedStatement.setInt(2, newAcademicAreaInformation.getIdAreaAcademica());         
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Log.getLogger(AcademicAreaDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible actualizar la area academica", Status.ERROR);
        }
        return result;
    }
        
}
