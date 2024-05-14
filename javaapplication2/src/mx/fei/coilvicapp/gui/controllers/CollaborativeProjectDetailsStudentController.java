package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.feedback.FeedbackDAO;
import mx.fei.coilvicapp.logic.feedback.IFeedback;
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
                    wasRegisteredConfirmation();
                } catch (IOException exception) {
                    handleIOException(exception);
                }
            } else {
                inform("No se encontraron los recuros para genera la constancia");
            }
        } else {
            inform("Es necesario completar la retroalimentación para descargar la constancia");
        }

    }

    private void inform(String message) {
        DialogController.getInformativeConfirmationDialog("Aviso", message);
    }

    private boolean wasRegisteredConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog("Aviso", "La constancia se descargó con éxito");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }

    private void handleIOException(IOException exception) {
        DialogController.getInformativeConfirmationDialog("Lo sentimos", exception.getMessage());
    }

    private boolean isFeedbackDone() {

        return true;
    }

    @FXML
    void startFeedBackIsPressed(ActionEvent event) {
        if (collaborativeProject.getStatus().equals("Aceptado")) {
            IFeedback feedbackDAO = new FeedbackDAO();
            try {
                if (feedbackDAO.areThereStudentQuestions()) {
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/FeedbackOnCollaborativeProject");
                } else {
                    Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog("Lo sentimos", "No "
                            + "es posible realizar la retroalimentación debido a que aún no hay preguntas para el estudiante");
                }
            } catch (DAOException exception) {
                handleDAOException(exception);
                Log.getLogger(CollaborativeProjectDetailsStudentController.class).error(exception.getMessage(), exception);
            } catch (IOException exception) {
                Log.getLogger(CollaborativeProjectDetailsStudentController.class).error(exception.getMessage(), exception);
            }
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
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/UniversityManager");
                case FATAL ->
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/LoginParticipantController");
            }
        } catch (IOException ioException) {
            Log.getLogger(CollaborativeProjectDetailsStudentController.class).error(ioException.getMessage(), exception);
        }
    }

}
