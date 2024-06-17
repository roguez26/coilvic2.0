package unit.test.EmailSenderDAO;

import javax.mail.MessagingException;
import org.junit.Test;
import mx.fei.coilvicapp.logic.emailSender.EmailSender;
import mx.fei.coilvicapp.logic.professor.Professor;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class EmailSenderTest {

    private EmailSender initializeEmailSender() {
        EmailSender emailSender = new EmailSender();
        Professor professor = new Professor();

        professor.setEmail("ivanrfcoc@gmail.com");
        
        emailSender.setReceiver(professor);
        emailSender.setMessage("Este es un mensaje para hacer las pruebas");
        emailSender.setSubject("Asunto para prueba");

        return emailSender;
    }

    @Test
    public void testCreateEmailSuccess() {
        EmailSender emailSender = initializeEmailSender();
        boolean result = emailSender.createEmail();
        System.out.println(result);
        assertTrue(result);
    }

    @Test
    public void testCreateEmailSenderFailByNoReceiver() {
        EmailSender emailSender = initializeEmailSender();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
                emailSender.setReceiver(null));
        System.out.println(exception.getMessage());
    }

    @Test
    public void testCreateEmailSenderFailByNotFoundPassword() {
        EmailSender emailSender = initializeEmailSender();
        emailSender.setPassword("no password");
        boolean result = emailSender.createEmail();
        System.out.println(result);
        assertTrue(!result);
    }

    @Test
    public void testCreateEmailSenderFailByNotFoundSender() {
        EmailSender emailSender = initializeEmailSender();
        emailSender.setSender("no sender");
        boolean result = emailSender.createEmail();
        System.out.println(result);
        assertTrue(!result);
    }

    @Test
    public void testSendEmailSuccess() throws MessagingException {
        EmailSender emailSender = initializeEmailSender();
        emailSender.createEmail();
        boolean result = emailSender.sendEmail();
        System.out.println(result);
        assertTrue(result);
    }

    @Test
    public void testSendEmailFailByNotCreatedEmail() {
        EmailSender emailSender = initializeEmailSender();
        MessagingException exception = assertThrows(MessagingException.class, () -> 
                emailSender.sendEmail());
        System.out.println(exception.getMessage());
    }

}
