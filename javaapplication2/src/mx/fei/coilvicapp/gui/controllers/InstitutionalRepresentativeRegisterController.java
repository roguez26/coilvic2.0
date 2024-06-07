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
import mx.fei.coilvicapp.logic.institutionalrepresentative.IInstitutionalRepresentative;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.university.IUniversity;
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

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
        universitiesComboBox.setItems(FXCollections.observableArrayList(initializeUniversitiesArrayForComboBox()));
    }

    private ArrayList<University> initializeUniversitiesArrayForComboBox() {
        IUniversity universityDAO = new UniversityDAO();
        ArrayList<University> universities = new ArrayList<>();
        try {
            universities = universityDAO.getAllUniversities();
        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeRegisterController.class).error(exception.getMessage(), exception);
        }
        return universities;
    }

    @FXML
    private void cancelButtonIsPressed(ActionEvent event) {
        if (confirmCancelation()) {
            goBack();
        }
    }

    private void goBack() {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/InstitutionalRepresentativeManager");
        } catch (IOException exception) {
            Log.getLogger(InstitutionalRepresentativeRegisterController.class).error(
                    exception.getMessage(), exception);
        }
    }

    private boolean confirmCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog(
                "Confirmar cancelacion", "¿Deseas cancelar el registro?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean wasRegisteredConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog(
                "Registrado", "Se ha registrado el representante institucional con éxito");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }

    private boolean emptyFieldsConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog(
                "Campos vacios", "Asegurese de llenar todos los campos con *");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }

    @FXML
    private void acceptButtonIsPressed(ActionEvent event) throws IOException {
        try {
            invokeInstitutionalRepresentativeRegistration();
        } catch (IllegalArgumentException exception) {
            handleValidationException(exception);
        } catch (DAOException exception) {
            handleDAOException(exception);
        } catch (IOException exception) {
            Log.getLogger(InstitutionalRepresentativeRegisterController.class).error(exception.getMessage(), exception);
        }
    }

    private void invokeInstitutionalRepresentativeRegistration() throws DAOException, IOException {
        if (!fieldsAreEmpty()) {
            IInstitutionalRepresentative institutionalRepresentativeDAO = new InstitutionalRepresentativeDAO();
            InstitutionalRepresentative institutionalRepresentative = initializeInstitutionalRepresentative();
            
            if (institutionalRepresentativeDAO.registerInstitutionalRepresentative(institutionalRepresentative) > 0) {
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
        if (nameTextField.getText().isEmpty()
                || paternalSurnameTextField.getText().isEmpty()
                || emailTextField.getText().isEmpty()
                || phoneNumberTextField.getText().isEmpty()
                || universitiesComboBox.getValue() == null) {
            emptyFieldsCheck = true;
        }

        return emptyFieldsCheck;
    }

    private InstitutionalRepresentative initializeInstitutionalRepresentative() throws DAOException {
        UniversityDAO universityDAO = new UniversityDAO();
        InstitutionalRepresentative institutionalRepresentative = new InstitutionalRepresentative();
        institutionalRepresentative.setName(nameTextField.getText());
        institutionalRepresentative.setPaternalSurname(paternalSurnameTextField.getText());
        institutionalRepresentative.setMaternalSurname(maternalSurnameTextField.getText());
        institutionalRepresentative.setEmail(emailTextField.getText());
        institutionalRepresentative.setPhoneNumber(phoneNumberTextField.getText());
        institutionalRepresentative.setUniversity(universityDAO.getUniversityById(
                universitiesComboBox.getValue().getIdUniversity()));
        return institutionalRepresentative;
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    goBack();
                case FATAL ->
                    MainApp.handleFatal();
            }
        } catch (IOException ioException) {
            Log.getLogger(InstitutionalRepresentativeRegisterController.class).error(ioException.getMessage(), ioException);
        }
    }

    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getDialog(new AlertMessage(exception.getMessage(), Status.WARNING));
    }

}
