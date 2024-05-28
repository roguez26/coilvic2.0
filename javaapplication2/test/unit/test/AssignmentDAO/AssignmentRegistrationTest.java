package unit.test.AssignmentDAO;

import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import log.Log;
import mx.fei.coilvicapp.logic.assignment.Assignment;
import mx.fei.coilvicapp.logic.assignment.AssignmentDAO;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.course.Course;
import mx.fei.coilvicapp.logic.course.CourseDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.language.Language;
import mx.fei.coilvicapp.logic.language.LanguageDAO;
import mx.fei.coilvicapp.logic.modality.Modality;
import mx.fei.coilvicapp.logic.modality.ModalityDAO;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.professor.ProfessorDAO;
import mx.fei.coilvicapp.logic.term.Term;
import mx.fei.coilvicapp.logic.term.TermDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 *
 * @author ivanr
 */
public class AssignmentRegistrationTest {

    private static final LanguageDAO LANGUAGE_DAO = new LanguageDAO();
    private static final Language AUX_LANGUAGE = new Language();

    private static final TermDAO TERM_DAO = new TermDAO();
    private static final Term AUX_TERM = new Term();

    private static final ModalityDAO MODALITY_DAO = new ModalityDAO();
    private static final Modality AUX_MODALITY = new Modality();

    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country AUX_COUNTRY_ONE = new Country();
    private static final Country AUX_COUNTRY_TWO = new Country();

    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final University AUX_UNIVERSITY_ONE = new University();
    private static final University AUX_UNIVERSITY_TWO = new University();

    private static final ProfessorDAO PROFESSOR_DAO = new ProfessorDAO();
    private static final Professor AUX_PROFESSOR_ONE = new Professor();
    private static final Professor AUX_PROFESSOR_TWO = new Professor();

    private static final CourseDAO COURSE_DAO = new CourseDAO();
    private static final Course AUX_COURSE_ONE = new Course();
    private static final Course AUX_COURSE_TWO = new Course();

    private static final CollaborativeProjectDAO COLLABORATIVE_PROJECT_DAO = new CollaborativeProjectDAO();
    private static final CollaborativeProject AUX_COLLABORATIVE_PROJECT = new CollaborativeProject();

    private static final AssignmentDAO ASSIGNMENT_DAO = new AssignmentDAO();
    private static final Assignment ASSIGNMENT_FOR_TESTING = new Assignment();

    @Before
    public void setUp() {
        initializeCountries();
        initializeUniversities();
        initializeProfessors();
        initializeLanguage();
        initializeTerm();
        intializeCourses();
        initializeModality();
        initializeCollaborativeProject();
        initializeAssignment();
    }

