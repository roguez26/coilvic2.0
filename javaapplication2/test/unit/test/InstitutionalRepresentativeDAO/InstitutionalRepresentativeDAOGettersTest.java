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
    private static final String[] NAMES = {"Natalia", "Daniel", "Juan"};
    private static final String[] PATERNAL_SURNAMES = {"Hernandez", "Romero", "Mata"};
    private static final String[] MATERNAL_SURNAMES = {"Alvarez", "Cid", "Alba"};
    private static final String[] PHONE_NUMBERS = {"2293846226", "2283948372", "2273820393"};
    private static final String[] EMAILS = {"natalia@gmail.com", "daniel@gmail.com", "juan@gmail.com"};
    private static final int[] REPRESENTATIVE_IDS = {0, 0, 0};

    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final University AUX_UNIVERSITY = new University();
    private static final String AUX_NAME = "Universidad Católica Andrés Bello";
    private static final String AUX_ACRONYM = "UCAB";
    private static final String AUX_JURISDICTION = "Caracas";
    private static final String AUX_CITY = "Guayana";
    
    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country AUX_COUNTRY = new Country();
    private static final String AUX_COUNTRY_NAME = "Mexico";
    
    public InstitutionalRepresentativeDAOGettersTest() {

    }

    @Before
    public void setUp() {
        int idCountry;
        int idUniversity;
        
        AUX_COUNTRY.setName(AUX_COUNTRY_NAME);
        intitliazeAuxiliarUniversity();
        initializeRepresentative();
        
        try {
            idCountry = COUNTRY_DAO.registerCountry(AUX_COUNTRY);
            AUX_COUNTRY.setIdCountry(idCountry);
            AUX_UNIVERSITY.setIdCountry(idCountry);
            idUniversity = UNIVERSITY_DAO.registerUniversity(AUX_UNIVERSITY);
            AUX_UNIVERSITY.setIdUniversity(idUniversity);
            for (int i = 0; i < REPRESENTATIVES_FOR_TESTING.size(); i++) {
                REPRESENTATIVES_FOR_TESTING.get(i).setIdUniversity(idUniversity);
                REPRESENTATIVE_IDS[i] = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(REPRESENTATIVES_FOR_TESTING.get(i));
                REPRESENTATIVES_FOR_TESTING.get(i).setIdInstitutionalRepresentative(REPRESENTATIVE_IDS[i]);
            }
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    private void initializeRepresentative() {
        for (int i = 0; i < 3; i++) {
            InstitutionalRepresentative institutionalRepresentative = new InstitutionalRepresentative();
            institutionalRepresentative.setName(NAMES[i]);
            institutionalRepresentative.setPaternalSurname(PATERNAL_SURNAMES[i]);
            institutionalRepresentative.setMaternalSurname(MATERNAL_SURNAMES[i]);
            institutionalRepresentative.setPhoneNumber(PHONE_NUMBERS[i]);
            institutionalRepresentative.setEmail(EMAILS[i]);
            REPRESENTATIVES_FOR_TESTING.add(institutionalRepresentative);
        }
    }
    
    private void intitliazeAuxiliarUniversity() {
        AUX_UNIVERSITY.setName(AUX_NAME);
        AUX_UNIVERSITY.setAcronym(AUX_ACRONYM);
        AUX_UNIVERSITY.setJurisdiction(AUX_JURISDICTION);
        AUX_UNIVERSITY.setCity(AUX_CITY);
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
