package mx.fei.coilvicapp.logic.institutionalrepresentative;

import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.implementations.Status;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import log.Log;

public class InstitutionalRepresentativeDAO implements IInstitutionalRepresentative {
    
    /**
     * Este método se utiliza para registrar nuevos representantes institucinales en la base de datos, 
     * público para que pueda ser implementado desde la interfaz IInstitutionalRepresentative
     * @param institutionalRepresentative Éste es el nuevo representante que se desea sea registrado
     * @return 0 en caso de que no pueda ser registrado, en otro caso retornará el id autoincremental
     * generado en la base de datos
     * @throws DAOException Puede lanzar DAOException en los siguientes casos: en caso de que el
     * nombre ya se encuentre registrado, en caso de que la universidad no exista o en caso de que 
     * ocurra una excepción del tipo SQL
     */

    @Override
    public int registerInstitutionalRepresentative(InstitutionalRepresentative institutionalRepresentative) throws DAOException {
        int result = 0;

        if (!checkEmailDuplication(institutionalRepresentative)) {
            if (checkUniversityExistence(institutionalRepresentative.getIdUniversity())) {
                result = insertInstitutionalRepresentativeTransaction(institutionalRepresentative);
            }
        }
        return result;
    }
    
    /**
     * Este método se utiliza para actualizar representantes institucionales que se encuentran
     * registrados en la base de datos
     * @param institutionalRepresentative Este es el representante que se desea actualizar
     * @return 0 en caso de que no se pueda actualizar, en otro caso se retornará el número de 
     * filas afectadas que será 1
     * @throws DAOException Puede lanzar DAOException en caso de que ya exista un representante
     * con el mismo correo o que ocurra una excepción del tipo SQL
     */

    @Override
    public int updateInstitutionalRepresentative(InstitutionalRepresentative institutionalRepresentative) throws DAOException {
        int result = 0;
        if (validateInstitutionalRepresentativeForUpdate(institutionalRepresentative)) {
            result = updateInstitutionalRepresentativeTransaction(institutionalRepresentative);
        }
        return result;
    }
    
    /**
     * Este método se utiliza para eliminar representantes institucionales que se encuentren 
     * registrados en la base de datos
     * @param institutionalRepresentative Éste es el representantes que se desea eliminar
     * @return 0 en caso de que no se pueda actualizar, en otro caso se retornará el número de 
     * filas afectadas que será 1
     * @throws DAOException Puede lanzar DAOException en caso de que ya exista un representante
     * con el mismo correo o que ocurra una excepción del tipo SQL
     */

    @Override
    public int deleteInstitutionalRepresentative(InstitutionalRepresentative institutionalRepresentative) throws DAOException {
        int result = 0;
        String statement = "DELETE FROM RepresentanteInstitucional WHERE idrepresentante=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, institutionalRepresentative.getIdInstitutionalRepresentative());
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(InstitutionalRepresentativeDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar al representante", Status.ERROR);
        }
        return result;
    }
    
    /**
     * Este método se utiliza para obtener todos los representantes intitucionales registrados
     * en la base de datos
     * @return Retornará un arreglo de representantes, en caso de no poder obtenerlos el arreglo se
     * retornará vacío
     * @throws DAOException Puede lanzar DAOExceptcion en caso de que ocurra una excepción del tipo
     * SQL 
     */

