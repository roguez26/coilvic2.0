package mx.fei.coilvicapp.logic.country;

import java.util.ArrayList;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;

public class CountryDAO implements ICountry {

    private final String GET_ALL_COUNTRIES = "SELECT * FROM Pais";
    private final String GET_COUNTRY_BY_ID = "SELECT * FROM Pais WHERE idPais=?";

    public CountryDAO() {

    }

    @Override
    public ArrayList<Country> getAllCountries() throws DAOException {
        ArrayList<Country> countries = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        ResultSet resultSet = null;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(GET_ALL_COUNTRIES);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Country instance = new Country();
                instance.setIdCountry(resultSet.getInt("IdPais"));
                instance.setName(resultSet.getString("Nombre"));
                countries.add(instance);
            }

        } catch (SQLException e) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return countries;
    }

    @Override
    public Country getCountryById(int idCountry) throws DAOException {
        Country country = new Country();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        ResultSet resultSet = null;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(GET_COUNTRY_BY_ID);
            preparedStatement.setInt(1, idCountry);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                country.setIdCountry(resultSet.getInt("IdPais"));
                country.setName(resultSet.getString("Nombre"));
            }
        } catch (SQLException e) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return country;
    }

}
