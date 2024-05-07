package mx.fei.coilvicapp.logic.course;

import java.util.List;
import mx.fei.coilvicapp.logic.implementations.DAOException;

/*
 * @author d0ubl3_d
 */

public interface ICourse {

    public int insertCourse(Course course) throws DAOException;
    public Course getCourseByIdCourse(int idCourse) throws DAOException;
    public List<Course> getCoursesByStatus(String state) throws DAOException;
    public int updateCourse(Course course) throws DAOException;
    public int updateCourseStatusByIdCourse(int idCourse, String status) throws DAOException;    
    public List<Course> getCoursesByIdProfessor(int idProfessor) throws DAOException;
    public int deleteCourseByIdCourse(int idCourse) throws DAOException;
}
