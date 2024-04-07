package mx.fei.coilvicapp.logic.student;

import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.implementations.DAOException;

public class StudentDAO implements IStudent {

    @Override
    public int insertStudent(Student student) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "insert into estudiante(nombre, apellidoPaterno, apellidoMaterno, correo, genero, ascendencia) "
                + "values ('?', '?', '?', '?', '?', '?')";
        DatabaseManager databaseManager = new DatabaseManager();

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getPaternalSurname());
            preparedStatement.setString(3, student.getMaternalSurname());
            preparedStatement.setString(4, student.getEmail());
            preparedStatement.setString(5, student.getGender());
            preparedStatement.setString(6, student.getLineage());

            return preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, exception);
            return -1;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }

    @Override
    public List<String> getStudentByEmail(String email) throws DAOException {
        List<String> students = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "select * from estudiante where select * from estudiante where correo = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, email);

            resultSet = preparedStatement.executeQuery();

            while (resultSet != null && resultSet.next()) {
                students.add(resultSet.getString("nombre"));
                students.add(resultSet.getString("apellidoPaterno"));
                students.add(resultSet.getString("apellidoMaterno"));
                students.add(resultSet.getString("correo"));
                students.add(resultSet.getString("genero"));
                students.add(resultSet.getString("ascendencia"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, exception);
            return null;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return students;
    }

}
