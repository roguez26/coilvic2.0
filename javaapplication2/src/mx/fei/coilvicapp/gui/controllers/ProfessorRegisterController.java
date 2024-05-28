package mx.fei.coilvicapp.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.professor.ProfessorDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;
import java.io.IOException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import log.Log;
import main.MainApp;
import mx.fei.coilvicapp.logic.academicarea.AcademicArea;
import mx.fei.coilvicapp.logic.academicarea.AcademicAreaDAO;
import mx.fei.coilvicapp.logic.hiringcategory.HiringCategory;
import mx.fei.coilvicapp.logic.hiringcategory.HiringCategoryDAO;
import mx.fei.coilvicapp.logic.hiringtype.HiringType;
import mx.fei.coilvicapp.logic.hiringtype.HiringTypeDAO;
import mx.fei.coilvicapp.logic.implementations.XLSXCreator;
import mx.fei.coilvicapp.logic.professor.ProfessorUV;
import mx.fei.coilvicapp.logic.region.Region;
import mx.fei.coilvicapp.logic.region.RegionDAO;

public class ProfessorRegisterController implements Initializable { 
    
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField paternalSurnameTextField;

    @FXML
    private TextField maternalSurnameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private ComboBox<University> universitiesComboBox;
    
    @FXML
    private ComboBox<AcademicArea> academicAreasComboBox;

    @FXML
    private Label academicAreaLabel;    
    
    @FXML
    private ComboBox<HiringCategory> hiringCategoriesComboBox;

    @FXML
    private Label hiringCategoryLabel;    
    
    @FXML
    private ComboBox<HiringType> hiringTypesComboBox;
    
    @FXML
    private ComboBox<Region> regionsComboBox;

    @FXML
    private Label regionLabel;    

    @FXML
    private Label hiringTypeLabel;    
    
    @FXML
    private Label uvPersonalNumberLabel;

    @FXML
    private TextField uvPersonalNumberTextField;    
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button acceptButton;
    
