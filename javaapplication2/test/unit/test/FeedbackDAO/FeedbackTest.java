package unit.test.FeedbackDAO;

import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.feedback.FeedbackDAO;
import mx.fei.coilvicapp.logic.feedback.Response;
import mx.fei.coilvicapp.logic.feedback.Question;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.student.Student;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import unit.test.Initializer.TestHelper;

public class FeedbackTest {

    private CollaborativeProject auxCollaborativeProject;
    private Professor auxProfessor;
    private Student auxStudent;

    private final FeedbackDAO FEEDBACK_DAO = new FeedbackDAO();
    private static final Question QUESTION_FOR_TESTING = new Question();

    private static final ArrayList<Question> QUESTIONS_FOR_TESTING = new ArrayList<>();

    private static final ArrayList<Response> RESPONSES_FOR_TESTING = new ArrayList<>();
    private final TestHelper TEST_HELPER = new TestHelper();

    public FeedbackTest() {

    }

    @Before
    public void setUp() {
        TEST_HELPER.initializeCollaborativeProject();
        auxCollaborativeProject = TEST_HELPER.getCollaborativeProject();
        auxProfessor = TEST_HELPER.getProfessorOne();
        auxStudent = TEST_HELPER.getStudentOne();
    }

    @After
    public void tearDown() {
        try {
            FEEDBACK_DAO.deleteQuestion(QUESTION_FOR_TESTING);
            if (!QUESTIONS_FOR_TESTING.isEmpty()) {
                deleteQuestions();
            }
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
        TEST_HELPER.deleteAll();
    }

    @Test
    public void testAreThereProfessorQuestionsSuccess() {
        boolean result = false;
        initializeProfessorQuestions();
        try {
            result = FEEDBACK_DAO.areThereProfessorQuestions();
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(result);
        assertTrue(result);
    }

    @Test
    public void testAreThereProfessorQuestionsFailByNoQuestions() {
        boolean result = false;
        try {
            result = FEEDBACK_DAO.areThereProfessorQuestions();
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(result);
        assertTrue(!result);
    }

    @Test
    public void testAreThereStudentQuestionsSuccess() {
        boolean result = false;
        initializeStudentQuestions();
        try {
            result = FEEDBACK_DAO.areThereStudentQuestions();
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(result);
        assertTrue(result);
    }

    @Test
    public void testAreThereStudentQuestionsFailByNoQuestions() {
        boolean result = false;
        try {
            result = FEEDBACK_DAO.areThereStudentQuestions();
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(result);
        assertTrue(!result);
    }

    @Test
    public void testRegisterProfessorResponsesSuccess() {
        int result = 0;
        initializeProfessorQuestions();
        initializeProfessorResponses();
        try {
            result = FEEDBACK_DAO.registerProfessorResponses(RESPONSES_FOR_TESTING);
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
        deleteProfessorResponses();
        System.out.println(result);
        assertTrue(result > 0);
    }

    @Test
    public void testRegisterStudentResponsesSuccess() {
        int result = 0;
        initializeStudentQuestions();
        initializeStudentResponses();
        try {
            result = FEEDBACK_DAO.registerStudentResponses(RESPONSES_FOR_TESTING);
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
        deleteStudentResponses();
        System.out.println(result);
        assertTrue(result > 0);
    }

    @Test
    public void testGetResponsesByQuestionAndIdCollaborativeProjectSuccess() {
        ArrayList<Response> result = new ArrayList<>();
        ArrayList<Response> expected = new ArrayList<>();
        initializeStudentQuestions();
        initializeStudentResponses();
        try {
            FEEDBACK_DAO.registerStudentResponses(RESPONSES_FOR_TESTING);
            result = FEEDBACK_DAO.getResponsesByQuestionAndIdCollaborativeProject(QUESTIONS_FOR_TESTING.get(1), auxCollaborativeProject.getIdCollaborativeProject());
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
        expected.add(RESPONSES_FOR_TESTING.get(1));
        deleteStudentResponses();
        System.out.println(result);
        assertEquals(expected, result);
    }

    @Test
    public void testRegisterQuestionSuccess() {
        int idQuestion = 0;
        initializeQuestion();
        try {
            idQuestion = FEEDBACK_DAO.registerQuestion(QUESTION_FOR_TESTING);
            QUESTION_FOR_TESTING.setIdQuestion(idQuestion);
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(idQuestion);
        assertTrue(idQuestion > 0);
    }

    @Test
    public void testUpdateQuestionSuccess() {
        int result = 0;
        initializeQuestion();
        try {
            QUESTION_FOR_TESTING.setIdQuestion(FEEDBACK_DAO.registerQuestion(QUESTION_FOR_TESTING));
            QUESTION_FOR_TESTING.setQuestionText("¿Nueva pregunta?");
            result = FEEDBACK_DAO.updateQuestionTransaction(QUESTION_FOR_TESTING);
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(result);
        assertTrue(result > 0);
    }

    @Test
    public void testUpdateQuestionFailByDuplicatedQuestion() {
        initializeStudentQuestions();

        QUESTION_FOR_TESTING.setQuestionType(QUESTIONS_FOR_TESTING.get(2).getQuestionType());
        QUESTION_FOR_TESTING.setQuestionText(QUESTIONS_FOR_TESTING.get(2).getQuestionText());
        
        DAOException exception  = assertThrows(DAOException.class, () -> FEEDBACK_DAO.registerQuestion(QUESTION_FOR_TESTING));
        System.out.println(exception.getMessage());
    }

    @Test
    public void testRegisterQuestionFailByDuplicatedQuestion() {
        initializeStudentQuestions();
        QUESTION_FOR_TESTING.setQuestionText(QUESTIONS_FOR_TESTING.get(2).getQuestionText());
        QUESTION_FOR_TESTING.setQuestionType(QUESTIONS_FOR_TESTING.get(2).getQuestionType());

        DAOException exception = assertThrows(DAOException.class, () -> FEEDBACK_DAO.registerQuestion(QUESTION_FOR_TESTING));

        System.out.println(exception.getMessage());
    }
    
    @Test
    public void testDeleteQuestionSuccess() {
        int result = 0;
        initializeQuestion();
        try {
            QUESTION_FOR_TESTING.setIdQuestion(FEEDBACK_DAO.registerQuestion(QUESTION_FOR_TESTING));
            result = FEEDBACK_DAO.deleteQuestion(QUESTION_FOR_TESTING);
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(result);
        assertTrue(result > 0);
    }
    
    @Test
    public void testDeleteQuestionFailByQuestionUsed() {
        initializeStudentQuestions();
        initializeStudentResponses();
        try {
            FEEDBACK_DAO.registerStudentResponses(RESPONSES_FOR_TESTING);
            
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
        DAOException exception = assertThrows(DAOException.class, () -> FEEDBACK_DAO.deleteQuestion(QUESTIONS_FOR_TESTING.get(1)));
        System.out.println(exception.getMessage());
        deleteStudentResponses();
    }

    public void initializeProfessorQuestions() {
        String questionTexts[] = {"¿Qué te pareció la experiencia?", "¿El profesor cumplió con sus "
            + "resposabilidades?", "¿Qué fue los mejor de esta experiencia?"};
        String questionTypes[] = {"Profesor", "Profesor", "Profesor"};
        try {
            for (int i = 0; i < 3; i++) {
                Question question = new Question();
                question.setQuestionText(questionTexts[i]);
                question.setQuestionType(questionTypes[i]);
                question.setIdQuestion(FEEDBACK_DAO.registerQuestion(question));
                QUESTIONS_FOR_TESTING.add(question);
            }
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
    }

    public void deleteQuestions() {
        try {
            for (int i = 0; i < 3; i++) {
                FEEDBACK_DAO.deleteQuestion(QUESTIONS_FOR_TESTING.get(i));
            }
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
    }

    public void initializeQuestion() {
        QUESTION_FOR_TESTING.setQuestionText("¿Recomendarias esta colaboracion?");
        QUESTION_FOR_TESTING.setQuestionType("Estudiante-POST");
    }

    public void initializeStudentQuestions() {
        String questionTexts[] = {"¿Qué te pareció la experiencia?", "¿El profesor cumplió con sus "
            + "resposabilidades?", "¿Qué fue los mejor de esta experiencia?"};
        String questionTypes[] = {"Estudiante-POST", "Estudiante-POST", "Estudiante-POST"};
        try {
            for (int i = 0; i < 3; i++) {
                Question question = new Question();
                question.setQuestionText(questionTexts[i]);
                question.setQuestionType(questionTypes[i]);
                question.setIdQuestion(FEEDBACK_DAO.registerQuestion(question));
                QUESTIONS_FOR_TESTING.add(question);
            }
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
    }

    public void initializeProfessorResponses() {
        String responseTexts[] = {"Todo salió perfecto", "Cumplió con todas sus actividades",
            "Los alumnos formaron nuevos amigos"};

        for (int i = 0; i < 3; i++) {
            Response response = new Response();
            response.setQuestion(QUESTIONS_FOR_TESTING.get(i));
            response.setResponseText(responseTexts[i]);
            response.setIdCollaborativeProject(auxCollaborativeProject.getIdCollaborativeProject());
            response.setIdParticipant(auxProfessor.getIdProfessor());
            RESPONSES_FOR_TESTING.add(response);
        }
    }

    public void initializeStudentResponses() {
        String responseTexts[] = {"Todo salió perfecto", "Cumplió con todas sus actividades",
            "Conocer nuevos amigos"};

        for (int i = 0; i < 3; i++) {
            Response response = new Response();
            response.setQuestion(QUESTIONS_FOR_TESTING.get(i));
            response.setResponseText(responseTexts[i]);
            response.setIdCollaborativeProject(auxCollaborativeProject.getIdCollaborativeProject());
            response.setIdParticipant(auxStudent.getIdStudent());
            RESPONSES_FOR_TESTING.add(response);
        }
    }

    public void deleteProfessorResponses() {
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        try {
            feedbackDAO.deleteProfessorResponsesByIdAndIdCollaborativeProject(
                    auxProfessor.getIdProfessor(),
                    auxCollaborativeProject.getIdCollaborativeProject());
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
    }

    public void deleteStudentResponses() {
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        try {
            feedbackDAO.deleteStudentResponsesByIdAndIdCollaborativeProject(
                    auxStudent.getIdStudent(),
                    auxCollaborativeProject.getIdCollaborativeProject());
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
    }

}
