package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.professor.Professor;

/**
 * FXML Controller class
 *
 * @author axel_
 */
public class ProfessorMainMenuController implements Initializable {

    @FXML
    private Button collaborativeOfferButton;

    @FXML
    private Button coursesButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Button projectRequestsButton;

    @FXML
    private Button projectsButton;

    @FXML
    private Label userInformationLabel;

    private Professor professor;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
    }

    @FXML
    void collaborativeOfferButtonIsPressed(ActionEvent event) {

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
    void projectRequestsButtonIsPressed(ActionEvent event) {

    }

    @FXML
    void projectsButtonIsPressed(ActionEvent event) {

    }

    @FXML
    void userInformationLabelIsPressed(MouseEvent event) {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/ProfessorValidate");
        } catch (IOException exception) {
            Log.getLogger(ProfessorMainMenuController.class).error(exception.getMessage(), exception);
        }
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
        userInformationLabel.setText(professor.getName() + " " + professor.getPaternalSurname());
    }

}
