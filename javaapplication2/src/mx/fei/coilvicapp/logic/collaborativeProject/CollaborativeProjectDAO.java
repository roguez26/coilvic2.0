package mx.fei.coilvicapp.logic.collaborativeProject;


import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;
import mx.fei.coilvicapp.logic.course.*;

/*
 * @author d0ubl3_d
 */

public class CollaborativeProjectDAO implements ICollaborativeProject{
    
    public CollaborativeProjectDAO() {        
    }
    
    @Override
    public int registerCollaborativeProject(CollaborativeProject collaborativeProject) throws DAOException {
        int result = -1;
        
        if (!checkDuplicateCollaborativeProjectCode(collaborativeProject)) {
            result = insertCollaborativeProject(collaborativeProject);
        }
        return result;
    }
    
    private boolean checkDuplicateCollaborativeProjectCode(CollaborativeProject collaborativeProject) throws DAOException {
        boolean check = false;
        CollaborativeProject auxCollaborativeProject = new CollaborativeProject();
                
        try {
            auxCollaborativeProject = getCollaborativeProjectByCode(collaborativeProject.getCode());
        } catch (DAOException exception){
            throw new DAOException("No fue posible hacer la validación del proyecto colaborativo", Status.WARNING);
        }
        if (auxCollaborativeProject.getIdCollaborativeProject() != collaborativeProject.getIdCollaborativeProject() &&
        auxCollaborativeProject.getIdCollaborativeProject() != 0) {
            throw new DAOException("El código del proyecto colaborativo ya está en uso. Por favor, elija otro código", Status.WARNING);
        }
        return check;
    }
    
