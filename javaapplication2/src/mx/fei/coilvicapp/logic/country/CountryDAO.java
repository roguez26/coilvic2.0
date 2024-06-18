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

    /**
     * Este método se utiliza para verificar que exista al menos un país registrado
     * en la base de datos
     * @return false en caso de que no haya al menos un país registrado, true en caso
     * de haber al menos un registro en la base de datos
     * @throws DAOException Puede lanzar una DAOException en caso de que ocurrra una 
     * excepción del tipo SQL
     */
    @Override
    public boolean isThereAtLeastOneCountry() throws DAOException {
        boolean result = false;
        String statement = "SELECT EXISTS(SELECT 1 FROM Pais LIMIT 1) AS hay_registros;";
        DatabaseManager databaseManager = new DatabaseManager();

        try (Connection connection = databaseManager.getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement); ResultSet resultSet
                        = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                result = resultSet.getBoolean("hay_registros");
            }
        } catch (SQLException exception) {
            Log.getLogger(CountryDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible verificar que haya paises", Status.ERROR);
        }
        return result;
    }

    private boolean checkNameDuplication(Country country) throws DAOException {
        Country countryforCheck;
        int idCountry = 0;

        try {
            countryforCheck = getCountryByName(country.getName());
            idCountry = countryforCheck.getIdCountry();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde",
                    Status.ERROR);
        }
        if (idCountry != country.getIdCountry() && idCountry > 0) {
            throw new DAOException("El nombre ya se encuentra registrado", Status.WARNING);
        }
        return false;
    }
    
    /**
     * Este método se utiliza para registrar un país dentro de la base de datos, público para que pueda
     * ser implementados de la interfaz ICountry
     * @param country Éste es el país el cual se desea registrar
     * @return 0 en caso de que no pueda ser registrado, en otro caso retornará el id generado al 
     * insertarse en la base de datos
     * @throws DAOException Puee lanzar una DAOexception en caso de que el nombre se encuentre
     * duplicado o en caso de que ocurra una excepción del tipo SQL
     */

    @Override
    public int registerCountry(Country country) throws DAOException {
        int result = 0;

        if (!checkNameDuplication(country)) {
            result = insertCountryTransaction(country);
        }
        return result;
    }
    
    /**
     * Este método se utiliza para actualizar un país dentro de la base de datos
     * @param country Éste es el país el cual se desea actualizar con los nuevos datos
     * @return 0 en caso de que no se pueda actualizar, de otro modo retornará el 
     * número de filas afectadas que será 1
     * @throws DAOException Puede lanzar DAOException en caso de que el nuevo nombre
     * que se desea registrar ya se encuentre registrado en la base de datos o en caso de 
     * que ocurra una excepción del tipo SQL
     */

    @Override
    public int updateCountry(Country country) throws DAOException {
        int result = 0;

        if (validateCountryForUpdate(country)) {
            result = updateCountryTransaction(country);
        }
        return result;
    }
    
    /**
     * Este método se utiliza para eliminar un país de la base de datos
     * @param idCountry Éste es el id del país que se desea eliminar
     * @return 0 en caso de que no haya sido posible eliminarlo, de otro modo se
     * retornará el id de las filas afectadas que serpa 1
     * @throws DAOException  Puede lanzar una DAOExceptión en caso de que el 
     * país ya esté enlazado con una universidad o en caso de que ocurrra
     * una excepción del tipo SQL
     */

    @Override
    public int deleteCountry(int idCountry) throws DAOException {
        int result = 0;

        if (validateCountryForDelete(idCountry)) {
            result = deleteCountryTransaction(idCountry);
        }
        return result;
    }
    
    private boolean validateCountryForDelete(int idCountry) throws DAOException {
        UniversityDAO universityDAO = new UniversityDAO();
        University university = new University();

        try {
            university = universityDAO.getUniversityByCountryId(idCountry);
        } catch (DAOException exception) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible realizar la validacion para eliminar el pais", Status.ERROR);
        }
        if (university.getIdUniversity() > 0) {
            throw new DAOException("No se pudo eliminar el pais debido a que existen aun universidades "
                    + "relacionadas a este pais", Status.WARNING);
        }
        return true;
    }

    private boolean validateCountryForUpdate(Country country) throws DAOException {
        Country oldCountry = getCountryById(country.getIdCountry());
        boolean result = true;

        if (!oldCountry.getName().equals(country.getName())) {
            result = !checkNameDuplication(country);
        }
        return result;
    }
    
    private int insertCountryTransaction(Country country) throws DAOException {
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
    
    /**
     * Este método se utiliza para actualizar los datos de un país en la base de datos
     * @param country Éste objeto posee toda la nueva información para el país
     * @return -1 en caso de que 
     * @throws DAOException Puede lanzar una DAOException en caso de que ocurra
     * una excepción del tipo SQL
     */

    public int updateCountryTransaction(Country country) throws DAOException {
        int result = -1;
        String statement = "UPDATE Pais SET nombre=?, codigoPais=? WHERE idPais=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement);) {
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
    
    /**
     * Este método se utiliza para eliminar los datos de un país en la base de datos
     * @param idCountry Éste es el id del país que se desea eliminar
     * @return -1 en caso de que 
     * @throws DAOException Puede lanzar una DAOException en caso de que ocurra
     * una excepción del tipo SQL
     */

    public int deleteCountryTransaction(int idCountry) throws DAOException {
        int result = -1;
        String statement = "DELETE FROM Pais WHERE idPais=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement);) {
            preparedStatement.setInt(1, idCountry);
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(CountryDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar el país", Status.ERROR);
        }
        return result;
    }
    
    /**
     * Este método se utiliza para obtener todos los países que se enecuentran registrados
     * dentro de la base de datos
     * @return Retornar un arreglo de países, en caso de no encontrar retornará un arreglo vacío
     * @throws DAOException Puede lanzar una DAOException en caso de que ocurra una excepción
     * del tipo SQL
     */

    @Override
    public ArrayList<Country> getAllCountries() throws DAOException {
        ArrayList<Country> countries = new ArrayList<>();
        String statement = "SELECT * FROM Pais";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement); ResultSet resultSet
                = preparedStatement.executeQuery();) {
            while (resultSet.next()) {
                Country country = new Country();
                country.setIdCountry(resultSet.getInt("IdPais"));
                country.setName(resultSet.getString("Nombre"));
                country.setCountryCode(resultSet.getString("CodigoPais"));
                countries.add(country);
            }
        } catch (SQLException exception) {
            Log.getLogger(CountryDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener los países", Status.ERROR);
        }
        return countries;
    }
    
    /**
     * Este método se utiliza para obtener un país con base en su id
     * @param idCountry Éste es el id del país que se desea obtener
     * @return Retornará un objeto Country, en caso de no obtenerlo el objeto tendrá los
     * datos vacíos
     * @throws DAOException Puede lanzar una DAOException en caso de que ocurra una excepción
     * del tipo SQL
     */

    @Override
    public Country getCountryById(int idCountry) throws DAOException {
        Country country = new Country();
        String statement = "SELECT * FROM Pais WHERE idPais=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement);) {
            preparedStatement.setInt(1, idCountry);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    country.setIdCountry(resultSet.getInt("IdPais"));
                    country.setName(resultSet.getString("Nombre"));
                    country.setCountryCode(resultSet.getString("CodigoPais"));
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(CountryDAO.class).error(exception.getMessage(), exception);
        }
        return country;
    }

    
    /**
     * Este método se utiliza pra obtener un país con base en su nombre
     * @param countryName Éste es el nombre del país que se desea obtener
     * @return Retornará un objeto Country, en caso de no encontrarlo retornará el objeto
     * con los datos vacíos
     * @throws DAOException Puede lanzar una DAOException en caso de que ocurra una 
     * excepción del tipo SQL
     */
    public Country getCountryByName(String countryName) throws DAOException {
        Country country = new Country();
        String statement = "SELECT * FROM Pais WHERE nombre=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement);) {
            preparedStatement.setString(1, countryName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    country.setIdCountry(resultSet.getInt("idPais"));
                    country.setName(resultSet.getString("nombre"));
                    country.setCountryCode(resultSet.getString("CodigoPais"));
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(CountryDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener el país", Status.ERROR);
        }
        return country;
    }

}
