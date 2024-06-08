package mx.fei.coilvicapp.logic.collaborativeproject;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.collaborativeprojectrequest.CollaborativeProjectRequest;
import mx.fei.coilvicapp.logic.implementations.DAOException;

/*
 * @author d0ubl3_d
 */

public interface ICollaborativeProject {
    
    public int registerCollaborativeProject(CollaborativeProject collaborativeProject,
    CollaborativeProjectRequest collaborativeProjectRequest) throws DAOException;
    public ArrayList<CollaborativeProject> getCollaborativeProjectsProposals() throws DAOException;    
    public int evaluateCollaborativeProjectProposal(CollaborativeProject collaborativeProject, String status) throws DAOException;    
    public ArrayList<CollaborativeProject> getPendingCollaborativeProjectsByProfessor(int idProfessor) throws DAOException;
    public ArrayList<CollaborativeProject> getAcceptedCollaborativeProjectsByProfessor(int idProfessor) throws DAOException;
    public ArrayList<CollaborativeProject> getRejectedCollaborativeProjectsByProfessor(int idProfessor) throws DAOException;
    public ArrayList<CollaborativeProject> getFinishedCollaborativeProjectsByProfessor(int idProfessor) throws DAOException;
    public CollaborativeProject getCollaborativeProjectByCode(String code) throws DAOException;
    public int updateCollaborativeProject(CollaborativeProject collaborativeProject) throws DAOException;
    public int finalizeCollaborativeProject(CollaborativeProject collaborativeProject) throws DAOException;
    public ArrayList<CollaborativeProject> getAllAcceptedCollaborativeProjects() throws DAOException;
    public ArrayList<CollaborativeProject> getAllRejectedCollaborativeProjects() throws DAOException;
    public ArrayList<CollaborativeProject> getAllFinishedCollaborativeProjects() throws DAOException;
    public boolean hasThreeActivitiesAtLeast(CollaborativeProject collaborativeProject) throws DAOException;
    public CollaborativeProject getFinishedCollaborativeProjectByIdCourse(int idCourse)
    throws DAOException;
    public CollaborativeProject getCollaborativeProjectByIdCollaborativeProject
    (int idCollaborativeProject) throws DAOException;
}