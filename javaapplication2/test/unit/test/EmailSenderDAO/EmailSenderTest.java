
package unit.test.EmailSenderDAO;
import javax.mail.MessagingException;
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
    public void testSendEmailSuccess() {
        emailSender.createEmail();
        try {
            emailSender.sendEmail();
        } catch(MessagingException exception) {
            
        }
            
        
    }
    
}
