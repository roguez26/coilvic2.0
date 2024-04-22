package mx.fei.coilvicapp.logic.country;

import java.util.Objects;

public class Country {

    private int idCountry = 0;
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
        boolean isEqual = false;
        
        if (this == object) {
            isEqual = true;
        } else if (object != null && getClass() == object.getClass()) {
            Country toCompare = (Country) object;
            isEqual = idCountry == toCompare.idCountry && Objects.equals(name, toCompare.name);
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCountry, name);
    }

    @Override
    public String toString() {
        return name;
    }

}
