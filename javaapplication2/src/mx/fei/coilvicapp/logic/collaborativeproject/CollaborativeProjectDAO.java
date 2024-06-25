package mx.fei.coilvicapp.logic.collaborativeproject;

import mx.fei.coilvicapp.logic.assignment.AssignmentDAO;
import mx.fei.coilvicapp.logic.assignment.Assignment;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.collaborativeprojectrequest.*;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;
import mx.fei.coilvicapp.logic.course.*;
import mx.fei.coilvicapp.logic.modality.ModalityDAO;

public class CollaborativeProjectDAO implements ICollaborativeProject {

    public CollaborativeProjectDAO() {
    }

    /**
     * Registra un nuevo proyecto colaborativo en la base de datos.
     *
     * @param collaborativeProject El objeto de proyecto colaborativo a registrar.
     * @param collaborativeProjectRequest La solicitud de proyecto colaborativo relacionada.
     * @return El ID del proyecto colaborativo registrado, o -1 si hubo un error.
     * @throws DAOException Si ocurre un error al registrar el proyecto colaborativo.
     */
    @Override
    public int registerCollaborativeProject(CollaborativeProject collaborativeProject,
    CollaborativeProjectRequest collaborativeProjectRequest) throws DAOException {
        int result = -1;
        CollaborativeProjectRequestDAO collaboratibeProjectRequestDAO
        = new CollaborativeProjectRequestDAO();

        if (collaboratibeProjectRequestDAO.checkCollaborativeProjectRequestStatus(collaborativeProjectRequest).equals("Aceptado")) {
            if (checkCoursesForCollaborativeProject(collaborativeProject) == 0) {
                if (!checkDuplicateCollaborativeProjectCode(collaborativeProject)) {
                    result = insertCollaborativeProject(collaborativeProject);
                }
            } else {
                throw new DAOException("Ya existe un proyecto con alguno de esos cursos", Status.WARNING);
            }
        } else {
            throw new DAOException("La solicitud de proyecto colaborativo debe "
            + "ser aceptada antes de poder crear el proyecto colaborativo", Status.WARNING);
        }

        return result;
    }

    private boolean checkDuplicateCollaborativeProjectCode(CollaborativeProject collaborativeProject)
            throws DAOException {
        boolean check = false;
        CollaborativeProject auxCollaborativeProject = new CollaborativeProject();

        try {
            auxCollaborativeProject = getCollaborativeProjectByCode(collaborativeProject.getCode());
        } catch (DAOException exception) {
            throw new DAOException("No fue posible hacer la validación del proyecto colaborativo", Status.WARNING);
        }
        if (auxCollaborativeProject.getIdCollaborativeProject()
        != collaborativeProject.getIdCollaborativeProject()
        && auxCollaborativeProject.getIdCollaborativeProject() != 0) {
            throw new DAOException("El código del proyecto colaborativo ya está en uso."
            + " Por favor, elija otro código", Status.WARNING);
        }
        return check;
    }

