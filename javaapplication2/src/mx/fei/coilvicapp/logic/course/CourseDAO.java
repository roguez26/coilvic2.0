package mx.fei.coilvicapp.logic.course;

import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.professor.ProfessorDAO;
import mx.fei.coilvicapp.logic.language.LanguageDAO;
import mx.fei.coilvicapp.logic.term.TermDAO;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;
import mx.fei.coilvicapp.logic.implementations.Status;

public class CourseDAO implements ICourse {

    public CourseDAO() {

    }

    /**
     * Registra un nuevo curso en la base de datos.
     *
     * @param course El curso a registrar.
     * @return El resultado de la operación de registro: 1 si se registró correctamente, -1 si no se pudo
     * registrar debido a duplicados.
     * @throws DAOException Si ocurre un error durante el registro del curso.
     */
    @Override
    public int registerCourse(Course course) throws DAOException {
        int result = -1;

        if (!checkCourseDuplicate(course)) {
            result = insertCourse(course);
        }
        return result;
    }

    private boolean checkCourseDuplicate(Course course) throws DAOException {
        boolean check = false;
        Course auxCourse = new Course();

        try {
            auxCourse = getCourseByProfessorNameAndTerm(course);
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validación", Status.WARNING);
        }
        if (auxCourse.getIdCourse() != course.getIdCourse() && auxCourse.getIdCourse() != 0) {
            throw new DAOException("Ya existe un curso con el mismo nombre y periodo", Status.WARNING);
        }
        return check;
    }

