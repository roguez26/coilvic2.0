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
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.university.University;

public class CountryDAO implements ICountry {

    public CountryDAO() {

    }

    private boolean checkNameDuplication(Country country) throws DAOException {
        Country instance;
        int idCountry = 0;

        try {
            instance = getCountryByName(country.getName());
            idCountry = instance.getIdCountry();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde", Status.ERROR);
        }
        if(idCountry != country.getIdCountry() && idCountry > 0) {
            throw new DAOException("El nombre ya se encuentra registrado", Status.WARNING);
        }
        return false;
    }
    
    @Override
    public int registerCountry(Country country) throws DAOException {
        int result = 0;
        
        if(!checkNameDuplication(country)) {
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
        
        if(validateCountryForDelete(idCountry)) {
            result = deleteCountryTransaction(idCountry);
        }
        return result;
    }
    
    public boolean validateCountryForDelete(int idCountry) throws DAOException {
        UniversityDAO instanceDAO = new UniversityDAO();
        University instance = new University();
        
        try {
            instance = instanceDAO.getUniversityByCountryId(idCountry);
        } catch (DAOException exception) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible realizar la validacion para eliminar el pais", Status.ERROR);
        }
        if(instance.getIdUniversity() > 0) {
            throw new DAOException ("No se pudo eliminar el pais debido a que existen aun universidades relacionadas a este pais", Status.WARNING);
        }        
        return true;
    }
    
    public boolean validateCountryForUpdate(Country country) throws DAOException {
        Country oldCountry = getCountryById(country.getIdCountry());
        boolean result = true;
        
        if(!oldCountry.getName().equals(country.getName())) {
            result = !checkNameDuplication(country);
        }
        return result;
    }
    
    public int insertCountryTransaction (Country country) throws DAOException {
        int result = -1;
        Connection connection;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "INSERT INTO Pais (nombre) VALUES (?)";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, country.getName());
            result = preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible registrar el pais", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return result;
    }
    
    public int updateCountryTransaction(Country country) throws DAOException {
        int result = -1;
        Connection connection;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "UPDATE Pais SET nombre=? WHERE idPais=?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, country.getName());
            preparedStatement.setInt(2, country.getIdCountry());
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible actualizar el pais", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return result;
    }
    
    public int deleteCountryTransaction(int idCountry) throws DAOException {
        int result = -1;
        Connection connection;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "DELETE FROM Pais WHERE idPais=?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, idCountry);
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible eliminar el pais", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return result;
    }

    @Override
    public ArrayList<Country> getAllCountries() throws DAOException {
        ArrayList<Country> countries = new ArrayList<>();
        Connection connection;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        ResultSet resultSet = null;
        String statement = "SELECT * FROM Pais";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Country instance = new Country();
                instance.setIdCountry(resultSet.getInt("IdPais"));
                instance.setName(resultSet.getString("Nombre"));
                countries.add(instance);
            }

        } catch (SQLException exception) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return countries;
    }

    @Override
    public Country getCountryById(int idCountry) throws DAOException {
        Country country = new Country();
        Connection connection;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        ResultSet resultSet = null;
        String statement = "SELECT * FROM Pais WHERE idPais=?";
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, idCountry);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                country.setIdCountry(resultSet.getInt("IdPais"));
                country.setName(resultSet.getString("Nombre"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return country;
    }

    public Country getCountryByName(String countryName) throws DAOException {
        Country country = new Country();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM Pais WHERE nombre=?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, countryName);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                country.setIdCountry(resultSet.getInt("idPais"));
                country.setName(resultSet.getString("nombre"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener el pais", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return country;
    }

}
