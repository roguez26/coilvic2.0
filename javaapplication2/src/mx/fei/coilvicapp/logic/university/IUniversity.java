package mx.fei.coilvicapp.logic.university;

import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.util.ArrayList;

public interface IUniversity {
    
    public boolean isThereAtLeastOneUniversity() throws DAOException;
    
    public int registerUniversity (University university) throws DAOException;
    
    public int updateUniversity(University university) throws DAOException;
    
    public int deleteUniversity(int idUniversity) throws DAOException;
    
    public ArrayList<University> getAllUniversities() throws DAOException;
    
    public University getUniversityByName(String UniversityName) throws DAOException;
    
    public ArrayList<University> getAvailableUniversities() throws DAOException;
    
}
