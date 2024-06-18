package mx.fei.coilvicapp.logic.hiringcategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;

public class HiringCategoryDAO implements IHiringCategory{
       
    /**
     * Verifica si hay al menos una categoría de contratación registrada en la base de datos.
     * 
     * @return true si hay al menos una categoría de contratación registrada, false si no hay ninguna
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */    
    @Override
    public boolean isThereAtLeastOneHiringCategory() throws DAOException {
        boolean result = false;
        String statement = "SELECT EXISTS(SELECT 1 FROM categoriacontratación LIMIT 1) AS hay_registros;";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                result = resultSet.getBoolean("hay_registros");
            }
        } catch (SQLException exception) {
            Log.getLogger(HiringCategoryDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible verificar que haya categorias de contratacion", Status.WARNING);
        }
        return result;
    }
    
    /**
     * Registra una nueva categoría de contratación en la base de datos.
     * 
     * @param hiringCategory El objeto HiringCategory que representa la categoría de contratación a registrar
     * @return El número de registros afectados en la base de datos (debería ser 1)
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public int registerHiringCategory(HiringCategory hiringCategory) throws DAOException {
        int result = 0;

        if (!checkNameDuplication(hiringCategory)) {
            result = insertHiringCategoryTransaction(hiringCategory);
        }
        return result;        
    }
    
    /**
     * Actualiza la información de una categoría de contratación en la base de datos.
     * 
     * @param newHiringCategory El objeto HiringCategory con la nueva información
     * @return El número de registros actualizados en la base de datos (debería ser 1)
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public int updateHiringCategory(HiringCategory newHiringCategory) throws DAOException {   
        int result = 0;

        if (!checkNameDuplication(newHiringCategory)) {
            result = updateHiringCategoryTransaction(newHiringCategory);
        }
        return result;          
    }  
    
    /**
     * Elimina una categoría de contratación de la base de datos por su ID.
     * 
     * @param idHiringCategory El ID de la categoría de contratación a eliminar
     * @return El número de registros eliminados en la base de datos (debería ser 1)
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public int deleteHiringCategory(int idHiringCategory) throws DAOException {
        int result = -1;
        String statement = "DELETE FROM categoriacontratación WHERE idCategoriaContratación = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement)) {            
            preparedStatement.setInt(1, idHiringCategory);            
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Log.getLogger(HiringCategoryDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar la categoria academica", Status.WARNING);
        }
        return result;
    }
    
    /**
     * Obtiene una categoría de contratación por su nombre desde la base de datos.
     * 
     * @param hiringCategoryName El nombre de la categoría de contratación a buscar
     * @return El objeto HiringCategory si se encuentra, null si no se encuentra
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public HiringCategory getHiringCategoryByName(String academicAreaName) throws DAOException {
        HiringCategory hiringCategory = new HiringCategory();
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM categoriacontratación WHERE nombre = ?";

        try (Connection connection = databaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, academicAreaName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    hiringCategory.setIdHiringCategory(resultSet.getInt("idCategoriaContratación"));
                    hiringCategory.setName(resultSet.getString("nombre"));
                }
            } 
        } catch (SQLException exception) {
            Log.getLogger(HiringCategoryDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener la categoria de contratacion", Status.WARNING);
        } 
        return hiringCategory;         
    }
    
    /**
     * Obtiene una categoría de contratación por su ID desde la base de datos.
     * 
     * @param idHiringCategory El ID de la categoría de contratación a buscar
     * @return El objeto HiringCategory si se encuentra, null si no se encuentra
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public HiringCategory getHiringCategoryById(int idAcademicArea) throws DAOException {
        HiringCategory hiringCategory = new HiringCategory();
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM categoriacontratación WHERE idCategoriaContratación = ?";

        try (Connection connection = databaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, idAcademicArea);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    hiringCategory.setIdHiringCategory(resultSet.getInt("idCategoriaContratación"));
                    hiringCategory.setName(resultSet.getString("nombre"));
                }
            } 
        } catch (SQLException exception) {
            Log.getLogger(HiringCategoryDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener la categoria de contratacion", Status.WARNING);
        } 
        return hiringCategory;         
    }    
    
    /**
     * Obtiene todas las categorías de contratación registradas en la base de datos.
     * 
     * @return Una lista de objetos HiringCategory con todas las categorías de contratación encontradas
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public ArrayList<HiringCategory> getHiringCategories() throws DAOException {
        ArrayList<HiringCategory> hiringCategories = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM categoriacontratación";
        
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {                    
                    HiringCategory hiringCategory = new HiringCategory();
                    hiringCategory.setIdHiringCategory(resultSet.getInt("idCategoriaContratación"));
                    hiringCategory.setName(resultSet.getString("nombre"));
                    hiringCategories.add(hiringCategory);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(HiringCategoryDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener las categorias de contratacion", Status.WARNING);
        } 
        return hiringCategories;
    }
    
    private int insertHiringCategoryTransaction(HiringCategory hiringCategory) throws DAOException {
        int result = -1;
        String statement = "INSERT INTO categoriacontratación(nombre) VALUES(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement, 
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, hiringCategory.getName());
            preparedStatement.executeUpdate();   
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                    System.out.println(result);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(HiringCategoryDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar la categoria de contratacion", Status.WARNING);
        } 
        return result;
    }

    private boolean checkNameDuplication(HiringCategory hiringCategory) throws DAOException {
        HiringCategory hiringCategoryAux;
        int idHiringCategory = 0;

        try {
            hiringCategoryAux = getHiringCategoryByName(hiringCategory.getName());
            idHiringCategory = hiringCategoryAux.getIdHiringCategory();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde", Status.WARNING);
        }
        if (idHiringCategory != hiringCategory.getIdHiringCategory() && idHiringCategory > 0) {
            throw new DAOException("La categoria de contratacion ya se encuentra registrada", Status.WARNING);
        }
        return false;
    }     
    
    private int updateHiringCategoryTransaction(HiringCategory newHiringCategory) throws DAOException {
        int result = -1;
        String statement = "UPDATE categoriacontratación SET nombre = ? WHERE idCategoriaContratación = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, newHiringCategory.getName());
            preparedStatement.setInt(2, newHiringCategory.getIdHiringCategory());            
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Log.getLogger(HiringCategoryDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible actualizar la categoria de contratacion", Status.WARNING);
        }
        return result;
    }
     
}
