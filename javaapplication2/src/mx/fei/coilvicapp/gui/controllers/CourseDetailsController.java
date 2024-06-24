package mx.fei.coilvicapp.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;
import java.io.IOException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.course.*;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;
import mx.fei.coilvicapp.logic.language.*;
import mx.fei.coilvicapp.logic.professor.*;
import mx.fei.coilvicapp.logic.term.*;

public class CourseDetailsController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextArea generalObjectiveTextArea;

    @FXML
    private TextField topicsInterestTextField;

    @FXML
    private ComboBox<Integer> numberStudentsComboBox;

    @FXML
    private ComboBox<Term> termComboBox;

    @FXML
    private ComboBox<Language> languageComboBox;

    @FXML
    private TextField studentsProfileTextField;

    @FXML
    private TextArea additionalInformationTextArea;

    @FXML
    private TextField statusTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelCourseProposalButton;

    @FXML
    private Button updateButton;

    private Course course;
    private Professor professor;

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
    void backButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == backButton) {
            goBack();
        }
    }

    private void goBack() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mx/fei/coilvicapp/gui/views/ProfessorCourseManagement.fxml"));
            MainApp.changeView(fxmlLoader);
            ProfessorCourseManagementController professorCourseManagementController
                    = fxmlLoader.getController();
            professorCourseManagementController.setProfessor(professor);
        } catch (IOException exception) {
            Log.getLogger(CourseDetailsController.class).error(exception.getMessage(), exception);
        }
    }

    @FXML
    void cancelButtonIsPressed(ActionEvent event) {
        if (confirmCancelation()) {
            changeComponentsEditabilityFalse();
            initializeAll();
        }
    }

    @FXML
    private void saveButtonIsPressed(ActionEvent event) throws IOException {
        if (event.getSource() == saveButton) {
            try {
                invokeCourseUpdate();
            } catch (IllegalArgumentException exception) {
                handleValidationException(exception);
            } catch (DAOException exception) {
                handleDAOException(exception);
            }
        }
    }

    @FXML
    private void cancelCourseProposalButtonIsPressed(ActionEvent event) throws IOException {
        if (confirmCourseProposalCancelation()) {
            int result = -1;
            CourseDAO courseDAO = new CourseDAO();
            try {
                result = courseDAO.cancelCourseProposal(course);
            } catch (DAOException exception) {
                handleDAOException(exception);
            }
            if (result > 0) {
                wasCanceledConfirmation();
                course.setStatus("Cancelado");
                cleanFields();
                initializeAll();
            }
        }
    }

    @FXML
    private void updateButtonIsPressed(ActionEvent event) throws IOException {
        changeComponentsEditabilityTrue();
    }

    private void invokeCourseUpdate() throws DAOException {
        if (!fieldsAreEmpty()) {
            int result = -1;
            CourseDAO courseDAO = new CourseDAO();
            try {
                result = courseDAO.updateCourse(updateCourse());
            } catch (DAOException exception) {
                handleDAOException(exception);
            }
            if (result > 0) {
                wasUpdateConfirmation();
                cleanFields();
                initializeAll();
                changeComponentsEditabilityFalse();
            }
        } else {
            emptyFieldsConfirmation();
        }
    }

    private Course updateCourse() {
        course.setProfessor(professor);
        course.setName(nameTextField.getText());
        course.setGeneralObjective(generalObjectiveTextArea.getText());
        course.setTopicsInterest(topicsInterestTextField.getText());
        course.setNumberStudents((int) numberStudentsComboBox.getValue());
        course.setTerm((Term) termComboBox.getValue());
        course.setLanguage((Language) languageComboBox.getValue());
        course.setStudentsProfile(studentsProfileTextField.getText());
        course.setAdditionalInformation(additionalInformationTextArea.getText());
        return course;
    }

    private boolean confirmCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cancelacion", "¿Está seguro de que desea cancelar?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean confirmCourseProposalCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cancelacion", "¿Está seguro de que desea cancelar el curso?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    public void initializeAll() {
        numberStudentsComboBox.setItems(FXCollections.observableArrayList(initializeNumberStudentsArrayForComboBox()));
        numberStudentsComboBox.setValue(course.getNumberStudents());
        try {
            termComboBox.setItems(FXCollections.observableArrayList(initializeTermsArrayForComboBox()));
            termComboBox.setValue(course.getTerm());
            languageComboBox.setItems(FXCollections.observableArrayList(initializeLanguagesArrayForComboBox()));
        } catch (DAOException exception) {
            handleDAOException(exception);
        }

        languageComboBox.setValue(course.getLanguage());
        initializeTextFields();
        if (course.getStatus().equals("Pendiente") || course.getStatus().equals("Rechazado")) {
            updateButton.setVisible(true);
            cancelCourseProposalButton.setVisible(true);
        } else {
            updateButton.setVisible(false);
            cancelCourseProposalButton.setVisible(false);
        }
    }

    public void initializeTextFields() {
        nameTextField.setText(course.getName());
        generalObjectiveTextArea.setText(course.getGeneralObjective());
        topicsInterestTextField.setText(course.getTopicsInterest());
        studentsProfileTextField.setText(course.getStudentsProfile());
        additionalInformationTextArea.setText(course.getAdditionalInformation());
        statusTextField.setText(course.getStatus());
    }

    private void cleanFields() {
        nameTextField.setText("");
        generalObjectiveTextArea.setText("");
        topicsInterestTextField.setText("");
        numberStudentsComboBox.setValue(null);
        termComboBox.setValue(null);
        languageComboBox.setValue(null);
        studentsProfileTextField.setText("");
        additionalInformationTextArea.setText("");
    }

    private ArrayList<Integer> initializeNumberStudentsArrayForComboBox() {
        ArrayList<Integer> numberStudents = new ArrayList<>();
        for (int number = 1; number <= 50; number++) {
            numberStudents.add(number);
        }
        return numberStudents;
    }

    private ArrayList<Term> initializeTermsArrayForComboBox() throws DAOException {
        TermDAO termDAO = new TermDAO();
        ArrayList<Term> terms = new ArrayList<>();
        terms = termDAO.getTerms();
        return terms;
    }

    private ArrayList<Language> initializeLanguagesArrayForComboBox() throws DAOException {
        LanguageDAO languageDAO = new LanguageDAO();
        ArrayList<Language> languages = new ArrayList<>();
        languages = languageDAO.getLanguages();
        return languages;
    }

    private void changeComponentsEditabilityTrue() {
        saveButton.setVisible(true);
        cancelButton.setVisible(true);
        updateButton.setVisible(false);
        cancelCourseProposalButton.setVisible(false);

        nameTextField.setEditable(true);
        generalObjectiveTextArea.setEditable(true);
        topicsInterestTextField.setEditable(true);
        numberStudentsComboBox.setDisable(false);
        termComboBox.setDisable(false);
        languageComboBox.setDisable(false);
        studentsProfileTextField.setEditable(true);
        additionalInformationTextArea.setEditable(true);
    }

    private void changeComponentsEditabilityFalse() {
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
        updateButton.setVisible(true);
        cancelCourseProposalButton.setVisible(true);

        nameTextField.setEditable(false);
        generalObjectiveTextArea.setEditable(false);
        topicsInterestTextField.setEditable(false);
        numberStudentsComboBox.setDisable(true);
        termComboBox.setDisable(true);
        languageComboBox.setDisable(true);
        studentsProfileTextField.setEditable(false);
        additionalInformationTextArea.setEditable(false);
    }

    private boolean fieldsAreEmpty() {
        return nameTextField.getText().isEmpty()
                || generalObjectiveTextArea.getText().isEmpty()
                || topicsInterestTextField.getText().isEmpty()
                || numberStudentsComboBox.getValue() == null
                || termComboBox.getValue() == null
                || languageComboBox.getValue() == null
                || studentsProfileTextField.getText().isEmpty()
                || additionalInformationTextArea.getText().isEmpty();
    }

    private boolean emptyFieldsConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog("Campos vacios", "Debe completar todos los campos");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }

    private boolean wasUpdateConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog("Curso actualizado", "Los cambios fueron realizados con exito");
        return response.get() == DialogController.BUTTON_ACCEPT;
    }

    private boolean wasCanceledConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog("Curso cancelado", "Se cancelo el curso con éxito");
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
            Log.getLogger(CourseDetailsController.class).error(ioException.getMessage(), ioException);
        }
    }

    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getDialog(new AlertMessage(exception.getMessage(), Status.WARNING));
    }

}
