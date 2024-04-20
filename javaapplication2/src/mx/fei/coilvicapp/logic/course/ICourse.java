package mx.fei.coilvicapp.logic.course;

import java.util.List;
import mx.fei.coilvicapp.logic.implementations.DAOException;

/*
 * @author d0ubl3_d
 */

public interface ICourse {

    public int insertCourse(Course course) throws DAOException;
    public List<Course> getCoursesByStatus(String state) throws DAOException;    
    public int updateCourseStatusByIdCourse(int idCourse, String newState) throws DAOException;
    
}
