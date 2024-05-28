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
    public void testGeneratePasswordFailure() {
        String password = PasswordGenerator.generatePassword();
        assertNotNull(password);
        assertEquals(12, password.length());
        assertTrue(hasOnlyLowercaseLetters(password));
        assertTrue(hasOnlyUppercaseLetters(password));
        assertTrue(hasOnlyNumbers(password));
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
