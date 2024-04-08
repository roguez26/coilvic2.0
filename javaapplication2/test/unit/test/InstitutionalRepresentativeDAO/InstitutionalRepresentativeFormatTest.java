package unit.test.InstitutionalRepresentativeDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentative;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author ivanr
 */
public class InstitutionalRepresentativeFormatTest {

    private static final InstitutionalRepresentative REPRESENTATIVE_FOR_TESTING = new InstitutionalRepresentative();
    private final String NAME = "Natalia";
    private final String PATERNAL_SURNAME = "Hernandez";
    private final String MATERNAL_SURNAME = "Alvarez";
    private final String PHONE_NUMBER = "2293846226";
    private final String EMAIL = "natalia@gmail.com";
    private final int ID_UNIVERSITY = 1;

    public InstitutionalRepresentativeFormatTest() {

    }

    @Before
    public void setUp() {
        initializeRepresentative();
    }

    private void initializeRepresentative() {
        REPRESENTATIVE_FOR_TESTING.setName(NAME);
        REPRESENTATIVE_FOR_TESTING.setPaternalSurname(PATERNAL_SURNAME);
        REPRESENTATIVE_FOR_TESTING.setMaternalSurname(MATERNAL_SURNAME);
        REPRESENTATIVE_FOR_TESTING.setPhoneNumber(PHONE_NUMBER);
        REPRESENTATIVE_FOR_TESTING.setEmail(EMAIL);
        REPRESENTATIVE_FOR_TESTING.setIdUniversity(ID_UNIVERSITY);
    }

    @Test
    public void testValidationEmailSuccess() {
        String validEmail = "emailValido@gmail.com";
        try {
            REPRESENTATIVE_FOR_TESTING.setEmail(validEmail);
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(InstitutionalRepresentativeFormatTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(REPRESENTATIVE_FOR_TESTING.getEmail().equals(validEmail));
    }

    @Test
    public void testValidationEmailFailByTooLong() {
        String tooLongEmail = "ejemplo_de_email_que_es_demasiado_largo_para_aceptar@gmail.com";
        try {
            REPRESENTATIVE_FOR_TESTING.setEmail(tooLongEmail);
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(InstitutionalRepresentativeFormatTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(REPRESENTATIVE_FOR_TESTING.getEmail().equals(tooLongEmail));
    }

    @Test
    public void testValidationEmailFailByTooShort() {
        String tooLongEmail = "@";
        try {
            REPRESENTATIVE_FOR_TESTING.setEmail(tooLongEmail);
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(InstitutionalRepresentativeFormatTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(REPRESENTATIVE_FOR_TESTING.getEmail().equals(tooLongEmail));
    }

    @Test
    public void testValidationEmailFailByMoreThanAtSing() {
        String tooLongEmail = "email@conArroba@.com";
        try {
            REPRESENTATIVE_FOR_TESTING.setEmail(tooLongEmail);
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(InstitutionalRepresentativeFormatTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(REPRESENTATIVE_FOR_TESTING.getEmail().equals(tooLongEmail));
    }
    
    @Test
    public void testValidationEmailFailByNoFormat() {
        String tooLongEmail = "emailNoValido";
        try {
            REPRESENTATIVE_FOR_TESTING.setEmail(tooLongEmail);
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(InstitutionalRepresentativeFormatTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(REPRESENTATIVE_FOR_TESTING.getEmail().equals(tooLongEmail));
    }
    
    @Test
    public void testValidationEmailFailByBlankSpaces() {
        String tooLongEmail = "email noValido@gmail.com";
        try {
            REPRESENTATIVE_FOR_TESTING.setEmail(tooLongEmail);
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(InstitutionalRepresentativeFormatTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(REPRESENTATIVE_FOR_TESTING.getEmail().equals(tooLongEmail));
    }
}
