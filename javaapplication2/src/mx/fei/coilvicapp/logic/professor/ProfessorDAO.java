package mx.fei.coilvicapp.logic.professor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;

public class ProfessorDAO implements IProfessor {

    @Override
    public int insertProfessor(Professor professor) throws DAOException {

        Connection connection = null;
        CallableStatement callableStatement = null;
        String statement = "{CALL insertarProfesor(?, ?, ?, ?, ?, ?, ?)}";
        DatabaseManager databaseManager = new DatabaseManager();

        try {
            connection = databaseManager.getConnection();
            callableStatement = connection.prepareCall(statement);
            callableStatement.setString(1, professor.getName());
            callableStatement.setString(2, professor.getPaternalSurname());
            callableStatement.setString(3, professor.getMaternalSurname());
            callableStatement.setString(4, professor.getEmail());
            callableStatement.setString(5, professor.getGender());
            callableStatement.setString(6, professor.getPhoneNumber());
            callableStatement.setInt(7, professor.getUniversity().getIdUniversity());

            return callableStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(ProfessorDAO.class.getName()).log(Level.SEVERE, null, exception);
            return -1;
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(ProfessorDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }

    @Override
    public int updateProfessor(Professor newProfessorInformation) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE profesor SET nombre = ?, apellidoPaterno = ?,"
                + " apellidoMaterno = ?, correo = ?, genero = ?, telefono = ? WHERE idProfesor = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, newProfessorInformation.getName());
            preparedStatement.setString(2, newProfessorInformation.getPaternalSurname());
            preparedStatement.setString(3, newProfessorInformation.getMaternalSurname());
            preparedStatement.setString(4, newProfessorInformation.getEmail());
            preparedStatement.setString(5, newProfessorInformation.getGender());
            preparedStatement.setString(6, newProfessorInformation.getPhoneNumber());
            preparedStatement.setInt(7, newProfessorInformation.getIdProfessor());
            
            return preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(ProfessorDAO.class.getName()).log(Level.SEVERE, null, exception);
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
                Logger.getLogger(ProfessorDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }

    @Override
    public int deleteProfessorByID(int idProfessor) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "DELETE FROM profesor WHERE idProfessor = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, idProfessor);
            
            return preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(ProfessorDAO.class.getName()).log(Level.SEVERE, null, exception);
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
                Logger.getLogger(ProfessorDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        } 
    }

}
