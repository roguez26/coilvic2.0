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
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.io.IOException;
import javafx.event.ActionEvent;
import log.Log;
import main.MainApp;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentativeDAO;

public class InstitutionalRepresentativeManagerController implements Initializable {
    
    private UniversityDAO universityDAO = new UniversityDAO();
    private InstitutionalRepresentativeDAO institutionalRepresentativeDAO = 
            new InstitutionalRepresentativeDAO();
    
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
    private void backButtonIsPressed(ActionEvent event) {
        try {   
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/AssistantMainMenu");
        } catch (IOException exception) {
            Log.getLogger(InstitutionalRepresentativeManagerController.class).error(exception.getMessage(),
                    exception);
        }
    }
           
    @FXML
    private void seeDetailsButtonIsPressed (ActionEvent event) {
        InstitutionalRepresentative institutionalRepresentative = 
                (InstitutionalRepresentative) professorsTableView.getSelectionModel().getSelectedItem();
        if (institutionalRepresentative != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                        "/mx/fei/coilvicapp/gui/views/InstitutionalRepresentativeDetails.fxml"));
                MainApp.changeView(fxmlLoader);
                InstitutionalRepresentativeDetailsController
                        institutionalRepresentativeDetailsController = fxmlLoader.getController();
                institutionalRepresentativeDetailsController.setInstitutionalRepresentativeDetailsController(
                        institutionalRepresentative);
            } catch (IOException exception) {
                Log.getLogger(InstitutionalRepresentativeManagerController.class).error(exception.getMessage(),
                        exception);
            }
        }
    }

    @FXML
    private void registerButtonIsPressed(ActionEvent event) {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/InstitutionalRepresentativeRegister");
        } catch (IOException exception) {
            Log.getLogger(InstitutionalRepresentativeManagerController.class).error(exception.getMessage(), 
                    exception);
        }
    }    
    
    private ArrayList<InstitutionalRepresentative> initializeInstitutionalRepresentativeArray() {
        ArrayList<InstitutionalRepresentative> institutionalRepresentatives = new ArrayList<>();
        try {
            institutionalRepresentatives = institutionalRepresentativeDAO.getAllInstitutionalRepresentatives();
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
        return institutionalRepresentatives;
    }
    
    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/AssistantMainMenu");
                case FATAL ->
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/LoginParticipant");
            }
        } catch (IOException ioException) {
            Log.getLogger(FeedbackFormsController.class).error(exception.getMessage(), exception);
        }
    }    
    
}