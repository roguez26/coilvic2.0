package mx.fei.coilvicapp.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import main.MainApp;
import mx.fei.coilvicapp.logic.collaborativeprojectrequest.*;
import mx.fei.coilvicapp.logic.course.*;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;
import mx.fei.coilvicapp.logic.professor.*;

/**
 * FXML Controller class
 *
 * @author edgar
 */
public class CollaborativeProjectRequestDetailsController implements Initializable {

    
    @FXML    
    private Button backButton;
    
    @FXML
    private Label nameCourse1Label;
    
    @FXML
    private TextField nameCourse1TextField;
    
    @FXML
    private TextArea generalObjectiveTextArea;
    
    @FXML
    private TextField topicsInterestTextField;
    
    @FXML
    private TextField numberStudentsTextField;
    
    @FXML
    private TextField languageTextField;
    
    @FXML
    private TextField studentsProfileTextField;
    
    @FXML
    private TextArea additionalInformationTextArea;
    
    @FXML
    private TextField termTextField;
    
    @FXML
    private Label nameCourse2Label;
    
    @FXML
    private TextField nameCourse2TextField;        
    
    @FXML
    private TextField nameProfessorTextField;
    
    @FXML
    private TextField paternalSurnameTextField;
    
    @FXML
    private TextField maternalSurnameTextField;
    
    @FXML
    private TextField emailTextField;
    
    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private TextField genderTextField;
    
    @FXML
    private TextField universityTextField;
    
    @FXML
    private TextField countryTextField;
    
    @FXML
    private TextField statusTextField;
    
    @FXML
    private TextField requestDateTextField;
    
    @FXML
    private TextField validationDateTextField;
               
    @FXML
    private Button cancelRequestButton;
    
    @FXML
    private Button rejectButton;
    
    @FXML
    private Button acceptButton;
    
    private final CollaborativeProjectRequestDAO COLLABORATIVE_PROJECT_REQUEST_DAO = new CollaborativeProjectRequestDAO();
    private CollaborativeProjectRequest collaborativeProjectRequest;
    
    private Professor professor;    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public CollaborativeProjectRequest getCollaborativeProjectRequest() {
        return collaborativeProjectRequest;
    }
    
    public void setCollaborativeProjectRequest(CollaborativeProjectRequest collaborativeProjectRequest) {
        if (collaborativeProjectRequest != null) {
            this.collaborativeProjectRequest = collaborativeProjectRequest;
            initializeAll();
        }
    }
    
    public Professor getProfessor() {              
        return professor;
    }
    
    public void setProfessor(Professor professor) {        
        this.professor = professor;
    }
    
