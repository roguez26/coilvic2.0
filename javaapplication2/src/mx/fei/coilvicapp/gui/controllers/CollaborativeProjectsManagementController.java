package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import mx.fei.coilvicapp.logic.collaborativeproject.ICollaborativeProject;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;

public class CollaborativeProjectsManagementController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private Button seeDetailsButton;

    @FXML
    private MenuButton statusMenuButton;

    @FXML
    private TableView<CollaborativeProject> collaborativeProjecsTableView;

    @FXML
    private TableColumn courseOneTableColumn;

    @FXML
    private TableColumn courseTwoTableColumn;

    @FXML
    private TableColumn nameTableColumn;

    @FXML
    private TableColumn<CollaborativeProject, String> universityOneTableColumn;

    @FXML
    private TableColumn<CollaborativeProject, String> universityTwoTableColumn;

    @FXML
    private Button validateButton;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
        intializeCollaborativeProjectsTable();
    }

    private void intializeCollaborativeProjectsTable() {
        ICollaborativeProject collaborativeProjectDAO = new CollaborativeProjectDAO();
        ArrayList<CollaborativeProject> collaborativeProjectsList = new ArrayList<>();

        try {
            collaborativeProjectsList = collaborativeProjectDAO.getCollaborativeProjectsProposals();
        } catch (DAOException exception) {
            handleDAOException(exception);
        }

        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        courseOneTableColumn.setCellValueFactory(new PropertyValueFactory<>("requesterCourse"));
        courseTwoTableColumn.setCellValueFactory(new PropertyValueFactory<>("requestedCourse"));
        universityOneTableColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getRequesterCourse().getProfessor().getUniversity()
                        .getName()));

        universityTwoTableColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getRequestedCourse().getProfessor().getUniversity()
                        .getName()));
        if (!collaborativeProjectsList.isEmpty()) {
            collaborativeProjecsTableView.getItems().addAll(collaborativeProjectsList);
        }
    }

    @FXML
    void seeDetailsButton(ActionEvent event) throws IOException {
        CollaborativeProject selectedCollaborativeProject = collaborativeProjecsTableView
                .getSelectionModel().getSelectedItem();
        if (selectedCollaborativeProject != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/"
                    + "CollaborativeProjectDetailsProfessor.fxml"));
            MainApp.changeView(fxmlLoader);
            CollaborativeProjectDetailsProfessorController collaborativeProjectDetailsProfessorController
                    = fxmlLoader.getController();
            collaborativeProjectDetailsProfessorController.setCollaborativeProject(
                    selectedCollaborativeProject);
        } else {
            DialogController.getInformativeConfirmationDialog("Sin proyecto seleccionado", "Necesita "
                    + "seleccionar un proyecto para poder ver sus detalles");
        }
    }

    @FXML
    void pendingMenuButtonIsSelected(ActionEvent event) {
        ICollaborativeProject collaborativeProjectDAO = new CollaborativeProjectDAO();
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        try {
            setValidateMode(true);
            updateTableView(collaborativeProjectDAO.getCollaborativeProjectsProposals());
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    @FXML
    void acceptedMenuButtonIsSelected(ActionEvent event) {
        ICollaborativeProject collaborativeProjectDAO = new CollaborativeProjectDAO();
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        try {
            setValidateMode(false);
            updateTableView(collaborativeProjectDAO.getAllAcceptedCollaborativeProjects());
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    @FXML
    void rejectedMenuButtonIsSelected(ActionEvent event) {
        ICollaborativeProject collaborativeProjectDAO = new CollaborativeProjectDAO();
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        try {
            setValidateMode(false);
            updateTableView(collaborativeProjectDAO.getAllRejectedCollaborativeProjects());
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    @FXML
    void finishedMenuButtonIsSelected(ActionEvent event) {
        ICollaborativeProject collaborativeProjectDAO = new CollaborativeProjectDAO();
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        try {
            setValidateMode(false);
            updateTableView(collaborativeProjectDAO.getAllFinishedCollaborativeProjects());
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    private void setValidateMode(boolean isVisible) {
        validateButton.setVisible(isVisible);
        validateButton.setManaged(isVisible);
        seeDetailsButton.setVisible(!isVisible);
    }

    private void updateTableView(ArrayList<CollaborativeProject> collaborativeProjectsList) {
        collaborativeProjecsTableView.getItems().clear();
        collaborativeProjecsTableView.getItems().addAll(collaborativeProjectsList);
    }

    @FXML
    void validateButtonIsPressed(ActionEvent event) throws IOException {
        CollaborativeProject selectedCollaborativeProject = collaborativeProjecsTableView
                .getSelectionModel().getSelectedItem();
        if (selectedCollaborativeProject != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/"
                    + "CollaborativeProjectValidation.fxml"));
            MainApp.changeView(fxmlLoader);
            CollaborativeProjectValidationController collaborativeProjectDetailsStudentController
                    = fxmlLoader.getController();
            collaborativeProjectDetailsStudentController.setCollaborativeProject(
                    selectedCollaborativeProject);
        } else {
            DialogController.getInformativeConfirmationDialog("Sin proyecto seleccionado", "Necesita "
                    + "seleccionar un proyecto para poder inciar la validación");
        }
    }

    @FXML
    void backButtonIsPressed(ActionEvent event) {
        goBack();
    }

    private void goBack() {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/CoordinationMainMenu");
        } catch (IOException exception) {
            Log.getLogger(CollaborativeProjectsManagementController.class).error(exception.getMessage(),
                    exception);
        }
    }

    private void handleDAOException(DAOException exception) {
        collaborativeProjecsTableView.getItems().clear();
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
            Log.getLogger(CollaborativeProjectsManagementController.class).error(
                    ioException.getMessage(), ioException);
        }
    }

}
