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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.course.*;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;
import mx.fei.coilvicapp.logic.language.Language;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.term.Term;

/**
 * FXML Controller class
 *
 * @author d0ubl3_d
 */
public class CourseOffersOrProposalsManagementController implements Initializable {
    
    @FXML    
    private Label titleLabel;
    
    @FXML
    private Button backButton;
    
    @FXML
    private TextField searchTextField;
    
    @FXML
    private Button searchButton;
    
    @FXML
    private Button seeAllButton;
    
    @FXML
    private TableView<Course> coursesTableView;
    
    @FXML
    private TableColumn<Course, String> nameTableColumn;

    @FXML
    private TableColumn<Professor, String> professorTableColumn;

    @FXML
    private TableColumn<Term, String> termTableColumn;
    
    @FXML
    private TableColumn<Course, String> universityTableColumn;
    
    @FXML
    private TableColumn<Course, String> countryTableColumn;
    
    @FXML
    private TableColumn<Course, Language> languageTableColumn;
    
    @FXML
    private Button seeDetailsButton;
            
    private final CourseDAO COURSE_DAO = new CourseDAO();
    
    private Professor professor;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {       
        initializeAll();
    }
    
    public Professor getProfessor() {
        return professor;
    }
    
    public void setProfessor(Professor professor) {
        if (professor != null) {
            this.professor = professor;
            initializeAll();
        } else {
            // ERROR
        }
    }
    
    @FXML
    private void backButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == backButton) {
            if (professor == null) {
                // VENTANA DE LA ADMINISTRACION COILVIC
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/CoordinationMainMenu");
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/ProfessorMainMenu.fxml"));
                MainApp.changeView(fxmlLoader);
                ProfessorMainMenuController professorMainMenuController = fxmlLoader.getController();
                professorMainMenuController.setProfessor(professor);                
            }            
        }
    }
    
    @FXML
    void searchButtonIsPressed(ActionEvent event) {
        if (event.getSource() == searchButton) {
            if (!searchTextField.getText().isEmpty()) {
                ArrayList<Course> courses = new ArrayList<>();                
                try {
                    if (professor == null) {
                        courses = COURSE_DAO.getCourseProposalsByUniversity(searchTextField.getText());                
                    } else {
                        courses = COURSE_DAO.getCourseOfferingsByUniversity(searchTextField.getText());                
                    }                    
                    updateTableView(courses);
                } catch (DAOException exception) {
                    handleDAOException(exception);
                }
            } else {
                DialogController.getInformativeConfirmationDialog("Campo de búsqueda vacío", "Ingrese el nombre de una universidad");
            }
        }
    }
    
    @FXML
    void seeAllButtonIsPressed(ActionEvent event) {
        if (event.getSource() == seeAllButton) {
            updateTableView(initializeCoursesArray());
            searchTextField.setText("");
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
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/CourseOfferOrProposalDetails.fxml"));
                MainApp.changeView(fxmlLoader);
                CourseOfferOrProposalDetailsController courseOfferOrProposalDetailsController = fxmlLoader.getController();
                if (professor != null) {
                    courseOfferOrProposalDetailsController.setProfessor(professor);
                }
                courseOfferOrProposalDetailsController.setCourse(course);                
            } else {
                DialogController.getInformativeConfirmationDialog("Sin curso seleccionado", "Necesita seleccionar un curso para poder ver sus detalles");
            }
        }
    }
    
    private void initializeAll() {
        coursesTableView.getItems().addAll(initializeCoursesArray());
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        professorTableColumn.setCellValueFactory(new PropertyValueFactory<>("professor"));
        termTableColumn.setCellValueFactory(new PropertyValueFactory<>("term"));               
        universityTableColumn.setCellValueFactory((var cellData) -> {
            Course course = cellData.getValue();
            if (course != null && course.getProfessor() != null) {
                return new SimpleStringProperty(course.getProfessor().getUniversity().toString());
            } else {
                return new SimpleStringProperty("");
            }
        });    
        countryTableColumn.setCellValueFactory(cellData -> {
            Course course = cellData.getValue();
            if (course != null && course.getProfessor() != null) {
                return new SimpleStringProperty(course.getProfessor().getUniversity().getCountry().toString());
            } else {
                return new SimpleStringProperty("");
            }
        });                 
        languageTableColumn.setCellValueFactory(new PropertyValueFactory<>("language"));
        if (professor != null) {
            titleLabel.setText("Oferta Cursos");
        }                        
    }
    
    private ArrayList<Course> initializeCoursesArray() {
        ArrayList<Course> courses = new ArrayList<>();
        try {
            if (professor == null) {
                courses = COURSE_DAO.getCourseProposals();
            } else {
                courses = COURSE_DAO.getCourseOfferings();
            }            
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
        return courses;
    }
    
    private void handleDAOException(DAOException exception) {
        coursesTableView.getItems().clear();
        try {
             DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/ ");
                case FATAL ->
                    MainApp.handleFatal();
            }
        } catch (IOException ioException) {
            Log.getLogger(NotifyProfessorController.class).error(ioException.getMessage(), ioException);
        }
    }    
}
