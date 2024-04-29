
package mx.fei.coilvicapp.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.professor.IProfessor;
import mx.fei.coilvicapp.logic.professor.ProfessorDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;
import java.io.IOException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import main.MainApp;

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
    private Button deleteButton;
    
    @FXML
    private Button updateButton;
    
    @FXML
    private Button registerButton;
    
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
    private void back(ActionEvent event) throws IOException {
        if (event.getSource() == backButton) {
            if (backConfirmation()) {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/main");   
            }            
        }
    }
    
    private boolean backConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Regresar al menu principal", "¿Deseas regresar al menu principal?");
        return (response.get() == DialogController.BUTTON_YES);
    }
    
    @FXML
    private void delete(ActionEvent event) throws IOException {
        if (event.getSource() == deleteButton) {
            //////////
        }
    }
    
    @FXML
    private void update(ActionEvent event) throws IOException {
        if (event.getSource() == updateButton) {
            Professor professor = (Professor) professorsTableView.getSelectionModel().getSelectedItem();
            if (professor != null){
                //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/UpdateUniversity.fxml"));
                //MainApp.changeView(fxmlLoader);
                //UpdateUniversityController updateUniversitycontroller = fxmlLoader.getController();
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/ProfessorDetails");   
                //updateUniversitycontroller.setUniversity(university);                
            } else {
                
            }
        }
    }

    @FXML
    private void register(ActionEvent event) throws IOException {
        if (event.getSource() == registerButton) {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/RegisterProfessor");
        }
    }    
    
    private ArrayList<Professor> initializeProfessorArray() {
        ArrayList<Professor> professors = new ArrayList<>();
        try {
            professors = professorDAO.getAllProfessors();
        } catch (DAOException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        return professors;
    }
    
}
