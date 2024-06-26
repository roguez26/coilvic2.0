package mx.fei.coilvicapp.logic.collaborativeprojectrequest;

import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;
import mx.fei.coilvicapp.logic.course.*;

public class CollaborativeProjectRequestDAO implements ICollaborativeProjectRequest {

    public CollaborativeProjectRequestDAO() {
    }

    /**
     * Registra una solicitud de proyecto colaborativo.
     *
     * @param collaborativeProjectRequest La solicitud de proyecto colaborativo a registrar.
     * @return Un entero que indica el resultado de la operación (-1 si falla).
     * @throws DAOException si hay un error al acceder a los datos o si no se puede enviar la solicitud por
     * diversas razones.
     */
    @Override
    public int registerCollaborativeProjectRequest
    (CollaborativeProjectRequest collaborativeProjectRequest) throws DAOException {
        int result = -1;
        CourseDAO courseDAO = new CourseDAO();
        
        if (checkCollaborativeProjectRequestsSent(collaborativeProjectRequest) == 0) {
            switch (courseDAO.checkCourseStatus(collaborativeProjectRequest.getRequesterCourse())) {
                case "Aceptado" -> {
                switch (courseDAO.checkCourseStatus(collaborativeProjectRequest.getRequestedCourse())) {
                    case "Aceptado" -> result = insertCollaborativeProjectRequest(collaborativeProjectRequest);
                    case "Pendiente" -> throw new DAOException
                    ("No se pudo enviar la solicitud, el curso que solicitó aún no ha sido aceptado", Status.WARNING);
                    case "Rechazado" -> throw new DAOException
                    ("No se pudo enviar la solicitud, el curso que solicitó fue rechazado", Status.WARNING);
                    case "Colaboracion" -> throw new DAOException("No se pudo enviar la solicitud,"
                    + " el curso que solicitó ya forma parte de un proyecto colaborativo", Status.WARNING);
                    default -> {}
                }
                }
                case "Pendiente" -> throw new DAOException("No se pudo enviar la solicitud,"
                + " su curso aún no ha sido aceptado", Status.WARNING);
                case "Rechazado" -> throw new DAOException("No se pudo enviar la solicitud,"
                + " su curso fue rechazado", Status.WARNING);
                case "Colaboracion" -> throw new DAOException("No se pudo enviar la solicitud,"
                + " el curso ya forma parte de un proyecto colaborativo", Status.WARNING);
                default -> {}
            }
        } else {
            throw new DAOException("No puedes enviar más de una solicitud de"
            + " proyecto colaborativo con el mismo curso", Status.WARNING);
        }
        return result;
    }

