package unit.test.InstitutionalRepresentativeDAO;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.university.University;
import org.junit.After;
import static org.junit.Assert.assertEquals;
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
    public void setUp() throws DAOException {
        TEST_HELPER.initializeUniversities();
        auxUniversity = TEST_HELPER.getUniversityOne();
        initializeInstitutionalRepresentatives();
        initializeInstitutionalRepresentative();
    }

    @After
    public void tearDown() throws DAOException {
        for (int i = 0; i < 3; i++) {
            REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(REPRESENTATIVES_FOR_TESTING.get(i));
        }
        REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING);
        TEST_HELPER.deleteAll();

    }

    private void initializeInstitutionalRepresentatives() throws DAOException {
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
            institutionalRepresentative.setIdInstitutionalRepresentative(REPRESENTATIVE_DAO.registerInstitutionalRepresentative(institutionalRepresentative));
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
    public void testRegisterInstitutionalRepresentativeSuccess() throws DAOException {
        int idRepresentative = 0;

        idRepresentative = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING);
        INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(idRepresentative);
        System.out.println(idRepresentative);
        assertTrue(idRepresentative > 0);
    }

    @Test(expected = DAOException.class)
    public void testRegisterInstitutionalRepresentativeFailByEmailDuplicated() throws DAOException {
        INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setEmail(REPRESENTATIVES_FOR_TESTING.get(2).getEmail());
        REPRESENTATIVE_DAO.registerInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING);
    }

    @Test(expected = DAOException.class)
    public void testRegisterInstitutionalRepresentativeFailByNonexistenceUniversity() throws DAOException {
        InstitutionalRepresentative auxInstitutionalRepresentative = new InstitutionalRepresentative();

        auxInstitutionalRepresentative.setName(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.getName());
        auxInstitutionalRepresentative.setPaternalSurname(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.getPaternalSurname());
        auxInstitutionalRepresentative.setMaternalSurname(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.getMaternalSurname());
        auxInstitutionalRepresentative.setPhoneNumber(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.getPhoneNumber());
        auxInstitutionalRepresentative.setEmail(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.getEmail());
        REPRESENTATIVE_DAO.registerInstitutionalRepresentative(auxInstitutionalRepresentative);
    }

    @Test
    public void testDeleteInstitutionalRepresentativeSuccess() throws DAOException {
        int rowsAffected = 0;

        INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(REPRESENTATIVE_DAO.registerInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING));
        rowsAffected = REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING);
        System.out.println(rowsAffected);
        assertTrue(rowsAffected > 0);
    }

    @Test
    public void testUpdateInstitutionalRepresentativeSuccess() throws DAOException {
        String newEmail = "javier2@gmail.com";
        int rowsAffected = 0;

        INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(REPRESENTATIVE_DAO.registerInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING));
        INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setEmail(newEmail);
        rowsAffected = REPRESENTATIVE_DAO.updateInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING);
        System.out.println(rowsAffected);
        assertTrue(rowsAffected > 0);
    }

    @Test(expected = DAOException.class)
    public void testUpdateInstitutionalRepresentativeFailByDuplicatedEmail() throws DAOException {

        INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(REPRESENTATIVE_DAO.registerInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING));
        INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING.setEmail(REPRESENTATIVES_FOR_TESTING.get(2).getEmail());
        REPRESENTATIVE_DAO.updateInstitutionalRepresentative(INSTITUTIONAL_REPRESENTATIVE_FOR_TESTING);
    }

    @Test
    public void testGetAllInstitutionalRepresentativesSuccesss() throws DAOException {
        ArrayList<InstitutionalRepresentative> result = new ArrayList<>();

        result = REPRESENTATIVE_DAO.getAllInstitutionalRepresentatives();
        System.out.println(result);
        assertEquals(REPRESENTATIVES_FOR_TESTING, result);
    }

    @Test
    public void testGetInstitutionalRepresentativeByIdSuccess() throws DAOException {
        InstitutionalRepresentative result = new InstitutionalRepresentative();
        int ForSearch = 1;

        result = REPRESENTATIVE_DAO.getInstitutionalRepresentativeById(REPRESENTATIVES_FOR_TESTING.get(ForSearch).getIdInstitutionalRepresentative());
        System.out.println(result);
        assertEquals(REPRESENTATIVES_FOR_TESTING.get(ForSearch), result);
    }

    @Test
    public void testGetInstitutionalRepresentativeByIdFailByNonexistenceId() throws DAOException {
        InstitutionalRepresentative result = new InstitutionalRepresentative();
        int nonexistenceId = 0;

        result = REPRESENTATIVE_DAO.getInstitutionalRepresentativeById(nonexistenceId);
        System.out.println(result.getIdInstitutionalRepresentative());
        assertTrue(result.getIdInstitutionalRepresentative() == 0);
    }

    @Test
    public void testGetInstitutionalRepresentativeByEmailSuccess() throws DAOException {
        InstitutionalRepresentative result = new InstitutionalRepresentative();
        int ForSearch = 1;

        result = REPRESENTATIVE_DAO.getInstitutionalRepresentativeByEmail(REPRESENTATIVES_FOR_TESTING.get(ForSearch).getEmail());
        System.out.println(result);
        assertEquals(REPRESENTATIVES_FOR_TESTING.get(ForSearch), result);
    }

    @Test
    public void testGetInstitutionalRepresentativeByEmailFailByNonexistenceEmail() throws DAOException {
        InstitutionalRepresentative result = new InstitutionalRepresentative();
        String nonexistenceEmail = "xxxx@gmail.com";

        result = REPRESENTATIVE_DAO.getInstitutionalRepresentativeByEmail(nonexistenceEmail);
        System.out.println(result);
        assertTrue(result.getIdInstitutionalRepresentative() == 0);
    }

}
