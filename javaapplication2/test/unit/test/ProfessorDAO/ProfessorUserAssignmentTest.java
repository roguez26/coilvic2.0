package unit.test.ProfessorDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.professor.ProfessorDAO;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.user.User;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertTrue;

public class ProfessorUserAssignmentTest {

    private static final ProfessorDAO PROFESSOR_DAO = new ProfessorDAO();
    private static final Professor PROFESSOR_FOR_TESTING = new Professor();    
    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    
    @Before
    public void setUp() {
        initializeProfessor();
        
        int idProfessor = 0;
        try {
            idProfessor = PROFESSOR_DAO.registerProfessor(PROFESSOR_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorUserAssignmentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        PROFESSOR_FOR_TESTING.setIdProfessor(idProfessor);
    }
    
    @Test
    public void testUserRegistration() {
        User user = null;
        try {
            user = PROFESSOR_DAO.assignUser(PROFESSOR_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorUserAssignmentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(user != null && user.getIdUser() > 0);
    }
    
    @After
    public void tearDown() {
        try {
            PROFESSOR_DAO.deleteProfessorByID(PROFESSOR_FOR_TESTING.getIdProfessor());
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorUserAssignmentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
    
    private Country initializeCountry() {
        Country country = new Country();
        
        country.setName("Mexico");
        try {
            int idCountry = COUNTRY_DAO.registerCountry(country);
            country.setIdCountry(idCountry);
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorUserAssignmentTest.class.getName()).log(Level.SEVERE, null, exception);
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
            Logger.getLogger(ProfessorUserAssignmentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        return university;
    }    
    
    public void initializeProfessor() {
        PROFESSOR_FOR_TESTING.setName("Roberto");
        PROFESSOR_FOR_TESTING.setPaternalSurname("Martinez");
        PROFESSOR_FOR_TESTING.setMaternalSurname("Garza");
        PROFESSOR_FOR_TESTING.setEmail("roberto@gmail.com");
        PROFESSOR_FOR_TESTING.setGender("M");
        PROFESSOR_FOR_TESTING.setPhoneNumber("2283728394");
        PROFESSOR_FOR_TESTING.setUniversity(initializeUniversity());
    } 
}