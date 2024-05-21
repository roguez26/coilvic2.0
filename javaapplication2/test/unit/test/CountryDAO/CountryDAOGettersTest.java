package unit.test.CountryDAO;

import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 *
 * @author ivanr
 */
public class CountryDAOGettersTest {

    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final ArrayList<Country> COUNTRIES_FOR_TESTING = new ArrayList<>();
    private int countriesQuantity;
    
    public CountryDAOGettersTest() {
     
    }
    
    @Before
    public void setUp() {
        int idCountry;
        initializeCountries();

        try {
            for (int i = 0; i < COUNTRIES_FOR_TESTING.size(); i++) {
                idCountry = COUNTRY_DAO.registerCountry(COUNTRIES_FOR_TESTING.get(i));
                COUNTRIES_FOR_TESTING.get(i).setIdCountry(idCountry);
            }
        } catch (DAOException exception) {
            Logger.getLogger(CountryDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    private void initializeCountries() {
        String[] countryNames = {"Argentina", "Ecuador", "Rusia", "Italia", "Colombia"};
        countriesQuantity = countryNames.length;
        
        for (int i = 0; i < countriesQuantity; i++) {
            Country country = new Country();
            country.setName(countryNames[i]);
            COUNTRIES_FOR_TESTING.add(country);
        }
    }

    @Test
    public void testGetAllCountries() {
        ArrayList<Country> result = new ArrayList<>();

        try {
            result = COUNTRY_DAO.getAllCountries();
        } catch (DAOException exception) {
            Logger.getLogger(CountryDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertEquals(COUNTRIES_FOR_TESTING, result);
    }

    @Test
    public void testGetCountryByIdSuccess() {
        Country result = new Country();
        int positionForSearch = 1;

        try {
            result = COUNTRY_DAO.getCountryById(COUNTRIES_FOR_TESTING.get(positionForSearch).getIdCountry());
        } catch (DAOException exception) {
            Logger.getLogger(CountryDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        
        assertEquals(COUNTRIES_FOR_TESTING.get(positionForSearch), result);
    }
    
    @Test
    public void testGetCountryByIdFailByNonexistenceId() {
        Country result = new Country();
        int idNonexistence = 0;
        
        try {
            result = COUNTRY_DAO.getCountryById(idNonexistence);
        } catch (DAOException exception) {
            Logger.getLogger(CountryDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(result.getIdCountry()> 0);
    }
    
    @Test
    public void testGetCountryByNameSuccess() {
        Country result = new Country();
        int positionforSearch = 2;
        
        try {
            result = COUNTRY_DAO.getCountryByName(COUNTRIES_FOR_TESTING.get(positionforSearch).getName());
        } catch (DAOException exception) {
            Logger.getLogger(CountryDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result.getIdCountry() > 0);
    }
    
    @Test
    public void testGetCountryByNameFailByNonexistenceName() {
        Country result = new Country();
        String nonexistenceName = "nombre no existente";
        
        try {
            result = COUNTRY_DAO.getCountryByName(nonexistenceName);
        } catch (DAOException exception) {
            Logger.getLogger(CountryDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getLocalizedMessage());
        }
        assertTrue(result.getIdCountry() > 0);
    }
    
    @After
    public void tearDown() {
        try {
            for (int i = 0; i < countriesQuantity; i++) {
                COUNTRY_DAO.deleteCountry(COUNTRIES_FOR_TESTING.get(i).getIdCountry());
            }
        } catch (DAOException exception) {
            Logger.getLogger(CountryDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

}
