package mx.fei.coilvicapp.logic.language;

import java.util.Objects;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;


/*
 * @author d0ubl3_d
 */
public class Language {

    private int idLanguage = 0;
    private String name;

    public Language() {
    }

    public int getIdLanguage() {
        return idLanguage;
    }

    public void setIdLanguage(int idLanguage) {
        this.idLanguage = idLanguage;
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
            Language toCompare = (Language) object;
            isEqual = idLanguage == toCompare.idLanguage && Objects.equals(name, toCompare.name);
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLanguage, name);
    }

    @Override
    public String toString() {
        return name;
    }
}
