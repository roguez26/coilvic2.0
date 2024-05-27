package unit.test.StudentDAO;

import log.Log;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.academicarea.AcademicArea;
import mx.fei.coilvicapp.logic.academicarea.AcademicAreaDAO;
import mx.fei.coilvicapp.logic.region.Region;
import mx.fei.coilvicapp.logic.region.RegionDAO;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;


        
import mx.fei.coilvicapp.logic.student.StudentUV;
import mx.fei.coilvicapp.logic.student.StudentDAO;
import mx.fei.coilvicapp.logic.university.University;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StudentUVTest {
    
    private static final StudentUV TEST_STUDENTUV = new StudentUV();
    private static final StudentUV AUX_TEST_STUDENTUV = new StudentUV();
    private static final StudentDAO STUDENT_DAO = new StudentDAO();       
    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final RegionDAO REGION_DAO = new RegionDAO();
    private static final AcademicAreaDAO ACADEMIC_AREA_DAO = new AcademicAreaDAO();
    
    
    @Before
    public void setUp() {
        initializeTestStudentUV();
        try {
            int idTestStudentUV = STUDENT_DAO.registerStudentUV(TEST_STUDENTUV);
            TEST_STUDENTUV.setIdStudent(idTestStudentUV);            
        } catch (DAOException exception) {
            Log.getLogger(StudentUVTest.class).error(exception.getMessage(), exception);
        }
    }
    
    @After 
    public void tearDown(){
        int idStudentUV = TEST_STUDENTUV.getIdStudent();
        int idUniversity = TEST_STUDENTUV.getIdUniversity();
        int idCountry = TEST_STUDENTUV.getUniversity().getIdCountry();
        int idAcademicArea = TEST_STUDENTUV.getIdAcademicArea();
        int idRegion = TEST_STUDENTUV.getIdRegion();
        
        try {
            STUDENT_DAO.deleteStudentUVById(idStudentUV);
            ACADEMIC_AREA_DAO.deleteAcademicArea(idAcademicArea);
            REGION_DAO.deleteRegion(idRegion);
            UNIVERSITY_DAO.deleteUniversity(idUniversity);
            COUNTRY_DAO.deleteCountry(idCountry);
        } catch (DAOException exception) {
            Log.getLogger(StudentUVTest.class).error(exception.getMessage(), exception);
        }                
    }
    
    @Test
    public void testSuccessRegisterStudentUV() {
        int idStudent = 0;
        initializeAuxTestStudent();
        try {          
            idStudent = STUDENT_DAO.registerStudentUV(AUX_TEST_STUDENTUV);
            STUDENT_DAO.deleteStudentUVById(idStudent);
        } catch (DAOException exception) {
            Log.getLogger(StudentUVTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(idStudent > 0);
    }        

    @Test
    public void testFailureRegisterStudentUVByEmailAlreadyRegistered() {
        int idStudent = 0;
        initializeAuxTestStudent();
        AUX_TEST_STUDENTUV.setEmail(TEST_STUDENTUV.getEmail());
        try {          
            idStudent = STUDENT_DAO.registerStudentUV(AUX_TEST_STUDENTUV);
            STUDENT_DAO.deleteStudentUVById(idStudent);
        } catch (DAOException exception) {
            Log.getLogger(StudentUVTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(idStudent > 0);
    }     
    
    @Test
    public void testFailureRegisterStudentUVByEnrollmentAlreadyRegistered() {
        int idStudent = 0;
        initializeAuxTestStudent();
        AUX_TEST_STUDENTUV.setEnrollment(TEST_STUDENTUV.getEnrollment());
        try {          
            idStudent = STUDENT_DAO.registerStudentUV(AUX_TEST_STUDENTUV);
            STUDENT_DAO.deleteStudentUVById(idStudent);
        } catch (DAOException exception) {
            Log.getLogger(StudentUVTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(idStudent > 0);
    } 

    @Test
    public void testFailureRegisterStudentUVByEnrollmentAndEmailAlreadyRegistered() {
        int idStudent = 0;
        initializeAuxTestStudent();
        AUX_TEST_STUDENTUV.setEnrollment(TEST_STUDENTUV.getEnrollment());
        AUX_TEST_STUDENTUV.setEmail(TEST_STUDENTUV.getEmail());
        try {          
            idStudent = STUDENT_DAO.registerStudentUV(AUX_TEST_STUDENTUV);
            STUDENT_DAO.deleteStudentUVById(idStudent);
        } catch (DAOException exception) {
            Log.getLogger(StudentUVTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(idStudent > 0);
    }     
    
    @Test
    public void testSuccessUpdateStudentUV() {
        int rowsAffected = 0;
        
        initializeAuxTestStudent();
        AUX_TEST_STUDENTUV.setIdStudent(TEST_STUDENTUV.getIdStudent());
        try {
            rowsAffected = STUDENT_DAO.updateStudent(AUX_TEST_STUDENTUV);
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(rowsAffected > 0);           
    }
    
    @Test
    public void testFailureUpdateStudentUVByAlreadyRegisteredEmail() {
        int rowsAffected = 0;
        
        initializeAuxTestStudent();
        try {
            AUX_TEST_STUDENTUV.setIdStudent(STUDENT_DAO.registerStudent(AUX_TEST_STUDENTUV));
            AUX_TEST_STUDENTUV.setEmail(TEST_STUDENTUV.getEmail());
            rowsAffected = STUDENT_DAO.updateStudentUV(AUX_TEST_STUDENTUV);            
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        } finally {
            try {
                STUDENT_DAO.deleteStudentById(AUX_TEST_STUDENTUV.getIdStudent());
            } catch (DAOException exception) {
                Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
            }
        }
        Assert.assertTrue(rowsAffected > 0);   
    }   
    
    @Test
    public void testFailureUpdateStudentUVByAlreadyRegisteredEnrollment() {
        int rowsAffected = 0;
        
        initializeAuxTestStudent();
        try {
            AUX_TEST_STUDENTUV.setIdStudent(STUDENT_DAO.registerStudent(AUX_TEST_STUDENTUV));
            AUX_TEST_STUDENTUV.setEnrollment(TEST_STUDENTUV.getEnrollment());
            rowsAffected = STUDENT_DAO.updateStudentUV(AUX_TEST_STUDENTUV);            
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        } finally {
            try {
                STUDENT_DAO.deleteStudentById(AUX_TEST_STUDENTUV.getIdStudent());
            } catch (DAOException exception) {
                Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
            }
        }
        Assert.assertTrue(rowsAffected > 0);   
    }     
        
    @Test 
    public void testSuccessDeleteStudentUVById() {
        int rowsAffected = 0;
        
        try {          
            rowsAffected = STUDENT_DAO.deleteStudentUVById(TEST_STUDENTUV.getIdStudent());
        } catch (DAOException exception) {
            Log.getLogger(StudentUVTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(rowsAffected > 0);        
    }
    
    @Test 
    public void testFailureDeleteStudentUVByIdNotAvailable() {
        int rowsAffected = 0;
        
        try {          
            rowsAffected = STUDENT_DAO.deleteStudentUVById(999);
        } catch (DAOException exception) {
            Log.getLogger(StudentUVTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(rowsAffected > 0);        
    }    
    
    @Test
    public void testSuccessGetStudentUVByEnrollment() {
        StudentUV studentUV = new StudentUV();

        try {
            studentUV = STUDENT_DAO.getStudentUVByEnrollment(TEST_STUDENTUV.getEnrollment());
        } catch (DAOException exception) {
            Log.getLogger(StudentUVTest.class).error(exception.getMessage(), exception);
        }

        Assert.assertEquals(TEST_STUDENTUV.getIdStudent(), studentUV.getIdStudent());
    }
    
    @Test
    public void testSuccessGetStudentUVByEnrollmentNotAvailable() {
        StudentUV studentUV = new StudentUV();

        try {
            studentUV = STUDENT_DAO.getStudentUVByEnrollment("s22016381");
        } catch (DAOException exception) {
            Log.getLogger(StudentUVTest.class).error(exception.getMessage(), exception);
        }

        Assert.assertEquals(TEST_STUDENTUV.getIdStudent(), studentUV.getIdStudent());
    }    

    
    private AcademicArea initializeAcademicArea() {
        AcademicArea academicArea = new AcademicArea();
        
        academicArea.setName("Tecnica");
        try {
            int idAcademicArea = ACADEMIC_AREA_DAO.registerAcademicArea(academicArea);
            academicArea.setIdAreaAcademica(idAcademicArea);
        } catch (DAOException exception) {
            Log.getLogger(StudentUVTest.class).error(exception.getMessage(), exception);
        }
        return academicArea;
    }
    
    private Region initializeRegion() {
        Region region = new Region();
        
        region.setName("Xalapa");
        try {
            int idRegion = REGION_DAO.registerRegion(region);
            region.setIdRegion(idRegion);
        } catch (DAOException exception) {
            Log.getLogger(StudentUVTest.class).error(exception.getMessage(), exception);
        }
        return region;
    }    
    
    private Country initializeCountry() {
        Country country = new Country();
        
        country.setName("Mexico");
        try {
            int idCountry = COUNTRY_DAO.registerCountry(country);
            country.setIdCountry(idCountry);
        } catch (DAOException exception) {
            Log.getLogger(StudentUVTest.class).error(exception.getMessage(), exception);
        }
        return country;
    }
    
    private University initializeUniversity() {
        University university = new University();
        
        university.setName("Universidad Veracruzana");
        university.setAcronym("UV");
        university.setJurisdiction("Veracruz");
        university.setCity("Xalapa");
        try {
            university.setCountry(initializeCountry());
            int idUniversity = UNIVERSITY_DAO.registerUniversity(university);
            university.setIdUniversity(idUniversity);
        } catch (DAOException exception) {
            Log.getLogger(StudentUVTest.class).error(exception.getMessage(), exception);
        }
        return university;
    }    
    
    private void initializeTestStudentUV() {
        TEST_STUDENTUV.setName("Axel");
        TEST_STUDENTUV.setPaternalSurname("Valdes");
        TEST_STUDENTUV.setMaternalSurname("Contreras");
        TEST_STUDENTUV.setEmail("axlvaldez74@gmail.com");
        TEST_STUDENTUV.setGender("Masculino");
        TEST_STUDENTUV.setLineage("Mexicano");
        TEST_STUDENTUV.setEnrollment("S22013627");
        TEST_STUDENTUV.setUniversity(initializeUniversity());
        TEST_STUDENTUV.setRegion(initializeRegion());
        TEST_STUDENTUV.setAcademicArea(initializeAcademicArea());
    }
    
    private void initializeAuxTestStudent() {
        AUX_TEST_STUDENTUV.setName("Ivan");
        AUX_TEST_STUDENTUV.setPaternalSurname("Rodriguez");
        AUX_TEST_STUDENTUV.setMaternalSurname("Franco");
        AUX_TEST_STUDENTUV.setEmail("roguez@gmail.com");
        AUX_TEST_STUDENTUV.setGender("Masculino");
        AUX_TEST_STUDENTUV.setLineage("Mexicano");
        AUX_TEST_STUDENTUV.setEnrollment("S22013671");
        AUX_TEST_STUDENTUV.setUniversity(TEST_STUDENTUV.getUniversity());
        AUX_TEST_STUDENTUV.setRegion(TEST_STUDENTUV.getRegion());
        AUX_TEST_STUDENTUV.setAcademicArea(TEST_STUDENTUV.getAcademicArea());
    }   
    
}