    @Test
    public void registerAssignmentSuccess() {
        int idAssignment = 0;
        try {
            idAssignment = ASSIGNMENT_DAO.registerAssignment(ASSIGNMENT_FOR_TESTING, AUX_COLLABORATIVE_PROJECT);
            ASSIGNMENT_FOR_TESTING.setIdAssignment(idAssignment);
        } catch (DAOException exception) {
            Log.getLogger(AssignmentRegistrationTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(idAssignment > 0);
    }
    
    @Test
    public void registerAssignmentFailByInappropiateState() {
        int idAssignment = 0;
        AUX_COLLABORATIVE_PROJECT.setStatus("Rechazado");
        try {
            idAssignment = ASSIGNMENT_DAO.registerAssignment(ASSIGNMENT_FOR_TESTING, AUX_COLLABORATIVE_PROJECT);
            ASSIGNMENT_FOR_TESTING.setIdAssignment(idAssignment);
        } catch (DAOException exception) {
            Log.getLogger(AssignmentRegistrationTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(idAssignment > 0);
    }

//    @After
//    public void tearDown() {
//        try {
//            ASSIGNMENT_DAO.deleteAssignmentByIdAssignment(ASSIGNMENT_FOR_TESTING.getIdAssignment());
//            COLLABORATIVE_PROJECT_DAO.deleteCollaborativeProjectByidCollaborativeProject(AUX_COLLABORATIVE_PROJECT.getIdCollaborativeProject());
//            MODALITY_DAO.deleteModality(AUX_MODALITY.getIdModality());
//            COURSE_DAO.deleteCourseByIdCourse(AUX_COURSE_ONE.getIdCourse());
//            COURSE_DAO.deleteCourseByIdCourse(AUX_COURSE_TWO.getIdCourse());
//            LANGUAGE_DAO.deleteLanguage(AUX_LANGUAGE.getIdLanguage());
//            TERM_DAO.deleteTerm(AUX_TERM.getIdTerm());
//            PROFESSOR_DAO.deleteProfessorByID(AUX_PROFESSOR_ONE.getIdProfessor());
//            PROFESSOR_DAO.deleteProfessorByID(AUX_PROFESSOR_TWO.getIdProfessor());
//            UNIVERSITY_DAO.deleteUniversity(AUX_UNIVERSITY_ONE.getIdUniversity());
//            UNIVERSITY_DAO.deleteUniversity(AUX_UNIVERSITY_TWO.getIdUniversity());
//            COUNTRY_DAO.deleteCountry(AUX_COUNTRY_ONE.getIdCountry());
//            COUNTRY_DAO.deleteCountry(AUX_COUNTRY_TWO.getIdCountry());
//        } catch (DAOException exception) {
//            Log.getLogger(AssignmentRegistrationTest.class).error(exception.getMessage(), exception);
//        }
//    }
    
    private void initializeLanguage() {
        AUX_LANGUAGE.setName("Inglés");

        try {
            AUX_LANGUAGE.setIdLanguage(LANGUAGE_DAO.registerLanguage(AUX_LANGUAGE));
        } catch (DAOException exception) {
            Log.getLogger(AssignmentRegistrationTest.class).error(exception.getMessage(), exception);
        }
    }

    private void initializeTerm() {
        AUX_TERM.setName("Febrero2024-Junio2024");

        try {
            AUX_TERM.setIdTerm(TERM_DAO.registerTerm(AUX_TERM));
        } catch (DAOException exception) {
            Log.getLogger(AssignmentRegistrationTest.class).error(exception.getMessage(), exception);
        }
    }

    private void initializeModality() {
        AUX_MODALITY.setName("Clase espejo");

        try {
            AUX_MODALITY.setIdModality(MODALITY_DAO.registerModality(AUX_MODALITY));
        } catch (DAOException exception) {
            Log.getLogger(AssignmentRegistrationTest.class).error(exception.getMessage(), exception);
        }
    }

    private void initializeCountries() {
        AUX_COUNTRY_ONE.setName("México");
        AUX_COUNTRY_TWO.setName("Venezuela");

        try {
            AUX_COUNTRY_ONE.setIdCountry(COUNTRY_DAO.registerCountry(AUX_COUNTRY_ONE));
            AUX_COUNTRY_TWO.setIdCountry(COUNTRY_DAO.registerCountry(AUX_COUNTRY_TWO));
        } catch (DAOException exception) {
            Log.getLogger(AssignmentRegistrationTest.class).error(exception.getMessage(), exception);
        }
    }

    private void initializeUniversities() {
        AUX_UNIVERSITY_ONE.setName("Universidad Veracruzana");
        AUX_UNIVERSITY_ONE.setAcronym("UV");
        AUX_UNIVERSITY_ONE.setJurisdiction("Veracruz");
        AUX_UNIVERSITY_ONE.setCity("Xalapa");
        AUX_UNIVERSITY_ONE.setCountry(AUX_COUNTRY_ONE);

        AUX_UNIVERSITY_TWO.setName("Universidad Católica Andrés Bello");
        AUX_UNIVERSITY_TWO.setAcronym("UCAB");
        AUX_UNIVERSITY_TWO.setJurisdiction("Caracas");
        AUX_UNIVERSITY_TWO.setCity("Guayana");
        AUX_UNIVERSITY_TWO.setCountry(AUX_COUNTRY_TWO);
        try {
            AUX_UNIVERSITY_ONE.setIdUniversity(UNIVERSITY_DAO.registerUniversity(AUX_UNIVERSITY_ONE));
            AUX_UNIVERSITY_TWO.setIdUniversity(UNIVERSITY_DAO.registerUniversity(AUX_UNIVERSITY_TWO));
        } catch (DAOException exception) {
            Log.getLogger(AssignmentRegistrationTest.class).error(exception.getMessage(), exception);
        }
    }

    private void initializeProfessors() {
        AUX_PROFESSOR_ONE.setName("Maria");
        AUX_PROFESSOR_ONE.setPaternalSurname("Arenas");
        AUX_PROFESSOR_ONE.setMaternalSurname("Valdes");
        AUX_PROFESSOR_ONE.setEmail("aaren@uv.mx");
        AUX_PROFESSOR_ONE.setGender("Mujer");
        AUX_PROFESSOR_ONE.setPhoneNumber("1234567890");
        AUX_PROFESSOR_ONE.setUniversity(AUX_UNIVERSITY_ONE);

        AUX_PROFESSOR_TWO.setName("Jorge");
        AUX_PROFESSOR_TWO.setPaternalSurname("Ocharan");
        AUX_PROFESSOR_TWO.setMaternalSurname("Hernandez");
        AUX_PROFESSOR_TWO.setEmail("jochar@uv.mx");
        AUX_PROFESSOR_TWO.setGender("Hombre");
        AUX_PROFESSOR_TWO.setPhoneNumber("1234567890");
        AUX_PROFESSOR_TWO.setUniversity(AUX_UNIVERSITY_TWO);
        try {
            AUX_PROFESSOR_ONE.setIdProfessor(PROFESSOR_DAO.registerProfessor(AUX_PROFESSOR_ONE));
            AUX_PROFESSOR_TWO.setIdProfessor(PROFESSOR_DAO.registerProfessor(AUX_PROFESSOR_TWO));
        } catch (DAOException exception) {
            Log.getLogger(AssignmentRegistrationTest.class).error(exception.getMessage(), exception);
        }
    }

    private void intializeCourses() {
        AUX_COURSE_ONE.setName("Programacion");
        AUX_COURSE_ONE.setGeneralObjective("Los alumnos comprendan la"
                + " programacion estructurada y orientada a objetos");
        AUX_COURSE_ONE.setTopicsInterest("Abstraccion, Herencia, Polimorfismo");
        AUX_COURSE_ONE.setNumberStudents(35);
        AUX_COURSE_ONE.setStudentsProfile("Ingenieria de software");
         AUX_COURSE_ONE.setTerm(AUX_TERM);
         AUX_COURSE_ONE.setLanguage(AUX_LANGUAGE);
        AUX_COURSE_ONE.setAdditionalInformation("Ademas de la programacion"
                + " orientada a objetos veremos el paradigma funcional");
        AUX_COURSE_ONE.setProfessor(AUX_PROFESSOR_ONE);

        AUX_COURSE_TWO.setName("Bases de Datos");
        AUX_COURSE_TWO.setGeneralObjective("Los alumnos comprendan el diseño, "
                + "implementación y administración de bases de datos relacionales.");
        AUX_COURSE_TWO.setTopicsInterest("Modelado de datos, SQL, Normalización");
        AUX_COURSE_TWO.setNumberStudents(40);
        AUX_COURSE_TWO.setStudentsProfile("Ingeniería en Sistemas Computacionales");
         AUX_COURSE_TWO.setTerm(AUX_TERM);
          AUX_COURSE_TWO.setLanguage(AUX_LANGUAGE);
        AUX_COURSE_TWO.setAdditionalInformation("Además de bases de datos relacionales, "
                + "se introducirá a bases de datos NoSQL y técnicas avanzadas de optimización.");
        AUX_COURSE_TWO.setProfessor(AUX_PROFESSOR_TWO);
        try {
             AUX_COURSE_TWO.setIdCourse(COURSE_DAO.registerCourse(AUX_COURSE_TWO));
            AUX_COURSE_ONE.setIdCourse(COURSE_DAO.registerCourse(AUX_COURSE_ONE));
        } catch (DAOException exception) {
            Log.getLogger(AssignmentRegistrationTest.class).error(exception.getMessage(), exception);
        }
    }

    private void initializeCollaborativeProject() {
        AUX_COLLABORATIVE_PROJECT.setName("Programación y Bases de Datos");
        AUX_COLLABORATIVE_PROJECT.setStatus("Aceptado");
        AUX_COLLABORATIVE_PROJECT.setDescription("Este proyecto combina los conocimientos de programación orientada a objetos y bases de datos "
                + "para desarrollar una aplicación completa que gestione información de manera eficiente.");
        AUX_COLLABORATIVE_PROJECT.setGeneralObjective("Integrar conceptos de programación y bases de datos para desarrollar una solución software completa.");
        AUX_COLLABORATIVE_PROJECT.setModality(AUX_MODALITY);
        AUX_COLLABORATIVE_PROJECT.setCode("PROG-BASDAT-2024");
        AUX_COLLABORATIVE_PROJECT.setSyllabusPath("/syllabus/proyecto_integrador.pdf");
        AUX_COLLABORATIVE_PROJECT.setRequestedCourse(AUX_COURSE_ONE);
        AUX_COLLABORATIVE_PROJECT.setRequesterCourse(AUX_COURSE_TWO);
        try {
            AUX_COLLABORATIVE_PROJECT.setIdCollaborativeProject(COLLABORATIVE_PROJECT_DAO.registerCollaborativeProject(AUX_COLLABORATIVE_PROJECT));
        } catch (DAOException exception) {
            Log.getLogger(AssignmentRegistrationTest.class).error(exception.getMessage(), exception);
        }
    }

    private void initializeAssignment() {
        ASSIGNMENT_FOR_TESTING.setName("Rompehielos");
        ASSIGNMENT_FOR_TESTING.setDescription("Actividad donde se presentaron los alumnos de Programación y los de Bases de Datos");
        ASSIGNMENT_FOR_TESTING.setPath("/files/id/Rompehielos.pdf");
        ASSIGNMENT_FOR_TESTING.setIdColaborativeProject(AUX_COLLABORATIVE_PROJECT.getIdCollaborativeProject());
    }
}
