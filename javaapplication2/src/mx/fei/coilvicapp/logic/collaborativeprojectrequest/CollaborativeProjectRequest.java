package mx.fei.coilvicapp.logic.collaborativeprojectrequest;
import mx.fei.coilvicapp.logic.course.Course;

/*
 * @author d0ubl3_d
 */
public class CollaborativeProjectRequest {
    
    private int idCollaborativeProjectRequest = 0;
    
    private Course requesterCourse;
    private Course requestedCourse;
    
    private String status;
    private String requestDate;
    private String validationDate;
    
    public CollaborativeProjectRequest() {
    }
    
    public int getIdCollaboratibeProjectRequest() {
        return idCollaborativeProjectRequest;
    }
    
    public Course getRequesterCourse() {
        return requesterCourse;
    }
    
    public void setRequesterCourse(Course requesterCourse) {
        if (requesterCourse == null) {
            throw new IllegalArgumentException("Debe sleecionar uno de sus cursos para enviar la solicitud");
        }
        if (requestedCourse != null && requesterCourse.getProfessor().getIdProfessor() == 
        requestedCourse.getProfessor().getIdProfessor()) {
            throw new IllegalArgumentException("No puede enviar una solicitud a si mismo");
        }
        this.requesterCourse = requesterCourse;
    }
    
    public Course getRequestedCourse() {
        return requestedCourse;
    }
    
    public void setRequestedCourse(Course requestedCourse) {
        if (requestedCourse == null) {
            throw new IllegalArgumentException("Debe sleecionar un curso al que enviar solicitud");
        }
        if (requesterCourse != null && requestedCourse.getProfessor().getIdProfessor() == 
        requesterCourse.getProfessor().getIdProfessor()) {
            throw new IllegalArgumentException("No puede enviar una solicitud a si mismo");
        }
        this.requestedCourse = requestedCourse;
    }
    
    public void setIdCollaboratibeProjectRequest(int idCollaborativeProjectRequest) {
        this.idCollaborativeProjectRequest = idCollaborativeProjectRequest;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getRequestDate() {
        return requestDate;
    }
    
    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }
    
    public String getValidationDate() {
        return validationDate;
    }
    
    public void setValidationDate(String validationDate) {
        this.validationDate = validationDate;
    }
     
    @Override
    public String toString() {
        return requestedCourse.toString() + " - " + requesterCourse.toString();
    } 
}
