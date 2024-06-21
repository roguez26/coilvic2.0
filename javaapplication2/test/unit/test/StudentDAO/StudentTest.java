package unit.test.StudentDAO;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.student.*;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import unit.test.Initializer.TestHelper;

public class StudentTest {

    private static Student TEST_STUDENT = new Student();
    private static Student AUX_TEST_STUDENT = new Student();
    private static final StudentDAO STUDENT_DAO = new StudentDAO();
    private final TestHelper testHelper = new TestHelper();

    @Before
    public void setUp() throws DAOException {
        testHelper.initializeStudent();
        TEST_STUDENT = testHelper.getStudentOne();
    }

    @After
    public void tearDown() throws DAOException {
        testHelper.deleteAll();
    }

    @Test
    public void testSuccessInsertStudent() throws DAOException {
        int idTestStudent;
        initializeAuxStudent();

        idTestStudent = STUDENT_DAO.registerStudent(AUX_TEST_STUDENT);
        STUDENT_DAO.deleteStudentById(idTestStudent);

        Assert.assertTrue(idTestStudent > 0);
    }

    @Test(expected = DAOException.class)
    public void testFailureInsertStudentByEmailAlreadyRegistered() throws DAOException {
        initializeAuxStudent();
        AUX_TEST_STUDENT.setEmail(TEST_STUDENT.getEmail());
        STUDENT_DAO.registerStudent(AUX_TEST_STUDENT);
    }

    @Test
    public void testSuccessUpdateStudent() throws DAOException {
        int result;
        initializeAuxStudent();
        AUX_TEST_STUDENT.setIdStudent(TEST_STUDENT.getIdStudent());

        result = STUDENT_DAO.updateStudent(AUX_TEST_STUDENT);
        Assert.assertTrue(result > 0);
    }

    @Test(expected = DAOException.class)
    public void testFailureUpdateStudentByAlreadyRegisteredEmail() throws DAOException {
        initializeAuxStudent();
        int idTestStudent = STUDENT_DAO.registerStudent(AUX_TEST_STUDENT);
        AUX_TEST_STUDENT.setEmail(TEST_STUDENT.getEmail());
        try {
            STUDENT_DAO.updateStudent(AUX_TEST_STUDENT);
        } finally {
            STUDENT_DAO.deleteStudentById(idTestStudent);
        }
    }

    @Test
    public void testSuccessDeleteStudent() throws DAOException {
        int result = STUDENT_DAO.deleteStudentById(TEST_STUDENT.getIdStudent());
        Assert.assertTrue(result > 0);
    }

    @Test
    public void testFailureDeleteStudentByIdNotAvailable() throws DAOException {
        int result = STUDENT_DAO.deleteStudentById(999);
        Assert.assertTrue(result == 0);
    }

    @Test
    public void testSuccessGetStudentByEmail() throws DAOException {
        Student student = STUDENT_DAO.getStudentByEmail(TEST_STUDENT.getEmail());
        Assert.assertEquals(TEST_STUDENT, student);
    }

    @Test
    public void testFailureGetStudentByEmailNotAvailable() throws DAOException {
        Student student = STUDENT_DAO.getStudentByEmail("test@example.com");
        Assert.assertNotEquals(TEST_STUDENT, student);
    }

    @Test
    public void testSuccessGetStudentById() throws DAOException {
        Student student = STUDENT_DAO.getStudentById(TEST_STUDENT.getIdStudent());
        Assert.assertEquals(TEST_STUDENT, student);
    }

    @Test
    public void testFailureGetStudentByIdNotAvailable() throws DAOException {
        Student student = STUDENT_DAO.getStudentById(999);
        Assert.assertNotEquals(TEST_STUDENT, student);
    }

    @Test
    public void testSuccessGetAllStudents() throws DAOException {
        ArrayList<Student> expectedStudents = initializeStudentsArray();
        ArrayList<Student> actualStudents = STUDENT_DAO.getAllStudents();  
        Assert.assertEquals(expectedStudents, actualStudents);
    }

    @Test
    public void testFailureGetAllStudents() throws DAOException {
        ArrayList<Student> expectedStudents = initializeStudentsArray();
        STUDENT_DAO.deleteStudentById(TEST_STUDENT.getIdStudent());
        testHelper.deleteAll();
        ArrayList<Student> actualStudents = STUDENT_DAO.getAllStudents();
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
