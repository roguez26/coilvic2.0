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
import main.MainApp;
import mx.fei.coilvicapp.logic.implementations.Status;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;

/**
 *
 * @author ivanr
 */
public class UpdateUniversityController implements Initializable {

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
    private Button deleteButton;

    @FXML
    private Button updateButton;
   
    

    private final ICountry COUNTRY_DAO = new CountryDAO();
    private final IUniversity UNIVERSITY_DAO = new UniversityDAO();
    @FXML
    private University university;

    public void setUniversity(University university) {
        this.university = university;
        initializeTextFields(university);
    }

    private void initializeTextFields(University university) {
        nameTextField.setText(university.getName());
        acronymTextField.setText(university.getAcronym());
        jurisdictionTextField.setText(university.getJurisdiction());
        cityTextField.setText(university.getCity());
        countryTextField.setText(university.getCountry().toString());
    }

    private void initializeTextFields() {
        nameTextField.setText(university.getName());
        acronymTextField.setText(university.getAcronym());
        jurisdictionTextField.setText(university.getJurisdiction());
        cityTextField.setText(university.getCity());
        countryTextField.setText(university.getCountry().toString());
    }

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {

        initializeCombobox();
    }

    public University getUniversity() {
        return university;
    }

    private void initializeCombobox() {
        ArrayList<Country> countries = new ArrayList<>();

        try {
            countries = COUNTRY_DAO.getAllCountries();
        } catch (DAOException exception) {

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
                try {
                    invokeUpdateUniversity();
                } catch (DAOException exception) {
                    handleDAOException(exception);
                }
            }
        } catch (IllegalArgumentException ioException) {
            handleValidationException(ioException);
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
        initializeTextFields();
        updateButton.setVisible(true);
        nameTextField.setEditable(false);
        acronymTextField.setEditable(false);
        jurisdictionTextField.setEditable(false);
        cityTextField.setEditable(false);
        acceptButton.setVisible(false);
        countriesCombobox.setVisible(false);
        deleteButton.setVisible(true);
        cancelButton.setVisible(false);
    }

    @FXML
    private void updateButtonIsPressed(ActionEvent event) throws IOException {
        initializeCombobox();
        updateButton.setVisible(false);
        nameTextField.setEditable(true);
        acronymTextField.setEditable(true);
        jurisdictionTextField.setEditable(true);
        cityTextField.setEditable(true);
        acceptButton.setVisible(true);
        countriesCombobox.setVisible(true);
        deleteButton.setVisible(false);
        cancelButton.setVisible(true);
    }

    @FXML
    private void deleteButtonIsPressed(ActionEvent event) throws IOException {
        if (deleteConfirmation()) {
            try {
                invokeDeleteUniversity(university);
            } catch (DAOException exception) {
                handleDAOException(exception);
            }
        }

    }
    
    @FXML
    private void optionIsSelected(ActionEvent event) throws IOException {
        countryTextField.setText(countriesCombobox.getValue().toString());
    }

    private void invokeUpdateUniversity() throws DAOException, IOException {
        int result;
        result = UNIVERSITY_DAO.updateUniversity(initializeNewUniversity());
        if (result > 0) {
            wasUpdatedConfirmation();
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/UniversityManager");
        }
    }

    private void invokeDeleteUniversity(University university) throws DAOException, IOException {
        int result;
        result = UNIVERSITY_DAO.deleteUniversity(university.getIdUniversity());
        if (result > 0) {
            wasDeletedConfirmation();
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/UniversityManager");
        }
    }

    private boolean updateConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar actualizacion", "¿Deseas actualizar la universidad?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean deleteConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar eliminacion", "¿Deseas eliminar la universidad?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean wasUpdatedConfirmation() {
        Optional<ButtonType> response = DialogController.getPositiveConfirmationDialog("Actualizada", "La universidad fue actualizada");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }

    private boolean wasDeletedConfirmation() {
        Optional<ButtonType> response = DialogController.getPositiveConfirmationDialog("Eliminada", "La universidad fue eliminada");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }

    private void handleValidationException(IllegalArgumentException ex) {
        DialogController.getDialog(new AlertMessage(ex.getMessage(), Status.WARNING));
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
}
