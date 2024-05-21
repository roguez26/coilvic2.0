package unit.test.CountryDAO;

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
public class CountryRegistrationTest {
    
    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country COUNTRY_FOR_TESTING = new Country();

    private static final Country AUX_COUNTRY = new Country();
    
    @Before
    public void setUp() {
        int idCountry = 0;
        initializaeCountries();
        
        try {
            idCountry = COUNTRY_DAO.registerCountry(AUX_COUNTRY);
        } catch (DAOException exception) {
            Logger.getLogger(CountryRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        AUX_COUNTRY.setIdCountry(idCountry);
    }
    
    private void initializaeCountries() {
        COUNTRY_FOR_TESTING.setName( "Alemania");
        AUX_COUNTRY.setName("Japon");
    }
    
    @Test
    public void testRegisterCountrySuccess() {
        int idCountry = 0;
        
        try {
            idCountry = COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(CountryRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        COUNTRY_FOR_TESTING.setIdCountry(idCountry);
        assertTrue(idCountry > 0);
    }
    
    @Test
    public void testRegisterCountryFailByDuplicatedName() {
        int idCountry = 0;
        
        COUNTRY_FOR_TESTING.setName(AUX_COUNTRY.getName());
        try {
            idCountry = COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(CountryRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        COUNTRY_FOR_TESTING.setIdCountry(idCountry);
        assertTrue(idCountry > 0);
    }
    
    @After
    public void tearDown() {
        try {
            COUNTRY_DAO.deleteCountry(COUNTRY_FOR_TESTING.getIdCountry());
            COUNTRY_DAO.deleteCountry(AUX_COUNTRY.getIdCountry());
        } catch(DAOException exception) {
            Logger.getLogger(CountryRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
    
}
