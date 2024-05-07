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
        this.requesterCourse = requesterCourse;
    }
    
    public Course getRequestedCourse() {
        return requestedCourse;
    }
    
    public void setRequestedCourse(Course requestedCourse) {
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
     
}
