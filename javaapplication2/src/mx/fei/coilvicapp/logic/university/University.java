package mx.fei.coilvicapp.logic.university;
import java.util.Objects;
/**
 *
 * @author ivanr
 */

public class University {
   
    private int idUniversity;
    private String name;
    private String acronym;
    private String jurisdiction;
    private String city;
    private int idCountry;
    
    public University() {
        
    }
    
    public int getIdUniversity() {
        return idUniversity;
    }

    public void setIdUniversity(int idUniversity) {
        this.idUniversity = idUniversity;
    }

    public String getName() {
        return name;
    }
    
    public String getAcronym() {
        return acronym;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(int idCountry) {
        this.idCountry = idCountry;
    }
    
    @Override
    public boolean equals(Object object) {
        
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        University toCompare = (University) object;
        return idUniversity == toCompare.idUniversity &&
                idCountry == toCompare.idCountry &&
                Objects.equals(name, toCompare.name) &&
                Objects.equals(jurisdiction, toCompare.jurisdiction) &&
                Objects.equals(city, toCompare.city);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idUniversity, name, jurisdiction, city, idCountry);
    }
    
    @Override
    public String toString() {
        return "University{" +
                "idUniversity=" + idUniversity +
                ", name='" + name + '\'' +
                ", jurisdiction='" + jurisdiction + '\'' +
                ", city='" + city + '\'' +
                ", idCountry=" + idCountry +
                '}';
    }

}
