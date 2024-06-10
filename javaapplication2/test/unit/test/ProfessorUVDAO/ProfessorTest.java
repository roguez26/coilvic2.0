package unit.test.ProfessorUVDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.professor.ProfessorDAO;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.country.Country;
import static org.junit.Assert.assertTrue;

public class ProfessorTest {
    
    private static final Professor TEST_PROFESSOR = new Professor();
    private static final Professor AUX_TEST_PROFESSOR = new Professor();
    private static final ProfessorDAO PROFESSOR_DAO = new ProfessorDAO();
    private static final ArrayList<Professor> TEST_PROFESSORS = new ArrayList<>();
    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    
    
    @Before
    public void setUp() {
        try {          
            initializeTestProfessor();
            int idTestProfessor = PROFESSOR_DAO.registerProfessor(TEST_PROFESSOR);
            TEST_PROFESSOR.setIdProfessor(idTestProfessor);
            System.out.println(idTestProfessor);
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorTest.class.getName()).log(Level.SEVERE, null, exception);
        }                  
    }
    
    @After
    public void tearDown() {
        int idProfessor = TEST_PROFESSOR.getIdProfessor();
        int idUniversity = TEST_PROFESSOR.getIdUniversity();
        int idCountry = TEST_PROFESSOR.getUniversity().getIdCountry();
        
        try {
            PROFESSOR_DAO.deleteProfessorByID(idProfessor);
            UNIVERSITY_DAO.deleteUniversity(idUniversity);
            COUNTRY_DAO.deleteCountry(idCountry);
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorTest.class.getName()).log(Level.SEVERE, null, exception);
        }      
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
        System.out.print("registerProfessor success");
    }
    
    @Test
    public void testFailureRegisterProfessorByEmailAlreadyRegistered() {
        int idProfessor = 0;
        
        try {         
            idProfessor = PROFESSOR_DAO.registerProfessor(TEST_PROFESSOR);
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorTest.class.getName()).log(Level.SEVERE, null, exception);
        }   
        assertTrue(idProfessor > 0);
        System.out.print("registerProfessor failure, email already registered");
    }
    
    @Test
    public void testSuccesUpdateProfessor() {
        int result = 0;        
        
        initializeAuxTestProfessor();
        AUX_TEST_PROFESSOR.setIdProfessor(TEST_PROFESSOR.getIdProfessor());
        try {
            result = PROFESSOR_DAO.updateProfessor(AUX_TEST_PROFESSOR);
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result > 0);
    }
    
    @Test
    public void testFailureUpdateProfessorByEmailAlreadyRegistered() {
        int result = 0;
        
        initializeAuxTestProfessor();
        AUX_TEST_PROFESSOR.setIdProfessor(TEST_PROFESSOR.getIdProfessor());
        AUX_TEST_PROFESSOR.setEmail(TEST_PROFESSOR.getEmail());
        try {
            result = PROFESSOR_DAO.updateProfessor(AUX_TEST_PROFESSOR);
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result > 0);
    }    
    
    @Test
    public void testSuccessDeleteInstitutionalRepresentative() {
        int result = 0;
        
        try {
            result = PROFESSOR_DAO.deleteProfessorByID(TEST_PROFESSOR.getIdProfessor());
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result > 0);
    }  
    
    @Test
    public void testFailureDeleteInstitutionalRepresentativeByIdNotFound() {
        int result = 0;
        
        try {
            result = PROFESSOR_DAO.deleteProfessorByID(3);
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result > 0);
    }  
    
    
    @Test
    public void testSuccessGetProfessorByID() {
        Professor professor = new Professor();
        try {
            professor = PROFESSOR_DAO.getProfessorById(TEST_PROFESSOR.getIdProfessor());            
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertEquals(professor.getIdProfessor(), TEST_PROFESSOR.getIdProfessor());       
    }
    
    @Test 
    public void testFailureGetProfessorByIDNotFound() {
        Professor professor = new Professor();
        try {
            professor = PROFESSOR_DAO.getProfessorById(3);            
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertEquals(professor.getIdProfessor(), TEST_PROFESSOR.getIdProfessor());         
    }
    
    @Test 
    public void testSuccessGetProfessorByEmail() {
        Professor professor = new Professor();
        try {
            professor = PROFESSOR_DAO.getProfessorByEmail(TEST_PROFESSOR.getEmail());            
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorTest.class.getName()).log(Level.SEVERE, null, exception);
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
            Logger.getLogger(ProfessorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertEquals(professor.getIdProfessor(), TEST_PROFESSOR.getIdProfessor());       
    }  
   
    private Country initializeCountry() {
        Country country = new Country();
        
        country.setName("Mexico");
        try {
            int idCountry = COUNTRY_DAO.registerCountry(country);
            country.setIdCountry(idCountry);
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorTest.class.getName()).log(Level.SEVERE, null, exception);
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
            Logger.getLogger(ProfessorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        return university;
    }
    
    private void initializeTestProfessor() {        
        TEST_PROFESSOR.setName("Maria");
        TEST_PROFESSOR.setPaternalSurname("Arenas");
        TEST_PROFESSOR.setMaternalSurname("Valdes");
        TEST_PROFESSOR.setEmail("aaren@uv.mx");
        TEST_PROFESSOR.setGender("Mujer");
        TEST_PROFESSOR.setPhoneNumber("1234567890");
        TEST_PROFESSOR.setUniversity(initializeUniversity());
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
