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
import main.MainApp;
import mx.fei.coilvicapp.logic.course.Course;
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
        MainApp.changeView("/mx/fei/coilvicapp/gui/views/main");
    }

    @FXML
    private void registerButton(ActionEvent event) throws IOException {
        MainApp.changeView("/mx/fei/coilvicapp/gui/views/registerUniversity");
    }

    @FXML
    private void seeDetailsButtonIsPressed(ActionEvent event) throws IOException {
        University university = (University) universitiesTableView.getSelectionModel().getSelectedItem();
        if (university != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/UpdateUniversity.fxml"));
            MainApp.changeView(fxmlLoader);
            UpdateUniversityController updateUniversitycontroller = fxmlLoader.getController();
            
            updateUniversitycontroller.setUniversity(university);
        } 
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
