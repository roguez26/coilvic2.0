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
    private static final String NAME = "Universidad Veracruzana";
    private static final String ACRONYM = "UV";
    private static final String JURISDICTION = "Veracruz";
    private static final String CITY = "Xalapa";
    private static final int ID_COUNTRY = 1;

    private static final University AUX_UNIVERSITY_FOR_TESTING = new University();
    private static final String AUX_NAME = "Universidad Católica Andrés Bello";
    private static final String AUX_ACRONYM = "UCAB";
    private static final String AUX_JURISDICTION = "Caracas";
    private static final String AUX_CITY = "Guayana";
    
    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country AUX_COUNTRY = new Country();
    private static final String AUX_COUNTRY_NAME = "Mexico";

    private void intitliazeUniversity() {
        UNIVERSITY_FOR_TESTING.setName(NAME);
        UNIVERSITY_FOR_TESTING.setAcronym(ACRONYM);
        UNIVERSITY_FOR_TESTING.setJurisdiction(JURISDICTION);
        UNIVERSITY_FOR_TESTING.setCity(CITY);
        UNIVERSITY_FOR_TESTING.setIdCountry(ID_COUNTRY);
    }

    private void intitliazeAuxiliarUniversity() {
        AUX_UNIVERSITY_FOR_TESTING.setName(AUX_NAME);
        AUX_UNIVERSITY_FOR_TESTING.setAcronym(AUX_ACRONYM);
        AUX_UNIVERSITY_FOR_TESTING.setJurisdiction(AUX_JURISDICTION);
        AUX_UNIVERSITY_FOR_TESTING.setCity(AUX_CITY);
    }

    @Before
    public void setUp() {
        int idUniversity = 0;
        int idCountry = 0;
        intitliazeAuxiliarUniversity();
        intitliazeUniversity();
        AUX_COUNTRY.setName(AUX_COUNTRY_NAME);
        
        try {
            idCountry = COUNTRY_DAO.registerCountry(AUX_COUNTRY);
            AUX_UNIVERSITY_FOR_TESTING.setIdCountry(idCountry);
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
        
        UNIVERSITY_FOR_TESTING.setName(AUX_NAME);
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
    public void testRegistarUniversityFailByNonexistenceCountryId() {
        int idUniversity = 0;
        int nonexistenceCountryId = -1;
        
        UNIVERSITY_FOR_TESTING.setIdCountry(nonexistenceCountryId);
        try {
            idUniversity = UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        UNIVERSITY_FOR_TESTING.setIdUniversity(idUniversity);
        assertTrue(idUniversity > 0);   
    }

    @After
    public void tearDown() {
        try {
            UNIVERSITY_DAO.deleteUniversity(UNIVERSITY_FOR_TESTING);
            UNIVERSITY_DAO.deleteUniversity(AUX_UNIVERSITY_FOR_TESTING);
            COUNTRY_DAO.deleteCountry(AUX_COUNTRY);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }

    }

}
