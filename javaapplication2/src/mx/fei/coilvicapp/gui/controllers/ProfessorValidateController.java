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
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.professor.ProfessorDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.io.IOException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.implementations.Status;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;

public class ProfessorValidateController implements Initializable {

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
    private Label universityLabel;    
    
    @FXML
    private ComboBox<University> universitiesComboBox;    
    
    @FXML
    private Label passwordLabel;    
    
    @FXML
    private PasswordField identifierPasswordField;
    
    @FXML
    private Button showButton;    
    
    @FXML
    private Button rejectButton;   

    @FXML
    private Button cancelButton;    
    
    @FXML
    private Button updateButton;   
    
    @FXML
    private Button saveButton;    
    
    @FXML
    private Button acceptButton;      

    @FXML
    private Professor professor;
    
    private ProfessorDAO professorDAO = new ProfessorDAO();
    private UniversityDAO universityDAO = new UniversityDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    @FXML
    private void backButtonIsPressed(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/ProfessorMainMenu.fxml"));
        if (backConfirmation()) {
            try {   
                MainApp.changeView(fxmlLoader);
                ProfessorMainMenuController professorMainMenuController = fxmlLoader.getController();
                professorMainMenuController.setProfessor(professor);
            } catch (IOException exception) {
                Log.getLogger(ProfessorValidateController.class).error(exception.getMessage(), exception);
            }
        }            
    }  
    
    @FXML
    private void rejectButtonIsPressed(ActionEvent event) throws  IOException {
        
    }
    
    @FXML
    private void cancelButtonIsPressed(ActionEvent event) throws IOException {
        if(cancelConfirmation()) {
            changeComponentsEditabilityFalseForUpdate();
            initializeTextFields();
        } 
    }      
        
    @FXML
    private void updateButtonIsPressed(ActionEvent event) throws IOException {
        changeComponentsEditabilityTrueForUpdate();
    } 
    
    @FXML
    private void saveButtonIsPressed(ActionEvent event) {
        int rowsAffected = -1;
        if(updateConfirmation()) {
            try {
                rowsAffected = professorDAO.updateProfessor(initializeProfessor());
            } catch (DAOException exception) {
                handleDAOException(exception);
                Log.getLogger(ProfessorValidateController.class).error(exception.getMessage(), exception);
            }      
        }
        if (rowsAffected > 0) {
            if (wasUpdatedConfirmation()) {
                try {   
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/ProfessorManager");
                } catch (IOException exception) {
                    Log.getLogger(ProfessorValidateController.class).error(exception.getMessage(), exception);
                }
            } else {
                wasNotUpdatedConfirmation();
            }
        } 
    } 

    @FXML
    private void acceptButtonIsPressed(ActionEvent event) {
        
    }
    
    @FXML
    private void showButtonIsPressed(MouseEvent event) {

    }

    @FXML
    private void showButtonIsReleased(MouseEvent event) {

    }    
    
    private void changeComponentsEditabilityTrueForUpdate() {
        cancelButton.setVisible(true);
        updateButton.setVisible(false);
        saveButton.setVisible(true);
        
        cancelButton.setManaged(true);
        updateButton.setManaged(false);
        saveButton.setManaged(true);
        
        nombreTextField.setEditable(true);
        paternalSurnameTextField.setEditable(true);
        maternalSurnameTextField.setEditable(true);
        emailTextField.setEditable(true);
        gendersComboBox.setDisable(false);
        telefonoTextField.setEditable(true);
        passwordLabel.setVisible(true);
        passwordLabel.setManaged(true);
        identifierPasswordField.setEditable(true);
        identifierPasswordField.setVisible(true);
        identifierPasswordField.setManaged(true);
        showButton.setVisible(true);
        showButton.setManaged(true);
    }   
    
    private void changeComponentsEditabilityFalseForUpdate() {
        cancelButton.setVisible(false);
        updateButton.setVisible(true);
        saveButton.setVisible(false);
        cancelButton.setManaged(false);
        updateButton.setManaged(true);
        saveButton.setManaged(false);
        nombreTextField.setEditable(false);
        paternalSurnameTextField.setEditable(false);
        maternalSurnameTextField.setEditable(false);
        emailTextField.setEditable(false);
        gendersComboBox.setDisable(true);
        telefonoTextField.setEditable(false);
        
        passwordLabel.setVisible(false);
        passwordLabel.setManaged(false);
        identifierPasswordField.setEditable(false);
        identifierPasswordField.setVisible(false);
        identifierPasswordField.setManaged(false);
        showButton.setVisible(false);
        showButton.setManaged(false);
    }    
    
    private void changeComponentsVisibilityTrueForValidation() {
        universityLabel.setVisible(true);
        universityLabel.setManaged(true);
        universitiesComboBox.setVisible(true);
        universitiesComboBox.setManaged(true);
        
        updateButton.setVisible(false);
        updateButton.setManaged(false);
        acceptButton.setVisible(true);
        acceptButton.setManaged(true);
        rejectButton.setVisible(true);
        rejectButton.setManaged(true);
    }
    
   private void changeComponentsVisibilityFalseForValidation() {
        universityLabel.setVisible(false);
        universityLabel.setManaged(false);
        universitiesComboBox.setVisible(false);
        universitiesComboBox.setManaged(false);
        
        updateButton.setVisible(true);
        updateButton.setManaged(true);
        acceptButton.setVisible(false);
        acceptButton.setManaged(false);
        rejectButton.setVisible(false);
        rejectButton.setManaged(false);
    }    
         
    
    private boolean backConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Regresar a la ventana profesores", "¿Deseas regresar a la ventana profesores?");
        return (response.get() == DialogController.BUTTON_YES);
    }   
    
    private boolean cancelConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Regresar a los detalles del profesor", "¿Desea deshacer los cambios y regresar?");
        return (response.get() == DialogController.BUTTON_YES);        
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
        identifierPasswordField.setText("");
    }
    
    private void initializeGenderComboBox(Professor professor) {
        ArrayList<String> genders = new ArrayList<>();
        genders.add("Hombre");
        genders.add("Mujer");
        genders.add("Otro");
        gendersComboBox.setItems(FXCollections.observableArrayList(genders));
        gendersComboBox.setValue(professor.getGender());
    }
        
    public void setProfessorForUpdate(Professor professor) {
        this.professor = professor;
        initializeTextFields(professor);
        initializeGenderComboBox(professor);
        universitiesComboBox.setValue(professor.getUniversity());    
    }
    
    public void setProfessorForValidation(Professor professor) {
        this.professor = professor;
        initializeTextFields(professor);
        initializeGenderComboBox(professor);
        universitiesComboBox.setValue(professor.getUniversity());    
        changeComponentsVisibilityTrueForValidation();
    }    
    
    public Professor getProfessor() {
        return professor;
    }
    
    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage (exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR -> MainApp.changeView("/mx/fei/coilvicapp/gui/views/MainApp");
                case FATAL -> MainApp.changeView("/main/MainApp");
                
            }
        } catch (IOException ioException) {
            Log.getLogger(ProfessorValidateController.class).error(exception.getMessage(), exception);
        }
    }
    
    private void handleValidationException(IllegalArgumentException ex) {
        DialogController.getDialog(new AlertMessage( ex.getMessage(), Status.WARNING));
    }    
    
}
