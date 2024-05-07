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
import javafx.scene.layout.VBox;
import main.MainApp;
import mx.fei.coilvicapp.logic.collaborativeProject.CollaborativeProject;
import mx.fei.coilvicapp.logic.implementations.PDFCreator;
import mx.fei.coilvicapp.logic.student.Student;
import mx.fei.coilvicapp.logic.implementations.FileManager;
import mx.fei.coilvicapp.logic.implementations.Status;

/**
 *
 * @author ivanr
 */
public class CollaborativeProjectDetailsStudentController implements Initializable {

    @FXML
    private VBox backgroundVBox;

    @FXML
    private Button downloadCertificateButton;

    @FXML
    private Button startFeedBackButton;

    private Student student;
    private CollaborativeProject collaborativeProject;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
        System.out.println("hola");
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
        DialogController.getInformativeConfirmationDialog("Lo sentimosss", exception.getMessage());
    }

    private boolean isFeedbackDone() {

        return true;
    }

    @FXML
    void startFeedBackIsPressed(ActionEvent event) {
        System.out.println(collaborativeProject);
        if (collaborativeProject.getStatus().equals("Aceptado")) {
            try {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/FeedbackOnCollaborativeProject");
            } catch (IOException exception) {

            }
        } else {
            System.out.println("Pendiente/...");
        }
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCollaborativeProject(CollaborativeProject collaborativeProject) {
        this.collaborativeProject = collaborativeProject;
        if(collaborativeProject != null) { 
            System.out.println(collaborativeProject.getStatus());
        } else {
            System.out.println("es nul");
        }
    }

}
