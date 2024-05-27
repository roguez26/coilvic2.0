package mx.fei.coilvicapp.logic.institutionalRepresentative;

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

/**
 *
 * @author ivanr
 */
public class InstitutionalRepresentativeDAO implements IInstitutionalRepresentative {

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

    @Override
    public int updateInstitutionalRepresentative(InstitutionalRepresentative institutionalRepresentative) throws DAOException {
        int result = 0;
        System.out.println(institutionalRepresentative + " entra update");
        if (validateInstitutionalRepresentativeForUpdate(institutionalRepresentative)) {
            result = updateInstitutionalRepresentativeTransaction(institutionalRepresentative);
        }
        return result;
    }

    @Override
    public int deleteInstitutionalRepresentative(InstitutionalRepresentative institutionalRepresentative) throws DAOException {
        int result = -1;
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

    public boolean validateInstitutionalRepresentativeForUpdate(InstitutionalRepresentative institutionalRepresentative) throws DAOException {
        InstitutionalRepresentative oldInstitutionalRepresentative = getInstitutionalRepresentativeById(institutionalRepresentative.getIdInstitutionalRepresentative());
        boolean result = true;

        if (!oldInstitutionalRepresentative.getEmail().equals(institutionalRepresentative.getEmail())) {
            result = !checkEmailDuplication(institutionalRepresentative);
        }
        return result;
    }

    public int insertInstitutionalRepresentativeTransaction(InstitutionalRepresentative institutionalRepresentative) throws DAOException {
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

    public int updateInstitutionalRepresentativeTransaction(
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
