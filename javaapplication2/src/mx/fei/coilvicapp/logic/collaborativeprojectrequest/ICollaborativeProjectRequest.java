package mx.fei.coilvicapp.logic.collaborativeprojectrequest;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;

/*
 * @author d0ubl3_d
 */

public interface ICollaborativeProjectRequest {
 
    public int registerCollaborativeProjectRequest(CollaborativeProjectRequest collaborativeProjectRequest) throws DAOException;
    
    public ArrayList<CollaborativeProjectRequest> getCollaborativeProjectRequests(int idProfessor) throws DAOException;
    public ArrayList<CollaborativeProjectRequest> getReceivedCollaborativeProjectRequests(int idProfessor) throws DAOException; 
    public ArrayList<CollaborativeProjectRequest> getSentCollaborativeProjectRequests(int idProfessor) throws DAOException; 
    
    
    public ArrayList<CollaborativeProjectRequest> getPendingReceivedCollaborativeProjectRequests(int idProfessor) throws DAOException;
    public ArrayList<CollaborativeProjectRequest> getAcceptedReceivedCollaborativeProjectRequests(int idProfessor) throws DAOException;
    public ArrayList<CollaborativeProjectRequest> getRejectedReceivedCollaborativeProjectRequests(int idProfessor) throws DAOException;
    
    public ArrayList<CollaborativeProjectRequest> getPendingSentCollaborativeProjectRequests(int idProfessor) throws DAOException;
    public ArrayList<CollaborativeProjectRequest> getAcceptedSentCollaborativeProjectRequests(int idProfessor) throws DAOException;
    public ArrayList<CollaborativeProjectRequest> getRejectedSentCollaborativeProjectRequests(int idProfessor) throws DAOException;    
    public ArrayList<CollaborativeProjectRequest> getCancelledSentCollaborativeProjectRequests(int idProfessor) throws DAOException;
    
    public ArrayList<CollaborativeProjectRequest> getAcceptedCollaborativeProjectRequests(int idProfessor) throws DAOException;
    public int attendCollaborativeProjectRequest(CollaborativeProjectRequest collaborativeProjectRequest, String status) throws DAOException;
    public int cancelCollaborativeProjectRequest(CollaborativeProjectRequest collaborativeProjectRequest) throws DAOException;
    public int finalizeCollaborativeProjectRequest(CollaborativeProjectRequest collaborativeProjectRequest) throws DAOException;
    
    public ArrayList<CollaborativeProjectRequest> getAceptedCollaborativeProjectRequestsByIdProfessor
    (int idProfessor) throws DAOException;
}
