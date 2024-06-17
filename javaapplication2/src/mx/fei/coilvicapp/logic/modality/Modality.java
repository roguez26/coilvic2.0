package mx.fei.coilvicapp.logic.modality;

import java.util.Objects;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;

public class Modality {

    private int idModality = 0;
    private String name;

    public Modality() {
    }

    public int getIdModality() {
        return idModality;
    }

    public void setIdModality(int idModality) {
        this.idModality = idModality;
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
            Modality toCompare = (Modality) object;
            isEqual = idModality == toCompare.idModality && Objects.equals(name, toCompare.name);
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idModality, name);
    }

    @Override
    public String toString() {
        return name;
    }
}
