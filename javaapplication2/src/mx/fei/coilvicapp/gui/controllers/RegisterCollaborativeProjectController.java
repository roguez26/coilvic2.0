package mx.fei.coilvicapp.gui.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author d0ubl3_d
 */
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.assignment.AssignmentDAO;
import mx.fei.coilvicapp.logic.assignment.IAssignment;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import mx.fei.coilvicapp.logic.collaborativeproject.ICollaborativeProject;
import mx.fei.coilvicapp.logic.collaborativeprojectrequest.CollaborativeProjectRequest;
import mx.fei.coilvicapp.logic.collaborativeprojectrequest.CollaborativeProjectRequestDAO;
import mx.fei.coilvicapp.logic.collaborativeprojectrequest.ICollaborativeProjectRequest;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.FileManager;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;
import mx.fei.coilvicapp.logic.modality.IModality;
import mx.fei.coilvicapp.logic.modality.Modality;
import mx.fei.coilvicapp.logic.modality.ModalityDAO;
import mx.fei.coilvicapp.logic.professor.Professor;

public class RegisterCollaborativeProjectController implements Initializable {

    @FXML
    private ComboBox<Modality> modalitiesComboBox;

    @FXML
    private VBox backgroundVBox;

    @FXML
    private Button backButton;

    @FXML
    private Button cancelButton;

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
    private TextField nameTextField;

    @FXML
    private TextField professorOneTextField;

    @FXML
    private TextField professorTwoTextField;

    @FXML
    private TextField syllabusPathTextField;

    @FXML
    private Button saveButton;

    @FXML
    private ComboBox<CollaborativeProjectRequest> acceptedRequestesComboBox;

    @FXML
    private Button uploadSyllabusButton;
    private Professor professor;
    private final ICollaborativeProjectRequest COLLABORATIVE_PROJECT_REQUEST_DAO = new CollaborativeProjectRequestDAO();
    private CollaborativeProjectRequest selectedRequest = new CollaborativeProjectRequest();
    private File selectedFile;
    private final FileManager FILE_MANAGER = new FileManager();
    private final CollaborativeProject COLLABORATIVE_PROJECT = new CollaborativeProject();

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
        initializeModalities();
    }

    private void initializeModalities() {
        IModality modalityDAO = new ModalityDAO();
        ArrayList<Modality> modalities;
        try {
            modalities = modalityDAO.getModalities();
            ObservableList<Modality> observableModalities = FXCollections.observableArrayList(modalities);
            modalitiesComboBox.setItems(observableModalities);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    @FXML
    void backButtonIsPressed(ActionEvent event) {
        goBack();
    }

    @FXML
    void cancelButtonIsPressed(ActionEvent event) {

    }

    private void initializeCollaborativeProject() {

        COLLABORATIVE_PROJECT.setRequestedCourse(selectedRequest.getRequestedCourse());
        COLLABORATIVE_PROJECT.setRequesterCourse(selectedRequest.getRequesterCourse());
        COLLABORATIVE_PROJECT.setName(nameTextField.getText());
        COLLABORATIVE_PROJECT.setDescription(descriptionTextArea.getText());
        COLLABORATIVE_PROJECT.setGeneralObjective(generalObjetiveTextArea.getText());
        COLLABORATIVE_PROJECT.setCode(codeTextField.getText());

    }

    @FXML
    void saveButtonIsPressed(ActionEvent event) {
        try {
            initializeCollaborativeProject();
            initializeForSave();
            invokeRegisterCollaborativeProject();
        } catch (IllegalArgumentException exception) {
            handleValidationException(exception);
        } catch (IOException exception) {
            handleIOException(exception);
        } catch (DAOException exception) {
            FILE_MANAGER.undoSaveAssignment();
            handleDAOException(exception);

        }
    }

    private void invokeRegisterCollaborativeProject() throws IOException, DAOException {
        ICollaborativeProject collaborativeProjectDAO = new CollaborativeProjectDAO();
        if (confirmSave()) {
            COLLABORATIVE_PROJECT.setSyllabusPath(FILE_MANAGER.saveAssignment());

            if (collaborativeProjectDAO.registerCollaborativeProject(COLLABORATIVE_PROJECT, selectedRequest) > 0)  {
                DialogController.getInformativeConfirmationDialog("Registrado", "El proyecto colaborativo fue registrado");
                goBack();
            }
        }
    }

    private void initializeForSave() {
        FILE_MANAGER.setFile(selectedFile);
        FILE_MANAGER.setSyllabusDestination();
        FILE_MANAGER.setDestinationDirectory(selectedRequest.getIdCollaboratibeProjectRequest());
        FILE_MANAGER.isValidFileForSave();
    }

    @FXML
    void uploadSyllabusButtonIsPressed(ActionEvent event) {
        selectedFile = FILE_MANAGER.selectPDF(backgroundVBox.getScene().getWindow());
        if (selectedFile != null) {
            syllabusPathTextField.setText(selectedFile.getName());
        } else {
            syllabusPathTextField.clear();
        }
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
        initalizaAcceptedRequestsCombobox(professor);

    }

    private void initalizaAcceptedRequestsCombobox(Professor professor) {
        ArrayList<CollaborativeProjectRequest> acceptedRequestes = new ArrayList<>();
        try {
            ArrayList<CollaborativeProjectRequest> acceptedRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getAcceptedCollaborativeProjectRequests(professor.getIdProfessor());
            ObservableList<CollaborativeProjectRequest> observableAcceptedRequests = FXCollections.observableArrayList(acceptedRequests);
            acceptedRequestesComboBox.setItems(observableAcceptedRequests);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    @FXML
    void requestIsSelected(ActionEvent event) {
        selectedRequest = acceptedRequestesComboBox.getValue();
        System.out.println(selectedRequest.getIdCollaboratibeProjectRequest());
        courseOneTextField.setText(selectedRequest.getRequestedCourse().toString());
        courseTwoTextField.setText(selectedRequest.getRequesterCourse().toString());
        professorOneTextField.setText(selectedRequest.getRequestedCourse().getProfessor().toString());
        professorTwoTextField.setText(selectedRequest.getRequesterCourse().getProfessor().toString());
    }

    @FXML
    void modalityIsSelected(ActionEvent event) {
        COLLABORATIVE_PROJECT.setModality(modalitiesComboBox.getValue());
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    goBack();
                case FATAL ->
                    MainApp.changeView("/main/MainApp");
            }
        } catch (IOException ioException) {
            Log.getLogger(RegisterCollaborativeProjectController.class).error(exception.getMessage(), exception);
        }
    }

    private void handleIOException(IOException exception) {
        DialogController.getInformativeConfirmationDialog("Lo sentimos", exception.getMessage());
    }

    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getInvalidDataDialog(exception.getMessage());
    }

    private void goBack() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/CollaborativeProjectsProfessor.fxml"));
        try {
            MainApp.changeView(fxmlLoader);
            CollaborativeProjectsProfessorController collaborativeProjectsProfessorController = fxmlLoader.getController();
            collaborativeProjectsProfessorController.setProfessor(professor);
        } catch (IOException exception) {
            Log.getLogger(CollaborativeProjectsProfessorController.class).error(exception.getMessage(), exception);
        }
    }

    private boolean confirmSave() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar", "¿Deseas registrar este nuevo proyecto colaborativo?");
        return (response.get() == DialogController.BUTTON_YES);
    }
}
