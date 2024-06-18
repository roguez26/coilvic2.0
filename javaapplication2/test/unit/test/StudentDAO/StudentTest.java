package unit.test.StudentDAO;

import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.academicarea.AcademicAreaDAO;
import mx.fei.coilvicapp.logic.student.*;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.region.RegionDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.assertThrows;
import unit.test.Initializer.TestHelper;

public class StudentTest {

    private static Student TEST_STUDENT = new Student();
    private static Student AUX_TEST_STUDENT = new Student();
    private static final StudentDAO STUDENT_DAO = new StudentDAO();
    private final TestHelper testHelper = new TestHelper();

    @Before
    public void setUp() {
        testHelper.initializeStudent();
        TEST_STUDENT = testHelper.getStudentOne();
    }

    @After
    public void tearDown() {
        testHelper.deleteAll();
    }
    
    @Test 
    public void testSuccessCheckPreconditions() {
        boolean result = false;
        AcademicAreaDAO academicAreaDAO = new AcademicAreaDAO();
        RegionDAO regionDAO = new RegionDAO();        
        testHelper.initializeAcademicArea();
        testHelper.initializeRegion();
        
        try {
            result = STUDENT_DAO.checkPreconditions();
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        } finally {
            try {
                academicAreaDAO.deleteAcademicArea(testHelper.getAcademicArea().getIdAreaAcademica());
                regionDAO.deleteRegion(testHelper.getRegion().getIdRegion());
            } catch (DAOException exception) {
                Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
            }            
        }
        Assert.assertTrue(result);
    }
    
    @Test 
    public void testFailureCheckPreconditions() {
        boolean result = false;
        
        try {
            result = STUDENT_DAO.checkPreconditions();
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }        
        Assert.assertTrue(!result);      
    }    

    @Test
    public void testSuccessInsertStudent() {
        int idTestStudent = 0;
        initializeAuxStudent();

        try {
            idTestStudent = STUDENT_DAO.registerStudent(AUX_TEST_STUDENT);
            STUDENT_DAO.deleteStudentById(idTestStudent);
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(idTestStudent > 0);
    }

    @Test
    public void testFailureInsertStudentByEmailAlreadyRegistered() {
        initializeAuxStudent();
        AUX_TEST_STUDENT.setEmail(TEST_STUDENT.getEmail());
        assertThrows(DAOException.class, () -> {
            STUDENT_DAO.registerStudent(AUX_TEST_STUDENT);
        });
    }

    @Test
    public void testSuccessUpdateStudent() {
        int result = 0;
        
        initializeAuxStudent();
        AUX_TEST_STUDENT.setIdStudent(TEST_STUDENT.getIdStudent());

        try {
            result = STUDENT_DAO.updateStudent(AUX_TEST_STUDENT);
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(result > 0);
    }

    @Test
    public void testFailureUpdateStudentByAlreadyRegisteredEmail() {
        initializeAuxStudent();
        int idTestStudent = 0;

        try {
            idTestStudent = STUDENT_DAO.registerStudent(AUX_TEST_STUDENT);
            AUX_TEST_STUDENT.setEmail(TEST_STUDENT.getEmail());
            DAOException exception = assertThrows(DAOException.class, () -> {
                STUDENT_DAO.updateStudent(AUX_TEST_STUDENT);
            });
        } catch(DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        } finally {
            try {
                STUDENT_DAO.deleteStudentById(idTestStudent);
            } catch (DAOException exception) {
                Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
            }
        }
    }

    @Test
    public void testSuccessDeleteStudent() {
        int result = 0;

        try {
            result = STUDENT_DAO.deleteStudentById(TEST_STUDENT.getIdStudent());
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(result > 0);
    }

    @Test
    public void testFailureDeleteStudentByIdNotAvailable() {
        int result = 0;

        try {
            result = STUDENT_DAO.deleteStudentById(999);
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(result == 0);
    }

    @Test
    public void testSuccessGetStudentByEmail() {
        Student student = new Student();

        try {
            student = STUDENT_DAO.getStudentByEmail(TEST_STUDENT.getEmail());
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertEquals(TEST_STUDENT, student);
    }

    @Test
    public void testFailureGetStudentByEmailNotAvailable() {
        Student student = new Student();

        try {
            student = STUDENT_DAO.getStudentByEmail("test@example.com");
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertNotEquals(TEST_STUDENT, student);
    }

    @Test
    public void testSuccessGetStudentById() {
        Student student = new Student();

        try {
            student = STUDENT_DAO.getStudentById(TEST_STUDENT.getIdStudent());
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertEquals(TEST_STUDENT, student);
    }

    @Test
    public void testFailureGetStudentByIdNotAvailable() {
        Student student = new Student();

        try {
            student = STUDENT_DAO.getStudentById(999);
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertNotEquals(TEST_STUDENT, student);
    }

    @Test
    public void testSuccessGetAllStudents() {
        ArrayList<Student> expectedStudents = new ArrayList<>();
        ArrayList<Student> actualStudents = new ArrayList<>();

        expectedStudents = initializeStudentsArray();
        try {
            actualStudents = STUDENT_DAO.getAllStudents();  
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        } 
        Assert.assertEquals(expectedStudents, actualStudents);
    }

    @Test
    public void testFailureGetAllStudents() {
        ArrayList<Student> expectedStudents = new ArrayList<>();
        ArrayList<Student> actualStudents = new ArrayList<>();
        
        expectedStudents = initializeStudentsArray();
        try {
            STUDENT_DAO.deleteStudentById(TEST_STUDENT.getIdStudent());
            testHelper.deleteAll();
            actualStudents = STUDENT_DAO.getAllStudents();
        } catch (DAOException exception) {
            Log.getLogger(StudentTest.class).error(exception.getMessage(), exception);
        } 
        Assert.assertNotEquals(expectedStudents, actualStudents);
    }

    public ArrayList<Student> initializeStudentsArray() {
        ArrayList<Student> students = new ArrayList<>();
        students.add(testHelper.getStudentOne());
        students.add(testHelper.getStudentTwo());
        return students;
    }
    
    public void initializeAuxStudent() {
        AUX_TEST_STUDENT.setName("Axel");
        AUX_TEST_STUDENT.setPaternalSurname("Valdes");
        AUX_TEST_STUDENT.setMaternalSurname("Contreras");
        AUX_TEST_STUDENT.setEmail("axlvaldez74@gmail.com");
        AUX_TEST_STUDENT.setGender("Masculino");
        AUX_TEST_STUDENT.setLineage("Mexicano");
        AUX_TEST_STUDENT.setUniversity(testHelper.getUniversityTwo());   
    }    
        
}
