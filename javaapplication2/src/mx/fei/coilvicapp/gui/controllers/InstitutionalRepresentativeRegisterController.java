package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;

public class InstitutionalRepresentativeRegisterController implements Initializable {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField paternalSurnameTextField;

    @FXML
    private TextField maternalSurnameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private ComboBox<University> universitiesComboBox;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button acceptButton;
    
    private final InstitutionalRepresentativeDAO INSTITUTIONAL_REPRESENTATIVE_DAO = new InstitutionalRepresentativeDAO();
    private final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        universitiesComboBox.setItems(FXCollections.observableArrayList(initializeUniversitiesArrayForComboBox()));     
    }    
    
    private ArrayList<University> initializeUniversitiesArrayForComboBox() {
        ArrayList<University> universities = new ArrayList<>();
        try {
            universities = UNIVERSITY_DAO.getAllUniversities();
        } catch(DAOException exception) {
            Log.getLogger(ProfessorRegisterController.class).error(exception.getMessage(), exception);
        }
        return universities;
    }  
    
    @FXML
    private void cancelButtonIsPressed(ActionEvent event) throws IOException {
        if (confirmCancelation()) {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/InstitutionalRepresentativeManager");
        }
    }    
    
    private boolean confirmCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cancelacion", "¿Deseas cancelar el registro?");
        return (response.get() == DialogController.BUTTON_YES);
    }
    
    private boolean wasRegisteredConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog
        ("Registrado","Se ha registrado el representante institucional con éxito");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }
    
    private boolean emptyFieldsConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog
        ("Campos vacios","Asegurese de llenar todos los campos con *");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }    
    
    @FXML
    private void acceptButtonIsPressed (ActionEvent event) throws IOException {
        try {
            invokeInstitutionalRepresentativeRegistration();
        } catch (IllegalArgumentException exception) {
            handleValidationException(exception);
        } catch (DAOException exception) {
            handleDAOException(exception);
        } catch (IOException exception) {
            Log.getLogger(ProfessorRegisterController.class).error(exception.getMessage(), exception);
        } 
    }

    
    private void invokeInstitutionalRepresentativeRegistration() throws DAOException, IOException {
        if (!fieldsAreEmpty()) {
            InstitutionalRepresentative institutionalRepresentative = initializeInstitutionalRepresentative();
            int idInstitutionalRepresentative = INSTITUTIONAL_REPRESENTATIVE_DAO.registerInstitutionalRepresentative(institutionalRepresentative);
            if(idInstitutionalRepresentative > 0) {
                wasRegisteredConfirmation();
                cleanFields();
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/InstitutionalRepresentativeManager");
            }
        } else {
            emptyFieldsConfirmation();
        }
    }    
    
    private void cleanFields() {
        nameTextField.setText("");
        paternalSurnameTextField.setText("");
        maternalSurnameTextField.setText("");
        emailTextField.setText("");
        phoneNumberTextField.setText("");
        universitiesComboBox.setValue(null);             
    }

    
    private boolean fieldsAreEmpty() {
        boolean emptyFieldsCheck = false;
        if (nameTextField.getText().isEmpty() || 
                paternalSurnameTextField.getText().isEmpty() ||
                emailTextField.getText().isEmpty() ||
                phoneNumberTextField.getText().isEmpty() ||
                universitiesComboBox.getValue() == null) {
            emptyFieldsCheck = true;
        }

        return emptyFieldsCheck;
    }
    
    private InstitutionalRepresentative initializeInstitutionalRepresentative() throws DAOException{
        InstitutionalRepresentative institutionalRepresentative = new InstitutionalRepresentative();
        institutionalRepresentative.setName(nameTextField.getText());
        institutionalRepresentative.setPaternalSurname(paternalSurnameTextField.getText());
        institutionalRepresentative.setMaternalSurname(maternalSurnameTextField.getText());
        institutionalRepresentative.setEmail(emailTextField.getText());
        institutionalRepresentative.setPhoneNumber(phoneNumberTextField.getText());
        institutionalRepresentative.setUniversity(UNIVERSITY_DAO.getUniversityById(
                universitiesComboBox.getValue().getIdUniversity()));
        return institutionalRepresentative;
    }
    
    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage (exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR -> MainApp.changeView("/mx/fei/coilvicapp/gui/views/MainApp");
                case FATAL -> MainApp.changeView("/main/MainApp");
                
            }
        } catch (IOException ioException) {
            
        }
    }
    
    private void handleValidationException(IllegalArgumentException ex) {
        DialogController.getDialog(new AlertMessage( ex.getMessage(), Status.WARNING));
    }    
    
}
