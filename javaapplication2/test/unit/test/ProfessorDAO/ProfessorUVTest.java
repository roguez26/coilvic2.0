package unit.test.ProfessorDAO;

import log.Log;
import mx.fei.coilvicapp.logic.academicarea.AcademicArea;
import mx.fei.coilvicapp.logic.academicarea.AcademicAreaDAO;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.hiringcategory.HiringCategory;
import mx.fei.coilvicapp.logic.hiringcategory.HiringCategoryDAO;
import mx.fei.coilvicapp.logic.hiringtype.HiringType;
import mx.fei.coilvicapp.logic.hiringtype.HiringTypeDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.professor.ProfessorDAO;
import mx.fei.coilvicapp.logic.professor.ProfessorUV;
import mx.fei.coilvicapp.logic.region.Region;
import mx.fei.coilvicapp.logic.region.RegionDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class ProfessorUVTest {
    
    private static final ProfessorUV TEST_PROFESSOR = new ProfessorUV();
    private static final ProfessorUV AUX_TEST_PROFESSOR = new ProfessorUV();
    private static final ProfessorDAO PROFESSOR_DAO = new ProfessorDAO();
    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final AcademicAreaDAO ACADEMIC_AREA_DAO = new AcademicAreaDAO();
    private static final HiringCategoryDAO HIRING_CATEGORY_DAO = new HiringCategoryDAO();
    private static final HiringTypeDAO HIRING_TYPE_DAO = new HiringTypeDAO();
    private static final RegionDAO REGION_DAO = new RegionDAO();
    
    @Before
    public void setUp() {
        initializeTestProfessorUV();
        try {
            int idProfessorUV = PROFESSOR_DAO.registerProfessorUV(TEST_PROFESSOR);
            TEST_PROFESSOR.setIdProfessor(idProfessorUV);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorUVTest.class).error(exception.getMessage(), exception);
        }
    }
    
    @After 
    public void tearDown(){
        int idProfessorUV = TEST_PROFESSOR.getIdProfessor();
        int idUniversity = TEST_PROFESSOR.getIdUniversity();
        int idCountry = TEST_PROFESSOR.getUniversity().getIdCountry();
        int idAcademicArea = TEST_PROFESSOR.getIdAcademicArea();
        int idRegion = TEST_PROFESSOR.getIdRegion();
        int idHiringType = TEST_PROFESSOR.getIdHiringType();
        int idHiringCategory = TEST_PROFESSOR.getIdHiringCategory();
        
        try {
            PROFESSOR_DAO.deleteProfessorUVByID(idProfessorUV);
            UNIVERSITY_DAO.deleteUniversity(idUniversity);
            COUNTRY_DAO.deleteCountry(idCountry);
            ACADEMIC_AREA_DAO.deleteAcademicArea(idAcademicArea);
            REGION_DAO.deleteRegion(idRegion);
            HIRING_TYPE_DAO.deleteHiringType(idHiringType);
            HIRING_CATEGORY_DAO.deleteHiringCategory(idHiringCategory);            
        } catch (DAOException exception) {
            Log.getLogger(ProfessorUVTest.class).error(exception.getMessage(), exception);
        }                
    }     
    
    @Test
    public void testSuccessRegisterProfessorUV() {
        int idProfessorUV = 0;
        initializeAuxTestProfessorUV();
        try {          
            idProfessorUV = PROFESSOR_DAO.registerProfessorUV(AUX_TEST_PROFESSOR);
            PROFESSOR_DAO.deleteProfessorUVByID(idProfessorUV);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorUVTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(idProfessorUV > 0);
    }
    
    private Country initializeCountry() {
        Country country = new Country();
        
        country.setName("Mexico");
        try {
            int idCountry = COUNTRY_DAO.registerCountry(country);
            country.setIdCountry(idCountry);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorUVTest.class).error(exception.getMessage(), exception);
        }
        return country;
    }
    
    private AcademicArea initializeAcademicArea() {
        AcademicArea academicArea = new AcademicArea();
        
        academicArea.setName("Economico Administrativo");
        try {
            int idAcademicArea = ACADEMIC_AREA_DAO.registerAcademicArea(academicArea);
            academicArea.setIdAreaAcademica(idAcademicArea);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorUVTest.class).error(exception.getMessage(), exception);
        }
        return academicArea;
    }
    
    private HiringCategory initializeHiringCategory() {
        HiringCategory hiringCategory = new HiringCategory();
        
        hiringCategory.setName("Docente TC");
        try {
            int idHiringCategory = HIRING_CATEGORY_DAO.registerHiringCategory(hiringCategory);
            hiringCategory.setIdHiringCategory(idHiringCategory);
            System.out.println(idHiringCategory + "inicial");
        } catch (DAOException exception) {
            Log.getLogger(ProfessorUVTest.class).error(exception.getMessage(), exception);
        }
        return hiringCategory;
    }  
    
    private HiringType initializeHiringType() {
        HiringType hiringType = new HiringType();
        
        hiringType.setName("Planta");
        try {
            int iHiringType = HIRING_TYPE_DAO.registerHiringType(hiringType);
            hiringType.setIdHiringType(iHiringType);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorUVTest.class).error(exception.getMessage(), exception);
        }
        return hiringType;
    }  

    private Region initializeRegion() {
        Region region = new Region();
        
        region.setName("Xalapa");
        try {
            int idRegion = REGION_DAO.registerRegion(region);
            region.setIdRegion(idRegion);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorUVTest.class).error(exception.getMessage(), exception);
        }
        return region;
    }      
    
    private University initializeUniversity() {
        University university = new University();
        
        university.setName("Universidad Veracruzana");
        university.setAcronym("UV");
        university.setJurisdiction("Veracruz");
        university.setCity("Xalapa");
        try {
            university.setCountry(initializeCountry());
            int idUniversity = UNIVERSITY_DAO.registerUniversity(university);
            university.setIdUniversity(idUniversity);
        } catch (DAOException exception) {
            Log.getLogger(ProfessorUVTest.class).error(exception.getMessage(), exception);
        }
        return university;
    }
    
    private void initializeTestProfessorUV() {        
        TEST_PROFESSOR.setName("Maria");
        TEST_PROFESSOR.setPaternalSurname("Arenas");
        TEST_PROFESSOR.setMaternalSurname("Valdes");
        TEST_PROFESSOR.setEmail("aaren@uv.mx");
        TEST_PROFESSOR.setGender("Mujer");
        TEST_PROFESSOR.setPhoneNumber("1234567890");
        TEST_PROFESSOR.setUniversity(initializeUniversity());
        TEST_PROFESSOR.setAcademicArea(initializeAcademicArea());
        TEST_PROFESSOR.setHiringCategory(initializeHiringCategory());
        TEST_PROFESSOR.setHiringType(initializeHiringType());
        TEST_PROFESSOR.setRegion(initializeRegion());
        TEST_PROFESSOR.setPersonalNumber(10);  
        System.out.println(TEST_PROFESSOR.toString() + "test");
    }
    
    private void initializeAuxTestProfessorUV() {        
        AUX_TEST_PROFESSOR.setName("Jorge");
        AUX_TEST_PROFESSOR.setPaternalSurname("Ocharan");
        AUX_TEST_PROFESSOR.setMaternalSurname("Hernandez");
        AUX_TEST_PROFESSOR.setEmail("jochar@uv.mx");
        AUX_TEST_PROFESSOR.setGender("Hombre");
        AUX_TEST_PROFESSOR.setPhoneNumber("1234567890");
        AUX_TEST_PROFESSOR.setUniversity(TEST_PROFESSOR.getUniversity());
        AUX_TEST_PROFESSOR.setAcademicArea(TEST_PROFESSOR.getAcademicArea());
        AUX_TEST_PROFESSOR.setHiringCategory(TEST_PROFESSOR.getHiringCategory());
        AUX_TEST_PROFESSOR.setHiringType(TEST_PROFESSOR.getHiringType());
        AUX_TEST_PROFESSOR.setRegion(TEST_PROFESSOR.getRegion());
        AUX_TEST_PROFESSOR.setPersonalNumber(11);
        System.out.println(TEST_PROFESSOR.toString() + "aux");
    }
}
