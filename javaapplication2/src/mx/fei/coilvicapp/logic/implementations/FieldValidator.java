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
    private static final String SHORT_RANGE = "^[\\p{L}0-9\\s]{3,45}$";
    private static final String LONG_RANGE = "^[\\p{L}0-9\\s]{3,255}$"; 
    private final String PHONE_NUMBER_REGEX = "^\\d{10}$";
    
    public boolean checkEmail(String eMail) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(eMail);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("El Email debe contener las siguientes características:\n"
                    + "1.- No debe contener espacios en blanco\n"
                    + "2.- Solo los siguientes dominios son permitidos: (@uv.mx, @estudiantes.uv.mx, "
                    + "@gmail.com, @hotmail.com, @outlook.com, @edu.mx)\n");
        }
        return true;
    }
    
    public boolean checkName(String name) {
        Pattern pattern = Pattern.compile(NAME_REGEX);
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("El nombre debe tener las siguientes características:\n"
                    + "1.- Debe contener de 3 a 45 caractéres como máximo\n"
                    + "2.- No puede contener más de 2 espacios en blanco juntos\n"
                    + "3.- No puede tener solo espacios en blanco\n"
                    + "4.- No debe contener los siguientes símbolos: (!, \", #, $, %, &, ', (, ), *, +, "
                    + ",, -, ., /, :, ;, <, =, >, ?, @, [, \\, ], ^, _, `, {, |, }, ~)\n");
        }
        return true;
    }
    
    public boolean checkPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("El número telefónico debe tener las siguientes características:\n"
                    + "1.- Debe contener un total de 10 dígitos\n"
                    + "2.- Solo debe contener números\n"
                    + "3.- No puede tener espacios en blanco\n"
                    + "4.- No debe contener los siguientes símbolos: (!, \", #, $, %, &, ', (, ), *, +, "
                    + ",, -, ., /, :, ;, <, =, >, ?, @, [, \\, ], ^, _, `, {, |, }, ~)\n");
        }
        return true;
    }
    
    public static boolean checkShortRange(String stringForCheck) {
        Pattern pattern = Pattern.compile(SHORT_RANGE);
        Matcher matcher = pattern.matcher(stringForCheck);
        if(!matcher.matches()) {
            throw new IllegalArgumentException("El campo debe tener las siguientes características:\n"
                    +"1.- Debe contener al un rango de 3 a 45 caractéres \n");
        }
        return true;
    }
    
    public static boolean checkLongRange(String stringForCheck) {
        Pattern pattern = Pattern.compile(LONG_RANGE);
        Matcher matcher = pattern.matcher(stringForCheck);
        if(!matcher.matches()) {
            throw new IllegalArgumentException("El campo debe tener las siguientes características:\n"
                    +"1.- Debe contener al un rango de 3 a 255 caractéres \n");
        }
        return true;
    }
}
