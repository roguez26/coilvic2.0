
package unit.test.EmailSenderDAO;
import org.junit.Test;
import mx.fei.coilvicapp.logic.emailSender.EmailSender;

/**
 *
 * @author ivanr
 */
public class EmailSenderTest {
    
    private final EmailSender emailSender = new EmailSender();
    
    public EmailSenderTest() {
        
    }
    
    @Test
    public void sendEmailSuccess() {
        emailSender.createEmail();
        emailSender.sendEmail();
    }
    
}
