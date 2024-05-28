package unit.test.UniversityDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author ivanr
 */
public class UniversityDeleteTest {
    
    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final University UNIVERSITY_FOR_TESTING = new University();
     
    private static final InstitutionalRepresentativeDAO REPRESENTATIVE_DAO = new InstitutionalRepresentativeDAO();
    private static final InstitutionalRepresentative AUX_REPRESENTATIVE = new InstitutionalRepresentative();
    
    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country AUX_COUNTRY = new Country();
    
    
    @Before
    public void setUp() {
        int idUniversity;
        int idInstitutionalRepresentative = 0;
        int idCountry;
        
        initializeAuxiliarCountry();
        intitliazeUniversity();
        initializeAuxiliarInstitutionalRepresentative();
        try {
            idCountry = COUNTRY_DAO.registerCountry(AUX_COUNTRY);
            AUX_COUNTRY.setIdCountry(idCountry);
            UNIVERSITY_FOR_TESTING.setCountry(AUX_COUNTRY);
            
            idUniversity = UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING);
            UNIVERSITY_FOR_TESTING.setIdUniversity(idUniversity);
            AUX_REPRESENTATIVE.setUniversity(UNIVERSITY_FOR_TESTING);
            idInstitutionalRepresentative = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(AUX_REPRESENTATIVE);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        AUX_REPRESENTATIVE.setIdInstitutionalRepresentative(idInstitutionalRepresentative);
    }
    
    private void initializeAuxiliarCountry() {
        AUX_COUNTRY.setName("Mexico");
    }

    private void intitliazeUniversity() {
        UNIVERSITY_FOR_TESTING.setName("Universidad Veracruzana");
        UNIVERSITY_FOR_TESTING.setAcronym("UV");
        UNIVERSITY_FOR_TESTING.setJurisdiction("Veracruz");
        UNIVERSITY_FOR_TESTING.setCity("Xalapa");
    }
    
    private void initializeAuxiliarInstitutionalRepresentative() {
        AUX_REPRESENTATIVE.setName("Carlos");
        AUX_REPRESENTATIVE.setPaternalSurname("Oliva");
        AUX_REPRESENTATIVE.setMaternalSurname("Ramirez");
        AUX_REPRESENTATIVE.setPhoneNumber("2297253222");
        AUX_REPRESENTATIVE.setEmail("Carlos@gmail.com");
    }
    
    @Test
    public void testDeleteUniversitySuccess() {
        int result = 0;
        
        try {
            REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(AUX_REPRESENTATIVE);
            result = UNIVERSITY_DAO.deleteUniversity(UNIVERSITY_FOR_TESTING.getIdUniversity());
        } catch (DAOException exception) {
            Logger.getLogger(UniversityUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result > 0);
    }
    
    @Test
    public void testDeleteUniversityFailByDependencies() {
        int result = 0;
        
        try {
            result = UNIVERSITY_DAO.deleteUniversity(UNIVERSITY_FOR_TESTING.getIdUniversity());
        } catch (DAOException exception) {
            Logger.getLogger(UniversityUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(result > 0);
    }
    
    @Test
    public void testDeleteUniversityFailByNonexistenceId() {
        int result = 0;
        int idNonexistence = 0;
        
        try {
            result = UNIVERSITY_DAO.deleteUniversity(idNonexistence);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(result > 0);
    }
    
    
    @After
    public void tearDown() {
        try {
            REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(AUX_REPRESENTATIVE);
            UNIVERSITY_DAO.deleteUniversity(UNIVERSITY_FOR_TESTING.getIdUniversity());
            COUNTRY_DAO.deleteCountry(AUX_COUNTRY.getIdCountry());
        } catch (DAOException exception) {
            Logger.getLogger(UniversityUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
}
