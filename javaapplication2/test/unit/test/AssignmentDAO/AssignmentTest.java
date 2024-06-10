package unit.test.AssignmentDAO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.assignment.Assignment;
import mx.fei.coilvicapp.logic.assignment.AssignmentDAO;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import unit.test.Initializer.TestHelper;

/**
 *
 * @author ivanr
 */
public class AssignmentTest {

    private final Assignment ASSIGNMENT_FOR_TESTING = new Assignment();
    private final AssignmentDAO ASSIGNMENT_DAO = new AssignmentDAO();
    private CollaborativeProject auxCollaborativeProject;
    private final ArrayList<Assignment> ASSGIGNMENTES_FOR_TESTING = new ArrayList<>();
    private final TestHelper testHelper = new TestHelper();

    @Before
    public void setUp() {
        testHelper.initializeCollaborativeProject();
        auxCollaborativeProject = testHelper.getCollaborativeProject();
        initializeAssignment();
    }

    @After
    public void tearDown() {
        AssignmentDAO assignmentDAO = new AssignmentDAO();
        try {
            assignmentDAO.deleteAssignment(ASSIGNMENT_FOR_TESTING.getIdAssignment(), auxCollaborativeProject);
        } catch (DAOException exception) {
            Log.getLogger(AssignmentTest.class).error(exception.getMessage(), exception);
        }
        if (!ASSGIGNMENTES_FOR_TESTING.isEmpty()) {
            deleteAssignments();
        }
        testHelper.deleteAll();
    }

