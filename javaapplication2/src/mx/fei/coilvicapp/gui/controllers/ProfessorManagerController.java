package mx.fei.coilvicapp.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.professor.ProfessorDAO;
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Window;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.implementations.FileManager;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;
import mx.fei.coilvicapp.logic.professor.IProfessor;

public class ProfessorManagerController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TableView professorsTableView;

    @FXML
    private TableColumn nameTableColumn;

    @FXML
    private TableColumn paternalSurnameTableColumn;

    @FXML
    private TableColumn maternalSurnameTableColumn;

    @FXML
    private TableColumn emailTableColumn;

    @FXML
    private TableColumn genderTableColumn;

    @FXML
    private TableColumn phoneNumberTableColumn;

    @FXML
    private TableColumn universityTableColumn;

    @FXML
    private Button seeDetailsButton;

    @FXML
    private Button exportValidatedProfessorsButton;
    private boolean allProfessorAreVisibles;
    private Professor professorSession;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        paternalSurnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("paternalSurname"));
        maternalSurnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("maternalSurname"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        genderTableColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        phoneNumberTableColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        universityTableColumn.setCellValueFactory(new PropertyValueFactory<>("university"));
        professorsTableView.getItems().addAll(initializeProfessorArray());
    }

    @FXML
    private void backButtonIsPressed(ActionEvent event) {
        goBack();
    }
    
    private void goBack() {
        try {
            if (professorSession != null) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/"
                            + "ProfessorMainMenu.fxml"));

                    MainApp.changeView(fxmlLoader);
                    ProfessorMainMenuController professorMainMenuController = fxmlLoader.getController();
                    professorMainMenuController.setProfessor(professorSession);
                }
            else if (allProfessorAreVisibles) {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/CoordinationMainMenu");
            } else {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/AssistantMainMenu");
            }
        } catch (IOException exception) {
            Log.getLogger(ProfessorManagerController.class).error(exception.getMessage(), exception);
        }
    }

    @FXML
    private void seeDetailsButtonIsPressed(ActionEvent event) {
        Professor professor = (Professor) professorsTableView.getSelectionModel().getSelectedItem();
        if (professor != null) {
            if (allProfessorAreVisibles) {
                changeToSeeHistory(professor);
            } else {
                changeToValidateProfessor(professor);
            }
        }
    }

    private void changeToSeeHistory(Professor professor) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/mx/fei/coilvicapp/gui/views/ProfessorDetails.fxml"));
            MainApp.changeView(fxmlLoader);
            ProfessorDetailsController professorDetailsController = fxmlLoader.getController();
            professorDetailsController.setProfessor(professor);
            if(professorSession != null) {
                professorDetailsController.setProfessorSession(professorSession);
            }
        } catch (IOException ioException) {
            Log.getLogger(ProfessorManagerController.class).error(ioException.getMessage(), ioException);
        }
    }

    private void changeToValidateProfessor(Professor professor) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/mx/fei/coilvicapp/gui/views/ProfessorValidate.fxml"));
            MainApp.changeView(fxmlLoader);
            ProfessorValidateController professorValidateController = fxmlLoader.getController();
            professorValidateController.setProfessorForValidation(professor);
        } catch (IOException ioException) {
            Log.getLogger(ProfessorManagerController.class).error(ioException.getMessage(), ioException);
        }
    }

    @FXML
    private void exportValidatedProfessorsButtonIsPressed(ActionEvent event) {
        FileManager fileManager = new FileManager();
        Window window = ((Node) event.getSource()).getScene().getWindow();
        try {
            fileManager.copyXLSXToSelectedDirectory(window);
        } catch (IOException exception) {
            handleIOException(exception);
            Log.getLogger(ProfessorManagerController.class).error(exception.getMessage(), exception);
        } catch (IllegalArgumentException exception) {
            handleValidationException(exception);
        }
    }

    private ArrayList<Professor> initializeProfessorArray() {
        IProfessor professorDAO = new ProfessorDAO();
        ArrayList<Professor> professors = new ArrayList<>();
        try {
            professors = professorDAO.getProfessorsByPendingStatus();
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
        return professors;
    }

    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getInvalidDataDialog(exception.getMessage());
    }

    private void handleIOException(IOException exception) {
        DialogController.getInformativeConfirmationDialog(
                "Lo sentimos", "No fue posible exportar los profesores validados");
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    goBack();
                case FATAL ->
                    MainApp.handleFatal();
            }
        } catch (IOException ioException) {
            Log.getLogger(ProfessorManagerController.class).error(ioException.getMessage(), ioException);
        }
    }

    public void setAllProfessorsMode(boolean allProfessorAreVisibles) {
        this.allProfessorAreVisibles = allProfessorAreVisibles;
        professorsTableView.getItems().clear();
        professorsTableView.getItems().addAll(initializeAllProfessorArray());
    }

    private ArrayList<Professor> initializeAllProfessorArray() {
        IProfessor professorDAO = new ProfessorDAO();
        ArrayList<Professor> professors = new ArrayList<>();
        try {
            professors = professorDAO.getAllProfessors();
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
        return professors;
    }

    public void setProfessorSession(Professor professor) {
        this.professorSession = professor;
        this.allProfessorAreVisibles = true;
        exportValidatedProfessorsButton.setVisible(false);
        professorsTableView.getItems().clear();
        professorsTableView.getItems().addAll(initializeAllProfessorArray());
    }

}
