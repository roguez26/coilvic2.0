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
import java.util.ArrayList;
import log.Log;

/**
 *
 * @author ivanr
 */
public class UniversityDAO implements IUniversity {

    @Override
    public int registerUniversity(University university) throws DAOException {
        int result = 0;

        if (!checkNameDuplication(university)) {
            if (checkCountryExistence(university.getCountry().getIdCountry())) {
                result = insertUniversityTransaction(university);
            }
        }
        return result;
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

        if (validateUniversityForDelete(idUniversity)) {
            result = deleteUniversityTransaction(idUniversity);
        }
        return result;
    }

    private boolean checkNameDuplication(University university) throws DAOException {
        University universityForCheck;

        try {
            universityForCheck = getUniversityByName(university.getName());
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente mas tarde", Status.ERROR);
        }
        if (universityForCheck.getIdUniversity() != university.getIdUniversity() && universityForCheck.getIdUniversity() > 0) {
            throw new DAOException("El nombre de esta universidad ya esta registrado", Status.WARNING);
        }
        return false;
    }

    public boolean checkCountryExistence(int idCountry) throws DAOException {
        CountryDAO countryDAO = new CountryDAO();
        Country country;

        try {
            country = countryDAO.getCountryById(idCountry);
        } catch (DAOException exception) {
            throw new DAOException("No se pudo hacer la validacion para registrar la universidad", Status.ERROR);
        }
        if (country.getIdCountry() <= 0) {
            throw new DAOException("Este pais aun no se encuentra registrado", Status.WARNING);
        }
        return true;
    }

    public boolean validateUniversityForDelete(int idUniversity) throws DAOException {
        InstitutionalRepresentativeDAO institutionalRepresentativeDAO = new InstitutionalRepresentativeDAO();
        InstitutionalRepresentative intitutionalRepresentative = new InstitutionalRepresentative();

        try {
            intitutionalRepresentative = institutionalRepresentativeDAO.getInstitutionalRepresentativeByUniversityId(idUniversity);
        } catch (DAOException exception) {
            Log.getLogger(UniversityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible realizar la validacion para eliminar la universidad", Status.ERROR);
        }
        if (intitutionalRepresentative.getIdInstitutionalRepresentative() > 0) {
            throw new DAOException("No se pudo eliminar la universidad debido a que existen representates relacionados con esta universidad", Status.WARNING);
        }
        return true;
    }

    public boolean validateUniversityForUpdate(University university) throws DAOException {
        University oldUniversity = getUniversityById(university.getIdUniversity());
        boolean result = true;

        if (!oldUniversity.getName().equals(university.getName())) {
            result = !checkNameDuplication(university);
        }
        return result;
    }

    public int insertUniversityTransaction(University university) throws DAOException {
        int result = -1;
        String statement = "INSERT INTO Universidad(nombre, acronimo, jurisdiccion, ciudad, idPais) values (?, ?, ?, ?, ?)";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = initializeStatement(connection, statement, university);) {
            result = preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(UniversityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar la universidad", Status.ERROR);
        }
        return result;
    }

    public int updateUniversityTransaction(University university) throws DAOException {
        int result = -1;
        String statement = "UPDATE Universidad SET nombre = ?, acronimo = ?, jurisdiccion = ?, ciudad = ?, "
                + "idPais = ? WHERE idUniversidad = ?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = initializeStatement(connection, statement, university)) {
            preparedStatement.setInt(6, university.getIdUniversity());
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(UniversityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible actualizar la universidad", Status.ERROR);
        }
        return result;
    }

    public int deleteUniversityTransaction(int idUniversity) throws DAOException {
        int result = -1;
        String statement = "DELETE FROM Universidad where idUniversidad=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, idUniversity);
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(UniversityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar la universidad", Status.ERROR);
        }
        return result;
    }

    @Override
    public ArrayList<University> getAllUniversities() throws DAOException {
        ArrayList<University> universitiesList = new ArrayList<>();
        String statement = "SELECT * FROM Universidad";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareCall(statement); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                universitiesList.add(initializeUniversity(resultSet));
            }
        } catch (SQLException exception) {
            Log.getLogger(UniversityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener las universidades", Status.ERROR);
        }
        return universitiesList;
    }

    public University getUniversityById(int idUniversity) throws DAOException {
        University university = new University();
        String statement = "SELECT * FROM Universidad WHERE idUniversidad=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareCall(statement)) {
            preparedStatement.setInt(1, idUniversity);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    university = initializeUniversity(resultSet);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(UniversityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener la universidad", Status.ERROR);
        }
        return university;
    }

    public University getUniversityByCountryId(int idCountry) throws DAOException {
        University university = new University();
        String statement = "SELECT * FROM Universidad WHERE idPais=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareCall(statement);) {
            preparedStatement.setInt(1, idCountry);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    university = initializeUniversity(resultSet);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(UniversityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener la universidad", Status.ERROR);
        }
        return university;
    }

    @Override
    public University getUniversityByName(String universityName) throws DAOException {
        University university = new University();
        String statement = "SELECT * FROM Universidad WHERE nombre=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, universityName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    university = initializeUniversity(resultSet);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(UniversityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener la universidad", Status.ERROR);
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

    private University initializeUniversity(ResultSet resultSet) throws DAOException {
        University university = new University();
        CountryDAO countryDAO = new CountryDAO();

        try {
            university.setIdUniversity(resultSet.getInt("IdUniversidad"));
            university.setName(resultSet.getString("Nombre"));
            university.setAcronym(resultSet.getString("Acronimo"));
            university.setJurisdiction(resultSet.getString("Jurisdiccion"));
            university.setCity(resultSet.getString("Ciudad"));
            university.setCountry(countryDAO.getCountryById(resultSet.getInt("IdPais")));
        } catch (SQLException exception) {
            Log.getLogger(UniversityDAO.class).error(exception.getMessage(), exception);
        }
        return university;
    }
}
