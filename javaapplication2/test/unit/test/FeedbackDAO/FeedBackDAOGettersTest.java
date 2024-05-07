package unit.test.FeedbackDAO;


import mx.fei.coilvicapp.logic.feedback.FeedbackDAO;
import mx.fei.coilvicapp.logic.feedback.Question;
import mx.fei.coilvicapp.logic.feedback.Response;
import org.junit.Before;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;

/**
 *
 * @author ivanr
 */
public class FeedBackDAOGettersTest {
    
    private static final FeedbackDAO FEEDBACK_DAO = new FeedbackDAO();
    private static final Question QUESTION_FOR_TESTING = new Question();
    private static final String QUESTION_TEXT = "¿Surgieron problemas en el momento de la implementacion?";
    private static final String QUESTION_TYPE = "P";
    
    private static final ArrayList<Response> REPONSES_FOR_TESTING = new ArrayList<>();
    private static final Response REPONSE_FOR_TESTING = new Response();
    private static final String RESPONSE_TEXT = "Todo ocurrio de maravilla";
    private static final int ID_QUESTION = 30;
    private static final int ID_STUDENT = 1;
    private static final int ID_COLLABORATIVE_PROJECT = 1;
    
    @Before
    public void setUp() {
        QUESTION_FOR_TESTING.setQuestionText(QUESTION_TEXT);
        QUESTION_FOR_TESTING.setQuestionType(QUESTION_TYPE);
        QUESTION_FOR_TESTING.setIdQuestion(30);
        REPONSE_FOR_TESTING.setIdResponse(1);
        REPONSE_FOR_TESTING.setResponseText(RESPONSE_TEXT);
        REPONSE_FOR_TESTING.setIdQuestion(ID_QUESTION);
        REPONSE_FOR_TESTING.setIdStudent(ID_STUDENT);
        REPONSE_FOR_TESTING.setIdCollaborativeProject(ID_COLLABORATIVE_PROJECT);
        
        REPONSES_FOR_TESTING.add(REPONSE_FOR_TESTING);
    }
    
    @Test
    public void testGetQuestionByQuestionText() {
        Question result = new Question();
        
        try {
            result = FEEDBACK_DAO.getQuestionByQuestionText("¿Surgieron problemas en el momento de la implementacion?");
        } catch (DAOException exception) {
            Logger.getLogger(FeedBackDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
       assertEquals(QUESTION_FOR_TESTING, result);
    } 
    
    @Test
    public void testGetResponsesByIdQuestion() {
        ArrayList<Response> result = new ArrayList<>();
        
        try {
            result = FEEDBACK_DAO.getResponsesByIdQuestionAndIdCollaborativeProject(ID_QUESTION, ID_COLLABORATIVE_PROJECT);
        } catch (DAOException exception) {
            Logger.getLogger(FeedBackDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertEquals(REPONSES_FOR_TESTING, result);
    }
}
