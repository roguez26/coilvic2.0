package mx.fei.coilvicapp.gui.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import log.Log;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import mx.fei.coilvicapp.logic.collaborativeproject.ICollaborativeProject;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.professor.Professor;

/**
 *
 * @author ivanr
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
    private final ICollaborativeProject COLLABORATIVE_PROJECT_DAO = new CollaborativeProjectDAO();

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {

    }

    @FXML
    void backButtonIsPressed(ActionEvent event) {

    }

    @FXML
    void seeDetailsButtonIsPressed(ActionEvent event) {

    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
        fillCollborativeProjectsTableView(professor);
    }

    private void fillCollborativeProjectsTableView(Professor professor) {
        ArrayList<CollaborativeProject> collaborativeProjectList = new ArrayList<>();

        try {
            collaborativeProjectList = COLLABORATIVE_PROJECT_DAO.getFinishedCollaborativeProjectsByProfessor(professor.getIdProfessor(), "Finalizado");
        } catch (DAOException exception) {
            Log.getLogger(CollaborativeProjectsHistoryController.class).error(exception.getMessage(), exception);
        }

        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        termTableColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getRequesterCourse().getTerm().toString()));
       
        collaborativeProjectsTableView.getItems().addAll(collaborativeProjectList);
    }
}