    private Stage stage;
    private final ProfessorDAO PROFESSOR_DAO = new ProfessorDAO();
    private final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private final AcademicAreaDAO ACADEMIC_AREA_DAO = new AcademicAreaDAO();
    private final HiringTypeDAO HIRING_TYPE_DAO = new HiringTypeDAO();
    private final HiringCategoryDAO HIRING_CATEGORY_DAO = new HiringCategoryDAO();
    private final RegionDAO REGION_DAO = new RegionDAO();   


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderComboBox.setItems(FXCollections.observableArrayList(initializeGendersArrayForComboBox()));
        universitiesComboBox.setItems(FXCollections.observableArrayList(initializeUniversitiesArrayForComboBox()));     
        academicAreasComboBox.setItems(FXCollections.observableArrayList(initializeAcademicAreasArrayForComboBox()));
        hiringTypesComboBox.setItems(FXCollections.observableArrayList(initializeHiringTypesArrayForComboBox()));
        hiringCategoriesComboBox.setItems(FXCollections.observableArrayList(initializeHiringCategoriesArrayForComboBox()));
        regionsComboBox.setItems(FXCollections.observableArrayList(initializeRegionsArrayForComboBox()));                
    }       
    
    private ArrayList<String> initializeGendersArrayForComboBox() {
        ArrayList<String> genders = new ArrayList<>();
        genders.add("Hombre");
        genders.add("Mujer");
        genders.add("Otro");
        return genders;
    }    
    
    @FXML
    private void cancelButtonIsPressed(ActionEvent event) {
        if (confirmCancelation()) {
            try {
                changeWindowHeight(700);
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/LoginParticipant");
            } catch (IOException exception) {
                Log.getLogger(ProfessorRegisterController.class).error(exception.getMessage(), exception);
            }
        }
    }
       
    @FXML
    private void acceptButtonIsPressed (ActionEvent event) {
        try {
            invokeProfessorRegistration();
        } catch (IllegalArgumentException exception) {
            handleValidationException(exception);
        } catch (DAOException exception) {
            handleDAOException(exception);
        } catch (IOException exception) {
            Log.getLogger(ProfessorRegisterController.class).error(exception.getMessage(), exception);
        } 
    }

    @FXML
    private void universidadVeracruzanaIsSelected(ActionEvent event) {
        if ("Universidad Veracruzana".equals(universitiesComboBox.getValue().getName())) {
            unhideProfessorUvComponents();
        } else {
            hideProfessorUvComponents();
        }
    }    
    
    private boolean confirmCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cancelacion", "¿Deseas cancelar el registro?");
        return (response.get() == DialogController.BUTTON_YES);
    }    
    
    private void invokeProfessorRegistration() throws DAOException, IOException{
        int idRegisteredProfessor;
        if (!fieldsAreEmpty()) {
            Professor professor = initializeProfessor();
            if ("Universidad Veracruzana".equals(universitiesComboBox.getValue().getName())) {
                ProfessorUV professorUV = initializeProfessorUV(professor);
                idRegisteredProfessor = PROFESSOR_DAO.registerProfessorUV(professorUV);
            } else {
                idRegisteredProfessor = PROFESSOR_DAO.registerProfessor(professor);
            }
            if(idRegisteredProfessor > 0) {
                DialogController.getInformativeConfirmationDialog("Registrado",
                        "Se ha registrado su información con éxito, se le notificara cuando se haya validado su información");
                MainApp.changeView("/mx/fei/coilvicapp/gui/views/LoginParticipant");
            }
        } else {
            DialogController.getInformativeConfirmationDialog(
                    "Campos vacios","Asegurese de llenar todos los campos con *");
        }
    }
    
    private boolean fieldsAreEmpty() {
        boolean emptyFieldsCheck = false;
        if (nameTextField.getText().isEmpty() || 
                paternalSurnameTextField.getText().isEmpty() ||
                emailTextField.getText().isEmpty() ||
                genderComboBox.getValue() == null ||
                phoneNumberTextField.getText().isEmpty() ||
                universitiesComboBox.getValue() == null) {
            emptyFieldsCheck = true;
        }
        
        if ("Universidad Veracruzana".equals(universitiesComboBox.getValue().getName())) {
            if (uvPersonalNumberLabel.getText().isEmpty() || 
                    regionsComboBox.getValue() == null ||
                    academicAreasComboBox.getValue() == null ||
                    hiringCategoriesComboBox == null ||
                    hiringTypesComboBox.getValue() == null) {
                emptyFieldsCheck = true;
            }
        }

        return emptyFieldsCheck;
    }
    
    private Professor initializeProfessor() throws DAOException{
        Professor professor = new Professor();
        professor.setName(nameTextField.getText());
        professor.setPaternalSurname(paternalSurnameTextField.getText());
        professor.setMaternalSurname(maternalSurnameTextField.getText());
        professor.setEmail(emailTextField.getText());
        professor.setGender(genderComboBox.getValue());
        professor.setPhoneNumber(phoneNumberTextField.getText());
        professor.setUniversity(universitiesComboBox.getValue());
        return professor;
    }
    
    private ProfessorUV initializeProfessorUV(Professor professor) throws DAOException {
        ProfessorUV professorUV = new ProfessorUV();
        professor = initializeProfessor();
        professorUV.setName(professor.getName());
        professorUV.setPaternalSurname(professor.getPaternalSurname());
        professorUV.setMaternalSurname(professor.getMaternalSurname());
        professorUV.setEmail(professor.getEmail());
        professorUV.setGender(professor.getGender());
        professorUV.setPhoneNumber(professor.getPhoneNumber());
        professorUV.setState(professor.getState());
        professorUV.setUniversity(professor.getUniversity());
        professorUV.setPersonalNumber(Integer.parseInt(uvPersonalNumberTextField.getText()));
        professorUV.setAcademicArea(academicAreasComboBox.getValue());
        professorUV.setRegion(regionsComboBox.getValue());
        professorUV.setHiringCategory(hiringCategoriesComboBox.getValue());
        professorUV.setHiringType(hiringTypesComboBox.getValue());      
        return professorUV;
    }
    
    private void hideProfessorUvComponents() {   
        uvPersonalNumberLabel.setVisible(false);
        academicAreaLabel.setVisible(false);
        hiringCategoryLabel.setVisible(false);
        regionLabel.setVisible(false);
        hiringTypeLabel.setVisible(false);          
        uvPersonalNumberTextField.setVisible(false);
        academicAreasComboBox.setVisible(false);
        hiringCategoriesComboBox.setVisible(false);
        hiringTypesComboBox.setVisible(false);
        regionsComboBox.setVisible(false);        
        uvPersonalNumberLabel.setManaged(false);
        academicAreaLabel.setManaged(false);
        hiringCategoryLabel.setManaged(false);
        regionLabel.setManaged(false);
        hiringTypeLabel.setManaged(false);        
        uvPersonalNumberTextField.setManaged(false);
        academicAreasComboBox.setManaged(false);
        hiringCategoriesComboBox.setManaged(false);
        hiringTypesComboBox.setManaged(false);
        regionsComboBox.setManaged(false);        
        changeWindowHeight(700);        
    }
    
    private void unhideProfessorUvComponents() {
        uvPersonalNumberLabel.setVisible(true);
        academicAreaLabel.setVisible(true);
        hiringCategoryLabel.setVisible(true);
        regionLabel.setVisible(true);
        hiringTypeLabel.setVisible(true);            
        uvPersonalNumberTextField.setVisible(true);
        academicAreasComboBox.setVisible(true);
        hiringCategoriesComboBox.setVisible(true);
        hiringTypesComboBox.setVisible(true);
        regionsComboBox.setVisible(true);        
        uvPersonalNumberLabel.setManaged(true);
        academicAreaLabel.setManaged(true);
        hiringCategoryLabel.setManaged(true);
        regionLabel.setManaged(true);
        hiringTypeLabel.setManaged(true);            
        uvPersonalNumberTextField.setManaged(true);
        academicAreasComboBox.setManaged(true);
        hiringCategoriesComboBox.setManaged(true);
        hiringTypesComboBox.setManaged(true);
        regionsComboBox.setManaged(true);
        changeWindowHeight(750);        
    }    
    
    private void changeWindowHeight(int height) {
        stage = (Stage) acceptButton.getScene().getWindow();        
        stage.setHeight(height);
    }    
    
    private ArrayList<University> initializeUniversitiesArrayForComboBox() {
        ArrayList<University> universities = new ArrayList<>();
        try {
            universities = UNIVERSITY_DAO.getAllUniversities();
        } catch(DAOException exception) {
            Log.getLogger(ProfessorRegisterController.class).error(exception.getMessage(), exception);
        }
        return universities;
    }         
    
    private ArrayList<AcademicArea> initializeAcademicAreasArrayForComboBox() {
        ArrayList<AcademicArea> academicAreas = new ArrayList<>();
        try {
            academicAreas = ACADEMIC_AREA_DAO.getAcademicAreas();
        } catch (DAOException exception) {
            Log.getLogger(ProfessorRegisterController.class).error(exception.getMessage(), exception);
        }
        return academicAreas;
    }
    
    private ArrayList<HiringType> initializeHiringTypesArrayForComboBox() {
        ArrayList<HiringType> hiringTypes = new ArrayList<>();
        try {
            hiringTypes = HIRING_TYPE_DAO.getHiringTypes();
        } catch (DAOException exception) {
            Log.getLogger(ProfessorRegisterController.class).error(exception.getMessage(), exception);
        }
        return hiringTypes;
    }
    
    private ArrayList<HiringCategory> initializeHiringCategoriesArrayForComboBox() {
        ArrayList<HiringCategory> hiringCategories = new ArrayList<>();
        try {
            hiringCategories = HIRING_CATEGORY_DAO.getHiringCategories();
        } catch (DAOException exception) {
            Log.getLogger(ProfessorRegisterController.class).error(exception.getMessage(), exception);
        }
        return hiringCategories;
    }
    
    private ArrayList<Region> initializeRegionsArrayForComboBox() {
        ArrayList<Region> regions = new ArrayList<>();
        try {
            regions = REGION_DAO.getRegions();
        } catch (DAOException exception) {
            Log.getLogger(ProfessorRegisterController.class).error(exception.getMessage(), exception);
        }
        return regions;
    }  
    
    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage (exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR -> MainApp.changeView("/mx/fei/coilvicapp/gui/views/MainApp");
                case FATAL -> MainApp.changeView("/main/MainApp");
                
            }
        } catch (IOException ioException) {
            Log.getLogger(ProfessorRegisterController.class).error(exception.getMessage(), exception);
        }
    }
    
    private void handleValidationException(IllegalArgumentException ex) {
        DialogController.getDialog(new AlertMessage( ex.getMessage(), Status.WARNING));
    }

}
