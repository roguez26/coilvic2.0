package unit.test.InstitutionalRepresentativeDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentativeDAO;
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
public class InstitutionalRepresentativeUpdateTest {

    private static final InstitutionalRepresentativeDAO REPRESENTATIVE_DAO = new InstitutionalRepresentativeDAO();
    private static final InstitutionalRepresentative REPRESENTATIVE_FOR_TESTING = new InstitutionalRepresentative();

    private static final InstitutionalRepresentative AUX_REPRESENTATIVE_FOR_TESTING = new InstitutionalRepresentative();
    
    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final University AUX_UNIVERSITY = new University();

    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country AUX_COUNTRY = new Country();

    public InstitutionalRepresentativeUpdateTest() {

    }

    @Before
    public void setUp() {
        int idRepresentative;
        int idCountry;
        int idUniversity;
        initializeAuxiliarCountry();
        intitliazeAuxiliarUniversity();
        initializeRepresentative();
        initializeAuxiliarRepresentative();
        
        try {
            idCountry = COUNTRY_DAO.registerCountry(AUX_COUNTRY);
            AUX_COUNTRY.setIdCountry(idCountry);
            AUX_UNIVERSITY.setCountry(AUX_COUNTRY);
            idUniversity = UNIVERSITY_DAO.registerUniversity(AUX_UNIVERSITY);
            AUX_UNIVERSITY.setIdUniversity(idUniversity);
            AUX_REPRESENTATIVE_FOR_TESTING.setUniversity(AUX_UNIVERSITY);
            REPRESENTATIVE_FOR_TESTING.setUniversity(AUX_UNIVERSITY);
            idRepresentative = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(AUX_REPRESENTATIVE_FOR_TESTING);
            AUX_REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(idRepresentative);
            idRepresentative = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(REPRESENTATIVE_FOR_TESTING);
            REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(idRepresentative);
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
    
    private void initializeAuxiliarCountry() {
        AUX_COUNTRY.setName("Mexico");
    }
    
    private void intitliazeAuxiliarUniversity() {
        AUX_UNIVERSITY.setName("Universidad Veracruzana");
        AUX_UNIVERSITY.setAcronym("UV");
        AUX_UNIVERSITY.setJurisdiction("Veracruz");
        AUX_UNIVERSITY.setCity("Xalapa");
    }

    private void initializeRepresentative() {
        REPRESENTATIVE_FOR_TESTING.setName("Javier");
        REPRESENTATIVE_FOR_TESTING.setPaternalSurname("Hernandez");
        REPRESENTATIVE_FOR_TESTING.setMaternalSurname("Hernandez");
        REPRESENTATIVE_FOR_TESTING.setPhoneNumber("2293445226");
        REPRESENTATIVE_FOR_TESTING.setEmail("javier@gmail.com");
    }
    
    private void initializeAuxiliarRepresentative() {
        AUX_REPRESENTATIVE_FOR_TESTING.setName("Emmanuel");
        AUX_REPRESENTATIVE_FOR_TESTING.setPaternalSurname("Perez");
        AUX_REPRESENTATIVE_FOR_TESTING.setMaternalSurname("Martinez");
        AUX_REPRESENTATIVE_FOR_TESTING.setPhoneNumber("2233415629");
        AUX_REPRESENTATIVE_FOR_TESTING.setEmail("emmanuel@gmail.com");
    }

    @Test
    public void testUpdateInstitutionalRepresentativeSuccess() {
        String newEmail = "javier2@gmail.com";
        int result = 0;

        REPRESENTATIVE_FOR_TESTING.setEmail(newEmail);
        try {
            result = REPRESENTATIVE_DAO.updateInstitutionalRepresentative(REPRESENTATIVE_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(result > 0);
    }

    @Test
    public void testUpdateInstitutionalRepresentativeFailByDuplicatedEmail() {
        int result = 0;

        REPRESENTATIVE_FOR_TESTING.setEmail(AUX_REPRESENTATIVE_FOR_TESTING.getEmail());
        try {
            result = REPRESENTATIVE_DAO.updateInstitutionalRepresentative(REPRESENTATIVE_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(result > 0);
    }

    @After
    public void tearDown() {
        try {
            REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(REPRESENTATIVE_FOR_TESTING);
            REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(AUX_REPRESENTATIVE_FOR_TESTING);
            UNIVERSITY_DAO.deleteUniversity(AUX_UNIVERSITY.getIdUniversity());
            COUNTRY_DAO.deleteCountry(AUX_COUNTRY.getIdCountry());
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
}
