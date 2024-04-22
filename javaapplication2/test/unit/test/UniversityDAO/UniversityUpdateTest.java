package unit.test.UniversityDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 *
 * @author ivanr
 */
public class UniversityUpdateTest {

    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final University UNIVERSITY_FOR_TESTING = new University();
    private static final String NAME = "Universidad Veracruzana";

    private static final University AUX_UNIVERSITY_FOR_TESTING = new University();

    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country AUX_COUNTRY = new Country();

    public UniversityUpdateTest() {

    }

    @Before
    public void setUp() {
        int idUniversity;
        int idCountry;
        initializeAuxiliarCountry();
        intitliazeAuxiliarUniversity();
        intitliazeUniversity();

        try {
            idCountry = COUNTRY_DAO.registerCountry(AUX_COUNTRY);
            AUX_COUNTRY.setIdCountry(idCountry);
            AUX_UNIVERSITY_FOR_TESTING.setIdCountry(idCountry);
            UNIVERSITY_FOR_TESTING.setIdCountry(idCountry);
            idUniversity = UNIVERSITY_DAO.registerUniversity(AUX_UNIVERSITY_FOR_TESTING);
            AUX_UNIVERSITY_FOR_TESTING.setIdUniversity(idUniversity);
            idUniversity = UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING);
            UNIVERSITY_FOR_TESTING.setIdUniversity(idUniversity);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    private void initializeAuxiliarCountry() {
        AUX_COUNTRY.setName("Mexico");
    }

    private void intitliazeUniversity() {
        UNIVERSITY_FOR_TESTING.setName("Universidad Veracruzana");
        UNIVERSITY_FOR_TESTING.setAcronym("UV");
        UNIVERSITY_FOR_TESTING.setJurisdiction("Veracruz");
        UNIVERSITY_FOR_TESTING.setCity("Xalapa");
        UNIVERSITY_FOR_TESTING.setCountry(AUX_COUNTRY);
    }

    private void intitliazeAuxiliarUniversity() {
        AUX_UNIVERSITY_FOR_TESTING.setName("Universidad Católica Andrés Bello");
        AUX_UNIVERSITY_FOR_TESTING.setAcronym("UCAB");
        AUX_UNIVERSITY_FOR_TESTING.setJurisdiction("Caracas");
        AUX_UNIVERSITY_FOR_TESTING.setCity("Guayana");
        AUX_UNIVERSITY_FOR_TESTING.setCountry(AUX_COUNTRY);
    }

    @Test
    public void testUpdateUniversitySuccess() {
        String newName = "universidad V";
        int result = 0;

        UNIVERSITY_FOR_TESTING.setName(newName);
        try {
            result = UNIVERSITY_DAO.updateUniversity(UNIVERSITY_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result > 0);
    }

    @Test
    public void testUpdateUniversityFailByDuplicatedName() {
        String newName = AUX_UNIVERSITY_FOR_TESTING.getName();
        int result = 0;

        UNIVERSITY_FOR_TESTING.setName(newName);
        try {
            result = UNIVERSITY_DAO.updateUniversity(UNIVERSITY_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(result > 0);
    }

    @After
    public void tearDown() {
        try {
            UNIVERSITY_DAO.deleteUniversity(UNIVERSITY_FOR_TESTING.getIdUniversity());
            UNIVERSITY_DAO.deleteUniversity(AUX_UNIVERSITY_FOR_TESTING.getIdUniversity());
            COUNTRY_DAO.deleteCountry(AUX_COUNTRY.getIdCountry());
        } catch (DAOException exception) {
            Logger.getLogger(UniversityUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
    }
}
