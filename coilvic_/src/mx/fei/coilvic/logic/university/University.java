/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.fei.coilvic.logic.university;
import java.util.Objects;
/**
 *
 * @author ivanr
 */

public class University {
   
    private int idUniversity;
    private String name;
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

    public void setName(String name) {
        this.name = name;
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
