package unit.test.InstitutionalRepresentativeDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentative;
import java.util.ArrayList;

/**
 *
 * @author ivanr
 */
public class InstitutionalRepresentativeDAOGettersTest {

    private static final InstitutionalRepresentativeDAO REPRESENTATIVE_DAO = new InstitutionalRepresentativeDAO();
    private static final ArrayList<InstitutionalRepresentative> REPRESENTATIVES_FOR_TESTING = new ArrayList<>();
    private static final ArrayList<InstitutionalRepresentative> AUX_REPRESENTATIVES_FOR_TESTING = new ArrayList<>();
    private static final String[] NAMES = {"Natalia", "Daniel", "Juan"};
    private static final String[] PATERNAL_SURNAMES = {"Hernandez", "Romero", "Mata"};
    private static final String[] MATERNAL_SURNAMES = {"Alvarez", "Cid", "Alba"};
    private static final String[] PHONE_NUMBERS = {"2293846226", "2283948372", "2273820393"};
    private static final String[] EMAILS = {"natalia@gmail.com", "daniel@gmail.com", "juan@gmail.com"};
    private static final int[] REPRESENTATIVE_IDS = {0, 0, 0};
    private static final int ID_UNIVERSITY = 1;

    public InstitutionalRepresentativeDAOGettersTest() {

    }

    @Before
    public void setUp() {
        try {
            initializeRepresentative();
            for (int i = 0; i < AUX_REPRESENTATIVES_FOR_TESTING.size(); i++) {
                REPRESENTATIVE_IDS[i] = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(AUX_REPRESENTATIVES_FOR_TESTING.get(i));
                AUX_REPRESENTATIVES_FOR_TESTING.get(i).setIdInstitutionalRepresentative(REPRESENTATIVE_IDS[i]);
                REPRESENTATIVES_FOR_TESTING.add(AUX_REPRESENTATIVES_FOR_TESTING.get(i));
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
            institutionalRepresentative.setIdUniversity(ID_UNIVERSITY);
            AUX_REPRESENTATIVES_FOR_TESTING.add(institutionalRepresentative);
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
        }
        assertNotNull(result);
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
        String nonexistenceEmail = "xx@gmail.com";
        
        try {
            result = REPRESENTATIVE_DAO.getInstitutionalRepresentativeByEmail(nonexistenceEmail);            
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertNotNull(result);
    }

    @After
    public void tearDown() {
        try {
            for (int i = 0; i < 3; i++) {
                REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(REPRESENTATIVES_FOR_TESTING.get(i));
            }
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);

        }
    }

}
