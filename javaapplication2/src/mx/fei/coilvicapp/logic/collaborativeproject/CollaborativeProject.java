package mx.fei.coilvicapp.logic.collaborativeproject;
import mx.fei.coilvicapp.logic.course.Course;
import mx.fei.coilvicapp.logic.modality.Modality;

/*
 * @author d0ubl3_d
 */

public class CollaborativeProject {
    
    private int idCollaborativeProject = 0;
    
    private Course requesterCourse;
    private Course requestedCourse; 
    private Modality modality;
    
    private String name;
    private String status;
    private String description;
    private String generalObjective;    
    private String code;
    private String syllabusPath;
    
    public CollaborativeProject() { 
    }
    
    public int getIdCollaborativeProject() {
        return idCollaborativeProject;
    }
    
    public void setIdCollaborativeProject(int idCollaborativeProject) {
        this.idCollaborativeProject = idCollaborativeProject;
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
    
    public Modality getModality() {
        return modality;
    }
    
    public void setModality(Modality modality) {
        this.modality = modality;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getGeneralObjective() {
        return generalObjective;
    }
    
    public void setGeneralObjective(String generalObjective) {
        this.generalObjective = generalObjective;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
 
    public String getSyllabusPath() {
        return syllabusPath;
    }
    
    public void setSyllabusPath(String syllabusPath) {
        this.syllabusPath = syllabusPath;
    }
    
    
}
