package unit.test.CourseDAO;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.course.*;
import unit.test.Initializer.TestHelper;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CourseTest {
        
    private static final Course COURSE_FOR_TESTING = new Course();
    private static final ArrayList<Course> COURSES_FOR_TESTING = new ArrayList<>();
    private static final CourseDAO COURSE_DAO = new CourseDAO();
    private final TestHelper testHelper = new TestHelper();
    
    public void setUp() {
        testHelper.initializeProfessors();
        testHelper.initializeLanguage();
        testHelper.initializeTerm();
        initializeCourse();
        
    }
    
    public void initializeCourse() {
        COURSE_FOR_TESTING.setName("Introduccion a la Programacion");
        COURSE_FOR_TESTING.setGeneralObjective("Los alumnos comprendan la"
        + " programacion estructurada y orientada a objetos");
        COURSE_FOR_TESTING.setTopicsInterest("Abstraccion, Herencia, Polimorfismo");
        COURSE_FOR_TESTING.setNumberStudents(35);
        COURSE_FOR_TESTING.setStudentsProfile("Ingenieria de software");
        COURSE_FOR_TESTING.setLanguage(testHelper.getLanguage());
        COURSE_FOR_TESTING.setTerm(testHelper.getTerm());        
        COURSE_FOR_TESTING.setAdditionalInformation("Ademas de la programacion"
        + " orientada a objetos veremos el paradigma funcional");
        COURSE_FOR_TESTING.setProfessor(testHelper.getProfessorOne());
    }
    
    public void initializeCourses() throws DAOException {
        setUp();
        Course course = new Course();
        int idCourse;
        course.setName("Análisis de Datos");
        course.setGeneralObjective("Que los estudiantes aprendan a analizar y visualizar datos utilizando herramientas modernas.");
        course.setTopicsInterest("Estadística, Visualización de datos, Machine Learning");
        course.setNumberStudents(40);
        course.setStudentsProfile("Ciencia de Datos");
        course.setLanguage(testHelper.getLanguage());
        course.setTerm(testHelper.getTerm());
        course.setAdditionalInformation("El curso también cubrirá temas avanzados de machine learning y técnicas de big data.");
        course.setProfessor(testHelper.getProfessorOne());
        COURSES_FOR_TESTING.add(COURSE_FOR_TESTING);
        COURSES_FOR_TESTING.add(course);
        
        idCourse = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
        COURSE_FOR_TESTING.setIdCourse(idCourse);
        idCourse = COURSE_DAO.registerCourse(course);
        course.setIdCourse(idCourse);
    }
    
    public void deleteCourses() throws DAOException {
        for (Course course : COURSES_FOR_TESTING) {
            COURSE_DAO.deleteCourseByIdCourse(course.getIdCourse());
        }        
    }
    
    @Test
    public void registerCourseSucces() throws DAOException {
        int idCourse;
        setUp();
        idCourse = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
        COURSE_FOR_TESTING.setIdCourse(idCourse);
        assertTrue(idCourse > 0);
    }
    
    @Test(expected = DAOException.class)
    public void registerCourseFailByDuplicateCourse() throws DAOException {
        testHelper.initializeCourses();
        initializeCourse();
        COURSE_FOR_TESTING.setName(testHelper.getCourseOne().getName());
        COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
    }
    
    @Test
    public void acceptCourseProposalSucces() throws DAOException {                        
        String status;
        setUp();
        int idCourse;
        idCourse = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
        COURSE_FOR_TESTING.setIdCourse(idCourse);
        COURSE_DAO.evaluateCourseProposal(COURSE_FOR_TESTING, "Aceptado");
        status = COURSE_DAO.getCourseByIdCourse(idCourse).getStatus();
        assertEquals("Aceptado",status);
    }
    
    @Test
    public void rejectCourseProposalSucces() throws DAOException {                        
        String status;
        setUp();
        int idCourse;
        idCourse = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
        COURSE_FOR_TESTING.setIdCourse(idCourse);
        COURSE_DAO.evaluateCourseProposal(COURSE_FOR_TESTING, "Rechazado");
        status = COURSE_DAO.getCourseByIdCourse(idCourse).getStatus();
        assertEquals("Rechazado",status);
    }
    
    @Test(expected = DAOException.class)
    public void evaluateCourseProposalFailByAlreadyEvaluatedCourse() throws DAOException {
        testHelper.initializeCourses();
        Course course;
        course = testHelper.getCourseOne();
        COURSE_DAO.evaluateCourseProposal(course, "Aceptado");
    }
    
    @Test(expected = DAOException.class)
    public void evaluateCourseProposalFailByCanceledCourse() throws DAOException {        
        setUp();
        int idCourse;
        idCourse = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
        COURSE_FOR_TESTING.setIdCourse(idCourse);
        COURSE_DAO.cancelCourseProposal(COURSE_FOR_TESTING);
        COURSE_DAO.evaluateCourseProposal(COURSE_FOR_TESTING, "Aceptado");
    }
    
    @Test(expected = DAOException.class)
    public void evaluateCourseProposalFailByCourseInCollaborativeProject() throws DAOException {        
        testHelper.initializeCollaborativeProject();
        Course course;
        course = testHelper.getCourseOne();
        COURSE_DAO.evaluateCourseProposal(course, "Aceptado");
    }
    
    @Test(expected = DAOException.class)
    public void evaluateCourseProposalFailByFinalizedCourse() throws DAOException {        
        testHelper.initializeCollaborativeProject();
        CollaborativeProjectDAO collaborativeProjectDAO= new CollaborativeProjectDAO();
        Course course;
        course = testHelper.getCourseOne();
        collaborativeProjectDAO.finalizeCollaborativeProject(testHelper.getCollaborativeProject());
        COURSE_DAO.finalizeCourse(course);
        COURSE_DAO.evaluateCourseProposal(course, "Aceptado");
    }
    
    @Test
    public void updatePendingCourseSucces() throws DAOException {
        Course course;
        int idCourse;
        setUp();
        idCourse = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
        COURSE_FOR_TESTING.setIdCourse(idCourse);
        COURSE_FOR_TESTING.setName("Programación orientada a objetos");        
        COURSE_DAO.updateCourse(COURSE_FOR_TESTING);
        course = COURSE_DAO.getCourseByIdCourse(idCourse);
        assertEquals(COURSE_FOR_TESTING,course);
    }
    
    @Test
    public void updateRejectedCourseSucces() throws DAOException {
        Course course;
        int idCourse;
        setUp();
        idCourse = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
        COURSE_FOR_TESTING.setIdCourse(idCourse);
        COURSE_DAO.evaluateCourseProposal(COURSE_FOR_TESTING, "Rechazado");        
        COURSE_FOR_TESTING.setName("Programación orientada a objetos");
        COURSE_DAO.updateCourse(COURSE_FOR_TESTING);
        course = COURSE_DAO.getCourseByIdCourse(idCourse);
        assertEquals(COURSE_FOR_TESTING,course);
    }
    
    @Test(expected = DAOException.class)
    public void updateCourseFailByAcceptedCourse() throws DAOException {
        int idCourse;
        setUp();
        idCourse = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
        COURSE_FOR_TESTING.setIdCourse(idCourse);
        COURSE_DAO.evaluateCourseProposal(COURSE_FOR_TESTING, "Aceptado");        
        COURSE_FOR_TESTING.setName("Programación orientada a objetos");
        COURSE_DAO.updateCourse(COURSE_FOR_TESTING);
    }
    
    @Test(expected = DAOException.class)
    public void updateCourseFailByCourseInCollaborativeProject() throws DAOException {
        testHelper.initializeCollaborativeProject();
        Course course;
        course = testHelper.getCourseOne();
        course.setName("Programación orientada a objetos");
        COURSE_DAO.updateCourse(course);
    }
    
    @Test(expected = DAOException.class)
    public void updateCourseFailByCanceledCourse() throws DAOException {
        setUp();
        int idCourse;
        idCourse = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
        COURSE_FOR_TESTING.setIdCourse(idCourse);
        COURSE_DAO.cancelCourseProposal(COURSE_FOR_TESTING);
        COURSE_FOR_TESTING.setName("Programación orientada a objetos");
        COURSE_DAO.updateCourse(COURSE_FOR_TESTING);
    }
    
    @Test(expected = DAOException.class)
    public void updateCourseFailByFinalizedCourse() throws DAOException {
        testHelper.initializeCollaborativeProject();
        CollaborativeProjectDAO collaborativeProjectDAO= new CollaborativeProjectDAO();
        Course course;
        course = testHelper.getCourseOne();
        collaborativeProjectDAO.finalizeCollaborativeProject(testHelper.getCollaborativeProject());
        COURSE_DAO.finalizeCourse(course);
        course.setName("Programación orientada a objetos");
        COURSE_DAO.updateCourse(COURSE_FOR_TESTING);
    }
    
    @Test
    public void cancelPendingCourseProposalSucces() throws DAOException {
        String status;
        setUp();
        int idCourse;
        idCourse = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
        COURSE_FOR_TESTING.setIdCourse(idCourse);
        COURSE_DAO.cancelCourseProposal(COURSE_FOR_TESTING);
        status = COURSE_DAO.getCourseByIdCourse(idCourse).getStatus();
        assertEquals("Cancelado",status);
    }
    
    @Test
    public void cancelRejectedCourseProposalSucces() throws DAOException {
        String status;
        setUp();
        int idCourse;
        idCourse = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
        COURSE_FOR_TESTING.setIdCourse(idCourse);
        COURSE_DAO.evaluateCourseProposal(COURSE_FOR_TESTING, "Rechazado");
        COURSE_DAO.cancelCourseProposal(COURSE_FOR_TESTING);
        status = COURSE_DAO.getCourseByIdCourse(idCourse).getStatus();
        assertEquals("Cancelado",status);
    }
    
    @Test(expected = DAOException.class)
    public void cancelCourseProposalFailByCourseInCollaborativeProject() throws DAOException {
        testHelper.initializeCollaborativeProject();
        Course course;
        course = testHelper.getCourseOne();        
        COURSE_DAO.cancelCourseProposal(course);
    }
    
    @Test(expected = DAOException.class)
    public void cancelCourseProposalFailByFinalizedCourse() throws DAOException {
        testHelper.initializeCollaborativeProject();
        CollaborativeProjectDAO collaborativeProjectDAO= new CollaborativeProjectDAO();
        Course course;
        course = testHelper.getCourseOne();
        collaborativeProjectDAO.finalizeCollaborativeProject(testHelper.getCollaborativeProject());
        COURSE_DAO.finalizeCourse(course);
        COURSE_DAO.cancelCourseProposal(course);
    }
    
    @Test(expected = DAOException.class)
    public void cancelCourseProposalFailByCanceledCourse() throws DAOException {        
        setUp();
        int idCourse;
        idCourse = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
        COURSE_FOR_TESTING.setIdCourse(idCourse);
        COURSE_DAO.cancelCourseProposal(COURSE_FOR_TESTING);
        COURSE_DAO.cancelCourseProposal(COURSE_FOR_TESTING);
    }
    
    @Test
    public void changeCourseStatusToCollaborationSucces() throws DAOException {
        String status;
        setUp();
        int idCourse;
        idCourse = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
        COURSE_FOR_TESTING.setIdCourse(idCourse);
        COURSE_DAO.evaluateCourseProposal(COURSE_FOR_TESTING, "Aceptado");
        COURSE_DAO.changeCourseStatusToCollaboration(COURSE_FOR_TESTING);
        status = COURSE_DAO.getCourseByIdCourse(idCourse).getStatus();
        assertEquals("Colaboracion",status);
    }
    
    @Test(expected = DAOException.class)
    public void changeCourseStatusToCollaborationFailByInapropiateState() throws DAOException {        
        setUp();
        int idCourse;
        idCourse = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
        COURSE_FOR_TESTING.setIdCourse(idCourse);
        COURSE_DAO.changeCourseStatusToCollaboration(COURSE_FOR_TESTING);
    }
    
    @Test
    public void finalizeCourseSucces() throws DAOException {
        String status;
        testHelper.initializeCollaborativeProject();
        CollaborativeProjectDAO collaborativeProjectDAO= new CollaborativeProjectDAO();
        Course course;
        course = testHelper.getCourseOne();
        collaborativeProjectDAO.finalizeCollaborativeProject(testHelper.getCollaborativeProject());
        COURSE_DAO.finalizeCourse(course);
        status = COURSE_DAO.getCourseByIdCourse(course.getIdCourse()).getStatus();
        assertEquals("Finalizado",status);
    }
    
    @Test(expected = DAOException.class)
    public void finalizeCourseFailByNonFinalizedCollaborativeProject() throws DAOException {
        testHelper.initializeCollaborativeProject();        
        Course course;
        course = testHelper.getCourseOne();
        COURSE_DAO.finalizeCourse(course);
    }
    
    @Test
    public void getCourseProposalsSucces() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        result = COURSE_DAO.getCourseProposals();
        deleteCourses();
        assertEquals(COURSES_FOR_TESTING,result);
    }
    
    @Test
    public void getCourseProposalsFail() throws DAOException {
        ArrayList<Course> result;
        result = COURSE_DAO.getCourseProposals();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void getCourseProposalsByUniversitySucces() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        result = COURSE_DAO.getCourseProposalsByUniversity(testHelper.getProfessorOne().getUniversity().toString());
        deleteCourses();
        assertEquals(COURSES_FOR_TESTING,result);
    }
    
    @Test
    public void getCourseProposalsByUniversityFailByNonFoundUniversity() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        result = COURSE_DAO.getCourseProposalsByUniversity("not found");
        deleteCourses();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void getCourseOfferingsSucces() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        for (Course course : COURSES_FOR_TESTING) {
            COURSE_DAO.evaluateCourseProposal(course, "Aceptado");
        }
        result = COURSE_DAO.getCourseOfferings();
        deleteCourses();
        assertEquals(COURSES_FOR_TESTING,result);
    }
    
    @Test
    public void getCourseOfferingsFail() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        result = COURSE_DAO.getCourseOfferings();
        deleteCourses();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void getCourseOfferingsByUniversitySucces() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        for (Course course : COURSES_FOR_TESTING) {
            COURSE_DAO.evaluateCourseProposal(course, "Aceptado");
        }
        result = COURSE_DAO.getCourseOfferingsByUniversity(testHelper.getProfessorOne().getUniversity().toString());
        deleteCourses();
        assertEquals(COURSES_FOR_TESTING,result);
    }
    
    @Test
    public void getCourseOfferingsByUniversityFail() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        result = COURSE_DAO.getCourseOfferingsByUniversity(testHelper.getProfessorOne().getUniversity().toString());
        deleteCourses();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void getAllCoursesByProfessorSucces() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        result = COURSE_DAO.getAllCoursesByProfessor(testHelper.getProfessorOne().getIdProfessor());
        deleteCourses();
        assertEquals(COURSES_FOR_TESTING,result);
    }
    
    @Test
    public void getAllCoursesByProfessorFail() throws DAOException {
        ArrayList<Course> result;
        result = COURSE_DAO.getAllCoursesByProfessor(testHelper.getProfessorOne().getIdProfessor());
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void getAllCoursesByProfessorFailByNonExistenceIdProfessor() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        result = COURSE_DAO.getAllCoursesByProfessor(0);
        deleteCourses();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void getPendingCoursesByProfessorSucces() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        result = COURSE_DAO.getPendingCoursesByProfessor(testHelper.getProfessorOne().getIdProfessor());
        deleteCourses();
        assertEquals(COURSES_FOR_TESTING,result);
    }
    
    @Test
    public void getPendingCoursesByProfessorFail() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        for (Course course : COURSES_FOR_TESTING) {
            COURSE_DAO.evaluateCourseProposal(course, "Aceptado");
        }
        result = COURSE_DAO.getPendingCoursesByProfessor(testHelper.getProfessorOne().getIdProfessor());
        deleteCourses();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void getPendingCoursesByProfessorFailByNonExistenceIdProfessor() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        result = COURSE_DAO.getPendingCoursesByProfessor(0);
        deleteCourses();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void getAcceptedCoursesByProfessorSucces() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        for (Course course : COURSES_FOR_TESTING) {
            COURSE_DAO.evaluateCourseProposal(course, "Aceptado");
        }
        result = COURSE_DAO.getAcceptedCoursesByProfessor(testHelper.getProfessorOne().getIdProfessor());
        deleteCourses();
        assertEquals(COURSES_FOR_TESTING,result);
    }
    
    @Test
    public void getAcceptedCoursesByProfessorFail() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        result = COURSE_DAO.getAcceptedCoursesByProfessor(testHelper.getProfessorOne().getIdProfessor());
        deleteCourses();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void getAcceptedCoursesByProfessorFailByNonExistenceIdProfessor() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        for (Course course : COURSES_FOR_TESTING) {
            COURSE_DAO.evaluateCourseProposal(course, "Aceptado");
        }
        result = COURSE_DAO.getAcceptedCoursesByProfessor(0);
        deleteCourses();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void getRejectedCoursesByProfessorSucces() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        for (Course course : COURSES_FOR_TESTING) {
            COURSE_DAO.evaluateCourseProposal(course, "Rechazado");
        }
        result = COURSE_DAO.getRejectedCoursesByProfessor(testHelper.getProfessorOne().getIdProfessor());
        deleteCourses();
        assertEquals(COURSES_FOR_TESTING,result);
    }
    
    @Test
    public void getRejectedCoursesByProfessorFail() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        result = COURSE_DAO.getRejectedCoursesByProfessor(testHelper.getProfessorOne().getIdProfessor());
        deleteCourses();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void getRejectedCoursesByProfessorFailByNonExistenceIdProfessor() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        for (Course course : COURSES_FOR_TESTING) {
            COURSE_DAO.evaluateCourseProposal(course, "Rechazado");
        }
        result = COURSE_DAO.getRejectedCoursesByProfessor(0);
        deleteCourses();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void getColaborationCoursesByProfessorSucces() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        for (Course course : COURSES_FOR_TESTING) {
            COURSE_DAO.evaluateCourseProposal(course, "Aceptado");
            COURSE_DAO.changeCourseStatusToCollaboration(course);
        }
        result = COURSE_DAO.getColaborationCoursesByProfessor(testHelper.getProfessorOne().getIdProfessor());
        deleteCourses();
        assertEquals(COURSES_FOR_TESTING,result);
    }
    
    @Test
    public void getColaborationCoursesByProfessorFail() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        result = COURSE_DAO.getColaborationCoursesByProfessor(testHelper.getProfessorOne().getIdProfessor());
        deleteCourses();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void getColaborationCoursesByProfessorFailByNonExistenceIdProfessor() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        for (Course course : COURSES_FOR_TESTING) {
            COURSE_DAO.evaluateCourseProposal(course, "Aceptado");
            COURSE_DAO.changeCourseStatusToCollaboration(course);
        }
        result = COURSE_DAO.getColaborationCoursesByProfessor(0);
        deleteCourses();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void getCancelledCoursesByProfessorSucces() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        for (Course course : COURSES_FOR_TESTING) {
            COURSE_DAO.cancelCourseProposal(course);
        }
        result = COURSE_DAO.getCancelledCoursesByProfessor(testHelper.getProfessorOne().getIdProfessor());
        deleteCourses();
        assertEquals(COURSES_FOR_TESTING,result);
    }
    
    @Test
    public void getCancelledCoursesByProfessorFail() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        result = COURSE_DAO.getCancelledCoursesByProfessor(testHelper.getProfessorOne().getIdProfessor());
        deleteCourses();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void getCancelledCoursesByProfessorFailByNonExistenceIdProfessor() throws DAOException {
        ArrayList<Course> result;
        initializeCourses();
        for (Course course : COURSES_FOR_TESTING) {
            COURSE_DAO.cancelCourseProposal(course);
        }
        result = COURSE_DAO.getCancelledCoursesByProfessor(0);
        deleteCourses();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void getCoursesByProfessorAndNameSucces() throws DAOException {
        ArrayList<Course> result;
        setUp();
        COURSES_FOR_TESTING.add(COURSE_FOR_TESTING);
        int idCourse;
        idCourse = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
        COURSE_FOR_TESTING.setIdCourse(idCourse);
        result = COURSE_DAO.getCoursesByProfessorAndName
        (testHelper.getProfessorOne().getIdProfessor(), COURSE_FOR_TESTING.getName());
        assertEquals(COURSES_FOR_TESTING,result);
    }
    
    @Test
    public void getCoursesByProfessorAndNameFailByNonExistenceIdProfessor() throws DAOException {
        ArrayList<Course> result;
        setUp();
        int idCourse;
        idCourse = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
        COURSE_FOR_TESTING.setIdCourse(idCourse);
        result = COURSE_DAO.getCoursesByProfessorAndName
        (0, COURSE_FOR_TESTING.getName());
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void getCoursesByProfessorAndNameFailByNonExistenceCourse() throws DAOException {
        ArrayList<Course> result;
        setUp();
        int idCourse;
        idCourse = COURSE_DAO.registerCourse(COURSE_FOR_TESTING);
        COURSE_FOR_TESTING.setIdCourse(idCourse);
        result = COURSE_DAO.getCoursesByProfessorAndName
        (testHelper.getProfessorOne().getIdProfessor(), "not found");
        assertTrue(result.isEmpty());
    }
    
    @After
    public void tearDown() throws DAOException {
        COURSE_DAO.deleteCourseByIdCourse(COURSE_FOR_TESTING.getIdCourse());                        
        testHelper.deleteAll();
        COURSES_FOR_TESTING.clear();
    }          
}