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
import mx.fei.coilvicapp.logic.academicarea.AcademicArea;
import mx.fei.coilvicapp.logic.academicarea.AcademicAreaDAO;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;
import mx.fei.coilvicapp.logic.region.Region;
import mx.fei.coilvicapp.logic.region.RegionDAO;
import mx.fei.coilvicapp.logic.student.IStudent;
import mx.fei.coilvicapp.logic.student.Student;
import mx.fei.coilvicapp.logic.student.StudentDAO;
import mx.fei.coilvicapp.logic.student.StudentUV;
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
    private ComboBox<Region> regionCombobox;

    @FXML
    private ComboBox<AcademicArea> academicAreaCombobox;

    @FXML
    private ComboBox<University> universitiesCombobox;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {
        gendersCombobox.setItems(FXCollections.observableArrayList(initializeGendersArrayForComboBox()));
        universitiesCombobox.setItems(FXCollections.observableArrayList(initializeUniversitiesArrayForComboBox()));
        academicAreaCombobox.setItems(FXCollections.observableArrayList(initializeAcademicAreasArrayForComboBox()));
        initializeLineajesArrayForCombobox();
        regionCombobox.setItems(FXCollections.observableArrayList(initializeRegionsArrayForComboBox())); 
    }

    private void initializeLineajesArrayForCombobox() {

        lineagesCombobox.setItems(FXCollections.observableArrayList("Hispano, Latino u origen español", 
                "Blanco", "Negro o africano", "Indio americano o nativo de Alaska", 
                "Asiático o Isleño del Pacífico", "Otro"));
    }

    private ArrayList<String> initializeGendersArrayForComboBox() {
        ArrayList<String> genders = new ArrayList<>();
        genders.add("Hombre");
        genders.add("Mujer");
        genders.add("Otro");
        return genders;
    }

    private ArrayList<AcademicArea> initializeAcademicAreasArrayForComboBox() {
        AcademicAreaDAO academicAreaDAO = new AcademicAreaDAO();
        ArrayList<AcademicArea> academicAreas = new ArrayList<>();
        try {
            academicAreas = academicAreaDAO.getAcademicAreas();
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
        return academicAreas;
    }

    private ArrayList<University> initializeUniversitiesArrayForComboBox() {
        IUniversity universityDAO = new UniversityDAO();
        ArrayList<University> universities = new ArrayList<>();
        try {
            universities = universityDAO.getAllUniversities();
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
        return universities;
    }

    private ArrayList<Region> initializeRegionsArrayForComboBox() {
        RegionDAO regionDAO = new RegionDAO();
        ArrayList<Region> regions = new ArrayList<>();
        try {
            regions = regionDAO.getRegions();
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
        return regions;
    }

    @FXML
    void acceptButtonIsPressed(ActionEvent event) {
        try {
            invokeRegisterStudent();
            DialogController.getInformativeConfirmationDialog("Registrado", "El estudiante fue "
                    + "registrado con éxito");
            goBack();
        } catch (IllegalArgumentException exception) {
            handleValidationException(exception);
        } catch (DAOException exception) {
            handleDAOException(exception);
        }
    }

    @FXML
    void universityIsSelected(ActionEvent event) {
        if (isMemberUV(universitiesCombobox.getValue())) {
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

    private Student initializeStudent() {
        Student student = new Student();
        student.setName(nameTextField.getText());
        student.setPaternalSurname(paternalSurnameTextField.getText());
        student.setMaternalSurname(maternalSurnameTextField.getText());
        student.setEmail(emailTextField.getText());
        student.setGender(gendersCombobox.getValue());
        student.setLineage(lineagesCombobox.getValue());
        student.setUniversity(universitiesCombobox.getValue());
        return student;
    }

    private void invokeRegisterStudent() throws DAOException {
        IStudent studentDAO = new StudentDAO();
        if (isMemberUV(universitiesCombobox.getValue())) {
            studentDAO.registerStudentUV(initializeStudentUV(initializeStudent()));
        } else {
            initializeStudent();
            studentDAO.registerStudent(initializeStudent());
        }
    }

    private StudentUV initializeStudentUV(Student student) {
        StudentUV studentUV = new StudentUV();
        
        studentUV.setName(nameTextField.getText());
        studentUV.setPaternalSurname(paternalSurnameTextField.getText());
        studentUV.setMaternalSurname(maternalSurnameTextField.getText());
        studentUV.setEmail(emailTextField.getText());
        studentUV.setGender(gendersCombobox.getValue());
        studentUV.setLineage(lineagesCombobox.getValue());
        studentUV.setUniversity(universitiesCombobox.getValue());
        studentUV.setAcademicArea(academicAreaCombobox.getValue());
        studentUV.setRegion(regionCombobox.getValue());
        studentUV.setEnrollment(enrollmentTextField.getText());
        return studentUV;
    }

    @FXML
    void cancelButtonIsPressed(ActionEvent event) {
        if (confirmCancelation()) {
            try {
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/LoginParticipant");
            } catch (IOException exception) {
                Log.getLogger(RegisterStudentController.class).error(exception.getMessage(), exception);
            }
        }
    }

    private boolean confirmCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog(
                "Confirmar cancelacion", "¿Deseas cancelar el registro?");
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
                    goBack();
                case FATAL ->
                    MainApp.handleFatal();
            }
        } catch (IOException ioException) {
            Log.getLogger(RegisterStudentController.class).error(ioException.getMessage(), ioException);
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
