package mx.fei.coilvicapp.logic.professor;

import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.util.List;

public interface IProfessor {
    
    public int insertProfessor(Professor professor) throws DAOException;
    public int updateProfessor(Professor newProfessorInformation) throws DAOException;
    public int deleteProfessorByID(int idProfessor) throws DAOException;
    
}
