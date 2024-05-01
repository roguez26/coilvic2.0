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
    public void setUp() {
        int idCountry;
        int idUniversity;
        int idProfessor;
        
        initializeCountry();
        intitliazeUniversity();
        initializeProfessor();
        intializeEmail();
        
        try {
            idCountry = COUNTRY_DAO.registerCountry(AUX_COUNTRY);
            AUX_COUNTRY.setIdCountry(idCountry);
            AUX_UNIVERSITY.setCountry(AUX_COUNTRY);
            idUniversity = UNIVERSITY_DAO.registerUniversity(AUX_UNIVERSITY);
            AUX_UNIVERSITY.setIdUniversity(idUniversity);
            AUX_PROFESSOR.setUniversity(AUX_UNIVERSITY);
            idProfessor = PROFESSOR_DAO.registerProfessor(AUX_PROFESSOR);
            AUX_PROFESSOR.setIdProfessor(idProfessor);
        } catch (DAOException exception) {
            Logger.getLogger(EmailRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        EMAIL_SENDER_FOR_TESTING.setReceiver(AUX_PROFESSOR);
    }

    private void initializeCountry() {
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
    }
    
    public void intializeEmail() {
        EMAIL_SENDER_FOR_TESTING.setMessage("Mensaje de prueba");
        EMAIL_SENDER_FOR_TESTING.setSubject("Asunto de prueba");

    }
    
    @Test
    public void testRegisterSentEmailSuccess() {
        int idEmail = 0;
        
        try {
            idEmail = EMAIL_SENDER_DAO.registerEmail(EMAIL_SENDER_FOR_TESTING);
            EMAIL_SENDER_FOR_TESTING.setIdEmail(idEmail);
        } catch (DAOException exception) {
            Logger.getLogger(EmailRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(idEmail > 0);
    }
//    
//    @After
//    public void tearDown() {
//        try {
//            EMAIL_SENDER_DAO.deleteEmail(EMAIL_SENDER_FOR_TESTING.getIdEmail());
//            PROFESSOR_DAO.deleteProfessorByID(AUX_PROFESSOR.getIdProfessor());
//            UNIVERSITY_DAO.deleteUniversity(AUX_UNIVERSITY.getIdUniversity());
//            COUNTRY_DAO.deleteCountry(AUX_COUNTRY.getIdCountry());
//        } catch (DAOException exception) {
//            Logger.getLogger(EmailRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
//        }
//    }
}
