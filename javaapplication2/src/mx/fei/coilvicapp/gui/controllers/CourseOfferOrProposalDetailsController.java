package mx.fei.coilvicapp.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.collaborativeprojectrequest.*;
import mx.fei.coilvicapp.logic.course.*;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;
import mx.fei.coilvicapp.logic.professor.*;

/**
 * FXML Controller class
 *
 * @author d0ubl3_d
 */
public class CourseOfferOrProposalDetailsController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TextField nameCourseTextField;

    @FXML
    private TextArea generalObjectiveTextArea;

    @FXML
    private TextField topicsInterestTextField;

    @FXML
    private TextField numberStudentsTextField;

    @FXML
    private TextField languageTextField;

    @FXML
    private TextField studentsProfileTextField;

    @FXML
    private TextArea additionalInformationTextArea;

    @FXML
    private TextField termTextField;

    @FXML
    private TextField nameProfessorTextField;

    @FXML
    private TextField paternalSurnameTextField;

    @FXML
    private TextField maternalSurnameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private TextField genderTextField;

    @FXML
    private TextField universityTextField;

    @FXML
    private TextField countryTextField;

    @FXML
    private Label acceptedCoursesLabel;

    @FXML
    private ComboBox<Course> acceptedCoursesComboBox;

    @FXML
    private Button sendRequestButton;

    @FXML
    private Button rejectButton;

    @FXML
    private Button acceptButton;

    private final CourseDAO COURSE_DAO = new CourseDAO();
    private Course course = new Course();

    private Professor professor;

    private final CollaborativeProjectRequestDAO COLLABORATIVE_PROJECT_REQUEST = new CollaborativeProjectRequestDAO();

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        if (course != null) {
            this.course = course;
            initializeAll();
        }
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    @FXML
    void backButtonIsPressed(ActionEvent event) {
        if (event.getSource() == backButton) {
            goBack();
        }
    }

    private void goBack() {
        try {
            if (professor == null) {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/CourseOffersOrProposalsManagement");
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/CourseOffersOrProposalsManagement.fxml"));
                MainApp.changeView(fxmlLoader);
                CourseOffersOrProposalsManagementController courseOffersOrProposalsManagementController = fxmlLoader.getController();
                courseOffersOrProposalsManagementController.setProfessor(professor);
            }
        } catch (IOException exception) {
            Log.getLogger(CourseOfferOrProposalDetailsController.class).error(exception.getMessage(), exception);
        }
    }

    @FXML
    void sendRequestButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == sendRequestButton) {
            if (acceptedCoursesComboBox.getValue() != null) {
                if (confirmSendRequest()) {
                    int result = -1;
                    try {
                        CollaborativeProjectRequest collaborativeProjectRequest = new CollaborativeProjectRequest();
                        collaborativeProjectRequest.setRequesterCourse(acceptedCoursesComboBox.getValue());
                        collaborativeProjectRequest.setRequestedCourse(course);
                        result = COLLABORATIVE_PROJECT_REQUEST.registerCollaborativeProjectRequest(collaborativeProjectRequest);
                    } catch (IllegalArgumentException exception) {
                        handleValidationException(exception);
                    } catch (DAOException exception) {
                        handleDAOException(exception);
                    }
                    if (result > 0) {
                        collaborativeProjectRequestConfirmationSent();
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/CourseOffersOrProposalsManagement.fxml"));
                        MainApp.changeView(fxmlLoader);
                        CourseOffersOrProposalsManagementController courseOffersOrProposalsManagementController = fxmlLoader.getController();
                        courseOffersOrProposalsManagementController.setProfessor(professor);
                    }
                }
            } else {
                emptyComboBoxConfirmation();
            }
        }
    }

    @FXML
    void rejectButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == rejectButton) {
            if (confirmRejectCourse()) {
                int result = -1;
                try {
                    result = COURSE_DAO.evaluateCourseProposal(course, "Rechazado");
                } catch (IllegalArgumentException exception) {
                    handleValidationException(exception);
                } catch (DAOException exception) {
                    handleDAOException(exception);
                }
                if (result > 0) {
                    wasRejectedConfirmation();
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/CourseOffersOrProposalsManagement");
                }
            }
        }
    }

    @FXML
    void acceptButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == acceptButton) {
            if (confirmAcceptCourse()) {
                int result = -1;
                try {
                    result = COURSE_DAO.evaluateCourseProposal(course, "Aceptado");
                } catch (IllegalArgumentException exception) {
                    handleValidationException(exception);
                } catch (DAOException exception) {
                    handleDAOException(exception);
                }
                if (result > 0) {
                    wasAcceptedConfirmation();
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/CourseOffersOrProposalsManagement");
                }
            }
        }
    }

    public void initializeAll() {
        nameCourseTextField.setText(course.getName());
        generalObjectiveTextArea.setText(course.getGeneralObjective());
        topicsInterestTextField.setText(course.getTopicsInterest());
        numberStudentsTextField.setText(String.valueOf(course.getNumberStudents()));
        languageTextField.setText(course.getLanguage().toString());
        studentsProfileTextField.setText(course.getStudentsProfile());
        additionalInformationTextArea.setText(course.getAdditionalInformation());
        termTextField.setText(course.getTerm().toString());

        nameProfessorTextField.setText(course.getProfessor().getName());
        paternalSurnameTextField.setText(course.getProfessor().getPaternalSurname());
        maternalSurnameTextField.setText(course.getProfessor().getMaternalSurname());
        emailTextField.setText(course.getProfessor().getEmail());
        phoneNumberTextField.setText(course.getProfessor().getPhoneNumber());
        genderTextField.setText(course.getProfessor().getGender());
        universityTextField.setText(course.getProfessor().getUniversity().toString());
        countryTextField.setText(course.getProfessor().getUniversity().getCountry().toString());
        if (professor == null) {
            sendRequestButton.setVisible(false);
            rejectButton.setVisible(true);
            acceptButton.setVisible(true);
        } else {
            sendRequestButton.setVisible(true);
            rejectButton.setVisible(false);
            acceptButton.setVisible(false);
            acceptedCoursesLabel.setVisible(true);
            acceptedCoursesComboBox.setVisible(true);
            acceptedCoursesComboBox.setItems(FXCollections.observableArrayList(initializeAcceptedCoursesArrayForComboBox()));
        }
    }

    private ArrayList<Course> initializeAcceptedCoursesArrayForComboBox() {
        ArrayList<Course> courses = new ArrayList<>();
        try {
            courses = COURSE_DAO.getAcceptedCoursesByProfessor(professor.getIdProfessor());
            if(courses.isEmpty()) {
                DialogController.getInformativeConfirmationDialog("Aviso", "No tienes cursos aceptados para poder"
                        + " enviar solicitudes para proyecto colaborativo");
            }
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
        return courses;
    }

    private boolean confirmRejectCourse() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar rechazo", "¿Está seguro de que desea rechazar el curso?");
        return (response.get() == DialogController.BUTTON_YES);
    }
    
    private boolean confirmSendRequest() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar solicitud", 
                "¿Está seguro de enviar la solicitud curso? Una vez enviada no podrá usar este curso "
                        + "para otro proyecto");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean confirmAcceptCourse() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar aceptación", "¿Está seguro de que desea aceptar el curso?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean wasRejectedConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog("Curso rechazado", "Se rechazó el curso con éxito");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }

    private boolean wasAcceptedConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog("Curso aceptado", "Se aceptó el curso con éxito");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }

    private boolean emptyComboBoxConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog("Alerta", "Seleccione un curso para enviar solicitud");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }

    private boolean collaborativeProjectRequestConfirmationSent() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog("Solicitud enviada", "Se envió la solicitud con éxito");
        return response.get() == DialogController.BUTTON_ACCEPT;
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
            Log.getLogger(CourseOfferOrProposalDetailsController.class).error(ioException.getMessage(), ioException);
        }
    }

    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getDialog(new AlertMessage(exception.getMessage(), Status.WARNING));
    }
}
