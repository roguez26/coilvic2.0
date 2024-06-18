package mx.fei.coilvicapp.logic.university;

import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentative;
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

public class UniversityDAO implements IUniversity {
    
   /**
    * Este método revisa que haya al menos una universidad registrada en la base de datos
    * @return true si hay al menos una universidad, false si no hay al menos una
    * @throws DAOException en caso de que ocurra una excepción del tipo SQL
    */

    @Override
    public boolean isThereAtLeastOneUniversity() throws DAOException {
        boolean result = false;
        String statement = "SELECT EXISTS(SELECT 1 FROM universidad LIMIT 1) AS hay_registros;";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try (Connection connection = databaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                result = resultSet.getBoolean("hay_registros");
            }
        } catch (SQLException exception) {
            Log.getLogger(UniversityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible verificar que haya universidades", Status.ERROR);
        }
        return result;
    }  
    /**
     * Este método se utiliza para registrar una nueva universidad, es implementada para ser pública
     * y poder ser utilizada desde al interfaz
     * @param university Ésta es la universidad que será registrada en la base de datos
     * @return 0 en caso de no poder registrarse, en otro caso de poder registrar la universidad 
     * retornará el id autoincremental generada en la base de datos
     * @throws DAOException Puede lanzar una DAOException en los siguientes casos: cuando se encuentra 
     * una universidad registrada con el mismo nombre, cuando no se encuentra el país con el que se 
     * quiere registrar o cuando ocurre una excepción del tipo SQL
     */
    
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
    
    /**
     * Este método se utiliza para actualizar una universidad en la base de datos, es implementada 
     * para ser pública y poder ser utilizada desde la interfaz IUniversity
     * @param university Ésta es la universidad que será actualizada en la base de datos
     * @return 0 en caso de que no se pudo ser actualizada, en otro caso retornará la cantidad
     * de filas afectadas en la base de datos, lo esperado siempre es 1
     * @throws DAOException Puede lanzar una DAOException en los siguientes casos: cuando ya
     * existe una universidad con el mismo nombre o cuando ocurre una excepción del tipo SQL
     */

    @Override
    public int updateUniversity(University university) throws DAOException {
        int result = 0;

        if (validateUniversityForUpdate(university)) {
            result = updateUniversityTransaction(university);
        }
        return result;
    }
    /**
     * Este método se utiliza para eliminar universidades de la base de datos, es implementada
     * para se pública y poder ser utiliada desde la interfaz IUniversity
     * @param idUniversity Éste es el id asignado de la universidad la cual será actualizada
     * @return 0 en caso de que no se pueda actualizar la universida, en otro caso retornará 
     * la cantidad de filas afectadas que será 1
     * @throws DAOException  Puede lanzar una DAOException en caso de que exista un representante
     * institucional asignado a esta universidad o en caso de que ocurra una excepción
     * del tipo SQL 
     */

    @Override
    public int deleteUniversity(int idUniversity) throws DAOException {
        int result = 0;

        if (validateUniversityForDelete(idUniversity)) {
            result = deleteUniversityTransaction(idUniversity);
        }
        return result;
    }
    
    /**
     * Este método es utilizado para revisar si el nombre de la universidad ya se encuentra registrado 
     * en la base de datos
     * @param university Ésta es la universidad de la cual se tomará el nombre para revisar si su nombre
     * ya se encuentra registrado o no
     * @return false en caso de que el nombre no esté registrado
     * @throws DAOException Puede lanzar una DAOException en caso de que el nombre ya se encuentre 
     * registrado o en caso de que ocurra una excepción del tipo SQL
     */

    private boolean checkNameDuplication(University university) throws DAOException {
        University universityForCheck;

        try {
            universityForCheck = getUniversityByName(university.getName());
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente mas tarde", Status.ERROR);
        }
        if (universityForCheck.getIdUniversity() != university.getIdUniversity() && universityForCheck.
                getIdUniversity() > 0) {
            throw new DAOException("El nombre de esta universidad ya esta registrado", Status.WARNING);
        }
        return false;
    }
    
    /**
     * Este método es utilizado para revisar que el país con el que se quiere registrar la universidad
     * exista
     * @param idCountry Éste es el id del país del que se quiere revisar su existencia
     * @return true en caso de que el país exista
     * @throws DAOException Puede lanzar DAOException en caso de que el país no exista o en caso de
     * que ocurra una excepción del tipo SQL
     */

    private boolean checkCountryExistence(int idCountry) throws DAOException {
        CountryDAO countryDAO = new CountryDAO();
        Country country;

        try {
            country = countryDAO.getCountryById(idCountry);
        } catch (DAOException exception) {
            throw new DAOException("No se pudo hacer la validación para registrar la universidad", 
                    Status.ERROR);
        }
        if (country.getIdCountry() <= 0) {
            throw new DAOException("Este país aún no se encuentra registrado", Status.WARNING);
        }
        return true;
    }

