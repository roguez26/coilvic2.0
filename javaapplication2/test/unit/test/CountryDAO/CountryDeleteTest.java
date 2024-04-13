package unit.test.CountryDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
/**
 *
 * @author ivanr
 */
public class CountryDeleteTest {
    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country COUNTRY_FOR_TESTING = new Country();
    private static final String NAME = "Colombia";
    
    private static final UniversityDAO AUX_UNIVERSITY_DAO = new UniversityDAO();
    private static final University AUX_UNIVERSITY = new University();
    private static final String AUX_NAME = "Universidad Católica Andrés Bello";
    private static final String AUX_ACRONYM = "UCAB";
    private static final String AUX_JURISDICTION = "Caracas";
    private static final String AUX_CITY = "Guayana";
    
    public CountryDeleteTest() {

    }
    
    @Before
    public void setUp() {
        int idCountry = 0;
        int idUniversity = 0;
        intitliazeAuxiliarUniversity() ;
        COUNTRY_FOR_TESTING.setName(NAME);
        try {
            idCountry = COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING);
            AUX_UNIVERSITY.setIdCountry(idCountry);
            idUniversity = AUX_UNIVERSITY_DAO.registerUniversity(AUX_UNIVERSITY);
        } catch (DAOException exception) {
            Logger.getLogger(CountryRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        COUNTRY_FOR_TESTING.setIdCountry(idCountry);
        AUX_UNIVERSITY.setIdUniversity(idUniversity);
    }
    
    private void intitliazeAuxiliarUniversity() {
        AUX_UNIVERSITY.setName(AUX_NAME);
        AUX_UNIVERSITY.setAcronym(AUX_ACRONYM);
        AUX_UNIVERSITY.setJurisdiction(AUX_JURISDICTION);
        AUX_UNIVERSITY.setCity(AUX_CITY);
    }
    
    @Test
    public void testDeleteCountrySuccess() {
        int result = 0;
        
        try {
            AUX_UNIVERSITY_DAO.deleteUniversity(AUX_UNIVERSITY);
            result = COUNTRY_DAO.deleteCountry(COUNTRY_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(CountryRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result > 0);
    }
    
    @Test
    public void testDeleteCountryFailByDependencies() {
        int result = 0;

        try {
            result = COUNTRY_DAO.deleteCountry(COUNTRY_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(CountryRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(result > 0);
    }
    
    @After
    public void tearDown() {
        try {
            AUX_UNIVERSITY_DAO.deleteUniversity(AUX_UNIVERSITY);
            COUNTRY_DAO.deleteCountry(COUNTRY_FOR_TESTING);
        } catch(DAOException exception) {
            Logger.getLogger(CountryRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
}
