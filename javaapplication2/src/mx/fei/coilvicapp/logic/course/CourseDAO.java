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
        String statement = "insert into Curso (idProfesor, nombre,"
        + " objetivoGeneral, temasInteres, numeroEstudiantes, perfilEstudiantes,"
        + " periodo, idioma, informacionAdicional)"
        + " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        DatabaseManager databaseManager = new DatabaseManager();
        int rowsAffected = -1; 
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setInt(1, course.getProfessor().getIdProfessor());
            preparedStatement.setString(2, course.getName());        
            preparedStatement.setString(3, course.getGeneralObjective());
            preparedStatement.setString(4, course.getTopicsInterest());
            preparedStatement.setInt(5,course.getNumerStudents());
            preparedStatement.setString(6, course.getStudentsProfile());
            preparedStatement.setString(7, course.getTerm());
            preparedStatement.setString(8, course.getLanguage());
            preparedStatement.setString(9, course.getAdditionalInformation());
            
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible registrar el curso", Status.WARNING);
        } finally {
            try {
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
        return rowsAffected;
    }
    
    @Override
    public List<Course> getCoursesByStatus(String state) throws DAOException {
        List<Course> courses = new ArrayList<>();
        Course course = new Course();
        Professor professor = new Professor();
        ProfessorDAO professorDAO = new ProfessorDAO();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "select * from Curso where estado = ?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,state);
            
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet != null && resultSet.next()) {
                course.setIdProfessor(resultSet.getInt("idProfesor"));
                course.setName(resultSet.getString("nombre"));
                course.setStatus(resultSet.getString("estado"));
                course.setGeneralObjective(resultSet.getString("objetivoGeneral"));
                course.setTopicsInterest(resultSet.getString("temasInteres"));
                course.setNumberStudents(resultSet.getInt("numeroEstudiantes"));
                course.setStudentsProfile(resultSet.getString("perfilEstudiantes"));
                course.setTerm(resultSet.getString("periodo"));
                course.setLanguage(resultSet.getString("idioma"));
                course.setAdditionalInformation(resultSet.getString("informacionAdicional"));
                professor = professorDAO.getProfessorById(course.getIdProfessor());
                course.setProfessor(professor);
                courses.add(course);
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
    public int updateCourseStatusByIdCourse(int courseId, String newState) throws DAOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "update Curso set estado = ? where idCurso = ?";
        int rowsAffected = -1;
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,newState);
            preparedStatement.setInt(2,courseId);
            
            rowsAffected = preparedStatement.executeUpdate();
        } catch(SQLException exception) {
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible cambiar el estado del curso", Status.WARNING);
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

