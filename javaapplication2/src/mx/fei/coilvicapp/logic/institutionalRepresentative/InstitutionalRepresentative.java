package mx.fei.coilvicapp.logic.institutionalRepresentative;

import java.util.Objects;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;
import mx.fei.coilvicapp.logic.university.University;

/**
 *
 * @author ivanr
 */
public class InstitutionalRepresentative {

    private int idInstitutionalRepresentative = 0;
    private String name;
    private String paternalSurname;
    private String maternalSurname;
    private String eMail;
    private String phoneNumber;
    private University university;
    
    public InstitutionalRepresentative() {
        university = new University();
    }

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
        if (!maternalSurname.isEmpty()) {
            FieldValidator fieldValidator = new FieldValidator();
            fieldValidator.checkName(maternalSurname);
        }
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
    
    public University getUniversity() {
        return university;
    }
    
    public void setUniversity(University university) {
        this.university = university;
    }

    public void setIdUniversity(int idUniversity) {
        university.setIdUniversity(idUniversity);
    }
    
    public int getIdUniversity() {
        return university.getIdUniversity();
    }

    @Override
    public boolean equals(Object object) {
        boolean isEqual = false;
        
        if (this == object) {
            isEqual = true;
        } else if (object != null && getClass() == object.getClass()) {
            InstitutionalRepresentative toCompare = (InstitutionalRepresentative) object;
        isEqual = idInstitutionalRepresentative == toCompare.idInstitutionalRepresentative
                && Objects.equals(university, toCompare.university)
                && Objects.equals(name, toCompare.name)
                && Objects.equals(paternalSurname, toCompare.paternalSurname)
                && Objects.equals(maternalSurname, toCompare.maternalSurname)
                && Objects.equals(eMail, toCompare.eMail)
                && Objects.equals(phoneNumber, toCompare.phoneNumber);
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInstitutionalRepresentative, name, paternalSurname, maternalSurname, eMail, phoneNumber, university);
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
                + ", university=" + university
                + '}';
    }
}
