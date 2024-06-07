package unit.test.UniversityDAO;

import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ivanr
 */
public class UniversityTest {

    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final ArrayList<University> UNIVERSITIES_FOR_TESTING = new ArrayList<>();
    private static final University UNIVERSITY_FOR_TESTING = new University();

    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country AUX_COUNTRY = new Country();

    private static final InstitutionalRepresentativeDAO REPRESENTATIVE_DAO = new InstitutionalRepresentativeDAO();
    private static final InstitutionalRepresentative AUX_REPRESENTATIVE = new InstitutionalRepresentative();

    public UniversityTest() {

    }

    @Before
    public void setUp() {
        initializeAuxCountry();
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
            COUNTRY_DAO.deleteCountry(AUX_COUNTRY.getIdCountry());
        } catch (DAOException exception) {
            Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
        }
    }

    private void initializeAuxiliarInstitutionalRepresentative() {
        AUX_REPRESENTATIVE.setName("Carlos");
        AUX_REPRESENTATIVE.setPaternalSurname("Oliva");
        AUX_REPRESENTATIVE.setMaternalSurname("Ramirez");
        AUX_REPRESENTATIVE.setPhoneNumber("2297253222");
        AUX_REPRESENTATIVE.setEmail("Carlos@gmail.com");
        AUX_REPRESENTATIVE.setUniversity(UNIVERSITY_FOR_TESTING);

        try {
            AUX_REPRESENTATIVE.setIdInstitutionalRepresentative(REPRESENTATIVE_DAO.registerInstitutionalRepresentative(AUX_REPRESENTATIVE));
        } catch (DAOException exception) {
            Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
        }
    }

    private void initializeUniversities() {
        String[] names = {"Universidad Autonoma de México", "Tecnologico de Monterrey", "Universidad Autonoma de Guadalajara"};
        String[] acronyms = {"UNAM", "ITESM", "UAG"};
        String[] jurisdictions = {"CDMX", "Nuevo León", "Jalisco"};
        String[] cities = {"Ciudad de México", "Monterrey", "Guadalajara"};
        for (int i = 0; i < 3; i++) {
            University university = new University();
            university.setName(names[i]);
            university.setAcronym(acronyms[i]);
            university.setJurisdiction(jurisdictions[i]);
            university.setCity(cities[i]);
            university.setCountry(AUX_COUNTRY);
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
        UNIVERSITY_FOR_TESTING.setCountry(AUX_COUNTRY);
    }

    private void initializeAuxCountry() {
        AUX_COUNTRY.setName("México");

        try {
            AUX_COUNTRY.setIdCountry(COUNTRY_DAO.registerCountry(AUX_COUNTRY));
        } catch (DAOException exception) {
            Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
        }
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
        assertTrue(idUniversity > 0);
    }

    @Test
    public void testRegisterUniverityFailByNameDuplication() {
        DAOException result = null;

        UNIVERSITY_FOR_TESTING.setName(UNIVERSITIES_FOR_TESTING.get(2).getName());
        try {
            UNIVERSITY_FOR_TESTING.setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING));
        } catch (DAOException exception) {
            result = exception;
            System.out.println(result.getMessage());
        }
        assertTrue(result != null);
    }

    @Test
    public void testRegisterUniversityFailByNonexistenceCountry() {
        int nonexistenceCountryId = -1;
        int idCountry;
        DAOException result = null;

        idCountry = UNIVERSITY_FOR_TESTING.getIdCountry();
        UNIVERSITY_FOR_TESTING.setIdCountry(nonexistenceCountryId);
        try {
            UNIVERSITY_FOR_TESTING.setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING));
        } catch (DAOException exception) {
            result = exception;
            System.out.println(result.getMessage());
        }
        UNIVERSITY_FOR_TESTING.setIdCountry(idCountry);
        assertTrue(result != null);
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
        assertTrue(result > 0);
    }

    @Test
    public void testDeleteUniversityFailByDependencies() {
        initilizeUniversity();
        DAOException result = null;
        try {
            UNIVERSITY_FOR_TESTING.setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING));
            initializeAuxiliarInstitutionalRepresentative();

            UNIVERSITY_DAO.deleteUniversity(UNIVERSITY_FOR_TESTING.getIdUniversity());
        } catch (DAOException exception) {
            result = exception;
            System.out.println(result.getMessage());
        }
        assertTrue(result != null);
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
        assertTrue(result > 0);
    }

    @Test
    public void testUpdateUniversityFailByDuplicatedName() {
        String newName = UNIVERSITIES_FOR_TESTING.get(2).getName();
        initilizeUniversity();
        DAOException result = null;

        try {
            UNIVERSITY_FOR_TESTING.setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING));
            UNIVERSITY_FOR_TESTING.setName(newName);
            UNIVERSITY_DAO.updateUniversity(UNIVERSITY_FOR_TESTING);
        } catch (DAOException exception) {
            result = exception;
            System.out.println(result.getMessage());
        }
        assertTrue(result != null);
    }

    @Test
    public void testGetUniversityByNameSuccess() {
        University result = new University();
        int positionForSearch = 2;

        try {
            result = UNIVERSITY_DAO.getUniversityByName(UNIVERSITIES_FOR_TESTING.get(positionForSearch).getName());
        } catch (DAOException exception) {
            Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
        }
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
        assertTrue(result.getIdUniversity() == 0);
    }

}