    private int checkCollaborativeProjectRequestsSent(CollaborativeProjectRequest collaborativeProjectRequest) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT COUNT(*) FROM SolicitudProyectoColaborativo"
                + " WHERE idCursoSolicitante = ? AND (estado = 'Pendiente' OR"
                + " estado = 'Aceptado')";
        int result = -1;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, collaborativeProjectRequest.getRequesterCourse().getIdCourse());

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No se pudo realizar la validación, intente más tarde", Status.ERROR);
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
                Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
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

            preparedStatement.setInt(1, collaborativeProjectRequest.getRequesterCourse().getIdCourse());
            preparedStatement.setInt(2, collaborativeProjectRequest.getRequestedCourse().getIdCourse());

            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar la solicitud de proyecto colaborativo", Status.ERROR);
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
                Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            }
        }
        return result;
    }

    private CollaborativeProjectRequest initializeCollaborativeProjectRequest(ResultSet resultSet)
            throws DAOException {
        CollaborativeProjectRequest collaborativeProjectRequest = new CollaborativeProjectRequest();
        CourseDAO courseDAO = new CourseDAO();

        try {
            collaborativeProjectRequest.setIdCollaborativeProjectRequest(resultSet.getInt("idSolicitudProyectoColaborativo"));
            collaborativeProjectRequest.setRequesterCourse(courseDAO.getCourseByIdCourse(resultSet.getInt("idCursoSolicitante")));
            collaborativeProjectRequest.setRequestedCourse(courseDAO.getCourseByIdCourse(resultSet.getInt("idCursoSolicitado")));
            collaborativeProjectRequest.setStatus(resultSet.getString("estado"));
            collaborativeProjectRequest.setRequestDate(resultSet.getString("fechaSolicitud"));
            collaborativeProjectRequest.setValidationDate(resultSet.getString("fechaValidacion"));
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
        }
        return collaborativeProjectRequest;
    }

    /**
     * Obtiene las solicitudes de proyectos colaborativos enviadas por un profesor.
     *
     * @param idProfessor El ID del profesor cuyas solicitudes de proyectos colaborativos se quieren obtener.
     * @return Una lista de solicitudes de proyectos colaborativos enviadas por el profesor.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public ArrayList<CollaborativeProjectRequest> getCollaborativeProjectRequests(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests;
        collaborativeProjectRequests = getCollaborativeProjectRequestsByIdProfessor(idProfessor);
        return collaborativeProjectRequests;
    }

    /**
     * Obtiene las solicitudes de proyectos colaborativos recibidas por un profesor.
     *
     * @param idProfessor El ID del profesor que recibió las solicitudes de proyectos colaborativos.
     * @return Una lista de solicitudes de proyectos colaborativos recibidas por el profesor.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public ArrayList<CollaborativeProjectRequest> getReceivedCollaborativeProjectRequests(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests;
        collaborativeProjectRequests = getReceivedCollaborativeProjectRequestByIdProfessor(idProfessor);
        return collaborativeProjectRequests;
    }

    private ArrayList<CollaborativeProjectRequest> getReceivedCollaborativeProjectRequestByIdProfessor(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT SolicitudProyectoColaborativo.*"
                + " FROM SolicitudProyectoColaborativo"
                + " JOIN Curso ON SolicitudProyectoColaborativo.idCursoSolicitado = Curso.idCurso"
                + " JOIN Profesor ON Curso.idProfesor = Profesor.idProfesor"
                + " WHERE Profesor.idProfesor = ?"
                + " AND SolicitudProyectoColaborativo.estado != 'Finalizado';";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, idProfessor);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                collaborativeProjectRequests.add(initializeCollaborativeProjectRequest(resultSet));
            }
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar las solicitudes de proyecto colaborativo", Status.ERROR);
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
                Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            }
        }
        return collaborativeProjectRequests;
    }

    /**
     * Obtiene las solicitudes de proyectos colaborativos enviadas por un profesor.
     *
     * @param idProfessor El ID del profesor que envió las solicitudes de proyectos colaborativos.
     * @return Una lista de solicitudes de proyectos colaborativos enviadas por el profesor.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public ArrayList<CollaborativeProjectRequest> getSentCollaborativeProjectRequests(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests;
        collaborativeProjectRequests = getSentCollaborativeProjectRequestByIdProfessor(idProfessor);
        return collaborativeProjectRequests;
    }

    private ArrayList<CollaborativeProjectRequest> getSentCollaborativeProjectRequestByIdProfessor(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT solicitudProyectoColaborativo.*"
                + " FROM SolicitudProyectoColaborativo"
                + " JOIN curso ON SolicitudProyectoColaborativo.idCursoSolicitante = curso.idCurso"
                + " JOIN profesor ON curso.idProfesor = profesor.idProfesor"
                + " WHERE profesor.idProfesor = ?"
                + " AND SolicitudProyectoColaborativo.estado != 'Finalizado';";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, idProfessor);

            resultSet = preparedStatement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                collaborativeProjectRequests.add(initializeCollaborativeProjectRequest(resultSet));
            }
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar las solicitudes de proyecto colaborativo", Status.ERROR);
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
                Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            }
        }
        return collaborativeProjectRequests;
    }

    public ArrayList<CollaborativeProjectRequest> getCollaborativeProjectRequestsByIdProfessor(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT solicitudProyectoColaborativo.*"
                + " FROM SolicitudProyectoColaborativo"
                + " JOIN Curso cursoSolicitante ON"
                + " SolicitudProyectoColaborativo.idCursoSolicitante = cursoSolicitante.idCurso"
                + " JOIN Curso cursoSolicitado ON"
                + " SolicitudProyectoColaborativo.idCursoSolicitado = cursoSolicitado.idCurso"
                + " JOIN Profesor profesorSolicitante ON"
                + " cursoSolicitante.idProfesor = profesorSolicitante.idProfesor"
                + " JOIN Profesor profesorSolicitado ON"
                + " cursoSolicitado.idProfesor = profesorSolicitado.idProfesor"
                + " WHERE (profesorSolicitante.idProfesor = ? OR profesorSolicitado.idProfesor = ?)"
                + " AND SolicitudProyectoColaborativo.estado != 'Finalizado';";

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
            Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar las solicitudes aceptadas de proyecto colaborativo", Status.ERROR);
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
                Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            }
        }
        return collaborativeProjectRequests;
    }

    /**
     * Obtiene las solicitudes de proyectos colaborativos pendientes recibidas por un profesor.
     *
     * @param idProfessor El ID del profesor que recibió las solicitudes de proyectos colaborativos pendientes.
     * @return Una lista de solicitudes de proyectos colaborativos pendientes recibidas por el profesor.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public ArrayList<CollaborativeProjectRequest> getPendingReceivedCollaborativeProjectRequests(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests;
        collaborativeProjectRequests
                = getReceivedCollaborativeProjectRequestByIdProfessorAndStatus(idProfessor, "Pendiente");
        return collaborativeProjectRequests;
    }

    /**
     * Obtiene las solicitudes de proyectos colaborativos aceptadas recibidas por un profesor.
     *
     * @param idProfessor El ID del profesor que recibió las solicitudes de proyectos colaborativos aceptadas.
     * @return Una lista de solicitudes de proyectos colaborativos aceptadas recibidas por el profesor.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public ArrayList<CollaborativeProjectRequest> getAcceptedReceivedCollaborativeProjectRequests(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests;
        collaborativeProjectRequests
                = getReceivedCollaborativeProjectRequestByIdProfessorAndStatus(idProfessor, "Aceptado");
        return collaborativeProjectRequests;
    }

    /**
     * Obtiene las solicitudes de proyectos colaborativos rechazadas recibidas por un profesor.
     *
     * @param idProfessor El ID del profesor que recibió las solicitudes de proyectos colaborativos rechazadas.
     * @return Una lista de solicitudes de proyectos colaborativos rechazadas recibidas por el profesor.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public ArrayList<CollaborativeProjectRequest> getRejectedReceivedCollaborativeProjectRequests(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests;
        collaborativeProjectRequests
                = getReceivedCollaborativeProjectRequestByIdProfessorAndStatus(idProfessor, "Rechazado");
        return collaborativeProjectRequests;
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
            Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar las solicitudes de proyecto colaborativo", Status.ERROR);
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
                Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            }
        }
        return collaborativeProjectRequests;
    }

    /**
     * Obtiene las solicitudes de proyectos colaborativos pendientes enviadas por un profesor.
     *
     * @param idProfessor El ID del profesor que envió las solicitudes de proyectos colaborativos pendientes.
     * @return Una lista de solicitudes de proyectos colaborativos pendientes enviadas por el profesor.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public ArrayList<CollaborativeProjectRequest> getPendingSentCollaborativeProjectRequests(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests;
        collaborativeProjectRequests
                = getSentCollaborativeProjectRequestByIdProfessorAndStatus(idProfessor, "Pendiente");
        return collaborativeProjectRequests;
    }

    /**
     * Obtiene las solicitudes de proyectos colaborativos aceptadas enviadas por un profesor.
     *
     * @param idProfessor El ID del profesor que envió las solicitudes de proyectos colaborativos aceptadas.
     * @return Una lista de solicitudes de proyectos colaborativos aceptadas enviadas por el profesor.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public ArrayList<CollaborativeProjectRequest> getAcceptedSentCollaborativeProjectRequests(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests;
        collaborativeProjectRequests
                = getSentCollaborativeProjectRequestByIdProfessorAndStatus(idProfessor, "Aceptado");
        return collaborativeProjectRequests;
    }

    /**
     * Obtiene las solicitudes de proyectos colaborativos rechazadas enviadas por un profesor.
     *
     * @param idProfessor El ID del profesor que envió las solicitudes de proyectos colaborativos rechazadas.
     * @return Una lista de solicitudes de proyectos colaborativos rechazadas enviadas por el profesor.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public ArrayList<CollaborativeProjectRequest> getRejectedSentCollaborativeProjectRequests(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests;
        collaborativeProjectRequests
                = getSentCollaborativeProjectRequestByIdProfessorAndStatus(idProfessor, "Rechazado");
        return collaborativeProjectRequests;
    }

    /**
     * Obtiene las solicitudes de proyectos colaborativos canceladas enviadas por un profesor.
     *
     * @param idProfessor El ID del profesor que envió las solicitudes de proyectos colaborativos canceladas.
     * @return Una lista de solicitudes de proyectos colaborativos canceladas enviadas por el profesor.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public ArrayList<CollaborativeProjectRequest> getCancelledSentCollaborativeProjectRequests(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests;
        collaborativeProjectRequests
                = getSentCollaborativeProjectRequestByIdProfessorAndStatus(idProfessor, "Cancelado");
        return collaborativeProjectRequests;
    }

    private ArrayList<CollaborativeProjectRequest> getSentCollaborativeProjectRequestByIdProfessorAndStatus(int idProfessor, String status) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT SolicitudProyectoColaborativo.*"
                + " FROM SolicitudProyectoColaborativo"
                + " JOIN curso ON SolicitudProyectoColaborativo.idCursoSolicitante = curso.idCurso"
                + " JOIN profesor ON curso.idProfesor = profesor.idProfesor"
                + " WHERE profesor.idProfesor = ? and SolicitudProyectoColaborativo.estado = ?;";

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
            Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar las solicitudes de proyecto colaborativo", Status.ERROR);
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
                Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            }
        }
        return collaborativeProjectRequests;
    }

    /**
     * Obtiene las solicitudes de proyectos colaborativos aceptadas por un profesor.
     *
     * @param idProfessor El ID del profesor cuyas solicitudes se desean obtener.
     * @return Una lista de solicitudes de proyectos colaborativos aceptadas.
     * @throws DAOException Si ocurre un error al recuperar las solicitudes.
     */
    @Override
    public ArrayList<CollaborativeProjectRequest> getAcceptedCollaborativeProjectRequests(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests;
        collaborativeProjectRequests = getAceptedCollaborativeProjectRequestsByIdProfessor(idProfessor);
        return collaborativeProjectRequests;
    }

    /**
     * Obtiene las solicitudes de proyectos colaborativos aceptadas por un profesor según su ID.
     *
     * @param idProfessor El ID del profesor cuyas solicitudes se desean obtener.
     * @return Una lista de solicitudes de proyectos colaborativos aceptadas.
     * @throws DAOException Si ocurre un error al recuperar las solicitudes.
     */
    @Override
    public ArrayList<CollaborativeProjectRequest> getAceptedCollaborativeProjectRequestsByIdProfessor(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT SolicitudProyectoColaborativo.*"
                + " FROM SolicitudProyectoColaborativo"
                + " JOIN curso cursoSolicitante ON"
                + " SolicitudProyectoColaborativo.idCursoSolicitante = cursoSolicitante.idCurso"
                + " JOIN curso cursoSolicitado ON"
                + " SolicitudProyectoColaborativo.idCursoSolicitado = cursoSolicitado.idCurso"
                + " JOIN profesor profesorSolicitante ON"
                + " cursoSolicitante.idProfesor = profesorSolicitante.idProfesor"
                + " JOIN profesor profesorSolicitado ON"
                + " cursoSolicitado.idProfesor = profesorSolicitado.idProfesor"
                + " WHERE SolicitudProyectoColaborativo.estado = 'Aceptado'"
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
            Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar las solicitudes aceptadas de proyecto colaborativo", Status.ERROR);
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
                Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            }
        }
        return collaborativeProjectRequests;
    }

    /**
     * Atiende una solicitud de proyecto colaborativo cambiando su estado.
     *
     * @param collaborativeProjectRequest La solicitud de proyecto colaborativo a atender.
     * @param status El estado al que se cambiará la solicitud.
     * @return El resultado de la operación de atención de la solicitud.
     * @throws DAOException Si ocurre un error al atender la solicitud.
     */
    @Override
    public int attendCollaborativeProjectRequest(CollaborativeProjectRequest collaborativeProjectRequest, String status) throws DAOException {
        int result = -1;
        String currentStatus;
        currentStatus = checkCollaborativeProjectRequestStatus(collaborativeProjectRequest);

        switch (currentStatus) {
            case "Pendiente" -> {
                result = updateCollaborativeProjectRequestStatusById(collaborativeProjectRequest.getIdCollaborativeProjectRequest(), status);
                if (status.equals("Aceptado")) {
                    if (result == 1) {
                        CourseDAO courseDAO = new CourseDAO();
                        courseDAO.changeCourseStatusToCollaboration(collaborativeProjectRequest.getRequesterCourse());
                        courseDAO.changeCourseStatusToCollaboration(collaborativeProjectRequest.getRequestedCourse());
                        RejectCollaborativeProjectRequests(collaborativeProjectRequest);
                    }
                }
            }
            case "Cancelado" ->
                throw new DAOException("No se pudo evaluar la solicitud,"
                        + " la solicitud fue cancelada por el remitente", Status.WARNING);
            default ->
                throw new DAOException("No puede responder a una solicitud a la que ya respondio", Status.WARNING);
        }

        return result;
    }

    /**
     * Cancela una solicitud de proyecto colaborativo cambiando su estado.
     *
     * @param collaborativeProjectRequest La solicitud de proyecto colaborativo a cancelar.
     * @return El resultado de la operación de cancelación de la solicitud.
     * @throws DAOException Si ocurre un error al cancelar la solicitud.
     */
    @Override
    public int cancelCollaborativeProjectRequest(CollaborativeProjectRequest collaborativeProjectRequest) throws DAOException {
        int result = -1;

        switch (checkCollaborativeProjectRequestStatus(collaborativeProjectRequest)) {
            case "Pendiente" ->
                result = cancelCollaborativeProjectRequestByIdCollaborativeProjectRequest(collaborativeProjectRequest.getIdCollaborativeProjectRequest());
            case "Cancelado" ->
                throw new DAOException("No puede cancelar una solicitud de proyecto colaborativo que ya cancelo", Status.WARNING);
            default ->
                throw new DAOException("No puede cancelar una solicitud de proyecto colaborativo que ya fue atendida", Status.WARNING);
        }
        return result;
    }

    private int cancelCollaborativeProjectRequestByIdCollaborativeProjectRequest(int idCollaborativeProjectRequest) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE SolicitudProyectoColaborativo SET estado = 'Cancelado'"
                + " WHERE idSolicitudProyectoColaborativo = ?;";
        int rowsAffected = -1;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, idCollaborativeProjectRequest);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible cancelar la solicitud", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            }
        }
        return rowsAffected;
    }

    /**
     * Finaliza una solicitud de proyecto colaborativo cambiando su estado.
     *
     * @param collaborativeProjectRequest La solicitud de proyecto colaborativo a finalizar.
     * @return El resultado de la operación de finalización de la solicitud.
     * @throws DAOException Si ocurre un error al finalizar la solicitud.
     */
    @Override
    public int finalizeCollaborativeProjectRequest(CollaborativeProjectRequest collaborativeProjectRequest) throws DAOException {
        int result = -1;

        if (checkCollaborativeProjectRequestStatus(collaborativeProjectRequest).equals("Aceptado")) {
            result = updateCollaborativeProjectRequestStatusById(collaborativeProjectRequest.getIdCollaborativeProjectRequest(), "Finalizado");
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
                + " fechaValidacion = NOW() WHERE idSolicitudProyectoColaborativo = ?;";
        int rowsAffected = -1;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, idCollaborativeProjectRequest);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible evaluar la solicitud", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            }
        }
        return rowsAffected;
    }

    public String checkCollaborativeProjectRequestStatus(CollaborativeProjectRequest collaborativeProjectRequest) throws DAOException {
        String status = "";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT estado FROM SolicitudProyectoColaborativo"
                + " WHERE idSolicitudProyectoColaborativo = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, collaborativeProjectRequest.getIdCollaborativeProjectRequest());

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                status = resultSet.getString("estado");
            }
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener el estado", Status.ERROR);
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
                Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            }
        }
        return status;
    }

    private int RejectCollaborativeProjectRequests(CollaborativeProjectRequest collaborativeProjectRequest) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE SolicitudProyectoColaborativo"
                + " SET estado = 'Rechazado', fechaValidacion = NOW()"
                + " WHERE idSolicitudProyectoColaborativo != ?"
                + " AND idCursoSolicitado = ?";
        int rowsAffected = -1;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, collaborativeProjectRequest.getIdCollaborativeProjectRequest());
            preparedStatement.setInt(2, collaborativeProjectRequest.getRequestedCourse().getIdCourse());

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
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
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar la solicitud de proyecto colaborativo", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            }
        }
        return rowsAffected;
    }

    /**
     * Obtiene una solicitud de proyecto colaborativo utilizando los ID de los cursos solicitado y solicitante.
     *
     * @param idRequestedCourse El ID del curso solicitado.
     * @param idRequesterCourse El ID del curso solicitante.
     * @return La solicitud de proyecto colaborativo encontrada.
     * @throws DAOException Si ocurre un error al recuperar la solicitud.
     */
    @Override
    public CollaborativeProjectRequest getCollaborativeProjectByCoursesId(int idRequestedCourse, int idRequesterCourse) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String Statement = "SELECT * FROM SolicitudProyectoColaborativo WHERE idCursoSolicitado = ? AND idCursoSolicitante = ?";
        CollaborativeProjectRequest collaborativeProjectRequest = new CollaborativeProjectRequest();
        ResultSet resultSet = null;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(Statement);
            preparedStatement.setInt(1, idRequestedCourse);
            preparedStatement.setInt(2, idRequesterCourse);

            resultSet = preparedStatement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                collaborativeProjectRequest = initializeCollaborativeProjectRequest(resultSet);
            }
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar la solicitud", Status.ERROR);
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
                Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            }
        }
        return collaborativeProjectRequest;
    }

    /**
     * Verifica si hay solicitudes de proyecto colaborativo disponibles para un profesor.
     *
     * @param idProfessor El ID del profesor para el que se verifica la disponibilidad de solicitudes.
     * @return true si hay solicitudes disponibles, false de lo contrario.
     * @throws DAOException Si ocurre un error al verificar la disponibilidad de solicitudes.
     */
    
    @Override
    public boolean areThereAvailableRequests(int idProfessor) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "SELECT EXISTS ( "
                + "    SELECT 1 "
                + "    FROM solicitudproyectocolaborativo s "
                + "    JOIN curso c1 ON s.idCursoSolicitante = c1.idCurso "
                + "    JOIN curso c2 ON s.idCursoSolicitado = c2.idCurso "
                + "    LEFT JOIN proyectocolaborativo p ON (s.idCursoSolicitante = p.idCursoSolicitante OR "
                + "s.idCursoSolicitado = p.idCursoSolicitado) "
                + "    WHERE (c1.idProfesor = ? OR c2.idProfesor = ?) "
                + "      AND s.estado = 'Aceptado' "
                + "      AND p.idProyectoColaborativo IS NULL "
                + ") AS solicitudDisponible;";
        ResultSet resultSet = null;
        boolean solicitudDisponible = false;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, idProfessor);
            preparedStatement.setInt(2, idProfessor);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                solicitudDisponible = resultSet.getBoolean(1);
            }
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible revisar si hay solicitudes disponibles", Status.ERROR);
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
                Log.getLogger(CollaborativeProjectRequestDAO.class).error(exception.getMessage(), exception);
            }
        }

        return solicitudDisponible;
    }
}
