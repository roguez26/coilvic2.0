package mx.fei.coilvicapp.logic.university;

import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 *
 * @author ivanr
 */
public class UniversityDAO implements IUniversity {

    public UniversityDAO() {

    }

    @Override
    public int insertUniversity(University university) throws DAOException {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "INSERT INTO Universidad(nombre, jurisdiccion, ciudad, idPais) "
                + "values (?, ?, ?, ?)";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = initializeStatement(connection, statement, university);
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            databaseManager.closeConnection();
        }
        return result;
    }

    @Override
    public int updateUniversity(University university) throws DAOException {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "UPDATE Universidad SET nombre = ?, jurisdiccion = ?, ciudad = ?, "
                + "idPais = ? WHERE idUniversidad = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = initializeStatement(connection, statement, university);
            preparedStatement.setInt(5, university.getIdUniversity());
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            databaseManager.closeConnection();
        }
        return result;
    }

    @Override
    public int deleteUniversity(University university) throws DAOException {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "DELETE FROM Universidad where idUniversidad=?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, university.getIdUniversity());
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            databaseManager.closeConnection();
        }
        return result;
    }

    @Override
    public ArrayList<University> getAllUniversities() throws DAOException {
        Connection connection = null;
        ArrayList<University> universitiesList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        University university = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM Universidad";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                university = initializeUniversity(resultSet);
                universitiesList.add(university);
            }
        } catch (SQLException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            databaseManager.closeConnection();
        }
        return universitiesList;
    }

    @Override
    public University getUniversityById(int idUniversity) throws DAOException {
        Connection connection = null;
        University university = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM Universidad WHERE idUniversidad=?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);
            preparedStatement.setInt(1, idUniversity);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                university = initializeUniversity(resultSet);
            }
        } catch (SQLException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            databaseManager.closeConnection();
        }
        return university;
    }

    private PreparedStatement initializeStatement(Connection connection, String statement, University university) throws SQLException {
        PreparedStatement preparedStatement = null;
        preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, university.getName());
        preparedStatement.setString(2, university.getJurisdiction());
        preparedStatement.setString(3, university.getCity());
        preparedStatement.setInt(4, university.getIdCountry());
        return preparedStatement;
    }

    private University initializeUniversity(ResultSet resultSet) throws SQLException {
        University instance = new University();
        instance.setIdUniversity(resultSet.getInt("IdUniversidad"));
        instance.setName(resultSet.getString("Nombre"));
        instance.setJurisdiction(resultSet.getString("Jurisdiccion"));
        instance.setCity(resultSet.getString("Ciudad"));
        instance.setIdCountry(resultSet.getInt("IdPais"));
        return instance;
    }
}
