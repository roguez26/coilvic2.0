package mx.fei.coilvicapp.logic.academicarea;

import java.util.Objects;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;

public class AcademicArea {
    
    private int idAreaAcademica = 0;
    private String name;
    
    public AcademicArea() {
        
    }
    
    public int getIdAreaAcademica() {
        return idAreaAcademica;
    }
    
    public void setIdAreaAcademica(int idAreaAcademica) {
        this.idAreaAcademica = idAreaAcademica;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkName(name);
        this.name = name;
    } 
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AcademicArea toCompare = (AcademicArea) obj;
        return idAreaAcademica == toCompare.idAreaAcademica && Objects.equals(name, toCompare.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idAreaAcademica, name);
    }
    
}
