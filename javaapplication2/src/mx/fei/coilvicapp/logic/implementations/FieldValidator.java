package mx.fei.coilvicapp.logic.implementations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ivanr
 */
public class FieldValidator {

    private final String EMAIL_REGEX = "^(?=.{3,45}$)[^\\s@]+@(?:uv\\.mx|estudiantes\\.uv\\.mx|gmail\\.com|hotmail\\.com|outlook\\.com|edu\\.mx)$";
    private final String NAME_REGEX = "^(?!.*[\\!\\#\\$%\\&'\\(\\)\\*\\+\\-\\.,\\/\\:\\;<\\=\\>\\?\\@\\[\\\\\\]\\^_`\\{\\|\\}\\~])(?!.*  )(?!^ $)(?!.*\\d)^.{1,45}$";
    private final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private final String SHORT_RANGE = "^[\\p{L}0-9\\s]{3,45}$";
    private final String LONG_RANGE = "^.{3,255}$";
    private final String PHONE_NUMBER_REGEX = "^\\d{10}$";
    private final String ENROLLMENT_REGEX = "^S\\d{8}$";

    public void checkEmail(String eMail) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        if (eMail != null) {
            Matcher matcher = pattern.matcher(eMail);
            if (matcher.matches()) {
                return;
            }
        }
        throw new IllegalArgumentException("El Email debe contener las siguientes características:\n"
                + "1.- No debe contener espacios en blanco\n"
                + "2.- Solo los siguientes dominios son permitidos: (@uv.mx, @estudiantes.uv.mx, "
                + "@gmail.com, @hotmail.com, @outlook.com, @edu.mx)\n");
    }

    public void checkName(String name) {
        Pattern pattern = Pattern.compile(NAME_REGEX);
        if (name != null) {
            Matcher matcher = pattern.matcher(name);
            if (matcher.matches()) {
                return;
            }
        }
        throw new IllegalArgumentException("El nombre debe tener las siguientes características:\n"
                + "1.- Debe contener de 3 a 45 caractéres como máximo\n"
                + "2.- No puede contener más de 2 espacios en blanco juntos\n"
                + "3.- No puede tener solo espacios en blanco\n"
                + "4.- No debe contener los siguientes símbolos: (!, \", #, $, %, &, ', (, ), *, +, "
                + ",, -, ., /, :, ;, <, =, >, ?, @, [, \\, ], ^, _, `, {, |, }, ~)\n");
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
                + "4.- No debe contener los siguientes símbolos: (!, \", #, $, %, &, ', (, ), *, +, "
                + ",, -, ., /, :, ;, <, =, >, ?, @, [, \\, ], ^, _, `, {, |, }, ~)\n");

    }

    public void checkShortRange(String stringForCheck) {
        Pattern pattern = Pattern.compile(SHORT_RANGE);
        if (stringForCheck != null) {
            Matcher matcher = pattern.matcher(stringForCheck);
            if (matcher.matches()) {
                return;
            }
        }
        throw new IllegalArgumentException("El campo debe tener las siguientes características:\n"
                + "1.- Debe contener al un rango de 3 a 45 caractéres \n");
    }

    public void checkLongRange(String stringForCheck) {
        Pattern pattern = Pattern.compile(LONG_RANGE);
        if (stringForCheck != null) {
            Matcher matcher = pattern.matcher(stringForCheck);
            if (matcher.matches()) {
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
                + "1.- Debe contener al menos 8 caracteres (mayúsculas y minúsculas).\n"
                + "2.- Debe contener  al menos un carácter especial (@, $, !, %, *, ? o &.\n"
                + "3.- Debe contener al menos un número.\n");

    }
}
