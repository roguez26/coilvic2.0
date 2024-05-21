package mx.fei.coilvicapp.gui.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import mx.fei.coilvicapp.logic.collaborativeproject.ICollaborativeProject;
import mx.fei.coilvicapp.logic.course.Course;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.university.University;

/**
 *
 * @author ivanr
 */
public class CollaborativeProjectsManagmentController implements Initializable {
    
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
    private TableColumn<Course, String> courseOneTableColumn;

    @FXML
    private TableColumn<Course, String> courseTwoTableColumn;

    @FXML
    private TableColumn nameTableColumn;
    
    @FXML
    private TableColumn<University, String> universityOneTableColumn;

    @FXML
    private TableColumn<University, String> universityTwoTableColumn;

    private final ICollaborativeProject COLLABORATIVE_PROJECT_DAO = new CollaborativeProjectDAO();
    @FXML
    private Button validateButton;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
       ArrayList<CollaborativeProject> collaborativeProjectsList = new ArrayList<>();
        
        try {
            collaborativeProjectsList = COLLABORATIVE_PROJECT_DAO.getCollaborativeProjectsProposals();
        } catch (DAOException exception) {
            
        }
        System.out.println(collaborativeProjectsList);
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        courseOneTableColumn.setCellValueFactory(new PropertyValueFactory<>("CourseRequested"));
        courseTwoTableColumn.setCellValueFactory(new PropertyValueFactory<>("CourseRequester"));
        universityOneTableColumn.setCellValueFactory(new PropertyValueFactory<>("modality"));
        universityTwoTableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        collaborativeProjecsTableView.getItems().addAll(collaborativeProjectsList);
    }
    
    @FXML
    void seeDetailsButton(ActionEvent event) {

    }

    @FXML
    void statusMenuButtonIsSelected(ActionEvent event) {

    }

    @FXML
    void validateButtonIsPressed(ActionEvent event) {
        
        
//        
//        acronymTableColumn.setCellValueFactory(new PropertyValueFactory<>("acronym"));
//        jurisdictionTableColumn.setCellValueFactory(new PropertyValueFactory<>("jurisdiction"));
//        cityTableColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
//        countryTableColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
//        universitiesTableView.getItems().addAll(universitiesList);

    }
    
    @FXML
    void backButtonIsPressed(ActionEvent event) {

    }

    @FXML
    void searchButtonIsPressed(ActionEvent event) {

    }
    
}
