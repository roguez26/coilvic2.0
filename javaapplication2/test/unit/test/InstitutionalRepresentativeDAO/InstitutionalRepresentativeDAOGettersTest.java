package unit.test.InstitutionalRepresentativeDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentative;
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;

/**
 *
 * @author ivanr
 */
public class InstitutionalRepresentativeDAOGettersTest {

    private static final InstitutionalRepresentativeDAO REPRESENTATIVE_DAO = new InstitutionalRepresentativeDAO();
    private static final ArrayList<InstitutionalRepresentative> REPRESENTATIVES_FOR_TESTING = new ArrayList<>();

    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final University AUX_UNIVERSITY = new University();

    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country AUX_COUNTRY = new Country();

    public InstitutionalRepresentativeDAOGettersTest() {

    }

    @Before
    public void setUp() {
        int idCountry;
        int idUniversity;
        int idInstitutionalRepresentative;

        initializeCountry();
        intitliazeUniversity();
        initializeInstitutionalRepresentatives();

        try {
            idCountry = COUNTRY_DAO.registerCountry(AUX_COUNTRY);
            AUX_COUNTRY.setIdCountry(idCountry);
            AUX_UNIVERSITY.setCountry(AUX_COUNTRY);
            idUniversity = UNIVERSITY_DAO.registerUniversity(AUX_UNIVERSITY);
            AUX_UNIVERSITY.setIdUniversity(idUniversity);
            for (int i = 0; i < 3; i++) {
                REPRESENTATIVES_FOR_TESTING.get(i).setUniversity(AUX_UNIVERSITY);
                idInstitutionalRepresentative = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(REPRESENTATIVES_FOR_TESTING.get(i));
                REPRESENTATIVES_FOR_TESTING.get(i).setIdInstitutionalRepresentative(idInstitutionalRepresentative);
            }
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    private void initializeCountry() {
        AUX_COUNTRY.setName("Mexico");
    }

    private void intitliazeUniversity() {
        AUX_UNIVERSITY.setName("Universidad Veracruzana");
        AUX_UNIVERSITY.setAcronym("UV");
        AUX_UNIVERSITY.setJurisdiction("Veracruz");
        AUX_UNIVERSITY.setCity("Xalapa");
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
            REPRESENTATIVES_FOR_TESTING.add(institutionalRepresentative);
        }
    }

    @Test
    public void testGetAllInstitutionalRepresentativesSuccesss() {
        ArrayList<InstitutionalRepresentative> result = new ArrayList<>();

        try {
            result = REPRESENTATIVE_DAO.getAllInstitutionalRepresentatives();
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
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
            Logger.getLogger(InstitutionalRepresentativeDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
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
            Logger.getLogger(InstitutionalRepresentativeDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
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
            Logger.getLogger(InstitutionalRepresentativeDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
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
            Logger.getLogger(InstitutionalRepresentativeDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result.getIdInstitutionalRepresentative() > 0);
    }

    @After
    public void tearDown() {
        try {
            for (int i = 0; i < 3; i++) {
                REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(REPRESENTATIVES_FOR_TESTING.get(i));
            }
            UNIVERSITY_DAO.deleteUniversity(AUX_UNIVERSITY.getIdUniversity());
            COUNTRY_DAO.deleteCountry(AUX_COUNTRY.getIdCountry());
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

}
