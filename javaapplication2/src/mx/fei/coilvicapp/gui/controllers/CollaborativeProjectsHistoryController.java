package mx.fei.coilvicapp.gui.controllers;

import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.collaborativeproject.ICollaborativeProject;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.professor.Professor;

/**
 * FXML Controller class for CollaborativeProjectsHistory view.
 *
 * author ivanr
 */
public class CollaborativeProjectsHistoryController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TableView<CollaborativeProject> collaborativeProjectsTableView;

    @FXML
    private TableColumn<CollaborativeProject, String> nameTableColumn;

    @FXML
    private Button seeDetailsButton;

    @FXML
    private TableColumn<CollaborativeProject, String> termTableColumn;

    private Professor professor;
    private Professor professorSession;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
    }

    @FXML
    void backButtonIsPressed(ActionEvent event) {

        changeToProfessorDetails();

    }

    private void changeToProfessorDetails() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/"
                + "ProfessorDetails.fxml"));
        try {
            MainApp.changeView(fxmlLoader);
            ProfessorDetailsController professorDetailsController = fxmlLoader.getController();
            professorDetailsController.setProfessor(professor);
            if(professorSession != null) {
                professorDetailsController.setProfessorSession(professorSession);
            }
        } catch (IOException exception) {
            Log.getLogger(CollaborativeProjectsHistoryController.class).error(exception.getMessage(), 
                    exception);
        }
    }

    @FXML
    void seeDetailsButtonIsPressed(ActionEvent event) {
        CollaborativeProject selectedCollaborativeProject = collaborativeProjectsTableView
                .getSelectionModel().getSelectedItem();
        if (selectedCollaborativeProject != null) {
            try {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/CollaborativeProjectDetails", controller -> {
                    CollaborativeProjectDetailsController collaborativeProjectDetailsController = 
                            (CollaborativeProjectDetailsController) controller;
                    collaborativeProjectDetailsController.setCollaborativeProject(selectedCollaborativeProject);
                });
            } catch (IOException exception) {
                Log.getLogger(CollaborativeProjectsHistoryController.class).error(exception.getMessage(), 
                        exception);
            }
        } else {
            DialogController.getInformativeConfirmationDialog("Sin selección", "Seleccione un proyecto "
                    + "colaborativo para ver sus detalles");
        }

    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
        fillCollaborativeProjectsTableView(professor);
    }

    private void fillCollaborativeProjectsTableView(Professor professor) {
        ArrayList<CollaborativeProject> collaborativeProjectList = new ArrayList<>();
        ICollaborativeProject collaborativeProjectDAO = new CollaborativeProjectDAO();
        try {
            collaborativeProjectList = collaborativeProjectDAO.getFinishedCollaborativeProjectsByProfessor(
                    professor.getIdProfessor());
        } catch (DAOException exception) {
            Log.getLogger(CollaborativeProjectsHistoryController.class).error(exception.getMessage(), 
                    exception);
        }

        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        termTableColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getRequesterCourse().getTerm().toString()));

        collaborativeProjectsTableView.getItems().addAll(collaborativeProjectList);
    }

    public void setProfessorSession(Professor professor) {
        this.professorSession = professor;
    }
}
