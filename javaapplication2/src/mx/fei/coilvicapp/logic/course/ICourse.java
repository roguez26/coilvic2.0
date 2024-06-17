package mx.fei.coilvicapp.logic.course;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;

public interface ICourse {

    public int registerCourse(Course course) throws DAOException;
    public ArrayList<Course> getCourseProposals() throws DAOException;
    public ArrayList<Course> getCourseProposalsByUniversity(String nameUniversity) throws DAOException;
    public ArrayList<Course> getCourseOfferings() throws DAOException;    
    public ArrayList<Course> getCourseOfferingsByUniversity(String nameUniversity) throws DAOException;    
    public int evaluateCourseProposal(Course course, String status) throws DAOException;    
    public int updateCourse(Course course) throws DAOException;
    public ArrayList<Course> getAllCoursesByProfessor(int idProfessor) throws DAOException;
    public ArrayList<Course> getPendingCoursesByProfessor(int idProfessor) throws DAOException;
    public ArrayList<Course> getAcceptedCoursesByProfessor(int idProfessor) throws DAOException;
    public ArrayList<Course> getRejectedCoursesByProfessor(int idProfessor) throws DAOException; 
    public ArrayList<Course> getColaborationCoursesByProfessor(int idProfessor) throws DAOException;
    public ArrayList<Course> getFinishedCoursesByProfessor(int idProfessor) throws DAOException;
    public ArrayList<Course> getCancelledCoursesByProfessor(int idProfessor) throws DAOException;
    public ArrayList<Course> getCoursesByProfessorAndName(int idProfessor, String name) throws DAOException;
    public int cancelCourseProposal(Course course) throws DAOException;
    public int changeCourseStatusToCollaboration(Course course) throws DAOException;
    public int finalizeCourse(Course course) throws DAOException;
}
