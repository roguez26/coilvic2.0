package mx.fei.coilvicapp.gui.controllers;

import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.collaborativeproject.ICollaborativeProject;
import mx.fei.coilvicapp.logic.feedback.FeedbackDAO;
import mx.fei.coilvicapp.logic.feedback.IFeedback;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.FileManager;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;
import mx.fei.coilvicapp.logic.professor.Professor;

/**
 *
 * @author ivanr
 */
public class CollaborativeProjectDetailsProfessorController implements Initializable {

    @FXML
    private TextField codeTextField;

    @FXML
    private TextField courseOneTextField;

    @FXML
    private TextField courseTwoTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextArea generalObjetiveTextArea;

    @FXML
    private TextField modalityTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField professorOneTextField;

    @FXML
    private TextField professorTwoTextField;

    @FXML
    private Button seeActivitiesButton;

    @FXML
    private Button seeFeedbackButton;

    @FXML
    private Button backButton;

    @FXML
    private Button finishButton;

    private CollaborativeProject collaborativeProject;
    private Professor professor;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
    }

    @FXML
    void finishButtonIsPressed(ActionEvent event) {
        IFeedback feedbackDAO = new FeedbackDAO();
        ICollaborativeProject collaborativeProjectDAO = new CollaborativeProjectDAO();
        try {
            if (feedbackDAO.areThereProfessorQuestions() && collaborativeProjectDAO.hasThreeActivitiesAtLeast(
                    collaborativeProject)) {
                if (!feedbackDAO.hasCompletedProfessorForm(professor, collaborativeProject)) {
                    if (confirmFinish()) {
                        feedbackOnCollaborativeProject();
                        finishCollaborativeProject();
                    }
                } else {
                    DialogController.getInformativeConfirmationDialog("Aviso", "Ya has completado el proceso de "
                            + "retroalimentación");
                }
            } else {
                DialogController.getInformativeConfirmationDialog("Lo sentimos", "No es posible realizar la "
                        + "retroalimentación debido a que aún no hay preguntas para el profesor");
            }
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    @FXML
    void backButtonIsPressed(ActionEvent event) {
        goBack();
    }

    @FXML
    void seeFeedbackButtonIsPressed(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/"
                    + "Feedback.fxml"));
            MainApp.changeView(fxmlLoader);
            FeedbackController feedbackController = fxmlLoader.getController();
            feedbackController.setCollaborativeProject(collaborativeProject);
        } catch (IOException exception) {
            Log.getLogger(CollaborativeProjectDetailsProfessorController.class).error(exception.getMessage(), 
                    exception);
        }
    }

    private void finishCollaborativeProject() throws DAOException {
        IFeedback feedbackDAO = new FeedbackDAO();

        if (feedbackDAO.hasCompletedProfessorForm(collaborativeProject.getRequestedCourse().getProfessor(),
                collaborativeProject) && feedbackDAO.hasCompletedProfessorForm(
                        collaborativeProject.getRequesterCourse().getProfessor(), collaborativeProject)) {
            ICollaborativeProject collaborativeProjectDAO = new CollaborativeProjectDAO();

            collaborativeProjectDAO.finalizeCollaborativeProject(collaborativeProject);
            DialogController.getInformativeConfirmationDialog("Aviso", "El proyecto ha finalizado con éxito");
        } else {
            DialogController.getInformativeConfirmationDialog("Aviso", "El proyecto finalizará hasta que ambos"
                    + " profesores realicen la retroalimentación");
        }
    }

    private boolean confirmFinish() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar", "Para concluir "
                + "el proyecto colaborativo debe completar la retroalimentación ¿Desea continuar?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private void feedbackOnCollaborativeProject() {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/FeedbackOnCollaborativeProject", controller -> {
                FeedbackOnCollaborativeProjectController feedbackOnCollaborativeProjectController = 
                        (FeedbackOnCollaborativeProjectController) controller;
                feedbackOnCollaborativeProjectController.setCollaborativeProject(collaborativeProject);
                feedbackOnCollaborativeProjectController.setProfessor(professor);
                feedbackOnCollaborativeProjectController.setTypeQuestiones("Profesor");
            });
        } catch (IOException exception) {
            Log.getLogger(CollaborativeProjectDetailsProfessorController.class).error(exception.getMessage(), 
                    exception);
        }

    }

    @FXML
    void seeSyllabusButtonIsPressed(ActionEvent event) {
        FileManager fileManager = new FileManager();
        
        try {
            fileManager.openFile(collaborativeProject.getSyllabusPath());
        } catch (IllegalArgumentException exception) {
            DialogController.getInformativeConfirmationDialog("Lo sentimos", exception.getMessage());
        } catch (IOException exception) {
            DialogController.getInformativeConfirmationDialog("Algo salio mal", exception.getMessage());
        }
    }

    @FXML
    void seeActivitiesButtonIsPressed(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/"
                + "ActivitiesManagement.fxml"));

        try {
            MainApp.changeView(fxmlLoader);
            ActivitiesManagementController activitiesManagementController = fxmlLoader.getController();
            if (professor != null) {
                activitiesManagementController.setProfessorSession(professor);
            } else {
                activitiesManagementController.setJustVisibleMode(true);
            }
            activitiesManagementController.setCollaborativeProject(collaborativeProject);
        } catch (IOException exception) {
            Log.getLogger(CollaborativeProjectDetailsProfessorController.class).error(exception.getMessage(), 
                    exception);
        }

    }

    public void setCollaborativeProject(CollaborativeProject collaborativeProject) {
        this.collaborativeProject = collaborativeProject;
        if (collaborativeProject.getStatus().equals("Aceptado")) {
            finishButton.setVisible(true);
        }
        if (collaborativeProject.getStatus().equals("Pendiente") || collaborativeProject.getStatus().equals("Rechazado")) {
            seeActivitiesButton.setVisible(false);
        }

        initializeFields(collaborativeProject);
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
        seeFeedbackButton.setVisible(false);
    }

    public void initializeFields(CollaborativeProject collaborativeProject) {
        if (collaborativeProject != null) {
            codeTextField.setText(collaborativeProject.getCode());
            courseOneTextField.setText(collaborativeProject.getRequesterCourse().toString());
            courseTwoTextField.setText(collaborativeProject.getRequestedCourse().toString());
            descriptionTextArea.setText(collaborativeProject.getDescription());
            generalObjetiveTextArea.setText(collaborativeProject.getGeneralObjective());
            modalityTextField.setText(collaborativeProject.getModality().toString());
            nameTextField.setText(collaborativeProject.getName());
            professorOneTextField.setText(collaborativeProject.getRequesterCourse().getProfessor().toString());
            professorTwoTextField.setText(collaborativeProject.getRequestedCourse().getProfessor().toString());
        }

    }

    private void goBack() {
        try {
            if (professor == null) {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/CollaborativeProjectsManagement");
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/"
                        + "CollaborativeProjectsProfessor.fxml"));
                MainApp.changeView(fxmlLoader);
                CollaborativeProjectsProfessorController collaborativeProjectsProfessorController = 
                        fxmlLoader.getController();
                collaborativeProjectsProfessorController.setProfessor(professor);
            }
        } catch (IOException exception) {
            Log.getLogger(CollaborativeProjectDetailsProfessorController.class).error(exception.getMessage(), 
                    exception);
        }
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    goBack();
                case FATAL ->
                    MainApp.handleFatal();
            }
        } catch (IOException ioException) {
            Log.getLogger(CollaborativeProjectDetailsProfessorController.class).error(ioException.getMessage(),
                    ioException);
        }
    }
}
