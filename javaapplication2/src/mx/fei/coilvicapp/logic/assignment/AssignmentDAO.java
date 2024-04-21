package mx.fei.coilvicapp.logic.assignment;

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
import mx.fei.coilvicapp.logic.implementations.Status;

/*
 * @author d0ubl3_d
 */
public class AssignmentDAO implements IAssignment {

    @Override
    public int insertAssignment(Assignment assignment) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        // TODO Hacer procedimiento almacenado para insertar, ya ahi se mete la fecha
        String statement = "insert into Actividad (idProyectoColaborativo, nombre, descripcion,"
                + " fecha, ruta) values (?, ?, ?, ?, ?)";
        DatabaseManager databaseManager = new DatabaseManager();
        int rowsAffected = -1;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, assignment.getIdColaborativeProject());
            preparedStatement.setString(2, assignment.getName());
            preparedStatement.setString(3, assignment.getDescription());
            preparedStatement.setString(4, assignment.getDate());
            preparedStatement.setString(5, assignment.getPath());

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(AssignmentDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible registrar la actividad", Status.WARNING);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(AssignmentDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return rowsAffected;
    }

    @Override
    public List<Assignment> getAssignmentsByIdProjectColaborative(int idColaborativeProject) throws DAOException {
        List<Assignment> assignments = new ArrayList<>();
        Assignment assignment = new Assignment();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "select * from Actividad where idProyectoColaborativo = ?";
        DatabaseManager databaseManager = new DatabaseManager();

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, idColaborativeProject);

            resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                assignment.setIdColaborativeProject(resultSet.getInt("idProyectoColaborativo"));
                assignment.setName(resultSet.getString("nombre"));
                assignment.setDescription(resultSet.getString("descripcion"));
                assignment.setDate(resultSet.getString("fecha"));
                assignment.setPath(resultSet.getString("ruta"));
                assignments.add(assignment);
            }
        } catch (SQLException exception) {
            Logger.getLogger(AssignmentDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible recuperar las actividades", Status.WARNING);
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
                Logger.getLogger(AssignmentDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return assignments;
    }
    
    @Override
    public int updateAssignment(Assignment assignment) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        // TODO procedimiento almacenado para que se meta automaticamente la fecha
        String statement = "update Actividad set nombere = ?,"
        + " descripcion = ?, fecha = ?, ruta = ? where idActividad = ?)";
        DatabaseManager databaseManager = new DatabaseManager();
        int rowsAffected = -1;
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setString(1, assignment.getName());
            preparedStatement.setString(2, assignment.getDescription());
            preparedStatement.setString(3, assignment.getDate());
            preparedStatement.setString(4, assignment.getPath());
            
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(AssignmentDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible actualizar la actividad", Status.WARNING);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(AssignmentDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return rowsAffected;
    }
    
    @Override
    public int deleteAssignmentByIdAssignment(int idAssignment) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "delete from Profesor where idProfesor = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        int rowsAffected = -1;
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setInt(1, idAssignment);
            
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(AssignmentDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible eliminar la actividad", Status.WARNING);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(AssignmentDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return rowsAffected;
    }
}
