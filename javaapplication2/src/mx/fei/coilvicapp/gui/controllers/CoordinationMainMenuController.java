package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import log.Log;
import main.MainApp;

public class CoordinationMainMenuController implements Initializable {

    @FXML
    private Button consultButton;

    @FXML
    private Button coursesButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Button projectsButton;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
    }

    @FXML
    void consultButtonIsPressed(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/"
                    + "ProfessorManager.fxml"));

            MainApp.changeView(fxmlLoader);
            ProfessorManagerController professorManagerController = fxmlLoader.getController();
            professorManagerController.setAllProfessorsMode(true);
        } catch (IOException exception) {
            Log.getLogger(CoordinationMainMenuController.class).error(exception.getMessage(), 
                    exception);
        }
    }

    @FXML
    void coursesButtonIsPressed(ActionEvent event) {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/CourseOffersOrProposalsManagement");
        } catch (IOException exception) {
            Log.getLogger(CoordinationMainMenuController.class).error(exception.getMessage(), 
                    exception);
        }
    }

    @FXML
    void logOutButton(ActionEvent event) {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/LoginParticipant");
        } catch (IOException exception) {
            Log.getLogger(CoordinationMainMenuController.class).error(exception.getMessage(), 
                    exception);
        }
    }

    @FXML
    void projectsButtonIsPressed(ActionEvent event) {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/CollaborativeProjectsManagement");
        } catch (IOException exception) {
            Log.getLogger(CoordinationMainMenuController.class).error(exception.getMessage(), 
                    exception);
        }
    }

}
