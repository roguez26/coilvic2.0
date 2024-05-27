package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import mx.fei.coilvicapp.logic.feedback.FeedbackDAO;
import mx.fei.coilvicapp.logic.feedback.IFeedback;
import mx.fei.coilvicapp.logic.feedback.Question;
import mx.fei.coilvicapp.logic.feedback.Response;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.student.Student;

/**
 *
 * @author ivanr
 */
public class FeedbackOnCollaborativeProjectController implements Initializable {

    @FXML
    private Button finishButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;

    @FXML
    private Label questionNumberLabel;

    @FXML
    private Label questionTextLabel;

    @FXML
    private TextArea responseTextArea;

    @FXML
    private Label titleLabel;

    private final IFeedback FEEDBACK_DAO = new FeedbackDAO();
    private final ArrayList<Response> responsesList = new ArrayList<>();
    private int currentQuestion;
    private String questionType;
    private Professor professor = null;
    private Student student = null;
    CollaborativeProject collaborativeProject;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {

    }

    private void initializeQuestions(String questionType) {
        ArrayList<Question> questionsList = new ArrayList<>();
        try {
            questionsList = FEEDBACK_DAO.getQuestionByType(questionType);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
        initializeResponses(questionsList);
        updateQuestion();
        hideButtons();
    }

    public void initializeResponses(ArrayList<Question> questionsList) {
        int idParticipant = 0;
        if (student != null) {
            idParticipant = student.getIdStudent();
        } else if (professor != null) {
            idParticipant = professor.getIdProfessor();
        }
        for (int i = 0; i < questionsList.size(); i++) {
            Response response = new Response();
            response.setQuestion(questionsList.get(i));
            response.setIdParticipant(idParticipant);
            response.setIdCollaborativeProject(collaborativeProject.getIdCollaborativeProject());
            responsesList.add(response);
        }
    }

    public void updateQuestion() {
        if (currentQuestion < responsesList.size()) {
            questionNumberLabel.setText(String.valueOf(currentQuestion + 1) + "/" + String.valueOf(responsesList.size()));
            questionTextLabel.setText(responsesList.get(currentQuestion).getQuestion().getQuestionText());
            responseTextArea.setText(responsesList.get(currentQuestion).getResponseText());
        }
    }

    public void getResponseText() {
        responsesList.get(currentQuestion).setResponseText(responseTextArea.getText());
        responseTextArea.setText("");
    }

    @FXML
    void finishButtonIsPressed(ActionEvent event) {
        try {
            getResponseText();
            if (finishConfirmation()) {
                if (student != null) {
                    FEEDBACK_DAO.registerStudentResponses(responsesList);
                    changeToCollaborativeProjectDetailsStudent();
                } else if (professor != null) {
                    FEEDBACK_DAO.registerProfessorResponses(responsesList);
                    closeWindow();
                }
            }
        } catch (DAOException exception) {
            handleDAOException(exception);
        } catch (IllegalArgumentException exception) {
            handleValidationException(exception);
        }
    }

    @FXML
    void cancelButtonIsPressed(ActionEvent event) {
        if (cancelConfirmation()) {
            if (student != null) {
                changeToCollaborativeProjectDetailsStudent();
            } else if (professor != null) {
                closeWindow();
            }
        }
    }

    @FXML
    void nextButtonIsPressed(ActionEvent event) {
        if (currentQuestion < responsesList.size() - 1) {
            try {
                getResponseText();
                currentQuestion++;
                updateQuestion();
            } catch (IllegalArgumentException exception) {
                handleValidationException(exception);
            }
            hideButtons();
        }
    }

    @FXML
    void previousButtonIsPressed(ActionEvent event) {
        if (currentQuestion > 0) {
            currentQuestion--;
            updateQuestion();
            hideButtons();
        }
    }

    private boolean cancelConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cancelación", "¿Deseas cancelar la retroalimentación?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean finishConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar finalización", "¿Deseas finalizar el proceso de retroalimentación?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private void hideButtons() {
        previousButton.setVisible(!(currentQuestion == 0));
        nextButton.setVisible(!(currentQuestion == responsesList.size() - 1));
    }

    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getInvalidDataDialog(exception.getMessage());
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR -> {
                    if (student != null) {
                        changeToCollaborativeProjectDetailsStudent();
                    } else if (professor != null) {
                        closeWindow();
                    }
                }
                case FATAL ->
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/LoginParticipant");
            }
        } catch (IOException ioException) {
            Log.getLogger(FeedbackOnCollaborativeProjectController.class).error(ioException.getMessage(), ioException);
        }
    }

    private void changeToCollaborativeProjectDetailsStudent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/CollaborativeProjectDetailsStudent.fxml"));
        try {
            MainApp.changeView(fxmlLoader);
            CollaborativeProjectDetailsStudentController collaborativeProjectDetailsStudentController = fxmlLoader.getController();
            collaborativeProjectDetailsStudentController.setStudent(student);
            collaborativeProjectDetailsStudentController.setCollaborativeProject(collaborativeProject);
        } catch (IOException exception) {
            Log.getLogger(FeedbackOnCollaborativeProjectController.class).error(exception.getMessage(), exception);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) finishButton.getScene().getWindow();
        stage.close();
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setTypeQuestiones(String questionType) {
        this.questionType = questionType;
        initializeQuestions(questionType);
    }

    public void setProfessor(Professor professor) {
        System.out.println(professor.getName());
        this.professor = professor;
    }

    public void setCollaborativeProject(CollaborativeProject collaborativeProject) {
        this.collaborativeProject = collaborativeProject;
        System.out.println(collaborativeProject);
    }
}
