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

public class UserDAO implements IUser {

    /**
     * Este método sirve para poder verificar que los datos del usuarios coinciden
     * con alguno de los registrados en la base de datos
     * @param email Éste es el correo que sirve como usuario para poder encontrar
     * algún profesor que contenga este correo
     * @param password Ésta será la contraseña que tiene el usuario para poder 
     * acceder al sistema
     * @return true en caso de que encuentre un profesor con ese correo y la contraseña
     * coicida con la proporcionada, false si no encontró nada de lo anterior
     * @throws DAOException Puede lanzar DAOException en caso de que la constraseña no 
     * coincida o que no se encuentre el correo, además puede lanzada una DAOException en 
     * caso de que ocurrra una excepción del tipo SQL
     */
    @Override
    public boolean authenticateUser(String email, String password) throws DAOException {
        ProfessorDAO professorDAO = new ProfessorDAO();
        Professor professor = new Professor();
        boolean result = false;

        professor = professorDAO.getProfessorByEmail(email);
        if (professor.getIdProfessor() > 0) {
            if (professor.getUser().getIdUser() > 0 && professor.getUser().getPassword().equals(encryptPassword(password))) {
                result = true;
            } else {
                throw new DAOException("La contraseña proporcionada es incorrecta", Status.WARNING);
            }
        } else {
            throw new DAOException("El correo no se encuentra registrado", Status.WARNING);
        }
        return result;
    }

    /**
     * Este método sirve para poder verificar que los datos del usuarios coinciden
     * con alguno de los registrados en la base de datos
     * @param idAdministrative Éste es el id del usuario del administrativo que
     * se encuentra registrado en la base de datos
     * @param password Ésta será la contraseña que tiene el usuario para poder 
     * acceder al sistema
     * @return true en caso de que encuentre un profesor con ese correo y la contraseña
     * coicida con la proporcionada, false si no encontró nada de lo anterior
     * @throws DAOException Puede lanzar DAOException en caso de que la constraseña no 
     * coincida o que no se encuentre el correo, además puede lanzada una DAOException en 
     * caso de que ocurrra una excepción del tipo SQL 
     */
    @Override
    public boolean authenticateAdministrativeUser(int idAdministrative, String password) throws DAOException {
        User user = new User();
        boolean result = false;

        user = this.getUserById(idAdministrative);
        if (user.getIdUser()> 0) {
            if (user.getPassword().equals(encryptPassword(password))) {
                result = true;
            } else {
                throw new DAOException("La contraseña proporcionada es incorrecta", Status.WARNING);
            }
        } else {
            throw new DAOException("El usuario no se encuentra registrado", Status.WARNING);
        }
        return result;
    }

    /**
     * Éste método sirve para poder registrar un usuario en la base de datos
     * @param user Éste sera el usuario que contiene los datos que serán registrados 
     * en la base de datos
     * @return -1 en caso de no poder insertarlo, de otro modo retornará el id autoincremental
     * generado en la base de datos
     * @throws DAOException 
     */
    @Override
    public int registerUser(User user) throws DAOException {
        int result = insertUserTransaction(user);

        return result;
    }
    
    /**
     * Éste método sirve para eliminar usuarios de la base de datos
     * @param idUser Éste será el id del usuario que se desea será eliminada
     * @return -1 en caso de no poder eliminarlo, de otro modo retornará el número de filas
     * afectadas que será 1
     * @throws DAOException Puede lanzar DAOException en caso de que ocurra una excepción
     * del tipo SQL
     */

    @Override
    public int deleteUser(int idUser) throws DAOException {

        return deleteUserTransaction(idUser);
    }

    private int insertUserTransaction(User user) throws DAOException {
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

    private int deleteUserTransaction(int idUser) throws DAOException {
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
    
    /**
     * Este método es utilizado para poder actualizar usuarios que se encuentran en la base de datos
     * @param user Éste es el usuario que se desea actualizar, contiene los nuevos datos
     * @return -1 en caso de que no se pueda actualizar, de otro modo retornará el número de 
     * filas afectadas que será 1
     * @throws DAOException Puede lanzar DAOException en caso de que ocurra una excepción
     * del tipo SQL  
     */

    @Override
    public int updateUserPassword(User user) throws DAOException {
        int rowsAffected = -1;
        String statement = "UPDATE usuario SET contrasenia = ? WHERE idUsuario = ?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

            preparedStatement.setString(1, encryptPassword(user.getPassword()));
            preparedStatement.setInt(2, user.getIdUser());

            rowsAffected = preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            Log.getLogger(UserDAO.class).error("No fue posible actualizar la contrasenia", exception);
            throw new DAOException("No fue posible actualizar la contrasenia", Status.ERROR);
        }

        return rowsAffected;
    }

    /**
     * Éste método se utiliza para obtener usuarios de la base de datos con base en su id
     * @param idUser Éste es el id del usuario que se desea buscar
     * @return Retornará un objeto User que contendrá los datos del usuario solicitado, 
     * en caso de no encontrarlo retornará el objeto con los datos vacíos
     * @throws DAOException Puede lanzar DAOException en caso de que ocurra una excepción
     * del tipo SQL  
     */
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
