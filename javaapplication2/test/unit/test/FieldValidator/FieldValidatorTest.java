package unit.test.FieldValidator;

import mx.fei.coilvicapp.logic.implementations.FieldValidator;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import org.junit.Test;

/**
 *
 * @author ivanr
 */
public class FieldValidatorTest {

    private static final FieldValidator FIELD_VALIDATOR = new FieldValidator();

    public FieldValidatorTest() {

    }

    @Test
    public void testCheckNameSuccess() {
        String validName = "Alejandro A-";
        IllegalArgumentException illegalArgumentException = null;

        try {
            FIELD_VALIDATOR.checkName(validName);
        } catch (IllegalArgumentException exception) {
            illegalArgumentException = exception;
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(illegalArgumentException == null);
    }
    
    @Test
    public void testCheckNameFailByNoAllowedSignsAtTheEnd() {
        String invalidName = "Alejandro A-"; 
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FIELD_VALIDATOR.checkName(invalidName));
        System.out.println(exception.getMessage());
    }
    
    @Test
    public void testCheckNameFailByNoAllowedSigns() {
        String invalidName = "Alej4ndro"; 
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FIELD_VALIDATOR.checkName(invalidName));
        System.out.println(exception.getMessage());
    }
    
    @Test
    public void testCheckNameFailByRepeatedCharacters() {
        String invalidName = "Diannnna"; 
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FIELD_VALIDATOR.checkName(invalidName));
        System.out.println(exception.getMessage());
    }
    
    @Test
    public void testCheckNameFailByRepeatedSigns() {
        String invalidName = "Tomás-. Marcos"; 
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FIELD_VALIDATOR.checkName(invalidName));
        System.out.println(exception.getMessage());
    }

    @Test
    public void testCheckNameFailByNull() {
        String nullName = null;
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkName(nullName);
            result = true;
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result);
    }

    @Test
    public void testCheckPhoneNumberSuccess() {
        String validPhoneNumber = "2293849284";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkPhoneNumber(validPhoneNumber);
            result = true;
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result);
    }

    @Test
    public void testCheckEmailSuccess() {
        String validEmail = "stancoop@gmail.com";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkEmail(validEmail);
            result = true;
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result);
    }

    @Test
    public void testCheckNameFailByTooShort() {
        String tooShortName = "a";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkName(tooShortName);
            result = true;
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result);
    }

    @Test
    public void testCheckNameFailByTooLong() {
        String tooLongName = "ejemplo de nombre que puede resultar excesivamente largo";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkName(tooLongName);
            result = true;
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result);
    }

    @Test
    public void testCheckNameFailBySigns() {
        String NameWithSigns = "ejemplo.de#nombre'con\simbolos?3!";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkName(NameWithSigns);
            result = true;
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result);
    }

    @Test
    public void testCheckNameFailByConsecutivelyBlankSpaces() {
        String NameWithBlankSpaces = "nombre con mas de un espacios  seguido";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkName(NameWithBlankSpaces);
            result = true;
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result);
    }

    @Test
    public void testCheckNameFailByOnlyBlankSpaces() {
        String NameWithOnlySpaces = "      ";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkName(NameWithOnlySpaces);
            result = true;
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result);
    }

    @Test
    public void testCheckPhoneNumberFailByChars() {
        String validPhoneNumber = "234dostres";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkPhoneNumber(validPhoneNumber);
            result = true;
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result);
    }

    @Test
    public void testCheckPhoneNumberFailBySigns() {
        String PhoneNumberWithSigns = "23839284/3";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkPhoneNumber(PhoneNumberWithSigns);
            result = true;
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result);
    }

    @Test
    public void testCheckPhoneNumberFailByTooLong() {
        String tooLongPhoneNumber = "238392843373839383";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkPhoneNumber(tooLongPhoneNumber);
            result = true;
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result);
    }

    @Test
    public void testCheckPhoneNumberFailByTooShort() {
        String PhoneNumberWithSpaces = "23 82 72 93 28";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkPhoneNumber(PhoneNumberWithSpaces);
            result = true;
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result);
    }

    @Test
    public void testCheckEmailFailByTooLong() {
        String tooLongEmail = "ejemplo_de_correo_que_puede_ser_demasiado_largo@gmail.com";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkEmail(tooLongEmail);
            result = true;
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result);
    }

    @Test
    public void testCheckEmailFailByTooShort() {
        String toShortEmail = "@";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkEmail(toShortEmail);
            result = true;
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result);
    }

    @Test
    public void testCheckEmailFailByNoDomain() {
        String EmailWithoutDomain = "mi_correo@";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkEmail(EmailWithoutDomain);
            result = true;
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result);
    }

    @Test
    public void testCheckEmailFailByMoreThanOneAtSing() {
        String EmailWithMoreThanAtSing = "correo_ejemplo@@gmail.com";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkEmail(EmailWithMoreThanAtSing);
            result = true;
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result);
    }

    @Test
    public void testCheckEmailFailByNoAtSing() {
        String EmailWithoutAtSign = "correo_ejemplogmail.com";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkEmail(EmailWithoutAtSign);
            result = true;
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result);
    }

    @Test
    public void testCheckEmailFailByBlankSpaces() {
        String EmailWithBlankSpaces = "correo ejemplo@gmail.com";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkEmail(EmailWithBlankSpaces);
            result = true;
        } catch (IllegalArgumentException exception) {
            Logger.getLogger(FieldValidatorTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result);
    }

}
