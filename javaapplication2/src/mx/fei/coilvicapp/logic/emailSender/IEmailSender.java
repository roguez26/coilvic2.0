package mx.fei.coilvicapp.logic.emailSender;
import mx.fei.coilvicapp.logic.implementations.DAOException;

/**
 *
 * @author ivanr
 */
public interface IEmailSender {
    
    public int registerEmail (EmailSender emailSender) throws DAOException;
}
