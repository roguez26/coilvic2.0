package mx.fei.coilvicapp.logic.assignment;

import java.util.Objects;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;

public class Assignment {

    private int idAssignment = 0;
    private String name;
    private String description;
    private String date;
    private String path;

    public Assignment() {
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
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkName(name);
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        FieldValidator fieldValidator = new FieldValidator();
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
    public boolean equals(Object object) {
        boolean isEqual = false;
        if (this == object) {
            isEqual = true;
        } else if (object != null && getClass() == object.getClass()) {
            Assignment assignment = (Assignment) object;
            isEqual = idAssignment == assignment.idAssignment
                    && Objects.equals(name, assignment.name)
                    && Objects.equals(description, assignment.description)
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
