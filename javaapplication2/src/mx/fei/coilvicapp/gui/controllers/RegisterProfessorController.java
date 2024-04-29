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

public class RegisterProfessorController implements Initializable {

    private UniversityDAO universityDAO;
    private ProfessorDAO professorDAO;

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
    
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        if (event.getSource() == cancelButton) {
            if (confirmCancelation()) {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/ProfessorManager");
            }
        }
    }
   
    private boolean confirmCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cancelacion", "¿Deseas cancelar el registro?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private ArrayList<University> initializeUniversitiesArrayForComboBox() {
        ArrayList<University> universities = new ArrayList<>();
        try {
            universities = UNIVERSITY_DAO.getAllUniversities();
        } catch(DAOException exception) {
            Logger.getLogger(RegisterProfessorController.class.getName()).log(Level.SEVERE, null, exception);
        }
        return universities;
    }
    
    private boolean wasRegisteredConfirmation() {
        Optional<ButtonType> response = DialogController.getPositiveConfirmationDialog("Registrado","El profesor se ha registrado con exito");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }
    
    private boolean emptyFieldsConfirmation() {
        //Optional<ButtonType> response = DialogController.getEmptyFieldsConfirmationDialog("Campos Vacios", "Llene los campos para hacer un registro");
       // return response.get() == DialogController.BUTTON_ACCEPT;
       return false;
    }
    
    @FXML
    private void accept(ActionEvent event) throws IOException {
        if (event.getSource() == acceptButton) {
            try {
                invokeProfessorRegistration();
            } catch (IllegalArgumentException ioException) {
                handleValidationException(ioException);
            } catch (DAOException daoException) {
                handleDAOException(daoException);
            }
        }
    }

    
    private void invokeProfessorRegistration() throws DAOException {
        if (!fieldsAreEmpty()) {
            System.out.println("campos no vacios");
            int idProfessor = PROFESSOR_DAO.registerProfessor(initializeProfessor());
            if(idProfessor > 0) {
                wasRegisteredConfirmation();
                cleanFields();
            }
        } else {
            System.out.println("campos vacios");
            emptyFieldsConfirmation();
        }
    }
    
    private void cleanFields() {
        nameTextField.setText("");
        paternalSurnameTextField.setText("");
        maternalSurnameTextField.setText("");
        emailTextField.setText("");
        
    }
    
    private ArrayList<String> initializeGendersArrayForComboBox() {
        ArrayList<String> genders = new ArrayList<>();
        genders.add("Hombre");
        genders.add("Mujer");
        genders.add("Otro");
        return genders;
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

    
    private Professor initializeProfessor() {
        Professor professor = new Professor();
        professor.setName(nameTextField.getText());
        professor.setPaternalSurname(paternalSurnameTextField.getText());
        professor.setMaternalSurname(maternalSurnameTextField.getText());
        professor.setEmail(emailTextField.getText());
        professor.setGender(genderComboBox.getValue());
        professor.setPhoneNumber(phoneNumberTextField.getText());
        professor.setIdUniversity(universitiesComboBox.getValue().getIdUniversity());
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
