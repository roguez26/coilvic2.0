package unit.test.UniversityDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.university.University;

/**
 *
 * @author ivanr
 */
public class UniversityDAOGettersTest {

    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final ArrayList<University> UNIVERSITIES_FOR_TESTING = new ArrayList<>();
    private static final ArrayList<University> AUX_UNIVERSITIES_FOR_TESTING = new ArrayList<>();
    private static final String[] NAMES = {"Harvard University", "Stanford University", "Universidad Autonoma de Mexico"};
    private static final String[] ACRONYMS = {"Harvard", "Stanford", "UNAM"};
    private static final String[] JURISDICTIONS = {"Massachusetts", "California", "CDMX"};
    private static final String[] CITIES = {"Cambridge", "Stanford", "Ciudad de Mexico"};
    private static final int[] UNIVERSITY_IDS = {0, 0, 0};
    
    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country AUX_COUNTRY = new Country();
    private static final String AUX_NAME = "Alemania";

    public UniversityDAOGettersTest() {

    }

    @Before
    public void setUp() {
        int idCountry = 0;
        AUX_COUNTRY.setName(AUX_NAME);
        
        try {
            idCountry = COUNTRY_DAO.registerCountry(AUX_COUNTRY);
            AUX_COUNTRY.setIdCountry(idCountry);
            initializeUniversities(idCountry);
            for (int i = 0; i < AUX_UNIVERSITIES_FOR_TESTING.size(); i++) {
                UNIVERSITY_IDS[i] = UNIVERSITY_DAO.registerUniversity(AUX_UNIVERSITIES_FOR_TESTING.get(i));
                AUX_UNIVERSITIES_FOR_TESTING.get(i).setIdUniversity(UNIVERSITY_IDS[i]);
                UNIVERSITIES_FOR_TESTING.add(AUX_UNIVERSITIES_FOR_TESTING.get(i));
            }
        } catch (DAOException exception) {
            Logger.getLogger(UniversityDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    private void initializeUniversities(int idCountry) {
        for (int i = 0; i < 3; i++) {
            University university = new University();
            university.setName(NAMES[i]);
            university.setAcronym(ACRONYMS[i]);
            university.setJurisdiction(JURISDICTIONS[i]);
            university.setCity(CITIES[i]);
            university.setIdCountry(idCountry);
            AUX_UNIVERSITIES_FOR_TESTING.add(university);
        }
    }

    @Test
    public void testGetAllUniveritiesSuccess() {
        ArrayList<University> result = new ArrayList<>();

        try {
            result = UNIVERSITY_DAO.getAllUniversities();
        } catch (DAOException exception) {
            Logger.getLogger(UniversityDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertEquals(UNIVERSITIES_FOR_TESTING, result);
    }

    @Test
    public void testGetUniversityByIdSuccess() {
        University result = new University();
        int forSearch = 2;
        
        try {
            result = UNIVERSITY_DAO.getUniversityById(UNIVERSITIES_FOR_TESTING.get(forSearch).getIdUniversity());
        } catch (DAOException exception) {
            Logger.getLogger(UniversityDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertEquals(UNIVERSITIES_FOR_TESTING.get(forSearch), result);
    }
    
    @Test
    public void testGetUniversityByIdFailByNonexistenceId() {
        University result = new University();
        int nonexistencId = 0;
        try {
            result = UNIVERSITY_DAO.getUniversityById(nonexistencId);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        System.out.println(result);
        assertTrue(result.getIdUniversity() > 0);
    }
    
    @Test
    public void testGetUniversityByNameSuccess() {
        University result = new University();
        int forSearch = 2;
        
        try {
            result = UNIVERSITY_DAO.getUniversityByName(UNIVERSITIES_FOR_TESTING.get(forSearch).getName());
        } catch (DAOException exception) {
            Logger.getLogger(UniversityDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        System.out.println(result);
        assertTrue(result.getIdUniversity() > 0);
    }
    
    @Test
    public void testGetUniversityByNameFailByNonexistenceName() {
        University result = new University();
        String nonexistenceName = "nombre no existente";
        
        try {
            result = UNIVERSITY_DAO.getUniversityByName(nonexistenceName);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        System.out.println(result);
        assertTrue(result.getIdUniversity() > 0);
    }

    @After
    public void tearDown() {
        try {
            for (int i = 0; i < 3; i++) {
                UNIVERSITY_DAO.deleteUniversity(AUX_UNIVERSITIES_FOR_TESTING.get(i));
            }
            COUNTRY_DAO.deleteCountry(AUX_COUNTRY);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
}
