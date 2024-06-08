package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.feedback.FeedbackDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.PDFCreator;
import mx.fei.coilvicapp.logic.student.Student;
import mx.fei.coilvicapp.logic.implementations.FileManager;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;

/**
 *
 * @author ivanr
 */
public class CollaborativeProjectDetailsStudentController implements Initializable {

    @FXML
    private VBox backgroundVBox;

    @FXML
    private Label collaborativeProjectNameLabel;

    @FXML
    private TextField collaborativeProjectNameTextField;

    @FXML
    private Button downloadCertificateButton;

    @FXML
    private Label professorOneLabel;

    @FXML
    private TextField professorOneTextField;

    @FXML
    private Label professorTwoLabel;

    @FXML
    private TextField professorTwoTextField;

    @FXML
    private Button startFeedBackButton;

    @FXML
    private Label titleLabel;

    @FXML
    private Label universityOneLabel;

    @FXML
    private TextField universityOneTextField;

    @FXML
    private Label universityTwoLabel;

    @FXML
    private TextField universityTwoTextField;

    @FXML
    private Button exitButton;

    private Student student;
    private CollaborativeProject collaborativeProject;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
    }

    @FXML
    void downloadCertificateButtonIsPressed(ActionEvent event) {
        if (isFeedbackDone()) {
            PDFCreator certificateCreator = new PDFCreator();
            
            if (certificateCreator.templateExists()) {
                try {
                    certificateCreator.generateCertificate(student.getName(), new FileManager().selectDirectoryPath(backgroundVBox.getScene().getWindow()));
                    DialogController.getInformativeConfirmationDialog("Aviso", "La constancia se descargó con éxito");
                } catch (IOException exception) {
                    handleIOException(exception);
                }
            } else {
                DialogController.getInformativeConfirmationDialog("Aviso", "No se encontraron los recursos para generar la constancia");
            }
        } else {
            DialogController.getInformativeConfirmationDialog("Aviso", "Es necesario completar la retroalimentación para descargar la constancia");
        }
    }

    private void handleIOException(IOException exception) {
        DialogController.getInformativeConfirmationDialog("Lo sentimos", exception.getMessage());
    }

    private boolean isFeedbackDone() {
        boolean result = false;
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        
        try {
            result = (feedbackDAO.hasCompletedPreForm(student, collaborativeProject) && feedbackDAO.hasCompletedPostForm(student, collaborativeProject));
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
        return result;
    }

    @FXML
    void startFeedBackIsPressed(ActionEvent event) {
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        
        try {
            if (feedbackDAO.areThereStudentQuestions()) {
                if (!feedbackDAO.hasCompletedPreForm(student, collaborativeProject)) {
                    feedbackOnCollaborativeProject("Estudiante-PRE");
                } else if (!feedbackDAO.hasCompletedPostForm(student, collaborativeProject)) {
                    if (collaborativeProject.getStatus().equals("Finalizado")) {
                        feedbackOnCollaborativeProject("Estudiante-POST");
                    } else {
                        DialogController.getInformativeConfirmationDialog("Aviso", "Podrás realizar la POST-Retroalimentación cuando la colaboración haya finalizado");
                    }
                } else {
                    DialogController.getInformativeConfirmationDialog("Aviso", "Ya has completado el proceso de retroalimentación");
                }
            } else {
                DialogController.getInformativeConfirmationDialog("Lo sentimos", "No es posible realizar la retroalimentación debido a que aún no hay preguntas para el estudiante");
            }
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    private void feedbackOnCollaborativeProject(String type) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/FeedbackOnCollaborativeProject.fxml"));
        
        try {
            MainApp.changeView(fxmlLoader);
            FeedbackOnCollaborativeProjectController feedbackOnCollaborativeProjectController = fxmlLoader.getController();
            feedbackOnCollaborativeProjectController.setCollaborativeProject(collaborativeProject);
            feedbackOnCollaborativeProjectController.setStudent(student);
            feedbackOnCollaborativeProjectController.setTypeQuestiones(type);
        } catch (IOException exception) {
            Log.getLogger(CollaborativeProjectDetailsStudentController.class).error(exception.getMessage(), exception);
        }
    }

    @FXML
    void exitButtonIsPressed(ActionEvent event) {
        goBack();
    }
    
    private void goBack() {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/LoginParticipant");
        } catch (IOException exception) {
            Log.getLogger(CollaborativeProjectDetailsStudentController.class).error(exception.getMessage(), exception);
        }
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCollaborativeProject(CollaborativeProject collaborativeProject) {
        this.collaborativeProject = collaborativeProject;
        fillTextFields(collaborativeProject);
    }

    private void fillTextFields(CollaborativeProject collaborativeProject) {
        collaborativeProjectNameTextField.setText(collaborativeProject.getName());
        professorOneTextField.setText(collaborativeProject.getRequesterCourse().getProfessor().getName());
        professorTwoTextField.setText(collaborativeProject.getRequestedCourse().getProfessor().getName());
        universityOneTextField.setText(collaborativeProject.getRequesterCourse().getProfessor().getUniversity().toString());
        universityTwoTextField.setText(collaborativeProject.getRequestedCourse().getProfessor().getUniversity().toString());
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    goBack();
                case FATAL ->
                    MainApp.handleFatal();
            }
        } catch (IOException ioException) {
            Log.getLogger(CollaborativeProjectDetailsStudentController.class).error(ioException.getMessage(), exception);
        }
    }
}
