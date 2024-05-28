package mx.fei.coilvicapp.logic.course;

import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.professor.ProfessorDAO;
import mx.fei.coilvicapp.logic.language.LanguageDAO;
import mx.fei.coilvicapp.logic.term.TermDAO;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;
import mx.fei.coilvicapp.logic.professor.Professor;

/*
 * @author d0ubl3_d
 */
public class CourseDAO implements ICourse {

    public CourseDAO() {

    }

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
            auxCourse = getCourseByNameAndIdTerm(course);
        } catch (DAOException exception) {
            throw new DAOException("No fue posible hacer la validación del proyecto colaborativo", Status.WARNING);
        }
        if (auxCourse.getIdCourse() != course.getIdCourse()
                && auxCourse.getIdCourse() != 0) {
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
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible registrar el curso", Status.WARNING);
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
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return result;
    }

    @Override
    public ArrayList<Course> getCourseProposals() throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByStatus("Pendiente");

        if (!courses.isEmpty()) {
            return courses;
        } else {
            throw new DAOException("No hay registros de cursos", Status.WARNING);
        }
    }
    
    @Override
    public ArrayList<Course> getCourseProposalsByUniversity(String nameUniversity) throws DAOException {
        ArrayList<Course> courses;
        courses = getCourseProposalsByUniversityName(nameUniversity, "Pendiente");
        
        if (!courses.isEmpty()) {
            return courses;            
        } else {
            throw new DAOException("No hay registros de cursos pendientes", Status.WARNING);
        }
    }
    
    private ArrayList<Course> getCourseProposalsByUniversityName(String universityName, String status) throws DAOException {
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

            preparedStatement.setString(1, "%" + universityName  + "%");
            preparedStatement.setString(2,status);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                courses.add(initializeCourse(resultSet));
            }
        } catch (SQLException exception) {
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener los cursos", Status.WARNING);
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
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return courses;
    }
    

    @Override
    public ArrayList<Course> getCourseOfferings() throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByStatus("Aceptado");

        if (!courses.isEmpty()) {
            return courses;
        } else {
            throw new DAOException("No hay cursos en la oferta", Status.WARNING);
        }
    }
    
    @Override
    public ArrayList<Course> getCourseOfferingsByUniversity(String nameUniversity) throws DAOException {
        ArrayList<Course> courses;
        courses = getCourseProposalsByUniversityName(nameUniversity, "Aceptado");
        
        if (!courses.isEmpty()) {
            return courses;            
        } else {
            throw new DAOException("No hay registros de cursos en oferta", Status.WARNING);
        }
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
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible recuperar el curso", Status.WARNING);
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
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
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
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
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
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener los cursos", Status.WARNING);
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
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return courses;
    }

    @Override
    public int evaluateCourseProposal(Course course, String status) throws DAOException {
        int result = -1;

        if (course.getStatus().equals("Pendiente")) {
            result = updateCourseStatusByCourse(course, status);
        } else {
            throw new DAOException("No puede evaluar un curso que ya fue evaluado", Status.ERROR);
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
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible evaluar el curso", Status.WARNING);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return rowsAffected;
    }

    @Override
    public int updateCourse(Course course) throws DAOException {
        int result = -1;
        if (course.getStatus().equals("Pendiente")
                || course.getStatus().equals("Rechazado")) {
            if (!checkCourseDuplicate(course)) {
                result = updateCoursePrivate(course);
            }
        } else {
            throw new DAOException("No puede actualizar un curso que ya fue aceptado, cancelado o finalizado", Status.WARNING);
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
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible actualizar el curso", Status.WARNING);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
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
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener los cursos", Status.WARNING);
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
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return courses;
    }
    
    @Override
    public ArrayList<Course> getAllCoursesByProfessor(int idProfessor) throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByIdProfessor(idProfessor);

        if (!courses.isEmpty()) {
            return courses;
        } else {
            throw new DAOException("No hay cursos", Status.WARNING);
        }
    }
    
    @Override
    public ArrayList<Course> getPendingCoursesByProfessor(int idProfessor) throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByIdProfessorAndStatus(idProfessor, "Pendiente");

        if (!courses.isEmpty()) {
            return courses;
        } else {
            throw new DAOException("No hay cursos pendientes", Status.WARNING);
        }
    }

    @Override
    public ArrayList<Course> getAcceptedCoursesByProfessor(int idProfessor) throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByIdProfessorAndStatus(idProfessor, "Aceptado");

        if (!courses.isEmpty()) {
            return courses;
        } else {
            throw new DAOException("No hay cursos aceptados para colaborar", Status.ERROR);
        }
    }

    @Override
    public ArrayList<Course> getRejectedCoursesByProfessor(int idProfessor) throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByIdProfessorAndStatus(idProfessor, "Rechazado");

        if (!courses.isEmpty()) {
            return courses;
        } else {
            throw new DAOException("No hay cursos rechazados", Status.WARNING);
        }
    }
    
    public ArrayList<Course> getColaborationCoursesByProfessor(int idProfessor) throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByIdProfessorAndStatus(idProfessor, "Colaboracion");

        if (!courses.isEmpty()) {
            return courses;
        } else {
            throw new DAOException("No hay cursos en colaboración", Status.WARNING);
        }
    }            

    @Override
    public ArrayList<Course> getFinishedCoursesByProfessor(int idProfessor) throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByIdProfessorAndStatus(idProfessor, "Finalizado");

        if (!courses.isEmpty()) {
            return courses;
        } else {
            throw new DAOException("No hay cursos finalizados", Status.WARNING);
        }
    }
    
    @Override
    public ArrayList<Course> getCancelledCoursesByProfessor(int idProfessor) throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByIdProfessorAndStatus(idProfessor, "Cancelado");

        if (!courses.isEmpty()) {
            return courses;
        } else {
            throw new DAOException("No hay cursos cancelados", Status.WARNING);
        }
    }

    @Override     
    public ArrayList<Course> getCoursesByProfessorAndName(int idProfessor, String name) throws DAOException {
        ArrayList<Course> courses;
        courses = getCoursesByIdProfessorAndName(idProfessor, name);

        if (!courses.isEmpty()) {
            return courses;
        } else {
            throw new DAOException("No hay cursos con ese nombre", Status.WARNING);
        }
    }
    
    private ArrayList<Course> getCoursesByIdProfessorAndName(int idProfessor,  String name) throws DAOException {
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
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener los cursos", Status.WARNING);
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
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return courses;
    }
    
    @Override
    public int cancelCourseProposal(Course course) throws DAOException {
        int result = -1;

        switch (course.getStatus()) {
            case "Pendiente", "Rechazado" ->
                result = updateCourseStatusByCourse(course, "Cancelado");
            case "Colaboracion" ->
                throw new DAOException("No pude cancelar un curso que es parte de un proyecto colaborativo", Status.WARNING);
            case "Finalizado" ->
                throw new DAOException("No pude cancelar un curso finalizado", Status.WARNING);
            default -> {
            }
        }
        return result;
    }

    @Override
    public int finalizeCourse(Course course) throws DAOException {
        CollaborativeProjectDAO collaborativeProjectDAO = new CollaborativeProjectDAO();
        int result = -1;

        if (collaborativeProjectDAO.getFinishedCollaborativeProjectByIdCourse(course.getIdCourse()).getIdCollaborativeProject() != 0) {
            result = updateCourseStatusByCourse(course, "Finalizado");
        } else {
            throw new DAOException("No pude finalizar un curso que es parte de un proyecto colaborativo", Status.WARNING);
        }
        return result;
    }

    private ArrayList<Course> getCoursesByIdProfessorAndStatus(int idProfessor, String status) throws DAOException {
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
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener los cursos", Status.WARNING);
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
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return courses;
    }

    public Course getCourseByNameAndIdTerm(Course course) throws DAOException {
        Course auxCourse = new Course();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM curso WHERE nombre = ? AND idPeriodo = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, course.getName());
            preparedStatement.setInt(2, course.getTerm().getIdTerm());

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                auxCourse = initializeCourse(resultSet);
            }
        } catch (SQLException exception) {
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible recuperar el curso", Status.WARNING);
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
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
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
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible eliminar el curso", Status.WARNING);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return rowsAffected;
    }
}
