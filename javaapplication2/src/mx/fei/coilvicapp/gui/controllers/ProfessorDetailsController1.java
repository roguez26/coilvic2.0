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
import java.util.ArrayList;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import log.Log;
import main.MainApp;

public class ProfessorDetailsController1 implements Initializable {

    @FXML
    private Button backButton;

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

    @FXML
    void backButtonIsPressed(ActionEvent event) {

    }

    @FXML
    void showHistoryButtonIsPressed(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/CollaborativeProjectsHistory.fxml"));
        MainApp.changeView(fxmlLoader);
        CollaborativeProjectsHistoryController collaborativeProjectsHistoryController = fxmlLoader.getController();
        collaborativeProjectsHistoryController.setProfessor(professor);
    }

    @FXML
    private Professor professor;
    
    

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
        professor = new Professor();
        professor.setEmail("aaren@uv.mx");
        professor.setGender("Mujer");
        professor.setIdProfessor(5);
        professor.setIdUniversity(3);
        professor.setMaternalSurname("Valdes");
        professor.setName("Maria");
        professor.setPaternalSurname("Arenas");
        professor.setPhoneNumber("1234567890");
        
        initializeTextFields();
        
    }

    @FXML
    private void back(ActionEvent event) {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/ProfessorManager");
        } catch (IOException exception) {
            Log.getLogger(ProfessorDetailsController1.class).error(exception.getMessage(), exception);
        }
    }

    private void initializeTextFields(Professor professor) {
        nombreTextField.setText(professor.getName());
        paternalSurnameTextField.setText(professor.getPaternalSurname());
        maternalSurnameTextField.setText(professor.getMaternalSurname());
        emailTextField.setText(professor.getEmail());
        telefonoTextField.setText(professor.getPhoneNumber());
    }

    private void initializeTextFields() {
        nombreTextField.setText(professor.getName());
        paternalSurnameTextField.setText(professor.getPaternalSurname());
        maternalSurnameTextField.setText(professor.getMaternalSurname());
        emailTextField.setText(professor.getEmail());
        telefonoTextField.setText(professor.getPhoneNumber());
        gendersComboBox.setValue(professor.getGender());
    }

    private void initializeGenderComboBox(Professor professor) {
        ArrayList<String> genders = new ArrayList<>();
        genders.add("Hombre");
        genders.add("Mujer");
        genders.add("Otro");
        gendersComboBox.setItems(FXCollections.observableArrayList(genders));
        gendersComboBox.setValue(professor.getGender());
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
        initializeTextFields(professor);
        initializeGenderComboBox(professor);
        universitiesComboBox.setValue(professor.getUniversity());
    }
}
