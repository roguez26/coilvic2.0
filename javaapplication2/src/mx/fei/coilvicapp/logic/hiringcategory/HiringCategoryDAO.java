package mx.fei.coilvicapp.logic.hiringcategory;

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

public class HiringCategoryDAO implements IHiringCategory {

    @Override
    public int insertHiringCategory(HiringCategory hiringCategory) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "INSERT INTO categoriacontratación(nombre) VALUES(?)";
        DatabaseManager databaseManager = new DatabaseManager();

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, hiringCategory.getName());

            return preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(HiringCategoryDAO.class.getName()).log(Level.SEVERE, null, exception);
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
                Logger.getLogger(HiringCategoryDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }

    @Override
    public int updateHiringCategory(String newHiringCategory, String hiringCategoryName) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE categoriacontratación SET nombre = ? WHERE nombre = ?";
        DatabaseManager databaseManager = new DatabaseManager();

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, newHiringCategory);
            preparedStatement.setString(2, hiringCategoryName);

            return preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(HiringCategoryDAO.class.getName()).log(Level.SEVERE, null, exception);
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
                Logger.getLogger(HiringCategoryDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }

    @Override
    public int deleteHiringCategory(String hiringCategoryName) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "DELETE FROM categoriacontratación WHERE nombre = ?";
        DatabaseManager databaseManager = new DatabaseManager();

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, hiringCategoryName);

            return preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(HiringCategoryDAO.class.getName()).log(Level.SEVERE, null, exception);
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
                Logger.getLogger(HiringCategoryDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }

    @Override
    public List<String> getHiringCategories() throws DAOException {
        List<String> hiringCategories = new ArrayList<>();
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
                hiringCategories.add(resultSet.getString("nombre"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(HiringCategoryDAO.class.getName()).log(Level.SEVERE, null, exception);
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
                Logger.getLogger(HiringCategoryDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return hiringCategories;
    }

}
