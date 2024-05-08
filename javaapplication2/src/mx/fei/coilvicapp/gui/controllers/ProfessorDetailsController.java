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
    private Button backButton;
    
    @FXML
    private TextField nombreTextField;
    
    @FXML
    private TextField paternalSurnameTextField;
    
    @FXML
    private TextField maternalSurnameTextField;
    
    @FXML
    private TextField emailTextField;
    
    @FXML
    private ComboBox<String> gendersComboBox;
    
    @FXML
    private TextField telefonoTextField;
    
    @FXML
    private ComboBox<University> universitiesComboBox;    
    
    @FXML
    private Button updateProfessorButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button validateButton;
    
    @FXML
    private Button updateButton;    
    
    @FXML
    private Professor professor;
    
    private ProfessorDAO professorDAO = new ProfessorDAO();
    private UniversityDAO universityDAO = new UniversityDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    @FXML
    private void back(ActionEvent event) throws IOException {
        if (event.getSource() == backButton) {
            if (backConfirmation()) {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/ProfessorManager");   
            }            
        }
    }
    
    @FXML
    private boolean backConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Regresar a la ventana profesores", "¿Deseas regresar a la ventana profesores?");
        return (response.get() == DialogController.BUTTON_YES);
    }    
        
    @FXML
    private void updateProfessor(ActionEvent event) throws IOException {
        if (event.getSource() == updateProfessorButton) {
            changeComponentsEditabilityTrue();
        }
    }
    
    private void changeComponentsEditabilityTrue() {
        updateProfessorButton.setVisible(false);
        validateButton.setVisible(false);
        cancelButton.setVisible(true);
        cancelButton.setDisable(false);
        updateButton.setVisible(true);
        updateButton.setDisable(false);
        gendersComboBox.setDisable(false);
        
        nombreTextField.setEditable(true);
        paternalSurnameTextField.setEditable(true);
        maternalSurnameTextField.setEditable(true);
        emailTextField.setEditable(true);
        gendersComboBox.setDisable(false);
        telefonoTextField.setEditable(true);
    }
    
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        if (event.getSource() == cancelButton) {
            if(cancelConfirmation()) {
                changeComponentsEditabilityFalse();
                initializeTextFields();
            } 
        } 
    } 
    
    private void changeComponentsEditabilityFalse() {
        updateProfessorButton.setVisible(true);
        validateButton.setVisible(true);
        cancelButton.setVisible(false);
        updateButton.setVisible(false);
        gendersComboBox.setDisable(true);
        
        nombreTextField.setEditable(false);
        paternalSurnameTextField.setEditable(false);
        maternalSurnameTextField.setEditable(false);
        emailTextField.setEditable(false);
        gendersComboBox.setDisable(false);
        telefonoTextField.setEditable(false);
    }    
    
    private boolean cancelConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Regresar a los detalles del profesor", "¿Desea deshacer los cambios y regresar?");
        return (response.get() == DialogController.BUTTON_YES);        
    }
    
    @FXML
    private void validate(ActionEvent event) throws IOException {
        
    }
    
    @FXML
    private void update(ActionEvent event) throws IOException {
        int rowsAffected = -1;
        if (event.getSource() == updateButton) {
            if(updateConfirmation()) {
                try {
                    rowsAffected = professorDAO.updateProfessor(initializeProfessor());
                } catch (DAOException exception) {
                    Logger.getLogger(ProfessorDetailsController.class.getName()).log(Level.SEVERE, null, exception);
                }      
            }
            if (rowsAffected > 0) {
                if (wasUpdatedConfirmation()) {
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/ProfessorManager");   
                } else {
                    wasNotUpdatedConfirmation();
                }
            } 
        }               
    }  
    
    private boolean wasNotUpdatedConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog("Informacion no actualizada", "Los cambios no se pudieron realizar");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }    
    
    private boolean wasUpdatedConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog("Informacion actualizada", "Los cambios fueron realizados con exito");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }
    
    private boolean updateConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cambios", "¿Desea realizar los cambios?");
        return (response.get() == DialogController.BUTTON_YES);        
    }    
    
    private Professor initializeProfessor() {
        Professor newProfessorInformation = new Professor();
        
        newProfessorInformation.setIdProfessor(professor.getIdProfessor());
        newProfessorInformation.setName(nombreTextField.getText());
        newProfessorInformation.setPaternalSurname(paternalSurnameTextField.getText());
        newProfessorInformation.setMaternalSurname(maternalSurnameTextField.getText());
        newProfessorInformation.setEmail(emailTextField.getText());
        newProfessorInformation.setGender(gendersComboBox.getValue());
        newProfessorInformation.setPhoneNumber(telefonoTextField.getText());        
        return newProfessorInformation;
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
    
    public Professor getProfessor() {
        return professor;
    }
    
}
