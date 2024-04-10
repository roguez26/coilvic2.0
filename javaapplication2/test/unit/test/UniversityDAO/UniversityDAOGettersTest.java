package unit.test.UniversityDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.util.ArrayList;
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
    private static final int COUNTRY_ID = 3;

    public UniversityDAOGettersTest() {

    }

    @Before
    public void setUp() {
        try {
            addPredeterminedUniversityInDB();
            initializeRepresentative();

            for (int i = 0; i < AUX_UNIVERSITIES_FOR_TESTING.size(); i++) {
                UNIVERSITY_IDS[i] = UNIVERSITY_DAO.registerUniversity(AUX_UNIVERSITIES_FOR_TESTING.get(i));
                AUX_UNIVERSITIES_FOR_TESTING.get(i).setIdUniversity(UNIVERSITY_IDS[i]);
                UNIVERSITIES_FOR_TESTING.add(AUX_UNIVERSITIES_FOR_TESTING.get(i));
            }
        } catch (DAOException exception) {
            Logger.getLogger(UniversityDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    private void initializeRepresentative() {
        for (int i = 0; i < 3; i++) {
            University university = new University();
            university.setName(NAMES[i]);
            university.setAcronym(ACRONYMS[i]);
            university.setJurisdiction(JURISDICTIONS[i]);
            university.setCity(CITIES[i]);
            university.setIdCountry(COUNTRY_ID);
            AUX_UNIVERSITIES_FOR_TESTING.add(university);
        }
    }

    private void addPredeterminedUniversityInDB() {
        University predeterminedUniversity = new University();
        predeterminedUniversity.setIdUniversity(1);
        predeterminedUniversity.setName("NombreUniversidad");
        predeterminedUniversity.setAcronym("NU");
        predeterminedUniversity.setJurisdiction("Jurisdiccion");
        predeterminedUniversity.setCity("Ciudad");
        predeterminedUniversity.setIdCountry(1);
        UNIVERSITIES_FOR_TESTING.add(predeterminedUniversity);
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
            result = UNIVERSITY_DAO.getUniversityById(UNIVERSITIES_FOR_TESTING.get(forSearch + 1).getIdUniversity());
        } catch (DAOException exception) {
            Logger.getLogger(UniversityDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertEquals(UNIVERSITIES_FOR_TESTING.get(forSearch + 1), result);
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
        assertNotNull(result);
    }
    
    @Test
    public void testGetUniversityByNameSuccess() {
        int result = 0;
        int forSearch = 2;
        try {
            result = UNIVERSITY_DAO.getUniverstiyIdByName(UNIVERSITIES_FOR_TESTING.get(forSearch + 1));
        } catch (DAOException exception) {
            Logger.getLogger(UniversityDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        System.out.println(result);
        assertTrue(result > 0);
    }
    
    @Test
    public void testGetUniversityByNameFailByNonexistenceName() {
        int result = 0;
        int forSearch = 2;
        String nonexistenceName = "nombre no existente";
        
        try {
            UNIVERSITIES_FOR_TESTING.get(forSearch + 1).setName(nonexistenceName);
            result = UNIVERSITY_DAO.getUniverstiyIdByName(UNIVERSITIES_FOR_TESTING.get(forSearch + 1));
        } catch (DAOException exception) {
            Logger.getLogger(UniversityDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        System.out.println(result);
        assertTrue(result > 0);
    }

    @After
    public void tearDown() {
        try {
            for (int i = 0; i < 3; i++) {
                UNIVERSITY_DAO.deleteUniversity(AUX_UNIVERSITIES_FOR_TESTING.get(i));
            }
        } catch (DAOException exception) {
            Logger.getLogger(UniversityDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
}
