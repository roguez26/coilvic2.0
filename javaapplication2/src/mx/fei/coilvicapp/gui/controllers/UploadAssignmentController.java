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
    private Button deleteButton;

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
    private Assignment assignment;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
    }

    @FXML
    void acceptButtonIsPressed(ActionEvent event) {
        try {
            if (assignment == null) {
                invokeSaveAssignment();
            } else {
                invokeUpdateAssignment();
            }
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
        Assignment newAssignment = initializeAssignment();

        if (confirmUpload()) {
            invokeRegisterAssignment(fileManager.saveAssignment(selectedFile, collaborativeProject.getIdCollaborativeProject()), newAssignment);
        }
    }

    private void invokeUpdateAssignment() throws DAOException, IOException {
        Assignment newAssignment = initializeAssignment();
        File oldFile = new File(assignment.getPath());
        newAssignment.setPath(assignment.getPath());
        newAssignment.setIdAssignment(assignment.getIdAssignment());

        if (!newAssignment.equals(assignment) || !oldFile.equals(selectedFile)) {
            FileManager fileManager = new FileManager();
            if (!oldFile.equals(selectedFile)) {
                fileManager.isValidFileForSave(selectedFile);
            }

            if (confirmUpdate()) {
                IAssignment asigmentDAO = new AssignmentDAO();
                if (!oldFile.equals(selectedFile)) {
                    String newPath = fileManager.saveAssignment(selectedFile, collaborativeProject.getIdCollaborativeProject());
                    newAssignment.setPath(newPath);
                    fileManager.deleteFile(oldFile);
                }
                if (asigmentDAO.updateAssignment(newAssignment, collaborativeProject) > 0) {
                    DialogController.getInformativeConfirmationDialog("Actualizada", "La actividad fue actualizada"
                            + " con éxito");
                    closeWindow();
                }
            }
        } else {
            closeWindow();
        }
    }

    private void invokeRegisterAssignment(String newPath, Assignment assignment) throws DAOException, IOException {
        IAssignment asigmentDAO = new AssignmentDAO();

        assignment.setPath(newPath);
        if (asigmentDAO.registerAssignment(assignment, collaborativeProject) > 0) {
            DialogController.getInformativeConfirmationDialog("Subida", "La actividad fue subida con éxito");
            closeWindow();
        }
    }

    private Assignment initializeAssignment() {
        Assignment newAssignment = new Assignment();
        newAssignment.setName(nameTextField.getText());
        newAssignment.setDescription(descriptionTextArea.getText());
        newAssignment.setIdColaborativeProject(collaborativeProject.getIdCollaborativeProject());
        return newAssignment;
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
        } else {
            fileTextField.setText("");
        }
    }

    @FXML
    void deleteButtonIsPressed(ActionEvent event) {
        if (confirmDelete()) {
            FileManager fileManager = new FileManager();
            IAssignment assignmentDAO = new AssignmentDAO();
            File auxFile = selectedFile;

            try {
                fileManager.deleteFile(selectedFile);
                if (assignmentDAO.deleteAssignment(assignment.getIdAssignment(), collaborativeProject) > 0) {
                    DialogController.getInformativeConfirmationDialog("Eliminada", "La actividad fue eliminada");
                    closeWindow();
                }
            } catch (IOException exception) {
                handleIOException(exception);
            } catch (DAOException exception) {
                undoDeleteAssignment(auxFile);
                handleDAOException(exception);
            }
        }
    }

    private void undoDeleteAssignment(File file) {
        FileManager fileManager = new FileManager();
        try {
            fileManager.saveAssignment(file, collaborativeProject.getIdCollaborativeProject());
        } catch (IOException exception) {
            handleIOException(exception);
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
            Log.getLogger(UploadAssignmentController.class).error(ioException.getMessage(), ioException);
        }
    }

    private boolean confirmUpload() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar", "¿Deseas "
                + "subir esta nueva actividad?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean confirmUpdate() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar", "¿Deseas"
                + " actualizar esta actividad?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean confirmDelete() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar", "¿Deseas"
                + " eliminar esta actividad?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private void handleIOException(IOException exception) {
        DialogController.getInformativeConfirmationDialog("Lo sentimos", exception.getMessage());
    }

    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getInvalidDataDialog(exception.getMessage());
    }

    public void setCollaborativeProject(CollaborativeProject collaborativeProject) {
        this.collaborativeProject = collaborativeProject;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
        fillFields(assignment);
        deleteButton.setVisible(true);
        nameTextField.setEditable(true);
        descriptionTextArea.setEditable(true);

    }

    private void fillFields(Assignment assignment) {
        nameTextField.setText(assignment.getName());
        descriptionTextArea.setText(assignment.getDescription());
        fileTextField.setText(assignment.getPath());
        selectedFile = new File(assignment.getPath());
    }

    private void closeWindow() {
        Stage stage = (Stage) backgroundVBox.getScene().getWindow();
        stage.close();
    }

}
