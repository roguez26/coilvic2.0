package unit.test.UniversityDAO;

import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ivanr
 */
public class UniversityRegistrationTest {

    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final University UNIVERSITY_FOR_TESTING = new University();

    private static final University AUX_UNIVERSITY_FOR_TESTING = new University();

    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country AUX_COUNTRY = new Country();
    
    private void initializeAuxiliarCountry() {
        AUX_COUNTRY.setName("Mexico");
    }

    private void intitliazeUniversity() {
        UNIVERSITY_FOR_TESTING.setName("Universidad Veracruzana");
        UNIVERSITY_FOR_TESTING.setAcronym("UV");
        UNIVERSITY_FOR_TESTING.setJurisdiction("Veracruz");
        UNIVERSITY_FOR_TESTING.setCity("Xalapa");
        UNIVERSITY_FOR_TESTING.setCountry(AUX_COUNTRY);
    }

    private void intitliazeAuxiliarUniversity() {
        AUX_UNIVERSITY_FOR_TESTING.setName("Universidad Católica Andrés Bello");
        AUX_UNIVERSITY_FOR_TESTING.setAcronym("UCAB");
        AUX_UNIVERSITY_FOR_TESTING.setJurisdiction("Caracas");
        AUX_UNIVERSITY_FOR_TESTING.setCity("Guayana");
        AUX_UNIVERSITY_FOR_TESTING.setCountry(AUX_COUNTRY);
    }

    @Before
    public void setUp() {
        int idUniversity = 0;
        int idCountry = 0;
        initializeAuxiliarCountry();
        intitliazeAuxiliarUniversity();
        intitliazeUniversity();
        
        try {
            idCountry = COUNTRY_DAO.registerCountry(AUX_COUNTRY);
            AUX_COUNTRY.setIdCountry(idCountry);
            AUX_UNIVERSITY_FOR_TESTING.setCountry(AUX_COUNTRY);
            idUniversity = UNIVERSITY_DAO.registerUniversity(AUX_UNIVERSITY_FOR_TESTING);
            
        } catch (DAOException exception) {
            Logger.getLogger(UniversityRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        AUX_COUNTRY.setIdCountry(idCountry);
        UNIVERSITY_FOR_TESTING.setIdCountry(idCountry);
        AUX_UNIVERSITY_FOR_TESTING.setIdUniversity(idUniversity);
    }

    @Test
    public void testRegisterUniveritySuccess() {
        int idUniversity = 0;

        try {
            idUniversity = UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        UNIVERSITY_FOR_TESTING.setIdUniversity(idUniversity);
        assertTrue(idUniversity > 0);
    }

    @Test
    public void testRegisterUniverityFailByNameDuplication() {
        int idUniversity = 0;
        
        UNIVERSITY_FOR_TESTING.setName(AUX_UNIVERSITY_FOR_TESTING.getName());
        try {
            idUniversity = UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        UNIVERSITY_FOR_TESTING.setIdUniversity(idUniversity);
        assertTrue(idUniversity > 0);
    }
    
    @Test
    public void testRegisterUniversityFailByNonexistenceCountry() {
        int idUniversity = 0;
        int nonexistenceCountryId = -1;
        int idCountry;
        
        idCountry = UNIVERSITY_FOR_TESTING.getIdCountry();
        UNIVERSITY_FOR_TESTING.setIdCountry(nonexistenceCountryId);
        try {
            idUniversity = UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        UNIVERSITY_FOR_TESTING.setIdUniversity(idUniversity);
        UNIVERSITY_FOR_TESTING.setIdCountry(idCountry);
        assertTrue(idUniversity > 0);   
    }

    @After
    public void tearDown() {
        try {
            UNIVERSITY_DAO.deleteUniversity(UNIVERSITY_FOR_TESTING.getIdUniversity());
            UNIVERSITY_DAO.deleteUniversity(AUX_UNIVERSITY_FOR_TESTING.getIdUniversity());
            COUNTRY_DAO.deleteCountry(AUX_COUNTRY.getIdCountry());
        } catch (DAOException exception) {
            Logger.getLogger(UniversityRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }

    }

}
