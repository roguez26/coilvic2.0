package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import mx.fei.coilvicapp.logic.collaborativeproject.ICollaborativeProject;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.FileManager;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;
import mx.fei.coilvicapp.logic.professor.Professor;

/**
 *
 * @author ivanr
 */
public class ValidateCollaborativeProjectController implements Initializable {

    @FXML
    private VBox backgroundVBox;

    @FXML
    private Label collaborativeProjectNameLabel;

    @FXML
    private TextField collaborativeProjectNameTextField;

    @FXML
    private Button openSyllabusButton;

    @FXML
    private Button rejectButton;
    @FXML
    private Button acceptButton;
    @FXML
    private Button backButton;

    @FXML
    private Label professorOneLabel;

    @FXML
    private TextField professorOneTextField;

    @FXML
    private Label professorTwoLabel;

    @FXML
    private TextField professorTwoTextField;

    @FXML
    private Label titleLabel;

    @FXML
    private Label universityOneLabel;

    @FXML
    private TextField universityOneTextField;

    @FXML
    private Label universityTwoLabel;

    @FXML
    private TextField universityTwoTextField;
    private CollaborativeProject collaborativeProject;
    private final ICollaborativeProject COLLABORATIVE_PROJECT_DAO = new CollaborativeProjectDAO();

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {

    }

    @FXML
    void openSyllabusButtonIsPressed(ActionEvent event) {
        FileManager fileManager = new FileManager();
        try {
            fileManager.openFile(collaborativeProject.getSyllabusPath());
        } catch (IOException exception) {
            handleIOException(exception);
        } catch (IllegalArgumentException exception) {
            handleValidationException(exception);
        }

    }

    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getInformativeConfirmationDialog("Lo sentimos", exception.getMessage());
    }

    private void handleIOException(IOException exception) {
        DialogController.getInformativeConfirmationDialog("Lo sentimos", exception.getMessage());
    }

    @FXML
    void rejectButtonIsPressed(ActionEvent event) {
        if (confirmReject()) {
            try {
                COLLABORATIVE_PROJECT_DAO.evaluateCollaborativeProjectProposal(collaborativeProject, "Rechazado");
                notifyProfessor(collaborativeProject.getRequesterCourse().getProfessor());
                wasValidatedConfirmation();
            } catch (DAOException exception) {
                handleDAOException(exception);
            }
        }
    }
    
    private void wasValidatedConfirmation() {
         DialogController.getInformativeConfirmationDialog("Proyecto Validado", "El proyecto colaborativo fue validado con éxito");
         goBack();
    }

    private boolean confirmReject() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar rechazo", "¿Deseas rechazar este proyecto colaborativo?");
        return response.isPresent() && response.get() == DialogController.BUTTON_YES;
    }

    private boolean confirmAccept() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar Aceptacion", "¿Deseas aceptar este proyecto colaborativo?");
        return response.isPresent() && response.get() == DialogController.BUTTON_YES;
    }

    @FXML
    void acceptButtonIsPressed(ActionEvent event) {
        if (confirmAccept()) {
            try {
                COLLABORATIVE_PROJECT_DAO.evaluateCollaborativeProjectProposal(collaborativeProject, "Aceptado");
                notifyProfessor(collaborativeProject.getRequesterCourse().getProfessor());
                wasValidatedConfirmation();
            } catch (DAOException exception) {
                handleDAOException(exception);
            }
        }
    }

    @FXML
    void backButtonIsPressed(ActionEvent event) {
        goBack();
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
            Log.getLogger(RegisterStudentController.class).error(exception.getMessage(), exception);
        }
    }

    private void notifyProfessor(Professor professor) {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/NotifyProfessor", controller -> {
                NotifyProfessorController notifyProfessorController = (NotifyProfessorController) controller;
                notifyProfessorController.setProfessor(collaborativeProject.getRequesterCourse().getProfessor());
            });
        } catch (IOException exception) {
            Log.getLogger(LoginParticipantController.class).error(exception.getMessage(), exception);
        }

    }

    private void fillTextFields(CollaborativeProject collaborativeProject) {
        collaborativeProjectNameTextField.setText(collaborativeProject.getName());
        professorOneTextField.setText(collaborativeProject.getRequestedCourse().getProfessor().getName());
        professorTwoTextField.setText(collaborativeProject.getRequesterCourse().getProfessor().getName());
        universityOneTextField.setText(collaborativeProject.getRequestedCourse().getProfessor().getUniversity().getName());
        universityTwoTextField.setText(collaborativeProject.getRequesterCourse().getProfessor().getUniversity().getName());
    }

    public void setCollaborativeProject(CollaborativeProject collaborativeProject) {
        this.collaborativeProject = collaborativeProject;
        fillTextFields(collaborativeProject);
    }
    
    private void goBack() {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/CollaborativeProjectsManagement");
        } catch (IOException exception) {
            Log.getLogger(ValidateCollaborativeProjectController.class).error(exception.getMessage(), exception);
        }
    }
}
