package mx.fei.coilvicapp.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import log.Log;
import main.MainApp;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;
import mx.fei.coilvicapp.logic.student.IStudent;
import mx.fei.coilvicapp.logic.student.Student;
import mx.fei.coilvicapp.logic.student.StudentDAO;
import mx.fei.coilvicapp.logic.university.IUniversity;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;

/**
 *
 * @author ivanr
 */
public class RegisterStudentController implements Initializable {

    @FXML
    private Button acceptButton;

    @FXML
    private VBox backgroundVBox;

    @FXML
    private Button cancelButton;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField emailTextField;

    @FXML
    private ComboBox<String> gendersCombobox;

    @FXML
    private ComboBox<String> lineagesCombobox;

    @FXML
    private Label maternalSurnameLabel;

    @FXML
    private TextField maternalSurnameTextField;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField nameTextField;

    @FXML
    private Label paternalSurnameLabel;

    @FXML
    private TextField paternalSurnameTextField;

    @FXML
    private Label titleLabel;

    @FXML
    private Label enrollmentLabel;

    @FXML
    private TextField enrollmentTextField;

    @FXML
    private ComboBox<?> regionCombobox;

    @FXML
    private ComboBox<?> academicAreaCombobox;

    @FXML
    private ComboBox<University> universitiesCombobox;
    private final IUniversity UNIVERSITY_DAO = new UniversityDAO();
    private final IStudent STUDENT_DAO = new StudentDAO();
    private final Student STUDENT = new Student();

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
        gendersCombobox.setItems(FXCollections.observableArrayList("Masculino", "Femenino", "Otro"));
        lineagesCombobox.setItems(FXCollections.observableArrayList("Hispano, Latino u origen español", "Blanco", "Negro o africano", "Indio americano o nativo de Alaska", "Asiático o Isleño del Pacífico", "Otro"));
        ArrayList<University> universities = new ArrayList<>();
        try {
            universities = UNIVERSITY_DAO.getAllUniversities();
        } catch (DAOException exception) {
            goBack();
        }
        universitiesCombobox.setItems(FXCollections.observableArrayList(universities));
    }

    @FXML
    void acceptButtonIsPressed(ActionEvent event) {
        try {
            invokeRegisterStudent();
            DialogController.getInformativeConfirmationDialog("Registrado", "El estudiante fue registrado con éxito");
            goBack();
        } catch (IllegalArgumentException exception) {
            handleValidationException(exception);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    @FXML
    void universityIsSelected(ActionEvent event) {
        if (isMemberUV(universitiesCombobox.getValue()) == true) {
            setVisibilityOfUVFields(true);
        } else {
            setVisibilityOfUVFields(false);
        }
    }

    private void setVisibilityOfUVFields(boolean isVisible) {
        regionCombobox.setVisible(isVisible);
        academicAreaCombobox.setVisible(isVisible);
        enrollmentLabel.setVisible(isVisible);
        enrollmentTextField.setVisible(isVisible);
    }

    private boolean isMemberUV(University university) {
        return university.getName().equals("Universidad Veracruzana");
    }

    private void initializeStudent() {
        STUDENT.setName(nameTextField.getText());
        STUDENT.setPaternalSurname(paternalSurnameTextField.getText());
        STUDENT.setMaternalSurname(maternalSurnameTextField.getText());
        STUDENT.setEmail(emailTextField.getText());
        STUDENT.setGender(gendersCombobox.getValue());
        STUDENT.setLineage(lineagesCombobox.getValue());
        STUDENT.setUniversity(universitiesCombobox.getValue());
    }

    private void invokeRegisterStudent() throws DAOException {
        initializeStudent();
        STUDENT_DAO.registerStudent(STUDENT);
    }

    @FXML
    void cancelButtonIsPressed(ActionEvent event) {
        if (textFieldsAreClean() || confirmCancelation()) {
            try {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/LoginParticipant");
            } catch (IOException exception) {
                Log.getLogger(RegisterStudentController.class).error(exception.getMessage(), exception);
            }
        }
    }

    private boolean textFieldsAreClean() {
        return nameTextField.getText().equals("") && paternalSurnameTextField.getText().equals("") && maternalSurnameTextField.getText().equals("") && emailTextField.getText().equals("");
    }

    private boolean confirmCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cancelacion", "¿Deseas cancelar el registro?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getInvalidDataDialog(exception.getMessage());
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
            Log.getLogger(RegisterStudentController.class).error(exception.getMessage(), exception);
        }
    }

    private void goBack() {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/LoginParticipant");
        } catch (IOException exception) {
            Log.getLogger(RegisterStudentController.class).error(exception.getMessage(), exception);
        }
    }

}
