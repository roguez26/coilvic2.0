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

/**
 * FXML Controller class
 *
 * @author ivanr
 */
public class UniversityRegistrationController implements Initializable {

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
    
    private final ICountry COUNTRY_DAO = new CountryDAO();
    private final IUniversity UNIVERSITY_DAO = new UniversityDAO();

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
        ArrayList<Country> countries = new ArrayList<>();
        try {
            countries = COUNTRY_DAO.getAllCountries();
        } catch (DAOException exception) {
            
        }
        countryCombobox.setItems(FXCollections.observableArrayList(countries));   
    }
    
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        if (confirmedCancelation()) {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/UniversityManager", 600, 500);
        }
    }
    
    private boolean confirmedCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cancelacion", "¿Deseas cancelar el registro?");
        return (response.get() == DialogController.BUTTON_YES);
    }
    
    private boolean wasRegisteredConfirmation() {
        Optional<ButtonType> response = DialogController.getPositiveConfirmationDialog("Registrada","La universidad se registro con exito");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }
    
    @FXML
    private void accept (ActionEvent event) throws IOException {
        try {
            invokeUniversityRegistration(initializeUniversity());
        } catch (IllegalArgumentException ioException) {
            handleValidationException(ioException);
        } catch (DAOException daoException) {
            habdleDAOException(daoException);
        }
    }
    
    private void invokeUniversityRegistration(University university) throws DAOException {
        int idUniversity = UNIVERSITY_DAO.registerUniversity(university);
        if(idUniversity > 0) {
            wasRegisteredConfirmation();
            cleanFields();
        }
    }
    
    private void cleanFields() {
        nameTextField.setText("");
        acronymTextField.setText("");
        jurisdictionTextField.setText("");
        cityTextField.setText("");
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
    
    private void habdleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage (exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR -> MainApp.changeView("/mx/fei/coilvicapp/gui/views/UniversityManager", 0, 0);
                case FATAL -> MainApp.changeView("/main/MainApp", 0, 0);
                
            }
        } catch (IOException ioException) {
            
        }
    }
    
    private void handleValidationException(IllegalArgumentException ex) {
        DialogController.getDialog(new AlertMessage( ex.getMessage(), Status.WARNING));
    }

}
