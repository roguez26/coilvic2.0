package unit.test.EmailSenderDAO;

import mx.fei.coilvicapp.logic.emailSender.EmailSenderDAO;
import mx.fei.coilvicapp.logic.emailSender.EmailSender;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import log.Log;
import static org.junit.Assert.assertThrows;
import unit.test.Initializer.TestHelper;

public class EmailRegistrationTest {

    private static final EmailSenderDAO EMAIL_SENDER_DAO = new EmailSenderDAO();
    private static final EmailSender EMAIL_SENDER_FOR_TESTING = new EmailSender();

    private Professor auxProfessor;
    
    private final TestHelper TEST_HELPER = new TestHelper();


    @Before
    public void setUp() {
        TEST_HELPER.initializeProfessors();
        auxProfessor = TEST_HELPER.getProfessorOne();
        intializeEmail();
    }
    
    public void intializeEmail() {
        EMAIL_SENDER_FOR_TESTING.setMessage("Mensaje de prueba");
        EMAIL_SENDER_FOR_TESTING.setSubject("Asunto de prueba");
        EMAIL_SENDER_FOR_TESTING.setReceiver(auxProfessor);
    }
    
    @Test
    public void testRegisterSentEmailSuccess() {
        int idEmail = 0;
        
        try {
            idEmail = EMAIL_SENDER_DAO.registerEmail(EMAIL_SENDER_FOR_TESTING);
            EMAIL_SENDER_FOR_TESTING.setIdEmail(idEmail);
        } catch (DAOException exception) {
            Log.getLogger(EmailRegistrationTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(idEmail);
        assertTrue(idEmail > 0);
    }
    
    @Test
    public void testRegisterSentEmailFailByNoReceiver() {
        EMAIL_SENDER_FOR_TESTING.setReceiver(new Professor());
        
        DAOException exception = assertThrows(DAOException.class, () ->
                EMAIL_SENDER_DAO.registerEmail(EMAIL_SENDER_FOR_TESTING));
        System.out.println(exception.getMessage());
    }
    
    @After
    public void tearDown() {
        try {
            EMAIL_SENDER_DAO.deleteEmail(EMAIL_SENDER_FOR_TESTING.getIdEmail());
        } catch (DAOException exception) {
            Log.getLogger(EmailRegistrationTest.class).error(exception.getMessage(), exception);
        }
        TEST_HELPER.deleteAll();
    }
}
