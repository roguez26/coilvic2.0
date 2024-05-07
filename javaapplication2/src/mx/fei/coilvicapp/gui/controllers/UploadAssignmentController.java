package mx.fei.coilvicapp.gui.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import main.MainApp;
import mx.fei.coilvicapp.logic.assignment.Assignment;
import mx.fei.coilvicapp.logic.assignment.AssignmentDAO;
import mx.fei.coilvicapp.logic.assignment.IAssignment;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.FileManager;
import mx.fei.coilvicapp.logic.implementations.Status;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;

/**
 * FXML Controller class
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
    private Label titleLabel; //Corregir está mal escrito

    private File selectedFile;
    private final Assignment assignment = new Assignment();
    private final IAssignment asigmentDAO = new AssignmentDAO();
    private final FileManager fileManager = new FileManager();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    void acceptButtonIsPressed(ActionEvent event) {
        try {
            invokeSaveAssignment();
        } catch (IllegalArgumentException iaException) {
            handleValidationException(iaException);
        } catch (DAOException daoException) {
            handleDAOException(daoException);
            fileManager.undoSaveAssignment();
        } catch (IOException ioException) {
            handleIOException(ioException);
            fileManager.undoSaveAssignment();
        }
    }

    private void invokeSaveAssignment() throws DAOException, IOException {
        initializeAssignment();
        fileManager.setFile(selectedFile);
        fileManager.setDestinationDirectory(assignment.getIdColaborativeProject());
        fileManager.isValidFileForSave();
        if (confirmUpload()) {
            invokeRegisterAssignment(fileManager.saveAssignment());
        }
    }

    private void invokeRegisterAssignment(String newPath) throws DAOException, IOException {
        assignment.setPath(newPath);
        if (asigmentDAO.insertAssignment(assignment) > 0) {
            wasUploadedConfirmation();
            cleanFields();
        }
    }

    private void initializeAssignment() {
        assignment.setName(nameTextField.getText());
        assignment.setDescription(descriptionTextArea.getText());
        assignment.setIdColaborativeProject(1);
    }

    @FXML
    void cancelButtonIsPressed(ActionEvent event) {

    }

    @FXML
    void selectFileButtonIsPressed(ActionEvent event) {
        selectedFile = fileManager.selectPDF(backgroundVBox.getScene().getWindow());
        if (selectedFile != null) {
            fileTextField.setText(selectedFile.getName());
        }
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

        }
    }

    private boolean confirmUpload() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("", "¿Deseas subir esta nueva actividad?");
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

    private boolean wasUploadedConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog("Subida", "La actividad fue subida con éxito");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }

}
