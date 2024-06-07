package mx.fei.coilvicapp.logic.professor;

import java.util.ArrayList;
import javax.mail.MessagingException;
import mx.fei.coilvicapp.logic.implementations.DAOException;

public interface IProfessor {
    
    public boolean checkPreconditions() throws DAOException;
    public int registerProfessor(Professor professor) throws DAOException;
    public int registerProfessorUV(ProfessorUV professorUV) throws DAOException;
    public int updateProfessor(Professor newProfessorInformation) throws DAOException;
    public int updateProfessorUV(ProfessorUV newProfessorUVInformation) throws DAOException;
    public int deleteProfessorByID(int idProfessor) throws DAOException;
    public int deleteProfessorUVByID(int idProfessor) throws DAOException;    
    public Professor getProfessorById(int idProfessor)throws DAOException;
    public Professor getProfessorByEmail(String professorEmail) throws DAOException;
    public ProfessorUV getProfessorUVByPersonalNumber(int personalNumber) throws DAOException;
    public ArrayList<Professor> getAllProfessors() throws DAOException;
    public ArrayList<Professor> getProfessorsByPendingStatus() throws DAOException;
    public int acceptProfessor(Professor professor) throws DAOException;
    public int rejectProfessor(Professor professor) throws DAOException;
}