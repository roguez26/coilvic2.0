package mx.fei.coilvicapp.logic.university;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.util.ArrayList;
/**
 *
 * @author ivanr
 */
public interface IUniversity {
    public int insertUniversity(University university) throws DAOException;
    
    public int updateUniversity(University university) throws DAOException;
    
    public int deleteUniversity(University university) throws DAOException;
    
    public ArrayList<University> getAllUniversities() throws DAOException;
    
    public University getUniversityById(int idUniversity) throws DAOException;
}
