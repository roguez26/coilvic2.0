package unit.test.InstitutionalRepresentativeDAO;

import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.university.University;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import unit.test.Initializer.TestHelper;

public class InstitutionalRepresentativeTest {

    private University auxUniversity;

    private final InstitutionalRepresentativeDAO REPRESENTATIVE_DAO = new InstitutionalRepresentativeDAO();
    private final ArrayList<InstitutionalRepresentative> REPRESENTATIVES_FOR_TESTING = new ArrayList<>();
    private final InstitutionalRepresentative INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING = new InstitutionalRepresentative();

    private final TestHelper TEST_HELPER = new TestHelper();

    @Before
    public void setUp() {
        TEST_HELPER.initializeUniversities();
        auxUniversity = TEST_HELPER.getUniversityOne();
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
            TEST_HELPER.deleteAll();
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
            institutionalRepresentative.setUniversity(auxUniversity);
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
        INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setUniversity(auxUniversity);
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
        System.out.println(idRepresentative);
        assertTrue(idRepresentative > 0);
    }

    @Test
    public void testRegisterInstitutionalRepresentativeFailByEmailDuplicated() {
        INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setEmail(REPRESENTATIVES_FOR_TESTING.get(2).getEmail());
        DAOException exception = assertThrows(DAOException.class, () -> REPRESENTATIVE_DAO.registerInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING));
        System.out.println(exception.getMessage());
    }

    @Test
    public void testRegisterInstitutionalRepresentativeFailByNonexistenceUniversity() {
        int idUniversity = 0;
        idUniversity = INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.getIdUniversity();
        INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setIdUniversity(0);
        DAOException exception = assertThrows(DAOException.class, ()-> REPRESENTATIVE_DAO.registerInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING));
        System.out.println(exception.getMessage());
        INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setIdUniversity(idUniversity);
    }

    @Test
    public void testDeleteInstitutionalRepresentativeSuccess() {
        int rowsAffected = 0;
        try {
            INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(REPRESENTATIVE_DAO.registerInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING));
            rowsAffected = REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING);
        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(rowsAffected);
        assertTrue(rowsAffected > 0);
    }

    @Test
    public void testUpdateInstitutionalRepresentativeSuccess() {
        String newEmail = "javier2@gmail.com";
        int rowsAffected = 0;

        try {
            INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(REPRESENTATIVE_DAO.registerInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING));
            INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setEmail(newEmail);
            rowsAffected = REPRESENTATIVE_DAO.updateInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING);
        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(rowsAffected);
        assertTrue(rowsAffected > 0);
    }

    @Test
    public void testUpdateInstitutionalRepresentativeFailByDuplicatedEmail() {
        try {
            INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(REPRESENTATIVE_DAO.registerInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING));
            INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setEmail(REPRESENTATIVES_FOR_TESTING.get(2).getEmail());
        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
        }
        
        DAOException exception = assertThrows(DAOException.class, ()-> REPRESENTATIVE_DAO.updateInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING));
        System.out.println(exception.getMessage());
    }

    @Test
    public void testGetAllInstitutionalRepresentativesSuccesss() {
        ArrayList<InstitutionalRepresentative> result = new ArrayList<>();

        try {
            result = REPRESENTATIVE_DAO.getAllInstitutionalRepresentatives();
        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(result);
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
        System.out.println(result);
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
        }
        System.out.println(result.getIdInstitutionalRepresentative());
        assertTrue(result.getIdInstitutionalRepresentative() == 0);
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
        System.out.println(result);
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
        System.out.println(result);
        assertTrue(result.getIdInstitutionalRepresentative() == 0);
    }

}
