package mx.fei.coilvicapp.logic.hiringcategory;

import java.util.Objects;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;

public class HiringCategory {
    
    private int idHiringCategory = 0;
    private String name;
    
    public HiringCategory() {
        
    }
    
    public int getIdHiringCategory () {
        return idHiringCategory;
    }
    
    public void setIdHiringCategory(int idHiringCategory) {
        this.idHiringCategory = idHiringCategory;
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
        HiringCategory toCompare = (HiringCategory) obj;
        return idHiringCategory == toCompare.idHiringCategory && Objects.equals(name, toCompare.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idHiringCategory, name);
    }
    
}
