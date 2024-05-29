package mx.fei.coilvicapp.gui.controllers;

/**
 *
 * @author ivanr
 */
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.feedback.FeedbackDAO;
import mx.fei.coilvicapp.logic.feedback.IFeedback;
import mx.fei.coilvicapp.logic.feedback.Question;
import mx.fei.coilvicapp.logic.feedback.Response;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;

public class FeedbackController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private MenuItem professorQuestionsMenuItem;

    @FXML
    private TableView<Question> questionsTableView;

    @FXML
    private TableColumn<Response, String> responseTableColumn;

    @FXML
    private TableColumn<Question, String> questionTableColumn;

    @FXML
    private TableView<Response> responsesTableView;

    @FXML
    private Button seeResponsesButton;

    @FXML
    private MenuItem studentQuestionsMenuItem;

    private final IFeedback FEEDBACK_DAO = new FeedbackDAO();
    private CollaborativeProject collaborativeProject;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        questionTableColumn.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        responseTableColumn.setCellValueFactory(new PropertyValueFactory<>("responseText"));
    }

    @FXML
    void backButtonIsPressed(ActionEvent event) {
        goBack();
    }

    private void goBack() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/CollaborativeProjectDetailsProfessor.fxml"));
            MainApp.changeView(fxmlLoader);
            CollaborativeProjectDetailsProfessorController collaborativeProjectDetailsProfessorController = fxmlLoader.getController();
            collaborativeProjectDetailsProfessorController.setCollaborativeProject(collaborativeProject);
        } catch (IOException exception) {
            Log.getLogger(FeedbackController.class).error(exception.getMessage(), exception);
        }
    }

    @FXML
    void professorQuestionsMenuItemIsSelected(ActionEvent event) {
        ArrayList<Question> questions = new ArrayList<>();
        questionsTableView.getItems().clear();
        try {
            questions = FEEDBACK_DAO.getQuestionByType("Profesor");
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
        questionsTableView.getItems().addAll(questions);
    }

    @FXML
    void seeResponsesButtonIsPressed(ActionEvent event) {
        ArrayList<Response> responses = new ArrayList<>();
        responsesTableView.getItems().clear();
        Question selectedQuestion = questionsTableView.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            try {
                responses = FEEDBACK_DAO.getResponsesByQuestionAndIdCollaborativeProject(selectedQuestion, collaborativeProject.getIdCollaborativeProject());
                System.out.println(responses.toString());
            } catch (DAOException exception) {
                handleDAOException(exception);
            }

            if (responses.isEmpty()) {
                DialogController.getInformativeConfirmationDialog("Sin respuestas", "Aún no hay respuestas sobre esta pregunta");
            } else {
                responsesTableView.getItems().addAll(responses);
            }
        } else {
            DialogController.getInformativeConfirmationDialog("Sin selección", "Seleccione una pregunta para poder ver las respuestas");
        }
    }

    @FXML
    void studenQuestionsMenuItemIsSelected(ActionEvent event) {
        ArrayList<Question> questions = new ArrayList<>();
        questionsTableView.getItems().clear();
        try {
            questions = FEEDBACK_DAO.getQuestionByType("Estudiante");
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
        questionsTableView.getItems().addAll(questions);
    }

    public void setCollaborativeProject(CollaborativeProject collaborativeProject) {
        this.collaborativeProject = collaborativeProject;
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    goBack();
                case FATAL ->
                    MainApp.changeView("/main/MainApp");
            }
        } catch (IOException ioException) {
            Log.getLogger(CollaborativeProjectDetailsProfessorController.class).error(exception.getMessage(), exception);
        }
    }

}
