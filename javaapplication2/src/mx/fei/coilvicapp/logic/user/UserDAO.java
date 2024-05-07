package mx.fei.coilvicapp.logic.user;

import mx.fei.coilvicapp.logic.professor.ProfessorDAO;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;

/**
 *
 * @author ivanr
 */
public class UserDAO implements IUser {

    @Override
    public boolean authenticateUser(String email, String password) throws DAOException {
        ProfessorDAO professorDAO = new ProfessorDAO();
        Professor professor = new Professor();
        boolean result = false;

        try {
            professor = professorDAO.getProfessorByEmail(email);
        } catch (DAOException exception) {
            throw new DAOException("No fue posible hacer la validacion", Status.WARNING);
        }

        if (professor.getIdProfessor() > 0) {
            if (professor.getUser().getPassword().equals(encryptPassword(password))) {
                result = true;
            } else {
                throw new DAOException ("La contraseña proporcinada es incorrecta", Status.WARNING);
            }
        } else {
            throw new DAOException ("El correo no se encuentra registrado", Status.WARNING);
        }
        return result;
    }

    @Override
    public int registerUser(User user) throws DAOException {
        int result = 0;
        String encryptedPassword = encryptPassword(user.getPassword());

        user.setPassword(encryptedPassword);

        result = insertUserTransaction(user);

        return result;
    }

    @Override
    public int deleteUser(int idProfessor) throws DAOException {

        return deleteUserTransaction(idProfessor);
    }

    public int insertUserTransaction(User user) throws DAOException {
        int result = -1;
        Connection connection;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "INSERT INTO usuario(contraseña, tipo) "
                + "VALUES (?, ?)";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(2, user.getType());
            //preparedStatement.setInt(3, user.getIdProfessor());
            result = preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible registrar el usuario", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return result;
    }

    public int deleteUserTransaction(int idProfessor) throws DAOException {
        int result = -1;
        Connection connection;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "DELETE FROM usuario WHERE idProfessor = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, idProfessor);
            result = preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible eliminar el usuario del profesor", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return result;
    }

    public String encryptPassword(String password) throws DAOException {
        String encryptedPassword = "";

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();

            for (byte byteIntHashedBytes : hashedBytes) {
                hexString.append(String.format("%02x", byteIntHashedBytes));
            }

            encryptedPassword = hexString.toString();
        } catch (NoSuchAlgorithmException exception) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible asignar la contraseña al usuario", Status.ERROR);
        }
        return encryptedPassword;
    }

    public User getUserById(int idUser) throws DAOException {
        Connection connection;
        User user = new User();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM usuario WHERE idusuario=?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);
            preparedStatement.setInt(1, idUser);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                user.setPassword(resultSet.getString("Contraseña"));
                user.setType(resultSet.getString("Tipo"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, exception);
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
                Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return user;
    }
}
