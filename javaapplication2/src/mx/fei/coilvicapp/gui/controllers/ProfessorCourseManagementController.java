package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import mx.fei.coilvicapp.logic.course.*;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.language.Language;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.term.Term;

/**
 * FXML Controller class
 *
 * @author d0uble_d
 */
public class ProfessorCourseManagementController implements Initializable {    
  
    @FXML
    private Button backButton;
    
    @FXML
    private TextField searchTextField;
    
    @FXML
    private Button searchButton;

    @FXML
    private MenuButton statusMenuButton;
    
    @FXML
    private TableView<Course> coursesTableView;
    
    @FXML
    private TableColumn<Course, String> nameTableColumn;

    @FXML
    private TableColumn<Course, Term> termTableColumn;

    @FXML
    private TableColumn<Course, String> studentsProfileTableColumn;

    @FXML
    private TableColumn<Course, Language> languageTableColumn;

    @FXML
    private TableColumn<Course, String> statusTableColumn;
    
    @FXML
    private Button seeDetailsButton;
    
    @FXML
    private Button registerButton;
    
    private final CourseDAO COURSE_DAO = new CourseDAO();
    
    private Professor professor = new Professor();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        coursesTableView.getItems().addAll(initializeCoursesArray());
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        termTableColumn.setCellValueFactory(new PropertyValueFactory<>("term"));
        studentsProfileTableColumn.setCellValueFactory(new PropertyValueFactory<>("studentsProfile"));
        languageTableColumn.setCellValueFactory(new PropertyValueFactory<>("language"));
        statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    
        
    public Professor getProfesor() {
        return professor;
    }
    
    public void setProfessor(Professor professor) {
        this.professor = professor;        
    }
    
    @FXML
    private void backButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == backButton) {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/main");                           
        }
    }
    
    @FXML
    void searchButtonIsPressed(ActionEvent event) {
        if (event.getSource() == searchButton) {
            if (!searchTextField.getText().isEmpty()) {
                ArrayList<Course> courses = new ArrayList<>();
                try {
                    courses = COURSE_DAO.getCoursesByProfessorAndName(professor.getIdProfessor(),searchTextField.getText());                
                    updateTableView(courses);
                } catch (DAOException exception) {
                    handleDAOException(exception);
                }
            } else {
                DialogController.getInformativeConfirmationDialog("Campo de búsqueda vacío", "Ingrese el nombre de un curso");
            }
        }
    }
    
    @FXML
    void allMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        ArrayList<Course> courses = new ArrayList<>();
        courses = initializeCoursesArray();
        updateTableView(courses);        
    }
    
    @FXML
    void pendingMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        ArrayList<Course> courses = new ArrayList<>();
        try {
            courses = COURSE_DAO.getPendingCoursesByProfessor(professor.getIdProfessor());
            updateTableView(courses);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }
    
    @FXML
    void acceptedMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        ArrayList<Course> courses = new ArrayList<>();
        try {
            courses = COURSE_DAO.getAcceptedCoursesByProfessor(professor.getIdProfessor());
            updateTableView(courses);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }
    
    @FXML
    void rejectedMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        ArrayList<Course> courses = new ArrayList<>();
        try {
            courses = COURSE_DAO.getRejectedCoursesByProfessor(professor.getIdProfessor());
            updateTableView(courses);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }
    
    @FXML
    void colaborationMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        ArrayList<Course> courses = new ArrayList<>();
        try {
            courses = COURSE_DAO.getColaborationCoursesByProfessor(professor.getIdProfessor());
            updateTableView(courses);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }    
    
    @FXML
    void finishedMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        ArrayList<Course> courses = new ArrayList<>();
        try {
            courses = COURSE_DAO.getFinishedCoursesByProfessor(professor.getIdProfessor());
            updateTableView(courses);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }
    
    @FXML
    void cancelledMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        ArrayList<Course> courses = new ArrayList<>();
        try {
            courses = COURSE_DAO.getCancelledCoursesByProfessor(professor.getIdProfessor());
            updateTableView(courses);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }
    
    private void updateTableView(ArrayList<Course> courses) {
        coursesTableView.getItems().clear();
        coursesTableView.getItems().addAll(courses);
    }
    
    @FXML
    private void seeDetailsButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == seeDetailsButton) {
            Course course = (Course) coursesTableView.getSelectionModel().getSelectedItem();
            if (course != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/CourseDetails.fxml"));
                MainApp.changeView(fxmlLoader);                               
                CourseDetailsController courseDetailsController = fxmlLoader.getController();                
                courseDetailsController.setProfessor(professor);
                courseDetailsController.setCourse(course);
            } else {
                DialogController.getInformativeConfirmationDialog("Sin curso seleccionado", "Necesita seleccionar un curso para poder ver sus detalles");
            }
        }
    }
    
    @FXML
    private void registerButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == registerButton) {
            if (professor != null) {                
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/RegisterCourse.fxml"));
                MainApp.changeView(fxmlLoader);
                RegisterCourseController registerCourseController = fxmlLoader.getController();
                registerCourseController.setProfessor(professor);
            } else {
                // Algun error
                //
                //
                //
            }
        }
    }
    
    private ArrayList<Course> initializeCoursesArray() {
        ArrayList<Course> courses = new ArrayList<>();
        try {
            courses = COURSE_DAO.getAllCoursesByProfessor(professor.getIdProfessor());
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorCourseManagementController.class.getName()).log(Level.SEVERE, null, exception);
        }
        return courses;
    }
    
    private void handleDAOException(DAOException exception) {
        coursesTableView.getItems().clear();
        try {
            //DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/ ");
                case FATAL ->
                    MainApp.changeView("/main/MainApp");
            }
        } catch (IOException ioException) {
            Log.getLogger(NotifyProfessorController.class).error(exception.getMessage(), exception);
        }
    }
}