    @Override
    public ArrayList<InstitutionalRepresentative> getAllInstitutionalRepresentatives() throws DAOException {
        ArrayList<InstitutionalRepresentative> representativesList = new ArrayList<>();
        String statement = "SELECT * FROM RepresentanteInstitucional";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                representativesList.add(initializeInstitutionalRepresentative(resultSet));
            }
        } catch (SQLException exception) {
            Log.getLogger(InstitutionalRepresentativeDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar a los representantes", Status.ERROR);
        }
        return representativesList;
    }
    
    /**
     * Este método se utiliza para obtener representantes institucionales con base en su correo
     * @param institutionalRepresentativeEmail Éste es el correo con el que se desea obtener un
     * representante institucional
     * @return Retornará un objeto InstitucionalRepresentative, en caso de no poder recuperarlo
     * el objeto retornará con datos vacíos
     * @throws DAOException Puede lanzar DAOExceptcion en caso de que ocurra una excepción del tipo
     * SQL 
     */
    
    @Override
    public InstitutionalRepresentative getInstitutionalRepresentativeByEmail(String institutionalRepresentativeEmail) throws DAOException {
        InstitutionalRepresentative institutionalRepresentative = new InstitutionalRepresentative();
        String statement = "SELECT * FROM representanteinstitucional WHERE correo=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement);) {
            preparedStatement.setString(1, institutionalRepresentativeEmail);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    institutionalRepresentative = initializeInstitutionalRepresentative(resultSet);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(InstitutionalRepresentativeDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener al representante", Status.ERROR);
        }
        return institutionalRepresentative;
    }
    
    /**
     * Este método se utiliza para verificar si hay otros representantes que tienen el mismo correo
     * con base en un correo de entrada
     * @param institutionalRepresentative Éste es el representante que posee el correo el cual se
     * desea realizar la verificación
     * @return false en caso de que no haya ninguna representante que tenga ese correo
     * @throws DAOException Puede lanzar DAOException en caso de que exista un representante 
     * institucional con el correo proporcionado o en caso de que ocurra una excepción del tipo
     * SQL
     */

    private boolean checkEmailDuplication(InstitutionalRepresentative institutionalRepresentative) throws DAOException {
        InstitutionalRepresentative institutionalRepresentativeForCheck;
        int idRepresentative = 0;

        try {
            institutionalRepresentativeForCheck = getInstitutionalRepresentativeByEmail(institutionalRepresentative.getEmail());
            idRepresentative = institutionalRepresentativeForCheck.getIdInstitutionalRepresentative();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde", Status.ERROR);
        }
        if (idRepresentative != institutionalRepresentative.getIdInstitutionalRepresentative() && idRepresentative > 0) {
            throw new DAOException("El correo ya se encuentra registrado", Status.WARNING);
        }
        return false;
    }
    
    /**
     * Este método se utiliza para verificar que una universidad que se le asignará a un representante 
     * realmente exista
     * @param idUniversity Éste es el id de la universidad la cual se le quiere asignar al representante
     * @return true en casod que la universidad sí exista
     * @throws DAOException Puede lanzar DAOException en caso que la universidad ya no exista o en caso
     * de que ocurra una excepción del tipo SQL
     */

    private boolean checkUniversityExistence(int idUniversity) throws DAOException {
        UniversityDAO universityDAO = new UniversityDAO();
        University university = new University();

        try {
            university = universityDAO.getUniversityById(idUniversity);
        } catch (DAOException exception) {
            throw new DAOException("No se pudo hacer la validacion para registrar al representante", Status.ERROR);
        }
        if (university.getIdCountry() <= 0) {
            throw new DAOException("Esta universidad aun no se encuentra registrado", Status.WARNING);
        }
        return true;
    }
    
    /**
     * Este método se utiliza para validar que un representante institucional se encuentra en condiciones
     * para poder ser actualizado
     * @param institutionalRepresentative Éste es el representante institucional que se desea actualizar
     * @return true en caso de que el representante pueda ser actualizado, false en caso de que no
     * @throws DAOException Puede lanzar DAOException en caso de que el correo ya se encuentre 
     * registrado o en caso de que ocurra una excepción del tipo SQL 
     */

    private boolean validateInstitutionalRepresentativeForUpdate(InstitutionalRepresentative institutionalRepresentative) throws DAOException {
        InstitutionalRepresentative oldInstitutionalRepresentative = getInstitutionalRepresentativeById(institutionalRepresentative.getIdInstitutionalRepresentative());
        boolean result = true;

        if (!oldInstitutionalRepresentative.getEmail().equals(institutionalRepresentative.getEmail())) {
            result = !checkEmailDuplication(institutionalRepresentative);
        }
        return result;
    }
    
    /**
     * Este método se utiliza para insertar un representante institucional dentro de la base de datos
     * @param institutionalRepresentative Éste es el representante institucional que se desea registrar
     * @return -1 en caso de que no pueda ser insertado, en otro caso retornará el id del representante
     * @throws DAOException Puede lanzar DAOException en caso de que ocurra una excepción del 
     * tipo SQL
     */

    private int insertInstitutionalRepresentativeTransaction(InstitutionalRepresentative institutionalRepresentative) throws DAOException {
        int result = -1;
        String statement = "INSERT INTO RepresentanteInstitucional(nombre, apellidoPaterno, apellidoMaterno, correo, telefono, iduniversidad) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = initializeStatement(connection, statement, institutionalRepresentative)) {
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(InstitutionalRepresentativeDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar al representante institucional", Status.ERROR);
        }
        return result;
    }

    /**
     * Este método se utiliza para actualizar los datos de un representante institucional
     * dentro de la base de datos
     * @param institutionalRepresentative Éste posee los nuevos datos del representante que será
     * actualizado
     * @return -1 en caso de que no pueda ser registrado, de otro modo retornará 1
     * @throws DAOException puede lanzar DAOException en caso de que ocurra una excepción
     * del tipo SQL
     */
    
    private int updateInstitutionalRepresentativeTransaction(
            InstitutionalRepresentative institutionalRepresentative) throws DAOException {
        int result = -1;
        String statement = "UPDATE RepresentanteInstitucional SET nombre=?, apellidoPaterno=?,"
                + " apellidoMaterno=?, correo=?, telefono=? WHERE idRepresentante=?";
        try (Connection connection = new DatabaseManager().getConnection(); 
                PreparedStatement preparedStatement = initializeStatement(
                        connection, statement, institutionalRepresentative)) {
            preparedStatement.setInt(6, institutionalRepresentative.getIdInstitutionalRepresentative());
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(InstitutionalRepresentativeDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible actualizar al representante", Status.ERROR);
        }
        return result;
    }
    
    
    /**
     * Este método se utiliza para poder obtener un representante institucional con base en su id
     * @param idInstitutionalRepresentative Éste es el id del representante que se desea obtener
     * @return Retornará un objeteo InstitucionalRepresentative, en caso de poder encontrarlo
     * retornaráel objeto con los datos vacíos
     * @throws DAOException Puede lanzar una DAOException en caso de que ocurra una excepción
     * del tipo SQL
     */

    public InstitutionalRepresentative getInstitutionalRepresentativeById(int idInstitutionalRepresentative) throws DAOException {
        InstitutionalRepresentative institutionalRepresentative = new InstitutionalRepresentative();
        String statement = "SELECT * FROM RepresentanteInstitucional WHERE IdRepresentante=?";

        try (Connection connection = new DatabaseManager().getConnection(); 
                PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, idInstitutionalRepresentative);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    institutionalRepresentative = initializeInstitutionalRepresentative(resultSet);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(InstitutionalRepresentativeDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener al representante", Status.ERROR);
        }
        return institutionalRepresentative;
    }
    
    /**
     * Este método se utiliza para poder obtener un representante institucional con base en el id
     * de la universidad a la que está asociado
     * @param universityId Éste es el id la universidad de la que se espera obtener un representante
     * institucional
     * @return Retornará un objeteo InstitucionalRepresentative, en caso de poder encontrarlo
     * retornaráel objeto con los datos vacíos
     * @throws DAOException Puede lanzar una DAOException en caso de que ocurra una excepción
     * del tipo SQL
     */

    public InstitutionalRepresentative getInstitutionalRepresentativeByUniversityId(int universityId) throws DAOException {
        InstitutionalRepresentative institutionalRepresentative = new InstitutionalRepresentative();
        String statement = "SELECT * FROM RepresentanteInstitucional WHERE idUniversidad=?";
        
        try (Connection connection = new DatabaseManager().getConnection(); 
                PreparedStatement preparedStatement = connection.prepareStatement(statement);) {
            preparedStatement.setInt(1, universityId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    institutionalRepresentative = initializeInstitutionalRepresentative(resultSet);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(InstitutionalRepresentativeDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener al representante", Status.ERROR);
        }
        return institutionalRepresentative;
    }
    
    /**
     * Este método sirve para inicializar un PreparedStatement para que pueda ser utilizado en 
     * distintos métodos
     * @param connection Conexión a la base de datos
     * @param statement
     * @param institutionalRepresentative
     * @return Retornará el PreparedStatement con todos los datos configurados
     * @throws SQLException Puede lanzar SQLException en caso de que ocurra un error en 
     * la base de datos
     */

    private PreparedStatement initializeStatement(Connection connection, String statement, 
            InstitutionalRepresentative institutionalRepresentative) throws SQLException {
        PreparedStatement preparedStatement;

        preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, institutionalRepresentative.getName());
        preparedStatement.setString(2, institutionalRepresentative.getPaternalSurname());
        preparedStatement.setString(3, institutionalRepresentative.getMaternalSurname());
        preparedStatement.setString(4, institutionalRepresentative.getEmail());
        preparedStatement.setString(5, institutionalRepresentative.getPhoneNumber());
        preparedStatement.setInt(6, institutionalRepresentative.getIdUniversity());
        return preparedStatement;
    }
    
    /**
     * Este método se utiliza para inicializar un representante institucional con base en un 
     * resultset obtenido en una consulta, con el objetivo de ser reutilizado en distintos métodos
     * @param resultSet ResultSet de la consulta realizada
     * @return Retornará un objeto InstitucionalRepresentative, en caso de una SQLException se 
     * retornará con los datos vacíos
     * @throws DAOException puede lanzar una DAOException generada por la consulta con base en el
     * id del país
     */

    private InstitutionalRepresentative initializeInstitutionalRepresentative(ResultSet resultSet) throws DAOException {
        UniversityDAO universityDAO = new UniversityDAO();
        InstitutionalRepresentative instutionalRepresentative = new InstitutionalRepresentative();

        try {
            instutionalRepresentative.setIdInstitutionalRepresentative(resultSet.getInt("IdRepresentante"));
            instutionalRepresentative.setName(resultSet.getString("Nombre"));
            instutionalRepresentative.setPaternalSurname(resultSet.getString("ApellidoPaterno"));
            instutionalRepresentative.setMaternalSurname(resultSet.getString("ApellidoMaterno"));
            instutionalRepresentative.setEmail(resultSet.getString("Correo"));
            instutionalRepresentative.setPhoneNumber(resultSet.getString("Telefono"));
            instutionalRepresentative.setUniversity(universityDAO.getUniversityById(
                    resultSet.getInt("idUniversidad")));
        } catch (SQLException exception) {
            Log.getLogger(InstitutionalRepresentativeDAO.class).error(exception.getMessage(), exception);
        }
        return instutionalRepresentative;
    }
}
