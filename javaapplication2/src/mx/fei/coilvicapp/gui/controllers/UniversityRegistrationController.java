package mx.fei.coilvicapp.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import mx.fei.coilvicapp.logic.university.IUniversity;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.country.ICountry;
import mx.fei.coilvicapp.logic.country.Country;
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.io.IOException;
//import system.objects.AlertMessage;
//import system.objects.Status;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

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
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<Country> countries = new ArrayList<>();
        System.out.println("hola");
        try {
            countries = COUNTRY_DAO.getAllCountries();
        } catch (DAOException exception) {
            
        }
        System.out.println(countries);
        countryCombobox.setItems(FXCollections.observableArrayList(countries));   
    }
    
    @FXML
    private void cancel(ActionEvent event) throws IOException {
        System.out.println("Cerrar");
    }
    
    @FXML
    private void accept (ActionEvent event) throws IOException {
        try {
            invokeUniversityRegistration(initializeUniversity());
        } catch (DAOException exception) {
            System.out.println("No se pudo registrar");
        }
    }
    
    private void invokeUniversityRegistration(University university) throws DAOException {
        int idUniversity = UNIVERSITY_DAO.registerUniversity(university);
        if(idUniversity > 0) {
            System.out.println("Se registro");
        } else {
            System.out.println("No se registro");
        }
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

}
