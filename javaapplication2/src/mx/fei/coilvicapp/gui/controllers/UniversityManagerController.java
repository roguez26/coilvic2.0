package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.util.ArrayList;
import java.util.Optional;
import mx.fei.coilvicapp.logic.university.IUniversity;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import main.MainApp;
import mx.fei.coilvicapp.logic.implementations.Status;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;

/**
 *
 * @author ivanr
 */
public class UniversityManagerController implements Initializable {

    @FXML
    private TableView universitiesTableView;

    private final IUniversity UNIVERSITY_DAO = new UniversityDAO();

    @FXML
    private TableColumn<University, String> nameTableColumn;

    @FXML
    private TableColumn<University, String> acronymTableColumn;

    @FXML
    private TableColumn jurisdictionTableColumn;

    @FXML
    private TableColumn cityTableColumn;

    @FXML
    private TableColumn countryTableColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<University> universitiesList = new ArrayList<>();

        try {
            universitiesList = UNIVERSITY_DAO.getAllUniversities();
        } catch (DAOException exception) {

        }
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        acronymTableColumn.setCellValueFactory(new PropertyValueFactory<>("acronym"));
        jurisdictionTableColumn.setCellValueFactory(new PropertyValueFactory<>("jurisdiction"));
        cityTableColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        countryTableColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        universitiesTableView.getItems().addAll(universitiesList);
    }

    @FXML
    private void backButton(ActionEvent event) throws IOException {

    }

    @FXML
    private void deleteButton(ActionEvent event) throws IOException {
        University selectedUniversity = (University) universitiesTableView.getSelectionModel().getSelectedItem();
        if (selectedUniversity != null) {
            if (deleteConfirmation()) {
                try {
                    invokeDeleteUniversity(selectedUniversity);
                } catch (DAOException exception) {
                    habdleDAOException(exception);
                }
                universitiesTableView.getItems().remove(selectedUniversity);
                universitiesTableView.refresh();
            }
        } else {
            Optional<ButtonType> response = DialogController.getPositiveConfirmationDialog( "Aviso", "Seleccione una universidad");
        }
    }

    private void invokeDeleteUniversity(University university) throws DAOException {
        int result = 0;
        result = UNIVERSITY_DAO.deleteUniversity(university.getIdUniversity());
        if (result > 0) {
            wasDeletedConfirmation();
        }
    }

    @FXML
    private void registerButton(ActionEvent event) throws IOException {
        MainApp.changeView("/mx/fei/coilvicapp/gui/views/UniversityRegistrationFXML", 400, 500);
    }

    private boolean wasDeletedConfirmation() {
        Optional<ButtonType> response = DialogController.getPositiveConfirmationDialog("Eliminada", "La universidad fue eliminada");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }

    private boolean deleteConfirmation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar eliminacion", "¿Deseas salir eliminar la universidad?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private void habdleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/UniversityManager", 0, 0);
                case FATAL ->
                    MainApp.changeView("/main/MainApp", 0, 0);

            }
        } catch (IOException ioException) {

        }
    }

    private void handleValidationException(IllegalArgumentException ex) {
        DialogController.getDialog(new AlertMessage(ex.getMessage(), Status.WARNING));
    }
}
