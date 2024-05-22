package mx.fei.coilvicapp.logic.course;

import java.util.Objects;
import mx.fei.coilvicapp.logic.language.Language;
import mx.fei.coilvicapp.logic.professor.*;
import mx.fei.coilvicapp.logic.term.Term;

/*
 * @author d0ubl3_d
 */
public class Course {

    private Professor professor;
    private Language language;
    private Term term;

    private int idCourse = 0;
    private String name;
    private String status;
    private String generalObjective;
    private String topicsInterest;
    private int numberStudents;
    private String studentsProfile;
    private String additionalInformation;

    public Course() {
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int idCourse) {
        this.idCourse = idCourse;
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

    public int getNumberStudents() {
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

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (this == object) {
            return true;
        }

        Course toCompare = (Course) object;
        return idCourse == toCompare.getIdCourse()
                && Objects.equals(name, toCompare.getName())
                && Objects.equals(status, toCompare.getStatus())
                && Objects.equals(generalObjective, toCompare.getGeneralObjective())
                && Objects.equals(topicsInterest, toCompare.getTopicsInterest())
                && Objects.equals(numberStudents, toCompare.getNumberStudents())
                && Objects.equals(studentsProfile, toCompare.getStudentsProfile())
                && Objects.equals(additionalInformation, toCompare.getAdditionalInformation());
    }

}
