package mx.fei.coilvicapp.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import mx.fei.coilvicapp.logic.professor.Professor;
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
import log.Log;
import mx.fei.coilvicapp.logic.implementations.XLSXCreator;
import main.MainApp;

public class ProfessorRegisterController implements Initializable {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField paternalSurnameTextField;

    @FXML
    private TextField maternalSurnameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private ComboBox<University> universitiesComboBox;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button acceptButton;

    private final ProfessorDAO PROFESSOR_DAO = new ProfessorDAO();
    private final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Inicializando");
        genderComboBox.setItems(FXCollections.observableArrayList(initializeGendersArrayForComboBox()));
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
    
    private ArrayList<String> initializeGendersArrayForComboBox() {
        ArrayList<String> genders = new ArrayList<>();
        genders.add("Hombre");
        genders.add("Mujer");
        genders.add("Otro");
        return genders;
    }    
    
    @FXML
    private void cancelButtonIsPressed(ActionEvent event) throws IOException {
        if (confirmCancelation()) {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/LoginParticipant");
        }
    }
   
    private boolean confirmCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cancelacion", "¿Deseas cancelar el registro?");
        return (response.get() == DialogController.BUTTON_YES);
    }
    
    @FXML
    private void acceptButtonIsPressed (ActionEvent event) throws IOException {
        try {
            invokeProfessorRegistration();
        } catch (IllegalArgumentException exception) {
            handleValidationException(exception);
        } catch (DAOException exception) {
            handleDAOException(exception);
        } catch (IOException exception) {
            Log.getLogger(ProfessorRegisterController.class).error(exception.getMessage(), exception);
        } 
    }

    
    private void invokeProfessorRegistration() throws DAOException, IOException {
        XLSXCreator xslxCreator = new XLSXCreator();
        if (!fieldsAreEmpty()) {
            Professor professor = initializeProfessor();
            int idProfessor = PROFESSOR_DAO.registerProfessor(professor);
            xslxCreator.addProfessorIntoXLSX(professor);
            if(idProfessor > 0) {
                DialogController.getInformativeConfirmationDialog
                    ("Registrado","Se ha registrado su información con éxito, se le notificara cuando se haya validado su información");
                cleanFields();
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/LoginParticipant");
            }
        } else {
            DialogController.getInformativeConfirmationDialog(
                    "Campos vacios","Asegurese de llenar todos los campos con *");
        }
    }
    
    private void cleanFields() {
        nameTextField.setText("");
        paternalSurnameTextField.setText("");
        maternalSurnameTextField.setText("");
        emailTextField.setText("");
        genderComboBox.setValue("");
        phoneNumberTextField.setText("");
        universitiesComboBox.setValue(null);             
    }

    
    private boolean fieldsAreEmpty() {
        boolean emptyFieldsCheck = false;
        if (nameTextField.getText().isEmpty() || 
                paternalSurnameTextField.getText().isEmpty() ||
                emailTextField.getText().isEmpty() ||
                genderComboBox.getValue() == null ||
                phoneNumberTextField.getText().isEmpty() ||
                universitiesComboBox.getValue() == null) {
            emptyFieldsCheck = true;
        }

        return emptyFieldsCheck;
    }
    
    private Professor initializeProfessor() throws DAOException{
        Professor professor = new Professor();
        professor.setName(nameTextField.getText());
        professor.setPaternalSurname(paternalSurnameTextField.getText());
        professor.setMaternalSurname(maternalSurnameTextField.getText());
        professor.setEmail(emailTextField.getText());
        professor.setGender(genderComboBox.getValue());
        professor.setPhoneNumber(phoneNumberTextField.getText());
        professor.setUniversity(UNIVERSITY_DAO.getUniversityById(
                universitiesComboBox.getValue().getIdUniversity()));
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
            
        }
    }
    
    private void handleValidationException(IllegalArgumentException ex) {
        DialogController.getDialog(new AlertMessage( ex.getMessage(), Status.WARNING));
    }

}