    @Test
    public void registerAssignmentSuccess() {
        int idAssignment = 0;
        try {
            idAssignment = ASSIGNMENT_DAO.registerAssignment(ASSIGNMENT_FOR_TESTING, auxCollaborativeProject);
            ASSIGNMENT_FOR_TESTING.setIdAssignment(idAssignment);
        } catch (DAOException exception) {
            Log.getLogger(AssignmentTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(idAssignment > 0);
    }

    @Test
    public void registerAssignmentFailByInappropiateState() {
        CollaborativeProjectDAO collaborativeProjectDAO = new CollaborativeProjectDAO();
        
        try {
            collaborativeProjectDAO.finalizeCollaborativeProject(auxCollaborativeProject);
        } catch (DAOException exception) {
            Log.getLogger(AssignmentTest.class).error(exception.getMessage(), exception);
        }
        DAOException exception = assertThrows(DAOException.class, () -> ASSIGNMENT_DAO.registerAssignment(
                ASSIGNMENT_FOR_TESTING, auxCollaborativeProject)
        );
        System.out.println(exception.getMessage());
    }

    @Test
    public void deleteAssignmentSuccess() {
        int result = 0;
        int idAssignment;

        try {
            idAssignment = ASSIGNMENT_DAO.registerAssignment(ASSIGNMENT_FOR_TESTING, auxCollaborativeProject);
            ASSIGNMENT_FOR_TESTING.setIdAssignment(idAssignment);
            result = ASSIGNMENT_DAO.deleteAssignment(ASSIGNMENT_FOR_TESTING.getIdAssignment(), 
                    auxCollaborativeProject);
        } catch (DAOException exception) {
            Log.getLogger(AssignmentTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result > 0);
    }

    @Test
    public void deleteAssignmentFailByInappropiateState() {
        CollaborativeProjectDAO collaborativeProjectDAO = new CollaborativeProjectDAO();
        int idAssignment;

        try {
            idAssignment = ASSIGNMENT_DAO.registerAssignment(ASSIGNMENT_FOR_TESTING, auxCollaborativeProject);
            ASSIGNMENT_FOR_TESTING.setIdAssignment(idAssignment);
            collaborativeProjectDAO.finalizeCollaborativeProject(auxCollaborativeProject);
            
        } catch (DAOException exception) {
            Log.getLogger(AssignmentTest.class).error(exception.getMessage(), exception);
        }
        
        DAOException exception = assertThrows(DAOException.class, ()-> ASSIGNMENT_DAO.deleteAssignment(
                ASSIGNMENT_FOR_TESTING.getIdAssignment(), auxCollaborativeProject));
        System.out.println(exception.getMessage());
    }

    @Test
    public void updateAssignmentSuccess() {
        int idAssignment;
        int result = 0;
        try {
            idAssignment = ASSIGNMENT_DAO.registerAssignment(ASSIGNMENT_FOR_TESTING, auxCollaborativeProject);
            ASSIGNMENT_FOR_TESTING.setIdAssignment(idAssignment);
            ASSIGNMENT_FOR_TESTING.setName("Rompehielos dos");
            result = ASSIGNMENT_DAO.updateAssignment(ASSIGNMENT_FOR_TESTING, auxCollaborativeProject);
        } catch (DAOException exception) {
            Log.getLogger(AssignmentTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result > 0);
    }

    @Test
    public void updateAssignmentFailByInappropiateState() {
        CollaborativeProjectDAO collaborativeProjectDAO = new CollaborativeProjectDAO();
        int idAssignment;
        DAOException result = null;
        try {
            idAssignment = ASSIGNMENT_DAO.registerAssignment(ASSIGNMENT_FOR_TESTING, auxCollaborativeProject);
            ASSIGNMENT_FOR_TESTING.setIdAssignment(idAssignment);
            collaborativeProjectDAO.finalizeCollaborativeProject(auxCollaborativeProject);
            ASSIGNMENT_FOR_TESTING.setName("Rompehielos dos");
           
        } catch (DAOException exception) {
            Log.getLogger(AssignmentTest.class).error(exception.getMessage(), exception);
        }
        
        DAOException exception = assertThrows(DAOException.class, ()->  ASSIGNMENT_DAO.updateAssignment(
                ASSIGNMENT_FOR_TESTING, auxCollaborativeProject));
        System.out.println(exception.getMessage());
    }

    @Test
    public void getAssignmentsSuccess() {
        ArrayList<Assignment> result = new ArrayList<>();

        initializeAssignmentes();
        try {
            result = ASSIGNMENT_DAO.getAssignmentsByIdProjectColaborative(
                    auxCollaborativeProject.getIdCollaborativeProject());
        } catch (DAOException exception) {
            Log.getLogger(AssignmentTest.class).error(exception.getMessage(), exception);
        }
        deleteAssignments();
        assertEquals(ASSGIGNMENTES_FOR_TESTING, result);
    }

    private void initializeAssignment() {
        ASSIGNMENT_FOR_TESTING.setName("Rompehielos");
        ASSIGNMENT_FOR_TESTING.setDescription("Actividad donde se presentaron los alumnos de "
                + "Programación y los de Bases de Datos");
        ASSIGNMENT_FOR_TESTING.setPath("/files/id/Rompehielos.pdf");
        ASSIGNMENT_FOR_TESTING.setIdColaborativeProject(
                auxCollaborativeProject.getIdCollaborativeProject());
    }

    private void initializeAssignmentes() {
        String names[] = {"Rompehielos", "Ejercicios", "Presentacion"};
        String descriptions[] = {"Actividad para presentacion", "Actividades sobre culturizacion", 
            "Descripcion sobre la colaboracion"};
        String paths[] = {"/files/1/Rompehielos.pdf", "/files/2/ejercicios.pdf", "/files/3/presentacion.pdf"};
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        try {
            for (int i = 0; i < 3; i++) {
                Assignment assignment = new Assignment();
                assignment.setName(names[i]);
                assignment.setDescription(descriptions[i]);
                assignment.setPath(paths[i]);
                assignment.setIdAssignment(ASSIGNMENT_DAO.registerAssignment(assignment, auxCollaborativeProject));
                assignment.setDate(formattedDateTime);
                ASSGIGNMENTES_FOR_TESTING.add(assignment);
            }
        } catch (DAOException exception) {
            Log.getLogger(AssignmentTest.class).error(exception.getMessage(), exception);
        }
    }

    private void deleteAssignments() {
        try {
            for (int i = 0; i < 3; i++) {
                ASSIGNMENT_DAO.deleteAssignment(ASSGIGNMENTES_FOR_TESTING.get(i).getIdAssignment(), 
                        auxCollaborativeProject);
            }
        } catch (DAOException exception) {
            Log.getLogger(AssignmentTest.class).error(exception.getMessage(), exception);
        }
    }
}