    private int insertCollaborativeProject(CollaborativeProject collaborativeProject)
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        ResultSet resultSet = null;
        String statement = "INSERT INTO ProyectoColaborativo"
        + " (idCursoSolicitante, idCursoSolicitado, idModalidad,"
        + " nombre, descripcion, objetivoGeneral, codigo, rutaSyllabus)"
        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int result = -1;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);

            preparedStatement.setInt(1, collaborativeProject.getRequesterCourse().getIdCourse());
            preparedStatement.setInt(2, collaborativeProject.getRequestedCourse().getIdCourse());
            preparedStatement.setInt(3, collaborativeProject.getModality().getIdModality());
            preparedStatement.setString(4, collaborativeProject.getName());
            preparedStatement.setString(5, collaborativeProject.getDescription());
            preparedStatement.setString(6, collaborativeProject.getGeneralObjective());
            preparedStatement.setString(7, collaborativeProject.getCode());
            preparedStatement.setString(8, collaborativeProject.getSyllabusPath());

            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar el proyecto colaborativo", Status.ERROR);
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
                Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            }
        }
        return result;
    }

    private int checkCoursesForCollaborativeProject(CollaborativeProject collaborativeProject)
    throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT COUNT(*) FROM ProyectoColaborativo"
        + " WHERE (idCursoSolicitante = ? OR idCursoSolicitado = ?)"
        + " OR (idCursoSolicitante = ? OR idCursoSolicitado = ?)";
        int result = -1;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1,collaborativeProject.getRequesterCourse().getIdCourse());
            preparedStatement.setInt(2,collaborativeProject.getRequesterCourse().getIdCourse());
            preparedStatement.setInt(3,collaborativeProject.getRequestedCourse().getIdCourse());
            preparedStatement.setInt(4,collaborativeProject.getRequestedCourse().getIdCourse());

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
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
                Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            }
        }
        return result;
    }

    private CollaborativeProject initializeCollaborativeProject(ResultSet resultSet)
            throws DAOException {
        CollaborativeProject collaborativeProject = new CollaborativeProject();
        CourseDAO courseDAO = new CourseDAO();
        ModalityDAO modalityDAO = new ModalityDAO();

        try {
            collaborativeProject.setIdCollaborativeProject(resultSet.getInt("idProyectoColaborativo"));
            collaborativeProject.setRequesterCourse(courseDAO.getCourseByIdCourse(resultSet.getInt("idCursoSolicitante")));
            collaborativeProject.setRequestedCourse(courseDAO.getCourseByIdCourse(resultSet.getInt("idCursoSolicitado")));
            collaborativeProject.setModality(modalityDAO.getModalityByIdModality(resultSet.getInt("idModalidad")));
            collaborativeProject.setName(resultSet.getString("nombre"));
            collaborativeProject.setStatus(resultSet.getString("estado"));
            collaborativeProject.setDescription(resultSet.getString("descripcion"));
            collaborativeProject.setGeneralObjective(resultSet.getString("objetivoGeneral"));
            collaborativeProject.setCode(resultSet.getString("codigo"));
            collaborativeProject.setSyllabusPath(resultSet.getString("rutaSyllabus"));

        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
        }
        return collaborativeProject;
    }

    /**
     * Obtiene todas las propuestas de proyectos colaborativos con estado "Pendiente".
     *
     * @return Una lista de proyectos colaborativos pendientes.
     * @throws DAOException Si ocurre un error al recuperar los proyectos.
     */
    @Override
    public ArrayList<CollaborativeProject> getCollaborativeProjectsProposals() throws DAOException {
        ArrayList<CollaborativeProject> collaborativeProjects;
        collaborativeProjects = getCollaborativeProjectsByStatus("Pendiente");
        return collaborativeProjects;
    }

    /**
     * Recupera todos los proyectos colaborativos con el estado "Aceptado".
     *
     * @return Una lista de todos los proyectos colaborativos aceptados.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public ArrayList<CollaborativeProject> getAllAcceptedCollaborativeProjects() throws DAOException {
        ArrayList<CollaborativeProject> collaborativeProjects;
        collaborativeProjects = getCollaborativeProjectsByStatus("Aceptado");
        return collaborativeProjects;
    }

    /**
     * Recupera todos los proyectos colaborativos con el estado "Rechazado".
     *
     * @return Una lista de todos los proyectos colaborativos rechazados.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public ArrayList<CollaborativeProject> getAllRejectedCollaborativeProjects() throws DAOException {
        ArrayList<CollaborativeProject> collaborativeProjects;
        collaborativeProjects = getCollaborativeProjectsByStatus("Rechazado");
        return collaborativeProjects;
    }

    /**
     * Recupera todos los proyectos colaborativos con el estado "Finalizado".
     *
     * @return Una lista de todos los proyectos colaborativos finalizados.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public ArrayList<CollaborativeProject> getAllFinishedCollaborativeProjects() throws DAOException {
        ArrayList<CollaborativeProject> collaborativeProjects;
        collaborativeProjects = getCollaborativeProjectsByStatus("Finalizado");
        return collaborativeProjects;
    }

    private ArrayList<CollaborativeProject> getCollaborativeProjectsByStatus(String status)
            throws DAOException {
        ArrayList<CollaborativeProject> collaborativeProjects = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * from ProyectoColaborativo WHERE estado = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, status);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                collaborativeProjects.add(initializeCollaborativeProject(resultSet));
            }
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar los proyectos colaborativos", Status.ERROR);
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
                Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            }
        }
        return collaborativeProjects;
    }

    /**
     * Evalúa la propuesta de un proyecto colaborativo.
     *
     * @param collaborativeProject El proyecto colaborativo a evaluar.
     * @param status El nuevo estado del proyecto colaborativo.
     * @return El resultado de la evaluación (1 si es exitoso, -1 si no lo es).
     * @throws DAOException si hay un error al acceder a los datos o si el proyecto ya ha sido evaluado.
     */
    @Override
    public int evaluateCollaborativeProjectProposal(CollaborativeProject collaborativeProject, String status) throws DAOException {
        int result = -1;

        if (checkCollaborativeProjectStatus(collaborativeProject).equals("Pendiente")) {
            result = updateCollaborativeProjectStatusByCollaborativeProject(collaborativeProject, status);
        } else {
            throw new DAOException("No puede evaluar un proyecto colaborativo que ya fue evaluado", Status.WARNING);
        }
        return result;
    }

    /**
     * Recupera todos los proyectos colaborativos pendientes de un profesor.
     *
     * @param idProfessor El ID del profesor.
     * @return Una lista de proyectos colaborativos pendientes del profesor.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public ArrayList<CollaborativeProject> getPendingCollaborativeProjectsByProfessor(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProject> collaborativeProjects;
        collaborativeProjects = getCollaborativeProjectsByidProfessorAndStatus(idProfessor, "Pendiente");
        return collaborativeProjects;
    }

    /**
     * Recupera todos los proyectos colaborativos aceptados de un profesor.
     *
     * @param idProfessor El ID del profesor.
     * @return Una lista de proyectos colaborativos aceptados del profesor.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public ArrayList<CollaborativeProject> getAcceptedCollaborativeProjectsByProfessor(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProject> collaborativeProjects;
        collaborativeProjects = getCollaborativeProjectsByidProfessorAndStatus(idProfessor, "Aceptado");
        return collaborativeProjects;
    }

    /**
     * Recupera todos los proyectos colaborativos rechazados de un profesor.
     *
     * @param idProfessor El ID del profesor.
     * @return Una lista de proyectos colaborativos rechazados del profesor.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public ArrayList<CollaborativeProject> getRejectedCollaborativeProjectsByProfessor(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProject> collaborativeProjects;
        collaborativeProjects = getCollaborativeProjectsByidProfessorAndStatus(idProfessor, "Rechazado");
        return collaborativeProjects;
    }

    /**
     * Recupera todos los proyectos colaborativos finalizados de un profesor.
     *
     * @param idProfessor El ID del profesor.
     * @return Una lista de proyectos colaborativos finalizados del profesor.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public ArrayList<CollaborativeProject> getFinishedCollaborativeProjectsByProfessor(int idProfessor) throws DAOException {
        ArrayList<CollaborativeProject> collaborativeProjects;
        collaborativeProjects = getCollaborativeProjectsByidProfessorAndStatus(idProfessor, "Finalizado");
        return collaborativeProjects;
    }

    private ArrayList<CollaborativeProject> getCollaborativeProjectsByidProfessorAndStatus(int idProfessor, String status) throws DAOException {
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

            preparedStatement.setInt(1, idProfessor);
            preparedStatement.setInt(2, idProfessor);
            preparedStatement.setString(3, status);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                collaborativeProjects.add(initializeCollaborativeProject(resultSet));
            }
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar los proyectos colaborativos", Status.ERROR);
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
                Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            }
        }
        return collaborativeProjects;
    }

    /**
     * Recupera un proyecto colaborativo por su código.
     *
     * @param code El código del proyecto colaborativo.
     * @return El proyecto colaborativo correspondiente al código proporcionado.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public CollaborativeProject getCollaborativeProjectByCode(String code) throws DAOException {
        CollaborativeProject collaborativeProject = new CollaborativeProject();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM ProyectoColaborativo"
                + " WHERE codigo = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, code);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                collaborativeProject = initializeCollaborativeProject(resultSet);
            }
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar el proyecto colaborativo", Status.ERROR);
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
                Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            }
        }
        return collaborativeProject;
    }

    private int updateCollaborativeProjectStatusByCollaborativeProject(CollaborativeProject collaborativeProject, String status) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE ProyectoColaborativo SET estado = ? where idProyectoColaborativo = ?";
        int rowsAffected = -1;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, collaborativeProject.getIdCollaborativeProject());

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible evaluar el proyecto colaborativo", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            }
        }
        return rowsAffected;
    }

    private int updateCollaborativeProjectPrivate(CollaborativeProject collaborativeProject)
            throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE ProyectoColaborativo"
        + " SET idModalidad = ?, nombre = ?, estado = 'Pendiente',"
        + " descripcion = ?, objetivoGeneral = ?,"
        + " codigo = ?, rutaSyllabus = ?";
        int rowsAffected = -1;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, collaborativeProject.getModality().getIdModality());
            preparedStatement.setString(2, collaborativeProject.getName());
            preparedStatement.setString(3, collaborativeProject.getDescription());
            preparedStatement.setString(4, collaborativeProject.getGeneralObjective());
            preparedStatement.setString(5, collaborativeProject.getCode());
            preparedStatement.setString(6, collaborativeProject.getSyllabusPath());

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible actualizar el proyecto colaborativo", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            }
        }
        return rowsAffected;
    }

    /**
     * Actualiza un proyecto colaborativo.
     *
     * @param collaborativeProject El proyecto colaborativo a actualizar.
     * @return Un entero indicando el resultado de la operación.
     * @throws DAOException si hay un error al acceder a los datos o si el proyecto ya fue aceptado o
     * finalizado.
     */
    @Override
    public int updateCollaborativeProject(CollaborativeProject collaborativeProject)
            throws DAOException {
        int result = -1;

        switch (checkCollaborativeProjectStatus(collaborativeProject)) {
            case "Pendiente", "Rechazado" -> {
                if (!checkDuplicateCollaborativeProjectCode(collaborativeProject)) {
                    result = updateCollaborativeProjectPrivate(collaborativeProject);
                }
            }
            case "Aceptado" ->
                throw new DAOException("No puedes actualizar un proyecto colaborativo que ya fue aceptado", Status.WARNING);
            case "Finalizado" ->
                throw new DAOException("No puedes actualizar un proyecto colaborativo finalizado", Status.WARNING);
            default -> {
            }
        }
        return result;
    }

    /**
     * Finaliza un proyecto colaborativo.
     *
     * @param collaborativeProject El proyecto colaborativo a finalizar.
     * @return Un entero indicando el resultado de la operación.
     * @throws DAOException si hay un error al acceder a los datos o si el proyecto no ha sido aceptado.
     */
    @Override
    public int finalizeCollaborativeProject(CollaborativeProject collaborativeProject)
            throws DAOException {
        int result = -1;

        if (checkCollaborativeProjectStatus(collaborativeProject).equals("Aceptado")) {
            result = updateCollaborativeProjectStatusByCollaborativeProject(collaborativeProject, "Finalizado");
        } else {
            throw new DAOException("No puede finalizar un proyecto colaborativo que no fue aceptado", Status.WARNING);
        }
        return result;
    }

    /**
     * Verifica si un proyecto colaborativo tiene al menos tres actividades.
     *
     * @param collaborativeProject El proyecto colaborativo a verificar.
     * @return true si el proyecto tiene al menos tres actividades.
     * @throws DAOException si el proyecto no tiene al menos tres actividades.
     */
    @Override
    public boolean hasThreeActivitiesAtLeast(CollaborativeProject collaborativeProject)
            throws DAOException {
        if (countAssigments(collaborativeProject) >= 3) {
            return true;
        } else {
            throw new DAOException("No puede finalizar un proyecto colaborativo con menos de tres actividades", Status.WARNING);
        }
    }

    private int countAssigments(CollaborativeProject collaborativeProject) throws DAOException {
        ArrayList<Assignment> assignments;
        AssignmentDAO assignmentDAO = new AssignmentDAO();
        int numberAssignments;

        assignments = assignmentDAO.getAssignmentsByIdProjectColaborative(collaborativeProject.getIdCollaborativeProject());
        numberAssignments = assignments.size();

        return numberAssignments;
    }

    /**
     * Recupera un proyecto colaborativo por su ID.
     *
     * @param idCollaborativeProject El ID del proyecto colaborativo.
     * @return El proyecto colaborativo correspondiente al ID proporcionado.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    @Override
    public CollaborativeProject getCollaborativeProjectByIdCollaborativeProject(int idCollaborativeProject) throws DAOException {
        CollaborativeProject collaborativeProject = new CollaborativeProject();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM ProyectoColaborativo"
                + " WHERE idProyectoColaborativo = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, idCollaborativeProject);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                collaborativeProject = initializeCollaborativeProject(resultSet);
            }
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar el proyecto colaborativo", Status.ERROR);
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
                Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            }
        }
        return collaborativeProject;
    }

    /**
     * Recupera un proyecto colaborativo finalizado por el ID del curso.
     *
     * @param idCourse El ID del curso.
     * @return El proyecto colaborativo finalizado correspondiente al ID del curso proporcionado.
     * @throws DAOException si hay un error al acceder a los datos.
     */
    
    @Override
    public CollaborativeProject getFinishedCollaborativeProjectByIdCourse(int idCourse)
            throws DAOException {
        CollaborativeProject collaborativeProject = new CollaborativeProject();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM ProyectoColaborativo"
                + " WHERE estado = 'Finalizado' AND"
                + " (idCursoSolicitante = ? OR idCursoSolicitado = ?)";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, idCourse);
            preparedStatement.setInt(2, idCourse);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                collaborativeProject = initializeCollaborativeProject(resultSet);
            }
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar el proyecto colaborativo", Status.ERROR);
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
                Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            }
        }
        return collaborativeProject;
    }

    public String checkCollaborativeProjectStatus(CollaborativeProject collaborativeProject)
            throws DAOException {
        String status = "";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT estado FROM ProyectoColaborativo"
                + " WHERE idProyectoColaborativo = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, collaborativeProject.getIdCollaborativeProject());

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                status = resultSet.getString("estado");
            }
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
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
                Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            }
        }
        return status;
    }

    public int deleteCollaborativeProjectByidCollaborativeProject(int idCollaborativeProject)
            throws DAOException {
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
        } catch (SQLException exception) {
            Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue eliminar el proyecto colaborativo", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Log.getLogger(CollaborativeProjectDAO.class).error(exception.getMessage(), exception);
            }
        }
        return rowsAffected;
    }
}