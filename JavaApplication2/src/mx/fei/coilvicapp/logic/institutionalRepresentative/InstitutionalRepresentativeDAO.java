package mx.fei.coilvicapp.logic.institutionalRepresentative;

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
public class InstitutionalRepresentativeDAO implements IInstitutionalRepresentative {

    @Override
    public int registerInstitutionalRepresentative(InstitutionalRepresentative institutionalRepresentative) throws DAOException {
        checkEmailDuplication(institutionalRepresentative);
        return insertInstitutionalRepresentative(institutionalRepresentative);
    }

    public int insertInstitutionalRepresentative(InstitutionalRepresentative institutionalRepresentative) throws DAOException {
        int result = -1;
        Connection connection;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "INSERT INTO RepresentanteInstitucional(nombre, apellidoPaterno, apellidoMaterno, correo, telefono,"
                + " iduniversidad) values (?, ?, ?, ?, ?, ?)";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = intializeStatement(connection, statement, institutionalRepresentative);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible registrar al representante institucional", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(InstitutionalRepresentativeDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return result;
    }
    
    @Override
    public int updateInstitutionalRepresentative(InstitutionalRepresentative institutionalRepresentative) throws DAOException {
        int result = -1;
        Connection connection;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "UPDATE RepresentanteInstitucional SET nombre=?, apellidoPaterno=?, apellidoMaterno=?, correo=?, "
                + "telefono=?, iduniversidad=? WHERE idRepresentante=?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = intializeStatement(connection, statement, institutionalRepresentative);
            preparedStatement.setInt(7, institutionalRepresentative.getIdInstitutionalRepresentative());
             preparedStatement.executeUpdate();
             resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(InstitutionalRepresentativeDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return result;
    }

    @Override
    public int deleteInstitutionalRepresentative(InstitutionalRepresentative institutionalRepresentative) throws DAOException {
        int result = -1;
        Connection connection;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "DELETE FROM RepresentanteInstitucional WHERE idrepresentante=?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, institutionalRepresentative.getIdInstitutionalRepresentative());
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(InstitutionalRepresentativeDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return result;
    }

    public ArrayList<InstitutionalRepresentative> getAllInstitutionalRepresentatives() throws DAOException {
        ArrayList<InstitutionalRepresentative> representativesList = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        InstitutionalRepresentative instance;
        String statement = "SELECT * FROM RepresentanteInstitucional";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                instance = initializeInstitutionalRepresentative(resultSet);
                representativesList.add(instance);
            }
        } catch (SQLException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(InstitutionalRepresentativeDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return representativesList;
    }

    public InstitutionalRepresentative getInstitutionalRepresentativeById(int idInstitutionalRepresentative) throws DAOException {
        InstitutionalRepresentative institutionalRepresentative = null;
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM RepresentanteInstitucional WHERE IdRepresentante=?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, idInstitutionalRepresentative);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                institutionalRepresentative = initializeInstitutionalRepresentative(resultSet);
            }
        } catch (SQLException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(InstitutionalRepresentativeDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return institutionalRepresentative;
    }

    public InstitutionalRepresentative getInstitutionalRepresentativeByEmail(String InstitutionalRepresentativeEmail) throws DAOException {
        InstitutionalRepresentative institutionalRepresentative = null;
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM representanteinstitucional WHERE correo=?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, InstitutionalRepresentativeEmail);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                institutionalRepresentative = initializeInstitutionalRepresentative(resultSet);
            }
        } catch (SQLException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(InstitutionalRepresentativeDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return institutionalRepresentative;
    }

    public boolean checkEmailDuplication(InstitutionalRepresentative institutionalRepresentative) throws DAOException {
        if (getInstitutionalRepresentativeByEmail(institutionalRepresentative.getEmail()) != null) {
            throw new DAOException("El correo ya se encuentra registrado", Status.WARNING);
        }
        return true;
    }

    private PreparedStatement intializeStatement(Connection connection, String statement, InstitutionalRepresentative institutionalRepresentative) throws SQLException {
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

    private InstitutionalRepresentative initializeInstitutionalRepresentative(ResultSet resultSet) throws SQLException {
        InstitutionalRepresentative instance = new InstitutionalRepresentative();
        instance.setIdInstitutionalRepresentative(resultSet.getInt("IdRepresentante"));
        instance.setName(resultSet.getString("Nombre"));
        instance.setPaternalSurname(resultSet.getString("ApellidoPaterno"));
        instance.setMaternalSurname(resultSet.getString("ApellidoMaterno"));
        instance.setEmail(resultSet.getString("Correo"));
        instance.setPhoneNumber(resultSet.getString("Telefono"));
        instance.setIdUniversity(resultSet.getInt("IdUniversidad"));
        return instance;
    }

}
