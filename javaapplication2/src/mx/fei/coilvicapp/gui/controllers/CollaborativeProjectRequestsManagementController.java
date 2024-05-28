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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.collaborativeprojectrequest.*;
import mx.fei.coilvicapp.logic.course.*;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;
import mx.fei.coilvicapp.logic.professor.Professor;

/**
 * FXML Controller class
 *
 * @author edgar
 */
public class CollaborativeProjectRequestsManagementController implements Initializable {

    @FXML
    private Button backButton; 
        
    @FXML
    private MenuButton requestsMenuButton;
    
    @FXML
    private MenuButton statusMenuButton;
    
    @FXML
    private MenuItem receivedMenuItem;    
    
    @FXML
    private MenuItem cancelledMenuItem;
    
    @FXML
    private Button seeAllButton;
    
    @FXML
    private TableView<CollaborativeProjectRequest> requestsTableView;
    
    @FXML
    private TableColumn<CollaborativeProjectRequest, String> courseTableColumn;

    @FXML
    private TableColumn<CollaborativeProjectRequest, String> professorTableColumn;

    @FXML
    private TableColumn<CollaborativeProjectRequest, String> requestDateTableColumn;
        
    @FXML
    private TableColumn<CollaborativeProjectRequest, String> validationDateTableColumn;    

    @FXML
    private TableColumn<CollaborativeProjectRequest, String> statusTableColumn;
    
    @FXML
    private Button seeDetailsButton;
    
    private final CollaborativeProjectRequestDAO COLLABORATIVE_PROJECT_REQUEST_DAO = new CollaborativeProjectRequestDAO();
    
