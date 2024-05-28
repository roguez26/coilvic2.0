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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.collaborativeproject_.CollaborativeProject;
import mx.fei.coilvicapp.logic.collaborativeproject_.CollaborativeProjectDAO;
import mx.fei.coilvicapp.logic.collaborativeproject_.ICollaborativeProject;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;

/**
 *
 * @author ivanr
 */
public class CollaborativeProjectsManagementController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

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

    private final ICollaborativeProject COLLABORATIVE_PROJECT_DAO = new CollaborativeProjectDAO();
    private ArrayList<CollaborativeProject> collaborativeProjectsList = new ArrayList<>();
    @FXML
    private Button validateButton;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {

        try {
            collaborativeProjectsList = COLLABORATIVE_PROJECT_DAO.getCollaborativeProjectsProposals();
        } catch (DAOException exception) {

        }
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        courseOneTableColumn.setCellValueFactory(new PropertyValueFactory<>("requesterCourse"));
        courseTwoTableColumn.setCellValueFactory(new PropertyValueFactory<>("requestedCourse"));
        universityOneTableColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getRequesterCourse().getProfessor().getUniversity().getName()));

        universityTwoTableColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getRequestedCourse().getProfessor().getUniversity().getName()));
        if (!collaborativeProjectsList.isEmpty()) {
            collaborativeProjecsTableView.getItems().addAll(collaborativeProjectsList);
        }
    }

    @FXML
    void seeDetailsButton(ActionEvent event) {
        if (collaborativeProjecsTableView.getSelectionModel().getSelectedItem() != null) {
            //pendiente
        } else {
            DialogController.getInformativeConfirmationDialog("Sin proyecto seleccionado", "Necesita seleccionar un proyecto para poder ver sus detalles");
        }
    }

    @FXML
    void pendingMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        try {
            collaborativeProjectsList = COLLABORATIVE_PROJECT_DAO.getCollaborativeProjectsProposals();
            setValidateMode(true);
            updateTableView();
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    @FXML
    void acceptedMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        try {
            collaborativeProjectsList = COLLABORATIVE_PROJECT_DAO.getAllAcceptedCollaborativeProjects();
            setValidateMode(false);
            updateTableView();
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    @FXML
    void rejectedMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        try {
            collaborativeProjectsList = COLLABORATIVE_PROJECT_DAO.getAllRejectedCollaborativeProjects();
            setValidateMode(false);
            updateTableView();
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    @FXML
    void finishedMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        try {
            collaborativeProjectsList = COLLABORATIVE_PROJECT_DAO.getAllFinishedCollaborativeProjects();
            setValidateMode(false);
            updateTableView();
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    private void setValidateMode(boolean isVisible) {
        validateButton.setVisible(isVisible);
        validateButton.setManaged(isVisible);
        seeDetailsButton.setVisible(!isVisible);
    }

    private void updateTableView() {
        collaborativeProjecsTableView.getItems().clear();
        collaborativeProjecsTableView.getItems().addAll(collaborativeProjectsList);
    }

    @FXML
    void validateButtonIsPressed(ActionEvent event) throws IOException {
        CollaborativeProject selectedCollaborativeProject = collaborativeProjecsTableView.getSelectionModel().getSelectedItem();
        if (selectedCollaborativeProject != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/ValidateCollaborativeProject.fxml"));
            MainApp.changeView(fxmlLoader);
            ValidateCollaborativeProjectController collaborativeProjectDetailsStudentController = fxmlLoader.getController();
            collaborativeProjectDetailsStudentController.setCollaborativeProject(selectedCollaborativeProject);
        } else {
            DialogController.getInformativeConfirmationDialog("Sin proyecto seleccionado", "Necesita seleccionar un proyecto para poder inciar la validacion");
        }
    }

    @FXML
    void backButtonIsPressed(ActionEvent event) {

    }

    @FXML
    void searchButtonIsPressed(ActionEvent event) {

    }

    private void handleDAOException(DAOException exception) {
        collaborativeProjecsTableView.getItems().clear();
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/UniversityManager");
                case FATAL ->
                    MainApp.changeView("/main/MainApp");
            }
        } catch (IOException ioException) {
            Log.getLogger(NotifyProfessorController.class).error(exception.getMessage(), exception);
        }
    }

}
