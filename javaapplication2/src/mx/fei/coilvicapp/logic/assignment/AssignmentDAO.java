package mx.fei.coilvicapp.logic.assignment;

import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;

public class AssignmentDAO implements IAssignment {

    @Override
    public int registerAssignment(Assignment assignment, CollaborativeProject collaborativeProject)
    throws DAOException {
        int result = -1;
        CollaborativeProjectDAO collaborativeProjectDAO = new CollaborativeProjectDAO();
        String status = "";
        status = collaborativeProjectDAO.checkCollaborativeProjectStatus(collaborativeProject);
        
        if (status.equals("Aceptado")) {
            result = insertAssignment(assignment, collaborativeProject);
        } else if (status.equals("Rechazado")) {
            throw new DAOException
            ("No puede subir actividades a proyecto colaborativo rechazado", Status.WARNING);
        } else if (status.equals("Finalizado")) {
            throw new DAOException
            ("No puede subir actividades a proyecto colaborativo finalizado", Status.WARNING);
        }
        return result;
    }

    private int insertAssignment(Assignment assignment, CollaborativeProject collaborativeProject)
    throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "insert into Actividad "
        + "(idProyectoColaborativo, nombre, descripcion, ruta) "
        + "values (?, ?, ?, ?)";
        DatabaseManager databaseManager = new DatabaseManager();
        int result = -1;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, collaborativeProject.getIdCollaborativeProject());
            preparedStatement.setString(2, assignment.getName());
            preparedStatement.setString(3, assignment.getDescription());
            preparedStatement.setString(4, assignment.getPath());

            result = preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(AssignmentDAO.class).error(exception.getMessage(), exception);     
            throw new DAOException("No fue posible registrar la actividad", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Log.getLogger(AssignmentDAO.class).error(exception.getMessage(), exception);
            }
        }
        return result;
    }

    private Assignment initializeAssignment(ResultSet resultSet) throws DAOException {
        Assignment assignment = new Assignment();

        try {
            assignment.setIdAssignment(resultSet.getInt("idActividad"));
            assignment.setName(resultSet.getString("nombre"));
            assignment.setDescription(resultSet.getString("descripcion"));
            assignment.setDate(resultSet.getString("fecha"));
            assignment.setPath(resultSet.getString("ruta"));
        } catch (SQLException exception) {
            Log.getLogger(AssignmentDAO.class).error(exception.getMessage(), exception);
        }
        return assignment;
    }

    @Override
    public ArrayList<Assignment> getAssignmentsByIdProjectColaborative(int idCollaborativeProject)
    throws DAOException {
        ArrayList<Assignment> assignments = new ArrayList<>();
        Assignment assignment = new Assignment();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "select * from Actividad where idProyectoColaborativo = ?";
        DatabaseManager databaseManager = new DatabaseManager();

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, idCollaborativeProject);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                assignments.add(initializeAssignment(resultSet));
            }
        } catch (SQLException exception) {
            Log.getLogger(AssignmentDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar las actividades", Status.ERROR);
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
                Log.getLogger(AssignmentDAO.class).error(exception.getMessage(), exception);
            }
        }
        return assignments;
    }

    @Override
    public int updateAssignment(Assignment assignment, CollaborativeProject collaborativeProject)
    throws DAOException {
        int result = -1;
        CollaborativeProjectDAO collaborativeProjectDAO = new CollaborativeProjectDAO();
        
        if (collaborativeProjectDAO.checkCollaborativeProjectStatus
        (collaborativeProject).equals("Aceptado")) {
            result = updateAssignmentPrivate(assignment);
        } else {
            throw new DAOException("No puede modificar una actividad de un"
            + " proyecto colaborativo que no está en curso", Status.WARNING);
        }
        return result;
    }

    private int updateAssignmentPrivate(Assignment assignment) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "update Actividad set nombre = ?, ruta = ?,"
                + " descripcion = ?, fecha = NOW() where idActividad = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        int rowsAffected = -1;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, assignment.getName());
            preparedStatement.setString(2, assignment.getPath());
            preparedStatement.setString(3, assignment.getDescription());
            preparedStatement.setInt(4, assignment.getIdAssignment());

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(AssignmentDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible actualizar la actividad", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Log.getLogger(AssignmentDAO.class).error(exception.getMessage(), exception);
            }
        }
        return rowsAffected;
    }

    @Override
    public int deleteAssignment(int idAssignment, CollaborativeProject collaborativeProject)
    throws DAOException {
        int result = -1;
        CollaborativeProjectDAO collaborativeProjectDAO = new CollaborativeProjectDAO();
        
        if (collaborativeProjectDAO.checkCollaborativeProjectStatus
        (collaborativeProject).equals("Aceptado")) {
            result = deleteAssignmentByIdAssignment(idAssignment);
        } else {
            throw new DAOException
            ("No puede eliminar una actividad de un"
            + " proyecto colaborativo que no está en curso", Status.WARNING);
        }
        return result;
    }

    public int deleteAssignmentByIdAssignment(int idAssignment) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "delete from Actividad where idActividad = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        int rowsAffected = -1;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, idAssignment);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(AssignmentDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar la actividad", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Log.getLogger(AssignmentDAO.class).error(exception.getMessage(), exception);
            }
        }
        return rowsAffected;
    }
}
