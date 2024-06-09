package mx.fei.coilvicapp.logic.student;

import java.util.Objects;
import mx.fei.coilvicapp.logic.academicarea.AcademicArea;
import mx.fei.coilvicapp.logic.region.Region;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;

public class StudentUV extends Student {

    private String enrollment;
    private Student student;
    private AcademicArea academicArea;
    private Region region;

    public StudentUV() {
        student = new Student();
        academicArea = new AcademicArea();
        region = new Region();
    }

    public void setEnrollment(String enrollment) {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkEnrollment(enrollment);
        this.enrollment = enrollment;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public void setIdStudent(int idStudent) {
        this.student.setIdStudent(idStudent);
    }

    public int getIdStudent() {
        return student.getIdStudent();
    }

    public void setAcademicArea(AcademicArea academicArea) {
        if (academicArea == null) {
            throw new IllegalArgumentException("El campo del área académica no puede estar vacio");
        }
        this.academicArea = academicArea;
    }

    public AcademicArea getAcademicArea() {
        return academicArea;
    }

    public void setIdAcademicArea(int idAcademicArea) {
        this.academicArea.setIdAreaAcademica(idAcademicArea);
    }

    public int getIdAcademicArea() {
        return academicArea.getIdAreaAcademica();
    }

    public void setRegion(Region region) {
        if (academicArea == null) {
            throw new IllegalArgumentException("El campo de la región no puede estar vacio");
        }
        this.region = region;
    }

    public Region getRegion() {
        return region;
    }

    public void setIdRegion(int idRegion) {
        this.region.setIdRegion(idRegion);
    }

    public int getIdRegion() {
        return region.getIdRegion();
    }

    @Override
    public boolean equals(Object object) {
        boolean isEqual = false;

        if (this == object) {
            isEqual = true;
        } else if (object == null || getClass() != object.getClass()) {
            isEqual = false;
        } else if (!super.equals(object)) {
            isEqual = false;
        } else {
            StudentUV toCompare = (StudentUV) object;
            isEqual = Objects.equals(enrollment, toCompare.getEnrollment())
                    && Objects.equals(academicArea, toCompare.getAcademicArea())
                    && Objects.equals(region, toCompare.getRegion());
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), enrollment, academicArea, region);
    }

}
