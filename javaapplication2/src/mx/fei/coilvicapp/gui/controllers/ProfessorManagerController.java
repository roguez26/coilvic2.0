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
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.implementations.FileManager;

public class ProfessorManagerController implements Initializable {
    
    private UniversityDAO universityDAO = new UniversityDAO();
    private ProfessorDAO professorDAO = new ProfessorDAO();
    
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
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
    private void backButtonIsPressed(ActionEvent event) throws IOException {
        if (backConfirmation()) {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/main");   
        }
    }
    
    @FXML
    private boolean backConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog(
                "Regresar al menu principal", "¿Deseas regresar al menu principal?");
        return (response.get() == DialogController.BUTTON_YES);
    }
       
    @FXML
    private void seeDetailsButtonIsPressed(ActionEvent event) throws IOException {
        Professor professor = (Professor) professorsTableView.getSelectionModel().getSelectedItem();
        if (professor != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/mx/fei/coilvicapp/gui/views/ProfessorValidate.fxml"));
            MainApp.changeView(fxmlLoader);
            ProfessorValidateController professorValidateController = fxmlLoader.getController();
            professorValidateController.setProfessorForValidation(professor);
        } else {
            
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
            Log.getLogger(ProfessorManagerController.class).error(exception.getMessage(), exception);
        }
    }
    
    private ArrayList<Professor> initializeProfessorArray() {
        ArrayList<Professor> professors = new ArrayList<>();
        try {
            professors = professorDAO.getProfessorsByPendingStatus();
        } catch (DAOException exception) {
            Log.getLogger(ProfessorManagerController.class).error(exception.getMessage(), exception);
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
    
}