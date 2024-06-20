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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import unit.test.Initializer.TestHelper;

public class UniversityTest {

    private final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private final ArrayList<University> UNIVERSITIES_FOR_TESTING = new ArrayList<>();
    private final University UNIVERSITY_FOR_TESTING = new University();

    private Country auxCountry;

    private final InstitutionalRepresentativeDAO REPRESENTATIVE_DAO = new InstitutionalRepresentativeDAO();
    private final InstitutionalRepresentative AUX_REPRESENTATIVE = new InstitutionalRepresentative();

    private final TestHelper TEST_HELPER = new TestHelper();

    @Before
    public void setUp() {
        TEST_HELPER.initializeCountries();
        auxCountry = TEST_HELPER.getCountryOne();
        initializeUniversities();
        initializeAuxiliarInstitutionalRepresentative();
        initilizeUniversity();
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

        if (UNIVERSITY_FOR_TESTING.getIdCountry() == 0) {
            return;
        }
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
    public void testRegisterUniveritySuccess() throws DAOException {
        int idUniversity = 0;

        idUniversity = UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING);
        UNIVERSITY_FOR_TESTING.setIdUniversity(idUniversity);
        System.out.println(idUniversity);
        assertTrue(idUniversity > 0);

    }

    @Test(expected = DAOException.class)
    public void testRegisterUniverityFailByNameDuplication() throws DAOException {
        UNIVERSITY_FOR_TESTING.setName(UNIVERSITIES_FOR_TESTING.get(2).getName());
        UNIVERSITY_FOR_TESTING.setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING));
    }

    @Test(expected = DAOException.class)
    public void testRegisterUniversityFailByNonexistenceCountry() throws DAOException {
        University auxUniversity = new University();

        auxUniversity.setName(UNIVERSITY_FOR_TESTING.getName());
        auxUniversity.setAcronym(UNIVERSITY_FOR_TESTING.getAcronym());
        auxUniversity.setCity(UNIVERSITY_FOR_TESTING.getCity());
        auxUniversity.setJurisdiction(UNIVERSITY_FOR_TESTING.getJurisdiction());
        UNIVERSITY_DAO.registerUniversity(auxUniversity);
    }

    @Test
    public void testDeleteUniversitySuccess() throws DAOException {
        int result = 0;

        UNIVERSITY_FOR_TESTING.setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING));
        result = UNIVERSITY_DAO.deleteUniversity(UNIVERSITY_FOR_TESTING.getIdUniversity());
        System.out.println(result);
        assertTrue(result > 0);
    }

    @Test(expected = DAOException.class)
    public void testDeleteUniversityFailByDependencies() throws DAOException {

        UNIVERSITY_FOR_TESTING.setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING));
        initializeAuxiliarInstitutionalRepresentative();
        UNIVERSITY_DAO.deleteUniversity(UNIVERSITY_FOR_TESTING.getIdUniversity());
    }

    @Test
    public void testDeleteUniversityFailByNonexistenceId() throws DAOException {
        int result = 0;
        int idNonexistence = 0;

        result = UNIVERSITY_DAO.deleteUniversity(idNonexistence);
        assertTrue(result == 0);
    }

    @Test
    public void testUpdateUniversitySuccess() throws DAOException {

        String newName = "Universidad Veracruzana nuevo";
        int result = 0;

        UNIVERSITY_FOR_TESTING.setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING));
        UNIVERSITY_FOR_TESTING.setName(newName);
        result = UNIVERSITY_DAO.updateUniversity(UNIVERSITY_FOR_TESTING);
        System.out.println(result);
        assertTrue(result > 0);
    }

    @Test(expected = DAOException.class)
    public void testUpdateUniversityFailByDuplicatedName() throws DAOException {
        String newName = UNIVERSITIES_FOR_TESTING.get(2).getName();

        UNIVERSITY_FOR_TESTING.setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING));
        UNIVERSITY_FOR_TESTING.setName(newName);
        UNIVERSITY_DAO.updateUniversity(UNIVERSITY_FOR_TESTING);
    }

    @Test
    public void testGetUniversityByNameSuccess() throws DAOException {
        University result = new University();
        int positionForSearch = 2;

        result = UNIVERSITY_DAO.getUniversityByName(UNIVERSITIES_FOR_TESTING.get(positionForSearch).
                getName());
        System.out.println(result);
        assertTrue(result.getIdUniversity() > 0);
    }

    @Test
    public void testGetUniversityByNameFailByNonexistenceName() throws DAOException {
        University result = new University();
        String nonexistenceName = "nombre no existente";

        result = UNIVERSITY_DAO.getUniversityByName(nonexistenceName);
        System.out.println(result);
        assertTrue(result.getIdUniversity() == 0);
    }

    @Test
    public void testGetUniversitiesSuccess() throws DAOException {
        ArrayList<University> result = new ArrayList<>();

        result = UNIVERSITY_DAO.getAllUniversities();
        System.out.println(result);
        assertEquals(UNIVERSITIES_FOR_TESTING, result);
    }

    @Test
    public void testGetAvailableUniversities() throws DAOException {
        ArrayList<University> result = new ArrayList<>();

        UNIVERSITY_FOR_TESTING.setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING));
        initializeAuxiliarInstitutionalRepresentative();
        result = UNIVERSITY_DAO.getAvailableUniversities();
        System.out.println(result);
        assertEquals(UNIVERSITIES_FOR_TESTING, result);

    }

}
