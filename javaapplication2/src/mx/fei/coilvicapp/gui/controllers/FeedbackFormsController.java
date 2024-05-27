package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.feedback.FeedbackDAO;
import mx.fei.coilvicapp.logic.feedback.IFeedback;
import mx.fei.coilvicapp.logic.feedback.Question;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;

public class FeedbackFormsController implements Initializable {

    @FXML
    private Button acceptButton;

    @FXML
    private Button addQuestionButton;

    @FXML
    private Button backButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Question> professorQuestionsTableView;

    @FXML
    private TextField questionTextField;

    @FXML
    private TableView studentQuestionsTableView;

    @FXML
    private Label titleLabel;

    @FXML
    private TableColumn studentQuestionsTableColumn;

    @FXML
    private TableColumn professorQuestionsTableColumn;

    @FXML
    private TableColumn typeTableColumn;

    @FXML
    private ComboBox typeCombobox;
    private final IFeedback FEEDBACK_DAO = new FeedbackDAO();
    private ArrayList<Question> professorQuestions = new ArrayList<>();
    private ArrayList<Question> studentQuestions = new ArrayList<>();
    private Question selectedQuestion = null;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {

        try {
            professorQuestions = FEEDBACK_DAO.getQuestionByType("Profesor");
            studentQuestions = FEEDBACK_DAO.getQuestionByType("Estudiante");
        } catch (DAOException exception) {

        }

        studentQuestionsTableColumn.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("questionType"));
        professorQuestionsTableColumn.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        studentQuestionsTableView.getItems().addAll(studentQuestions);
        professorQuestionsTableView.getItems().addAll(professorQuestions);
        ObservableList<String> types = FXCollections.observableArrayList(
                "Profesor", "Estudiante-PRE", "Estudiante-POST");

        typeCombobox.setItems(types);

        professorQuestionsTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                studentQuestionsTableView.getSelectionModel().clearSelection();
            }
        });

        studentQuestionsTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                professorQuestionsTableView.getSelectionModel().clearSelection();
            }
        });
    }

    @FXML
    void acceptButtonIsPressed(ActionEvent event) {
        try {
            if (selectedQuestion == null) {
                invokeRegisterQuestion();
                setModeModifyQuestions(false);
            } else {
                invokeUpdateQuestion();
            }
            cleanFields();
        } catch (IllegalArgumentException exception) {
            DialogController.getInvalidDataDialog(exception.getMessage());
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    private void cleanFields() {
        questionTextField.clear();
        typeCombobox.getSelectionModel().clearSelection();
    }

    private void invokeRegisterQuestion() throws DAOException {
        Question question = initializeQuestion();
        int idQuestion = FEEDBACK_DAO.registerQuestion(question);
        if (idQuestion > 0) {
            DialogController.getInformativeConfirmationDialog("Pregunta añadida", 
                    "La pregunta se agregó con éxito");
            question.setIdQuestion(idQuestion);
            if (question.getQuestionType().equals("Profesor")) {
                professorQuestionsTableView.getItems().addAll(question);
            } else {
                studentQuestionsTableView.getItems().addAll(question);
            }
        }
    }

    private void invokeUpdateQuestion() throws DAOException {
        Question newQuestion = initializeQuestion();
        newQuestion.setIdQuestion(selectedQuestion.getIdQuestion());
        if (!newQuestion.equals(selectedQuestion)) {
            System.out.println(newQuestion.getIdQuestion());
            if (FEEDBACK_DAO.updateQuestionTransaction(newQuestion) > 0) {
                DialogController.getInformativeConfirmationDialog("Pregunta actualizada", 
                        "La pregunta se actualizó con éxito");
                updateTableViewForUpdate(newQuestion, selectedQuestion);
                cleanFields();
            }
        }
        setModeModifyQuestions(false);
    }

    private void updateTableViewForUpdate(Question newQuestion, Question oldQuestion) {
        if (newQuestion.getQuestionType().equals("Profesor")) {
            professorQuestionsTableView.getItems().addAll(newQuestion);
        } else {
            studentQuestionsTableView.getItems().addAll(newQuestion);
        }
        if (oldQuestion.getQuestionType().equals("Profesor")) {
            professorQuestionsTableView.getItems().remove(oldQuestion);
        } else {
            studentQuestionsTableView.getItems().remove(oldQuestion);
        }
    }

    private Question initializeQuestion() {
        Question question = new Question();

        question.setQuestionText(questionTextField.getText());
        question.setQuestionType((String) typeCombobox.getValue());
        return question;
    }

    @FXML
    void addQuestionIsPressed(ActionEvent event) {
        selectedQuestion = null;
        setModeModifyQuestions(true);
    }

    @FXML
    void backButtonIsPressed(ActionEvent event) {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/AssistantMainMenu");
        } catch (IOException exception) {
            Log.getLogger(FeedbackFormsController.class).error(exception.getMessage(), exception);
        }
    }

    @FXML
    void cancelButtonIsPressed(ActionEvent event) {
        cleanFields();
        setModeModifyQuestions(false);
    }

    private Question getSelectedQuestion() {
        Question question = new Question();
        if (professorQuestionsTableView.getSelectionModel().getSelectedItem() != null) {
            question = (Question) professorQuestionsTableView.getSelectionModel().getSelectedItem();
        } else if (studentQuestionsTableView.getSelectionModel().getSelectedItem() != null) {
            question = (Question) studentQuestionsTableView.getSelectionModel().getSelectedItem();
        } else {
            DialogController.getInformativeConfirmationDialog("Seleccione una pregunta", 
                    "Debe seleccionar una pregunta para poder modificarla");
        }
        return question;
    }

    @FXML
    void editButtonIsPressed(ActionEvent event) {
        selectedQuestion = getSelectedQuestion();
        if (selectedQuestion.getIdQuestion() > 0) {
            setModeModifyQuestions(true);
            typeCombobox.setValue(selectedQuestion.getQuestionType());
            questionTextField.setText(selectedQuestion.toString());
        }
    }

    @FXML
    void deleteButtonIsPressed(ActionEvent event) {
        selectedQuestion = getSelectedQuestion();
        System.out.println(selectedQuestion.getIdQuestion());
        if (selectedQuestion.getIdQuestion() > 0) {
            if (confirmDelete()) {
                try {
                    FEEDBACK_DAO.deleteQuestion(selectedQuestion);
                    updateTableViewForDelete();
                } catch (DAOException exception) {
                    handleDAOException(exception);
                }
            } else {
                cleanFields();
            }
        }
    }

    private void updateTableViewForDelete() {
        if (selectedQuestion.getQuestionType().equals("Profesor")) {
            professorQuestionsTableView.getItems().remove(selectedQuestion);
        } else {
            studentQuestionsTableView.getItems().remove(selectedQuestion);
        }
    }

    private boolean confirmDelete() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog(
                "Confirmar eliminación", "¿Deseas eliminar esta pregunta?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private void setModeModifyQuestions(boolean isVisible) {
        acceptButton.setVisible(isVisible);
        cancelButton.setVisible(isVisible);
        questionTextField.setVisible(isVisible);
        addQuestionButton.setVisible(!isVisible);
        addQuestionButton.setManaged(!isVisible);
        typeCombobox.setVisible(isVisible);
        deleteButton.setVisible(!isVisible);
        editButton.setVisible(!isVisible);
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/AssistantMainMenu");
                case FATAL ->
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/LoginParticipant");
            }
        } catch (IOException ioException) {
            Log.getLogger(FeedbackFormsController.class).error(exception.getMessage(), exception);
        }
    }
    
}
