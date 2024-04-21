package mx.fei.coilvicapp.logic.course;

import mx.fei.coilvicapp.logic.professor.*;

/*
 * @author d0ubl3_d
 */

public class Course {
    
    private Professor professor;
    private int idProfessor;
    
    private String name;
    private String status;
    private String generalObjective;
    private String topicsInterest;
    private int numberStudents;
    private String studentsProfile;
    private String term;
    private String language;
    private String additionalInformation;
        
    
    public Course() {        
    }
    
    public Professor getProfessor() {
        return professor;
    }
    
    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
    
    public int getIdProfessor() {
        return idProfessor;
    }
    
    public void setIdProfessor(int idProfessor) {
        this.idProfessor = idProfessor;
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
    
    public String getGeneralObjective() {
        return generalObjective;
    }
    
    public void setGeneralObjective(String generalObjective) {
        this.generalObjective = generalObjective;
    }
    
    public String getTopicsInterest() {
        return topicsInterest;
    }
    
    public void setTopicsInterest(String topicsInterest) {
        this.topicsInterest = topicsInterest;
    }
    
    public int getNumerStudents() {
        return numberStudents;
    }
    
    public void setNumberStudents(int numberStudents) {
        this.numberStudents = numberStudents;
    }
    
    public String getStudentsProfile() {
        return studentsProfile;
    }
    
    public void setStudentsProfile(String studentsProfile) {
        this.studentsProfile = studentsProfile;
    }
    
    public String getTerm() {
        return term;
    }
    
    public void setTerm(String term) {
        this.term = term;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getAdditionalInformation() {
        return additionalInformation;
    }
    
    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
        
}
