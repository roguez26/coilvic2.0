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
import mx.fei.coilvicapp.logic.user.User;
import mx.fei.coilvicapp.logic.user.UserDAO;

public class LoginParticipantController implements Initializable {

    @FXML
    private Button showButton;

    @FXML
    private Label emailLabel;

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

    private String passwordForShow;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
    }

    @FXML
    void roleButtonIsPressed(ActionEvent event) {
        switch (roleButton.getText()) {
            case "Profesor" -> {
                roleButton.setText("Estudiante");
                identifierLabel.setText("Código de proyecto:");
            }
            case "Estudiante" -> {
                emailLabel.setText("Usuario:");
                emailTextField.setPromptText("Ej. 22106");
                roleButton.setText("Administrativo");
                registerButton.setVisible(false);
                identifierLabel.setText("Contraseña:");
            }
            case "Administrativo" -> {
                emailLabel.setText("Correo:");
                emailTextField.setPromptText("Ej. coilvic@gmail.com");
                roleButton.setText("Profesor");
                registerButton.setVisible(true);
                identifierLabel.setText("Contraseña:");
            }
        }
    }

    @FXML
    void startButtonIsPressed(ActionEvent event) {
        try {
            switch (roleButton.getText()) {
                case "Profesor" ->
                    invokeAuthenticateProfessor();
                case "Estudiante" ->
                    invokeAuthenticateStudentAndCollaborativeProject();
                case "Administrativo" ->
                    invokeAuthenticateAdministrative();
                default -> {
                }
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
        IStudent studentDAO = new StudentDAO();
        CollaborativeProject collaborativeProject = new CollaborativeProject();
        
        student.setEmail(emailTextField.getText());
        collaborativeProject.setCode(identifierPasswordField.getText());
        student = studentDAO.getStudentByEmail(emailTextField.getText());
        if (student.getIdStudent() > 0) {
            ICollaborativeProject collaborativeProjectDAO = new CollaborativeProjectDAO();
            collaborativeProject
                    = collaborativeProjectDAO.getCollaborativeProjectByCode(identifierPasswordField.getText());
            if (collaborativeProject.getIdCollaborativeProject() > 0) {
                changeViewForStudent(student, collaborativeProject);
            } else {
                DialogController.getInformativeConfirmationDialog("Proyecto no encontrado",
                        "No se encontró ningún proyecto con el codigo: " + identifierPasswordField.getText());
            }
        } else {
            DialogController.getInformativeConfirmationDialog("Estudiante no encontrado",
                    "No se encontró ningún registro con el correo: " + emailTextField.getText());
        }
    }

    private void invokeAuthenticateProfessor() throws DAOException, IOException {
        Professor professor = new Professor();
        FieldValidator fieldValidator = new FieldValidator();
        IUser userDAO = new UserDAO();

        professor.setEmail(emailTextField.getText());
        fieldValidator.checkPassword(identifierPasswordField.getText());
        if (userDAO.authenticateUser(emailTextField.getText(), identifierPasswordField.getText())) {
            IProfessor professorDAO = new ProfessorDAO();
            professor = professorDAO.getProfessorByEmail(emailTextField.getText());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/ProfessorMainMenu.fxml"));

            MainApp.changeView(fxmlLoader);
            ProfessorMainMenuController professorMainMenuController = fxmlLoader.getController();
            professorMainMenuController.setProfessor(professor);

        }
    }

    private void invokeAuthenticateAdministrative() throws DAOException, IOException {
        User user;
        FieldValidator fieldValidator = new FieldValidator();
        IUser userDAO = new UserDAO();
        int idUser = 0;
        try {
            idUser = Integer.parseInt(emailTextField.getText());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("El usuario proporcionado es incorrecto");
        }
        

        fieldValidator.checkPassword(identifierPasswordField.getText());
        if (userDAO.authenticateAdministrativeUser(idUser, identifierPasswordField.getText())) {
            user = userDAO.getUserById(idUser);
            if (user.getType().equalsIgnoreCase("C")) {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/CoordinationMainMenu");
            } else if (user.getType().equalsIgnoreCase("A")) {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/AssistantMainMenu");
            } else {
                throw new IllegalArgumentException("No se encontró ningun usuario con esos datos");
            }

        }
    }

    private void changeViewForStudent(Student student, CollaborativeProject collaborativeProject) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/mx/fei/coilvicapp/gui/views/CollaborativeProjectDetailsStudent.fxml"));

        MainApp.changeView(fxmlLoader);
        CollaborativeProjectDetailsStudentController collaborativeProjectDetailsStudentController = fxmlLoader.getController();
        collaborativeProjectDetailsStudentController.setStudent(student);
        collaborativeProjectDetailsStudentController.setCollaborativeProject(collaborativeProject);
    }

    @FXML
    void registerButtonIsPressed(ActionEvent event) {
        try {

            if (roleButton.getText().equals("Profesor")) {
                IProfessor professorDAO = new ProfessorDAO();
                if (professorDAO.checkPreconditions()) {
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/ProfessorRegister");
                } else {
                    DialogController.getInformativeConfirmationDialog("Recursos no disponibles",
                            "No contamos con los recursos para realizar su registro");
                }
            } else {
                IStudent studentDAO = new StudentDAO();
                if (studentDAO.checkPreconditions()) {
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/RegisterStudent");
                } else {
                    DialogController.getInformativeConfirmationDialog("Recursos no disponibles",
                            "No contamos con los recursos para realizar su registro");
                }
            }
        } catch (DAOException exception) {
            handleDAOException(exception);
        } catch (IOException exception) {
            Log.getLogger(LoginParticipantController.class).error(exception.getMessage(), exception);
        }
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/LoginParticipant");
                case FATAL ->
                     MainApp.handleFatal();
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
