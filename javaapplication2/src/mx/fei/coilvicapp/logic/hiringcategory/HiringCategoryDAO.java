package mx.fei.coilvicapp.logic.hiringcategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;

public class HiringCategoryDAO implements IHiringCategory{
       
    @Override
    public int registerHiringCategory(HiringCategory hiringCategory) throws DAOException {
        int result = 0;

        if (!checkNameDuplication(hiringCategory)) {
            result = insertHiringCategoryTransaction(hiringCategory);
        }
        return result;        
    }
    
    @Override
    public int updateHiringCategory(HiringCategory newHiringCategory) throws DAOException {   
        int result = 0;

        if (!checkNameDuplication(newHiringCategory)) {
            result = updateHiringCategoryTransaction(newHiringCategory);
        }
        return result;          
    }  
    
    @Override
    public int deleteHiringCategory(int idHiringCategory) throws DAOException {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "DELETE FROM categoriacontratación WHERE idCategoriaContratación = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, idHiringCategory);
            
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(HiringCategoryDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible eliminar la categoria academica", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Logger.getLogger(HiringCategoryDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        } 
        return result;
    }
    
    @Override
    public HiringCategory getHiringCategoryByName(String academicAreaName) throws DAOException {
        HiringCategory hiringCategory = new HiringCategory();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM categoriacontratación WHERE nombre = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, academicAreaName);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                hiringCategory.setIdHiringCategory(resultSet.getInt("idCategoriaContratación"));
                hiringCategory.setName(resultSet.getString("nombre"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(HiringCategoryDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener la categoria de contratacion", Status.ERROR);
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
                Logger.getLogger(HiringCategoryDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return hiringCategory;         
    }
    
    @Override
    public ArrayList<HiringCategory> getHiringCategories() throws DAOException {
        ArrayList<HiringCategory> hiringCategories = new ArrayList<>();
        HiringCategory hiringCategory = new HiringCategory();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;        
        ResultSet resultSet = null;
        String statement = "SELECT * FROM categoriacontratación";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                hiringCategory.setIdHiringCategory(resultSet.getInt("idCategoriaContratación"));
                hiringCategory.setName(resultSet.getString("nombre"));
                hiringCategories.add(hiringCategory);
            }
        } catch (SQLException exception) {
            Logger.getLogger(HiringCategoryDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener las categorias de contratacion", Status.ERROR);
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
                Logger.getLogger(HiringCategoryDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return hiringCategories;
    }
    
    private int insertHiringCategoryTransaction(HiringCategory hiringCategory) throws DAOException {
        int result = -1;
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "INSERT INTO categoriacontratación(nombre) VALUES(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);
            preparedStatement.setString(1, hiringCategory.getName());
            preparedStatement.executeUpdate();   
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }   
        } catch (SQLException exception) {
            Logger.getLogger(HiringCategoryDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible registrar la categoria de contratacion", Status.ERROR);
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
                Logger.getLogger(HiringCategoryDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
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
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde", Status.ERROR);
        }
        if (idHiringCategory != hiringCategory.getIdHiringCategory() && idHiringCategory > 0) {
            throw new DAOException("La categoria de contratacion ya se encuentra registrada", Status.WARNING);
        }
        return false;
    }     
    
    private int updateHiringCategoryTransaction(HiringCategory newHiringCategory) throws DAOException {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE categoriacontratación SET nombre = ? WHERE idCategoriaContratación = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, newHiringCategory.getName());
            preparedStatement.setInt(2, newHiringCategory.getIdHiringCategory());
            
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(HiringCategoryDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible actualizar la categoria de contratacion", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Logger.getLogger(HiringCategoryDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return result;
    }
     
}
