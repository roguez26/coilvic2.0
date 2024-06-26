package mx.fei.coilvicapp.logic.region;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;

public class RegionDAO implements IRegion {
        
    /**
     * Verifica si hay al menos una región registrada en la base de datos.
     * 
     * @return true si hay al menos una región registrada, false si no hay ninguna
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public boolean isThereAtLeastOneRegion() throws DAOException {
        boolean result = false;
        String statement = "SELECT EXISTS(SELECT 1 FROM region LIMIT 1) AS hay_registros;";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                result = resultSet.getBoolean("hay_registros");
            }
        } catch (SQLException exception) {
            Log.getLogger(RegionDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible verificar que haya regiones", Status.ERROR);
        }
        return result;
    }
        
    /**
     * Registra una nueva región en la base de datos.
     * 
     * @param region El objeto Region que representa la región a registrar
     * @return El número de registros afectados en la base de datos (debería ser 1)
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public int registerRegion(Region region) throws DAOException {
        int result = 0;

        if (!checkNameDuplication(region)) {
            result = insertRegionTransaction(region);
        }
        return result;        
    }
    
    /**
     * Actualiza la información de una región en la base de datos.
     * 
     * @param newRegionInformation El objeto Region con la nueva información
     * @return El número de registros actualizados en la base de datos (debería ser 1)
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public int updateRegion(Region newRegionInformation) throws DAOException {   
        int result = 0;

        if (!checkNameDuplication(newRegionInformation)) {
            result = updateRegionTransaction(newRegionInformation);
        }
        return result;          
    }    
    
    /**
     * Elimina una región de la base de datos por su ID.
     * 
     * @param idRegion El ID de la región a eliminar
     * @return El número de registros eliminados en la base de datos (debería ser 1)
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public int deleteRegion(int idRegion) throws DAOException {
        int result = -1;
        String statement = "DELETE FROM region WHERE idRegion = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement)) {            
            preparedStatement.setInt(1, idRegion);            
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Log.getLogger(RegionDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar la región", Status.ERROR);
        } 
        return result;
    }
    
    /**
     * Obtiene una región por su nombre desde la base de datos.
     * 
     * @param regionName El nombre de la región a buscar
     * @return El objeto Region si se encuentra, null si no se encuentra
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public Region getRegionByName(String regionName) throws DAOException {
        Region region = new Region();
        DatabaseManager databaseManager = new DatabaseManager();    
        String statement = "SELECT * FROM region WHERE nombre = ?";
        
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement);) {            
            preparedStatement.setString(1, regionName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    region.setIdRegion(resultSet.getInt("idRegion"));
                    region.setName(resultSet.getString("nombre"));
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(RegionDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener la región", Status.ERROR);
        } 
        return region;
    }
    
    /**
     * Obtiene una región por su ID desde la base de datos.
     * 
     * @param idRegion El ID de la región a buscar
     * @return El objeto Region si se encuentra, null si no se encuentra
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public Region getRegionById(int idRegion) throws DAOException {
        Region region = new Region();
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM region WHERE idRegion = ?";
        
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement);) {            
            preparedStatement.setInt(1, idRegion);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    region.setIdRegion(resultSet.getInt("idRegion"));
                    region.setName(resultSet.getString("nombre"));
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(RegionDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener la región", Status.ERROR);
        }
        return region;
    }    
    
    /**
     * Obtiene todas las regiones registradas en la base de datos.
     * 
     * @return Una lista de objetos Region con todas las regiones encontradas
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public ArrayList<Region> getRegions() throws DAOException {
        ArrayList<Region> regions = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();    
        String statement = "SELECT * FROM region";
        
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Region region = new Region();                    
                    region.setIdRegion(resultSet.getInt("idRegion"));
                    region.setName(resultSet.getString("nombre"));
                    regions.add(region);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(RegionDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener las regiones", Status.ERROR);
        } 
        return regions;
    }
    
    private boolean checkNameDuplication(Region region) throws DAOException {
        Region regionAux;
        int idRegion = 0;

        try {
            regionAux = getRegionByName(region.getName());
            idRegion = regionAux.getIdRegion();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde", Status.ERROR);
        }
        if (idRegion != region.getIdRegion() && idRegion > 0) {
            throw new DAOException("La región ya se encuentra registrada", Status.WARNING);
        }
        return false;
    }
    
    private int insertRegionTransaction(Region region) throws DAOException {
        int result = -1;
        String statement = "INSERT INTO region(nombre) VALUES(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, region.getName());
            preparedStatement.executeUpdate();   
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(RegionDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar la región", Status.ERROR);
        }
        return result;
    }
    
    private int updateRegionTransaction(Region newRegionInformation) throws DAOException {
        int result = -1;
        String statement = "UPDATE region SET nombre = ? WHERE idRegion = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, newRegionInformation.getName());
            preparedStatement.setInt(2, newRegionInformation.getIdRegion());         
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Log.getLogger(RegionDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible actualizar la región", Status.ERROR);
        }
        return result;
    }
        
}
