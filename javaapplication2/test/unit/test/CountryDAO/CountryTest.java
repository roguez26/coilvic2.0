package unit.test.CountryDAO;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import log.Log;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ivanr
 */
public class CountryTest {

    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final ArrayList<Country> COUNTRIES_FOR_TESTING = new ArrayList<>();
    private static final Country COUNTRY_FOR_TESTING = new Country();

    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final University AUX_UNIVERSITY = new University();

    @Before
    public void setUp() {
        initializeCountries();
        initializeCountry();
    }

    @After
    public void tearDown() {
        try {
            UNIVERSITY_DAO.deleteUniversity(AUX_UNIVERSITY.getIdUniversity());
            for (int i = 0; i < 5; i++) {
                COUNTRY_DAO.deleteCountry(COUNTRIES_FOR_TESTING.get(i).getIdCountry());
            }
            COUNTRY_DAO.deleteCountry(COUNTRY_FOR_TESTING.getIdCountry());
        } catch (DAOException exception) {
            Log.getLogger(CountryTest.class).error(exception.getMessage(), exception);
        }
    }

    private void initilizeUniversity() {
        AUX_UNIVERSITY.setName("Universidad Veracruzana");
        AUX_UNIVERSITY.setAcronym("UV");
        AUX_UNIVERSITY.setJurisdiction("Veracruz");
        AUX_UNIVERSITY.setCity("Xalapa");
        AUX_UNIVERSITY.setCountry(COUNTRY_FOR_TESTING);
    }

    private void initializeCountries() {
        String[] countryNames = {"Argentina", "Ecuador", "Rusia", "Italia", "Colombia"};

        for (int i = 0; i < 5; i++) {
            Country country = new Country();
            country.setName(countryNames[i]);

            try {
                country.setIdCountry(COUNTRY_DAO.registerCountry(country));
            } catch (DAOException exception) {
                Log.getLogger(CountryTest.class).error(exception.getMessage(), exception);
            }
            COUNTRIES_FOR_TESTING.add(country);
        }
    }

    public void initializeCountry() {
        COUNTRY_FOR_TESTING.setName("Mexico");
    }

    @Test
    public void testRegisterCountrySuccess() {
        int idCountry = 0;

        try {
            idCountry = COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING);
        } catch (DAOException exception) {
            Log.getLogger(CountryTest.class).error(exception.getMessage(), exception);
        }
        COUNTRY_FOR_TESTING.setIdCountry(idCountry);
        assertTrue(idCountry > 0);
    }

    @Test
    public void testRegisterCountryFailByDuplicatedName() {
        int idCountry = 0;
        DAOException result = null;

        COUNTRY_FOR_TESTING.setName(COUNTRIES_FOR_TESTING.get(2).getName());
        try {
            idCountry = COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING);
        } catch (DAOException exception) {
            result = exception;
            System.out.println(exception.getMessage());
        }
        COUNTRY_FOR_TESTING.setIdCountry(idCountry);
        assertTrue(result != null);
    }

    @Test
    public void testDeleteCountrySuccess() {
        int result = 0;
        initializeCountry();
        try {
            COUNTRY_FOR_TESTING.setIdCountry(COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING));
            result = COUNTRY_DAO.deleteCountry(COUNTRY_FOR_TESTING.getIdCountry());
        } catch (DAOException exception) {
            Log.getLogger(CountryTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result > 0);
    }

    @Test
    public void testDeleteCountryFailByNonexistenceId() {
        int idNonexistence = 0;
        int rowsAffected = -1;
        initializeCountry();
        try {
            COUNTRY_FOR_TESTING.setIdCountry(COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING));
            rowsAffected = COUNTRY_DAO.deleteCountry(idNonexistence);
        } catch (DAOException exception) {
            Log.getLogger(CountryTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(rowsAffected == 0);
    }

    @Test
    public void testDeleteCountryFailByDependencies() {
        DAOException result = null;
        initializeCountry();
        try {
            COUNTRY_FOR_TESTING.setIdCountry(COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING));
            initilizeUniversity();
            AUX_UNIVERSITY.setIdUniversity(UNIVERSITY_DAO.registerUniversity(AUX_UNIVERSITY));
            COUNTRY_DAO.deleteCountry(COUNTRY_FOR_TESTING.getIdCountry());
        } catch (DAOException exception) {
            result = exception;
            System.out.println(result.getMessage());
        }
        assertTrue(result != null);
    }

    @Test
    public void testUpdateCountrySuccess() {
        int result = 0;
        String newName = "España";

        try {
            COUNTRY_FOR_TESTING.setIdCountry(COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING));
            COUNTRY_FOR_TESTING.setName(newName);
            result = COUNTRY_DAO.updateCountry(COUNTRY_FOR_TESTING);
        } catch (DAOException exception) {
            Log.getLogger(CountryTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result > 0);
    }

    @Test
    public void testUpdateCountryFailByDuplicatedName() {
        String newName = COUNTRIES_FOR_TESTING.get(2).getName();
        DAOException result = null;

        try {
            COUNTRY_FOR_TESTING.setIdCountry(COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING));
            COUNTRY_FOR_TESTING.setName(newName);
            COUNTRY_DAO.updateCountry(COUNTRY_FOR_TESTING);
        } catch (DAOException exception) {
            result = exception;
            System.out.println(result.getMessage());
        }
        assertTrue(result != null);
    }

    @Test
    public void testGetAllCountries() {
        ArrayList<Country> result = new ArrayList<>();

        try {
            result = COUNTRY_DAO.getAllCountries();
        } catch (DAOException exception) {
            Logger.getLogger(CountryTest.class.getName()).log(Level.SEVERE, null, exception);
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
            Logger.getLogger(CountryTest.class.getName()).log(Level.SEVERE, null, exception);
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
            Logger.getLogger(CountryTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result.getIdCountry() == 0);
    }

    @Test
    public void testGetCountryByNameSuccess() {
        Country result = new Country();
        int positionforSearch = 2;

        try {
            result = COUNTRY_DAO.getCountryByName(COUNTRIES_FOR_TESTING.get(positionforSearch).getName());
        } catch (DAOException exception) {
            Logger.getLogger(CountryTest.class.getName()).log(Level.SEVERE, null, exception);
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
            Logger.getLogger(CountryTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result.getIdCountry() == 0);
    }
}
