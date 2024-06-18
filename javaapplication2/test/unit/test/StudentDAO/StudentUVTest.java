package unit.test.StudentDAO;

import log.Log;
import mx.fei.coilvicapp.logic.student.*;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.assertThrows;
import unit.test.Initializer.TestHelper;

public class StudentUVTest {
    
    private static StudentUV TEST_STUDENTUV = new StudentUV();
    private static StudentUV AUX_TEST_STUDENTUV = new StudentUV();
    private static final StudentDAO STUDENT_DAO = new StudentDAO();     
    private final TestHelper testHelper = new TestHelper();
    
    @Before
    public void setUp() {
        testHelper.initializeStudentUV();
        TEST_STUDENTUV = testHelper.getStudentUVOne();
    }
    
    @After
    public void tearDown() {
        testHelper.deleteAll();
    }
    
    @Test
    public void testSuccessInsertStudentUV() {
        int idTestStudent = 0;
        initializeAuxTestStudent();

        try {
            idTestStudent = STUDENT_DAO.registerStudentUV(AUX_TEST_STUDENTUV);
            STUDENT_DAO.deleteStudentUVById(idTestStudent);
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(idTestStudent > 0);
    }

    @Test
    public void testFailureInsertStudentUVByEmailAlreadyRegistered() {
        initializeAuxTestStudent();
        AUX_TEST_STUDENTUV.setEmail(TEST_STUDENTUV.getEmail());
        assertThrows(DAOException.class, () -> {
            STUDENT_DAO.registerStudentUV(AUX_TEST_STUDENTUV);
        });
    }    
    
    @Test
    public void testFailureRegisterStudentUVByEnrollmentAlreadyRegistered() {
        initializeAuxTestStudent();
        AUX_TEST_STUDENTUV.setEnrollment(TEST_STUDENTUV.getEnrollment());
        assertThrows(DAOException.class, () -> {
            STUDENT_DAO.registerStudentUV(AUX_TEST_STUDENTUV);
        });
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
        initializeAuxTestStudent();
        int idTestStudent = 0;

        try {
            idTestStudent = STUDENT_DAO.registerStudentUV(AUX_TEST_STUDENTUV);
            AUX_TEST_STUDENTUV.setEmail(TEST_STUDENTUV.getEmail());
            DAOException exception = assertThrows(DAOException.class, () -> {
                STUDENT_DAO.updateStudentUV(AUX_TEST_STUDENTUV);
            });
        } catch(DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        } finally {
            try {
                STUDENT_DAO.deleteStudentUVById(idTestStudent);
            } catch (DAOException exception) {
                Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
            }
        }
    }
    
    @Test
    public void testFailureUpdateStudentUVByAlreadyRegisteredEnrollment() {
        initializeAuxTestStudent();
        int idTestStudent = 0;

        try {
            idTestStudent = STUDENT_DAO.registerStudentUV(AUX_TEST_STUDENTUV);
            AUX_TEST_STUDENTUV.setEnrollment(TEST_STUDENTUV.getEnrollment());
            DAOException exception = assertThrows(DAOException.class, () -> {
                STUDENT_DAO.updateStudentUV(AUX_TEST_STUDENTUV);
            });
        } catch(DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        } finally {
            try {
                STUDENT_DAO.deleteStudentUVById(idTestStudent);
            } catch (DAOException exception) {
                Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
            }
        }
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
        Assert.assertTrue(rowsAffected == 0);        
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
            studentUV = STUDENT_DAO.getStudentUVByEnrollment("S99999999");
        } catch (DAOException exception) {
            Log.getLogger(StudentUVTest.class).error(exception.getMessage(), exception);
        }

        Assert.assertNotEquals(TEST_STUDENTUV.getIdStudent(), studentUV.getIdStudent());
    }   
    
    private void initializeAuxTestStudent() {
        AUX_TEST_STUDENTUV.setName("Axel");
        AUX_TEST_STUDENTUV.setPaternalSurname("Valdes");
        AUX_TEST_STUDENTUV.setMaternalSurname("Contreras");
        AUX_TEST_STUDENTUV.setEmail("axlvaldez74@gmail.com");
        AUX_TEST_STUDENTUV.setGender("Masculino");
        AUX_TEST_STUDENTUV.setLineage("Mexicano");
        AUX_TEST_STUDENTUV.setEnrollment("S22013627");
        AUX_TEST_STUDENTUV.setUniversity(testHelper.getUniversityOne());
        AUX_TEST_STUDENTUV.setRegion(testHelper.getRegion());
        AUX_TEST_STUDENTUV.setAcademicArea(testHelper.getAcademicArea());
    }
    
}
