package mx.fei.coilvicapp.logic.term;

import java.util.Objects;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;

/*
 * @author d0ubl3_d
 */
public class Term {

    private int idTerm = 0;
    private String name;

    public Term() {
    }

    public int getIdTerm() {
        return idTerm;
    }

    public void setIdTerm(int idTerm) {
        this.idTerm = idTerm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkTerm(name);
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        boolean isEqual = false;
        if (this == object) {
            isEqual = true;
        } else if (object != null && getClass() == object.getClass()) {
            Term toCompare = (Term) object;
            isEqual = idTerm == toCompare.idTerm && Objects.equals(name, toCompare.name);
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTerm, name);
    }

    @Override
    public String toString() {
        return name;
    }

}
