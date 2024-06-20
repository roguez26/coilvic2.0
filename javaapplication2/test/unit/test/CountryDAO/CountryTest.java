package unit.test.CountryDAO;

import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class CountryTest {

    private final CountryDAO COUNTRY_DAO = new CountryDAO();
    private final ArrayList<Country> COUNTRIES_FOR_TESTING = new ArrayList<>();
    private final Country COUNTRY_FOR_TESTING = new Country();

    private final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private final University AUX_UNIVERSITY = new University();

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
            COUNTRIES_FOR_TESTING.clear();
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
        String[] countryCodes = {"+54", "+593", "+7", "+39", "+57"};

        for (int i = 0; i < 5; i++) {
            Country country = new Country();
            country.setName(countryNames[i]);
            country.setCountryCode(countryCodes[i]);
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
        COUNTRY_FOR_TESTING.setCountryCode("+52");
    }

    @Test
    public void testRegisterCountrySuccess() {
        int idCountry = 0;

        try {
            idCountry = COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING);
            COUNTRY_FOR_TESTING.setIdCountry(idCountry);
        } catch (DAOException exception) {
            Log.getLogger(CountryTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(idCountry);
        assertTrue(idCountry > 0);
    }

    @Test(expected = DAOException.class)
    public void testRegisterCountryFailByDuplicatedName() throws DAOException {
        COUNTRY_FOR_TESTING.setName(COUNTRIES_FOR_TESTING.get(2).getName());
        COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING);
    }

    @Test
    public void testDeleteCountrySuccess() throws DAOException {
        int result = 0;
        initializeCountry();
        COUNTRY_FOR_TESTING.setIdCountry(COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING));
        result = COUNTRY_DAO.deleteCountry(COUNTRY_FOR_TESTING.getIdCountry());
        System.out.println(result);
        assertTrue(result > 0);
    }

    @Test
    public void testDeleteCountryFailByNonexistenceId() throws DAOException {
        int idNonexistence = 0;
        int rowsAffected = -1;
        initializeCountry();
        COUNTRY_FOR_TESTING.setIdCountry(COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING));
        rowsAffected = COUNTRY_DAO.deleteCountry(idNonexistence);
        System.out.println(rowsAffected);
        assertTrue(rowsAffected == 0);
    }

    @Test(expected = DAOException.class)
    public void testDeleteCountryFailByDependencies() throws DAOException {
        initializeCountry();
        COUNTRY_FOR_TESTING.setIdCountry(COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING));
        initilizeUniversity();
        AUX_UNIVERSITY.setIdUniversity(UNIVERSITY_DAO.registerUniversity(AUX_UNIVERSITY));
        COUNTRY_DAO.deleteCountry(COUNTRY_FOR_TESTING.getIdCountry());
    }

    @Test
    public void testUpdateCountrySuccess() throws DAOException {
        int result = 0;
        String newName = "Irlanda";
        COUNTRY_FOR_TESTING.setIdCountry(COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING));
        COUNTRY_FOR_TESTING.setName(newName);
        result = COUNTRY_DAO.updateCountry(COUNTRY_FOR_TESTING);
        System.out.println(result);
        assertTrue(result > 0);
    }

    @Test(expected = DAOException.class)
    public void testUpdateCountryFailByDuplicatedName() throws DAOException {
        String newName = COUNTRIES_FOR_TESTING.get(2).getName();

        COUNTRY_FOR_TESTING.setIdCountry(COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING));
        COUNTRY_FOR_TESTING.setName(newName);
        COUNTRY_DAO.updateCountry(COUNTRY_FOR_TESTING);
    }

    @Test
    public void testGetAllCountries() throws DAOException {
        ArrayList<Country> result = new ArrayList<>();

        result = COUNTRY_DAO.getAllCountries();
        System.out.println(result);
        assertEquals(COUNTRIES_FOR_TESTING, result);
    }

    @Test
    public void testGetCountryByIdSuccess() throws DAOException {
        Country result = new Country();
        int positionForSearch = 1;

        result = COUNTRY_DAO.getCountryById(COUNTRIES_FOR_TESTING.get(positionForSearch).getIdCountry());
        System.out.println(result);
        assertEquals(COUNTRIES_FOR_TESTING.get(positionForSearch), result);
    }

    @Test
    public void testGetCountryByIdFailByNonexistenceId() throws DAOException {
        Country result = new Country();
        int idNonexistence = 0;

        result = COUNTRY_DAO.getCountryById(idNonexistence);
        assertTrue(result.getIdCountry() == 0);
    }

    @Test
    public void testGetCountryByNameSuccess() throws DAOException {
        Country result = new Country();
        int positionforSearch = 2;

        result = COUNTRY_DAO.getCountryByName(COUNTRIES_FOR_TESTING.get(positionforSearch).getName());
        System.out.println(result.getName());
        assertTrue(result.getIdCountry() > 0);
    }

    @Test
    public void testGetCountryByNameFailByNonexistenceName() throws DAOException {
        Country result = new Country();
        String nonexistenceName = "nombre no existente";

        result = COUNTRY_DAO.getCountryByName(nonexistenceName);
        System.out.println(result.getIdCountry());
        assertTrue(result.getIdCountry() == 0);
    }
}
