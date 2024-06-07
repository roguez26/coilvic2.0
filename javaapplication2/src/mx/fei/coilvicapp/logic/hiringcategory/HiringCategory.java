package mx.fei.coilvicapp.logic.hiringcategory;

import java.util.Objects;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;

public class HiringCategory {

    private int idHiringCategory = 0;
    private String name;

    public HiringCategory() {

    }

    public HiringCategory(String name) {
        this.name = name;
    }

    public int getIdHiringCategory() {
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
    public boolean equals(Object object) {
        boolean isEqual = false;
        if (this == object) {
            isEqual = true;
        } else if (object != null && getClass() == object.getClass()) {
            HiringCategory toCompare = (HiringCategory) object;
            isEqual = idHiringCategory == toCompare.idHiringCategory && Objects.equals(name, toCompare.name);
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idHiringCategory, name);
    }

    @Override
    public String toString() {
        return name;
    }

}
