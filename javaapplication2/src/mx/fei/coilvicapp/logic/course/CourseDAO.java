package mx.fei.coilvicapp.logic.course;

import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.professor.*;
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

public class CourseDAO implements ICourse{
    
    public CourseDAO() {
        
    }
    
    @Override
    public int insertCourse(Course course) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "INSERT INTO Curso (idProfesor, nombre,"
        + " objetivoGeneral, temasInteres, numeroEstudiantes, perfilEstudiantes,"
        + " periodo, idioma, informacionAdicional)"
        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        DatabaseManager databaseManager = new DatabaseManager();
        ResultSet resultSet = null;
        int result = -1; 
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);
            
            preparedStatement.setInt(1, course.getProfessor().getIdProfessor());
            preparedStatement.setString(2, course.getName());        
            preparedStatement.setString(3, course.getGeneralObjective());
            preparedStatement.setString(4, course.getTopicsInterest());
            preparedStatement.setInt(5,course.getNumerStudents());
            preparedStatement.setString(6, course.getStudentsProfile());
            preparedStatement.setString(7, course.getTerm());
            preparedStatement.setString(8, course.getLanguage());
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
            } catch(SQLException exception) {
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return result;
    }
    
    @Override
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
            } catch (SQLException exception){
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return course;
    }
    
    private Course initializeCourse(ResultSet resultSet) throws DAOException {
        Course course = new Course();
        ProfessorDAO professorDAO = new ProfessorDAO();
        
        try {
            course.setIdCourse(resultSet.getInt("idCurso"));
            course.setName(resultSet.getString("nombre"));
            course.setStatus(resultSet.getString("estado"));
            course.setGeneralObjective(resultSet.getString("objetivoGeneral"));
            course.setTopicsInterest(resultSet.getString("temasInteres"));
            course.setNumberStudents(resultSet.getInt("numeroEstudiantes"));
            course.setStudentsProfile(resultSet.getString("perfilEstudiantes"));
            course.setTerm(resultSet.getString("periodo"));
            course.setLanguage(resultSet.getString("idioma"));
            course.setAdditionalInformation(resultSet.getString("informacionAdicional"));         
            course.setProfessor(professorDAO.getProfessorById(resultSet.getInt("idProfesor")));
        } catch (SQLException exception) {
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
        } 
        return course;
    }
    
    @Override
    public List<Course> getCoursesByStatus(String status) throws DAOException {
        List<Course> courses = new ArrayList<>();        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM Curso WHERE estado = ?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setString(1,status);
            
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
            } catch (SQLException exception){
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return courses;
    }
   
    @Override
    public int updateCourseStatusByIdCourse(int idCourse, String status) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE Curso SET estado = ? where idCurso = ?";
        int rowsAffected = -1;
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setString(1,status);
            preparedStatement.setInt(2,idCourse);
            
            rowsAffected = preparedStatement.executeUpdate();
        } catch(SQLException exception) {
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
        if (!course.getStatus().equals("Aceptado")) {
            result = updateCoursePrivate(course);
        } else {
            throw new DAOException("No puede actualizar un curso que ya fue aceptado", Status.WARNING);
        }
        return result;
    }
    
    private int updateCoursePrivate(Course course) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE Curso SET nombre = ?, estado = 'Pendiente',"
        + " objetivoGeneral = ?, temasInteres = ?, numeroEstudiantes = ?,"
        + " perfilEstudiantes = ?, periodo = ?, idioma = ?, informacionAdicional = ?";
        int rowsAffected = -1;
       
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setString(1,course.getName());
            preparedStatement.setString(2,course.getGeneralObjective());
            preparedStatement.setString(3,course.getTopicsInterest());
            preparedStatement.setInt(4, course.getNumerStudents());
            preparedStatement.setString(5, course.getStudentsProfile());
            preparedStatement.setString(6,course.getTerm());
            preparedStatement.setString(7,course.getLanguage());
            preparedStatement.setString(8,course.getAdditionalInformation());
            
            rowsAffected = preparedStatement.executeUpdate();
        }catch (SQLException exception) {
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
    
    @Override 
    public List<Course> getCoursesByIdProfessor(int idProfessor) throws DAOException {     
        List<Course> courses = new ArrayList<>();        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM Curso WHERE idProfesor = ?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setInt(1,idProfessor);
            
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
            } catch (SQLException exception){
                Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return courses;   
    }
    
    @Override
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