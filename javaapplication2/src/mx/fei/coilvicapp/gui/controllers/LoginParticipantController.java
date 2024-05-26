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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import mx.fei.coilvicapp.logic.collaborativeproject.ICollaborativeProject;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;
import mx.fei.coilvicapp.logic.professor.IProfessor;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.professor.ProfessorDAO;
import mx.fei.coilvicapp.logic.student.IStudent;
import mx.fei.coilvicapp.logic.student.Student;
import mx.fei.coilvicapp.logic.student.StudentDAO;
import mx.fei.coilvicapp.logic.user.IUser;
import mx.fei.coilvicapp.logic.user.UserDAO;

/**
 *
 * @author ivanr
 */
public class LoginParticipantController implements Initializable {

    @FXML
    private Button showButton;

    @FXML
    private TextField emailTextField;

    @FXML
    private Label identifierLabel;

    @FXML
    private PasswordField identifierPasswordField;

    @FXML
    private Button registerButton;

    @FXML
    private Button roleButton;

    @FXML
    private Button startButton;

    @FXML
    private Label titleLabel;

    private final IProfessor PROFESSOR_DAO = new ProfessorDAO();
    private final IStudent STUDENT_DAO = new StudentDAO();
    private final IUser USER_DAO = new UserDAO();
    private final ICollaborativeProject COLLABORATIVE_PROJECT_DAO = new CollaborativeProjectDAO();
    private String passwordForShow;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {

    }

    @FXML
    void roleButtonIsPressed(ActionEvent event) {
        if (roleButton.getText().equals("Profesor")) {
            roleButton.setText("Estudiante");
            identifierLabel.setText("Código de proyecto:");
        } else {
            roleButton.setText("Profesor");
            identifierLabel.setText("Contraseña: ");
        }
    }

    @FXML
    void startButtonIsPressed(ActionEvent event) {
        try {
            if (roleButton.getText().equals("Profesor")) {
                invokeAuthenticateProfessor();
            } else {
                invokeAuthenticateStudentAndCollaborativeProject();
            }
        } catch (IllegalArgumentException exception) {
            DialogController.getInvalidDataDialog(exception.getMessage());
        } catch (DAOException exception) {
            handleDAOException(exception);
        } catch (IOException exception) {
            Log.getLogger(LoginParticipantController.class).error(exception.getMessage(), exception);
        }
    }

    private void invokeAuthenticateStudentAndCollaborativeProject() throws DAOException, IOException {
        Student student = new Student();

        student.setEmail(emailTextField.getText());
        student = STUDENT_DAO.getStudentByEmail(emailTextField.getText());
        if (student.getIdStudent() > 0) {
            CollaborativeProject collaborativeProject = COLLABORATIVE_PROJECT_DAO.getCollaborativeProjectByCode(identifierPasswordField.getText());
            if (collaborativeProject.getIdCollaborativeProject() > 0) {
                changeViewForStudent(student, collaborativeProject);
            } else {
                DialogController.getInformativeConfirmationDialog("Proyecto no encontrado", "No se encontró ningún proyecto con el codigo: " + identifierPasswordField.getText());
            }
        } else {
            DialogController.getInformativeConfirmationDialog("Estudiante no encontrado", "No se encontró ningún registro con el correo: " + emailTextField.getText());
        }
    }

    private void invokeAuthenticateProfessor() throws DAOException, IOException {
        Professor professor = new Professor();
        FieldValidator fieldValidator = new FieldValidator();

        professor.setEmail(emailTextField.getText());
        fieldValidator.checkPassword(identifierPasswordField.getText());
        if (USER_DAO.authenticateUser(emailTextField.getText(), identifierPasswordField.getText())) {
            professor = PROFESSOR_DAO.getProfessorByEmail(emailTextField.getText());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/RegisterCourse.fxml"));

        MainApp.changeView(fxmlLoader);
        RegisterCourseController collaborativeProjectDetailsStudentController = fxmlLoader.getController();
        collaborativeProjectDetailsStudentController.setProfessor(professor);

        }
    }

    private void changeViewForStudent(Student student, CollaborativeProject collaborativeProject) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/CollaborativeProjectDetailsStudent.fxml"));

        MainApp.changeView(fxmlLoader);
        CollaborativeProjectDetailsStudentController collaborativeProjectDetailsStudentController = fxmlLoader.getController();
        collaborativeProjectDetailsStudentController.setStudent(student);
        collaborativeProjectDetailsStudentController.setCollaborativeProject(collaborativeProject);
    }

    @FXML
    void registerButtonIsPressed(ActionEvent event) {
        try {
            if (roleButton.getText().equals("Profesor")) {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/ProfessorRegister");
            } else {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/RegisterStudent");
            }
        } catch (IOException exception) {
            Log.getLogger(LoginParticipantController.class).error(exception.getMessage(), exception);
        }
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    MainApp.changeView("/main/MainApp");
                case FATAL ->
                    MainApp.changeView("/main/MainApp");
            }
        } catch (IOException ioException) {
            Log.getLogger(LoginParticipantController.class).error(ioException.getMessage(), ioException);
        }
    }

    @FXML
    private void showButtonIsPressed() {
        if (identifierPasswordField != null) {
            passwordForShow = identifierPasswordField.getText();
            identifierPasswordField.clear();
            identifierPasswordField.setPromptText(passwordForShow);
        }
    }

    @FXML
    private void showButtonIsReleased() {
        identifierPasswordField.setText(passwordForShow);
        identifierPasswordField.setPromptText(null);
    }
}
