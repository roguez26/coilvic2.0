package unit.test.ProfessorDAO;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import log.Log;
import mx.fei.coilvicapp.logic.academicarea.AcademicAreaDAO;
import mx.fei.coilvicapp.logic.hiringcategory.HiringCategoryDAO;
import mx.fei.coilvicapp.logic.hiringtype.HiringTypeDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.professor.ProfessorDAO;
import mx.fei.coilvicapp.logic.region.RegionDAO;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import unit.test.Initializer.TestHelper;

public class ProfessorTest {
    
    private static Professor TEST_PROFESSOR = new Professor();
    private static Professor AUX_TEST_PROFESSOR = new Professor();
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
    public void testSuccessCheckPreconditions() {
        boolean result = false;
        AcademicAreaDAO academicAreaDAO = new AcademicAreaDAO();
        RegionDAO regionDAO = new RegionDAO();       
        HiringTypeDAO hiringType = new HiringTypeDAO();
        HiringCategoryDAO hiringCategory = new HiringCategoryDAO();
        testHelper.initializeAcademicArea();
        testHelper.initializeRegion();
        testHelper.initializeHiringCategory();
        testHelper.initializeHiringType();
        
        try {
            result = PROFESSOR_DAO.checkPreconditions();
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        } finally {
            try {
                academicAreaDAO.deleteAcademicArea(testHelper.getAcademicArea().getIdAreaAcademica());
                regionDAO.deleteRegion(testHelper.getRegion().getIdRegion());
                hiringType.deleteHiringType(testHelper.getHiringType().getIdHiringType());
                hiringCategory.deleteHiringCategory(testHelper.getHiringCategory().getIdHiringCategory());
            } catch (DAOException exception) {
                Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
            }            
        }
        Assert.assertTrue(result);
    }    
    
    @Test 
    public void testFailureCheckPreconditions() {
        boolean result = false;
        
        try {
            result = PROFESSOR_DAO.checkPreconditions();
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        } 
        Assert.assertTrue(!result);
    }      
    
