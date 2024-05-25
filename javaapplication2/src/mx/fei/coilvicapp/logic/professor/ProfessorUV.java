package mx.fei.coilvicapp.logic.professor;

import mx.fei.coilvicapp.logic.academicarea.AcademicArea;
import mx.fei.coilvicapp.logic.hiringcategory.HiringCategory;
import mx.fei.coilvicapp.logic.hiringtype.HiringType;
import mx.fei.coilvicapp.logic.region.Region;

public class ProfessorUV extends Professor {
    
    private int personalNumber;
    private HiringCategory hiringCategory;
    private HiringType hiringType;
    private AcademicArea academicArea;
    private Region region;    
    
    public ProfessorUV() {
        hiringCategory = new HiringCategory();
        hiringType = new HiringType();
        academicArea = new AcademicArea();
        region = new Region();
        
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
    
    public int getIdHiringCategory() {
        return this.hiringCategory.getIdHiringCategory();
    }
    
    public void setIdHiringCategory(int idHiringCategory) {
        this.hiringCategory.setIdHiringCategory(idHiringCategory);
    }    
    
    public HiringType getHiringType() {
        return hiringType;
    }
    
    public void setHiringType(HiringType hiringType) {
        this.hiringType = hiringType;
    }
    
    public int getIdHiringType() {
        return this.hiringType.getIdHiringType();
    }
    
    public void setIdHiringType(int idHiringType) {
        this.hiringType.setIdHiringType(idHiringType);
    }      
    
    public AcademicArea getAcademicArea() {
        return academicArea;
    }
    
    public void setAcademicArea(AcademicArea academicArea) {
        this.academicArea = academicArea;
    }
    
    public int getIdAcademicArea() {
        return this.academicArea.getIdAreaAcademica();
    }
    
    public void setIdAcademicArea(int idAcademicArea) {
        this.academicArea.setIdAreaAcademica(idAcademicArea);
    }
    
    public Region getRegion() {
        return region;
    }    
    
    public void setRegion(Region region) {
        this.region = region;
    }
    
    public int getIdRegion() {
        return this.region.getIdRegion();
    }
    
    public void setIdRegion(int idRegion) {
        this.region.setIdRegion(idRegion);
    }
    
    @Override
    public String toString() {
        return "ClassName{" +
                "personalNumber=" + personalNumber +
                ", hiringCategory=" + hiringCategory.getIdHiringCategory() +
                ", hiringType=" + hiringType.getIdHiringType() +
                ", academicArea=" + academicArea.getIdAreaAcademica() +
                ", region=" + region.getIdRegion() +
                '}';
    }

    
}
