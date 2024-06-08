package mx.fei.coilvicapp.logic.hiringtype;

import java.util.Objects;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;

public class HiringType {

    private int idHiringType = 0;
    private String name;

    public HiringType() {

    }

    public HiringType(String name) {
        this.name = name;
    }

    public int getIdHiringType() {
        return idHiringType;
    }

    public void setIdHiringType(int idHiringType) {
        this.idHiringType = idHiringType;
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
        boolean isEqual = false;
        if (this == obj) {
            isEqual = true;
        } else if (obj != null && getClass() == obj.getClass()) {
            HiringType toCompare = (HiringType) obj;
            isEqual = idHiringType == toCompare.idHiringType && Objects.equals(name, toCompare.name);
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idHiringType, name);
    }

    @Override
    public String toString() {
        return name;
    }

}
