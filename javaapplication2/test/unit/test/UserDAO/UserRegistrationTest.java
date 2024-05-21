
package unit.test.UserDAO;

import mx.fei.coilvicapp.logic.user.User;
import mx.fei.coilvicapp.logic.user.UserDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.user.UserDAO;
import mx.fei.coilvicapp.logic.user.User;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author ivanr
 */
public class UserRegistrationTest {
    
    private static final UserDAO USER_DAO = new UserDAO();
    private static final User USER_FOR_TESTING = new User();
 
    
    public void initializeUser() {
       // USER_FOR_TESTING.setIdProfessor(1);
    }
}
