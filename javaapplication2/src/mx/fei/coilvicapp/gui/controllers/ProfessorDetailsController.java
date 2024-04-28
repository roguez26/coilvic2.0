package mx.fei.coilvicapp.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import mx.fei.coilvicapp.logic.university.IUniversity;
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

public class ProfessorDetailsController implements Initializable {

    @FXML
    private TextField nombreTextField;
    
    @FXML
    private TextField paternalSurnameTextField;
    
    @FXML
    private TextField maternalSurnameTextField;
    
    @FXML
    private TextField emailTextField;
    
    @FXML
    private TextField genderTextField;
    
    @FXML
    private TextField telefonoTextField;
    
    @FXML
    private TextField universidadTextField;
    
    
    @FXML
    private Button updateProfessorButton;
    
    @FXML
    private Button validateButton;
    
    private ProfessorDAO professorDAO = new ProfessorDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Professor professor = new Professor();
        try {
            professor = professorDAO.getProfessorById(114);    
        } catch (DAOException exception) {
            Logger.getLogger(ProfessorDetailsController.class.getName()).log(Level.SEVERE, null, exception);
        }        
        nombreTextField.setText(professor.getName());
        paternalSurnameTextField.setText(professor.getPaternalSurname());
        maternalSurnameTextField.setText(professor.getMaternalSurname());
        emailTextField.setText(professor.getEmail());
        genderTextField.setText(professor.getGender());
        telefonoTextField.setText(professor.getPhoneNumber());
        universidadTextField.setText(professor.getUniversity().getName());
        
    }    
    
    
    
    @FXML
    private void updateProfessor(ActionEvent event) throws IOException {
        
    }
    
    @FXML
    private void validate(ActionEvent event) throws IOException {
        
    }
    
}
