package unit.test.ProfessorDAO;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.professor.ProfessorDAO;
import static org.junit.Assert.assertTrue;
import unit.test.Initializer.TestHelper;

public class ProfessorTest {

    private static Professor TEST_PROFESSOR = new Professor();
    private static final Professor AUX_TEST_PROFESSOR = new Professor();
    private static final ProfessorDAO PROFESSOR_DAO = new ProfessorDAO();
    private final TestHelper testHelper = new TestHelper();

    @Before
    public void setUp() {
        testHelper.initializeProfessors();
        TEST_PROFESSOR = testHelper.getProfessorOne();
    }

    @After
    public void tearDown() {
        testHelper.deleteAll();
    }

    @Test
    public void testSuccessCheckPreconditions() throws DAOException {
        testHelper.initializeAcademicArea();
        testHelper.initializeRegion();
        testHelper.initializeHiringCategory();
        testHelper.initializeHiringType();
        boolean result = PROFESSOR_DAO.checkPreconditions();
        Assert.assertTrue(result);
    }

    @Test
    public void testFailureCheckPreconditions() throws DAOException {
        boolean result = PROFESSOR_DAO.checkPreconditions();
        Assert.assertFalse(result);
    }

    @Test
    public void testSuccessRegisterProfessor() throws DAOException {
        initializeAuxTestProfessor();
        int idProfessor = PROFESSOR_DAO.registerProfessor(AUX_TEST_PROFESSOR);
        PROFESSOR_DAO.deleteProfessorByID(idProfessor);
        assertTrue(idProfessor > 0);
    }

    @Test(expected = DAOException.class)
    public void testFailureRegisterProfessorByEmailAlreadyRegistered() throws DAOException {
        initializeAuxTestProfessor();
        AUX_TEST_PROFESSOR.setEmail(TEST_PROFESSOR.getEmail());
        PROFESSOR_DAO.registerProfessor(AUX_TEST_PROFESSOR);
    }

    @Test
    public void testSuccessUpdateProfessor() throws DAOException {
        initializeAuxTestProfessor();
        AUX_TEST_PROFESSOR.setIdProfessor(TEST_PROFESSOR.getIdProfessor());
        int result = PROFESSOR_DAO.updateProfessor(AUX_TEST_PROFESSOR);
        assertTrue(result > 0);
    }

    @Test(expected = DAOException.class)
    public void testFailureUpdateProfessorByEmailAlreadyRegistered() throws DAOException {
        initializeAuxTestProfessor();
        AUX_TEST_PROFESSOR.setEmail(TEST_PROFESSOR.getEmail());
        int idProfessor = PROFESSOR_DAO.registerProfessor(AUX_TEST_PROFESSOR);
        AUX_TEST_PROFESSOR.setIdProfessor(idProfessor);

        try {
            PROFESSOR_DAO.updateProfessor(AUX_TEST_PROFESSOR);
        } finally {
            PROFESSOR_DAO.deleteProfessorByID(idProfessor);   
        }
    }

    @Test
    public void testSuccessDeleteProfessor() throws DAOException {
        int result = PROFESSOR_DAO.deleteProfessorByID(TEST_PROFESSOR.getIdProfessor());
        assertTrue(result > 0);
    }

    @Test
    public void testFailureDeleteProfessorByIdNotFound() throws DAOException {
        int result = PROFESSOR_DAO.deleteProfessorByID(999999);
        assertTrue(result == 0);
    }

    @Test
    public void testSuccessGetProfessorByID() throws DAOException {
        Professor professor = PROFESSOR_DAO.getProfessorById(TEST_PROFESSOR.getIdProfessor());
        Assert.assertEquals(professor.getIdProfessor(), TEST_PROFESSOR.getIdProfessor());
    }

    @Test
    public void testFailureGetProfessorByIDNotFound() throws DAOException {
        Professor professor = PROFESSOR_DAO.getProfessorById(999999);
        Assert.assertNotEquals(professor.getIdProfessor(), TEST_PROFESSOR.getIdProfessor());
    }

    @Test
    public void testSuccessGetProfessorByEmail() throws DAOException {
        Professor professor = PROFESSOR_DAO.getProfessorByEmail(TEST_PROFESSOR.getEmail());
        Assert.assertEquals(professor, TEST_PROFESSOR);
    }

    @Test
    public void testFailureGetProfessorByEmailNotFound() throws DAOException {
        String emailToSearch = "testEmail@gmail.com";
        Professor professor = PROFESSOR_DAO.getProfessorByEmail(emailToSearch);
        Assert.assertNotEquals(professor, TEST_PROFESSOR);
    }

    @Test
    public void testSuccessGetAllProfessors() throws DAOException {
        ArrayList<Professor> expectedProfessors = initializeProfessorsArray();
        ArrayList<Professor> actualProfessors = PROFESSOR_DAO.getAllProfessors();
        Assert.assertEquals(expectedProfessors, actualProfessors);
    }

    @Test
    public void testFailureGetAllProfessors() throws DAOException {
        testHelper.deleteAll();
        ArrayList<Professor> expectedProfessors = initializeProfessorsArray();
        ArrayList<Professor> actualProfessors = PROFESSOR_DAO.getAllProfessors();
        Assert.assertNotEquals(expectedProfessors, actualProfessors);
    }

    @Test
    public void testSuccessAcceptProfessor() throws DAOException {
        int result = PROFESSOR_DAO.acceptProfessor(TEST_PROFESSOR);
        assertTrue(result > 0);
    }

    @Test
    public void testFailureAcceptProfessor() throws DAOException {
        TEST_PROFESSOR.setIdProfessor(99999);
        int result = PROFESSOR_DAO.acceptProfessor(TEST_PROFESSOR);
        assertTrue(result == 0);
    }

    public ArrayList<Professor> initializeProfessorsArray() throws DAOException {
        ArrayList<Professor> professors = new ArrayList<>();
        professors.add(testHelper.getProfessorOne());
        professors.add(testHelper.getProfessorTwo());
        return professors;
    }

    private void initializeAuxTestProfessor() {
        AUX_TEST_PROFESSOR.setName("Juan Carlos");
        AUX_TEST_PROFESSOR.setPaternalSurname("Perez");
        AUX_TEST_PROFESSOR.setMaternalSurname("Arriaga");
        AUX_TEST_PROFESSOR.setEmail("revo123@uv.mx");
        AUX_TEST_PROFESSOR.setGender("Hombre");
        AUX_TEST_PROFESSOR.setPhoneNumber("1234567890");
        AUX_TEST_PROFESSOR.setUniversity(TEST_PROFESSOR.getUniversity());
    }

}
