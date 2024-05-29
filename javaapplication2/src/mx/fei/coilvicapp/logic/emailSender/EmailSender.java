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
import mx.fei.coilvicapp.logic.implementations.FieldValidator;
import mx.fei.coilvicapp.logic.professor.Professor;

/**
 *
 * @author ivanr
 */
public class EmailSender {

    private int idEmail;
    private final String sender = "coilvicapplication@gmail.com";
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
        properties.setProperty("mail.smtp.user", sender);
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.setProperty("mail.smtp.auth", "true");
        session = Session.getDefaultInstance(properties);
        try {
            mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(sender));
            mail.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver.getEmail()));
            mail.setSubject(subject);
            mail.setText(message, "ISO-8859-1", "html");
        } catch (AddressException eException) {
            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, eException);

        } catch (MessagingException eException) {
            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, eException);
        }
    }

    public boolean sendEmail() throws MessagingException {
        boolean result = true;
        Transport transport = null;

        try {
            transport = session.getTransport("smtp");
            transport.connect(sender, password);
            transport.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
        } catch (NoSuchProviderException nspException) {
            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, nspException);
            throw new MessagingException("No fue posible realizar la notificacion");
        } catch (MessagingException | IllegalStateException exception) {
            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, exception);
            throw new MessagingException("No fue posible realizar la notificacion ");
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException mException) {
                    Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, mException);
                }
            }
        }
        return result;
    }

    public void setReceiver(Professor reveiver) {
        this.receiver = reveiver;
    }

    public void setSubject(String subject) {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkShortRange(subject);
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

    public void setIdEmail(int idEmail) {
        this.idEmail = idEmail;
    }

    public int getIdEmail() {
        return idEmail;
    }

}
