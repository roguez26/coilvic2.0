package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.FileManager;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;

/**
 *
 * @author ivanr
 */
public class CollaborativeProjectDetailsController implements Initializable {

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

    private CollaborativeProject collaborativeProject;

    @FXML
    private Button seeSyllabusButton;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
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

    public void setCollaborativeProject(CollaborativeProject collaborativeProject) {
        this.collaborativeProject = collaborativeProject;
        initializeFields(collaborativeProject);
    }

    public void initializeFields(CollaborativeProject collaborativeProject) {
        if (collaborativeProject != null) {
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

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    closeWindow();
                case FATAL ->
                    MainApp.handleFatal();
            }
        } catch (IOException ioException) {
            Log.getLogger(CollaborativeProjectDetailsProfessorController.class).error(ioException.getMessage(), ioException);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) seeSyllabusButton.getScene().getWindow();
        stage.close();
    }
}
