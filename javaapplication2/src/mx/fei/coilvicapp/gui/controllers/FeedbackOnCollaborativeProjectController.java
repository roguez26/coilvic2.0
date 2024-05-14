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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import log.Log;
import main.MainApp;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;

/**
 *
 * @author ivanr
 */
public class FeedbackOnCollaborativeProjectController implements Initializable {

    @FXML
    private Button acceptButton;

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

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
        
        ArrayList<Question> questionsList = new ArrayList<>();
        try {
            questionsList = FEEDBACK_DAO.getQuestionByType("E");
        } catch (DAOException exception) {

        }
        initializeResponses(questionsList);
        updateQuestion();
        hideButtons();
    }

    public void initializeResponses(ArrayList<Question> questionsList) {
        for (int i = 0; i < questionsList.size(); i++) {
            Response response = new Response();
            response.setQuestion(questionsList.get(i));
            response.setIdStudent(1);
            response.setIdCollaborativeProject(1);
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
    void acceptButtonIsPressed(ActionEvent event) {
        try {
            getResponseText();
            if (finishConfirmation()) {
                FEEDBACK_DAO.registerStudentResponses(responsesList);
                wasRegisteredConfirmation();
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/CollaborativeProjectDetailsStudent");
            }
        } catch (DAOException exception) {
            handleDAOException(exception);
        } catch (IllegalArgumentException exception) {
            handleValidationException(exception);
        } catch (IOException exception) {
            Log.getLogger(FeedbackOnCollaborativeProjectController.class).error(exception.getMessage(), exception);
        }
    }

    @FXML
    void cancelButtonIsPressed(ActionEvent event) {
        if (cancelConfirmation()) {
            try {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/CollaborativeProjectDetailsStudent");
            } catch (IOException exception) {
                Log.getLogger(FeedbackOnCollaborativeProjectController.class).error(exception.getMessage(), exception);
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

    private boolean wasRegisteredConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog("Aviso", "El proceso de retroalimentación ha salida con éxito");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }

    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getInvalidDataDialog(exception.getMessage());
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/UniversityManager");
                case FATAL ->
                    MainApp.changeView("/main/MainApp");
            }
        } catch (IOException ioException) {
            Log.getLogger(FeedbackOnCollaborativeProjectController.class).error(ioException.getMessage(), ioException);
        }
    }
}
