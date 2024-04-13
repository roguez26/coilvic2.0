package unit.test.InstitutionalRepresentativeDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
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
    private final String NAME = "Javier";
    private final String PATERNAL_SURNAME = "Hernandez";
    private final String MATERNAL_SURNAME = "Hernandez";
    private final String PHONE_NUMBER = "2293445226";
    private final String EMAIL = "javier@gmail.com";
    private final int ID_UNIVERSITY = 1;

    private static final InstitutionalRepresentative AUX_REPRESENTATIVE_FOR_TESTING = new InstitutionalRepresentative();
    private final String AUX_NAME = "Emmanuel";
    private final String AUX_PATERNAL_SURNAME = "Perez";
    private final String AUX_MATERNAL_SURNAME = "Martinez";
    private final String AUX_PHONE_NUMBER = "2233415629";
    private final String AUX_EMAIL = "emmanuel@gmail.com";
    private final int AUX_ID_UNIVERSITY = 1;

    public InstitutionalRepresentativeUpdateTest() {

    }

    @Before
    public void setUp() {
        int representativeId;
        initializeRepresentative();
        initializeAuxiliarRepresentative();
        
        try {
            representativeId = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(REPRESENTATIVE_FOR_TESTING);
            REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(representativeId);
            representativeId = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(AUX_REPRESENTATIVE_FOR_TESTING);
            AUX_REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(representativeId);
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    private void initializeRepresentative() {
        REPRESENTATIVE_FOR_TESTING.setName(NAME);
        REPRESENTATIVE_FOR_TESTING.setPaternalSurname(PATERNAL_SURNAME);
        REPRESENTATIVE_FOR_TESTING.setMaternalSurname(MATERNAL_SURNAME);
        REPRESENTATIVE_FOR_TESTING.setPhoneNumber(PHONE_NUMBER);
        REPRESENTATIVE_FOR_TESTING.setEmail(EMAIL);
        REPRESENTATIVE_FOR_TESTING.setIdUniversity(ID_UNIVERSITY);
    }
    
    private void initializeAuxiliarRepresentative() {
        AUX_REPRESENTATIVE_FOR_TESTING.setName(AUX_NAME);
        AUX_REPRESENTATIVE_FOR_TESTING.setPaternalSurname(AUX_PATERNAL_SURNAME);
        AUX_REPRESENTATIVE_FOR_TESTING.setMaternalSurname(AUX_MATERNAL_SURNAME);
        AUX_REPRESENTATIVE_FOR_TESTING.setPhoneNumber(AUX_PHONE_NUMBER);
        AUX_REPRESENTATIVE_FOR_TESTING.setEmail(AUX_EMAIL);
        AUX_REPRESENTATIVE_FOR_TESTING.setIdUniversity(AUX_ID_UNIVERSITY);
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
        String newEmail = AUX_EMAIL;
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

    @After
    public void tearDown() {
        try {
            REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(REPRESENTATIVE_FOR_TESTING);
            REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(AUX_REPRESENTATIVE_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
}
