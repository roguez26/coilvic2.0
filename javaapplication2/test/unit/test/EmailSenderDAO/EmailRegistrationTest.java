package unit.test.EmailSenderDAO;

import mx.fei.coilvicapp.logic.emailSender.EmailSenderDAO;
import mx.fei.coilvicapp.logic.emailSender.EmailSender;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.professor.ProfessorDAO;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;

/**
 *
 * @author ivanr
 */
public class EmailRegistrationTest {

    private static final EmailSenderDAO EMAIL_SENDER_DAO = new EmailSenderDAO();
    private static final EmailSender EMAIL_SENDER_FOR_TESTING = new EmailSender();

    private static final Professor AUX_PROFESSOR = new Professor();
    private static final ProfessorDAO PROFESSOR_DAO = new ProfessorDAO();

    private static final University AUX_UNIVERSITY = new University();
    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();

    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country AUX_COUNTRY = new Country();

    @Before
    private void setUp() {
        int idCountry = 0;
        int idUniversity = 0;
        int idProfessor = 0;

    }

    private void initializeAuxiliarCountry() {
        AUX_COUNTRY.setName("Mexico");
    }

    private void intitliazeUniversity() {
        AUX_UNIVERSITY.setName("Universidad Veracruzana");
        AUX_UNIVERSITY.setAcronym("UV");
        AUX_UNIVERSITY.setJurisdiction("Veracruz");
        AUX_UNIVERSITY.setCity("Xalapa");
        AUX_UNIVERSITY.setCountry(AUX_COUNTRY);
    }

    public void initializeProfessor() {
        AUX_PROFESSOR.setName("Ivan");
        AUX_PROFESSOR.setPaternalSurname("Rodriguez");
        AUX_PROFESSOR.setMaternalSurname("Franco");
        AUX_PROFESSOR.setEmail("ivanrfcoc@gmail.com");
        AUX_PROFESSOR.setGender("M");
        AUX_PROFESSOR.setPhoneNumber("2283728394");
     //  AUX_PROFESSOR
    }

}