    /**
     * Este método es utilizado para validar que la universidad está en condiciones para ser eliminada
     * @param idUniversity Éste es el id de la universidad de la cual se quiere hacer la validación
     * @return true en caso de que la universidad esté en condiciones para ser eliminada
     * @throws DAOException Puede lanzar DAOException en caso de que aún exista una dependencia 
     * sobre esta universidad o en caso de que ocurra una excepción del tipo SQL
     */
    
    private boolean validateUniversityForDelete(int idUniversity) throws DAOException {
        InstitutionalRepresentativeDAO institutionalRepresentativeDAO = new InstitutionalRepresentativeDAO();
        InstitutionalRepresentative intitutionalRepresentative = new InstitutionalRepresentative();

        try {
            intitutionalRepresentative = institutionalRepresentativeDAO.
                    getInstitutionalRepresentativeByUniversityId(idUniversity);
        } catch (DAOException exception) {
            Log.getLogger(UniversityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible realizar la validacion para eliminar la universidad", 
                    Status.ERROR);
        }
        if (intitutionalRepresentative.getIdInstitutionalRepresentative() > 0) {
            throw new DAOException("No se pudo eliminar la universidad debido a que existen "
                    + "representates relacionados con esta universidad", Status.WARNING);
        }
        return true;
    }
    
    /**
     * Este método se utiliza para validar que una universidad está en condiciones para ser actualizada
     * @param university Ésta es la universidad que será validada
     * @return true en caso de que se encuentre en condiciones, false en caso de que no
     * @throws DAOException Puede lanzar una DAOException en caso de que ocurra una excepción durante
     * la validación
     */

    private boolean validateUniversityForUpdate(University university) throws DAOException {
        University oldUniversity = getUniversityById(university.getIdUniversity());
        boolean result = true;

        if (!oldUniversity.getName().equals(university.getName())) {
            result = !checkNameDuplication(university);
        }
        return result;
    }
    
    /**
     * Esta método se utiliza para insertar las universidades a la base de datos
     * @param university Ésta es la universidad la cual será registrada en la base de datos
     * @return Retornará el id autoincremental generado por la base de datos
     * @throws DAOException Lanzará DAOException en caso de que ocurra una excepción
     * del tipo SQL
     */

    private int insertUniversityTransaction(University university) throws DAOException {
        int result = -1;
        String statement = "INSERT INTO Universidad(nombre, acronimo, jurisdiccion, ciudad, idPais)"
                + " values (?, ?, ?, ?, ?)";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = initializeStatement(connection, statement, university);) {
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
    
    /**
     * Este método es utilizado para actualizar universidades dentro de la base de datos
     * @param university Ésta es la universidad que será actualizada en la base de datos
     * @return Retornará 1 en caso de que se actualice, 0 en caso de que no encuentre la universidad
     * @throws DAOException Puede lanzar una DAOException en caso de que ocurra una excepción
     * del tipo SQL
     */

    private int updateUniversityTransaction(University university) throws DAOException {
        int result = -1;
        String statement = "UPDATE Universidad SET nombre = ?, acronimo = ?, jurisdiccion = ?, ciudad = ?, "
                + "idPais = ? WHERE idUniversidad = ?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement
                preparedStatement = initializeStatement(connection, statement, university)) {
            preparedStatement.setInt(6, university.getIdUniversity());
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(UniversityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible actualizar la universidad", Status.ERROR);
        }
        return result;
    }
    
    /**
     * Este método se utiliza para eliminar universidades de la base de datos
     * @param idUniversity Éste es el id de la universidad la cual se quiere eliminar
     * @return Retornará 1 en caso de que sea eliminada, 0 en caso de que no se encuentre la universidad
     * @throws DAOException Puede lanzar DAOException en caso de que ocurra una excepción 
     * del tipo SQL
     */

    private int deleteUniversityTransaction(int idUniversity) throws DAOException {
        int result = -1;
        String statement = "DELETE FROM Universidad where idUniversidad=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement
                preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, idUniversity);
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(UniversityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar la universidad", Status.ERROR);
        }
        return result;
    }
    
    /**
     * Este método se utiliza para obtener todas las universidades registradas en la base de datos,
     * público para que pueda ser implementado desde la interfaz IUniversity
     * @return Retornará un arreglo de universidades, en caso de que no lo encuentre el arreglo 
     * retornará vacío
     * @throws DAOException Puede lanzar una DAOException en caso de que ocurra una excepción
     * del tipo SQL
     */

