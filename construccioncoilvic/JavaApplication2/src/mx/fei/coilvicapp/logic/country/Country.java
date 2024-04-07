package mx.fei.coilvicapp.logic.country;

import java.util.Objects;

public class Country {
    private int idCountry;
    private String name;
    
    public Country() {
        
    }
    
    public int getIdCountry() {
        return idCountry;
    }
    
    public String getName() {
        return name;
    }
    
    public void setIdCountry(int idCountry) {
        this.idCountry = idCountry;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(Object object) {
        Country toCompare = (Country) object;
        return idCountry == toCompare.idCountry && Objects.equals(name, toCompare.name);
    } 
    
    @Override
    public int hashCode() {
        return Objects.hash(idCountry, name);
    }
    
    @Override
    public String toString() {
        return "Country {" + 
                "idCountry=" + idCountry +
                ", name=" + name + '}';
    }
    
}
