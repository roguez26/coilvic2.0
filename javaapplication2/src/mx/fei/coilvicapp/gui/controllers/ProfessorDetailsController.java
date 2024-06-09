package mx.fei.coilvicapp.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.university.University;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import log.Log;
import main.MainApp;

public class ProfessorDetailsController implements Initializable {

    @FXML
    private Button backButton;
    
    @FXML
    private TextField countryCodeTextField;
    

    @FXML
    private TextField emailTextField;

    @FXML
    private ComboBox<String> gendersComboBox;

    @FXML
    private TextField maternalSurnameTextField;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField paternalSurnameTextField;

    @FXML
    private Button showHistoryButton;

    @FXML
    private TextField telefonoTextField;

    @FXML
    private ComboBox<University> universitiesComboBox;
    private Professor professorForSeeDetails;
    private Professor professorSession;


    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {

    }
    
    @FXML
    void backButtonIsPressed(ActionEvent event) {
        if (professorSession == null) {
            changeToProfessorManager();
        } else {
            changeToProfessorManagerAsProfessor();
        }
    }
    
    private void changeToProfessorManager() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/ProfessorManager.fxml"));

            MainApp.changeView(fxmlLoader);
            ProfessorManagerController professorManagerController = fxmlLoader.getController();
            professorManagerController.setAllProfessorsMode(true);
        } catch (IOException exception) {
            Log.getLogger(ProfessorDetailsController.class).error(exception.getMessage(), exception);
        }
    }
    
    private void changeToProfessorManagerAsProfessor() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/ProfessorManager.fxml"));

            MainApp.changeView(fxmlLoader);
            ProfessorManagerController professorManagerController = fxmlLoader.getController();
            professorManagerController.setAllProfessorsMode(true);
            professorManagerController.setProfessorSession(professorSession);
        } catch (IOException exception) {
            Log.getLogger(ProfessorDetailsController.class).error(exception.getMessage(), exception);
        }
    }

    @FXML
    void showHistoryButtonIsPressed(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/CollaborativeProjectsHistory.fxml"));
        MainApp.changeView(fxmlLoader);
        CollaborativeProjectsHistoryController collaborativeProjectsHistoryController = fxmlLoader.getController();
        if (professorSession != null) {
            collaborativeProjectsHistoryController.setProfessorSession(professorSession);
        }
        collaborativeProjectsHistoryController.setProfessor(professorForSeeDetails);
    }

    private void initializeTextFields(Professor professor) {
        nombreTextField.setText(professor.getName());
        paternalSurnameTextField.setText(professor.getPaternalSurname());
        maternalSurnameTextField.setText(professor.getMaternalSurname());
        emailTextField.setText(professor.getEmail());
        telefonoTextField.setText(professor.getPhoneNumber());
        universitiesComboBox.setValue(professor.getUniversity());
        countryCodeTextField.setText(professor.getUniversity().getCountry().getCountryCode());
    }

    private void initializeTextFields() {
        nombreTextField.setText(professorForSeeDetails.getName());
        paternalSurnameTextField.setText(professorForSeeDetails.getPaternalSurname());
        maternalSurnameTextField.setText(professorForSeeDetails.getMaternalSurname());
        emailTextField.setText(professorForSeeDetails.getEmail());
        telefonoTextField.setText(professorForSeeDetails.getPhoneNumber());
        gendersComboBox.setValue(professorForSeeDetails.getGender());
        universitiesComboBox.setValue(professorForSeeDetails.getUniversity());
    }

    public void setProfessor(Professor professor) {
        this.professorForSeeDetails = professor;
        initializeTextFields(professor);

    }

    public void setProfessorSession(Professor professor) {
        this.professorSession = professor;
    }
}