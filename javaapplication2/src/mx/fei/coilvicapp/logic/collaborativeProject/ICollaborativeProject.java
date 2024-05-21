package mx.fei.coilvicapp.logic.collaborativeproject;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;

/*
 * @author d0ubl3_d
 */

public interface ICollaborativeProject {
    
    public int registerCollaborativeProject(CollaborativeProject collaborativeProject) throws DAOException;
    public ArrayList<CollaborativeProject> getCollaborativeProjectsProposals() throws DAOException;    
    public int evaluateCollaborativeProjectProposal(CollaborativeProject collaborativeProject, String status) throws DAOException;    
    public ArrayList<CollaborativeProject> getPendingCollaborativeProjectsByProfessor(int idProfessor, String status) throws DAOException;
    public ArrayList<CollaborativeProject> getAcceptedCollaborativeProjectsByProfessor(int idProfessor, String status) throws DAOException;
    public ArrayList<CollaborativeProject> getRejectedCollaborativeProjectsByProfessor(int idProfessor, String status) throws DAOException;
    public ArrayList<CollaborativeProject> getFinishedCollaborativeProjectsByProfessor(int idProfessor, String status) throws DAOException;
    public CollaborativeProject getCollaborativeProjectByCode(String code) throws DAOException;
    public int updateCollaborativeProject(CollaborativeProject collaborativeProject) throws DAOException;
    public int finalizeCollaborativeProject(CollaborativeProject collaborativeProject) throws DAOException;
}
