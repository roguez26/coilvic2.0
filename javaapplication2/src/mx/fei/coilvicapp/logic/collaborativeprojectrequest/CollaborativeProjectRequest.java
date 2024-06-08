package mx.fei.coilvicapp.logic.collaborativeprojectrequest;

import java.util.Objects;
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

    public int getIdCollaborativeProjectRequest() {
        return idCollaborativeProjectRequest;
    }

    public Course getRequesterCourse() {
        return requesterCourse;
    }

    public void setRequesterCourse(Course requesterCourse) {
        if (requesterCourse == null) {
            throw new IllegalArgumentException("Debe sleecionar uno de sus cursos para enviar la solicitud");
        }
        if (requestedCourse != null && requesterCourse.getProfessor().getIdProfessor()
                == requestedCourse.getProfessor().getIdProfessor()) {
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
        if (requesterCourse != null && requestedCourse.getProfessor().getIdProfessor()
                == requesterCourse.getProfessor().getIdProfessor()) {
            throw new IllegalArgumentException("No puede enviar una solicitud a si mismo");
        }
        this.requestedCourse = requestedCourse;
    }

    public void setIdCollaborativeProjectRequest(int idCollaborativeProjectRequest) {
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

    @Override
    public boolean equals(Object object) {
        boolean isEqual = false;
        if (this == object) {
            isEqual = true;
        } else if (object != null && getClass() == object.getClass()) {
            CollaborativeProjectRequest request = (CollaborativeProjectRequest) object;
            isEqual = idCollaborativeProjectRequest == request.idCollaborativeProjectRequest
                    && Objects.equals(requesterCourse, request.requesterCourse)
                    && Objects.equals(requestedCourse, request.requestedCourse)
                    && Objects.equals(status, request.status)
                    && Objects.equals(requestDate, request.requestDate)
                    && Objects.equals(validationDate, request.validationDate);
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.idCollaborativeProjectRequest;
        hash = 23 * hash + Objects.hashCode(this.requesterCourse);
        hash = 23 * hash + Objects.hashCode(this.requestedCourse);
        hash = 23 * hash + Objects.hashCode(this.status);
        hash = 23 * hash + Objects.hashCode(this.requestDate);
        hash = 23 * hash + Objects.hashCode(this.validationDate);
        return hash;
    }
}
