package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.assignment.Assignment;
import mx.fei.coilvicapp.logic.assignment.AssignmentDAO;
import mx.fei.coilvicapp.logic.assignment.IAssignment;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.implementations.FileManager;

public class ActivitiesManagementController implements Initializable {

    @FXML
    private TableView<Assignment> activitiesTableView;

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private Button editButton;

    @FXML
    private TableColumn<Assignment, String> dateTableColumn;

    @FXML
    private TableColumn<Assignment, String> descriptionTableColumn;

    @FXML
    private TableColumn<Assignment, String> nameTableColumn;

    @FXML
    private Button seeActivityButton;

    private CollaborativeProject collaborativeProject;
    private Professor professorSession;
    private boolean justVisibleMode;

    @FXML
    void addButtonIsPressed(ActionEvent event) {
        uploadAssignment();
    }

    private void uploadAssignment() {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/UploadAssignment", controller -> {
                UploadAssignmentController uploadAssignmentController = (UploadAssignmentController) controller;
                uploadAssignmentController.setCollaborativeProject(collaborativeProject);
            });
        } catch (IOException exception) {
            Log.getLogger(ActivitiesManagementController.class).error(exception.getMessage(), exception);
        }
        fillActivitiesTable(collaborativeProject);
    }

    @FXML
    void backButtonIsPressed(ActionEvent event) {
        goBack();
    }

    private void goBack() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/mx/fei/coilvicapp/gui/views/CollaborativeProjectDetailsProfessor.fxml"));
            MainApp.changeView(fxmlLoader);
            CollaborativeProjectDetailsProfessorController collaborativeProjectDetailsProfessorController
                    = fxmlLoader.getController();
            collaborativeProjectDetailsProfessorController.setCollaborativeProject(collaborativeProject);
            if (!justVisibleMode) {
                collaborativeProjectDetailsProfessorController.setProfessor(professorSession);
            }

        } catch (IOException exception) {
            Log.getLogger(ActivitiesManagementController.class).error(exception.getMessage(), exception);
        }
    }

    @FXML
    void seeActivityButtonIsPressed(ActionEvent event) {
        Assignment selectedAssignment = activitiesTableView.getSelectionModel().getSelectedItem();

        if (selectedAssignment != null) {
            FileManager fileManager = new FileManager();
            try {
                fileManager.openFile(selectedAssignment.getPath());
            } catch (IllegalArgumentException exception) {
                DialogController.getInformativeConfirmationDialog("Lo sentimos", exception.getMessage());
            } catch (IOException exception) {
                DialogController.getInformativeConfirmationDialog("Algo salió mal", exception.getMessage());
            }
        } else {
            DialogController.getInformativeConfirmationDialog("Sin actividad", "Seleccione una actividad para "
                    + "poder ver sus detalles");
        }
    }

    @FXML
    void editButtonIsPressed(ActionEvent event) {
        Assignment selectedAssignment = activitiesTableView.getSelectionModel().getSelectedItem();
        if (selectedAssignment != null) {
            try {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/UploadAssignment", controller -> {
                    UploadAssignmentController uploadAssignmentController = (UploadAssignmentController) controller;
                    uploadAssignmentController.setCollaborativeProject(collaborativeProject);
                    uploadAssignmentController.setAssignment(selectedAssignment);
                });
            } catch (IOException exception) {
                Log.getLogger(ActivitiesManagementController.class).error(exception.getMessage(), exception);
            }
            fillActivitiesTable(collaborativeProject);
        } else {
            DialogController.getInformativeConfirmationDialog("Sin actividad", "Seleccione una actividad para"
                    + " poder ver sus detalles");
        }
    }

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

    }

    public void setCollaborativeProject(CollaborativeProject collaborativeProject) {
        this.collaborativeProject = collaborativeProject;
        if (!collaborativeProject.getStatus().equals("Aceptado")) {
            editButton.setVisible(false);
            addButton.setVisible(false);
        }
        fillActivitiesTable(collaborativeProject);
    }

    public void setProfessorSession(Professor professor) {
        this.professorSession = professor;
        editButton.setVisible(true);
        addButton.setVisible(true);
    }

    public void setJustVisibleMode(boolean isJustVisible) {
        this.justVisibleMode = isJustVisible;
    }

    private void fillActivitiesTable(CollaborativeProject collaborativeProject) {
        IAssignment assignmentDAO = new AssignmentDAO();
        ArrayList<Assignment> assignmentsList = new ArrayList<>();
        activitiesTableView.getItems().clear();
        try {
            assignmentsList = assignmentDAO.getAssignmentsByIdProjectColaborative(
                    collaborativeProject.getIdCollaborativeProject());
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
        activitiesTableView.getItems().addAll(assignmentsList);
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(),
                    exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    goBack();
                case FATAL ->
                    MainApp.handleFatal();
            }
        } catch (IOException ioException) {
            Log.getLogger(ActivitiesManagementController.class).error(ioException.getMessage(), ioException);
        }
    }
}
