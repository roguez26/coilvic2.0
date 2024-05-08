package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.MainApp;
import mx.fei.coilvicapp.logic.collaborativeProject.CollaborativeProject;
import mx.fei.coilvicapp.logic.collaborativeProject.CollaborativeProjectDAO;
import mx.fei.coilvicapp.logic.collaborativeProject.ICollaborativeProject;
import mx.fei.coilvicapp.logic.implementations.DAOException;
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
    private Label emailLabel;

    @FXML
    private TextField emailTextField;

    @FXML
    private Label identifierLabel;

    @FXML
    private PasswordField identifierPasswordTextField;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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
        if (roleButton.getText().equals("Profesor")) {
            try {
                if (USER_DAO.authenticateUser(emailTextField.getText(), identifierPasswordTextField.getText())) {
                    Professor professor = PROFESSOR_DAO.getProfessorByEmail(emailTextField.getText());
                }
            } catch (DAOException daoException) {
                handleDAOException(daoException);
            }
        } else {
            try {
                Student student = STUDENT_DAO.getStudentByEmail(emailTextField.getText());
                if (student.getIdStudent() > 0) {
                    CollaborativeProject collaborativeProject = COLLABORATIVE_PROJECT_DAO.getCollaborativeProjectByCode(identifierPasswordTextField.getText());
                    if (collaborativeProject.getIdCollaborativeProject() > 0) {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/CollaborativeProjectDetailsStudent.fxml"));
                        MainApp.changeView(fxmlLoader);
                        CollaborativeProjectDetailsStudentController collaborativeProjectDetailsStudentController = fxmlLoader.getController();
                        collaborativeProjectDetailsStudentController.setStudent(student);
                        collaborativeProjectDetailsStudentController.setCollaborativeProject(collaborativeProject);
                    } else {
                        wasNotFound("Proyecto no encontrado", "No se encontró ningún proyecto con el codigo: " + identifierPasswordTextField.getText());
                    }
                } else {
                    wasNotFound("Estudiante no encontrado", "No se encontró ningún registro con el correo: " + emailTextField.getText());
                }
            } catch (DAOException daoException) {
                handleDAOException(daoException);
            } catch (IOException exception) {

            }
        }
    }

    @FXML
    void registerButtonIsPressed(ActionEvent event) {
        try {
            if (roleButton.getText().equals("Profesor")) {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/RegisterProfessor");
            } else {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/RegisterStudent");
            }

        } catch (IOException exception) {

        }
    }

    private boolean wasNotFound(String title, String message) {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog(title, message);
        return response.get() == DialogController.BUTTON_ACCEPT;
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
