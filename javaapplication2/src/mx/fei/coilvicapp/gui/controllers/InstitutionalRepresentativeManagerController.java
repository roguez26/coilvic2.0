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
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.io.IOException;
import javafx.event.ActionEvent;
import log.Log;
import main.MainApp;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;
import mx.fei.coilvicapp.logic.institutionalrepresentative.IInstitutionalRepresentative;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.institutionalrepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.university.IUniversity;
import mx.fei.coilvicapp.logic.university.UniversityDAO;

public class InstitutionalRepresentativeManagerController implements Initializable {

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
    public void initialize(URL URL, ResourceBundle resourceBundle) {
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
        goBack();
    }

    private void goBack() {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/AssistantMainMenu");
        } catch (IOException exception) {
            Log.getLogger(InstitutionalRepresentativeManagerController.class).error(
                    exception.getMessage(), exception);
        }
    }

    @FXML
    private void seeDetailsButtonIsPressed(ActionEvent event) {
        InstitutionalRepresentative institutionalRepresentative
                = (InstitutionalRepresentative) professorsTableView.getSelectionModel().getSelectedItem();
        if (institutionalRepresentative != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                        "/mx/fei/coilvicapp/gui/views/InstitutionalRepresentativeDetails.fxml"));
                MainApp.changeView(fxmlLoader);
                InstitutionalRepresentativeDetailsController institutionalRepresentativeDetailsController
                        = fxmlLoader.getController();
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
            IUniversity universityDAO = new UniversityDAO();
            if (universityDAO.isThereAtLeastOneUniversity()) {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/InstitutionalRepresentativeRegister");
            } else {
                DialogController.getInformativeConfirmationDialog("Aviso", "No puedes registrar representantes "
                        + "institucionales si no hay universidades registradas");
            }
        } catch (DAOException exception) {
            handleDAOException(exception);
        } catch (IOException exception) {
            Log.getLogger(InstitutionalRepresentativeManagerController.class).error(exception.getMessage(),
                    exception);
        }
    }

    private ArrayList<InstitutionalRepresentative> initializeInstitutionalRepresentativeArray() {
        IInstitutionalRepresentative institutionalRepresentativeDAO
                = new InstitutionalRepresentativeDAO();
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
                    goBack();
                case FATAL ->
                    MainApp.handleFatal();
                default -> {
                }
            }
        } catch (IOException ioException) {
            Log.getLogger(InstitutionalRepresentativeManagerController.class).error(ioException.getMessage(),
                    ioException);
        }
    }

}
