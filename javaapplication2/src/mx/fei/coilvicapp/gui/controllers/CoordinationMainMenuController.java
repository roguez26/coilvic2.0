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
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    @FXML
    void consultButtonIsPressed(ActionEvent event) {

    }

    @FXML
    void coursesButtonIsPressed(ActionEvent event) {

    }

    @FXML
    void logOutButton(ActionEvent event) {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/LoginParticipant");
        } catch (IOException exception) {
            Log.getLogger(ProfessorMainMenuController.class).error(exception.getMessage(), exception);
        }
    }

    @FXML
    void projectsButtonIsPressed(ActionEvent event) {

    }    
    
}
