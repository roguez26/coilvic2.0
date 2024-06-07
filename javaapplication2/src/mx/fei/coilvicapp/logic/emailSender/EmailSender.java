package mx.fei.coilvicapp.logic.emailSender;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import log.Log;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;
import mx.fei.coilvicapp.logic.professor.Professor;

/**
 *
 * @author ivanr
 */
public class EmailSender {

    private int idEmail;
    private final String SENDER = "sender";
    private Professor receiver;
    private String subject;
    private String message;
    private final String PASSWORD = "password";

    private final Properties properties;
    private Session session;
    private MimeMessage mail;

    public EmailSender() {
        properties = new Properties();
    }

    public boolean createEmail() {
        boolean result = false;
        Properties mailPermisionsFile = getPropertiesFile();
        
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.user", SENDER);
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.setProperty("mail.smtp.auth", "true");
        session = Session.getDefaultInstance(properties);

        String sender = mailPermisionsFile.getProperty(SENDER);
        String password = mailPermisionsFile.getProperty(PASSWORD);
        if (sender != null && password != null) {
            try {
                mail = new MimeMessage(session);
                mail.setFrom(new InternetAddress(sender));
                mail.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver.getEmail()));
                mail.setSubject(subject);
                mail.setText(message, "ISO-8859-1", "html");
                result = true;
            } catch (AddressException exception) {
                Log.getLogger(EmailSender.class).error(exception.getMessage(), exception);
            } catch (MessagingException exception) {
                Log.getLogger(EmailSender.class).error(exception.getMessage(), exception);
            }
        }
        return result;
    }

    public boolean sendEmail() throws MessagingException {
        boolean wasSent = false;
        Transport transport = null;
        Properties permitionsCredential = getPropertiesFile();

        if (session != null && mail != null) {
            try {
                transport = session.getTransport("smtp");
                transport.connect(permitionsCredential.getProperty(SENDER), permitionsCredential.getProperty(PASSWORD));
                transport.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
                wasSent = true;
            } catch (NoSuchProviderException exception) {
                Log.getLogger(EmailSender.class).error(exception.getMessage(), exception);
            } catch (MessagingException | IllegalStateException exception) {
                Log.getLogger(EmailSender.class).error(exception.getMessage(), exception);
            } finally {
                if (transport != null) {
                    try {
                        transport.close();
                    } catch (MessagingException exception) {
                        Log.getLogger(EmailSender.class).error(exception.getMessage(), exception);
                    }
                }
            }
        }
        if (!wasSent) {
            throw new MessagingException("No fue posible enviar el correo");
        }

        return wasSent;
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

    private Properties getPropertiesFile() {
        Properties permitionsPropertiesFile = null;
        
        try {
            try (InputStream file = new FileInputStream("resources\\mail\\mail.properties")) {
                if (file != null) {
                    permitionsPropertiesFile = new Properties();
                    permitionsPropertiesFile.load(file);
                }
            }
        } catch (FileNotFoundException exception) {
            Log.getLogger(EmailSender.class).error(exception.getMessage(), exception);
        } catch (IOException exception) {
            Log.getLogger(EmailSender.class).error(exception.getMessage(), exception);
        }
        return permitionsPropertiesFile;
    }
}
