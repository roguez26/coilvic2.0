package mx.fei.coilvicapp.logic.hiringtype;

import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;

public class HiringTypeDAO implements IHiringType{
    
    @Override
    public boolean isThereAtLeastOneHiringType() throws DAOException {
        boolean result = false;
        String statement = "SELECT EXISTS(SELECT 1 FROM tipocontratación LIMIT 1) AS hay_registros;";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                result = resultSet.getBoolean("hay_registros");
            }
        } catch (SQLException exception) {
            Log.getLogger(HiringTypeDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible verificar que haya tipos de contratacion", Status.ERROR);
        }
        return result;
    }
    
    @Override
    public int registerHiringType(HiringType hiringType) throws DAOException {
        int result = 0;

        if (!checkNameDuplication(hiringType)) {
            result = insertHiringTypeTransaction(hiringType);
        }
        return result;         
    }
    
    @Override
    public int updateHiringType(HiringType newHiringTypeInformation) throws DAOException {   
        int result = 0;

        if (!checkNameDuplication(newHiringTypeInformation)) {
            result = updateHiringTypeTransaction(newHiringTypeInformation);
        }
        return result;          
    } 
    
    @Override
    public int deleteHiringType(int idHiringType) throws DAOException {
        int result = -1;
        String statement = "DELETE FROM tipocontratación WHERE idTipoContratación = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement);) {
            preparedStatement.setInt(1, idHiringType);            
            result = preparedStatement.executeUpdate();       
        } catch (SQLException exception) {
            Log.getLogger(HiringTypeDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar el tipo de contratacion", Status.ERROR);
        }     
        return result;
    }
    
    @Override
    public HiringType getHiringTypeByName(String hiringTypeName) throws DAOException {
        HiringType hiringType = new HiringType();
        DatabaseManager databaseManager = new DatabaseManager();     
        String statement = "SELECT * FROM tipocontratación WHERE nombre = ?";
        
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement);) {          
            preparedStatement.setString(1, hiringTypeName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    hiringType.setIdHiringType(resultSet.getInt("idTipoContratación"));
                    hiringType.setName(resultSet.getString("nombre"));
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(HiringTypeDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener el tipo de contratacion", Status.ERROR);
        } 
        return hiringType;        
    }
    
    @Override
    public HiringType getHiringTypeById(int IdHiringType) throws DAOException {
        HiringType hiringType = new HiringType();
        DatabaseManager databaseManager = new DatabaseManager();     
        String statement = "SELECT * FROM tipocontratación WHERE idTipoContratación = ?";
        
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement);) {          
            preparedStatement.setInt(1, IdHiringType);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    hiringType.setIdHiringType(resultSet.getInt("idTipoContratación"));
                    hiringType.setName(resultSet.getString("nombre"));
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(HiringTypeDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener el tipo de contratacion", Status.ERROR);
        } 
        return hiringType;        
    }    
    
    @Override
    public ArrayList<HiringType> getHiringTypes() throws DAOException {
        ArrayList<HiringType> hiringTypes = new ArrayList<>();        
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM tipocontratación";
        
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement);) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    HiringType hiringType = new HiringType();
                    hiringType.setIdHiringType(resultSet.getInt("idTipoContratación"));
                    hiringType.setName(resultSet.getString("nombre"));
                    hiringTypes.add(hiringType);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(HiringTypeDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener los tipos de contratacion", Status.ERROR);
        } 
        return hiringTypes;
    }
    
    private boolean checkNameDuplication(HiringType hiringType) throws DAOException {
        HiringType hiringTypeAux;
        int idHiringType = 0;

        try {
            hiringTypeAux = getHiringTypeByName(hiringType.getName());
            idHiringType = hiringTypeAux.getIdHiringType();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde", Status.ERROR);
        }
        if (idHiringType != hiringType.getIdHiringType() && idHiringType > 0) {
            throw new DAOException("El tipo de contratacion ya se encuentra registrada", Status.WARNING);
        }
        return false;
    }
    
    private int insertHiringTypeTransaction(HiringType hiringType) throws DAOException {
        int result = -1;
        String statement = "INSERT INTO tipocontratación(nombre) VALUES(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareCall(statement);){
            preparedStatement.setString(1, hiringType.getName());
            preparedStatement.executeUpdate();   
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }    
        } catch (SQLException exception) {
            Log.getLogger(HiringTypeDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar el tipo de contratacion", Status.ERROR);
        } 
        return result;
    }     
    
    private int updateHiringTypeTransaction(HiringType newHiringTypeInformation) throws DAOException {
        int result = -1;
        String statement = "UPDATE tipocontratación SET nombre = ? WHERE idTipoContratación = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement);) {            
            preparedStatement.setString(1, newHiringTypeInformation.getName());
            preparedStatement.setInt(2, newHiringTypeInformation.getIdHiringType());         
            result = preparedStatement.executeUpdate();       
        } catch (SQLException exception) {
            Log.getLogger(HiringTypeDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible actualizar el tipo de contratacion", Status.ERROR);
        }    
        return result;
    }
        
}
