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
    public boolean equals(Object object) {
        boolean isEqual = false;
        if (this == object) {
            isEqual = true;
        } else if (object != null && getClass() == object.getClass()) {
            Region toCompare = (Region) object;
            isEqual = idRegion == toCompare.idRegion && Objects.equals(name, toCompare.name);
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRegion, name);
    }

    @Override
    public String toString() {
        return name;
    }

}