    private int insertCourse(Course course) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "INSERT INTO Curso (idProfesor, idIdioma,"
        + " idPeriodo, nombre, objetivoGeneral, temasInteres,"
        + " numeroEstudiantes, perfilEstudiantes, informacionAdicional)"
        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        DatabaseManager databaseManager = new DatabaseManager();
        ResultSet resultSet = null;
        int result = -1;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);

            preparedStatement.setInt(1, course.getProfessor().getIdProfessor());
            preparedStatement.setInt(2, course.getLanguage().getIdLanguage());
            preparedStatement.setInt(3, course.getTerm().getIdTerm());
            preparedStatement.setString(4, course.getName());
            preparedStatement.setString(5, course.getGeneralObjective());
            preparedStatement.setString(6, course.getTopicsInterest());
            preparedStatement.setInt(7, course.getNumberStudents());
            preparedStatement.setString(8, course.getStudentsProfile());
            preparedStatement.setString(9, course.getAdditionalInformation());

            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar el curso", Status.ERROR);
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
                Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            }
        }
        return result;
    }

    /**
     * Obtiene todas las propuestas de cursos pendientes de aprobación.
     *
     * @return Una lista de cursos que están pendientes de aprobación.
     * @throws DAOException Si ocurre un error al obtener las propuestas de cursos.
     */
    @Override
    public ArrayList<Course> getCourseProposals() throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByStatus("Pendiente");
        return courses;
    }

    /**
     * Obtiene todas las propuestas de cursos pendientes de aprobación de una universidad específica.
     *
     * @param nameUniversity El nombre de la universidad para la cual se desean obtener las propuestas de
     * cursos.
     * @return Una lista de cursos propuestos pendientes de aprobación de la universidad especificada.
     * @throws DAOException Si ocurre un error al obtener las propuestas de cursos por universidad.
     */
    @Override
    public ArrayList<Course> getCourseProposalsByUniversity(String nameUniversity) throws DAOException {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkName(nameUniversity);
        ArrayList<Course> courses;        
        courses = getCourseProposalsByUniversityName(nameUniversity, "Pendiente");
        return courses;
    }

    private ArrayList<Course> getCourseProposalsByUniversityName(String universityName, String status)
            throws DAOException {
        ArrayList<Course> courses = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT Curso.* FROM Curso"
                + " JOIN Profesor ON curso.idProfesor = Profesor.idProfesor"
                + " JOIN Universidad ON profesor.idUniversidad = Universidad.idUniversidad"
                + " WHERE Universidad.nombre LIKE ? AND Curso.estado = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, "%" + universityName + "%");
            preparedStatement.setString(2, status);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                courses.add(initializeCourse(resultSet));
            }
        } catch (SQLException exception) {
            Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener los cursos", Status.ERROR);
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
                Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            }
        }
        return courses;
    }

    /**
     * Obtiene todos los cursos ofrecidos que han sido aceptados.
     *
     * @return Una lista de cursos que han sido aceptados y están disponibles como ofertas.
     * @throws DAOException Si ocurre un error al obtener los cursos ofrecidos.
     */
    @Override
    public ArrayList<Course> getCourseOfferings() throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByStatus("Aceptado");
        return courses;
    }

    /**
     * Obtiene todos los cursos ofrecidos por una universidad específica que han sido aceptados.
     *
     * @param nameUniversity El nombre de la universidad para la cual se desean obtener los cursos ofrecidos.
     * @return Una lista de cursos ofrecidos que han sido aceptados por la universidad especificada.
     * @throws DAOException Si ocurre un error al obtener los cursos ofrecidos por universidad.
     */
    @Override
    public ArrayList<Course> getCourseOfferingsByUniversity(String nameUniversity) throws DAOException {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkName(nameUniversity);
        ArrayList<Course> courses;
        courses = getCourseProposalsByUniversityName(nameUniversity, "Aceptado");
        return courses;
    }

    public Course getCourseByIdCourse(int idCourse) throws DAOException {
        Course course = new Course();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM curso WHERE idCurso = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, idCourse);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                course = initializeCourse(resultSet);
            }
        } catch (SQLException exception) {
            Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar el curso", Status.ERROR);
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
                Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            }
        }
        return course;
    }

    private Course initializeCourse(ResultSet resultSet) throws DAOException {
        Course course = new Course();
        ProfessorDAO professorDAO = new ProfessorDAO();
        LanguageDAO langugeDAO = new LanguageDAO();
        TermDAO termDAO = new TermDAO();

        try {
            course.setIdCourse(resultSet.getInt("idCurso"));
            course.setProfessor(professorDAO.getProfessorById(resultSet.getInt("idProfesor")));
            course.setLanguage(langugeDAO.getLanguageByIdLanguage(resultSet.getInt("idIdioma")));
            course.setTerm(termDAO.getTermByIdTerm(resultSet.getInt("idPeriodo")));
            course.setName(resultSet.getString("nombre"));
            course.setStatus(resultSet.getString("estado"));
            course.setGeneralObjective(resultSet.getString("objetivoGeneral"));
            course.setTopicsInterest(resultSet.getString("temasInteres"));
            course.setNumberStudents(resultSet.getInt("numeroEstudiantes"));
            course.setStudentsProfile(resultSet.getString("perfilEstudiantes"));
            course.setAdditionalInformation(resultSet.getString("informacionAdicional"));
        } catch (SQLException exception) {
            Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
        }
        return course;
    }

    private ArrayList<Course> getCoursesByStatus(String status) throws DAOException {
        ArrayList<Course> courses = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM Curso WHERE estado = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, status);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                courses.add(initializeCourse(resultSet));
            }
        } catch (SQLException exception) {
            Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener los cursos", Status.ERROR);
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
                Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            }
        }
        return courses;
    }

    /**
     * Evalúa una propuesta de curso y actualiza su estado según la evaluación.
     *
     * @param course El curso que se va a evaluar.
     * @param status El nuevo estado que se asignará al curso después de la evaluación.
     * @return El resultado de la operación de evaluación (1 si se actualizó correctamente, de lo contrario,
     * -1).
     * @throws DAOException Si ocurre un error al evaluar la propuesta de curso.
     */
    
    @Override
    public int evaluateCourseProposal(Course course, String status) throws DAOException {
        int result = -1;
        switch (checkCourseStatus(course)) {
            case "Pendiente" ->
                result = updateCourseStatusByCourse(course, status);
            case "Cancelado" ->
                throw new DAOException("No puede evaluar un curso que fue cancelado", Status.WARNING);
            case "Colaboracion" ->
                throw new DAOException("No puede evaluar un curso que es parte de un proyecto colaborativo", Status.WARNING);
            case "Finalizado" ->
                throw new DAOException("No puede evaluar un curso finalizado", Status.WARNING);
            default ->
                throw new DAOException("No puede evaluar un curso que ya fue evaluado", Status.WARNING);
        }
        return result;
    }

    private int updateCourseStatusByCourse(Course course, String status) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE Curso SET estado = ? where idCurso = ?";
        int rowsAffected = -1;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, course.getIdCourse());

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible evaluar el curso", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            }
        }
        return rowsAffected;
    }

    /**
     * Actualiza la información de un curso en el sistema.
     *
     * @param course El curso con la información actualizada que se va a guardar.
     * @return El resultado de la operación de actualización (1 si se actualizó correctamente, de lo contrario,
     * -1).
     * @throws DAOException Si ocurre un error al actualizar el curso.
     */
    @Override
    public int updateCourse(Course course) throws DAOException {
        int result = -1;
        String courseStatus = "";
        courseStatus = checkCourseStatus(course);
        if (courseStatus.equals("Pendiente")
        || courseStatus.equals("Rechazado")) {
            if (!checkCourseDuplicate(course)) {
                result = updateCoursePrivate(course);
            }
        } else {
            throw new DAOException("No puede actualizar un curso en colaboración, aceptado, cancelado o finalizado", Status.WARNING);
        }
        return result;
    }

    private int updateCoursePrivate(Course course) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE Curso SET idIdioma = ?, idPeriodo = ?,"
                + " nombre = ?, estado = 'Pendiente', objetivoGeneral = ?,"
                + " temasInteres = ?, numeroEstudiantes = ?, perfilEstudiantes = ?,"
                + " informacionAdicional = ? WHERE idCurso = ?";
        int rowsAffected = -1;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, course.getLanguage().getIdLanguage());
            preparedStatement.setInt(2, course.getTerm().getIdTerm());
            preparedStatement.setString(3, course.getName());
            preparedStatement.setString(4, course.getGeneralObjective());
            preparedStatement.setString(5, course.getTopicsInterest());
            preparedStatement.setInt(6, course.getNumberStudents());
            preparedStatement.setString(7, course.getStudentsProfile());
            preparedStatement.setString(8, course.getAdditionalInformation());
            preparedStatement.setInt(9, course.getIdCourse());

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible actualizar el curso", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            }
        }
        return rowsAffected;
    }

    private ArrayList<Course> getCoursesByIdProfessor(int idProfessor) throws DAOException {
        ArrayList<Course> courses = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM Curso WHERE idProfesor = ?";
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, idProfessor);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                courses.add(initializeCourse(resultSet));
            }
        } catch (SQLException exception) {
            Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener los cursos", Status.ERROR);
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
                Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            }
        }
        return courses;
    }

    /**
     * Obtiene todos los cursos asociados a un profesor.
     *
     * @param idProfessor El ID del profesor cuyos cursos se quieren obtener.
     * @return Una lista de cursos asociados al profesor.
     * @throws DAOException Si ocurre un error al obtener los cursos.
     */
    @Override
    public ArrayList<Course> getAllCoursesByProfessor(int idProfessor) throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByIdProfessor(idProfessor);
        return courses;
    }

    /**
     * Obtiene los cursos pendientes asociados a un profesor.
     *
     * @param idProfessor El ID del profesor cuyos cursos pendientes se quieren obtener.
     * @return Una lista de cursos pendientes asociados al profesor.
     * @throws DAOException Si ocurre un error al obtener los cursos pendientes.
     */
    @Override
    public ArrayList<Course> getPendingCoursesByProfessor(int idProfessor) throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByIdProfessorAndStatus(idProfessor, "Pendiente");
        return courses;
    }

    /**
     * Obtiene los cursos aceptados asociados a un profesor.
     *
     * @param idProfessor El ID del profesor cuyos cursos aceptados se quieren obtener.
     * @return Una lista de cursos aceptados asociados al profesor.
     * @throws DAOException Si ocurre un error al obtener los cursos aceptados.
     */
    @Override
    public ArrayList<Course> getAcceptedCoursesByProfessor(int idProfessor) throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByIdProfessorAndStatus(idProfessor, "Aceptado");
        return courses;
    }

    /**
     * Obtiene los cursos rechazados asociados a un profesor.
     *
     * @param idProfessor El ID del profesor cuyos cursos rechazados se quieren obtener.
     * @return Una lista de cursos rechazados asociados al profesor.
     * @throws DAOException Si ocurre un error al obtener los cursos rechazados.
     */
    @Override
    public ArrayList<Course> getRejectedCoursesByProfessor(int idProfessor) throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByIdProfessorAndStatus(idProfessor, "Rechazado");
        return courses;
    }

    /**
     * Obtiene los cursos de colaboración asociados a un profesor.
     *
     * @param idProfessor El ID del profesor cuyos cursos de colaboración se quieren obtener.
     * @return Una lista de cursos de colaboración asociados al profesor.
     * @throws DAOException Si ocurre un error al obtener los cursos de colaboración.
     */
    @Override
    public ArrayList<Course> getColaborationCoursesByProfessor(int idProfessor) throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByIdProfessorAndStatus(idProfessor, "Colaboracion");
        return courses;
    }

    /**
     * Obtiene los cursos finalizados asociados a un profesor.
     *
     * @param idProfessor El ID del profesor cuyos cursos finalizados se quieren obtener.
     * @return Una lista de cursos finalizados asociados al profesor.
     * @throws DAOException Si ocurre un error al obtener los cursos finalizados.
     */
    @Override
    public ArrayList<Course> getFinishedCoursesByProfessor(int idProfessor) throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByIdProfessorAndStatus(idProfessor, "Finalizado");
        return courses;
    }

    /**
     * Obtiene los cursos cancelados asociados a un profesor.
     *
     * @param idProfessor El ID del profesor cuyos cursos cancelados se quieren obtener.
     * @return Una lista de cursos cancelados asociados al profesor.
     * @throws DAOException Si ocurre un error al obtener los cursos cancelados.
     */
    @Override
    public ArrayList<Course> getCancelledCoursesByProfessor(int idProfessor) throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByIdProfessorAndStatus(idProfessor, "Cancelado");
        return courses;
    }

    /**
     * Obtiene los cursos asociados a un profesor por nombre.
     *
     * @param idProfessor El ID del profesor cuyos cursos se quieren obtener.
     * @param name El nombre o parte del nombre de los cursos a buscar.
     * @return Una lista de cursos asociados al profesor que coinciden con el nombre especificado.
     * @throws DAOException Si ocurre un error al obtener los cursos por nombre.
     */
    @Override
    public ArrayList<Course> getCoursesByProfessorAndName(int idProfessor, String name)
    throws DAOException {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkName(name);
        ArrayList<Course> courses;
        courses = getCoursesByIdProfessorAndName(idProfessor, name);
        return courses;
    }

    private ArrayList<Course> getCoursesByIdProfessorAndName(int idProfessor, String name)
            throws DAOException {
        ArrayList<Course> courses = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM Curso WHERE idProfesor = ?"
                + " AND nombre LIKE ?";
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, idProfessor);
            preparedStatement.setString(2, "%" + name + "%");

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                courses.add(initializeCourse(resultSet));
            }
        } catch (SQLException exception) {
            Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener los cursos", Status.ERROR);
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
                Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            }
        }
        return courses;
    }

    /**
     * Cancela la propuesta de un curso.
     *
     * @param course El curso cuya propuesta se quiere cancelar.
     * @return El resultado de la operación de cancelación.
     * @throws DAOException Si ocurre un error al cancelar la propuesta del curso.
     */
    @Override
    public int cancelCourseProposal(Course course) throws DAOException {
        int result = -1;

        switch (checkCourseStatus(course)) {
            case "Pendiente", "Rechazado" ->
                result = updateCourseStatusByCourse(course, "Cancelado");
            case "Colaboracion" ->
                throw new DAOException("No puede cancelar un curso que es parte de un proyecto colaborativo", Status.WARNING);
            case "Finalizado" ->
                throw new DAOException("No puede cancelar un curso finalizado", Status.WARNING);
            case "Cancelado" ->
                throw new DAOException("No puede cancelar un curso que ya fue cancelado", Status.WARNING);
            default -> {
            }
        }
        return result;
    }

    /**
     * Cambia el estado de un curso a "Colaboración".
     *
     * @param course El curso cuyo estado se quiere cambiar a "Colaboración".
     * @return El resultado de cambiar el estado del curso.
     * @throws DAOException Si ocurre un error al cambiar el estado del curso a "Colaboración".
     */
    @Override
    public int changeCourseStatusToCollaboration(Course course) throws DAOException {
        int result = -1;
        if (checkCourseStatus(course).equals("Aceptado")) {
            result = updateCourseStatusByCourse(course, "Colaboracion");
        } else {
            throw new DAOException("No puede colaborar con un curso que no fue aceptado", Status.WARNING);
        }
        return result;
    }

    /**
     * Finaliza un curso.
     *
     * @param course El curso que se desea finalizar.
     * @return El resultado de la operación de finalización del curso.
     * @throws DAOException Si ocurre un error al finalizar el curso o si el curso es parte de un proyecto
     * colaborativo.
     */
    @Override
    public int finalizeCourse(Course course) throws DAOException {
        CollaborativeProjectDAO collaborativeProjectDAO = new CollaborativeProjectDAO();
        int result = -1;

        if (collaborativeProjectDAO.getFinishedCollaborativeProjectByIdCourse(course.getIdCourse()).getIdCollaborativeProject() > 0) {
            result = updateCourseStatusByCourse(course, "Finalizado");
        } else {
            throw new DAOException("No puede finalizar un curso que no es parte de un proyecto colaborativo finalizado", Status.WARNING);
        }
        return result;
    }

    private ArrayList<Course> getCoursesByIdProfessorAndStatus(int idProfessor, String status)
            throws DAOException {
        ArrayList<Course> courses = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM Curso WHERE idProfesor = ? AND estado = ?";
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, idProfessor);
            preparedStatement.setString(2, status);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                courses.add(initializeCourse(resultSet));
            }
        } catch (SQLException exception) {
            Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener los cursos", Status.ERROR);
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
                Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            }
        }
        return courses;
    }

    public String checkCourseStatus(Course course) throws DAOException {
        String status = "";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT estado FROM Curso"
                + " WHERE idCurso = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, course.getIdCourse());

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                status = resultSet.getString("estado");
            }
        } catch (SQLException exception) {
            Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
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
                Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            }
        }
        return status;
    }

    public Course getCourseByProfessorNameAndTerm(Course course) throws DAOException {
        Course auxCourse = new Course();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM Curso WHERE"
        + " idProfesor = ? AND nombre = ? AND idPeriodo = ?";        
                
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setInt(1, course.getProfessor().getIdProfessor());
            preparedStatement.setString(2, course.getName());
            preparedStatement.setInt(3, course.getTerm().getIdTerm());
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                auxCourse = initializeCourse(resultSet);
            }
        } catch (SQLException exception) {
            Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar el curso", Status.ERROR);
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
                Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            }
        }
        return auxCourse;
    }

    public int deleteCourseByIdCourse(int idCourse) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "DELETE FROM Curso WHERE idCurso = ?";
        int rowsAffected = -1;

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setInt(1, idCourse);

            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar el curso", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Log.getLogger(CourseDAO.class).error(exception.getMessage(), exception);
            }
        }
        return rowsAffected;
    }
}
