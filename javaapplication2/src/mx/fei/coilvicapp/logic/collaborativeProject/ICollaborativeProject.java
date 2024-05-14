package mx.fei.coilvicapp.logic.collaborativeproject;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;

/*
 * @author d0ubl3_d
 */

public interface ICollaborativeProject {
    
    public int registerCollaborativeProject(CollaborativeProject collaborativeProject) throws DAOException;
    public ArrayList<CollaborativeProject> getCollaborativeProjectsByStatus(String state) throws DAOException;
    public ArrayList<CollaborativeProject> getCollaborativeProjectsByidProfessorAndStatus(int idProfessor, String status) throws DAOException;
    public CollaborativeProject getCollaborativeProjectByCode(String code) throws DAOException;
    public int updateCollaborativeProject(CollaborativeProject collaborativeProject) throws DAOException;
    public int deleteCollaborativeProjectByidCollaborativeProject(int idCollaborativeProject) throws DAOException;
}
