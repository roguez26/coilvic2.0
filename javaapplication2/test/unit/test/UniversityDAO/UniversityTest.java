package unit.test.UniversityDAO;

import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
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

    public void setUp() {
        TEST_HELPER.initializeCountries();
        auxCountry = TEST_HELPER.getCountryOne();
        initializeUniversities();
        initializeAuxiliarInstitutionalRepresentative();
    }
    
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
    public void testRegisterUniveritySuccess() {
        setUp();
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
        tearDown();
    }

    @Test
    public void testRegisterUniverityFailByNameDuplication() {
        setUp();
        UNIVERSITY_FOR_TESTING.setName(UNIVERSITIES_FOR_TESTING.get(2).getName());

        DAOException exception = assertThrows(DAOException.class, () -> UNIVERSITY_FOR_TESTING.
                setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING)));
        System.out.println(exception.getMessage());
        tearDown();
    }

    @Test
    public void testRegisterUniversityFailByNonexistenceCountry() {
        setUp();
        int nonexistenceCountryId = -1;
        int idCountry;

        idCountry = UNIVERSITY_FOR_TESTING.getIdCountry();
        UNIVERSITY_FOR_TESTING.setIdCountry(nonexistenceCountryId);

        DAOException exception = assertThrows(DAOException.class, () -> UNIVERSITY_FOR_TESTING.
                setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING)));
        System.out.println(exception.getMessage());

        UNIVERSITY_FOR_TESTING.setIdCountry(idCountry);
        tearDown();
    }

    @Test
    public void testDeleteUniversitySuccess() {
        setUp();
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
        tearDown();
    }

    @Test
    public void testDeleteUniversityFailByDependencies() {
        setUp();
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
        tearDown();
    }

    @Test
    public void testDeleteUniversityFailByNonexistenceId() {
        setUp();
        int result = 0;
        int idNonexistence = 0;
        initilizeUniversity();
        try {
            result = UNIVERSITY_DAO.deleteUniversity(idNonexistence);
        } catch (DAOException exception) {
            Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result == 0);
        tearDown();
    }

    @Test
    public void testUpdateUniversitySuccess() {
        setUp();
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
        tearDown();
    }

    @Test
    public void testUpdateUniversityFailByDuplicatedName() {
        setUp();
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
        tearDown();
    }

    @Test
    public void testGetUniversityByNameSuccess() {
        setUp();
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
        tearDown();
    }

    @Test
    public void testGetUniversityByNameFailByNonexistenceName() {
        setUp();
        University result = new University();
        String nonexistenceName = "nombre no existente";

        try {
            result = UNIVERSITY_DAO.getUniversityByName(nonexistenceName);
        } catch (DAOException exception) {
            Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(result);
        assertTrue(result.getIdUniversity() == 0);
        tearDown();
    }
    
    @Test
    public void testGetUniversitiesSuccess() {
        setUp();
        ArrayList<University> result = new ArrayList<>();
        
        try {
            result = UNIVERSITY_DAO.getAllUniversities();
        } catch (DAOException exception) {
            Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(result);
        assertEquals(UNIVERSITIES_FOR_TESTING, result);
        tearDown();
    }
    
    @Test
    public void testGetAvailableUniversities() {
        setUp();
        ArrayList<University> result = new ArrayList<>();
        initilizeUniversity();
        try {
            UNIVERSITY_FOR_TESTING.setIdUniversity(UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING));
            initializeAuxiliarInstitutionalRepresentative();
            result = UNIVERSITY_DAO.getAvailableUniversities();
        } catch (DAOException exception) {
            Log.getLogger(UniversityTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(result);
        assertEquals(UNIVERSITIES_FOR_TESTING, result);
        tearDown();
    }

}
