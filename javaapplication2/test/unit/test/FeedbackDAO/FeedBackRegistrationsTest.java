package unit.test.FeedbackDAO;


import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.feedback.FeedbackDAO;
import mx.fei.coilvicapp.logic.feedback.Response;
import mx.fei.coilvicapp.logic.feedback.Question;


/**
 *
 * @author ivanr
 */
public class FeedBackRegistrationsTest {

    private final FeedbackDAO FEEDBACK_DAO = new FeedbackDAO();
    private static final Question QUESTION_FOR_TESTING = new Question();
    private static final String QUESTION_TEXT = "¿Recomendarias esta colaboracion?";
    private static final String QUESTION_TYPE = "E";
    
    private static final Question AUX_QUESTION_FOR_TESTING = new Question();
    private static final String AUX_QUESTION_TEXT = "¿Surgieron problemas en el momento de la implementacion?";
    private static final String AUX_QUESTION_TYPE = "P";
    
    private static final ArrayList<Response> RESPONSES_FOR_TESTING = new ArrayList<>();
    private static final String RESPONSE_TEXT = "Todo ocurrio de maravilla";
    private static final int AUX_ID_STUDENTE = 1;
    private static final int AUX_ID_COLLABORATIVE_PROJECT = 1;
    

    public FeedBackRegistrationsTest() {

    }

    @Before
    public void setUp() {
        int idQuestion = 0;
        
        QUESTION_FOR_TESTING.setQuestionText(QUESTION_TEXT);
        QUESTION_FOR_TESTING.setQuestionType(QUESTION_TYPE);
        AUX_QUESTION_FOR_TESTING.setQuestionText(AUX_QUESTION_TEXT);
        AUX_QUESTION_FOR_TESTING.setQuestionType(AUX_QUESTION_TYPE);
        
        Response instance = new Response();
        instance.setResponseText(RESPONSE_TEXT);
        instance.setIdCollaborativeProject(AUX_ID_COLLABORATIVE_PROJECT);
        instance.setIdStudent(AUX_ID_STUDENTE);
        
        
        try {
            idQuestion = FEEDBACK_DAO.registerQuestion(AUX_QUESTION_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(FeedBackRegistrationsTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        instance.setIdQuestion(idQuestion);
        RESPONSES_FOR_TESTING.add(instance);
        RESPONSES_FOR_TESTING.add(instance);
        RESPONSES_FOR_TESTING.add(instance);
        AUX_QUESTION_FOR_TESTING.setIdQuestion(idQuestion);
    }
    
    @Test
    public void testRegisterQuestionSuccess() {
        int idQuestion = 0;
        
        try {
            idQuestion = FEEDBACK_DAO.registerQuestion(QUESTION_FOR_TESTING);
            QUESTION_FOR_TESTING.setIdQuestion(idQuestion);
        } catch (DAOException exception) {
            Logger.getLogger(FeedBackRegistrationsTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        System.out.println(idQuestion);
        assertTrue(idQuestion > 0);
    }
    
    @Test
    public void testRegisterQuestionFailByDuplicatedQuestion () {
        int idQuestion = 0;
        
        QUESTION_FOR_TESTING.setQuestionText(AUX_QUESTION_TEXT);
        QUESTION_FOR_TESTING.setQuestionType(AUX_QUESTION_TYPE);
        try {
            idQuestion = FEEDBACK_DAO.registerQuestion(QUESTION_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(FeedBackRegistrationsTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        QUESTION_FOR_TESTING.setIdQuestion(idQuestion);
        assertTrue(idQuestion > 0);
    }
    
    @Test
    public void testRegisterResponsesSuccess() {
        int result = 0;
        
        try {
            result = FEEDBACK_DAO.registerStudentResponses(RESPONSES_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(FeedBackRegistrationsTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result > 0);
    }
    
//    @After
//    public void tearDown() {
//        try {
//            FEEDBACK_DAO.deleteQuestion(AUX_QUESTION_FOR_TESTING);
//            FEEDBACK_DAO.deleteQuestion(QUESTION_FOR_TESTING);
//        } catch (DAOException exception) {
//            Logger.getLogger(FeedbackRegistrationsTest.class.getName()).log(Level.SEVERE, null, exception);
//        }
//    }
}
