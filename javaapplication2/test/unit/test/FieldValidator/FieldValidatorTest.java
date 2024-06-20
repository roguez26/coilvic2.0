package unit.test.FieldValidator;

import mx.fei.coilvicapp.logic.implementations.FieldValidator;
import log.Log;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import org.junit.Test;

public class FieldValidatorTest {

    private static final FieldValidator FIELD_VALIDATOR = new FieldValidator();

    public FieldValidatorTest() {

    }

    @Test
    public void testCheckNameSuccess() {
        String validName = "D. Alejandro";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkName(validName);
            result = true;
        } catch (IllegalArgumentException exception) {
            Log.getLogger(FieldValidatorTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(result);
        assertTrue(result);
    }

    @Test
    public void testCheckNameFailByNoAllowedSigns() {
        String invalidName = "Alej4ndro";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
                FIELD_VALIDATOR.checkName(invalidName));
        System.out.println(exception.getMessage());
    }

    @Test
    public void testCheckNameFailByRepeatedCharacters() {
        String invalidName = "Diannnna";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
                FIELD_VALIDATOR.checkName(invalidName));
        System.out.println(exception.getMessage());
    }

    @Test
    public void testCheckNameFailByRepeatedSigns() {
        String invalidName = "Tomás-. Marcos";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
                FIELD_VALIDATOR.checkName(invalidName));
        System.out.println(exception.getMessage());
    }

    @Test
    public void testCheckNameFailByTooLong() {
        String invalidName = "Nombre que es excesivamente largo para el campo";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
                FIELD_VALIDATOR.checkName(invalidName));
        System.out.println(exception.getMessage());
    }

    @Test
    public void testCheckNameFailByTooShort() {
        String invalidName = "";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
                FIELD_VALIDATOR.checkName(invalidName));
        System.out.println(exception.getMessage());
    }

    
    
    
    
    
    
    @Test
    public void testCheckPhoneNumberSuccess() {
        String validPhoneNumber = "2293849284";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkPhoneNumber(validPhoneNumber);
            result = true;
        } catch (IllegalArgumentException exception) {
            Log.getLogger(FieldValidatorTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(result);
        assertTrue(result);
    }

    @Test
    public void testCheckPhoneNumberFailByTooLong() {
        String tooLongPhoneNumber = "238392843373839383";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FIELD_VALIDATOR.checkPhoneNumber(tooLongPhoneNumber));
        System.out.println(exception.getMessage());
    }

    @Test
    public void testCheckPhoneNumberFailByTooShort() {
        String tooShortPhoneNumber = "2";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FIELD_VALIDATOR.checkPhoneNumber(tooShortPhoneNumber));
        System.out.println(exception.getMessage());
    }

    @Test
    public void testCheckPhoneNumberFailByNoAllowedSigns() {
        String phoneNumberWithSigns = "227384#894";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FIELD_VALIDATOR.checkPhoneNumber(phoneNumberWithSigns));
        System.out.println(exception.getMessage());
    }

    
    
    
    
    
    
    
    @Test
    public void testCheckEmailSuccess() {
        String validEmail = "coilvicapp@gmail.com";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkEmail(validEmail);
            result = true;
        } catch (IllegalArgumentException exception) {
            Log.getLogger(FieldValidatorTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(result);
        assertTrue(result);
    }

    @Test
    public void testCheckEmailFailByTooShort() {
        String tooShortEmail = "";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FIELD_VALIDATOR.checkEmail(tooShortEmail));
        System.out.println(exception.getMessage());
    }

    @Test
    public void testCheckEmailFailByTooLong() {
        String tooLongEmail = "ejemplo_de_correo_que_puede_ser_demasiado_largo@gmail.com";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FIELD_VALIDATOR.checkEmail(tooLongEmail));
        System.out.println(exception.getMessage());
    }

    @Test
    public void testCheckEmailFailByNoAtSing() {
        String EmailWithoutAtSign = "correo_ejemplogmail.com";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FIELD_VALIDATOR.checkEmail(EmailWithoutAtSign));
        System.out.println(exception.getMessage());
    }

    @Test
    public void testCheckEmailFailByBlankSpaces() {
        String EmailWithBlankSpaces = "correo ejemplo@gmail.com";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FIELD_VALIDATOR.checkEmail(EmailWithBlankSpaces));
        System.out.println(exception.getMessage());
    }
    
    
    
    
    
    

    @Test
    public void testCheckShortRangeSuccess() {
        String validText = "Ejemplo de texto corto";
        boolean result = false;

        try {
            FIELD_VALIDATOR.checkShortRange(validText);
            result = true;
        } catch (IllegalArgumentException exception) {
            Log.getLogger(FieldValidatorTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result);
    }

    @Test
    public void testCheckShortRangeFailByTooShort() {
        String tooShortText = "e";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FIELD_VALIDATOR.checkShortRange(tooShortText));
        System.out.println(exception.getMessage());

    }

    @Test
    public void testCheckShortRangeFailByTooLong() {
        String tooLongText = "ejemplo de texto que puede ser muy largo para este campo";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FIELD_VALIDATOR.checkShortRange(tooLongText));
        System.out.println(exception.getMessage());

    }

    @Test
    public void testCheckShortRangeFailByNoAllowedSigns() {
        String textWithNoAllowedSigns = "¿ejemplo?";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FIELD_VALIDATOR.checkShortRange(textWithNoAllowedSigns));
        System.out.println(exception.getMessage());
    }
    
    
    
    
    
    @Test
    public void testCheckLongRangeSucess() {
        String validText = "Texto largo que sirve de ejemplo, con simbolos y numeros. 1991, fin?";
        boolean result = false;
        try {
            FIELD_VALIDATOR.checkLongRange(validText);
            result = true;
        } catch (IllegalArgumentException exception) {
            Log.getLogger(FieldValidatorTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result);
    }
    
    @Test
    public void testCheckLongRangeFailByTooLong() {
        String invalidText = "Texto de ejemplo que excede el limite de los 255 caracteres permitidos. Eisisi eidejoijdieded jidiejiedd odjeidjode ojijijoidejidejo ijdiejdoiejodde jidjeidedjeoi jiojodejidiide. Eiuide jijiodeide odiejdeodeid, jiudjeiudjdie; ijijeididde (4775). iduideie udjiudeide.";
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> FIELD_VALIDATOR.checkLongRange(invalidText));
        System.out.println(exception.getMessage());
    }
    
    @Test
    public void testCheckLongRangeFailByTooShort() {
        String invalidText = "s";
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> FIELD_VALIDATOR.checkLongRange(invalidText));
        System.out.println(exception.getMessage());
    }
    
    
    
    
    
    
    @Test
    public void testCheckPasswordSuccess() {
        String validPassword = "EjemploContr@Se&a83938!";
        boolean result = false;
        
        try {
            FIELD_VALIDATOR.checkPassword(validPassword);
            result = true;
        } catch (IllegalArgumentException exception) {
            Log.getLogger(FieldValidatorTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result);
    }
    
    @Test
    public void testCheckPasswordByNoSigns() {
        String invalidPassword = "Contra9384";
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> FIELD_VALIDATOR.checkPassword(invalidPassword));
        System.out.println(exception.getMessage());
    }
    
    @Test
    public void testCheckPasswordByNoNumbers() {
        String invalidPassword = "Contra!$";
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> FIELD_VALIDATOR.checkPassword(invalidPassword));
        System.out.println(exception.getMessage());
    }
    
    @Test
    public void testCheckPasswordByTooShort() {
        String invalidPassword = "Co";
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> FIELD_VALIDATOR.checkPassword(invalidPassword));
        System.out.println(exception.getMessage());
    }
    
    @Test
    public void testCheckPasswordByNoCapitalLetters() {
        String invalidPassword = "contrasena2233!$";
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> FIELD_VALIDATOR.checkPassword(invalidPassword));
        System.out.println(exception.getMessage());
    }
    
    
    
    
    
    
    
    @Test
    public void testCheckPersonalNumberSuccess() {
        int validPersonalNumber = 38293;
        boolean result = false;
        
        try {
            FIELD_VALIDATOR.checkUvPersonalNumber(validPersonalNumber);
            result = true;
        } catch (IllegalArgumentException exception) {
            Log.getLogger(FieldValidatorTest.class).error(exception.getMessage(), exception);
        }
        
        assertTrue(result);
    }
    
    @Test
    public void testCheckPersonalNumberFailByTooShort() {
        int invalidPersonalNumber = 3;
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> FIELD_VALIDATOR.checkUvPersonalNumber(invalidPersonalNumber));
        System.out.println(exception.getMessage());
    }
    
    @Test
    public void testCheckPersonalNumberFailByTooLong() {
        int invalidPersonalNumber = 373737;
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> FIELD_VALIDATOR.checkUvPersonalNumber(invalidPersonalNumber));
        System.out.println(exception.getMessage());
    }
    
    
    
    
    
    
    
    @Test
    public void testCheckNumberSuccess() {
        int validNumber = 33;
        
        assertTrue(FIELD_VALIDATOR.checkNumbers(validNumber));
    }
    
    
    
    
    
    @Test
    public void testCheckTextSuccess() {
        String validText = "Ejemplo de texto: que es valido.";
        boolean result = false;
       
        try {
            FIELD_VALIDATOR.checkText(validText);
            result = true;
        } catch (IllegalArgumentException exception) {
            Log.getLogger(FieldValidatorTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result);
    }
    
    @Test
    public void testCheckTextFailByTooShort() {
        String invalidText = "";
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> FIELD_VALIDATOR.checkText(invalidText));
        System.out.println(exception.getMessage()); 
    }
    
    @Test
    public void testCheckTextFailByNoAllowedSigns() {
        String invalidText = "Texto con signos?(*##";
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> FIELD_VALIDATOR.checkText(invalidText));
        System.out.println(exception.getMessage()); 
    }
    
}
