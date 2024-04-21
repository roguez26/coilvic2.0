package mx.fei.coilvicapp.logic.university;

import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.country.Country;
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

    private boolean checkNameDuplication(University university) throws DAOException {
        University instance;

        try {
            instance = getUniversityByName(university.getName());
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente mas tarde", Status.ERROR);
        }
        if (instance.getIdUniversity() != university.getIdUniversity() && instance.getIdUniversity() > 0) {
            throw new DAOException("El nombre de esta universidad ya esta registrado", Status.WARNING);
        }
        return false;
    }

    @Override
    public int registerUniversity(University university) throws DAOException {
        int result = 0;

        if (!checkNameDuplication(university)) {
            if(checkCountryExistence(university.getIdCountry())) {
                result = insertUniversityTransaction(university);
            }
        }
        return result;
    }
    
    public boolean checkCountryExistence(int idCountry) throws DAOException {
        CountryDAO instanceDAO = new CountryDAO();
        Country instance;
        
        try {
            instance = instanceDAO.getCountryById(idCountry);
        } catch (DAOException exception) {
            throw new DAOException ("No se pudo hacer la validacion para registrar la universidad", Status.ERROR);
        }
        if(instance.getIdCountry() <= 0) {
            throw new DAOException("Este pais aun no se encuentra registrado", Status.WARNING);
        }
        return true;
    }
    
    @Override
    public int updateUniversity(University university) throws DAOException {
        int result = 0;

        if (validateUniversityForUpdate(university)) {
            result = updateUniversityTransaction(university);
        }
        return result;
    }
    
    @Override
    public int deleteUniversity(int idUniversity) throws DAOException {
        int result = 0;
        
        if(validateUniversityForDelete(idUniversity)) {
            result = deleteUniversityTransaction(idUniversity);
        }
        return result;
    }
    
    public boolean validateUniversityForDelete(int idUniversity) throws DAOException {
        InstitutionalRepresentativeDAO instanceDAO = new InstitutionalRepresentativeDAO();
        InstitutionalRepresentative instance = new InstitutionalRepresentative();
        
        try {
            instance = instanceDAO.getInstitutionalRepresentativeByUniversityId(idUniversity);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible realizar la validacion para eliminar la universidad", Status.ERROR);
        }
        if(instance.getIdInstitutionalRepresentative() > 0) {
            throw new DAOException("No se pudo eliminar la universidad debido a que existen representates relacionados con esta universidad", Status.WARNING);
        }
        return true;
    }
    
    public boolean validateUniversityForUpdate(University university) throws DAOException {
        University oldUniversity = getUniversityById(university.getIdUniversity());
        boolean result = true;
        
        if(!oldUniversity.getName().equals(university.getName())) {
            result = !checkNameDuplication(university);
        }
        return result;
    }

    public int insertUniversityTransaction(University university) throws DAOException {
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

    public int updateUniversityTransaction(University university) throws DAOException {
        int result = -1;
        Connection connection;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "UPDATE Universidad SET nombre = ?, acronimo = ?, jurisdiccion = ?, ciudad = ?, "
                + "idPais = ? WHERE idUniversidad = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = initializeStatement(connection, statement, university);
            preparedStatement.setInt(6, university.getIdUniversity());
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible actualizar la universidad", Status.ERROR);
        } finally {
            try {
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

    
    public int deleteUniversityTransaction(int idUniversity) throws DAOException {
        int result = -1;
        Connection connection;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "DELETE FROM Universidad where idUniversidad=?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, idUniversity);
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible eliminar la universidad", Status.ERROR);
        } finally {
            try {
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
    public ArrayList<University> getAllUniversities() throws DAOException {
        Connection connection;
        ArrayList<University> universitiesList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM Universidad";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                University university = new University();
                university = initializeUniversity(resultSet);
                universitiesList.add(university);
            }
        } catch (SQLException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener las universidades", Status.ERROR);
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
        return universitiesList;
    }

    public University getUniversityById(int idUniversity) throws DAOException {
        Connection connection;
        University university = new University();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM Universidad WHERE idUniversidad=?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);
            preparedStatement.setInt(1, idUniversity);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                university = initializeUniversity(resultSet);
            }
        } catch (SQLException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener la universidad", Status.ERROR);
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
        return university;
    }
    
    public University getUniversityByCountryId(int idCountry) throws DAOException {
        Connection connection;
        University university = new University();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM Universidad WHERE idPais=?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);
            preparedStatement.setInt(1, idCountry);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                university = initializeUniversity(resultSet);
            }
        } catch (SQLException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener la universidad", Status.ERROR);
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
        return university;
    }

    @Override
    public University getUniversityByName(String universityName) throws DAOException {
        Connection connection;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        University university = new University();
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM Universidad WHERE nombre=?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, universityName);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                university = initializeUniversity(resultSet);
            }
        } catch (SQLException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener la universidad", Status.ERROR);
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
        return university;
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
        instance.setAcronym(resultSet.getString("Acronimo"));
        instance.setJurisdiction(resultSet.getString("Jurisdiccion"));
        instance.setCity(resultSet.getString("Ciudad"));
        instance.setIdCountry(resultSet.getInt("IdPais"));
        return instance;
    }
}
