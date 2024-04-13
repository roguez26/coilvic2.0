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
    private static final ArrayList<Country> AUX_COUNTRIES_FOR_TESTING = new ArrayList<>();
    private static final String[] NAMES = {"Argentina", "Ecuador", "Rusia"};
    private int[] COUNTRIES_IDS = {0, 0, 0};

    public CountryDAOGettersTest() {
        initializeCountries();

        try {
            for (int i = 0; i < AUX_COUNTRIES_FOR_TESTING.size(); i++) {
                COUNTRIES_IDS[i] = COUNTRY_DAO.registerCountry(AUX_COUNTRIES_FOR_TESTING.get(i));
                AUX_COUNTRIES_FOR_TESTING.get(i).setIdCountry(COUNTRIES_IDS[i]);
                COUNTRIES_FOR_TESTING.add(AUX_COUNTRIES_FOR_TESTING.get(i));
            }
        } catch (DAOException exception) {
            Logger.getLogger(CountryDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    private void initializeCountries() {
        for (int i = 0; i < 3; i++) {
            Country country = new Country();
            country.setName(NAMES[i]);
            AUX_COUNTRIES_FOR_TESTING.add(country);
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
        int forSearch = 1;

        try {
            result = COUNTRY_DAO.getCountryById(COUNTRIES_FOR_TESTING.get(forSearch).getIdCountry());
        } catch (DAOException exception) {
            Logger.getLogger(CountryDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertEquals(COUNTRIES_FOR_TESTING.get(forSearch), result);
    }
    
    @Test
    public void testGetCountryByIdFailByNonexistenceId() {
        Country result = new Country();
        int nonexistencId = 0;
        try {
            result = COUNTRY_DAO.getCountryById(nonexistencId);
        } catch (DAOException exception) {
            Logger.getLogger(CountryDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        System.out.println(result);
        assertTrue(result.getIdCountry()> 0);
    }
    
    @Test
    public void testGetCountryByNameSuccess() {
        Country result = new Country();
        int forSearch = 2;
        
        try {
            result = COUNTRY_DAO.getCountryByName(COUNTRIES_FOR_TESTING.get(forSearch).getName());
        } catch (DAOException exception) {
            Logger.getLogger(CountryDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        System.out.println(result);
        assertTrue(result.getIdCountry() > 0);
    }
    
    @Test
    public void testGetUniversityByNameFailByNonexistenceName() {
        Country result = new Country();
        String nonexistenceName = "nombre no existente";
        
        try {
            result = COUNTRY_DAO.getCountryByName(nonexistenceName);
        } catch (DAOException exception) {
            Logger.getLogger(CountryDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        System.out.println(result);
        assertTrue(result.getIdCountry() > 0);
    }
    
    @After
    public void tearDown() {
        try {
            for (int i = 0; i < 3; i++) {
                COUNTRY_DAO.deleteCountry(AUX_COUNTRIES_FOR_TESTING.get(i));
            }

        } catch (DAOException exception) {
            Logger.getLogger(CountryDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

}
