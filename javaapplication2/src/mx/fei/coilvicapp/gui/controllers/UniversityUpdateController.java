package mx.fei.coilvicapp.gui.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.country.ICountry;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.university.IUniversity;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.university.University;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import log.Log;
import main.MainApp;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;

public class UniversityUpdateController implements Initializable {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField acronymTextField;

    @FXML
    private TextField jurisdictionTextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField countryTextField;

    @FXML
    private ComboBox<Country> countriesCombobox;

    @FXML
    private Button acceptButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button updateButton;

    @FXML
    private University university;

    public void setUniversity(University university) {
        this.university = university;
        initializeUniversityTextFields(university);
    }

    private void initializeUniversityTextFields(University university) {
        nameTextField.setText(university.getName());
        acronymTextField.setText(university.getAcronym());
        jurisdictionTextField.setText(university.getJurisdiction());
        cityTextField.setText(university.getCity());
        countriesCombobox.setPromptText(university.getCountry().toString());
    }

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
        initializeUniversityCombobox();
    }

    public University getUniversity() {
        return university;
    }

    private void initializeUniversityCombobox() {
        ICountry countryDAO = new CountryDAO();
        ArrayList<Country> countries = new ArrayList<>();

        try {
            countries = countryDAO.getAllCountries();
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
        countriesCombobox.setItems(FXCollections.observableArrayList(countries));
    }

    @FXML
    private void backButtonIsPressed(ActionEvent event) throws IOException {
        MainApp.changeView("/mx/fei/coilvicapp/gui/views/UniversityManager");
    }

    @FXML
    private void acceptButtonIsPressed(ActionEvent event) throws IOException {
        try {
            if (isDifferent() && updateConfirmation()) {
                invokeUpdateUniversity();
            }
            goBack();
        } catch (IllegalArgumentException exception) {
            handleValidationException(exception);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    private University initializeNewUniversity() {
        University newUniversity = new University();
        newUniversity.setIdUniversity(university.getIdUniversity());
        newUniversity.setName(nameTextField.getText());
        newUniversity.setAcronym(acronymTextField.getText());
        newUniversity.setJurisdiction(jurisdictionTextField.getText());
        newUniversity.setCity(cityTextField.getText());
        if (countriesCombobox.getValue() != null) {
            newUniversity.setCountry(countriesCombobox.getValue());
        } else {
            newUniversity.setCountry(university.getCountry());
        }
        return newUniversity;
    }

    private boolean isDifferent() {
        return !university.equals(initializeNewUniversity());
    }

    @FXML
    private void cancelButtonIsPressed(ActionEvent event) throws IOException {
        initializeUniversityTextFields(university);
        setModifyMode(false);
    }

    @FXML
    private void updateButtonIsPressed(ActionEvent event) throws IOException {
        initializeUniversityCombobox();
        setModifyMode(true);
    }

    private void setModifyMode(boolean isModifiable) {
        nameTextField.setEditable(isModifiable);
        acronymTextField.setEditable(isModifiable);
        jurisdictionTextField.setEditable(isModifiable);
        cityTextField.setEditable(isModifiable);
        acceptButton.setVisible(isModifiable);
        cancelButton.setVisible(isModifiable);
        countriesCombobox.setDisable(!isModifiable);
        updateButton.setManaged(!isModifiable);
        updateButton.setVisible(!isModifiable);
    }

    private void invokeUpdateUniversity() throws DAOException {
        IUniversity universityDAO = new UniversityDAO();
        int result;
        result = universityDAO.updateUniversity(initializeNewUniversity());
        if (result > 0) {
            wasUpdatedConfirmation();
        }
    }

    private void goBack() {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/UniversityManager");
        } catch (IOException exception) {
            Log.getLogger(UniversityUpdateController.class).error(exception.getMessage(),
                    exception);
        }
    }

    private boolean updateConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar "
                + "actualización", "¿Deseas actualizar la universidad?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean wasUpdatedConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog("Actualizada",
                "La universidad fue actualizada");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }

    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getInvalidDataDialog(exception.getMessage());
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    goBack();
                case FATAL ->
                    MainApp.handleFatal();
                default -> {
                }
            }
        } catch (IOException ioException) {
            Log.getLogger(UniversityUpdateController.class).error(ioException.getMessage(), ioException);
        }
    }
}
