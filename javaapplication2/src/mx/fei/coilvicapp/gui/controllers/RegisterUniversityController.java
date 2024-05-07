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
import mx.fei.coilvicapp.logic.implementations.Status;
import java.io.IOException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import main.MainApp;
import javafx.scene.layout.VBox;

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
    private VBox backgroundVBox;

    @FXML
    private ComboBox<Country> countryCombobox;

    private final ICountry CountryDAO = new CountryDAO();
    private final IUniversity UniversityDAO = new UniversityDAO();

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
        ArrayList<Country> countries = new ArrayList<>();
        try {
            countries = CountryDAO.getAllCountries();
        } catch (DAOException exception) {

        }
        countryCombobox.setItems(FXCollections.observableArrayList(countries));
    }

    @FXML
    private void cancelButtonIsPressed(ActionEvent event) throws IOException {
        if (textFieldsAreCleaned() || confirmedCancelation()) {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/UniversityManager");
        }
    }

    private boolean textFieldsAreCleaned() {
        return nameTextField.getText().equals("") && acronymTextField.getText().equals("") && jurisdictionTextField.getText().equals("") && cityTextField.getText().equals("");
    }

    private boolean confirmedCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cancelacion", "¿Deseas cancelar el registro?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean confirmRegistration() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar registro", "¿Deseas añadir esta nueva universidad?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean wasRegisteredConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog("Registrada", "La universidad fue registrada con exito");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }

    @FXML
    private void acceptButtonIsPressed(ActionEvent event) throws IOException {
        try {
            invokeUniversityRegistration();
        } catch (IllegalArgumentException iaException) {
            handleValidationException(iaException);
        } catch (DAOException daoException) {
            handleDAOException(daoException);
        }
    }

    private void invokeUniversityRegistration() throws DAOException {
        University university = initializeUniversity();
        if (confirmRegistration()) {
            if (UniversityDAO.registerUniversity(university) > 0) {
                wasRegisteredConfirmation();
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
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/UniversityManager");
                case FATAL ->
                    MainApp.changeView("/main/MainApp");
            }
        } catch (IOException ioException) {

        }
    }

    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getInvalidDataDialog(exception.getMessage());
    }

}
