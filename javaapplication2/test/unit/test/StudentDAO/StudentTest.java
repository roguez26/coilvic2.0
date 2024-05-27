package unit.test.StudentDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import log.Log;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.student.*;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StudentTest {
    
    private static final Student TEST_STUDENT = new Student();
    private static final Student AUX_TEST_STUDENT = new Student();
    private static final StudentDAO STUDENT_DAO = new StudentDAO();    
    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final CountryDAO COUNTRY_DAO = new CountryDAO();

    @Before
    public void setUp() {
        initializeTestStudent();
        try {
            int idTestStudent = STUDENT_DAO.registerStudent(TEST_STUDENT);
            TEST_STUDENT.setIdStudent(idTestStudent);            
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }
    }
    
    @After 
    public void tearDown(){
        int idStudent = TEST_STUDENT.getIdStudent();
        int idUniversity = TEST_STUDENT.getIdUniversity();
        int idCountry = TEST_STUDENT.getUniversity().getIdCountry();
        
        try {
            STUDENT_DAO.deleteStudentById(idStudent);            
            UNIVERSITY_DAO.deleteUniversity(idUniversity);
            COUNTRY_DAO.deleteCountry(idCountry);
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }                
    }
    
    @Test
    public void testSuccessRegisterStudent() {
        int idStudent = 0;
        initializeAuxTestStudent();
        try {          
            idStudent = STUDENT_DAO.registerStudent(AUX_TEST_STUDENT);
            STUDENT_DAO.deleteStudentById(idStudent);
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(idStudent > 0);
    }        

    @Test
    public void testFailureRegisterStudentByEmailAlreadyRegistered() {
        int idStudent = 0;
        initializeAuxTestStudent();
        AUX_TEST_STUDENT.setEmail(TEST_STUDENT.getEmail());
        try {          
            idStudent = STUDENT_DAO.registerStudent(AUX_TEST_STUDENT);
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(idStudent > 0);
    }     
    
    @Test
    public void testSuccessDeleteStudentById() {
        int rowsAffected = 0;
        
        try {          
            rowsAffected = STUDENT_DAO.deleteStudentById(TEST_STUDENT.getIdStudent());
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(rowsAffected > 0);        
    }
    
    @Test
    public void testSuccessUpdateStudent() {
        int rowsAffected = 0;
        
        initializeAuxTestStudent();
        AUX_TEST_STUDENT.setIdStudent(TEST_STUDENT.getIdStudent());
        try {
            rowsAffected = STUDENT_DAO.updateStudent(AUX_TEST_STUDENT);
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(rowsAffected > 0);   
    }
    
    @Test
    public void testFailureUpdateStudentByAlreadyRegisteredEmail() {
        int rowsAffected = 0;
        
        initializeAuxTestStudent();
        try {
            AUX_TEST_STUDENT.setIdStudent(STUDENT_DAO.registerStudent(AUX_TEST_STUDENT));
            AUX_TEST_STUDENT.setEmail(TEST_STUDENT.getEmail());
            rowsAffected = STUDENT_DAO.updateStudent(AUX_TEST_STUDENT);            
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        } finally {
            try {
                STUDENT_DAO.deleteStudentById(AUX_TEST_STUDENT.getIdStudent());
            } catch (DAOException exception) {
                Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
            }
        }
        Assert.assertTrue(rowsAffected > 0);   
    }    
    
    @Test 
    public void testFailureDeleteStudentByIdNotAvailable() {
        int rowsAffected = 0;
        
        try {          
            rowsAffected = STUDENT_DAO.deleteStudentById(999);
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(rowsAffected > 0);        
    }    
    
    @Test
    public void testSuccessGetStudentByEmail() {
        Student student = new Student();

        try {
            student = STUDENT_DAO.getStudentByEmail(TEST_STUDENT.getEmail());
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }

        Assert.assertEquals(TEST_STUDENT.getIdStudent(), student.getIdStudent());
    }
    
    @Test
    public void testSuccessGetStudentByEmailNotAvailable() {
        Student student = new Student();

        try {
            student = STUDENT_DAO.getStudentByEmail("test@example.com");
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }

        Assert.assertEquals(TEST_STUDENT.getIdStudent(), student.getIdStudent());
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

    private void initializeTestStudent() {
        TEST_STUDENT.setName("Axel");
        TEST_STUDENT.setPaternalSurname("Valdes");
        TEST_STUDENT.setMaternalSurname("Contreras");
        TEST_STUDENT.setEmail("axlvaldez74@gmail.com");
        TEST_STUDENT.setGender("Masculino");
        TEST_STUDENT.setLineage("Mexicano");
        TEST_STUDENT.setUniversity(initializeUniversity());
    }
    
    private void initializeAuxTestStudent() {
        AUX_TEST_STUDENT.setName("Ivan");
        AUX_TEST_STUDENT.setPaternalSurname("Rodriguez");
        AUX_TEST_STUDENT.setMaternalSurname("Franco");
        AUX_TEST_STUDENT.setEmail("roguez@gmail.com");
        AUX_TEST_STUDENT.setGender("Masculino");
        AUX_TEST_STUDENT.setLineage("Mexicano");
        AUX_TEST_STUDENT.setUniversity(TEST_STUDENT.getUniversity());
    }   
}
