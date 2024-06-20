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

public class EmailSender {

    private int idEmail;
    private String sender = "sender";
    private Professor receiver;
    private String subject;
    private String message;
    private String password = "password";

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
        properties.setProperty("mail.smtp.user", sender);
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.setProperty("mail.smtp.auth", "true");
        session = Session.getDefaultInstance(properties);

        String senderPermisions = mailPermisionsFile.getProperty(this.sender);
        String passwordPermisions = mailPermisionsFile.getProperty(this.password);
        if (senderPermisions != null && passwordPermisions != null && receiver != null) {
            try {
                mail = new MimeMessage(session);
                mail.setFrom(new InternetAddress(senderPermisions));
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
                transport.connect(permitionsCredential.getProperty(sender), permitionsCredential.getProperty(
                        password));
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

    public void setReceiver(Professor receiver) {
        if(receiver == null) {
            throw new IllegalArgumentException("Es necesario un profesor para realizar la notificación");
        }
        this.receiver = receiver;
    }

    public void setSubject(String subject) {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkShortRange(subject);
        this.subject = subject;
    }

    public void setMessage(String message) {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkLongRange(message);
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
    
    public void setSender(String sender) {
        this.sender = sender;
    }
    
    public void setPassword(String password) {
        this.password = password;
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
