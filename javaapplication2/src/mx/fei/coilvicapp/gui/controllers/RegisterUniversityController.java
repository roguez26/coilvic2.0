package mx.fei.coilvicapp.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import mx.fei.coilvicapp.logic.university.IUniversity;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.country.ICountry;
import mx.fei.coilvicapp.logic.country.Country;
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.io.IOException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import main.MainApp;
import log.Log;

/**
 * FXML Controller class
 *
 * @author ivanr
 */
public class RegisterUniversityController implements Initializable {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField acronymTextField;

    @FXML
    private TextField jurisdictionTextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private ComboBox<Country> countryCombobox;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
        ICountry countryDAO = new CountryDAO();
        ArrayList<Country> countries = new ArrayList<>();
        try {
            countries = countryDAO.getAllCountries();
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
        countryCombobox.setItems(FXCollections.observableArrayList(countries));
    }

    @FXML
    private void cancelButtonIsPressed(ActionEvent event) {
        if (confirmCancelation()) {
            goBack();
        }
    }
    
    private void goBack() {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/UniversityManager");
        } catch (IOException exception) {
            Log.getLogger(RegisterUniversityController.class).error(exception.getMessage(), 
                    exception);
        }
    }

    private boolean confirmCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cancelación", 
                "¿Deseas cancelar el registro?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean confirmRegistration() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar registro", 
                "¿Deseas añadir esta nueva universidad?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    @FXML
    private void acceptButtonIsPressed(ActionEvent event) throws IOException {
        try {
            invokeUniversityRegistration();
        } catch (IllegalArgumentException exception) {
            handleValidationException(exception);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    private void invokeUniversityRegistration() throws DAOException {
        University university = initializeUniversity();
        if (confirmRegistration()) {
            IUniversity UniversityDAO = new UniversityDAO();
            if (UniversityDAO.registerUniversity(university) > 0) {
                DialogController.getInformativeConfirmationDialog("Registrada", "La universidad fue registrada"
                        + " con éxito");
                cleanFields();
            }
        }
    }

    private void cleanFields() {
        nameTextField.setText("");
        acronymTextField.setText("");
        jurisdictionTextField.setText("");
        cityTextField.setText("");
        countryCombobox.setValue(null);
    }

    private University initializeUniversity() {
        University university = new University();
        university.setName(nameTextField.getText());
        university.setAcronym(acronymTextField.getText());
        university.setJurisdiction(jurisdictionTextField.getText());
        university.setCity(cityTextField.getText());
        university.setCountry(countryCombobox.getValue());
        return university;
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
            Log.getLogger(RegisterUniversityController.class).error(ioException.getMessage(), ioException);
        }
    }

    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getInvalidDataDialog(exception.getMessage());
    }

}
