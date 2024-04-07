package mx.fei.coilvicapp.logic.institutionalRepresentative;

import java.util.Objects;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;

/**
 *
 * @author ivanr
 */
public class InstitutionalRepresentative {

    private int idInstitutionalRepresentative;
    private String name;
    private String paternalSurname;
    private String maternalSurname;
    private String eMail;
    private String phoneNumber;
    private int idUniversity;

    public int getIdInstitutionalRepresentative() {
        return idInstitutionalRepresentative;
    }

    public String getName() {
        return name;
    }

    public String getPaternalSurname() {
        return paternalSurname;
    }

    public String getMaternalSurname() {
        return maternalSurname;
    }

    public String getEmail() {
        return eMail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getIdUniversity() {
        return idUniversity;
    }

    public void setIdInstitutionalRepresentative(int idInstutionalRepresentative) {
        this.idInstitutionalRepresentative = idInstutionalRepresentative;
    }

    public void setName(String name) {
        FieldValidator fieldValidator = new FieldValidator();

        fieldValidator.checkName(name);
        this.name = name;
    }

    public void setPaternalSurname(String paternalSurname) {
        FieldValidator fieldValidator = new FieldValidator();

        fieldValidator.checkName(paternalSurname);
        this.paternalSurname = paternalSurname;
    }

    public void setMaternalSurname(String maternalSurname) {
        FieldValidator fieldValidator = new FieldValidator();

        fieldValidator.checkName(maternalSurname);
        this.maternalSurname = maternalSurname;
    }

    public void setEmail(String eMail) {
        FieldValidator fieldValidator = new FieldValidator();

        fieldValidator.checkEmail(eMail);
        this.eMail = eMail;
    }

    public void setPhoneNumber(String phoneNumber) {
        FieldValidator fieldValidator = new FieldValidator();
        
        fieldValidator.checkPhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    public void setIdUniversity(int university) {
        this.idUniversity = university;
    }

    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (this == object) {
            result = true;
        }
        if (object == null || getClass() != object.getClass()) {
            result = false;
        }
        InstitutionalRepresentative toCompare = (InstitutionalRepresentative) object;
        return idInstitutionalRepresentative == toCompare.idInstitutionalRepresentative
                && idUniversity == toCompare.idUniversity
                && Objects.equals(name, toCompare.name)
                && Objects.equals(paternalSurname, toCompare.paternalSurname)
                && Objects.equals(maternalSurname, toCompare.maternalSurname)
                && Objects.equals(eMail, toCompare.eMail)
                && Objects.equals(phoneNumber, toCompare.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInstitutionalRepresentative, name, paternalSurname, maternalSurname, eMail, phoneNumber, idUniversity);
    }

    @Override
    public String toString() {
        return "InstitutionalRepresentative{"
                + "idInstitutionalRepresentative=" + idInstitutionalRepresentative
                + ", name='" + name + '\''
                + ", paternalSurname='" + paternalSurname + '\''
                + ", maternalSurname='" + maternalSurname + '\''
                + ", eMail='" + eMail + '\''
                + ", phoneNumber='" + phoneNumber + '\''
                + ", university=" + idUniversity
                + '}';
    }
}
