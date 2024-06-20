package unit.test.CourseDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.course.*;
import mx.fei.coilvicapp.logic.professor.*;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.language.*;
import mx.fei.coilvicapp.logic.term.*;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class CourseTest {
    
    private static final CourseDAO COURSE_DAO = new CourseDAO();
    private static final Course COURSE_FOR_TESTING = new Course();
    
    private static final Course AUX_COURSE = new Course();
    
    private static final ProfessorDAO PROFESSOR_DAO = new ProfessorDAO();
    private static final Professor AUX_PROFESSOR = new Professor();
    
    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final University AUX_UNIVERSITY = new University();
    
    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country AUX_COUNTRY = new Country();
    
    private static final LanguageDAO LANGUAGE_DAO = new LanguageDAO();
    private static final Language AUX_LANGUAGE = new Language();
    
    private static final TermDAO TERM_DAO = new TermDAO();
    private static final Term AUX_TERM = new Term();
    
    @Before
    public void setUp() throws DAOException {        
        int idProfessor = 0;
        int idCountry = 0;
        int idUniversity = 0;
        int idLanguage = 0;
        int idTerm = 0;
        
        initializeAuxiliarProfessor();
        intitliazeAuxiliarUniversity();
        initializeAuxiliarCountry();
        initializeAuxiliarLanguage();
        initializeAuxiliarTerm();
        intializeCourse();
        intializeAuxiliaryCourse();
        
        try {
            idCountry = COUNTRY_DAO.registerCountry(AUX_COUNTRY);
            AUX_UNIVERSITY.setIdCountry(idCountry);
            idUniversity = UNIVERSITY_DAO.registerUniversity(AUX_UNIVERSITY);
            AUX_PROFESSOR.setIdUniversity(idUniversity);
            idProfessor = PROFESSOR_DAO.registerProfessor(AUX_PROFESSOR);
            idLanguage = LANGUAGE_DAO.registerLanguage(AUX_LANGUAGE);
            idTerm = TERM_DAO.registerTerm(AUX_TERM);                                          
        } catch (DAOException exception) {
            Logger.getLogger(CourseTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        AUX_COUNTRY.setIdCountry(idCountry);
        AUX_UNIVERSITY.setIdUniversity(idUniversity);
        AUX_PROFESSOR.setIdProfessor(idProfessor);
        AUX_LANGUAGE.setIdLanguage(idLanguage);
        AUX_TERM.setIdTerm(idTerm); 
        AUX_COURSE.setIdCourse(registerAuxiliaryCourse());
    }
    
    private void initializeAuxiliarProfessor() {
        AUX_PROFESSOR.setName("Juan Carlos");
        AUX_PROFESSOR.setPaternalSurname("Perez");
        AUX_PROFESSOR.setMaternalSurname("Arriaga");
        AUX_PROFESSOR.setEmail("elRebo@gmail.com");
        AUX_PROFESSOR.setGender("Masculino");
        AUX_PROFESSOR.setPhoneNumber("2286722098");
        AUX_PROFESSOR.setIdUniversity(1);
    }
    
    private void intitliazeAuxiliarUniversity() {
        AUX_UNIVERSITY.setName("Universidad Católica Andrés Bello");
        AUX_UNIVERSITY.setAcronym("UCAB");
        AUX_UNIVERSITY.setJurisdiction("Caracas");
        AUX_UNIVERSITY.setCity("Guayana");
    }
    
    private void initializeAuxiliarCountry() {
        AUX_COUNTRY.setName("Mexico");
    }
    
    private void initializeAuxiliarLanguage() {
        AUX_LANGUAGE.setName("Inglés");
    }
    
    private void initializeAuxiliarTerm() {
        AUX_TERM.setName("Agosto2024-Enero2025");       
    }
    
    private void intializeCourse() {
        COURSE_FOR_TESTING.setProfessor(AUX_PROFESSOR);
        COURSE_FOR_TESTING.setLanguage(AUX_LANGUAGE);
        COURSE_FOR_TESTING.setTerm(AUX_TERM);
        COURSE_FOR_TESTING.setName("Programacion");
        COURSE_FOR_TESTING.setGeneralObjective("Los alumnos comprenden la"
        + " programacion estructurada y orientada a objetos");
        COURSE_FOR_TESTING.setTopicsInterest("Abstraccion, Herencia, Polimorfismo");
        COURSE_FOR_TESTING.setNumberStudents(35);
        COURSE_FOR_TESTING.setStudentsProfile("Ingenieria de software");                              
        COURSE_FOR_TESTING.setAdditionalInformation("Ademas de la progrmacion"
        + " orientada a objetos veremos el paradigma funcional");
    }
    
    private void intializeAuxiliaryCourse() {
        AUX_COURSE.setProfessor(AUX_PROFESSOR);
        AUX_COURSE.setLanguage(AUX_LANGUAGE);
        AUX_COURSE.setTerm(AUX_TERM);
        AUX_COURSE.setName("Principios de Diseño de Software");
        AUX_COURSE.setGeneralObjective("Los alumnos obtienen saberes"
        + " tanto teoricos y practicos sobre el diseño de software");
        AUX_COURSE.setTopicsInterest("Patrones de diseño,"
        + " Metodologias de desarrollo de software, ICONIX ");
        AUX_COURSE.setNumberStudents(40);
        AUX_COURSE.setStudentsProfile("Ingenieria de software");                              
        AUX_COURSE.setAdditionalInformation("Trabajo de forma presencial"
        + " y combino el empleo de material didáctico digital, asi como actividades"
        + " en la plataforma institucional de la Universidad Veracruzana: Eminus");
    }
    
    private int registerAuxiliaryCourse() {
        int idAuxCourse = 0;
        try {
            idAuxCourse = COURSE_DAO.registerCourse(AUX_COURSE); 
        } catch (DAOException exception) {
            Logger.getLogger(CourseTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        return idAuxCourse;
    }
    
    @Test
    public void testRegisterCourseSucces() {
        int result = 0;
        
        try {
            result = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
            COURSE_FOR_TESTING.setIdCourse(result);
        } catch (DAOException exception) {
            Logger.getLogger(CourseTest.class.getName()).log(Level.SEVERE, null, exception);
        }        
        assertTrue(result > 0);     
    }
    
    @Test
    public void testRegisterCourseFailByDuplicateCourse() {
        int result = 0;
        COURSE_FOR_TESTING.setName(AUX_COURSE.getName());    
        try {
            result = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);            
        } catch (DAOException exception) {
            Logger.getLogger(CourseTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        COURSE_FOR_TESTING.setIdCourse(result);
        assertTrue(result > 0);
    }
    
    @Test
    public void testRegisterCourseFailByNonExistenceLanguage() {
        int result = 0;
        int idLanguage;
        
        idLanguage = COURSE_FOR_TESTING.getLanguage().getIdLanguage();
        COURSE_FOR_TESTING.getLanguage().setIdLanguage(-1);
        try {
            result = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);            
        } catch(DAOException exception) {
            Logger.getLogger(CourseTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }        
        COURSE_FOR_TESTING.setIdCourse(result);        
        COURSE_FOR_TESTING.getLanguage().setIdLanguage(idLanguage);
        assertTrue(result > 0);
    }
    
    @Test
    public void testRegisterCourseFailByNonExistenceTerm() {
        int result = 0;
        int idTerm;
        
        idTerm = COURSE_FOR_TESTING.getTerm().getIdTerm();
        COURSE_FOR_TESTING.getTerm().setIdTerm(-1);        
        try {
            result = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);            
        } catch(DAOException exception) {
            Logger.getLogger(CourseTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }        
        COURSE_FOR_TESTING.setIdCourse(result);
        COURSE_FOR_TESTING.getTerm().setIdTerm(idTerm);
        assertTrue(result > 0);
    }
    
    /*@After
    public void tearDown() {
        try {
            COURSE_DAO.deleteCourseByIdCourse(COURSE_FOR_TESTING.getIdCourse());
            COURSE_DAO.deleteCourseByIdCourse(AUX_COURSE.getIdCourse());            
            PROFESSOR_DAO.deleteProfessorByID(AUX_PROFESSOR.getIdProfessor());
            UNIVERSITY_DAO.deleteUniversity(AUX_UNIVERSITY.getIdUniversity());
            COUNTRY_DAO.deleteCountry(AUX_COUNTRY.getIdCountry());
            LANGUAGE_DAO.deleteLanguage(AUX_LANGUAGE.getIdLanguage());
            TERM_DAO.deleteTerm(AUX_TERM.getIdTerm());
        } catch (DAOException exception) {
            Logger.getLogger(CourseDAORegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }*/
}
