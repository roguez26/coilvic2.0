package unit.test.StudentDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import mx.fei.coilvicapp.logic.student.Student;
import mx.fei.coilvicapp.logic.student.StudentDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.university.UniversityDAO;

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
            Logger.getLogger(StudentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
    
    @After
    public void tearDown() {
        int idStudent = TEST_STUDENT.getIdStudent();
        int idUniversity = TEST_STUDENT.getIdUniversity();
        int idCountry = TEST_STUDENT.getUniversity().getIdCountry();
        
        try {
            STUDENT_DAO.deleteStudentById(idStudent);
            UNIVERSITY_DAO.deleteUniversity(idUniversity);
            COUNTRY_DAO.deleteCountry(idCountry);
        } catch (DAOException exception) {
            Logger.getLogger(StudentTest.class.getName()).log(Level.SEVERE, null, exception);
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
            Logger.getLogger(StudentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(idStudent > 0);
    }
    
    @Test
    public void testFailureRegisterStudentByEmailAlreadyRegistered() {
        int idStudent = 0;
        
        try {
            idStudent = STUDENT_DAO.registerStudent(TEST_STUDENT);
        } catch (DAOException exception) {
            Logger.getLogger(StudentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(idStudent > 0);
    }
    
    @Test
    public void testSuccesUpdateStudent() {
        int result = 0;        
        
        initializeAuxTestStudent();
        AUX_TEST_STUDENT.setIdStudent(TEST_STUDENT.getIdStudent());
        try {
            result = STUDENT_DAO.updateStudent(AUX_TEST_STUDENT);
        } catch (DAOException exception) {
            Logger.getLogger(StudentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testFailureUpdateStudentByEmailAlreadyRegistered() {
        int result = 0;
        
        initializeAuxTestStudent();
        AUX_TEST_STUDENT.setIdStudent(TEST_STUDENT.getIdStudent());
        AUX_TEST_STUDENT.setEmail(TEST_STUDENT.getEmail());
        try {
            result = STUDENT_DAO.updateStudent(AUX_TEST_STUDENT);
        } catch (DAOException exception) {
            Logger.getLogger(StudentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testSuccessDeleteStudent() {
        int result = 0;
        
        try {
            result = STUDENT_DAO.deleteStudentById(TEST_STUDENT.getIdStudent());
        } catch (DAOException exception) {
            Logger.getLogger(StudentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testFailureDeleteStudentByIdNotFound() {
        int result = 0;
        
        try {
            result = STUDENT_DAO.deleteStudentById(999);
        } catch (DAOException exception) {
            Logger.getLogger(StudentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testSuccessGetStudentById() {
        Student student = new Student();
        try {
            student = STUDENT_DAO.getStudentById(TEST_STUDENT.getIdStudent());
        } catch (DAOException exception) {
            Logger.getLogger(StudentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertEquals(student.getIdStudent(), TEST_STUDENT.getIdStudent());
    }
    
    @Test
    public void testFailureGetStudentByIdNotFound() {
        Student student = new Student();
        try {
            student = STUDENT_DAO.getStudentById(1);
        } catch (DAOException exception) {
            Logger.getLogger(StudentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertEquals(student.getIdStudent(), TEST_STUDENT.getIdStudent());
    }
    
    @Test
    public void testSuccessGetStudentByEmail() {
        Student student = new Student();
        try {
            student = STUDENT_DAO.getStudentByEmail(TEST_STUDENT.getEmail());
        } catch (DAOException exception) {
            Logger.getLogger(StudentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertEquals(student.getEmail(), TEST_STUDENT.getEmail());
    }
    
    @Test
    public void testFailureGetStudentByEmailNotFound() {
        Student student = new Student();
        String emailToSearch = "testEmail@gmail.com";
        try {
            student = STUDENT_DAO.getStudentByEmail(emailToSearch);
        } catch (DAOException exception) {
            Logger.getLogger(StudentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertEquals(student.getIdStudent(), TEST_STUDENT.getIdStudent());
    }
    
    private Country initializeCountry() {
        Country country = new Country();
        
        country.setName("Mexico");
        try {
            int idCountry = COUNTRY_DAO.registerCountry(country);
            country.setIdCountry(idCountry);
        } catch (DAOException exception) {
            Logger.getLogger(StudentTest.class.getName()).log(Level.SEVERE, null, exception);
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
            Logger.getLogger(StudentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        return university;
    }    
    
    private void initializeTestStudent() {
        TEST_STUDENT.setName("Francisco");
        TEST_STUDENT.setPaternalSurname("Perez");
        TEST_STUDENT.setMaternalSurname("Gonzalez");
        TEST_STUDENT.setEmail("franciscoperez@gmail.com");
        TEST_STUDENT.setGender("Masculino");
        TEST_STUDENT.setLineage("Mexicano");
        TEST_STUDENT.setUniversity(initializeUniversity());
    }
    
    private void initializeAuxTestStudent() {
        AUX_TEST_STUDENT.setName("Jose");
        AUX_TEST_STUDENT.setPaternalSurname("Jimenez");
        AUX_TEST_STUDENT.setMaternalSurname("Hernandez");
        AUX_TEST_STUDENT.setEmail("josehdz@gmail.com");
        AUX_TEST_STUDENT.setGender("Masculino");
        AUX_TEST_STUDENT.setLineage("Mexicano");
        AUX_TEST_STUDENT.setUniversity(TEST_STUDENT.getUniversity());
    }
}
