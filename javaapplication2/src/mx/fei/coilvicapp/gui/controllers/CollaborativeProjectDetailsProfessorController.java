package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
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
    private Button backButton;

    @FXML
    private Button finishButton;
    
   

    private CollaborativeProject collaborativeProject;
    private Professor professor = null;
    

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
        
    }

    @FXML
    void finishButtonIsPressed(ActionEvent event) {
        IFeedback feedbackDAO = new FeedbackDAO();
        ICollaborativeProject collaborativeProjectDAO = new CollaborativeProjectDAO();
        try {
            if (feedbackDAO.areThereProfessorQuestions() && collaborativeProjectDAO.hasThreeActivitiesAtLeast(collaborativeProject)) {
                if (!feedbackDAO.hasCompletedProfessorForm(professor, collaborativeProject)) {
                    if (confirmFinish()) {
                        feedbackOnCollaborativeProject();
                        finishCollaborativeProject();
                    }
                } else {
                    DialogController.getInformativeConfirmationDialog("Aviso", "Ya has completado el proceso de retroalimentación");
                }
            } else {
                DialogController.getInformativeConfirmationDialog("Lo sentimos", "No es posible realizar la retroalimentación debido a que aún no hay preguntas para el profesor");
            }
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    private void finishCollaborativeProject() throws DAOException {
        IFeedback feedbackDAO = new FeedbackDAO();

        if (feedbackDAO.hasCompletedProfessorForm(collaborativeProject.getRequestedCourse().getProfessor(), collaborativeProject) && feedbackDAO.hasCompletedProfessorForm(collaborativeProject.getRequesterCourse().getProfessor(), collaborativeProject)) {
            ICollaborativeProject collaborativeProjectDAO = new CollaborativeProjectDAO();

            collaborativeProjectDAO.finalizeCollaborativeProject(collaborativeProject);
            DialogController.getInformativeConfirmationDialog("Aviso", "El proyecto ha finalizado con éxito");
        } else {
            DialogController.getInformativeConfirmationDialog("Aviso", "El proyecto finalizará hasta que ambos profesores realicen la retroalimentación");
        }
    }

    private boolean confirmFinish() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar", "Para concluir el proyecto colaborativo debe completar la retroalimentación ¿Desea continuar?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private void feedbackOnCollaborativeProject() {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/FeedbackOnCollaborativeProject", controller -> {
                FeedbackOnCollaborativeProjectController feedbackOnCollaborativeProjectController = (FeedbackOnCollaborativeProjectController) controller;
                feedbackOnCollaborativeProjectController.setCollaborativeProject(collaborativeProject);
                feedbackOnCollaborativeProjectController.setProfessor(professor);
                feedbackOnCollaborativeProjectController.setTypeQuestiones("Profesor");
            });
        } catch (IOException exception) {
            Log.getLogger(CollaborativeProjectDetailsProfessorController.class).error(exception.getMessage(), exception);
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/ActivitiesManagement.fxml"));

        try {
            MainApp.changeView(fxmlLoader);
            ActivitiesManagementController activitiesManagementController = fxmlLoader.getController();
            activitiesManagementController.setCollaborativeProject(collaborativeProject);
            if (professor != null) {
                activitiesManagementController.setProfessorSession(professor);
            } else {
                activitiesManagementController.setJustVisibleMode(true);
            }
        } catch (IOException exception) {
            Logger.getLogger(CollaborativeProjectDetailsProfessorController.class.getName()).log(Level.SEVERE, null, exception);
        }

    }

    public void setCollaborativeProject(CollaborativeProject collaborativeProject) {
        this.collaborativeProject = collaborativeProject;
        if (!collaborativeProject.getStatus().equals("Aceptado")) {
            finishButton.setVisible(false);
        }
        initializeFields(collaborativeProject);
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
        finishButton.setVisible(true);
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

    @FXML
    void backButtonIsPressed(ActionEvent event) {
        goBack();
    }

    private void goBack() {
        
        try {
            if (professor == null) {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/CollaborativeProjectsManagement");
                
            } else {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/CollaborativeProjectsProfessor");
            }
        } catch (IOException exception) {
            Log.getLogger(CollaborativeProjectDetailsProfessorController.class).error(exception.getMessage(), exception);
        }
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    MainApp.changeView("/main/MainApp");
                case FATAL ->
                    MainApp.changeView("/main/MainApp");
            }
        } catch (IOException ioException) {
            Log.getLogger(CollaborativeProjectDetailsProfessorController.class).error(exception.getMessage(), exception);
        }
    }
}
