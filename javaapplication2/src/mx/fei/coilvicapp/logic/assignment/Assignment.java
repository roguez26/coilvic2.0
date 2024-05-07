package mx.fei.coilvicapp.logic.assignment;

import mx.fei.coilvicapp.logic.implementations.FieldValidator;

/*
 * @author d0ubl3_d
 */

public class Assignment {
    
    private int idColaborativeProject;
    
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
    
    public void setIdColaborativeProject (int idColaborativeProject) {
        this.idColaborativeProject = idColaborativeProject;
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
}