    @FXML
    void backButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == backButton) {
            if (professor != null) {                
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/CollaborativeProjectRequestsManagement.fxml"));
                MainApp.changeView(fxmlLoader);
                CollaborativeProjectRequestsManagementController collaborativeProjectRequestsManagementController = fxmlLoader.getController();
                collaborativeProjectRequestsManagementController.setProfessor(professor);
            }            
        }
    }
    
    @FXML
    void cancelRequestButtonButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == cancelRequestButton) {
            // TODO
        }
    }
    
    @FXML
    void rejectButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == rejectButton) {
            // TODO
        }
    }
    
    @FXML
    void acceptButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == acceptButton) {
            // TODO    
        }
    }        
    
    public void initializeAll() {                
        if (collaborativeProjectRequest.getRequesterCourse().getProfessor().getIdProfessor() == 
        professor.getIdProfessor()) {
            nameCourse1Label.setText("Curso solicitado");            
            nameCourse1TextField.setText(collaborativeProjectRequest.getRequestedCourse().toString());
            generalObjectiveTextArea.setText(collaborativeProjectRequest.getRequestedCourse().getGeneralObjective());
            topicsInterestTextField.setText(collaborativeProjectRequest.getRequestedCourse().getTopicsInterest());
            numberStudentsTextField.setText(String.valueOf(collaborativeProjectRequest.getRequestedCourse().getNumberStudents()));
            languageTextField.setText(collaborativeProjectRequest.getRequestedCourse().getLanguage().toString());
            studentsProfileTextField.setText(collaborativeProjectRequest.getRequestedCourse().getStudentsProfile());                        
            additionalInformationTextArea.setText(collaborativeProjectRequest.getRequestedCourse().getAdditionalInformation());
            termTextField.setText(collaborativeProjectRequest.getRequestedCourse().getTerm().toString());
            
            nameProfessorTextField.setText(collaborativeProjectRequest.getRequestedCourse().getProfessor().getName());
            paternalSurnameTextField.setText(collaborativeProjectRequest.getRequestedCourse().getProfessor().getPaternalSurname());
            maternalSurnameTextField.setText(collaborativeProjectRequest.getRequestedCourse().getProfessor().getMaternalSurname());
            emailTextField.setText(collaborativeProjectRequest.getRequestedCourse().getProfessor().getEmail());
            phoneNumberTextField.setText(collaborativeProjectRequest.getRequestedCourse().getProfessor().getPhoneNumber());
            genderTextField.setText(collaborativeProjectRequest.getRequestedCourse().getProfessor().getGender());
            universityTextField.setText(collaborativeProjectRequest.getRequestedCourse().getProfessor().getUniversity().toString());
            countryTextField.setText(collaborativeProjectRequest.getRequestedCourse().getProfessor().getUniversity().getCountry().toString());
            
            nameCourse2Label.setText("Curso solicitante");            
            nameCourse2TextField.setText(collaborativeProjectRequest.getRequesterCourse().toString());                                    
            
            if (collaborativeProjectRequest.getStatus().equals("Pendiente")) {
                cancelRequestButton.setVisible(true);                
            }            
        } else {
            nameCourse1Label.setText("Curso solicitante");            
            nameCourse1TextField.setText(collaborativeProjectRequest.getRequesterCourse().toString());
            generalObjectiveTextArea.setText(collaborativeProjectRequest.getRequesterCourse().getGeneralObjective());
            topicsInterestTextField.setText(collaborativeProjectRequest.getRequesterCourse().getTopicsInterest());
            numberStudentsTextField.setText(String.valueOf(collaborativeProjectRequest.getRequesterCourse().getNumberStudents()));
            languageTextField.setText(collaborativeProjectRequest.getRequesterCourse().getLanguage().toString());
            studentsProfileTextField.setText(collaborativeProjectRequest.getRequesterCourse().getStudentsProfile());                        
            additionalInformationTextArea.setText(collaborativeProjectRequest.getRequesterCourse().getAdditionalInformation());
            termTextField.setText(collaborativeProjectRequest.getRequesterCourse().getTerm().toString());
            
            nameProfessorTextField.setText(collaborativeProjectRequest.getRequesterCourse().getProfessor().getName());
            paternalSurnameTextField.setText(collaborativeProjectRequest.getRequesterCourse().getProfessor().getPaternalSurname());
            maternalSurnameTextField.setText(collaborativeProjectRequest.getRequesterCourse().getProfessor().getMaternalSurname());
            emailTextField.setText(collaborativeProjectRequest.getRequesterCourse().getProfessor().getEmail());
            phoneNumberTextField.setText(collaborativeProjectRequest.getRequesterCourse().getProfessor().getPhoneNumber());
            genderTextField.setText(collaborativeProjectRequest.getRequesterCourse().getProfessor().getGender());
            universityTextField.setText(collaborativeProjectRequest.getRequesterCourse().getProfessor().getUniversity().toString());
            countryTextField.setText(collaborativeProjectRequest.getRequesterCourse().getProfessor().getUniversity().getCountry().toString());
            
            nameCourse2Label.setText("Curso solicitado");            
            nameCourse2TextField.setText(collaborativeProjectRequest.getRequesterCourse().toString());                                    

            if (collaborativeProjectRequest.getStatus().equals("Pendiente")) {
                rejectButton.setVisible(true);
                acceptButton.setVisible(true);
            }
        }
        statusTextField.setText(collaborativeProjectRequest.getStatus());
        requestDateTextField.setText(collaborativeProjectRequest.getRequestDate());
        validationDateTextField.setText(collaborativeProjectRequest.getValidationDate());        
    }
    
    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage (exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR -> {
                    if (professor == null) {
                        MainApp.changeView("/mx/fei/coilvicapp/gui/views/CourseOffersOrProposalsManagement");
                    } else {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/CourseOffersOrProposalsManagement.fxml"));
                        MainApp.changeView(fxmlLoader);
                        CourseOffersOrProposalsManagementController courseOffersOrProposalsManagementController = fxmlLoader.getController();
                        courseOffersOrProposalsManagementController.setProfessor(professor);
                    } 
                }               
                case FATAL -> MainApp.changeView("/main/MainApp");
                
            }
        } catch (IOException ioException) {
            
        }
    }
    
    private void handleValidationException(IllegalArgumentException ex) {
        DialogController.getDialog(new AlertMessage( ex.getMessage(), Status.WARNING));
    }     
}
