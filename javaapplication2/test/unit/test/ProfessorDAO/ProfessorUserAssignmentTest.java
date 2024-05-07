package unit.test.ProfessorDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.professor.ProfessorDAO;
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
public class ProfessorUserAssignmentTest {
    
    private static final ProfessorDAO PROFESSOR_DAO = new ProfessorDAO();
    private static final Professor PROFESSOR_FOR_TESTING = new Professor();
    
    @Before
    public void setUp() {
        initializeProfessor();
        
        int idProfessor = 0;
        try {
            idProfessor = PROFESSOR_DAO.registerProfessor(PROFESSOR_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorUserAssignmentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        PROFESSOR_FOR_TESTING.setIdProfessor(idProfessor);
        
    }
    
    public void initializeProfessor() {
        PROFESSOR_FOR_TESTING.setName("Roberto");
        PROFESSOR_FOR_TESTING.setPaternalSurname("Martinez");
        PROFESSOR_FOR_TESTING.setMaternalSurname("Garza");
        PROFESSOR_FOR_TESTING.setEmail("roberto@gmail.com");
        PROFESSOR_FOR_TESTING.setGender("M");
        PROFESSOR_FOR_TESTING.setPhoneNumber("2283728394");
        PROFESSOR_FOR_TESTING.setIdUniversity(3);
    }
    
    @Test
    public void testUserRegistration() {
        int result = 0;
        String password = "newPassword123";
        
        try {
            result = PROFESSOR_DAO.assignUser(PROFESSOR_FOR_TESTING, password);
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorUserAssignmentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result > 0);
        
    }
    
//    @After
//    public void tearDown() {
//        try {
//            
//        }
//    }
    
}
