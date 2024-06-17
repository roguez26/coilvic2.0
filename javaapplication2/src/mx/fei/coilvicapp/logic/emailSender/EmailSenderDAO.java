package mx.fei.coilvicapp.logic.emailSender;

import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import log.Log;

public class EmailSenderDAO implements IEmailSender {

    @Override
    public int registerEmail(EmailSender emailSender) throws DAOException {
        int result = -1;
        String statement = "INSERT INTO correo (asunto, fecha, idProfesorNotificado) VALUES (?, NOW(), ?)";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, emailSender.getSubject());
            preparedStatement.setInt(2, emailSender.getReceiver().getIdProfessor());
            result = preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(EmailSenderDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar el correo", Status.ERROR);
        }
        return result;
    }

    public int deleteEmail(int idEmail) throws DAOException {
        int result = -1;
        String statement = "DELETE FROM correo WHERE idCorreo=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement
                preparedStatement = connection.prepareStatement(statement)) {

            preparedStatement.setInt(1, idEmail);
            result = preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            Log.getLogger(EmailSenderDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar el correo", Status.ERROR);
        }

        return result;
    }
}
