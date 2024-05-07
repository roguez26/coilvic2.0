package mx.fei.coilvicapp.logic.student;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;

public interface IStudent {
    
    public int registerStudent(Student student) throws DAOException;
    public int updateStudent(Student newStudentInformation) throws DAOException;
    public int deleteStudentById(int idStudent) throws DAOException;
    public Student getStudentById(int idStudent) throws DAOException;
    public Student getStudentByEmail(String studentEmail) throws DAOException;
    public ArrayList<Student> getAllStudents() throws DAOException;
   
}
