package mx.fei.coilvicapp.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentativeDAO;
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
import main.MainApp;

public class InstitutionalRepresentativeManagerController implements Initializable {
    
    private UniversityDAO universityDAO = new UniversityDAO();
    private InstitutionalRepresentativeDAO institutionalRepresentativeDAO = new InstitutionalRepresentativeDAO();
    
    @FXML
    private Button backButton;
    
    @FXML
    private TableView professorsTableView;
    
    @FXML
    private TableColumn nameTableColumn;
    
    @FXML
    private TableColumn paternalSurnameTableColumn;
    
    @FXML
    private TableColumn maternalSurnameTableColumn;
    
    @FXML
    private TableColumn emailTableColumn;
    
    @FXML
    private TableColumn phoneNumberTableColumn;
    
    @FXML
    private TableColumn universityTableColumn;
    
    @FXML
    private Button seeDetailsButton;
    
    @FXML
    private Button registerButton;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        paternalSurnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("paternalSurname"));
        maternalSurnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("maternalSurname"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumberTableColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        universityTableColumn.setCellValueFactory(new PropertyValueFactory<>("university"));
        professorsTableView.getItems().addAll(initializeInstitutionalRepresentativeArray());
    }

    @FXML
    private void backButtonIsPressed(ActionEvent event) throws IOException {
        if (backConfirmation()) {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/main");   
        }
    }
    
    @FXML
    private boolean backConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Regresar al menu principal", "¿Deseas regresar al menu principal?");
        return (response.get() == DialogController.BUTTON_YES);
    }
       
    @FXML
    private void seeDetailsButtonIsPressed (ActionEvent event) throws IOException {
        InstitutionalRepresentative institutionalRepresentative = (InstitutionalRepresentative) professorsTableView.getSelectionModel().getSelectedItem();
        if (institutionalRepresentative != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/InstitutionalRepresentativeDetails.fxml"));
            MainApp.changeView(fxmlLoader);
            InstitutionalRepresentativeDetailsController institutionalRepresentativeDetailsController = fxmlLoader.getController();
            institutionalRepresentativeDetailsController.setInstitutionalRepresentativeDetailsController(institutionalRepresentative);
        } else {
            
        }
    }

    @FXML
    private void registerButtonIsPressed(ActionEvent event) throws IOException {
        MainApp.changeView("/mx/fei/coilvicapp/gui/views/InstitutionalRepresentativeRegister");
    }    
    
    private ArrayList<InstitutionalRepresentative> initializeInstitutionalRepresentativeArray() {
        ArrayList<InstitutionalRepresentative> institutionalRepresentatives = new ArrayList<>();
        try {
            institutionalRepresentatives = institutionalRepresentativeDAO.getAllInstitutionalRepresentatives();
        } catch (DAOException exception) {
            Log.getLogger(InstitutionalRepresentativeManagerController.class).error(exception.getMessage(), exception);
        }
        return institutionalRepresentatives;
    }
    
}