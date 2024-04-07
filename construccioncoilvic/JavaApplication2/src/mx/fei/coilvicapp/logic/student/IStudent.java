package mx.fei.coilvicapp.logic.student;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.util.List;

public interface IStudent {
    
    public int insertStudent(Student student) throws DAOException;
    public List<String> getStudentByEmail(String email) throws DAOException;
    
}