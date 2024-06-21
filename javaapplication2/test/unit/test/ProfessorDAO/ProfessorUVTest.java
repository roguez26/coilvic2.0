package unit.test.ProfessorDAO;

import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.professor.ProfessorDAO;
import mx.fei.coilvicapp.logic.professor.ProfessorUV;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import unit.test.Initializer.TestHelper;

public class ProfessorUVTest {
    
    private static ProfessorUV TEST_PROFESSOR = new ProfessorUV();
    private static final ProfessorUV AUX_TEST_PROFESSOR = new ProfessorUV();
    private static final ProfessorDAO PROFESSOR_DAO = new ProfessorDAO();
    private final TestHelper testHelper = new TestHelper();
        
    @Before
    public void setUp() {
        testHelper.initializeProfessorsUV();
        TEST_PROFESSOR = testHelper.getProfessorUVOne();
    }
    
    @After
    public void tearDown() {
        testHelper.deleteAll();
    }    
    
    @Test
    public void testSuccessRegisterProfessorUV() throws DAOException {
        initializeAuxTestProfessorUV();
        int idProfessorUV = PROFESSOR_DAO.registerProfessorUV(AUX_TEST_PROFESSOR);
        PROFESSOR_DAO.deleteProfessorUVByID(idProfessorUV);
        assertTrue(idProfessorUV > 0);
    }
    
    @Test(expected = DAOException.class)
    public void testFailureRegisterProfessorUVByEmailAlreadyRegistered() throws DAOException {
        initializeAuxTestProfessorUV();
        AUX_TEST_PROFESSOR.setEmail(TEST_PROFESSOR.getEmail());
        PROFESSOR_DAO.registerProfessorUV(AUX_TEST_PROFESSOR);
    }  
    
    @Test(expected = DAOException.class)
    public void testFailureRegisterProfessorUVByPersonalNumberAlreadyRegistered() throws DAOException {
        initializeAuxTestProfessorUV();
        AUX_TEST_PROFESSOR.setPersonalNumber(TEST_PROFESSOR.getPersonalNumber());
        PROFESSOR_DAO.registerProfessorUV(AUX_TEST_PROFESSOR);
    }      
    
    @Test
    public void testSuccessUpdateProfessorUV() throws DAOException {        
        initializeAuxTestProfessorUV();
        AUX_TEST_PROFESSOR.setIdProfessor(TEST_PROFESSOR.getIdProfessor());
        int result = PROFESSOR_DAO.updateProfessorUV(AUX_TEST_PROFESSOR);
        assertTrue(result > 0);
    }    
    
    @Test(expected = DAOException.class)
    public void testFailureUpdateProfessorByAlreadyRegisteredEmail() throws DAOException {
        initializeAuxTestProfessorUV();
        AUX_TEST_PROFESSOR.setEmail(TEST_PROFESSOR.getEmail());
        int idProfessor = PROFESSOR_DAO.registerProfessor(AUX_TEST_PROFESSOR);
        AUX_TEST_PROFESSOR.setIdProfessor(idProfessor);        
        try {
            PROFESSOR_DAO.updateProfessorUV(AUX_TEST_PROFESSOR);
        } finally {
            PROFESSOR_DAO.deleteProfessorByID(idProfessor);  
        }
    }     
    
    @Test
    public void testSuccessDeleteProfessorUV() throws DAOException {
        int result = PROFESSOR_DAO.deleteProfessorUVByID(TEST_PROFESSOR.getIdProfessor());
        assertTrue(result > 0);
    } 
    
    @Test
    public void testFailureDeleteProfessorUVByIdNotFound() throws DAOException {
        int result = PROFESSOR_DAO.deleteProfessorUVByID(999);
        assertTrue(result == 0);
    }     
    
    @Test
    public void testSuccessGetProfessorUVByPersonalNumber() throws DAOException {
        ProfessorUV professorUV = PROFESSOR_DAO.getProfessorUVByPersonalNumber(TEST_PROFESSOR.getPersonalNumber());
        Assert.assertEquals(professorUV.getIdProfessor(), TEST_PROFESSOR.getIdProfessor());
    }  
    
    @Test
    public void testFailureGetProfessorUVByPersonalNumber() throws DAOException {
        ProfessorUV professorUV = PROFESSOR_DAO.getProfessorUVByPersonalNumber(999);
        Assert.assertNotEquals(professorUV.getIdProfessor(), TEST_PROFESSOR.getIdProfessor());
    }      
    
    private void initializeAuxTestProfessorUV() {        
        AUX_TEST_PROFESSOR.setName("Jorge");
        AUX_TEST_PROFESSOR.setPaternalSurname("Ocharan");
        AUX_TEST_PROFESSOR.setMaternalSurname("Hernandez");
        AUX_TEST_PROFESSOR.setEmail("jocha@uv.mx");
        AUX_TEST_PROFESSOR.setGender("Hombre");
        AUX_TEST_PROFESSOR.setState("Pendiente");
        AUX_TEST_PROFESSOR.setPhoneNumber("1234567890");
        AUX_TEST_PROFESSOR.setUniversity(TEST_PROFESSOR.getUniversity());
        AUX_TEST_PROFESSOR.setAcademicArea(TEST_PROFESSOR.getAcademicArea());
        AUX_TEST_PROFESSOR.setHiringCategory(TEST_PROFESSOR.getHiringCategory());
        AUX_TEST_PROFESSOR.setHiringType(TEST_PROFESSOR.getHiringType());
        AUX_TEST_PROFESSOR.setRegion(TEST_PROFESSOR.getRegion());
        AUX_TEST_PROFESSOR.setPersonalNumber(11000);
    }
}
