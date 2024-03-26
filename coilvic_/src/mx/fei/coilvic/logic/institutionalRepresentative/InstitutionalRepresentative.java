/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.fei.coilvic.logic.institutionalRepresentative;

import java.util.Objects;

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
    private int university;
    
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
    return university;
  }

  public void setIdInstitutionalRepresentative(int idInstutionalRepresentative) {
    this.idInstitutionalRepresentative = idInstutionalRepresentative;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPaternalSurname(String paternalSurname) {
    this.paternalSurname = paternalSurname;
  }

  public void setMaternalSurname(String maternalSurname) {
    this.maternalSurname = maternalSurname;
  }

  public void setEmail(String eMail) {
    this.eMail = eMail;
  }
  
  public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
  }

  public void setIdUniversity(int university) {
    this.university = university;
  }
   @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstitutionalRepresentative toCompare = (InstitutionalRepresentative) o;
        return idInstitutionalRepresentative == toCompare.idInstitutionalRepresentative &&
                university == toCompare.university &&
                Objects.equals(name, toCompare.name) &&
                Objects.equals(paternalSurname, toCompare.paternalSurname) &&
                Objects.equals(maternalSurname, toCompare.maternalSurname) &&
                Objects.equals(eMail, toCompare.eMail) &&
                Objects.equals(phoneNumber, toCompare.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInstitutionalRepresentative, name, paternalSurname, maternalSurname, eMail, phoneNumber, university);
    }

    @Override
    public String toString() {
        return "InstitutionalRepresentative{" +
                "idInstitutionalRepresentative=" + idInstitutionalRepresentative +
                ", name='" + name + '\'' +
                ", paternalSurname='" + paternalSurname + '\'' +
                ", maternalSurname='" + maternalSurname + '\'' +
                ", eMail='" + eMail + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", university=" + university +
                '}';
    }
    
}
