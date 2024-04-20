package mx.fei.coilvicapp.logic.professor;

import mx.fei.coilvicapp.logic.academicarea.AcademicArea;
import mx.fei.coilvicapp.logic.hiringcategory.HiringCategory;
import mx.fei.coilvicapp.logic.hiringtype.HiringType;
import mx.fei.coilvicapp.logic.region.Region;
import mx.fei.coilvicapp.logic.university.University;

public class ProfessorUV extends Professor {
    
    private int personalNumber;
    private HiringCategory hiringCategory;
    private HiringType hiringType;
    private AcademicArea academicArea;
    private Region region;    
    
    public ProfessorUV(Professor professor, int idUniversity) {
        super(professor.getName(), professor.getPaternalSurname(), professor.getMaternalSurname(),
                professor.getEmail(), professor.getGender(), professor.getPhoneNumber(), idUniversity);
    }
    
    public int getPersonalNumber() {
        return personalNumber;
    }
    
    public void setPersonalNumber(int personalNumber) {
        this.personalNumber = personalNumber;
    }
    
    public HiringCategory getHiringCategory() {
        return hiringCategory;
    }
    
    public void setHiringCategory(HiringCategory hiringCategory) {
        this.hiringCategory = hiringCategory;
    }
    
    public HiringType getHiringType() {
        return hiringType;
    }
    
    public void setHiringType(HiringType hiringType) {
        this.hiringType = hiringType;
    }
    
    public AcademicArea getAcademicArea() {
        return academicArea;
    }
    
    public void setAcademicArea(AcademicArea academicArea) {
        this.academicArea = academicArea;
    }
    
    public Region getRegion() {
        return region;
    }    
    
    public void setRegion(Region region) {
        this.region = region;
    }
    
}
