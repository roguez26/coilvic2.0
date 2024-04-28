package mx.fei.coilvicapp.logic.professor;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;

public interface IProfessor {
    
    public int insertProfessor(Professor professor) throws DAOException;
    public int updateProfessor(Professor newProfessorInformation) throws DAOException;
    public int deleteProfessorByID(int idProfessor) throws DAOException;
    public Professor getProfessorById(int idProfessor)throws DAOException;
    public Professor getProfessorByEmail(String professorEmail) throws DAOException;
    public ArrayList<Professor> getAllProfessors() throws DAOException;
   
}
