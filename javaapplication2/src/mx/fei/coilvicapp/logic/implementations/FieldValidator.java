package mx.fei.coilvicapp.logic.implementations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ivanr
 */
public class FieldValidator {

    private final String EMAIL_REGEX = "^(?=.{3,45}$)(?!.*\\.{2})(?![-._])[a-zA-Z0-9_-]+(?:\\.[a-zA-"
            + "Z0-9+_-]+)*(?<![-._])@(uv\\.mx|estudiantes\\.uv\\.mx|gmail\\.com|hotmail\\.com|outlook\\.com|ed"
            + "u\\.mx|yahoo\\.com|icloud\\.com|aol\\.com|protonmail\\.com|zoho\\.com|mail\\.com)$";
    private final String NAME_REGEX = "^(?=.{1,45}$)(?=[^\\.\\-']*[\\p{L}\\p{M}])(?!.*[.\\-']{2})(?!.*"
            + "  )[\\p{L} \\p{M}.'-]+(?<![ \\-'])$";
    private final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$"
            + "!%*?&]{8,128}$";
    private final String SHORT_RANGE_REGEX = "^[\\p{L}0-9\\s]{3,45}$";
    private final String LONG_RANGE_REGEX = "(?s)^.{3,255}$";
    private final String PHONE_NUMBER_REGEX = "^\\d{10}$";
    private final String ENROLLMENT_REGEX = "^S\\d{8}$";
    private final String UV_PERSONAL_NUMBER_REGEX = "^\\d{5}$";
    private final String TEXT_REGEX = "^[\\p{L}0-9\\s,\\.\\:]+$";
    private final String TERM_REGEX = "^(Enero|Febrero|Marzo|Abril|Mayo|Junio|Julio|Agosto|Septiembre"
            + "|Octubre|Noviembre|Diciembre)\\"
            + "d{4}-(Enero|Febrero|Marzo|Abril|Mayo|Junio|Julio|Agosto|Septiembre|Octubre|Noviembre|Diciembre)\\d{4}$";
    private final String QUESTION_REGEX = "^[\\p{L}\\p{N} ¿?]+$";

