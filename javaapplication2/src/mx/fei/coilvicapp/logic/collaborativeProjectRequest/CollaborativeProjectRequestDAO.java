package mx.fei.coilvicapp.logic.collaborativeprojectrequest;

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
public class CollaborativeProjectRequestDAO implements ICollaborativeProjectRequest {
    
    public CollaborativeProjectRequestDAO() {
    }    
      
    @Override
    public int registerCollaborativeProjectRequest(CollaborativeProjectRequest collaborativeProjectRequest) throws DAOException {
        int result = -1;
        if (collaborativeProjectRequest.getRequesterCourse().getProfessor().getIdProfessor() != 
           collaborativeProjectRequest.getRequestedCourse().getProfessor().getIdProfessor()) {
            if (checkCollaborativeProjectRequestsSent(collaborativeProjectRequest) == 0) {
                switch (collaborativeProjectRequest.getRequesterCourse().getStatus()) {
                    case "Aceptado" -> {
                    switch (collaborativeProjectRequest.getRequestedCourse().getStatus()) {
                        case "Aceptado" -> result = insertCollaborativeProjectRequest(collaborativeProjectRequest);
                        case "Pendiente" -> throw new DAOException("No se pudo enviar la solicitud, el curso que solicitó aun no ha sido aceptado", Status.WARNING);
                        case "Rechazado" -> throw new DAOException("No se pudo enviar la solicitud, el curso que solicitó fue rechazado", Status.WARNING);
                        case "Colaboracion" -> throw new DAOException("No se pudo enviar la solicitud, el curso que solicitó ya forma parte de un proyecto colaborativo", Status.WARNING);
                        default -> {}
                    }
                    }
                    case "Pendiente" -> throw new DAOException("No se pudo enviar la solicitud, su curso aun no ha sido aceptado", Status.WARNING);
                    case "Rechazado" -> throw new DAOException("No se pudo enviar la solicitud, su curso fue rechazado", Status.WARNING);
                    case "Colaboracion" -> throw new DAOException("No se pudo enviar la solicitud, el curso ya forma parte de un proyecto colaborativo", Status.WARNING);
                    default -> {}
                }
            } else {
                throw new DAOException("No puedes enviar mas de una solicitud de proyecto colaborativo con el mismo curso", Status.WARNING);
            }
        } else {
            throw new DAOException("No puedes enviar una solicitud de proyecto colaborativo a tu propio curso", Status.WARNING);
        }
        return result;
    }
    
