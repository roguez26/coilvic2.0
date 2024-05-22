package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.MainApp;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import mx.fei.coilvicapp.logic.collaborativeproject.ICollaborativeProject;
import mx.fei.coilvicapp.logic.implementations.DAOException;

/**
 *
 * @author ivanr
 */
public class CollaborativeProjectsManagementController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button seeDetailsButton;

    @FXML
    private MenuButton statusMenuButton;

    @FXML
    private TableView<CollaborativeProject> collaborativeProjecsTableView;

    @FXML
    private TableColumn courseOneTableColumn;

    @FXML
    private TableColumn courseTwoTableColumn;

    @FXML
    private TableColumn nameTableColumn;

    @FXML
    private TableColumn<CollaborativeProject, String> universityOneTableColumn;

    @FXML
    private TableColumn<CollaborativeProject, String> universityTwoTableColumn;

    private final ICollaborativeProject COLLABORATIVE_PROJECT_DAO = new CollaborativeProjectDAO();
    private ArrayList<CollaborativeProject> collaborativeProjectsList = new ArrayList<>();
    @FXML
    private Button validateButton;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {

        try {
            collaborativeProjectsList = COLLABORATIVE_PROJECT_DAO.getCollaborativeProjectsProposals();
        } catch (DAOException exception) {

        }
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        courseOneTableColumn.setCellValueFactory(new PropertyValueFactory<>("requesterCourse"));
        courseTwoTableColumn.setCellValueFactory(new PropertyValueFactory<>("requestedCourse"));
        universityOneTableColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getRequesterCourse().getProfessor().getUniversity().getName()));

        universityTwoTableColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getRequestedCourse().getProfessor().getUniversity().getName()));
        collaborativeProjecsTableView.getItems().addAll(collaborativeProjectsList);
    }

    @FXML
    void seeDetailsButton(ActionEvent event) {
        if(collaborativeProjecsTableView.getSelectionModel().getSelectedItem() != null) {
            
        } else {
            DialogController.getInformativeConfirmationDialog("Sin proyecto seleccionado", "Necesita seleccionar un proyecto para poder ver sus detalles");
        }

    }

    @FXML
    void pendingMenuButtonIsSelected(ActionEvent event) {
        setValidateMode(true);
        try {
            collaborativeProjectsList = COLLABORATIVE_PROJECT_DAO.getCollaborativeProjectsProposals();
        } catch (DAOException exception) {

        }
        collaborativeProjecsTableView.getItems().clear();
        collaborativeProjecsTableView.getItems().addAll(collaborativeProjectsList);

    }

    @FXML
    void acceptedMenuButtonIsSelected(ActionEvent event) {
        setValidateMode(false);

    }

    @FXML
    void rejectedMenuButtonIsSelected(ActionEvent event) {
        setValidateMode(false);
    }

    @FXML
    void finishedMenuButtonIsSelected(ActionEvent event) {
        setValidateMode(false);
    }

    private void setValidateMode(boolean isVisible) {
        validateButton.setVisible(isVisible);
        validateButton.setManaged(isVisible);
        seeDetailsButton.setVisible(!isVisible);
    }

    @FXML
    void validateButtonIsPressed(ActionEvent event) throws IOException {
        CollaborativeProject selectedCollaborativeProject = collaborativeProjecsTableView.getSelectionModel().getSelectedItem();
        if (selectedCollaborativeProject != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/ValidateCollaborativeProject.fxml"));

            MainApp.changeView(fxmlLoader);
            ValidateCollaborativeProjectController collaborativeProjectDetailsStudentController = fxmlLoader.getController();
            collaborativeProjectDetailsStudentController.setCollaborativeProject(selectedCollaborativeProject);
        } else {
            DialogController.getInformativeConfirmationDialog("Sin proyecto seleccionado", "Necesita seleccionar un proyecto para poder inciar la validacion");
        }
    }

    @FXML
    void backButtonIsPressed(ActionEvent event) {

    }

    @FXML
    void searchButtonIsPressed(ActionEvent event) {

    }

    private void goBack() {

    }

}