    public void checkEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        if (email != null) {
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                return;
            }
        }
        throw new IllegalArgumentException("El Email debe contener las siguientes características:\n"
                + "1.- No debe contener espacios en blanco\n"
                + "2.- Simbolos permitidos: +, -, . y _\n"
                + "3.- No debe empezar ni terminar con signos\n"
                + "4.- Dominios permitidos: (@uv.mx, @estudiantes.uv.mx, "
                + "@gmail.com, @hotmail.com, @outlook.com, @edu.mx, @yahoo.com, @icloud.com, "
                + "@aol.com, @protonmail.com, @zoho.com, @mail.com)\n");

    }

    public void checkName(String name) {
        Pattern pattern = Pattern.compile(NAME_REGEX);
        if (name != null) {
            Matcher matcher = pattern.matcher(name);
            if (matcher.matches()) {
                checkNoRepeatedCharacters(name);
                return;
            }
        }
        throw new IllegalArgumentException("El nombre debe tener las siguientes características:\n"
                + "1.- Los únicos símbolos permitidos son espacios, ., -, y '\n"
                + "2.- No debe contener dos símbolos consecutivos.\n"
                + "3.- No debe terminar con espacios en blanco, ' o -\n"
                + "4.- No debe contener números");

    }

    public void checkPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX);
        if (phoneNumber != null) {
            Matcher matcher = pattern.matcher(phoneNumber);
            if (matcher.matches()) {
                return;
            }
        }
        throw new IllegalArgumentException("El número telefónico debe tener las siguientes características:\n"
                + "1.- Debe contener un total de 10 dígitos\n"
                + "2.- Solo debe contener números\n"
                + "3.- No puede tener espacios en blanco\n"
                + "4.- No debe contener símbolos\n");

    }

    public void checkShortRange(String stringForCheck) {
        Pattern pattern = Pattern.compile(SHORT_RANGE_REGEX);
        if (stringForCheck != null) {
            Matcher matcher = pattern.matcher(stringForCheck);
            if (matcher.matches()) {
                checkNoRepeatedCharacters(stringForCheck);
                return;
            }
        }
        throw new IllegalArgumentException("El campo debe tener las siguientes características:\n"
                + "1.- Debe contener al un rango de 3 a 45 caractéres\n"
                + "2.- Solo se permiten caracteres: A-Z, 0-9\n"
                + "3.- No se permiten otros simbolos");
    }

    public void checkLongRange(String stringForCheck) {
        Pattern pattern = Pattern.compile(LONG_RANGE_REGEX);
        if (stringForCheck != null) {
            Matcher matcher = pattern.matcher(stringForCheck);
            if (matcher.matches()) {
                checkNoRepeatedCharacters(stringForCheck);
                return;
            }
        }
        throw new IllegalArgumentException("El campo debe tener las siguientes "
                + "características:\n1.- Debe contener al un rango de 3 a 255 caractéres \n");
    }

    public void checkEnrollment(String enrollment) {
        Pattern pattern = Pattern.compile(ENROLLMENT_REGEX);
        if (enrollment != null) {
            Matcher matcher = pattern.matcher(enrollment);
            if (matcher.matches()) {
                return;
            }
        }
        throw new IllegalArgumentException("La matricula debe comenzar por S "
                + "mayuscula y estar seguida de 8 numeros");
    }

    public void checkPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        if (password != null) {
            Matcher matcher = pattern.matcher(password);
            if (matcher.matches()) {
                return;
            }
        }
        throw new IllegalArgumentException("La contraseña debe tener las siguientes características:\n"
                + "1.- Debe contener al menos 8 y máximo 128 caracteres (mayúsculas y minúsculas).\n"
                + "2.- Debe contener  al menos un carácter especial (@, $, !, %, *, ? o &.\n"
                + "3.- Debe contener al menos un número.\n");
    }

    public void checkCode(String code) {
        try {
            checkPassword(code);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("El código debe tener las siguientes características:\n"
                    + "1.- Debe contener al menos 8 caracteres (mayúsculas y minúsculas).\n"
                    + "2.- Debe contener  al menos un carácter especial (@, $, !, %, *, ? o &.\n"
                    + "3.- Debe contener al menos un número.\n");
        }
    }

    public void checkUvPersonalNumber(int uvPersonalNumber) {
        String uvString = String.valueOf(uvPersonalNumber);
        Pattern pattern = Pattern.compile(UV_PERSONAL_NUMBER_REGEX);
        if (uvString != null) {
            Matcher matcher = pattern.matcher(uvString);
            if (matcher.matches()) {
                return;
            }
        }
        throw new IllegalArgumentException("El numero de personal UV debe tener las siguientes "
                + "caracteristicas:\n"
                + "1.- Debe contener 5 numeros.\n");
    }

    public boolean checkNumbers(int number) {
        return number >= 1 && number <= 100;
    }

    public void checkText(String text) {
        Pattern pattern = Pattern.compile(TEXT_REGEX);
        if (text != null) {
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()) {
                checkNoRepeatedCharacters(text);
                return;
            }
        }
        throw new IllegalArgumentException("El texto debe tener las siguientes características:\n"
                + "1.- Puede contener letras, números, espacios, comas, puntos y dos puntos.\n"
//                + "2.- No puede contener más de 2 espacios en blanco juntos\n"
//                + "3.- No puede tener solo espacios en blanco\n"
                + "2.- No debe contener los siguientes símbolos: (!, \", #, $, %, &, ', (, ), *,"
                + " +, -, /, ;, <, =, >, ?, @, [, \\, ], ^, _, `, {, |, }, ~)\n");
    }

    public boolean checkTerm(String term) {
        Pattern pattern = Pattern.compile(TERM_REGEX);
        Matcher matcher = pattern.matcher(term);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("El periodo debe tener el formato: 'MesAño–MesAño' (Ej., "
                    + "'Agosto2024–Enero2025')");
        }
        return true;
    }

    public void checkNoRepeatedCharacters(String text) {
        Pattern pattern = Pattern.compile("^(?!.*(.)\\1{2,}).+$");
        if (text != null) {
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()) {
                return;
            }
        }
        throw new IllegalArgumentException("No se permiten entradas con 3 o más caracteres repetidos "
                + "consecutivamente.\n");
    }

    public void checkQuestion(String questionText) {
        Pattern pattern = Pattern.compile(QUESTION_REGEX);
        if (questionText != null) {
            Matcher matcher = pattern.matcher(questionText);
            if (matcher.matches()) {
                checkNoRepeatedCharacters(questionText);
                return;
            }
        }
        throw new IllegalArgumentException("Las preguntas deben tener las siguientes características:\n"
                + "1.- Se aceptan letras de cualquier idioma\n"
                + "2.- Se aceptan números\n"
                + "3.- Solo se permiten los simbolos: ¿?");
    }
}