    private int checkCollaborativeProjectRequestsSent(CollaborativeProjectRequest collaborativeProjectRequest) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "SELECT * FROM SolicitudProyectoColaborativo"
        + " WHERE idCursoSolicitante = ? and (estado = 'Pendiente' OR"
        + " estado = 'Aceptado')";
        int result = -1;
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setInt(1,collaborativeProjectRequest.getRequesterCourse().getIdCourse());
            
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(CollaborativeProjectRequestDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception){
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
                throw new DAOException("No se pudo realizar la validación, intente más tarde", Status.WARNING);
            }
        }
        return result;
    }
    
    private int insertCollaborativeProjectRequest(CollaborativeProjectRequest collaborativeProjectRequest) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "INSERT INTO SolicitudProyectoColaborativo"
        + " (idCursoSolicitante, idCursoSolicitado, fechaSolicitud)"
        + " VALUES (?, ?, NOW())";
        DatabaseManager databaseManager = new DatabaseManager();
        ResultSet resultSet = null;
        int result = -1;
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);
            
            preparedStatement.setInt(1,collaborativeProjectRequest.getRequesterCourse().getIdCourse());
            preparedStatement.setInt(2,collaborativeProjectRequest.getRequestedCourse().getIdCourse());
            
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Logger.getLogger(CollaborativeProjectRequestDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible registrar la solicitud de proyecto colaborativo", Status.WARNING);
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
                Logger.getLogger(CollaborativeProjectRequestDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return result;
    }
     
    private CollaborativeProjectRequest initializeCollaborativeProjectRequest(ResultSet resultSet) throws DAOException {
        CollaborativeProjectRequest collaborativeProjectRequest = new CollaborativeProjectRequest();
        CourseDAO courseDAO = new CourseDAO();
        
        try {
            collaborativeProjectRequest.setIdCollaboratibeProjectRequest(resultSet.getInt("idSolicitudProyectoColaborativo"));
            collaborativeProjectRequest.setRequesterCourse(courseDAO.getCourseByIdCourse(resultSet.getInt("idCursoSolicitante")));         
            collaborativeProjectRequest.setRequestedCourse(courseDAO.getCourseByIdCourse(resultSet.getInt("idCursoSolicitado")));
            collaborativeProjectRequest.setStatus(resultSet.getString("estado"));
            collaborativeProjectRequest.setRequestDate(resultSet.getString("fechaSolicitud"));
            collaborativeProjectRequest.setValidationDate(resultSet.getString("fechaRespuesta"));
        } catch (SQLException exception) {
            Logger.getLogger(CollaborativeProjectRequestDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        return collaborativeProjectRequest;
    }
    
    @Override
    public ArrayList<CollaborativeProjectRequest> getPendingReceivedCollaborativeProjectRequests(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests;
        collaborativeProjectRequests = getReceivedCollaborativeProjectRequestByIdProfessorAndStatus(idProfessor, "Pendiente");
        
        if (!collaborativeProjectRequests.isEmpty()) {
            return collaborativeProjectRequests;
        } else {
            throw new DAOException("No hay solicitudes recibidas de proyecto colaborativo pendientes", Status.WARNING);
        }       
    }
    
    @Override
    public ArrayList<CollaborativeProjectRequest> getAceptedReceivedCollaborativeProjectRequests(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests;
        collaborativeProjectRequests = getReceivedCollaborativeProjectRequestByIdProfessorAndStatus(idProfessor, "Aceptado");
        
        if (!collaborativeProjectRequests.isEmpty()) {
            return collaborativeProjectRequests;
        } else {
            throw new DAOException("No hay solicitudes recibidas de proyecto colaborativo aceptadas", Status.WARNING);
        } 
    }
    
    @Override
    public ArrayList<CollaborativeProjectRequest> getRejectedReceivedCollaborativeProjectRequests(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests;
        collaborativeProjectRequests = getReceivedCollaborativeProjectRequestByIdProfessorAndStatus(idProfessor, "Rechazado");
        
        if (!collaborativeProjectRequests.isEmpty()) {
            return collaborativeProjectRequests;
        } else {
            throw new DAOException("No hay solicitudes recibidas de proyecto colaborativo rechazadas", Status.WARNING);
        } 
    }
    
    private ArrayList<CollaborativeProjectRequest> getReceivedCollaborativeProjectRequestByIdProfessorAndStatus(int idProfessor, String status) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT SolicitudProyectoColaborativo.*"
        + " FROM SolicitudProyectoColaborativo"
        + " JOIN Curso ON SolicitudProyectoColaborativo.idCursoSolicitado = Curso.idCurso"
        + " JOIN Profesor ON Curso.idProfesor = Profesor.idProfesor"
        + " WHERE Profesor.idProfesor = ? and SolicitudProyectoColaborativo.estado = ?;";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setInt(1, idProfessor);
            preparedStatement.setString(2, status);
            
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {                
                collaborativeProjectRequests.add(initializeCollaborativeProjectRequest(resultSet));
            }
        } catch (SQLException exception) {
            Logger.getLogger(CollaborativeProjectRequestDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible recuperar las solicitudes de proyecto colaborativo", Status.WARNING);
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
        return collaborativeProjectRequests;
    }
    
    @Override
    public ArrayList<CollaborativeProjectRequest> getPendingSentCollaborativeProjectRequests(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests;
        collaborativeProjectRequests = getSentCollaborativeProjectRequestByIdProfessorAndStatus(idProfessor, "Pendiente");
        
        if (!collaborativeProjectRequests.isEmpty()) {
            return collaborativeProjectRequests;
        } else {
            throw new DAOException("No hay solicitudes enviadas de proyecto colaborativo pendientes", Status.WARNING);
        }
    }
    
    @Override
    public ArrayList<CollaborativeProjectRequest> getAceptedSentCollaborativeProjectRequests(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests;
        collaborativeProjectRequests = getSentCollaborativeProjectRequestByIdProfessorAndStatus(idProfessor, "Aceptado");
        
        if (!collaborativeProjectRequests.isEmpty()) {
            return collaborativeProjectRequests;
        } else {
            throw new DAOException("No hay solicitudes enviadas de proyecto colaborativo aceptadas", Status.WARNING);
        }
    }
    
    @Override
    public ArrayList<CollaborativeProjectRequest> getRejectedSentCollaborativeProjectRequests(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests;
        collaborativeProjectRequests = getSentCollaborativeProjectRequestByIdProfessorAndStatus(idProfessor, "Rechazado");
        
        if (!collaborativeProjectRequests.isEmpty()) {
            return collaborativeProjectRequests;
        } else {
            throw new DAOException("No hay solicitudes enviadas de proyecto colaborativo rechazadas", Status.WARNING);
        }
    }
    
    private ArrayList<CollaborativeProjectRequest> getSentCollaborativeProjectRequestByIdProfessorAndStatus(int idProfessor, String status) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests = new ArrayList<>();       
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT solicitudProyectoColaborativo.*"
        + " FROM solicitudProyectoColaborativo"
        + " JOIN curso ON solicitudProyectoColaborativo.idCursoSolicitante = curso.idCurso"
        + " JOIN profesor ON curso.idProfesor = profesor.idProfesor"
        + " WHERE profesor.idProfesor = ? and solicitudProyectoColaborativo.estado = ?;";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setInt(1, idProfessor);
            preparedStatement.setString(2, status);
            
            resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {                
                collaborativeProjectRequests.add(initializeCollaborativeProjectRequest(resultSet));
            }
        } catch (SQLException exception) {
            Logger.getLogger(CollaborativeProjectRequestDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible recuperar las solicitudes de proyecto colaborativo", Status.WARNING);
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
        return collaborativeProjectRequests;
    }
    
    @Override // this one
    public ArrayList<CollaborativeProjectRequest> getAceptedCollaborativeProjectRequests(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests = new ArrayList<>();       
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT solicitudProyectoColaborativo.*"
             + " FROM solicitudProyectoColaborativo"
             + " JOIN curso cursoSolicitante ON solicitudProyectoColaborativo.idCursoSolicitante = cursoSolicitante.idCurso"
             + " JOIN curso cursoSolicitado ON solicitudProyectoColaborativo.idCursoSolicitado = cursoSolicitado.idCurso"
             + " JOIN profesor profesorSolicitante ON cursoSolicitante.idProfesor = profesorSolicitante.idProfesor"
             + " JOIN profesor profesorSolicitado ON cursoSolicitado.idProfesor = profesorSolicitado.idProfesor"
             + " WHERE solicitudProyectoColaborativo.estado = 'Aceptado'"
             + " AND (profesorSolicitante.idProfesor = ? OR profesorSolicitado.idProfesor = ?)";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setInt(1, idProfessor);
            preparedStatement.setInt(2, idProfessor);
            
            resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {                
                collaborativeProjectRequests.add(initializeCollaborativeProjectRequest(resultSet));
            }
        } catch (SQLException exception) {
            Logger.getLogger(CollaborativeProjectRequestDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible recuperar las solicitudes aceptadas de proyecto colaborativo", Status.WARNING);
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
        return collaborativeProjectRequests;
    }
    
    @Override
    public int attendCollaborativeProjectRequest (CollaborativeProjectRequest collaborativeProjectRequest, String status) throws DAOException {
        int result;
        
        result = updateCollaborativeProjectRequestStatusById(collaborativeProjectRequest.getIdCollaboratibeProjectRequest(), status);
        if (status.equals("Aceptado")) {            
            if (result == 1) {
                RejectCollaborativeProjectRequests(collaborativeProjectRequest);
            }
        }
        return result;
    }
    
    @Override
    public int cancelCollaborativeProjectRequest(CollaborativeProjectRequest collaborativeProjectRequest) throws DAOException {
        int result = -1;
        
        switch (collaborativeProjectRequest.getStatus()) {
            case "Pendiente" -> result = updateCollaborativeProjectRequestStatusById(collaborativeProjectRequest.getIdCollaboratibeProjectRequest(), "Cancelado");
            case "Aceptado" -> throw new DAOException("No puede cancelar una solicitud de proyecto colaborativo que ya fue aceptada", Status.WARNING);
            case "Rechazado" -> throw new DAOException("No puede cancelar una solicitud de proyecto colaborativo que ya fue rechazada", Status.WARNING);
            default -> {}
        }
        return result;
    }
    
    @Override 
    public int finalizeCollaborativeProjectRequest(CollaborativeProjectRequest collaborativeProjectRequest) throws DAOException {
        int result = -1;
        
        if (collaborativeProjectRequest.getStatus().equals("Aceptado")) {
            updateCollaborativeProjectRequestStatusById(collaborativeProjectRequest.getIdCollaboratibeProjectRequest(), "Finalizado");
        } else {
            throw new DAOException("No puede finalizar una solicitud que no fue aceptada", Status.WARNING);
        }
        return result;
    }
    
    private int updateCollaborativeProjectRequestStatusById(int idCollaborativeProjectRequest, String status) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE SolicitudProyectoColaborativo SET estado = ?,"
        + "fechaRespuesta = NOW() WHERE idSolicitudProyectoColaborativo = ?;";
        int rowsAffected = -1;
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setString(1,status);
            preparedStatement.setInt(2,idCollaborativeProjectRequest);
            
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(CollaborativeProjectRequestDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible evaluar la solicitud", Status.WARNING);
        } finally {
            try {
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
        return rowsAffected;
    }
    
    private int RejectCollaborativeProjectRequests(CollaborativeProjectRequest collaborativeProjectRequest) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE SolicitudProyectoColaborativo"
        + " SET estado = 'Rechazado', fechaRespuesta = NOW()"
        + " WHERE idSolicitudProyectoColaborativo != ?"                
        + " AND idCursoSolicitado = ?";
        int rowsAffected = -1;
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
                        
            preparedStatement.setInt(1,collaborativeProjectRequest.getIdCollaboratibeProjectRequest());
            preparedStatement.setInt(2,collaborativeProjectRequest.getRequestedCourse().getIdCourse());
            
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(CollaborativeProjectRequestDAO.class.getName()).log(Level.SEVERE, null, exception);            
        } finally {
            try {
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
        return rowsAffected;
    }
     
    public int deleteCollaborativeProjectRequestByidCollaborativeProjectRequest(int idCollaborativeProjectRequest) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String Statement = "DELETE FROM SolicitudProyectoColaborativo WHERE "
        + "idSolicitudProyectoColaborativo = ?";
        int rowsAffected = -1;
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(Statement);
            
            preparedStatement.setInt(1, idCollaborativeProjectRequest);
            
            rowsAffected = preparedStatement.executeUpdate();
        } catch(SQLException exception) {
            Logger.getLogger(CollaborativeProjectRequestDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible eliminar la solicitud de proyecto colaborativo", Status.WARNING);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(CollaborativeProjectRequestDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return rowsAffected;
    }
}