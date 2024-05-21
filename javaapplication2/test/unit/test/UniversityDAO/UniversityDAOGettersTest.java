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

    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country AUX_COUNTRY = new Country();

    public UniversityDAOGettersTest() {

    }

    @Before
    public void setUp() {
        int idCountry;
        int idUniversity;
        initializeAuxCountry();

        try {
            idCountry = COUNTRY_DAO.registerCountry(AUX_COUNTRY);
            AUX_COUNTRY.setIdCountry(idCountry);
            initializeUniversities();
            for (int i = 0; i < UNIVERSITIES_FOR_TESTING.size(); i++) {
                idUniversity = UNIVERSITY_DAO.registerUniversity(UNIVERSITIES_FOR_TESTING.get(i));
                UNIVERSITIES_FOR_TESTING.get(i).setIdUniversity(idUniversity);
            }
        } catch (DAOException exception) {
            Logger.getLogger(UniversityDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    private void initializeUniversities() {
        String[] names = {"Harvard University", "Stanford University", "Universidad Autonoma de Mexico"};
        String[] acronyms = {"Harvard", "Stanford", "UNAM"};
        String[] juristictions = {"Massachusetts", "California", "CDMX"};
        String[] cities = {"Cambridge", "Stanford", "Ciudad de Mexico"};
        for (int i = 0; i < 3; i++) {
            University university = new University();
            university.setName(names[i]);
            university.setAcronym(acronyms[i]);
            university.setJurisdiction(juristictions[i]);
            university.setCity(cities[i]);
            university.setCountry(AUX_COUNTRY);
            UNIVERSITIES_FOR_TESTING.add(university);
        }
    }

    private void initializeAuxCountry() {
        AUX_COUNTRY.setName("Alemania");
    }

    @Test
    public void testGetAllUniveritiesSuccess() {
        ArrayList<University> result = new ArrayList<>();

        try {
            result = UNIVERSITY_DAO.getAllUniversities();
        } catch (DAOException exception) {
            Logger.getLogger(UniversityDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        System.out.println(result);
        assertEquals(UNIVERSITIES_FOR_TESTING, result);
    }

    @Test
    public void testGetUniversityByIdSuccess() {
        University result = new University();
        int positionForSearch = 2;

        try {
            result = UNIVERSITY_DAO.getUniversityById(UNIVERSITIES_FOR_TESTING.get(positionForSearch).getIdUniversity());

        } catch (DAOException exception) {
            Logger.getLogger(UniversityDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertEquals(UNIVERSITIES_FOR_TESTING.get(positionForSearch), result);
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
        int positionForSearch = 2;

        try {
            result = UNIVERSITY_DAO.getUniversityByName(UNIVERSITIES_FOR_TESTING.get(positionForSearch).getName());

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
                UNIVERSITY_DAO.deleteUniversity(UNIVERSITIES_FOR_TESTING.get(i).getIdUniversity());
            }
            COUNTRY_DAO.deleteCountry(AUX_COUNTRY.getIdCountry());

        } catch (DAOException exception) {
            Logger.getLogger(UniversityDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
}
