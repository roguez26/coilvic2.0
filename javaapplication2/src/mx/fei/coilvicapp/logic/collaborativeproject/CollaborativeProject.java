package mx.fei.coilvicapp.logic.collaborativeproject;

import mx.fei.coilvicapp.logic.course.Course;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;
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
        if (requesterCourse == null) {
            throw new IllegalArgumentException("No se pudo recuperar la información de curso solicitante");
        }
        this.requesterCourse = requesterCourse;
    }

    public Course getRequestedCourse() {
        return requestedCourse;
    }

    public void setRequestedCourse(Course requestedCourse) {
        if (requestedCourse == null) {
            throw new IllegalArgumentException("No se pudo recuperar la información de curso solicitado");
        }
        this.requestedCourse = requestedCourse;
    }

    public Modality getModality() {
        return modality;
    }

    public void setModality(Modality modality) {
        if (modality == null) {
            throw new IllegalArgumentException("Debe seleccionar una modalidad");
        }
        this.modality = modality;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkLongRange(description);
        this.description = description;
    }

    public String getGeneralObjective() {
        return generalObjective;
    }

    public void setGeneralObjective(String generalObjective) {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkLongRange(description);
        this.generalObjective = generalObjective;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkCode(code);
        this.code = code;
    }

    public String getSyllabusPath() {
        return syllabusPath;
    }

    public void setSyllabusPath(String syllabusPath) {
        if (syllabusPath == null) {
            throw new IllegalArgumentException("Es necesario que asigne un archivo de syllabus");
        }
        this.syllabusPath = syllabusPath;
    }

}
