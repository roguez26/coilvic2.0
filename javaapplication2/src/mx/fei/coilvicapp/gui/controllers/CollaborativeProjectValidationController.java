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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

public class CollaborativeProjectValidationController implements Initializable {

    @FXML
    private TextField nameTextField;

    @FXML
    private Button acceptButton;

    @FXML
    private Button backButton;

    @FXML
    private Label codeLabel;

    @FXML
    private TextField codeTextField;

    @FXML
    private Label collaborativeProjectNameLabel;

    @FXML
    private TextField collaborativeProjectNameTextField;

    @FXML
    private Label courseOneLabel;

    @FXML
    private TextField courseOneTextField;

    @FXML
    private Label courseTwoLabel;

    @FXML
    private TextField courseTwoTextField;

    @FXML
    private Label descriptionLabel;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Label generalObjectiveLabel;

    @FXML
    private TextArea generalObjectiveTextArea;

    @FXML
    private Label modalityLabel;

    @FXML
    private TextField modalityTextField;

    @FXML
    private Button openSyllabusButton;

    @FXML
    private Label professorOneLabel;

    @FXML
    private TextField professorOneTextField;

    @FXML
    private Label professorTwoLabel;

    @FXML
    private TextField professorTwoTextField;

    @FXML
    private Button rejectButton;

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
            ICollaborativeProject collaborativeProjectDAO = new CollaborativeProjectDAO();
            try {
                collaborativeProjectDAO.evaluateCollaborativeProjectProposal(collaborativeProject, "Rechazado");
                notifyProfessor(collaborativeProject.getRequesterCourse().getProfessor());
                wasValidatedConfirmation();
            } catch (DAOException exception) {
                handleDAOException(exception);
            }
        }
    }

    private void wasValidatedConfirmation() {
        DialogController.getInformativeConfirmationDialog("Proyecto Validado", "El proyecto colaborativo "
                + "fue validado con éxito");
        goBack();
    }

    private boolean confirmReject() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar rechazo", "¿Deseas"
                + " rechazar este proyecto colaborativo?");
        return response.isPresent() && response.get() == DialogController.BUTTON_YES;
    }

    private boolean confirmAccept() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar aceptación",
                "¿Deseas aceptar este proyecto colaborativo?");
        return response.isPresent() && response.get() == DialogController.BUTTON_YES;
    }

    @FXML
    void acceptButtonIsPressed(ActionEvent event) {
        if (confirmAccept()) {
            ICollaborativeProject collaborativeProjectDAO = new CollaborativeProjectDAO();
            try {
                collaborativeProjectDAO.evaluateCollaborativeProjectProposal(collaborativeProject, "Aceptado");
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
                    goBack();
                case FATAL ->
                    MainApp.handleFatal();
                default -> {
                }
            }
        } catch (IOException ioException) {
            Log.getLogger(CollaborativeProjectValidationController.class).error(ioException.getMessage(),
                    ioException);
        }
    }

    private void notifyProfessor(Professor professor) {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/ProfessorNotification", controller -> {
                ProfessorNotificationController notifyProfessorController = (ProfessorNotificationController) controller;
                notifyProfessorController.setProfessor(collaborativeProject.getRequesterCourse().getProfessor());
            });
        } catch (IOException exception) {
            Log.getLogger(CollaborativeProjectValidationController.class).error(exception.getMessage(), exception);
        }
    }

    public void setCollaborativeProject(CollaborativeProject collaborativeProject) {
        this.collaborativeProject = collaborativeProject;
        initializeFields(collaborativeProject);
    }

    public void initializeFields(CollaborativeProject collaborativeProject) {
        if (collaborativeProject != null) {
            codeTextField.setText(collaborativeProject.getCode());
            courseOneTextField.setText(collaborativeProject.getRequesterCourse().toString());
            courseTwoTextField.setText(collaborativeProject.getRequestedCourse().toString());
            descriptionTextArea.setText(collaborativeProject.getDescription());
            generalObjectiveTextArea.setText(collaborativeProject.getGeneralObjective());
            modalityTextField.setText(collaborativeProject.getModality().toString());
            nameTextField.setText(collaborativeProject.getName());
            professorOneTextField.setText(collaborativeProject.getRequesterCourse().getProfessor().toString());
            professorTwoTextField.setText(collaborativeProject.getRequestedCourse().getProfessor().toString());
        }
    }

    private void goBack() {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/CollaborativeProjectsManagement");
        } catch (IOException exception) {
            Log.getLogger(CollaborativeProjectValidationController.class).error(exception.getMessage(),
                    exception);
        }
    }
}
