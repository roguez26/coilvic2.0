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
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import mx.fei.coilvicapp.logic.collaborativeproject.ICollaborativeProject;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;
import mx.fei.coilvicapp.logic.professor.Professor;

/**
 *
 * @author ivanr
 */
public class CollaborativeProjectsProfessorController implements Initializable {

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
    private Button registerButton;

    @FXML
    private TableColumn<CollaborativeProject, String> universityTwoTableColumn;

    private final ICollaborativeProject COLLABORATIVE_PROJECT_DAO = new CollaborativeProjectDAO();
    private ArrayList<CollaborativeProject> collaborativeProjectsList = new ArrayList<>();

    private Professor professor;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {

    }
    
    @FXML
    void seeDetailsButton(ActionEvent event) {
        CollaborativeProject selectedCollaborativeProject = collaborativeProjecsTableView.getSelectionModel().getSelectedItem();
        if (selectedCollaborativeProject != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/CollaborativeProjectDetailsProfessor.fxml"));
                MainApp.changeView(fxmlLoader);
                CollaborativeProjectDetailsProfessorController collaborativeProjectDetailsProfessorController = fxmlLoader.getController();
                collaborativeProjectDetailsProfessorController.setCollaborativeProject(selectedCollaborativeProject);
                collaborativeProjectDetailsProfessorController.setProfessor(professor);
            } catch (IOException exception) {
                Log.getLogger(ProfessorMainMenuController.class).error(exception.getMessage(), exception);
            }
        } else {
            DialogController.getInformativeConfirmationDialog("Sin proyecto seleccionado", "Necesita seleccionar un proyecto para poder ver sus detalles");
        }
    }
    
    @FXML
    void registerButtonIsPressed(ActionEvent event) {
        if (event.getSource() == registerButton) {
            try {
                if (professor != null) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/RegisterCollaborativeProject.fxml"));
                    MainApp.changeView(fxmlLoader);
                    RegisterCollaborativeProjectController registerCollaborativeProjectController = fxmlLoader.getController();
                    registerCollaborativeProjectController.setProfessor(professor);
                }
            } catch (IOException exception) {
                Log.getLogger(ProfessorMainMenuController.class).error(exception.getMessage(), exception);
            }
        } 
    }
    
    @FXML
    void pendingMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        try {
            collaborativeProjectsList = COLLABORATIVE_PROJECT_DAO.getPendingCollaborativeProjectsByProfessor(professor.getIdProfessor());
            updateTableView();
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    @FXML
    void acceptedMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        try {
            collaborativeProjectsList = COLLABORATIVE_PROJECT_DAO.getAcceptedCollaborativeProjectsByProfessor(professor.getIdProfessor());

            updateTableView();
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    @FXML
    void rejectedMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        try {
            collaborativeProjectsList = COLLABORATIVE_PROJECT_DAO.getRejectedCollaborativeProjectsByProfessor(professor.getIdProfessor());

            updateTableView();
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    @FXML
    void finishedMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        try {
            collaborativeProjectsList = COLLABORATIVE_PROJECT_DAO.getFinishedCollaborativeProjectsByProfessor(professor.getIdProfessor());
            updateTableView();
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    private void updateTableView() {
        collaborativeProjecsTableView.getItems().clear();
        collaborativeProjecsTableView.getItems().addAll(collaborativeProjectsList);
    }

    @FXML
    void backButtonIsPressed(ActionEvent event) {
        goBack();
    }

    @FXML
    void searchButtonIsPressed(ActionEvent event) {

    }

    private void goBack() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/ProfessorMainMenu.fxml"));
        try {
            MainApp.changeView(fxmlLoader);
            ProfessorMainMenuController professorMainMenuController = fxmlLoader.getController();
            professorMainMenuController.setProfessor(professor);
        } catch (IOException exception) {
            Log.getLogger(CollaborativeProjectsProfessorController.class).error(exception.getMessage(), exception);
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
                    MainApp.changeView("/main/MainApp");
            }
        } catch (IOException ioException) {
            Log.getLogger(CollaborativeProjectsProfessorController.class).error(exception.getMessage(), exception);
        }
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
        initializeTableView(professor);
    }

    private void initializeTableView(Professor professor) {
        try {
            collaborativeProjectsList = COLLABORATIVE_PROJECT_DAO.getPendingCollaborativeProjectsByProfessor(professor.getIdProfessor());
        } catch (DAOException exception) {
            handleDAOException(exception);
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
}