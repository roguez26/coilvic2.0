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

        FIELD_VALIDATOR.checkName(validName);
        result = true;
        System.out.println(result);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckNameFailByNoAllowedSigns() {
        String invalidName = "Alej4ndro";
        
        FIELD_VALIDATOR.checkName(invalidName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckNameFailByRepeatedCharacters() {
        String invalidName = "Diannnna";
        
        FIELD_VALIDATOR.checkName(invalidName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckNameFailByRepeatedSigns() {
        String invalidName = "Tomás-. Marcos";
        
        FIELD_VALIDATOR.checkName(invalidName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckNameFailByTooLong() {
        String invalidName = "Nombre que es excesivamente largo para el campo";
        
        FIELD_VALIDATOR.checkName(invalidName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckNameFailByTooShort() {
        String invalidName = "";
        
        FIELD_VALIDATOR.checkName(invalidName);
    }

    @Test
    public void testCheckPhoneNumberSuccess() {
        String validPhoneNumber = "2293849284";
        boolean result = false;

        FIELD_VALIDATOR.checkPhoneNumber(validPhoneNumber);
        result = true;
        System.out.println(result);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPhoneNumberFailByTooLong() {
        String tooLongPhoneNumber = "238392843373839383";

        FIELD_VALIDATOR.checkPhoneNumber(tooLongPhoneNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPhoneNumberFailByTooShort() {
        String tooShortPhoneNumber = "2";

        FIELD_VALIDATOR.checkPhoneNumber(tooShortPhoneNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPhoneNumberFailByNoAllowedSigns() {
        String phoneNumberWithSigns = "227384#894";

        FIELD_VALIDATOR.checkPhoneNumber(phoneNumberWithSigns);
    }

    @Test
    public void testCheckEmailSuccess() {
        String validEmail = "coilvicapp@gmail.com";
        boolean result = false;

        FIELD_VALIDATOR.checkEmail(validEmail);
        result = true;
        System.out.println(result);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckEmailFailByTooShort() {
        String tooShortEmail = "";

        FIELD_VALIDATOR.checkEmail(tooShortEmail);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckEmailFailByTooLong() {
        String tooLongEmail = "ejemplo_de_correo_que_puede_ser_demasiado_largo@gmail.com";

        FIELD_VALIDATOR.checkEmail(tooLongEmail);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckEmailFailByNoAtSing() {
        String EmailWithoutAtSign = "correo_ejemplogmail.com";
        
        FIELD_VALIDATOR.checkEmail(EmailWithoutAtSign);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckEmailFailByBlankSpaces() {
        String EmailWithBlankSpaces = "correo ejemplo@gmail.com";
        
        FIELD_VALIDATOR.checkEmail(EmailWithBlankSpaces);
    }

    @Test
    public void testCheckShortRangeSuccess() {
        String validText = "Ejemplo de texto corto";
        boolean result = false;

        FIELD_VALIDATOR.checkShortRange(validText);
        result = true;
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckShortRangeFailByTooShort() {
        String tooShortText = "e";

        FIELD_VALIDATOR.checkShortRange(tooShortText);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckShortRangeFailByTooLong() {
        String tooLongText = "ejemplo de texto que puede ser muy largo para este campo";
        FIELD_VALIDATOR.checkShortRange(tooLongText);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckShortRangeFailByNoAllowedSigns() {
        String textWithNoAllowedSigns = "¿ejemplo?";

        FIELD_VALIDATOR.checkShortRange(textWithNoAllowedSigns);
    }

    @Test
    public void testCheckLongRangeSucess() {
        String validText = "Texto largo que sirve de ejemplo, con simbolos y numeros. 1991, fin?";
        boolean result = false;

        FIELD_VALIDATOR.checkLongRange(validText);
        result = true;
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckLongRangeFailByTooLong() {
        String invalidText = "Texto de ejemplo que excede el limite de los 255 caracteres permitidos. Eisisi eidejoijdieded jidiejiedd odjeidjode ojijijoidejidejo ijdiejdoiejodde jidjeidedjeoi jiojodejidiide. Eiuide jijiodeide odiejdeodeid, jiudjeiudjdie; ijijeididde (4775). iduideie udjiudeide.";

        FIELD_VALIDATOR.checkLongRange(invalidText);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckLongRangeFailByTooShort() {
        String invalidText = "s";

        FIELD_VALIDATOR.checkLongRange(invalidText);
    }

    @Test
    public void testCheckPasswordSuccess() {
        String validPassword = "EjemploContr@Se&a83938!";
        boolean result = false;

        FIELD_VALIDATOR.checkPassword(validPassword);
        result = true;

        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPasswordByNoSigns() {
        String invalidPassword = "Contra9384";

        FIELD_VALIDATOR.checkPassword(invalidPassword);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPasswordFailByNoNumbers() {
        String invalidPassword = "Contra!$";

        FIELD_VALIDATOR.checkPassword(invalidPassword);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPasswordByTooShort() {
        String invalidPassword = "Co";
        
        FIELD_VALIDATOR.checkPassword(invalidPassword);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPasswordByNoCapitalLetters() {
        String invalidPassword = "contrasena2233!$";

        FIELD_VALIDATOR.checkPassword(invalidPassword);
    }

    @Test
    public void testCheckPersonalNumberSuccess() {
        int validPersonalNumber = 38293;
        boolean result = false;

        FIELD_VALIDATOR.checkUvPersonalNumber(validPersonalNumber);
        result = true;
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPersonalNumberFailByTooShort() {
        int invalidPersonalNumber = 3;

        FIELD_VALIDATOR.checkUvPersonalNumber(invalidPersonalNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPersonalNumberFailByTooLong() {
        int invalidPersonalNumber = 373737;
        
        FIELD_VALIDATOR.checkUvPersonalNumber(invalidPersonalNumber);
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
        
        FIELD_VALIDATOR.checkText(validText);
        result = true;
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckTextFailByTooShort() {
        String invalidText = "";
        
        FIELD_VALIDATOR.checkText(invalidText);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckTextFailByNoAllowedSigns() {
        String invalidText = "Texto con signos?(*##";

        FIELD_VALIDATOR.checkText(invalidText);
    }

}
