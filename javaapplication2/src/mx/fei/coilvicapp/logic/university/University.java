package mx.fei.coilvicapp.logic.university;

import java.util.Objects;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;
import mx.fei.coilvicapp.logic.country.Country;
/**
 *
 * @author ivanr
 */
public class University {

    private int idUniversity = 0;
    private String name;
    private String acronym;
    private String jurisdiction;
    private String city;
    private Country country;

    public University() {
        country = new Country();
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
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkName(name);
        this.name = name;
    }

    public void setAcronym(String acronym) {
        if (acronym != null) {
            FieldValidator fieldValidator = new FieldValidator();
            fieldValidator.checkName(acronym);
        }
        this.acronym = acronym;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkName(jurisdiction);
        this.jurisdiction = jurisdiction;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkName(city);
        this.city = city;
    }
    
    public Country getCountry() {
        return country;
    }
    
    public void setCountry(Country country) {
        this.country = country;
    }

    public int getIdCountry() {
        return country.getIdCountry();
    }
    
    public void setIdCountry(int idCountry) {
        country.setIdCountry(idCountry);
    }

    @Override
    public boolean equals(Object object) {
        boolean isEqual = false;
        
        if (this == object) {
            isEqual = true;
        } else if (object != null && getClass() == object.getClass()) {
            University toCompare = (University) object;
            isEqual = idUniversity == toCompare.idUniversity
                && Objects.equals(country, toCompare.country)
                && Objects.equals(name, toCompare.name)
                && Objects.equals(jurisdiction, toCompare.jurisdiction)
                && Objects.equals(city, toCompare.city);
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUniversity, name, jurisdiction, city, country);
    }

    @Override
    public String toString() {
        return "University{"
                + "idUniversity=" + idUniversity
                + ", name='" + name + '\''
                + ", jurisdiction='" + jurisdiction + '\''
                + ", city='" + city + '\''
                + ", country =" + country
                + '}';
    }

}
