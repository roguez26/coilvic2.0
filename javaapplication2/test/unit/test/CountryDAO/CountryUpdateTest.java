package unit.test.CountryDAO;

import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
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
public class CountryUpdateTest {

    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country COUNTRY_FOR_TESTING = new Country();

    private static final Country AUX_COUNTRY = new Country();


    public CountryUpdateTest() {

    }

    @Before
    public void setUp() {
        int idCountry;
        
        initializaeCountries();
        try {
            idCountry = COUNTRY_DAO.registerCountry(AUX_COUNTRY);
            AUX_COUNTRY.setIdCountry(idCountry);
            idCountry = COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING);
            COUNTRY_FOR_TESTING.setIdCountry(idCountry);
        } catch (DAOException exception) {
            Logger.getLogger(CountryUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
    
    private void initializaeCountries() {
        COUNTRY_FOR_TESTING.setName( "Alemania");
        AUX_COUNTRY.setName("Japon");
    }

    @Test
    public void testUpdateCountrySuccess() {
        int result = 0;
        String newName = "Espana";
        
        COUNTRY_FOR_TESTING.setName(newName);
        try {
            result = COUNTRY_DAO.updateCountry(COUNTRY_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(CountryUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result > 0);
    }
    
    @Test
    public void testUpdateCountryFailByDuplicatedName() {
        int result = 0;
        String newName = AUX_COUNTRY.getName();
        
        COUNTRY_FOR_TESTING.setName(newName);
        try {
            result = COUNTRY_DAO.updateCountry(COUNTRY_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(CountryUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(result > 0);
    }

    @After
    public void tearDown() {
        try {
            COUNTRY_DAO.deleteCountry(COUNTRY_FOR_TESTING.getIdCountry());
            COUNTRY_DAO.deleteCountry(AUX_COUNTRY.getIdCountry());
        } catch (DAOException exception) {
            Logger.getLogger(CountryRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
}