    @Override
    public ArrayList<University> getAllUniversities() throws DAOException {
        ArrayList<University> universitiesList = new ArrayList<>();
        String statement = "SELECT * FROM Universidad";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = connection.prepareCall(statement); ResultSet resultSet = 
                        preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                universitiesList.add(initializeUniversity(resultSet));
            }
        } catch (SQLException exception) {
            Log.getLogger(UniversityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener las universidades", Status.ERROR);
        }
        return universitiesList;
    }
    
    /**
     * Este método se utiliza para obtener las universidades que no cuentan con un representante, 
     * público para que pueda ser utilizado desde la interfaz IUniversity
     * @return Retornará un arreglo de universidades, en caso de que no lo encuentre el arreglo 
     * retornará vacío
     * @throws DAOException Puede lanzar una DAOException en caso de que ocurra una excepción
     * del tipo SQL
     */
    
    @Override
    public ArrayList<University> getAvailableUniversities() throws DAOException {
        ArrayList<University> universitiesList = new ArrayList<>();
        String statement = "select * from universidad where idUniversidad not in (select idUniversidad"
                + " from representanteinstitucional)";
        
        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement
                preparedStatement = connection.prepareCall(statement); ResultSet resultSet = 
                        preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                universitiesList.add(initializeUniversity(resultSet));
            }
        } catch (SQLException exception) {
            Log.getLogger(UniversityDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener las universidades", Status.ERROR);
        }
        return universitiesList;
    }
    
    /**
     * Este método se utiliza para recuperar universidades con base en su id
     * @param idUniversity Éste es el id de la universidad la cual se desea recuperar
     * @return Retornará un objeto Universidad, en caso de no encontrarlo el arreglo se retornará
     * vacío
     * @throws DAOException Puede lanzar una DAOException en caso de que ocurra una excepción
     * del tipo SQL
     */

    public University getUniversityById(int idUniversity) throws DAOException {
        University university = new University();
        String statement = "SELECT * FROM Universidad WHERE idUniversidad=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement
                preparedStatement = connection.prepareCall(statement)) {
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
    
     /**
      * Este método se utiliza para recuperar un objeto universidad con base en el id de un país
      * @param idCountry Éste es el id del país del que se quiere recupera la universidad
      * @return Retornará un objeto Universidad, en caso de no encontrar ninguno retornará el objeto 
      * vacío
      * @throws DAOException Puede lanzar una DAOException en caso de que ocurra una excepción
     * del tipo SQL
      */

    public University getUniversityByCountryId(int idCountry) throws DAOException {
        University university = new University();
        String statement = "SELECT * FROM Universidad WHERE idPais=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = connection.prepareCall(statement);) {
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
    
    /**
     * Este método se utiliza para recuperar una universidad con base en su nombre, público para
     * que pude ser implementado desde la interfaz IUniversity
     * @param universityName Éste es el nombre de la universidad el cual se quiere buscar
     * @return Retornará un objeto Universidad, en caso de no encontrar ninguno retornará el objeto 
      * vacío
     * @throws DAOException Puede lanzar una DAOException en caso de que ocurra una excepción
     * del tipo SQL
     */

    @Override
    public University getUniversityByName(String universityName) throws DAOException {
        University university = new University();
        String statement = "SELECT * FROM Universidad WHERE nombre=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement
                preparedStatement = connection.prepareStatement(statement)) {
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
    
    /**
     * Este método se utiliza para incializar un PreparedStatement para que puede ser utilizado 
     * en distintos métodos 
     * @param connection Conexión a la base de datos
     * @param statement Statement de la orden que se quiere realizar en la base de datos
     * @param university Universidad en la cual ser ejecutará la orden
     * @return Retornará el PreparedStatement con todos los datos configurados
     * @throws SQLException Puede lanzar SQLException en caso de que ocurra un error en 
     * la base de datos
     */

    private PreparedStatement initializeStatement(Connection connection, String statement,
            University university) throws SQLException {
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, university.getName());
        preparedStatement.setString(2, university.getAcronym());
        preparedStatement.setString(3, university.getJurisdiction());
        preparedStatement.setString(4, university.getCity());
        preparedStatement.setInt(5, university.getIdCountry());
        return preparedStatement;
    }
    
    /**
     * Este método se utiliza para inicializar una universidad y que pueda ser reutiliado 
     * en distintos métodos
     * @param resultSet ResultSet de la consulta realizada
     * @return Retornará un objeto University, en caso de una SQLException se retornará con
     * los datos vacíos
     * @throws DAOException puede lanzar una DAOException generada por la consulta con base en el
     * id del país
     */

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
