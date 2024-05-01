package mx.fei.coilvicapp.gui.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    private Label titileLabel;

    private Desktop desktop;
    private File selectedFile;
    private final Assignment assignment = new Assignment();
    private final IAssignment asigmentDAO = new AssignmentDAO();
    private static final long MAX_SIZE_BYTES = 10 * 1024 * 1024;
    Path destination = Paths.get("C:\\Users\\ivanr\\OneDrive\\Escritorio\\actividadesCoilvic");
    Path fileDestination;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    void acceptButtonIsPressed(ActionEvent event) {
        if (selectedFile != null && selectedFile.length() <= MAX_SIZE_BYTES) {
            saveFileInDirectory();
            initializeAssignment(fileDestination.toString());
            try {
                invokeRegisterAssignment();
            } catch (DAOException exception) {
                handleDAOException(exception);
            }
        }
    }

    private void saveFileInDirectory() {
        fileDestination = destination.resolve(selectedFile.getName());
        try {
            Files.copy(selectedFile.toPath(), fileDestination);
        } catch (IOException exception) {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), Status.WARNING));
        }
    }

    private void invokeRegisterAssignment() throws DAOException {
        asigmentDAO.insertAssignment(assignment);
    }

    private void initializeAssignment(String path) {
        assignment.setName(nameTextField.getText());
        assignment.setDescription(descriptionTextArea.getText());
        assignment.setPath(path);
        assignment.setIdColaborativeProject(1);
    }

    @FXML
    void cancelButtonIsPressed(ActionEvent event) {

    }

    @FXML
    void selectFileButtonIsPressed(ActionEvent event) {
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            FileChooser fileChooser = new FileChooser();
            ExtensionFilter pdfFilter = new ExtensionFilter("Archivos PDF (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(pdfFilter);
            selectedFile = fileChooser.showOpenDialog(backgroundVBox.getScene().getWindow());
            if (selectedFile != null) {
                fileTextField.setText(selectedFile.getName());
            }
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

}
