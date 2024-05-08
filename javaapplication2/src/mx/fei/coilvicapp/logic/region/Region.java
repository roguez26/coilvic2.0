package mx.fei.coilvicapp.logic.region;

import java.util.Objects;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;

public class Region {
    
    private int idRegion = 0;
    private String name;
    
    public Region() {
        
    } 
    
    public int getIdRegion() {
        return idRegion;
    }
    
    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
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
        Region toCompare = (Region) obj;
        return idRegion == toCompare.idRegion && Objects.equals(name, toCompare.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idRegion, name);
    }    
    
}
