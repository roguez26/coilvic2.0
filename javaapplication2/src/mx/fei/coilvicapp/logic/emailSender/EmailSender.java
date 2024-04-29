package mx.fei.coilvicapp.logic.emailSender;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import mx.fei.coilvicapp.logic.professor.Professor;

/**
 *
 * @author ivanr
 */
public class EmailSender {

    private final String emailSender = "coilvicapplication@gmail.com";
    private Professor receiver;
    private String subject;
    private String message;
    private final String password = "zlep fhwu rbiu qzci";

    private final Properties properties;
    private Session session;
    private MimeMessage mail;

    public EmailSender() {
        properties = new Properties();
    }

    public void createEmail() {
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.user", emailSender);
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.setProperty("mail.smtp.auth", "true");

        session = Session.getDefaultInstance(properties);

        try {
            mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(emailSender));
            mail.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver.getEmail()));
            mail.setSubject(subject);
            mail.setText(message, "ISO-8859-1", "html");
        } catch (AddressException eException) {
            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, eException);

        } catch (MessagingException eException) {
            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, eException);
        }
    }

    public void sendEmail() {
        try {
            Transport transport = session.getTransport("smtp");
            transport.connect(emailSender, password);
            transport.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
            transport.close();
        } catch (NoSuchProviderException nspException) {
            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, nspException);
        } catch (MessagingException mException) {
            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, mException);
        }
    }
    
    public void setReieiver(Professor reveiver) {
        this.receiver = reveiver;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Professor getReceiver() {
        return receiver;
    }
    
    public String getEmailReceiver() {
        return receiver.getEmail();
    }
     
    public String getSubject() {
        return subject;
    }
    
    public String getMessage() {
        return message;
    }

}
