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
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentative;
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
import log.Log;
import main.MainApp;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;

public class InstitutionalRepresentativeDetailsController implements Initializable {

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
    private TextField telefonoTextField;
    
    @FXML
    private ComboBox<University> universitiesComboBox;    
    
    @FXML
    private Button updateRepresentativeButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private Button saveButton;    
    
    @FXML
    private InstitutionalRepresentative institutionalRepresentative;
    
    private InstitutionalRepresentativeDAO institutionalRepresentativeDAO = new InstitutionalRepresentativeDAO();
    private UniversityDAO universityDAO = new UniversityDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    @FXML
    private void backButtonIsPressed(ActionEvent event) throws IOException {
        if (backConfirmation()) {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/InstitutionalRepresentativeManager");   
        }            
    }
    
    @FXML
    private boolean backConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog(
                "Regresar a la ventana representantes institucionales", "¿Deseas regresar a la ventana representantes institucionales?");
        return (response.get() == DialogController.BUTTON_YES);
    }    
        
    @FXML
    private void updateRepresentativeButtonIsPressed(ActionEvent event) throws IOException {
        changeComponentsEditabilityTrue();
    }
    
    private void changeComponentsEditabilityTrue() {
        updateRepresentativeButton.setVisible(false);
        deleteButton.setVisible(false);
        cancelButton.setVisible(true);
        cancelButton.setDisable(false);
        saveButton.setVisible(true);
        saveButton.setDisable(false);
        
        nombreTextField.setEditable(true);
        paternalSurnameTextField.setEditable(true);
        maternalSurnameTextField.setEditable(true);
        emailTextField.setEditable(true);
        telefonoTextField.setEditable(true);
    }
    
    @FXML
    private void cancelButtonIsPressed(ActionEvent event) throws IOException {
        if(cancelConfirmation()) {
            changeComponentsEditabilityFalse();
            initializeTextFields();
        } 
    } 
    
    private void changeComponentsEditabilityFalse() {
        updateRepresentativeButton.setVisible(true);
        deleteButton.setVisible(true);
        cancelButton.setVisible(false);
        saveButton.setVisible(false);
        
        nombreTextField.setEditable(false);
        paternalSurnameTextField.setEditable(false);
        maternalSurnameTextField.setEditable(false);
        emailTextField.setEditable(false);
        telefonoTextField.setEditable(false);
    }    
    
    private boolean cancelConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Regresar a los detalles del representante institucional", "¿Desea deshacer los cambios y regresar?");
        return (response.get() == DialogController.BUTTON_YES);        
    }
    
    @FXML
    private void deleteButtonIsPressed(ActionEvent event) throws IOException {
        if (deleteConfirmation()) {
            try {
                institutionalRepresentativeDAO.deleteInstitutionalRepresentative(institutionalRepresentative);
            } catch (DAOException exception) {
                handleDAOException(exception);
            }            
            if (wasDeleted()) {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/InstitutionalRepresentativeManager");   
            }
        }
    }
    
    private boolean deleteConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Eliminar representante institucional", "¿Desea eliminar al representante institucional?");
        return (response.get() == DialogController.BUTTON_YES);         
    }
    
    private boolean wasDeleted() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Eliminar representante institucional", "El representante institucional fue eliminado con exito");
        return (response.get() == DialogController.BUTTON_YES);         
    }    
    
    @FXML
    private void saveButtonIsPressed(ActionEvent event) throws IOException {
        int rowsAffected = -1;
        if (updateConfirmation()) {
            try {
                rowsAffected = institutionalRepresentativeDAO.updateInstitutionalRepresentative(initializeInstitutionalRepresentative());
            } catch (DAOException exception) {
                Log.getLogger(InstitutionalRepresentativeDetailsController.class).error(exception.getMessage(), exception);
            }      
        }
        if (rowsAffected > 0) {
            if (wasUpdatedConfirmation()) {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/InstitutionalRepresentativeManager");   
            } else {
                wasNotUpdatedConfirmation();
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
    
    private InstitutionalRepresentative initializeInstitutionalRepresentative() {
        InstitutionalRepresentative newInstitutionalRepresentativeInformation
                = new InstitutionalRepresentative();
        
        newInstitutionalRepresentativeInformation.setIdInstitutionalRepresentative(
                institutionalRepresentative.getIdInstitutionalRepresentative());
        newInstitutionalRepresentativeInformation.setName(nombreTextField.getText());
        newInstitutionalRepresentativeInformation.setPaternalSurname(paternalSurnameTextField.getText());
        newInstitutionalRepresentativeInformation.setMaternalSurname(maternalSurnameTextField.getText());
        newInstitutionalRepresentativeInformation.setEmail(emailTextField.getText());
        newInstitutionalRepresentativeInformation.setPhoneNumber(telefonoTextField.getText());        
        return newInstitutionalRepresentativeInformation;
    }
    
    private void initializeTextFields(InstitutionalRepresentative institutionalRepresentative) {
        nombreTextField.setText(institutionalRepresentative.getName());
        paternalSurnameTextField.setText(institutionalRepresentative.getPaternalSurname());
        maternalSurnameTextField.setText(institutionalRepresentative.getMaternalSurname());
        emailTextField.setText(institutionalRepresentative.getEmail());
        telefonoTextField.setText(institutionalRepresentative.getPhoneNumber());        
    }    
    
    private void initializeTextFields() {
        nombreTextField.setText(institutionalRepresentative.getName());
        paternalSurnameTextField.setText(institutionalRepresentative.getPaternalSurname());
        maternalSurnameTextField.setText(institutionalRepresentative.getMaternalSurname());
        emailTextField.setText(institutionalRepresentative.getEmail());
        telefonoTextField.setText(institutionalRepresentative.getPhoneNumber()); 
    }
    
    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage (exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR -> MainApp.changeView("/mx/fei/coilvicapp/gui/views/MainApp");
                case FATAL -> MainApp.changeView("/main/MainApp");
                
            }
        } catch (IOException ioException) {
            Log.getLogger(InstitutionalRepresentativeDetailsController.class).error(exception.getMessage(), ioException);
        }
    }    
        
    public void setInstitutionalRepresentativeDetailsController(InstitutionalRepresentative institutionalRepresentative) {
        this.institutionalRepresentative = institutionalRepresentative;
        initializeTextFields(institutionalRepresentative);
        universitiesComboBox.setValue(institutionalRepresentative.getUniversity());    
    }
    
    public InstitutionalRepresentative getInstitutionalRepresentativeDetailsController() {
        return institutionalRepresentative;
    }
    
}