package mx.fei.coilvicapp.logic.professor;

import java.util.Objects;

public class Professor {
    private int idProfessor = 0;
    private String name;
    private String paternalSurname;
    private String maternalSurname;
    private String email;
    private String gender;
    private String phoneNumber;
    private String state;
    private int idUniversity;
    
    public Professor() {
        
    }
    
    public Professor(String name, String paternalSurname, String maternalSurname,
            String email, String gender, String phoneNumber, int idUniversity) {
        this.name = name;
        this.paternalSurname = paternalSurname;
        this.maternalSurname = maternalSurname;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.idUniversity = idUniversity;
    }
    
    public int getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(int idProfessor) {
        this.idProfessor = idProfessor;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaternalSurname() {
        return paternalSurname;
    }

    public void setPaternalSurname(String paternalSurname) {
        this.paternalSurname = paternalSurname;
    }

    public String getMaternalSurname() {
        return maternalSurname;
    }

    public void setMaternalSurname(String maternalSurname) {
        this.maternalSurname = maternalSurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public int getIdUniversity() {
        return idUniversity;
    }

    public void setIdUniversity(int idUniversity) {
        this.idUniversity = idUniversity;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return idProfessor == professor.idProfessor &&
                Objects.equals(name, professor.name) &&
                Objects.equals(paternalSurname, professor.paternalSurname) &&
                Objects.equals(maternalSurname, professor.maternalSurname) &&
                Objects.equals(email, professor.email) &&
                Objects.equals(gender, professor.gender) &&
                Objects.equals(phoneNumber, professor.phoneNumber) &&
                Objects.equals(idUniversity, professor.idUniversity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProfessor, name, paternalSurname, maternalSurname, email, gender, phoneNumber, idUniversity);
    }

}
