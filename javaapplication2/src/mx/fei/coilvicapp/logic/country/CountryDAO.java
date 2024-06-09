package mx.fei.coilvicapp.logic.country;

import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.university.University;

public class CountryDAO implements ICountry {

    private boolean checkNameDuplication(Country country) throws DAOException {
        Country countryforCheck;
        int idCountry = 0;

        try {
            countryforCheck = getCountryByName(country.getName());
            idCountry = countryforCheck.getIdCountry();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde", Status.ERROR);
        }
        if (idCountry != country.getIdCountry() && idCountry > 0) {
            throw new DAOException("El nombre ya se encuentra registrado", Status.WARNING);
        }
        return false;
    }

    @Override
    public int registerCountry(Country country) throws DAOException {
        int result = 0;

        if (!checkNameDuplication(country)) {
            result = insertCountryTransaction(country);
        }
        return result;
    }

    @Override
    public int updateCountry(Country country) throws DAOException {
        int result = 0;

        if (validateCountryForUpdate(country)) {
            result = updateCountryTransaction(country);
        }
        return result;
    }

    @Override
    public int deleteCountry(int idCountry) throws DAOException {
        int result = 0;

        if (validateCountryForDelete(idCountry)) {
            result = deleteCountryTransaction(idCountry);
        }
        return result;
    }

    public boolean validateCountryForDelete(int idCountry) throws DAOException {
        UniversityDAO universityDAO = new UniversityDAO();
        University university = new University();

        try {
            university = universityDAO.getUniversityByCountryId(idCountry);
        } catch (DAOException exception) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible realizar la validacion para eliminar el pais", Status.ERROR);
        }
        if (university.getIdUniversity() > 0) {
            throw new DAOException("No se pudo eliminar el pais debido a que existen aun universidades relacionadas a este pais", Status.WARNING);
        }
        return true;
    }

    public boolean validateCountryForUpdate(Country country) throws DAOException {
        Country oldCountry = getCountryById(country.getIdCountry());
        boolean result = true;

        if (!oldCountry.getName().equals(country.getName())) {
            result = !checkNameDuplication(country);
        }
        return result;
    }

    public int insertCountryTransaction(Country country) throws DAOException {
        int result = -1;
        String statement = "INSERT INTO Pais (nombre, codigoPais) VALUES (?, ?)";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);) {
            preparedStatement.setString(1, country.getName());
            preparedStatement.setString(2, country.getCountryCode());
            result = preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(CountryDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar el país", Status.ERROR);
        }
        return result;
    }

    public int updateCountryTransaction(Country country) throws DAOException {
        int result = -1;
        String statement = "UPDATE Pais SET nombre=? codigoPais= ? WHERE idPais=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement);) {
            preparedStatement.setString(1, country.getName());
            preparedStatement.setString(2, country.getCountryCode());
            preparedStatement.setInt(3, country.getIdCountry());
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(CountryDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible actualizar el país", Status.ERROR);
        }
        return result;
    }

    public int deleteCountryTransaction(int idCountry) throws DAOException {
        int result = -1;
        String statement = "DELETE FROM Pais WHERE idPais=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement);) {
            preparedStatement.setInt(1, idCountry);
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(CountryDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar el país", Status.ERROR);
        }
        return result;
    }

    @Override
    public ArrayList<Country> getAllCountries() throws DAOException {
        ArrayList<Country> countries = new ArrayList<>();
        String statement = "SELECT * FROM Pais";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement); ResultSet resultSet = preparedStatement.executeQuery();) {
            while (resultSet.next()) {
                Country country = new Country();
                country.setIdCountry(resultSet.getInt("IdPais"));
                country.setName(resultSet.getString("Nombre"));
                country.setCountryCode(resultSet.getString("CodigoPais"));
                countries.add(country);
            }
        } catch (SQLException exception) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener los países", Status.ERROR);
        }
        return countries;
    }

    @Override
    public Country getCountryById(int idCountry) throws DAOException {
        Country country = new Country();
        String statement = "SELECT * FROM Pais WHERE idPais=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement);) {
            preparedStatement.setInt(1, idCountry);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    country.setIdCountry(resultSet.getInt("IdPais"));
                    country.setName(resultSet.getString("Nombre"));
                    country.setCountryCode(resultSet.getString("CodigoPais"));
                }
            }
        } catch (SQLException exception) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        return country;
    }

    public Country getCountryByName(String countryName) throws DAOException {
        Country country = new Country();
        String statement = "SELECT * FROM Pais WHERE nombre=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement);) {
            preparedStatement.setString(1, countryName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    country.setIdCountry(resultSet.getInt("idPais"));
                    country.setName(resultSet.getString("nombre"));
                    country.setCountryCode(resultSet.getString("CodigoPais"));
                }
            }
        } catch (SQLException exception) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener el país", Status.ERROR);
        }
        return country;
    }

}
