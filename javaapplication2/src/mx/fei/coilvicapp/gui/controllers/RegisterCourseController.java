package mx.fei.coilvicapp.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;
import java.io.IOException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import main.MainApp;
import mx.fei.coilvicapp.logic.course.*;
import mx.fei.coilvicapp.logic.language.*;
import mx.fei.coilvicapp.logic.professor.*;
import mx.fei.coilvicapp.logic.term.*;

/**
 * FXML Controller class
 *
 * @author d0uble_d
 */
public class RegisterCourseController implements Initializable {
    
    @FXML
    private TextField nameTextField;
    
    @FXML
    private TextArea generalObjectiveTextArea;
    
    @FXML
    private TextField topicsInterestTextField;
    
    @FXML
    private ComboBox<Integer> numberStudentsComboBox;
    
    @FXML
    private ComboBox<Term> termComboBox;
    
    @FXML
    private ComboBox<Language> languageComboBox;
    
    @FXML
    private TextField studentsProfileTextField;
    
    @FXML
    private TextArea additionalInformationTextArea;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button saveButton;
    
    private Professor professor = new Professor();
    private final CourseDAO COURSE_DAO = new CourseDAO();
    private final TermDAO TERM_DAO = new TermDAO();
    private final LanguageDAO LANGUAGE_DAO = new LanguageDAO();    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {           
        numberStudentsComboBox.setItems(FXCollections.observableArrayList(initializeNumberStudentsArrayForComboBox()));
        termComboBox.setItems(FXCollections.observableArrayList(initializeTermsArrayForComboBox()));
        languageComboBox.setItems(FXCollections.observableArrayList(initializeLanguagesArrayForComboBox()));
    }
    
    public Professor getProfessor() {
        return professor;
    }
    
    public void setProfessor(Professor professor) {
        this.professor = professor;        
    }
    
    @FXML
    private void cancelButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == cancelButton) {
            if (confirmCancelation()) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/ProfessorCourseManagement.fxml"));
                MainApp.changeView(fxmlLoader);
                ProfessorCourseManagementController professorCourseManagementController = fxmlLoader.getController();
                professorCourseManagementController.setProfessor(professor);                
            }
        }
    }
    
    private boolean confirmCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cancelacion", "¿Está seguro de que desea cancelar?");
        return (response.get() == DialogController.BUTTON_YES);
    }
    
    private ArrayList<Integer> initializeNumberStudentsArrayForComboBox() {
        ArrayList<Integer> numberStudents = new ArrayList<>();
        
        for (int number = 1; number <= 50; number++) {
            numberStudents.add(number);
        }
        
        return numberStudents;
    }
    
    private ArrayList<Term> initializeTermsArrayForComboBox() {
        ArrayList<Term> terms = new ArrayList<>();
        try {
            terms = TERM_DAO.getTerms();
        } catch(DAOException exception) {
            Logger.getLogger(RegisterCourseController.class.getName()).log(Level.SEVERE, null, exception);
        }
        return terms;
    }
    
    private ArrayList<Language> initializeLanguagesArrayForComboBox() {
        ArrayList<Language> languages = new ArrayList<>();
        try {
            languages = LANGUAGE_DAO.getLanguages();
        } catch(DAOException exception) {
            Logger.getLogger(RegisterCourseController.class.getName()).log(Level.SEVERE, null, exception);
        }
        return languages;
    }
    
    private boolean wasRegisteredConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog
        ("Registrado","El curso se ha registrado con exito");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }
    
    private boolean emptyFieldsConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog
        ("Campos vacios","Debe completar todos los campos");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }
    
    @FXML
    private void saveButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == saveButton) {
            try {
                invokeCourseRegistration();                
            } catch (IllegalArgumentException exception) {
                handleValidationException(exception);
            } catch (DAOException exception) {
                handleDAOException(exception);
            }
        }
    }
    
    private void invokeCourseRegistration() throws DAOException, IOException {
        if (!fieldsAreEmpty()) {            
            int idCourse = COURSE_DAO.registerCourse(initializeCourse());
            if (idCourse > 0) {
                wasRegisteredConfirmation();
                cleanFields();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/ProfessorCourseManagement.fxml"));
                MainApp.changeView(fxmlLoader);
                ProfessorCourseManagementController professorCourseManagementController = fxmlLoader.getController();
                professorCourseManagementController.setProfessor(professor);
            }
        } else {
            emptyFieldsConfirmation();
        }
    }
    
    private void cleanFields() {
        nameTextField.setText("");
        generalObjectiveTextArea.setText("");
        topicsInterestTextField.setText("");
        numberStudentsComboBox.setValue(null);
        termComboBox.setValue(null);
        languageComboBox.setValue(null);
        studentsProfileTextField.setText("");
        additionalInformationTextArea.setText("");
    }
    
    private boolean fieldsAreEmpty() {
        return nameTextField.getText().isEmpty() || 
               generalObjectiveTextArea.getText().isEmpty() ||
               topicsInterestTextField.getText().isEmpty() ||
               numberStudentsComboBox.getValue() == null ||
               termComboBox.getValue() == null ||
               languageComboBox.getValue() == null ||
               studentsProfileTextField.getText().isEmpty() ||
               additionalInformationTextArea.getText().isEmpty();
    }

    private Course initializeCourse() {
        Course course = new Course();
        course.setProfessor(professor);
        course.setName(nameTextField.getText());
        course.setGeneralObjective(generalObjectiveTextArea.getText());
        course.setTopicsInterest(topicsInterestTextField.getText());
        course.setNumberStudents((int) numberStudentsComboBox.getValue());
        course.setTerm((Term) termComboBox.getValue());
        course.setLanguage((Language) languageComboBox.getValue());
        course.setStudentsProfile(studentsProfileTextField.getText());
        course.setAdditionalInformation(additionalInformationTextArea.getText());        
        return course;
    }
    
    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage (exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR -> MainApp.changeView("/mx/fei/coilvicapp/gui/views/MainApp");
                case FATAL -> MainApp.changeView("/main/MainApp");
                
            }
        } catch (IOException ioException) {
            
        }
    }
    
    private void handleValidationException(IllegalArgumentException ex) {
        DialogController.getDialog(new AlertMessage( ex.getMessage(), Status.WARNING));
    }
    
}
