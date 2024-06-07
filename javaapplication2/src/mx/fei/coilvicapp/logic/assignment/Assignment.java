package mx.fei.coilvicapp.logic.assignment;

import java.util.Objects;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;

/*
 * @author d0ubl3_d
 */
public class Assignment {

    private int idColaborativeProject;

    private int idAssignment = 0;
    private String name;
    private String description;
    private String date;
    private String path;
    private final FieldValidator fieldValidator;

    public Assignment() {
        fieldValidator = new FieldValidator();
    }

    public int getIdColaborativeProject() {
        return idColaborativeProject;
    }

    public void setIdColaborativeProject(int idColaborativeProject) {
        this.idColaborativeProject = idColaborativeProject;
    }

    public int getIdAssignment() {
        return idAssignment;
    }

    public void setIdAssignment(int idAssignment) {
        this.idAssignment = idAssignment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        fieldValidator.checkShortRange(name);
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        fieldValidator.checkLongRange(description);
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (this == obj) {
            isEqual = true;
        } else if (obj != null && getClass() == obj.getClass()) {
            Assignment assignment = (Assignment) obj;
            isEqual = idAssignment == assignment.idAssignment
                    && Objects.equals(name, assignment.name)
                    && Objects.equals(description, assignment.description)
                    && Objects.equals(date, assignment.date)
                    && Objects.equals(path, assignment.path);
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.idAssignment;
        hash = 59 * hash + Objects.hashCode(this.name);
        hash = 59 * hash + Objects.hashCode(this.description);
        hash = 59 * hash + Objects.hashCode(this.date);
        hash = 59 * hash + Objects.hashCode(this.path);
        return hash;
    }

    @Override
    public String toString() {
        return name + " " + description + " " + date + " " + path + " " + idAssignment;
    }
}
