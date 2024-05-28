package mx.fei.coilvicapp.logic.professor;

import java.util.Objects;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;
import mx.fei.coilvicapp.logic.user.User;

public class Professor {
    private int idProfessor = 0;
    private String name;
    private String paternalSurname;
    private String maternalSurname;
    private String email;
    private String gender;
    private String phoneNumber;
    private String state;
    private University university;
    private User user;
    
    public Professor() {
        university = new University();
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
        FieldValidator fieldValidator = new FieldValidator();
        
        fieldValidator.checkName(name);
        this.name = name;
    }

    public String getPaternalSurname() {
        return paternalSurname;
    }

    public void setPaternalSurname(String paternalSurname) {
        FieldValidator fieldValidator = new FieldValidator();
        
        fieldValidator.checkName(paternalSurname);        
        this.paternalSurname = paternalSurname;
    }

    public String getMaternalSurname() {
        return maternalSurname;
    }

    public void setMaternalSurname(String maternalSurname) { 
        if(!maternalSurname.isEmpty()) {
            FieldValidator fieldValidator = new FieldValidator();        
            fieldValidator.checkName(paternalSurname);   
        }
        this.maternalSurname = maternalSurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        FieldValidator fieldValidator = new FieldValidator();
        
        fieldValidator.checkEmail(email);
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
        FieldValidator fieldValidator = new FieldValidator();
        
        fieldValidator.checkPhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public void setUniversity(University university) {
        this.university = university;
    }
    
    public University getUniversity() {
        return university;
    }
    
    public void setIdUniversity(int idUniversity) {
        this.university.setIdUniversity(idUniversity);
    }
    
    public int getIdUniversity() {
        return university.getIdUniversity();
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (this == object) {
            return true;
        }

        Professor toCompare = (Professor) object;
        return idProfessor == toCompare.getIdProfessor() &&
                Objects.equals(name, toCompare.getName()) &&
                Objects.equals(paternalSurname, toCompare.getPaternalSurname()) &&
                Objects.equals(maternalSurname, toCompare.getMaternalSurname()) &&
                Objects.equals(email, toCompare.getEmail()) &&
                Objects.equals(gender, toCompare.getGender()) &&
                Objects.equals(phoneNumber, toCompare.getPhoneNumber()) &&
                Objects.equals(state, toCompare.getState());
    }

    @Override
    public String toString() {
        return name +" "+ paternalSurname +" "+ maternalSurname;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idProfessor, name, paternalSurname, maternalSurname, email, gender, phoneNumber, state);
    }
    
}
