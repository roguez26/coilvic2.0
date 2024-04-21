package mx.fei.coilvicapp.logic.assignment;

/*
 * @author d0ubl3_d
 */

public class Assignment {
    
    private int idColaborativeProject;
    
    private String name;
    private String description;
    private String date;
    private String path;
    
    public Assignment() {
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
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
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
