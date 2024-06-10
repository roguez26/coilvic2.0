package unit.test.UniversityDAO;

import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import org.junit.After;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import unit.test.Initializer.TestHelper;

/**
 *
 * @author ivanr
 */
public class UniversityTest {

    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final ArrayList<University> UNIVERSITIES_FOR_TESTING = new ArrayList<>();
    private static final University UNIVERSITY_FOR_TESTING = new University();

    private Country auxCountry;

    private static final InstitutionalRepresentativeDAO REPRESENTATIVE_DAO = new InstitutionalRepresentativeDAO();
    private static final InstitutionalRepresentative AUX_REPRESENTATIVE = new InstitutionalRepresentative();

    private final TestHelper TEST_HELPER = new TestHelper();

    @Before
    public void setUp() {
        TEST_HELPER.initializeCountries();
        auxCountry = TEST_HELPER.getCountryOne();
        initializeUniversities();
        initializeAuxiliarInstitutionalRepresentative();
    }

    @After
    public void tearDown() {
        try {
            REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(AUX_REPRESENTATIVE);
            for (int i = 0; i < UNIVERSITIES_FOR_TESTING.size(); i++) {
                UNIVERSITY_DAO.deleteUniversity(UNIVERSITIES_FOR_TESTING.get(i).getIdUniversity());
            }
            UNIVERSITY_DAO.deleteUniversity(UNIVERSITY_FOR_TESTING.getIdUniversity());
        } catch (DAOException exception) {
            Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
        }
        TEST_HELPER.deleteAll();
    }

    private void initializeAuxiliarInstitutionalRepresentative() {
        AUX_REPRESENTATIVE.setName("Carlos");
        AUX_REPRESENTATIVE.setPaternalSurname("Oliva");
        AUX_REPRESENTATIVE.setMaternalSurname("Ramirez");
        AUX_REPRESENTATIVE.setPhoneNumber("2297253222");
        AUX_REPRESENTATIVE.setEmail("Carlos@gmail.com");
        AUX_REPRESENTATIVE.setUniversity(UNIVERSITY_FOR_TESTING);

        try {
            AUX_REPRESENTATIVE.setIdInstitutionalRepresentative(REPRESENTATIVE_DAO.
                    registerInstitutionalRepresentative(AUX_REPRESENTATIVE));
        } catch (DAOException exception) {
            Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
        }
    }

    private void initializeUniversities() {
        String[] names = {"Universidad Autonoma de México", "Tecnologico de Monterrey", "Universidad "
                + "Autonoma de Guadalajara"};
        String[] acronyms = {"UNAM", "ITESM", "UAG"};
        String[] jurisdictions = {"CDMX", "Nuevo León", "Jalisco"};
        String[] cities = {"Ciudad de México", "Monterrey", "Guadalajara"};
        for (int i = 0; i < 3; i++) {
            University university = new University();
            university.setName(names[i]);
            university.setAcronym(acronyms[i]);
            university.setJurisdiction(jurisdictions[i]);
            university.setCity(cities[i]);
            university.setCountry(auxCountry);
            try {
                university.setIdUniversity(UNIVERSITY_DAO.registerUniversity(university));
            } catch (DAOException exception) {
                Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
            }

            UNIVERSITIES_FOR_TESTING.add(university);
        }
    }

    private void initilizeUniversity() {
        UNIVERSITY_FOR_TESTING.setName("Universidad Veracruzana");
        UNIVERSITY_FOR_TESTING.setAcronym("UV");
        UNIVERSITY_FOR_TESTING.setJurisdiction("Veracruz");
        UNIVERSITY_FOR_TESTING.setCity("Xalapa");
        UNIVERSITY_FOR_TESTING.setCountry(auxCountry);
    }

