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
import log.Log;
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
        System.out.println(professor.getUser().getPassword() + " recuperada by email");
        String pw = encryptPassword(password);
        System.out.println(pw + " la encriptada");
        if (professor.getIdProfessor() > 0) {
            if (professor.getUser().getPassword().equals(encryptPassword(password))) {
                result = true;
            } else {
                throw new DAOException("La contraseña proporcinada es incorrecta", Status.WARNING);
            }
        } else {
            throw new DAOException("El correo no se encuentra registrado", Status.WARNING);
        }
        return result;
    }

    @Override
    public int registerUser(User user) throws DAOException {
        int result = insertUserTransaction(user);

        return result;
    }

    @Override
    public int deleteUser(int idUser) throws DAOException {

        return deleteUserTransaction(idUser);
    }

    public int insertUserTransaction(User user) throws DAOException {
        int result = -1;
        String statement = "INSERT INTO usuario(contrasenia, tipo) VALUES (?, ?)";
        String encryptedPassword = encryptPassword(user.getPassword());
        
        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);) {
            preparedStatement.setString(1, encryptedPassword);
            preparedStatement.setString(2, user.getType());
            result = preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(UserDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar el usuario", Status.ERROR);
        }
        return result;
    }

    public int deleteUserTransaction(int idUser) throws DAOException {
        int result = -1;
        String statement = "DELETE FROM usuario WHERE idUsuario = ?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);) {
            preparedStatement.setInt(1, idUser);
            result = preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(UserDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar el usuario del profesor", Status.ERROR);
        }
        return result;
    }

    public String encryptPassword(String password) throws DAOException {
        String encryptedPassword;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());

            StringBuilder hexadecimalString = new StringBuilder();

            for (byte byteIntHashedBytes : hashedBytes) {
                hexadecimalString.append(String.format("%02x", byteIntHashedBytes));
            }

            encryptedPassword = hexadecimalString.toString();
        } catch (NoSuchAlgorithmException exception) {
            Log.getLogger(UserDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible asignar la contraseña al usuario", Status.ERROR);
        }
        return encryptedPassword;
    }
    
    @Override
    public int updateUserPassword(User user) throws DAOException {
        int rowsAffected = -1;
        String statement = "UPDATE usuario SET contrasenia = ? WHERE idUsuario = ?";

        try (Connection connection = new DatabaseManager().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

            preparedStatement.setString(1, encryptPassword(user.getPassword()));
            preparedStatement.setInt(2, user.getIdUser());

            rowsAffected = preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            Log.getLogger(UserDAO.class).error("No fue posible actualizar la contrasenia", exception);
            throw new DAOException("No fue posible actualizar la contrasenia", Status.ERROR);
        }

        return rowsAffected;
    }
    
    @Override
    public User getUserById(int idUser) throws DAOException {
        User user = new User();
        String statement = "SELECT * FROM usuario WHERE idUsuario=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement);) {
            preparedStatement.setInt(1, idUser);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user.setIdUser(resultSet.getInt("idUsuario"));
                    user.setPassword(resultSet.getString("Contrasenia"));
                    user.setType(resultSet.getString("Tipo"));
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(UserDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener la universidad", Status.ERROR);
        }
        return user;
    }
}