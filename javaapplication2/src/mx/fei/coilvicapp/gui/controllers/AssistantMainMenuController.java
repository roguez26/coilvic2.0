package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import log.Log;
import main.MainApp;

public class AssistantMainMenuController implements Initializable {

    @FXML
    private Button consultProjectsHistoryButton;

    @FXML
    private Button createFormButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Button representativesButton;

    @FXML
    private Button universitiesButton;

    @FXML
    private Button validateProfessorsButton;
    
    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
    }    

    @FXML
    private void createFormButtonIsPressed(ActionEvent event) {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/FeedbackForms");
        } catch (IOException exception) {
            Log.getLogger(AssistantMainMenuController.class).error(exception.getMessage(), exception);
        }
    }

    @FXML
    private void logOutButton(ActionEvent event) {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/LoginParticipant");
        } catch (IOException exception) {
            Log.getLogger(AssistantMainMenuController.class).error(exception.getMessage(), exception);
        }
    }

    @FXML
    private void representativesButtonIsPressed(ActionEvent event) {
        try {   
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/InstitutionalRepresentativeManager");
        } catch (IOException exception) {
            Log.getLogger(AssistantMainMenuController.class).error(exception.getMessage(), exception);
        }
    }
    
    @FXML
    private void universitiesButtonIsPressed(ActionEvent event) {
        try {   
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/UniversityManager");
        } catch (IOException exception) {
            Log.getLogger(AssistantMainMenuController.class).error(exception.getMessage(), exception);
        }
    }

    @FXML
    private void validateProfessorsButtonIsPressed(ActionEvent event) {
        try {   
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/ProfessorManager");
        } catch (IOException exception) {
            Log.getLogger(AssistantMainMenuController.class).error(exception.getMessage(), exception);
        }
    }
}
