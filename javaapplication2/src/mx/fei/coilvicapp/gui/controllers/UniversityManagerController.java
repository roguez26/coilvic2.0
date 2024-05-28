package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.university.IUniversity;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import log.Log;
import main.MainApp;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;

public class UniversityManagerController implements Initializable {

    @FXML
    private TableView universitiesTableView;

    private final IUniversity UNIVERSITY_DAO = new UniversityDAO();

    @FXML
    private TableColumn nameTableColumn;

    @FXML
    private TableColumn acronymTableColumn;

    @FXML
    private TableColumn jurisdictionTableColumn;

    @FXML
    private TableColumn cityTableColumn;

    @FXML
    private TableColumn countryTableColumn;
   

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        acronymTableColumn.setCellValueFactory(new PropertyValueFactory<>("acronym"));
        jurisdictionTableColumn.setCellValueFactory(new PropertyValueFactory<>("jurisdiction"));
        cityTableColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        countryTableColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
    }

    private void initializeUniversitiesTable() {
        ArrayList<University> universitiesList = new ArrayList<>();
        
        try {
            universitiesList = UNIVERSITY_DAO.getAllUniversities();
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
        universitiesTableView.getItems().addAll(universitiesList);
    }
    
    @FXML
    private void backButtonIsPressed(ActionEvent event) {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/AssistantMainMenu");
        } catch (IOException ioException) {
            Log.getLogger(UniversityManagerController.class).error(ioException.getMessage(), ioException);
        }
    }

    @FXML
    private void registerButton(ActionEvent event) {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/registerUniversity");
        } catch (IOException ioException) {
            Log.getLogger(UniversityManagerController.class).error(ioException.getMessage(), ioException);
        }
    }

    @FXML
    private void seeDetailsButtonIsPressed(ActionEvent event) {
        University university = (University) universitiesTableView.getSelectionModel().getSelectedItem();
        if (university != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                        "/mx/fei/coilvicapp/gui/views/UpdateUniversity.fxml"));
                MainApp.changeView(fxmlLoader);
                UpdateUniversityController updateUniversitycontroller = fxmlLoader.getController();
                
                updateUniversitycontroller.setUniversity(university);
            } catch (IOException ioException) {
                Log.getLogger(UniversityManagerController.class).error(ioException.getMessage(), ioException);
            }
        } 
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/AssistantMainMenu");
                case FATAL ->
                    MainApp.changeView("/main/MainApp");
            }
        } catch (IOException ioException) {
            Log.getLogger(UniversityManagerController.class).error(ioException.getMessage(), ioException);
        }
    }
}
