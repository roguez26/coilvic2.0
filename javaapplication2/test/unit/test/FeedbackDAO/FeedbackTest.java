package unit.test.FeedbackDAO;

import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.feedback.FeedbackDAO;
import mx.fei.coilvicapp.logic.feedback.Response;
import mx.fei.coilvicapp.logic.feedback.Question;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.student.Student;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.junit.Before;
import unit.test.Initializer.TestHelper;

public class FeedbackTest {

    private CollaborativeProject auxCollaborativeProject;
    private Professor auxProfessor;
    private Student auxStudent;

    private final FeedbackDAO FEEDBACK_DAO = new FeedbackDAO();
    private final Question QUESTION_FOR_TESTING = new Question();

    private final ArrayList<Question> QUESTIONS_FOR_TESTING = new ArrayList<>();

    private final ArrayList<Response> RESPONSES_FOR_TESTING = new ArrayList<>();
    private final TestHelper TEST_HELPER = new TestHelper();

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
            if (QUESTION_FOR_TESTING != null) {
                FEEDBACK_DAO.deleteQuestion(QUESTION_FOR_TESTING);
            }
            if (!QUESTIONS_FOR_TESTING.isEmpty()) {
                deleteQuestions();
            }
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
        TEST_HELPER.deleteAll();
    }

    @Test
    public void testAreThereProfessorQuestionsSuccess() throws DAOException {
        boolean result = false;

        initializeProfessorQuestions();
        result = FEEDBACK_DAO.areThereProfessorQuestions();
        System.out.println(result);
        assertTrue(result);
    }

    @Test
    public void testAreThereProfessorQuestionsFailByNoQuestions() throws DAOException {
        boolean result = false;

        result = FEEDBACK_DAO.areThereProfessorQuestions();
        System.out.println(result);
        assertTrue(!result);
    }

    @Test
    public void testAreThereStudentQuestionsSuccess() throws DAOException {
        boolean result = false;

        initializeStudentQuestions();
        result = FEEDBACK_DAO.areThereStudentQuestions();
        System.out.println(result);
        assertTrue(result);
    }

    @Test
    public void testAreThereStudentQuestionsFailByNoQuestions() throws DAOException {
        boolean result = false;

        result = FEEDBACK_DAO.areThereStudentQuestions();
        System.out.println(result);
        assertTrue(!result);
    }

    @Test
    public void testRegisterProfessorResponsesSuccess() throws DAOException {
        int result = 0;

        initializeProfessorQuestions();
        initializeProfessorResponses();
        result = FEEDBACK_DAO.registerProfessorResponses(RESPONSES_FOR_TESTING);
        deleteProfessorResponses();
        System.out.println(result);
        assertTrue(result > 0);
    }

    @Test
    public void testRegisterStudentResponsesSuccess() throws DAOException {
        int result = 0;

        initializeStudentQuestions();
        initializeStudentResponses();
        result = FEEDBACK_DAO.registerStudentResponses(RESPONSES_FOR_TESTING);
        deleteStudentResponses();
        System.out.println(result);
        assertTrue(result > 0);
    }

    @Test
    public void testGetResponsesByQuestionAndIdCollaborativeProjectSuccess() throws DAOException {
        ArrayList<Response> result = new ArrayList<>();
        ArrayList<Response> expected = new ArrayList<>();
        initializeStudentQuestions();
        initializeStudentResponses();

        FEEDBACK_DAO.registerStudentResponses(RESPONSES_FOR_TESTING);
        result = FEEDBACK_DAO.getResponsesByQuestionAndIdCollaborativeProject(QUESTIONS_FOR_TESTING.get(1), auxCollaborativeProject.getIdCollaborativeProject());
        expected.add(RESPONSES_FOR_TESTING.get(1));
        deleteStudentResponses();
        System.out.println(result);
        assertEquals(expected, result);
    }

    @Test
    public void testRegisterQuestionSuccess() throws DAOException {
        int idQuestion = 0;
        initializeQuestion();

        idQuestion = FEEDBACK_DAO.registerQuestion(QUESTION_FOR_TESTING);
        QUESTION_FOR_TESTING.setIdQuestion(idQuestion);
        System.out.println(idQuestion);
        assertTrue(idQuestion > 0);
    }

    @Test
    public void testUpdateQuestionSuccess() throws DAOException {
        int result = 0;
        initializeQuestion();

        QUESTION_FOR_TESTING.setIdQuestion(FEEDBACK_DAO.registerQuestion(QUESTION_FOR_TESTING));
        QUESTION_FOR_TESTING.setQuestionText("¿Nueva pregunta?");
        result = FEEDBACK_DAO.updateQuestionTransaction(QUESTION_FOR_TESTING);
        System.out.println(result);
        assertTrue(result > 0);
    }

    @Test(expected = DAOException.class)
    public void testUpdateQuestionFailByDuplicatedQuestion() throws DAOException {
        initializeStudentQuestions();

        QUESTION_FOR_TESTING.setQuestionType(QUESTIONS_FOR_TESTING.get(2).getQuestionType());
        QUESTION_FOR_TESTING.setQuestionText(QUESTIONS_FOR_TESTING.get(2).getQuestionText());
        FEEDBACK_DAO.registerQuestion(QUESTION_FOR_TESTING);
    }

    @Test(expected = DAOException.class)
    public void testRegisterQuestionFailByDuplicatedQuestion() throws DAOException {
        initializeStudentQuestions();
        QUESTION_FOR_TESTING.setQuestionText(QUESTIONS_FOR_TESTING.get(2).getQuestionText());
        QUESTION_FOR_TESTING.setQuestionType(QUESTIONS_FOR_TESTING.get(2).getQuestionType());
        FEEDBACK_DAO.registerQuestion(QUESTION_FOR_TESTING);
    }

    @Test
    public void testDeleteQuestionSuccess() throws DAOException {
        int result = 0;

        initializeQuestion();
        QUESTION_FOR_TESTING.setIdQuestion(FEEDBACK_DAO.registerQuestion(QUESTION_FOR_TESTING));
        result = FEEDBACK_DAO.deleteQuestion(QUESTION_FOR_TESTING);
        System.out.println(result);
        assertTrue(result > 0);
    }

    @Test(expected = DAOException.class)
    public void testDeleteQuestionFailByQuestionUsed() throws DAOException {
        initializeStudentQuestions();
        initializeStudentResponses();
        FEEDBACK_DAO.registerStudentResponses(RESPONSES_FOR_TESTING);
        try {
            FEEDBACK_DAO.deleteQuestion(QUESTIONS_FOR_TESTING.get(1));
        } finally {
            deleteStudentResponses();
        }
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
            QUESTIONS_FOR_TESTING.clear();
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
            RESPONSES_FOR_TESTING.clear();
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
            RESPONSES_FOR_TESTING.clear();
        } catch (DAOException exception) {
            Log.getLogger(FeedbackTest.class).error(exception.getMessage(), exception);
        }
    }

}