    private int insertCollaborativeProject(CollaborativeProject collaborativeProject) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        ResultSet resultSet = null;
        String statement = "INSERT INTO ProyectoColaborativo"
        + " (idCursoSolicitante, idCursoSolicitado, nombre, descripcion,"
        + " objetivoGeneral, modalidad, codigo, rutaSyllabus)"
        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";        
        int result = -1;
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);
            
            preparedStatement.setInt(1,collaborativeProject.getRequesterCourse().getIdCourse());
            preparedStatement.setInt(2,collaborativeProject.getRequestedCourse().getIdCourse());
            preparedStatement.setString(3,collaborativeProject.getName());
            preparedStatement.setString(4,collaborativeProject.getDescription());
            preparedStatement.setString(5,collaborativeProject.getGeneralObjective());
            preparedStatement.setString(6,collaborativeProject.getModality());
            preparedStatement.setString(7,collaborativeProject.getCode());                       
            preparedStatement.setString(8,collaborativeProject.getSyllabusPath());
            
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Logger.getLogger(CollaborativeProjectDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible registrar el proyecto colaborativo", Status.WARNING);
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
                Logger.getLogger(CollaborativeProjectDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return result;                
    }
    
    private CollaborativeProject initializeCollaborativeProject(ResultSet resultSet) throws DAOException {
        CollaborativeProject collaborativeProject = new CollaborativeProject();
        CourseDAO courseDAO = new CourseDAO();
        
        try {
            collaborativeProject.setIdCollaborativeProject(resultSet.getInt("idProyectoColaborativo"));
            collaborativeProject.setRequesterCourse(courseDAO.getCourseByIdCourse(resultSet.getInt("idCursoSolicitante")));
            collaborativeProject.setRequestedCourse(courseDAO.getCourseByIdCourse(resultSet.getInt("idCursoSolicitado")));
            collaborativeProject.setName(resultSet.getString("nombre"));
            collaborativeProject.setDescription(resultSet.getString("descripcion"));
            collaborativeProject.setGeneralObjective(resultSet.getString("objetivoGeneral"));
            collaborativeProject.setModality(resultSet.getString("modalidad"));
            collaborativeProject.setCode(resultSet.getString("codigo"));
            collaborativeProject.setStatus(resultSet.getString("estado"));
            collaborativeProject.setSyllabusPath(resultSet.getString("rutaSyllabus"));
        } catch (SQLException exception) {
            Logger.getLogger(CollaborativeProjectDAO.class.getName()).log(Level.SEVERE, null, exception);            
        }
        return collaborativeProject;
    }
    
    @Override
    public ArrayList<CollaborativeProject> getCollaborativeProjectsByStatus(String status) throws DAOException {
        ArrayList<CollaborativeProject> collaborativeProjects = new ArrayList<>();         
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * from ProyectoColaborativo WHERE"
        + " estado = ?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setString(1,status);
            
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                collaborativeProjects.add(initializeCollaborativeProject(resultSet));
            }
        } catch (SQLException exception) {
            Logger.getLogger(CollaborativeProjectDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible recuperar los proyectos colaborativos", Status.WARNING);
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
            } catch (SQLException exception){
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return collaborativeProjects;
    }
    
    @Override
    public ArrayList<CollaborativeProject> getCollaborativeProjectsByidProfessorAndStatus(int idProfessor, String status) throws DAOException {
        ArrayList<CollaborativeProject> collaborativeProjects = new ArrayList<>();         
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT ProyectoColaborativo.*"
        + " FROM ProyectoColaborativo"
        + " LEFT JOIN Curso AS CursoSolicitante ON"
        + " ProyectoColaborativo.idCursoSolicitante = CursoSolicitante.idCurso"
        + " LEFT JOIN Profesor AS ProfesorSolicitante ON"
        + " CursoSolicitante.idProfesor = ProfesorSolicitante.idProfesor"
        + " LEFT JOIN Curso AS CursoSolicitado ON"
        + " ProyectoColaborativo.idCursoSolicitado = CursoSolicitado.idCurso"
        + " LEFT JOIN Profesor AS ProfesorSolicitado ON"
        + " CursoSolicitado.idProfesor = ProfesorSolicitado.idProfesor"
        + " WHERE (ProfesorSolicitante.idProfesor = ? OR ProfesorSolicitado.idProfesor = ?)"
        + " AND ProyectoColaborativo.estado = ?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setInt(1,idProfessor);
            preparedStatement.setInt(2,idProfessor);
            preparedStatement.setString(3, status);
            
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                collaborativeProjects.add(initializeCollaborativeProject(resultSet));
            }
        } catch (SQLException exception) {
            Logger.getLogger(CollaborativeProjectDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible recuperar los proyectos colaborativos", Status.WARNING);
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
            } catch (SQLException exception){
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return collaborativeProjects;
    }
    
    @Override
    public CollaborativeProject getCollaborativeProjectByCode(String code) throws DAOException {
        CollaborativeProject collaborativeProject = new CollaborativeProject();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM ProyectoColaborativo WHERE"
        + " codigo = ?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setString(1,code);
            
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                collaborativeProject = initializeCollaborativeProject(resultSet);
            }
        } catch (SQLException exception) {
            Logger.getLogger(CollaborativeProjectDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible recuperar el proyecto colaborativo", Status.WARNING);
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
            } catch (SQLException exception){
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return collaborativeProject;
    }
    
    private int updateCollaborativeProjectPrivate(CollaborativeProject collaborativeProject) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE ProyectoColaborativo SET nombre = ?,"
        + " descripcion = ?, objetivoGeneral = ?, modalidad = ?, codigo = ?,"
        + " rutaSyllabus = ?";
        int rowsAffected = -1;
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setString(1,collaborativeProject.getName());
            preparedStatement.setString(2,collaborativeProject.getDescription());
            preparedStatement.setString(3,collaborativeProject.getGeneralObjective());
            preparedStatement.setString(4,collaborativeProject.getModality());
            preparedStatement.setString(5,collaborativeProject.getCode());
            preparedStatement.setString(6,collaborativeProject.getSyllabusPath());
            
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(CollaborativeProjectDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible actualizar el proyecto colaborativo", Status.WARNING);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(CollaborativeProjectDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return rowsAffected;
    }
    
    @Override
    public int updateCollaborativeProject(CollaborativeProject collaborativeProject) throws DAOException {
        int result = -1;
        
        if (!collaborativeProject.getStatus().equals("Aceptado")) {
            if (!checkDuplicateCollaborativeProjectCode(collaborativeProject)) {
                result = updateCollaborativeProjectPrivate(collaborativeProject);
            }
        } else {
            throw new DAOException("No puedes actualizar un proyecto colaborativo que ya fue aceptado", Status.WARNING);
        }    
        return result;
    }
    
    public int deleteCollaborativeProjectByidCollaborativeProject(int idCollaborativeProject) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String Statement = "DELETE FROM ProyectoColaborativo WHERE "
        + "idProyectoColaborativo = ?";
        int rowsAffected = -1;
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(Statement);
            
            preparedStatement.setInt(1, idCollaborativeProject);
            
            rowsAffected = preparedStatement.executeUpdate();
        } catch(SQLException exception) {
            Logger.getLogger(CollaborativeProjectDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue eliminar el proyecto colaborativo", Status.WARNING);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(CollaborativeProjectDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return rowsAffected;
    }
}
