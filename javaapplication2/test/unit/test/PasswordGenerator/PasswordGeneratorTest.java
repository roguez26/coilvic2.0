package unit.test.PasswordGenerator;

import mx.fei.coilvicapp.logic.implementations.PasswordGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

public class PasswordGeneratorTest {

    @Test
    public void testGeneratePasswordSuccess() {
        String password = PasswordGenerator.generatePassword();
        assertNotNull(password);
        assertEquals(12, password.length());
        assertTrue(hasLowercaseLetter(password));
        assertTrue(hasUppercaseLetter(password));
        assertTrue(hasNumber(password));
        assertTrue(hasSymbol(password));
    }
    
    @Test
    public void testGeneratePasswordFailureByLength() {
        String password = "Si";
        assertNotNull(password);
        assertNotEquals(12, password.length());
    }    
    
    @Test
    public void testGeneratePasswordFailureByOnlyLowerCase() {
        String password = "contraseniaprueba";
        assertNotNull(password);
        assertTrue(hasOnlyLowercaseLetters(password));
    }  

    @Test
    public void testGeneratePasswordFailureByOnlyUpperCase() {
        String password = "CONTRASENIAPRUEBA";
        assertNotNull(password);
        assertTrue(hasOnlyUppercaseLetters(password));
    }      

    @Test
    public void testGeneratePasswordFailureByOnlyNumbers() {
        String password = "123456780";
        assertNotNull(password);
        assertTrue(hasOnlyNumbers(password));
    }      
    
    @Test
    public void testGeneratePasswordFailureByOnlySymbols() {
        String password = "@$!%*?&";
        assertNotNull(password);
        assertTrue(hasOnlySymbols(password));
    }      
    
    private boolean hasLowercaseLetter(String password) {
        return password.matches(".*[a-z].*");
    }

    private boolean hasUppercaseLetter(String password) {
        return password.matches(".*[A-Z].*");
    }

    private boolean hasNumber(String password) {
        return password.matches(".*[0-9].*");
    }

    private boolean hasSymbol(String password) {
        return password.matches(".*[@$!%*?&].*");
    }

    private boolean hasOnlyLowercaseLetters(String password) {
        return password.matches("[a-z]+");
    }

    private boolean hasOnlyUppercaseLetters(String password) {
        return password.matches("[A-Z]+");
    }

    private boolean hasOnlyNumbers(String password) {
        return password.matches("[0-9]+");
    }

    private boolean hasOnlySymbols(String password) {
        return password.matches("[@$!%*?&]+");
    }
}
