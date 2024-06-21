package mx.fei.coilvicapp.logic.course;

import java.util.Objects;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;
import mx.fei.coilvicapp.logic.language.Language;
import mx.fei.coilvicapp.logic.professor.*;
import mx.fei.coilvicapp.logic.term.Term;

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
        if (professor == null) {
            throw new IllegalArgumentException("No se encontro su información");
        }
        this.professor = professor;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        if (language == null) {
            throw new IllegalArgumentException("Debe seleccionar un idioma");
        }
        this.language = language;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        if (term == null) {
            throw new IllegalArgumentException("Debe seleccionar un periodo");
        }
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
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkName(name);
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
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkText(generalObjective);
        this.generalObjective = generalObjective;
    }

    public String getTopicsInterest() {
        return topicsInterest;
    }

    public void setTopicsInterest(String topicsInterest) {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkText(topicsInterest);
        this.topicsInterest = topicsInterest;
    }

    public int getNumberStudents() {
        return numberStudents;
    }

    public void setNumberStudents(int numberStudents) {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkNumbers(numberStudents);
        this.numberStudents = numberStudents;
    }

    public String getStudentsProfile() {
        return studentsProfile;
    }

    public void setStudentsProfile(String studentsProfile) {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkText(studentsProfile);
        this.studentsProfile = studentsProfile;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkText(additionalInformation);
        this.additionalInformation = additionalInformation;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        boolean isEqual = false;
        if (this == object) {
            isEqual = true;
        } else if (object != null && getClass() == object.getClass()) {
            Course course = (Course) object;
            isEqual = idCourse == course.getIdCourse()
                    && Objects.equals(name, course.getName())
                    //&& Objects.equals(status, course.getStatus())
                    && Objects.equals(generalObjective, course.getGeneralObjective())
                    && Objects.equals(topicsInterest, course.getTopicsInterest())
                    && Objects.equals(numberStudents, course.getNumberStudents())
                    && Objects.equals(studentsProfile, course.getStudentsProfile())
                    && Objects.equals(additionalInformation, course.getAdditionalInformation());
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.idCourse;
        hash = 41 * hash + Objects.hashCode(this.name);
        hash = 41 * hash + Objects.hashCode(this.status);
        hash = 41 * hash + Objects.hashCode(this.generalObjective);
        hash = 41 * hash + Objects.hashCode(this.topicsInterest);
        hash = 41 * hash + this.numberStudents;
        hash = 41 * hash + Objects.hashCode(this.studentsProfile);
        hash = 41 * hash + Objects.hashCode(this.additionalInformation);
        return hash;
    }
}
