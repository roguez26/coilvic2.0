package unit.test.InstitutionalRepresentativeDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;

/**
 *
 * @author ivanr
 */
public class InstitutionalRepresentativeRegistrationTest {

    private static final InstitutionalRepresentativeDAO REPRESENTATIVE_DAO = new InstitutionalRepresentativeDAO();
    private static final InstitutionalRepresentative REPRESENTATIVE_FOR_TESTING = new InstitutionalRepresentative();

    private static final InstitutionalRepresentative AUX_REPRESENTATIVE_FOR_TESTING = new InstitutionalRepresentative();
    
    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final University AUX_UNIVERSITY = new University();
    
    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country AUX_COUNTRY = new Country();

    public InstitutionalRepresentativeRegistrationTest() {

    }

    @Before
    public void setUp() {
        int idRepresentative;
        int idCountry;
        int idUniversity;
        
        initializeAuxiliarCountry();
        intitliazeAuxiliarUniversity();
        initializeAuxiliarRepresentative();
        initializeRepresentative();
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
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
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
        REPRESENTATIVE_FOR_TESTING.setName("Natalia");
        REPRESENTATIVE_FOR_TESTING.setPaternalSurname("Hernandez");
        REPRESENTATIVE_FOR_TESTING.setMaternalSurname(null);
        REPRESENTATIVE_FOR_TESTING.setPhoneNumber("2293846226");
        REPRESENTATIVE_FOR_TESTING.setEmail("natalia@gmail.com");
    }

    private void initializeAuxiliarRepresentative() {
        AUX_REPRESENTATIVE_FOR_TESTING.setName("Carlos");
        AUX_REPRESENTATIVE_FOR_TESTING.setPaternalSurname("Oliva");
        AUX_REPRESENTATIVE_FOR_TESTING.setMaternalSurname("Ramirez");
        AUX_REPRESENTATIVE_FOR_TESTING.setPhoneNumber("2297253222");
        AUX_REPRESENTATIVE_FOR_TESTING.setEmail("Carlos@gmail.com");
    }

    @Test
    public void testRegisterInstitutionalRepresentativeSuccess() {
        int idRepresentative = 0;

        try {
            idRepresentative = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(REPRESENTATIVE_FOR_TESTING);
            REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(idRepresentative);
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(idRepresentative > 0);
    }

    @Test
    public void testRegisterInstitutionalRepresentativeFailByEmailDuplicated() {
        int idRepresentative = 0;

        try {
            REPRESENTATIVE_FOR_TESTING.setEmail(AUX_REPRESENTATIVE_FOR_TESTING.getEmail());
            idRepresentative = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(REPRESENTATIVE_FOR_TESTING);
            
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(idRepresentative > 0);
    }
    
    @Test
    public void testRegisterInstitutionalRepresentativeFailByNonexistenceUniversity() {
        int idRepresentative = 0;
        int idUniversity;
        idUniversity = REPRESENTATIVE_FOR_TESTING.getIdUniversity();
        try {
            REPRESENTATIVE_FOR_TESTING.setIdUniversity(0);
            idRepresentative = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(REPRESENTATIVE_FOR_TESTING);
            
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        REPRESENTATIVE_FOR_TESTING.setIdUniversity(idUniversity);
        assertTrue(idRepresentative > 0);
    }

    @After
    public void tearDown() {
        try {
            REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(REPRESENTATIVE_FOR_TESTING);
            REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(AUX_REPRESENTATIVE_FOR_TESTING);
            UNIVERSITY_DAO.deleteUniversity(AUX_UNIVERSITY.getIdUniversity());
            COUNTRY_DAO.deleteCountry(AUX_COUNTRY.getIdCountry());
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

}
