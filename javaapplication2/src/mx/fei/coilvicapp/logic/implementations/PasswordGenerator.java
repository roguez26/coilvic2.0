package mx.fei.coilvicapp.logic.implementations;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_LETTERS = LOWERCASE_LETTERS.toUpperCase();
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "@$!%*?&";
    private static final int LENGTH = 12;
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generatePassword() {
        StringBuilder password = new StringBuilder();

        String characterSet = LOWERCASE_LETTERS + UPPERCASE_LETTERS + NUMBERS + SYMBOLS;
        
        password.append(getRandomChar(LOWERCASE_LETTERS));
        password.append(getRandomChar(UPPERCASE_LETTERS));
        password.append(getRandomChar(NUMBERS));
        password.append(getRandomChar(SYMBOLS));

        for (int i = 4; i < LENGTH; i++) {
            password.append(getRandomChar(characterSet));
        }

        return password.toString();
    }

    private static char getRandomChar(String characterSet) {
        int index = secureRandom.nextInt(characterSet.length());
        return characterSet.charAt(index);
    }

    public static void main(String[] args) {
        String password = generatePassword();
        System.out.println(password);
    }
}