    @Test
    public void testSuccessRegisterProfessor() {
        int idProfessor = 0;
        
        initializeAuxTestProfessor();
        try {
            idProfessor = PROFESSOR_DAO.registerProfessor(AUX_TEST_PROFESSOR);
            PROFESSOR_DAO.deleteProfessorByID(idProfessor);
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(idProfessor > 0);
    }
    
    @Test
    public void testFailureRegisterProfessorByEmailAlreadyRegistered() {
        initializeAuxTestProfessor();
        AUX_TEST_PROFESSOR.setEmail(TEST_PROFESSOR.getEmail());
        assertThrows(DAOException.class, () -> {
            PROFESSOR_DAO.registerProfessor(AUX_TEST_PROFESSOR);
        });
    }
    
    @Test
    public void testSuccesUpdateProfessor() {
        int result = 0;        
        
        initializeAuxTestProfessor();
        AUX_TEST_PROFESSOR.setIdProfessor(TEST_PROFESSOR.getIdProfessor());
        try {
            result = PROFESSOR_DAO.updateProfessor(AUX_TEST_PROFESSOR);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result > 0);
    }
    
    @Test
    public void testFailureUpdateProfessorByEmailAlreadyRegistered() {
        int result = 0;

        initializeAuxTestProfessor();
        AUX_TEST_PROFESSOR.setEmail(TEST_PROFESSOR.getEmail());
        try {
            result = PROFESSOR_DAO.registerProfessor(AUX_TEST_PROFESSOR);
            DAOException exception = assertThrows(DAOException.class, () -> {
                PROFESSOR_DAO.updateProfessor(AUX_TEST_PROFESSOR);
            });            
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        } finally {
            try {
                PROFESSOR_DAO.deleteProfessorByID(AUX_TEST_PROFESSOR.getIdProfessor());
            } catch (DAOException exception) {
                Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
            }
        }
    }
    
    @Test
    public void testSuccessDeleteProfessor() {
        int result = 0;
        
        try {
            result = PROFESSOR_DAO.deleteProfessorByID(TEST_PROFESSOR.getIdProfessor());
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result > 0);
    }  
    
    @Test
    public void testFailureDeleteProfessorByIdNotFound() {
        int result = 0;
        
        try {
            result = PROFESSOR_DAO.deleteProfessorByID(3);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result == 0);
    }  
    
    
    @Test
    public void testSuccessGetProfessorByID() {
        Professor professor = new Professor();
        try {
            professor = PROFESSOR_DAO.getProfessorById(TEST_PROFESSOR.getIdProfessor());            
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertEquals(professor.getIdProfessor(), TEST_PROFESSOR.getIdProfessor());       
    }
    
    @Test 
    public void testFailureGetProfessorByIDNotFound() {
        Professor professor = new Professor();
        try {
            professor = PROFESSOR_DAO.getProfessorById(999);            
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertNotEquals(professor.getIdProfessor(), TEST_PROFESSOR.getIdProfessor());         
    }
    
    @Test 
    public void testSuccessGetProfessorByEmail() {
        Professor professor = new Professor();
        try {
            professor = PROFESSOR_DAO.getProfessorByEmail(TEST_PROFESSOR.getEmail());            
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertEquals(professor.getEmail(), TEST_PROFESSOR.getEmail());
    }
    
    @Test 
    public void testFailureGetProfessorByEmailNotFound() {
        Professor professor = new Professor();
        String emailToSearch = "testEmail@gmail.com";
        try {
            professor = PROFESSOR_DAO.getProfessorByEmail(emailToSearch);            
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertNotEquals(professor.getIdProfessor(), TEST_PROFESSOR.getIdProfessor());       
    }  
    
    @Test
    public void testSuccessGetAllProfessors() {
        ArrayList<Professor> expectedProfessors = new ArrayList<>();
        ArrayList<Professor> actualProfessors = new ArrayList<>();
        
        expectedProfessors = initializeProfessorsArray();
        try {
            actualProfessors = PROFESSOR_DAO.getAllProfessors();            
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        } 
        Assert.assertEquals(expectedProfessors, actualProfessors);
    }
    
    @Test
    public void testFailureGetAllProfessors() {
        ArrayList<Professor> expectedProfessors = new ArrayList<>();
        ArrayList<Professor> actualProfessors = new ArrayList<>();
        
        expectedProfessors = initializeProfessorsArray();
        try {
            testHelper.deleteAll();
            actualProfessors = PROFESSOR_DAO.getAllProfessors();
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        } 
        Assert.assertNotEquals(expectedProfessors, actualProfessors);
    }    
    
    @Test
    public void testSuccessGetAllProfessorsByPendingStatus() {
        ArrayList<Professor> expectedProfessors = new ArrayList<>();
        ArrayList<Professor> actualProfessors = new ArrayList<>();
        
        expectedProfessors = initializeProfessorsArray();
        try {
            actualProfessors = PROFESSOR_DAO.getProfessorsByPendingStatus();
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        } 
        Assert.assertEquals(expectedProfessors, actualProfessors);
    }    
    
    @Test
    public void testFailureGetAllProfessorsByPendingStatus() {
        ArrayList<Professor> expectedProfessors = new ArrayList<>();
        ArrayList<Professor> actualProfessors = new ArrayList<>();
        
        try {
            actualProfessors = PROFESSOR_DAO.getProfessorsByPendingStatus();
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        } 
        Assert.assertNotEquals(expectedProfessors, actualProfessors);
    }       
    
    @Test
    public void testSuccessAcceptProfessor() {
        int result = 0;
        try {
            result = PROFESSOR_DAO.acceptProfessor(TEST_PROFESSOR);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        } 
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testFailureAcceptProfessor() {
        int result = 0;
        
        TEST_PROFESSOR.setIdProfessor(999);
        try {
            result = PROFESSOR_DAO.acceptProfessor(TEST_PROFESSOR);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        } 
        Assert.assertTrue(result == 0);
    }  
    
    @Test
    public void testSuccessRejectProfessor() {
        int result = 0;
        try {
            result = PROFESSOR_DAO.rejectProfessor(TEST_PROFESSOR);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        } 
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testFailureRejectProfessor() {
        int result = 0;
        
        TEST_PROFESSOR.setIdProfessor(999);
        try {
            result = PROFESSOR_DAO.rejectProfessor(TEST_PROFESSOR);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorTest.class).error(exception.getMessage(), exception);
        } 
        Assert.assertTrue(result == 0);
    }      
    
    public ArrayList<Professor> initializeProfessorsArray() {
        ArrayList<Professor> professors = new ArrayList<>();
        professors.add(testHelper.getProfessorOne());
        professors.add(testHelper.getProfessorTwo());
        return professors;
    }    
    
    private void initializeAuxTestProfessor() {        
        AUX_TEST_PROFESSOR.setName("Jorge");
        AUX_TEST_PROFESSOR.setPaternalSurname("Ocharan");
        AUX_TEST_PROFESSOR.setMaternalSurname("Hernandez");
        AUX_TEST_PROFESSOR.setEmail("jochar@uv.mx");
        AUX_TEST_PROFESSOR.setGender("Hombre");
        AUX_TEST_PROFESSOR.setPhoneNumber("1234567890");
        AUX_TEST_PROFESSOR.setUniversity(TEST_PROFESSOR.getUniversity()); 
    }

}
