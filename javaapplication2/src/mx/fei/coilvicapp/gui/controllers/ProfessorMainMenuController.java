package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.professor.Professor;

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
        if (event.getSource() == collaborativeOfferButton) {
            try {
                if (professor != null) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/CourseOffersOrProposalsManagement.fxml"));
                    MainApp.changeView(fxmlLoader);
                    CourseOffersOrProposalsManagementController courseOffersOrProposalsManagementController = fxmlLoader.getController();
                    courseOffersOrProposalsManagementController.setProfessor(professor);
                }
            } catch (IOException exception) {
                Log.getLogger(ProfessorMainMenuController.class).error(exception.getMessage(), exception);
            }
        }
    }

    @FXML
    void coursesButtonIsPressed(ActionEvent event) {
        if (event.getSource() == coursesButton) {
            try {
                if (professor != null) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/ProfessorCourseManagement.fxml"));
                    MainApp.changeView(fxmlLoader);
                    ProfessorCourseManagementController professorCourseManagementController = fxmlLoader.getController();
                    professorCourseManagementController.setProfessor(professor);
                }
            } catch (IOException exception) {
                Log.getLogger(ProfessorMainMenuController.class).error(exception.getMessage(), exception);
            }
        }
    }

    @FXML
    void logOutButton(ActionEvent event) {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/LoginParticipant.fxml");
        } catch (IOException exception) {
            Log.getLogger(ProfessorMainMenuController.class).error(exception.getMessage(), exception);
        }
    }

    @FXML
    void projectRequestsButtonIsPressed(ActionEvent event) {
        if (event.getSource() == projectRequestsButton) {
            try {
                if (professor != null) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/CollaborativeProjectRequestsManagement.fxml"));
                    MainApp.changeView(fxmlLoader);
                    CollaborativeProjectRequestsManagementController collaborativeProjectRequestsManagementController = fxmlLoader.getController();
                    collaborativeProjectRequestsManagementController.setProfessor(professor);
                }
            } catch (IOException exception) {
                Log.getLogger(ProfessorMainMenuController.class).error(exception.getMessage(), exception);
            }
        }
    }

    @FXML
    void projectsButtonIsPressed(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/CollaborativeProjectsProfessor.fxml"));
        try {
            MainApp.changeView(fxmlLoader);
            CollaborativeProjectsProfessorController collaborativeProjectsProfessorController = fxmlLoader.getController();
            collaborativeProjectsProfessorController.setProfessor(professor);
        } catch (IOException exception) {
            Log.getLogger(ProfessorMainMenuController.class).error(exception.getMessage(), exception);
        }
    }

    @FXML
    void userInformationLabelIsPressed(MouseEvent event) {
        try {
            if (professor != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/ProfessorValidate.fxml"));
                MainApp.changeView(fxmlLoader);
                ProfessorValidateController professorValidateController = fxmlLoader.getController();
                professorValidateController.setProfessorForUpdate(professor);
            }
        } catch (IOException exception) {
            Log.getLogger(ProfessorMainMenuController.class).error(exception.getMessage(), exception);
        }
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
        userInformationLabel.setText(professor.getName() + " " + professor.getPaternalSurname());
    }
}
