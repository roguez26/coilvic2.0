package mx.fei.coilvicapp.logic.university;

import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    public int registerUniversity(University university) throws DAOException {
        int idVerification;
        int idNewUniversity = -1;

        idVerification = getUniverstiyIdByName(university);
        if (idVerification > 0) {
            throw new DAOException("La universidad ya se encuentra registrada", Status.WARNING);
        } else {
            idNewUniversity = insertUniversity(university);
        }
        return idNewUniversity;
    }

    public int insertUniversity(University university) throws DAOException {
        int result = -1;
        Connection connection;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "INSERT INTO Universidad(nombre, acronimo, jurisdiccion, ciudad, idPais) "
                + "values (?, ?, ?, ?, ?)";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = initializeStatement(connection, statement, university);
            result = preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible registrar la universidad", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
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

    public int getUniverstiyIdByName(University university) throws DAOException {
        int result = -1;
        Connection connection;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT idUniversidad FROM Universidad WHERE nombre=?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, university.getName());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return result;
    }

    private PreparedStatement initializeStatement(Connection connection, String statement, University university) throws SQLException {
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, university.getName());
        preparedStatement.setString(2, university.getAcronym());
        preparedStatement.setString(3, university.getJurisdiction());
        preparedStatement.setString(4, university.getCity());
        preparedStatement.setInt(5, university.getIdCountry());
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
