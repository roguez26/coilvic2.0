package unit.test.InstitutionalRepresentativeDAO;

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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ivanr
 */
public class InstitutionalRepresentativeTest {

    private static final InstitutionalRepresentativeDAO REPRESENTATIVE_DAO = new InstitutionalRepresentativeDAO();
    private static final ArrayList<InstitutionalRepresentative> REPRESENTATIVES_FOR_TESTING = new ArrayList<>();
    private static final InstitutionalRepresentative INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING = new InstitutionalRepresentative();

    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final University AUX_UNIVERSITY = new University();

    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country AUX_COUNTRY = new Country();

    @Before
    public void setUp() {
        initializeCountry();
        intitliazeUniversity();
        initializeInstitutionalRepresentatives();
        initializeInstitutionalRepresentative();
    }

    @After
    public void tearDown() {
        try {
            for (int i = 0; i < 3; i++) {
                REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(REPRESENTATIVES_FOR_TESTING.get(i));
            }
            REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING);
            UNIVERSITY_DAO.deleteUniversity(AUX_UNIVERSITY.getIdUniversity());
            COUNTRY_DAO.deleteCountry(AUX_COUNTRY.getIdCountry());
        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
        }
    }

    private void initializeCountry() {
        AUX_COUNTRY.setName("Mexico");

        try {
            AUX_COUNTRY.setIdCountry(COUNTRY_DAO.registerCountry(AUX_COUNTRY));
        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
        }
    }

    private void intitliazeUniversity() {
        AUX_UNIVERSITY.setName("Universidad Veracruzana");
        AUX_UNIVERSITY.setAcronym("UV");
        AUX_UNIVERSITY.setJurisdiction("Veracruz");
        AUX_UNIVERSITY.setCity("Xalapa");
        AUX_UNIVERSITY.setCountry(AUX_COUNTRY);

        try {
            AUX_UNIVERSITY.setIdUniversity(UNIVERSITY_DAO.registerUniversity(AUX_UNIVERSITY));
        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
        }
    }

    private void initializeInstitutionalRepresentatives() {
        String[] names = {"Natalia", "Daniel", "Juan"};
        String[] paternalSurnames = {"Hernandez", "Romero", "Mata"};
        String[] maternalSurnames = {"Alvarez", "Cid", "Alba"};
        String[] phoneNumber = {"2293846226", "2283948372", "2273820393"};
        String[] emails = {"natalia@gmail.com", "daniel@gmail.com", "juan@gmail.com"};

        for (int i = 0; i < 3; i++) {
            InstitutionalRepresentative institutionalRepresentative = new InstitutionalRepresentative();
            institutionalRepresentative.setName(names[i]);
            institutionalRepresentative.setPaternalSurname(paternalSurnames[i]);
            institutionalRepresentative.setMaternalSurname(maternalSurnames[i]);
            institutionalRepresentative.setPhoneNumber(phoneNumber[i]);
            institutionalRepresentative.setEmail(emails[i]);
            institutionalRepresentative.setUniversity(AUX_UNIVERSITY);
            try {
                institutionalRepresentative.setIdInstitutionalRepresentative(REPRESENTATIVE_DAO.registerInstitutionalRepresentative(institutionalRepresentative));
            } catch (DAOException exception) {
                Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
            }
            REPRESENTATIVES_FOR_TESTING.add(institutionalRepresentative);
        }
    }

    private void initializeInstitutionalRepresentative() {
        INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setName("Carlos");
        INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setPaternalSurname("Oliva");
        INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setMaternalSurname("Ramirez");
        INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setPhoneNumber("2297253222");
        INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setEmail("Carlos@gmail.com");
        INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setUniversity(AUX_UNIVERSITY);
    }

    @Test
    public void testRegisterInstitutionalRepresentativeSuccess() {
        int idRepresentative = 0;

        try {
            idRepresentative = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING);
            INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(idRepresentative);
        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(idRepresentative > 0);
    }

    @Test
    public void testRegisterInstitutionalRepresentativeFailByEmailDuplicated() {
        int idRepresentative = 0;

        try {
            INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setEmail(REPRESENTATIVES_FOR_TESTING.get(2).getEmail());
            idRepresentative = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING);

        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(idRepresentative > 0);
    }

    @Test
    public void testRegisterInstitutionalRepresentativeFailByNonexistenceUniversity() {
        int idRepresentative = 0;
        int idUniversity;
        idUniversity = INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.getIdUniversity();
        try {
            INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setIdUniversity(0);
            idRepresentative = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING);

        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
            System.out.println(exception.getMessage());
        }
        INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setIdUniversity(idUniversity);
        assertTrue(idRepresentative > 0);
    }

    @Test
    public void testDeleteInstitutionalRepresentativeSuccess() {
        int result = 0;
        try {
            INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(REPRESENTATIVE_DAO.registerInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING));
            result = REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING);
        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result > 0);
    }

    @Test
    public void testUpdateInstitutionalRepresentativeSuccess() {
        String newEmail = "javier2@gmail.com";
        int result = 0;

        try {
            INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(REPRESENTATIVE_DAO.registerInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING));
            INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setEmail(newEmail);
            result = REPRESENTATIVE_DAO.updateInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING);
        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(result > 0);
    }

    @Test
    public void testUpdateInstitutionalRepresentativeFailByDuplicatedEmail() {
        int result = 0;

        try {
            INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(REPRESENTATIVE_DAO.registerInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING));
            INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setEmail(REPRESENTATIVES_FOR_TESTING.get(2).getEmail());
            result = REPRESENTATIVE_DAO.updateInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING);
        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(result > 0);
    }
    
    @Test
    public void testGetAllInstitutionalRepresentativesSuccesss() {
        ArrayList<InstitutionalRepresentative> result = new ArrayList<>();

        try {
            result = REPRESENTATIVE_DAO.getAllInstitutionalRepresentatives();
        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
        }
        assertEquals(REPRESENTATIVES_FOR_TESTING, result);
    }

    @Test
    public void testGetInstitutionalRepresentativeByIdSuccess() {
        InstitutionalRepresentative result = new InstitutionalRepresentative();
        int ForSearch = 1;

        try {
            result = REPRESENTATIVE_DAO.getInstitutionalRepresentativeById(REPRESENTATIVES_FOR_TESTING.get(ForSearch).getIdInstitutionalRepresentative());
        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
        }
        assertEquals(REPRESENTATIVES_FOR_TESTING.get(ForSearch), result);
    }

    @Test
    public void testGetInstitutionalRepresentativeByIdFailByNonexistenceId() {
        InstitutionalRepresentative result = new InstitutionalRepresentative();
        int nonexistenceId = 0;

        try {
            result = REPRESENTATIVE_DAO.getInstitutionalRepresentativeById(nonexistenceId);
        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(result.getIdInstitutionalRepresentative() > 0);
    }

    @Test
    public void testGetInstitutionalRepresentativeByEmailSuccess() {
        InstitutionalRepresentative result = new InstitutionalRepresentative();
        int ForSearch = 1;

        try {
            result = REPRESENTATIVE_DAO.getInstitutionalRepresentativeByEmail(REPRESENTATIVES_FOR_TESTING.get(ForSearch).getEmail());
        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
        }
        assertEquals(REPRESENTATIVES_FOR_TESTING.get(ForSearch), result);
    }

    @Test
    public void testGetInstitutionalRepresentativeByEmailFailByNonexistenceEmail() {
        InstitutionalRepresentative result = new InstitutionalRepresentative();
        String nonexistenceEmail = "xxxx@gmail.com";

        try {
            result = REPRESENTATIVE_DAO.getInstitutionalRepresentativeByEmail(nonexistenceEmail);
        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result.getIdInstitutionalRepresentative() > 0);
    }

}
