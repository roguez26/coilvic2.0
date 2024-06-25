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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import log.Log;
import main.MainApp;
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

public class CollaborativeProjectRegistrationController implements Initializable {

    @FXML
    private ComboBox<Modality> modalitiesComboBox;

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
    private CollaborativeProjectRequest selectedRequest;
    private File selectedFile;
    private CollaborativeProject collaborativeProjectForUpdate = null;

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
        if (confirmCancel()) {
            goBack();
        }
    }

    private CollaborativeProject initializeNewCollaborativeProject() {
        CollaborativeProject collaborativeProject = new CollaborativeProject();

        collaborativeProject.setRequestedCourse(selectedRequest.getRequestedCourse());
        collaborativeProject.setRequesterCourse(selectedRequest.getRequesterCourse());
        collaborativeProject.setName(nameTextField.getText());
        collaborativeProject.setDescription(descriptionTextArea.getText());
        collaborativeProject.setGeneralObjective(generalObjetiveTextArea.getText());
        collaborativeProject.setCode(codeTextField.getText());
        collaborativeProject.setModality(modalitiesComboBox.getValue());
        return collaborativeProject;
    }

    private CollaborativeProject initializeCollaborativeProjectForUpdate() {
        CollaborativeProject collaborativeProject = new CollaborativeProject();
        collaborativeProject.setIdCollaborativeProject(collaborativeProjectForUpdate.getIdCollaborativeProject());
        collaborativeProject.setName(nameTextField.getText());
        collaborativeProject.setDescription(descriptionTextArea.getText());
        collaborativeProject.setGeneralObjective(generalObjetiveTextArea.getText());
        collaborativeProject.setCode(codeTextField.getText());
        collaborativeProject.setModality(modalitiesComboBox.getValue());
        collaborativeProject.setSyllabusPath(syllabusPathTextField.getText());
        collaborativeProject.setRequestedCourse(collaborativeProjectForUpdate.getRequestedCourse());
        collaborativeProject.setRequesterCourse(collaborativeProjectForUpdate.getRequesterCourse());
        collaborativeProject.setStatus(collaborativeProjectForUpdate.getStatus());
        return collaborativeProject;
    }

    @FXML
    void saveButtonIsPressed(ActionEvent event) {
        try {
            if (collaborativeProjectForUpdate == null) {
                invokeRegisterCollaborativeProject();
            } else {
                invokeUpdateCollaborativeProject();
            }
        } catch (IllegalArgumentException exception) {
            handleValidationException(exception);
        } catch (IOException exception) {
            handleIOException(exception);
        }
    }

    private void invokeRegisterCollaborativeProject() throws IOException {
        ICollaborativeProject collaborativeProjectDAO = new CollaborativeProjectDAO();
        CollaborativeProject collaborativeProject = initializeNewCollaborativeProject();
        if (confirmSave()) {
            FileManager fileManager = new FileManager();
            String syllabusPath = fileManager.saveSyllabus(selectedFile, selectedRequest.getIdCollaborativeProjectRequest());
            collaborativeProject.setSyllabusPath(syllabusPath);
            try {
                if (collaborativeProjectDAO.registerCollaborativeProject(collaborativeProject, selectedRequest) > 0) {
                    DialogController.getInformativeConfirmationDialog("Registrado", "El proyecto colaborativo fue registrado");
                    goBack();
                }
            } catch (DAOException exception) {
                fileManager.deleteFile(new File(syllabusPath));
                handleDAOException(exception);
            }
        }
    }

    private void invokeUpdateCollaborativeProject() throws IOException {
        CollaborativeProject newCollaborativeProject = initializeCollaborativeProjectForUpdate();

        if (!newCollaborativeProject.equals(collaborativeProjectForUpdate)) {
            FileManager fileManager = new FileManager();
            if (fileManager.isValidFileForSave(selectedFile)) {
                if (confirmUpdate()) {
                    updateCollaborativeProject(newCollaborativeProject);
                }
            }
        } else {
            DialogController.getInformativeConfirmationDialog("Sin ediciones", "El proyecto colaborativo no ha sido modificado");
        }
    }

    private void updateCollaborativeProject(CollaborativeProject collaborativeProject) throws IOException {
        ICollaborativeProject collaborativeProjectDAO = new CollaborativeProjectDAO();
        FileManager fileManager = new FileManager();
        String newSyllabusPath = "";
        try {
            newSyllabusPath = fileManager.saveSyllabus(selectedFile, getIdCollaborativeProjectRequest());
            collaborativeProject.setSyllabusPath(newSyllabusPath);
            if (collaborativeProjectDAO.updateCollaborativeProject(collaborativeProject) > 0) {
                fileManager.deleteFile(new File(collaborativeProjectForUpdate.getSyllabusPath()));
                DialogController.getInformativeConfirmationDialog("Registrado", "El proyecto colaborativo fue"
                        + " actualizado y enviado de nuevo para ser validado");
                goBack();
            }
        } catch (DAOException exception) {
            if (!"".equals(newSyllabusPath)) {
                fileManager.deleteFile(new File(newSyllabusPath));
            }
            handleDAOException(exception);
        }
    }

    private int getIdCollaborativeProjectRequest() throws DAOException {
        ICollaborativeProjectRequest collaborativeProjectRequestDAO = new CollaborativeProjectRequestDAO();
        CollaborativeProjectRequest collaborativeProjectRequest = new CollaborativeProjectRequest();
        collaborativeProjectRequest = collaborativeProjectRequestDAO.getCollaborativeProjectByCoursesId(
                collaborativeProjectForUpdate.getRequestedCourse().getIdCourse(), collaborativeProjectForUpdate
                .getRequesterCourse().getIdCourse());
        return collaborativeProjectRequest.getIdCollaborativeProjectRequest();
    }

    @FXML
    void uploadSyllabusButtonIsPressed(ActionEvent event) {
        FileManager fileManager = new FileManager();
        selectedFile = fileManager.selectPDF(backButton.getScene().getWindow());
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
        ICollaborativeProjectRequest collaborativeProjectRequestDAO = new CollaborativeProjectRequestDAO();
        ArrayList<CollaborativeProjectRequest> acceptedRequestes = new ArrayList<>();
        try {
            ArrayList<CollaborativeProjectRequest> acceptedRequests = collaborativeProjectRequestDAO.getAcceptedCollaborativeProjectRequests(professor.getIdProfessor());
            ObservableList<CollaborativeProjectRequest> observableAcceptedRequests = FXCollections.observableArrayList(acceptedRequests);
            acceptedRequestesComboBox.setItems(observableAcceptedRequests);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    @FXML
    void requestIsSelected(ActionEvent event) {
        selectedRequest = acceptedRequestesComboBox.getValue();
        courseOneTextField.setText(selectedRequest.getRequestedCourse().toString());
        courseTwoTextField.setText(selectedRequest.getRequesterCourse().toString());
        professorOneTextField.setText(selectedRequest.getRequestedCourse().getProfessor().toString());
        professorTwoTextField.setText(selectedRequest.getRequesterCourse().getProfessor().toString());
    }

    private void handleDAOException(DAOException exception) {
        selectedFile = null;
        syllabusPathTextField.clear();
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
            Log.getLogger(CollaborativeProjectRegistrationController.class).error(ioException.getMessage(), ioException);
        }
    }

    private void handleIOException(IOException exception) {
        selectedFile = null;
        syllabusPathTextField.clear();
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
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar", "¿Deseas "
                + "registrar este nuevo proyecto colaborativo?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean confirmUpdate() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar", "¿Deseas "
                + "actualizar este proyecto colaborativo? Se enviará para ser validado de nuevo");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean confirmCancel() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar", "¿Deseas "
                + "cancelar el registro este nuevo proyecto colaborativo?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    public void setCollaborativeProject(CollaborativeProject collaborativeProject) {
        this.collaborativeProjectForUpdate = collaborativeProject;
        fillFields(collaborativeProjectForUpdate);
    }

    private void fillFields(CollaborativeProject collaborativeProject) {
        acceptedRequestesComboBox.setDisable(true);
        courseOneTextField.setText(collaborativeProject.getRequestedCourse().toString());
        courseTwoTextField.setText(collaborativeProject.getRequesterCourse().toString());
        professorOneTextField.setText(collaborativeProject.getRequestedCourse().getProfessor().toString());
        professorTwoTextField.setText(collaborativeProject.getRequesterCourse().getProfessor().toString());
        courseOneTextField.setDisable(true);
        courseTwoTextField.setDisable(true);
        professorOneTextField.setDisable(true);
        professorTwoTextField.setDisable(true);
        nameTextField.setText(collaborativeProject.getName());
        generalObjetiveTextArea.setText(collaborativeProject.getGeneralObjective());
        descriptionTextArea.setText(collaborativeProject.getDescription());
        codeTextField.setText(collaborativeProject.getCode());
        modalitiesComboBox.setValue(collaborativeProject.getModality());

        File syllabusFile = new File(collaborativeProject.getSyllabusPath());
        if (syllabusFile.exists()) {
            syllabusPathTextField.setText(collaborativeProject.getSyllabusPath());
            selectedFile = new File(collaborativeProject.getSyllabusPath());
        } else {
            syllabusPathTextField.setPromptText("Archivo no encontrado");
        }
    }
}