    @Test
    public void testRegisterUniveritySuccess() {
        int idUniversity = 0;
        initilizeUniversity();
        try {
            idUniversity = UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING);
        } catch (DAOException exception) {
            Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
        }
        UNIVERSITY_FOR_TESTING.setIdUniversity(idUniversity);
        System.out.println(idUniversity);
        assertTrue(idUniversity > 0);
    }

    @Test
    public void testRegisterUniverityFailByNameDuplication() {
        UNIVERSITY_FOR_TESTING.setName(UNIVERSITIES_FOR_TESTING.get(2).getName());

        DAOException exception = assertThrows(DAOException.class, () -> UNIVERSITY_FOR_TESTING.
                setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING)));
        System.out.println(exception.getMessage());

    }

    @Test
    public void testRegisterUniversityFailByNonexistenceCountry() {
        int nonexistenceCountryId = -1;
        int idCountry;

        idCountry = UNIVERSITY_FOR_TESTING.getIdCountry();
        UNIVERSITY_FOR_TESTING.setIdCountry(nonexistenceCountryId);

        DAOException exception = assertThrows(DAOException.class, () -> UNIVERSITY_FOR_TESTING.
                setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING)));
        System.out.println(exception.getMessage());

        UNIVERSITY_FOR_TESTING.setIdCountry(idCountry);
    }

    @Test
    public void testDeleteUniversitySuccess() {
        int result = 0;
        initilizeUniversity();

        try {
            UNIVERSITY_FOR_TESTING.setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING));
            result = UNIVERSITY_DAO.deleteUniversity(UNIVERSITY_FOR_TESTING.getIdUniversity());
        } catch (DAOException exception) {
            Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(result);
        assertTrue(result > 0);
    }

    @Test
    public void testDeleteUniversityFailByDependencies() {
        initilizeUniversity();

        try {
            UNIVERSITY_FOR_TESTING.setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING));
        } catch (DAOException exception) {
            Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
        }
        initializeAuxiliarInstitutionalRepresentative();
        DAOException exception = assertThrows(DAOException.class, () -> UNIVERSITY_DAO.deleteUniversity(
                UNIVERSITY_FOR_TESTING.getIdUniversity()));
        System.out.println(exception.getMessage());
    }

    @Test
    public void testDeleteUniversityFailByNonexistenceId() {
        int result = 0;
        int idNonexistence = 0;
        initilizeUniversity();
        try {
            result = UNIVERSITY_DAO.deleteUniversity(idNonexistence);
        } catch (DAOException exception) {
            Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result == 0);
    }

    @Test
    public void testUpdateUniversitySuccess() {
        String newName = "Universidad Veracruzana nuevo";
        int result = 0;
        initilizeUniversity();

        try {
            UNIVERSITY_FOR_TESTING.setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING));
            UNIVERSITY_FOR_TESTING.setName(newName);
            result = UNIVERSITY_DAO.updateUniversity(UNIVERSITY_FOR_TESTING);
        } catch (DAOException exception) {
            Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(result);
        assertTrue(result > 0);
    }

    @Test
    public void testUpdateUniversityFailByDuplicatedName() {
        String newName = UNIVERSITIES_FOR_TESTING.get(2).getName();
        initilizeUniversity();

        try {
            UNIVERSITY_FOR_TESTING.setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING));
            UNIVERSITY_FOR_TESTING.setName(newName);
        } catch (DAOException exception) {
            Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
        }
        
        DAOException exception = assertThrows(DAOException.class, ()-> UNIVERSITY_DAO.updateUniversity(
                UNIVERSITY_FOR_TESTING));
        System.out.println(exception.getMessage());
    }

    @Test
    public void testGetUniversityByNameSuccess() {
        University result = new University();
        int positionForSearch = 2;

        try {
            result = UNIVERSITY_DAO.getUniversityByName(UNIVERSITIES_FOR_TESTING.get(positionForSearch).
                    getName());
        } catch (DAOException exception) {
            Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
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
            Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(result);
        assertTrue(result.getIdUniversity() == 0);
    }

}
