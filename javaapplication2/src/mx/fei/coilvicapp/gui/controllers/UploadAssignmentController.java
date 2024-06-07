package mx.fei.coilvicapp.gui.controllers;

import java.io.File;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.assignment.Assignment;
import mx.fei.coilvicapp.logic.assignment.AssignmentDAO;
import mx.fei.coilvicapp.logic.assignment.IAssignment;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.FileManager;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;

/**
 *
 * @author ivanr
 */
public class UploadAssignmentController implements Initializable {

    @FXML
    private VBox backgroundVBox;

    @FXML
    private Button acceptButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label descriptionLabel;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField fileTextField;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button selectFileButton;

    @FXML
    private Label titleLabel;

    private File selectedFile;
    private CollaborativeProject collaborativeProject;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {   
    }

    @FXML
    void acceptButtonIsPressed(ActionEvent event) {
        try {
            invokeSaveAssignment();
            closeWindow();
        } catch (IllegalArgumentException exception) {
            handleValidationException(exception);
        } catch (DAOException exception) {
            handleDAOException(exception);
        } catch (IOException exception) {
            handleIOException(exception);
        }
    }

    private void invokeSaveAssignment() throws DAOException, IOException {
        FileManager fileManager = new FileManager();
        Assignment assignment = initializeAssignment();
        fileManager.setFile(selectedFile);
        fileManager.setDestinationDirectory(assignment.getIdColaborativeProject());
        fileManager.isValidFileForSave(selectedFile);
        if (confirmUpload()) {
            invokeRegisterAssignment(fileManager.saveAssignment(), assignment);
        }
    }

    private void invokeRegisterAssignment(String newPath, Assignment assignment) throws DAOException, IOException {
        IAssignment asigmentDAO = new AssignmentDAO();

        assignment.setPath(newPath);
        if (asigmentDAO.registerAssignment(assignment, collaborativeProject) > 0) {
            DialogController.getInformativeConfirmationDialog("Subida", "La actividad fue subida con éxito");
            cleanFields();
        }
    }

    private Assignment initializeAssignment() {
        Assignment assignment = new Assignment();
        assignment.setName(nameTextField.getText());
        assignment.setDescription(descriptionTextArea.getText());
        assignment.setIdColaborativeProject(collaborativeProject.getIdCollaborativeProject());
        return assignment;
    }

    @FXML
    void cancelButtonIsPressed(ActionEvent event) throws Throwable {
        closeWindow();
    }

    @FXML
    void selectFileButtonIsPressed(ActionEvent event) {
        FileManager fileManager = new FileManager();
        selectedFile = fileManager.selectPDF(backgroundVBox.getScene().getWindow());
        if (selectedFile != null) {
            fileTextField.setText(selectedFile.getName());
        }
    }

    private void handleDAOException(DAOException exception) {
        FileManager fileManager = new FileManager();
        fileManager.undoSaveAssignment(selectedFile);
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    closeWindow();
                case FATAL ->
                    MainApp.handleFatal();
            }
        } catch (IOException ioException) {
            Log.getLogger(UploadAssignmentController.class).error(ioException.getMessage(), ioException);
        }
    }

    private boolean confirmUpload() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar", "¿Deseas subir esta nueva actividad?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private void cleanFields() {
        nameTextField.setText("");
        descriptionTextArea.setText("");
        fileTextField.setText("");
        selectedFile = null;
    }

    private void handleIOException(IOException exception) {
        DialogController.getInformativeConfirmationDialog("Lo sentimos", "No fue posible subir la actividad");
    }

    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getInvalidDataDialog(exception.getMessage());
    }

    public void setCollaborativeProject(CollaborativeProject collaborativeProject) {
        this.collaborativeProject = collaborativeProject;
    }

    private void closeWindow() {
        Stage stage = (Stage) backgroundVBox.getScene().getWindow();
        stage.close();
    }

}