    private Professor professor;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
    }
    
    public Professor getProfessor() {
        return professor;
    }
    
    public void setProfessor(Professor professor) {
        if (professor != null) {
            this.professor = professor;
            initializeAll();
        }
    }
    
    @FXML
    private void backButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == backButton) {
            if (professor != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/ProfessorMainMenu.fxml"));
                MainApp.changeView(fxmlLoader);
                ProfessorMainMenuController professorMainMenuController = fxmlLoader.getController();
                professorMainMenuController.setProfessor(professor);                
            }            
        }
    }
    
    @FXML
    void receivedMenuButtonIsSelected(ActionEvent event) {
        requestsMenuButton.setText(((MenuItem) event.getSource()).getText());
        statusMenuButton.setVisible(true);
        cancelledMenuItem.setVisible(false);
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests = new ArrayList<>();
        
        try {
            switch (statusMenuButton.getText()) {                                    
                case "Todo" -> 
                    collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getReceivedCollaborativeProjectRequests(professor.getIdProfessor());
                case "Pendiente" -> 
                    collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getPendingReceivedCollaborativeProjectRequests(professor.getIdProfessor());
                case "Aceptado" -> 
                    collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getAcceptedReceivedCollaborativeProjectRequests(professor.getIdProfessor());
                case "Rechazado" -> 
                    collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getRejectedReceivedCollaborativeProjectRequests(professor.getIdProfessor());
                default ->  
                    collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getReceivedCollaborativeProjectRequests(professor.getIdProfessor());                
            }
                                   
            updateTableView(collaborativeProjectRequests);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }
    
    @FXML
    void sentMenuButtonIsSelected(ActionEvent event) {
        requestsMenuButton.setText(((MenuItem) event.getSource()).getText());
        statusMenuButton.setVisible(true);
        cancelledMenuItem.setVisible(true);
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests = new ArrayList<>();
        
        try {
            switch (statusMenuButton.getText()) {
                case "Todo" -> 
                    collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getSentCollaborativeProjectRequests(professor.getIdProfessor());
                case "Pendiente" -> 
                    collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getPendingSentCollaborativeProjectRequests(professor.getIdProfessor());
                case "Aceptado" -> 
                    collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getAcceptedSentCollaborativeProjectRequests(professor.getIdProfessor());
                case "Rechazado" -> 
                    collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getRejectedSentCollaborativeProjectRequests(professor.getIdProfessor());
                case "Cancelado" -> 
                    collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getCancelledSentCollaborativeProjectRequests(professor.getIdProfessor());
                default -> 
                    collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getSentCollaborativeProjectRequests(professor.getIdProfessor());                
            }
                                   
            updateTableView(collaborativeProjectRequests);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }
    
    @FXML
    void allMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        receivedMenuItem.setVisible(true);
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests = new ArrayList<>();
        
        try {
            if (requestsMenuButton.getText().equals("Recibidas")) {
                collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getReceivedCollaborativeProjectRequests(professor.getIdProfessor());
            } else if (requestsMenuButton.getText().equals("Enviadas")) {
                collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getSentCollaborativeProjectRequests(professor.getIdProfessor());
            }                        
            updateTableView(collaborativeProjectRequests);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }
    
    @FXML
    void pendingMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        receivedMenuItem.setVisible(true);
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests = new ArrayList<>();
        
        try {
            if (requestsMenuButton.getText().equals("Recibidas")) {
                collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getPendingReceivedCollaborativeProjectRequests(professor.getIdProfessor());
            } else if (requestsMenuButton.getText().equals("Enviadas")) {
                collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getPendingSentCollaborativeProjectRequests(professor.getIdProfessor());
            }                        
            updateTableView(collaborativeProjectRequests);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }
    
    @FXML
    void acceptedMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        receivedMenuItem.setVisible(true);
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests = new ArrayList<>();
        
        try {
            if (requestsMenuButton.getText().equals("Recibidas")) {
                collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getAcceptedReceivedCollaborativeProjectRequests(professor.getIdProfessor());
            } else if (requestsMenuButton.getText().equals("Enviadas")) {
                collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getAcceptedSentCollaborativeProjectRequests(professor.getIdProfessor());
            }                        
            updateTableView(collaborativeProjectRequests);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }
        
    @FXML
    void rejectedMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());   
        receivedMenuItem.setVisible(true);
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests = new ArrayList<>();
        
        try {
            if (requestsMenuButton.getText().equals("Recibidas")) {
                collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getRejectedReceivedCollaborativeProjectRequests(professor.getIdProfessor());
            } else if (requestsMenuButton.getText().equals("Enviadas")) {
                collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getRejectedSentCollaborativeProjectRequests(professor.getIdProfessor());
            }                        
            updateTableView(collaborativeProjectRequests);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }
    
    @FXML
    void cancelledMenuButtonIsSelected(ActionEvent event) {
        statusMenuButton.setText(((MenuItem) event.getSource()).getText());
        receivedMenuItem.setVisible(false);
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests = new ArrayList<>();
        
        try {            
            collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getCancelledSentCollaborativeProjectRequests(professor.getIdProfessor());                                  
            updateTableView(collaborativeProjectRequests);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }
                        
    @FXML
    void seeAllButtonIsPressed(ActionEvent event) {        
        if (event.getSource() == seeAllButton) {
            requestsMenuButton.setText("Solicitudes");
            statusMenuButton.setText("Estado");
            statusMenuButton.setVisible(false);
            receivedMenuItem.setVisible(true);
            ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests = new ArrayList<>();
            collaborativeProjectRequests = initializeCollaborativeProjectRequestsArray();
            updateTableView(collaborativeProjectRequests);
        }
    }
            
    @FXML
    private void seeDetailsButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == seeDetailsButton) {
            CollaborativeProjectRequest collaborativeProjectRequest = 
            (CollaborativeProjectRequest) requestsTableView.getSelectionModel().getSelectedItem();
            if (collaborativeProjectRequest != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/CollaborativeProjectRequestDetails.fxml"));
                MainApp.changeView(fxmlLoader);
                CollaborativeProjectRequestDetailsController collaborativeProjectRequestDetailsController = fxmlLoader.getController();              
                collaborativeProjectRequestDetailsController.setProfessor(professor);
                collaborativeProjectRequestDetailsController.setCollaborativeProjectRequest(collaborativeProjectRequest);                
            } else {
                DialogController.getInformativeConfirmationDialog("Sin solicitud seleccionada", "Necesita seleccionar una solicitud para poder ver sus detalles");
            }
        }
    }               
    
    private void initializeAll() {
        requestsTableView.getItems().addAll(initializeCollaborativeProjectRequestsArray());        
        courseTableColumn.setCellValueFactory((var cellData) -> {
            CollaborativeProjectRequest request = cellData.getValue();
            Course requesterCourse = request.getRequesterCourse();
            Course requestedCourse = request.getRequestedCourse();            
            if (requesterCourse != null && requesterCourse.getProfessor().getIdProfessor() !=
            professor.getIdProfessor()) {
                return new SimpleStringProperty(requesterCourse.toString());
            } else if (requestedCourse != null && requestedCourse.getProfessor().getIdProfessor() !=
            professor.getIdProfessor()) {
                return new SimpleStringProperty(requestedCourse.toString());
            } else {
                return new SimpleStringProperty("");
            }    
        });        
        professorTableColumn.setCellValueFactory(cellData -> {
            CollaborativeProjectRequest request = cellData.getValue();
            Course requesterCourse = request.getRequesterCourse();
            Course requestedCourse = request.getRequestedCourse();            
            if (requesterCourse != null && requesterCourse.getProfessor().getIdProfessor() !=
            professor.getIdProfessor()) {
                return new SimpleStringProperty(requesterCourse.getProfessor().toString());
            } else if (requestedCourse != null && requestedCourse.getProfessor().getIdProfessor() !=
            professor.getIdProfessor()) {
                return new SimpleStringProperty(requestedCourse.getProfessor().toString());
            } else {
                return new SimpleStringProperty("");
            }
        });        
        requestDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        validationDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("validationDate"));
        statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    
    private ArrayList<CollaborativeProjectRequest> initializeCollaborativeProjectRequestsArray() {
        ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests = new ArrayList<>();
        try {
            collaborativeProjectRequests = COLLABORATIVE_PROJECT_REQUEST_DAO.getCollaborativeProjectRequests(professor.getIdProfessor());
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
        return collaborativeProjectRequests;
    }
    
    private void updateTableView(ArrayList<CollaborativeProjectRequest> collaborativeProjectRequests) {        
        requestsTableView.getItems().clear();
        requestsTableView.getItems().addAll(collaborativeProjectRequests);
    }
            
    private void handleDAOException(DAOException exception) {
        requestsTableView.getItems().clear();
        try {
            
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            
            switch (exception.getStatus()) {
                case ERROR ->
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/ ");
                case FATAL ->
                    MainApp.changeView("/main/MainApp");
            }
        } catch (IOException ioException) {
            Log.getLogger(NotifyProfessorController.class).error(exception.getMessage(), exception);
        }
    }    
}
