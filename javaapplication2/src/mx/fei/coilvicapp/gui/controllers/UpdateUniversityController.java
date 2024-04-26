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
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
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

    private final ICountry COUNTRY_DAO = new CountryDAO();
    private final IUniversity UNIVERSITY_DAO = new UniversityDAO();
    @FXML
    private University university;

    public void setUniversity(University newUniversity) {
        this.university = newUniversity.copy();

        initializeCombobox();
        nameTextField.setText(newUniversity.getName());
        acronymTextField.setText(newUniversity.getAcronym());
        jurisdictionTextField.setText(newUniversity.getJurisdiction());
        cityTextField.setText(newUniversity.getCity());
        countryTextField.setText(newUniversity.getCountry().toString());
    }
    
    private void initializeUniversity(University newUniversity) {
        university.setIdUniversity(newUniversity.getIdUniversity());
        university.setName(newUniversity.getName());
        university.setAcronym(newUniversity.getAcronym());
        university.setJurisdiction(newUniversity.getJurisdiction());
        university.setCity(newUniversity.getCity());
        university.setCountry(newUniversity.getCountry());
    }
    
    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
        
        
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

    }

    @FXML
    private void cancelButtonIsPressed(ActionEvent event) throws IOException {

    }

    @FXML
    private void updateButtonIsPressed(ActionEvent event) throws IOException {
        initializeCombobox();
        nameTextField.setDisable(false);
        acronymTextField.setDisable(false);
        jurisdictionTextField.setDisable(false);
        cityTextField.setDisable(false);
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

    private void invokeDeleteUniversity(University university) throws DAOException {
        int result;
        result = UNIVERSITY_DAO.deleteUniversity(university.getIdUniversity());
        if (result > 0) {
            wasDeletedConfirmation();
        }
    }

    private boolean deleteConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar eliminacion", "¿Deseas salir eliminar la universidad?");
        return (response.get() == DialogController.BUTTON_YES);
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
