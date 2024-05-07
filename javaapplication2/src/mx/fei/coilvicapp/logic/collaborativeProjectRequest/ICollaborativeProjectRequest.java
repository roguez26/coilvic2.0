package mx.fei.coilvicapp.logic.collaborativeprojectrequest;

import java.util.List;
import mx.fei.coilvicapp.logic.implementations.DAOException;

/*
 * @author d0ubl3_d
 */

public interface ICollaborativeProjectRequest {
 
    public int registerCollaborativeProjectRequest(CollaborativeProjectRequest collaborativeProjectRequest) throws DAOException;
    public List<CollaborativeProjectRequest> getReceivedCollaborativeProjectRequestByIdProfessorAndStatus(int idProfessor, String status) throws DAOException;
    public List<CollaborativeProjectRequest> getSentCollaborativeProjectRequestByIdProfessorAndStatus(int idProfessor, String status) throws DAOException;
    public int attendCollaborativeProjectRequest(int idCollaborativeProjectRequest, String status) throws DAOException;
    public int deleteCollaborativeProjectRequestByidCollaborativeProjectRequest(int idCollaborativeProjectRequest) throws DAOException;
}
