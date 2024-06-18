package unit.test.ProfessorDAO;

import log.Log;
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
    private static ProfessorUV AUX_TEST_PROFESSOR = new ProfessorUV();
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
    public void testSuccessRegisterProfessorUV() {
        int idProfessorUV = 0;
        initializeAuxTestProfessorUV();
        try {          
            idProfessorUV = PROFESSOR_DAO.registerProfessorUV(AUX_TEST_PROFESSOR);
            PROFESSOR_DAO.deleteProfessorUVByID(idProfessorUV);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorUVTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(idProfessorUV > 0);
    }
    
    @Test
    public void testFailureRegisterProfessorUVByEmailAlreadyRegistered() {
        int idProfessorUV = 0;
        initializeAuxTestProfessorUV();
        AUX_TEST_PROFESSOR.setEmail(TEST_PROFESSOR.getEmail());
        try {          
            idProfessorUV = PROFESSOR_DAO.registerProfessorUV(AUX_TEST_PROFESSOR);
            PROFESSOR_DAO.deleteProfessorUVByID(idProfessorUV);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorUVTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(idProfessorUV <= 0);
    }  
    
    @Test
    public void testFailureRegisterProfessorUVByPersonalNumberAlreadyRegistered() {
        int idProfessorUV = 0;
        initializeAuxTestProfessorUV();
        AUX_TEST_PROFESSOR.setPersonalNumber(TEST_PROFESSOR.getPersonalNumber());
        try {          
            idProfessorUV = PROFESSOR_DAO.registerProfessorUV(AUX_TEST_PROFESSOR);
            PROFESSOR_DAO.deleteProfessorUVByID(idProfessorUV);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorUVTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(idProfessorUV <= 0);
    }      
    
    @Test
    public void testSuccesUpdateProfessor() {
        int result = 0;        
        
        initializeAuxTestProfessorUV();
        AUX_TEST_PROFESSOR.setIdProfessor(TEST_PROFESSOR.getIdProfessor());
        try {
            result = PROFESSOR_DAO.updateProfessorUV(AUX_TEST_PROFESSOR);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result > 0);
    }    
    
    @Test
    public void testFailureUpdateProfessorByAlreadyRegisteredEmail() {
        int result = 0;        
        
        initializeAuxTestProfessorUV();
        AUX_TEST_PROFESSOR.setIdProfessor(TEST_PROFESSOR.getIdProfessor());
        AUX_TEST_PROFESSOR.setEmail(TEST_PROFESSOR.getEmail());
        try {
            result = PROFESSOR_DAO.updateProfessorUV(AUX_TEST_PROFESSOR);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result > 0);
    }     
    
    @Test
    public void testSuccessDeleteProfessorUV() {
        int result = 0;
        
        try {
            result = PROFESSOR_DAO.deleteProfessorUVByID(TEST_PROFESSOR.getIdProfessor());
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result > 0);
    } 
    
    @Test
    public void testFailureDeleteProfessorUVByIdNotFound() {
        int result = 0;
        
        try {
            result = PROFESSOR_DAO.deleteProfessorUVByID(999);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result <= 0);
    }     
    
    @Test
    public void testSuccessGetProfessorUVByPersonalNumber() {
        ProfessorUV professorUV = new ProfessorUV();
        try {
            professorUV = PROFESSOR_DAO.getProfessorUVByPersonalNumber(
                    TEST_PROFESSOR.getPersonalNumber());            
            System.out.println("1 "+professorUV);
            System.out.println("2 "+TEST_PROFESSOR);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertEquals(professorUV.getIdProfessor(), TEST_PROFESSOR.getIdProfessor());       
    }  
    
    @Test
    public void testFailureGetProfessorUVByPersonalNumber() {
        ProfessorUV professorUV = new ProfessorUV();
        try {
            professorUV = PROFESSOR_DAO.getProfessorUVByPersonalNumber(999);            
            System.out.println("1 "+professorUV);
            System.out.println("2 "+TEST_PROFESSOR);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        }
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
