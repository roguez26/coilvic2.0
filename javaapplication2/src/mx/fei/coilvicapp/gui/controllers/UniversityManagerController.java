package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.util.ArrayList;
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
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.country.ICountry;
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
        initializeUniversitiesTable();
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
        goBack();
    }

    private void goBack() {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/AssistantMainMenu");
        } catch (IOException exception) {
            Log.getLogger(UniversityManagerController.class).error(exception.getMessage(), exception);
        }
    }

    @FXML
    private void registerButton(ActionEvent event) {
        try {
            ICountry countryDAO = new CountryDAO();
            if (countryDAO.isThereAtLeastOneCountry()) {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/UniversityRegistration");
            } else {
                DialogController.getInformativeConfirmationDialog("Aviso", "No puedes registrar universidades "
                        + "si no hay paises registrados");
            }

        } catch (DAOException exception) {
            handleDAOException(exception);
        } catch (IOException exception) {
            Log.getLogger(UniversityManagerController.class).error(exception.getMessage(), exception);
        }
    }

    @FXML
    private void seeDetailsButtonIsPressed(ActionEvent event) {
        University university = (University) universitiesTableView.getSelectionModel().getSelectedItem();
        if (university != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                        "/mx/fei/coilvicapp/gui/views/UniversityUpdate.fxml"));
                MainApp.changeView(fxmlLoader);
                UniversityUpdateController updateUniversitycontroller = fxmlLoader.getController();

                updateUniversitycontroller.setUniversity(university);
            } catch (IOException exception) {
                Log.getLogger(UniversityManagerController.class).error(exception.getMessage(), exception);
            }
        }
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
            Log.getLogger(UniversityManagerController.class).error(ioException.getMessage(), ioException);
        }
    }
}
