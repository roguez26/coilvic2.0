package unit.test.EmailSenderDAO;

import mx.fei.coilvicapp.logic.emailSender.EmailSenderDAO;
import mx.fei.coilvicapp.logic.emailSender.EmailSender;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import log.Log;
import org.junit.After;
import org.junit.Before;
import unit.test.Initializer.TestHelper;

public class EmailRegistrationTest {

    private final EmailSenderDAO EMAIL_SENDER_DAO = new EmailSenderDAO();
    private final EmailSender EMAIL_SENDER_FOR_TESTING = new EmailSender();

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
    public void testRegisterSentEmailSuccess() throws DAOException {
        int idEmail = 0;

        idEmail = EMAIL_SENDER_DAO.registerEmail(EMAIL_SENDER_FOR_TESTING);
        EMAIL_SENDER_FOR_TESTING.setIdEmail(idEmail);

        System.out.println(idEmail);
        assertTrue(idEmail > 0);
    }

    @Test(expected = DAOException.class)
    public void testRegisterSentEmailFailByNoReceiver() throws DAOException {
        EMAIL_SENDER_FOR_TESTING.setReceiver(new Professor());
        EMAIL_SENDER_DAO.registerEmail(EMAIL_SENDER_FOR_TESTING);
    }

    @After
    public void tearDown() throws DAOException {

        EMAIL_SENDER_DAO.deleteEmail(EMAIL_SENDER_FOR_TESTING.getIdEmail());

        TEST_HELPER.deleteAll();
    }
}
