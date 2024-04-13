package unit.test.InstitutionalRepresentativeDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.implementations.DAOException;

/**
 *
 * @author ivanr
 */
public class InstitutionalRepresentativeRegistrationTest {

    private static final InstitutionalRepresentativeDAO REPRESENTATIVE_DAO = new InstitutionalRepresentativeDAO();
    private static final InstitutionalRepresentative REPRESENTATIVE_FOR_TESTING = new InstitutionalRepresentative();
    private static final String NAME = "Natalia";
    private static final String PATERNAL_SURNAME = "Hernandez";
    private static final String MATERNAL_SURNAME = null;
    private static final String PHONE_NUMBER = "2293846226";
    private static final String EMAIL = "natalia@gmail.com";
    private static final int ID_UNIVERSITY = 1;

    private static final InstitutionalRepresentative AUX_REPRESENTATIVE_FOR_TESTING = new InstitutionalRepresentative();
    private static final String AUX_NAME = "Carlos";
    private static final String AUX_PATERNAL_SURNAME = "Oliva";
    private static final String AUX_MATERNAL_SURNAME = "Ramirez";
    private static final String AUX_PHONE_NUMBER = "2297253222";
    private static final String AUX_EMAIL = "Carlos@gmail.com";
    private static final int AUX_ID_UNIVERSITY = 1;

    public InstitutionalRepresentativeRegistrationTest() {

    }

    @Before
    public void setUp() {
        try {
            initializeRepresentative();
            initializeAuxiliarRepresentative();
            int idRepresentative = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(AUX_REPRESENTATIVE_FOR_TESTING);
            AUX_REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(idRepresentative);
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
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
            REPRESENTATIVE_FOR_TESTING.setEmail(AUX_EMAIL);
            idRepresentative = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(REPRESENTATIVE_FOR_TESTING);
            
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(idRepresentative > 0);
    }

    @After
    public void tearDown() {
        try {
            REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(REPRESENTATIVE_FOR_TESTING);
            REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(AUX_REPRESENTATIVE_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

}
