package unit.test.AssignmentDAO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.assignment.Assignment;
import mx.fei.coilvicapp.logic.assignment.AssignmentDAO;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProjectDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import unit.test.Initializer.TestHelper;

public class AssignmentTest {

    private final Assignment ASSIGNMENT_FOR_TESTING = new Assignment();
    private final AssignmentDAO ASSIGNMENT_DAO = new AssignmentDAO();
    private CollaborativeProject auxCollaborativeProject = new CollaborativeProject();
    private final ArrayList<Assignment> ASSGIGNMENTES_FOR_TESTING = new ArrayList<>();
    private final TestHelper TEST_HELPER = new TestHelper();

    @Before
    public void setUp() {

        TEST_HELPER.initializeCollaborativeProject();
        auxCollaborativeProject = TEST_HELPER.getCollaborativeProject();

        initializeAssignment();
    }

    @After
    public void tearDown() throws DAOException {
        AssignmentDAO assignmentDAO = new AssignmentDAO();
        if (ASSIGNMENT_FOR_TESTING.getIdAssignment() > 0) {
            assignmentDAO.deleteAssignmentByIdAssignment(ASSIGNMENT_FOR_TESTING.getIdAssignment());
           // assignmentDAO.deleteAssignment(ASSIGNMENT_FOR_TESTING.getIdAssignment(), auxCollaborativeProject);
        }

        if (!ASSGIGNMENTES_FOR_TESTING.isEmpty()) {
            deleteAssignments();
        }
        TEST_HELPER.deleteAll();
    }

    @Test
    public void registerAssignmentSuccess() throws DAOException {
        int idAssignment = 0;

        idAssignment = ASSIGNMENT_DAO.registerAssignment(ASSIGNMENT_FOR_TESTING, auxCollaborativeProject);
        ASSIGNMENT_FOR_TESTING.setIdAssignment(idAssignment);
        System.out.println(idAssignment);
        assertTrue(idAssignment > 0);
    }

    @Test(expected = DAOException.class)
    public void registerAssignmentFailByInappropiateState() throws DAOException {
        CollaborativeProjectDAO collaborativeProjectDAO = new CollaborativeProjectDAO();

        collaborativeProjectDAO.finalizeCollaborativeProject(auxCollaborativeProject);
        ASSIGNMENT_DAO.registerAssignment(
                ASSIGNMENT_FOR_TESTING, auxCollaborativeProject);

    }

    @Test
    public void deleteAssignmentSuccess() throws DAOException {
        int result = 0;
        int idAssignment = 0;

        idAssignment = ASSIGNMENT_DAO.registerAssignment(ASSIGNMENT_FOR_TESTING, auxCollaborativeProject);
        ASSIGNMENT_FOR_TESTING.setIdAssignment(idAssignment);
        result = ASSIGNMENT_DAO.deleteAssignment(ASSIGNMENT_FOR_TESTING.getIdAssignment(),
                auxCollaborativeProject);
        assertTrue(result > 0);
    }

    @Test(expected = DAOException.class)
    public void deleteAssignmentFailByInappropiateState() throws DAOException {
        CollaborativeProjectDAO collaborativeProjectDAO = new CollaborativeProjectDAO();
        int idAssignment = 0;

        idAssignment = ASSIGNMENT_DAO.registerAssignment(ASSIGNMENT_FOR_TESTING, auxCollaborativeProject);
        ASSIGNMENT_FOR_TESTING.setIdAssignment(idAssignment);
        collaborativeProjectDAO.finalizeCollaborativeProject(auxCollaborativeProject);

        try {
            ASSIGNMENT_DAO.deleteAssignment(ASSIGNMENT_FOR_TESTING.getIdAssignment(), auxCollaborativeProject);
        } finally {
            deleteAssignment();
        }

    }
    
    private void deleteAssignment() throws DAOException {
        
        ASSIGNMENT_DAO.deleteAssignmentByIdAssignment(ASSIGNMENT_FOR_TESTING.getIdAssignment());

    }

    @Test
    public void updateAssignmentSuccess() throws DAOException {
        int idAssignment = 0;
        int result = 0;
        idAssignment = ASSIGNMENT_DAO.registerAssignment(ASSIGNMENT_FOR_TESTING, auxCollaborativeProject);
        ASSIGNMENT_FOR_TESTING.setIdAssignment(idAssignment);
        ASSIGNMENT_FOR_TESTING.setName("Rompehielos dos");
        result = ASSIGNMENT_DAO.updateAssignment(ASSIGNMENT_FOR_TESTING, auxCollaborativeProject);

        assertTrue(result > 0);
    }

    @Test(expected = DAOException.class)
    public void updateAssignmentFailByInappropiateState() throws DAOException {
        CollaborativeProjectDAO collaborativeProjectDAO = new CollaborativeProjectDAO();
        int idAssignment;

        idAssignment = ASSIGNMENT_DAO.registerAssignment(ASSIGNMENT_FOR_TESTING, auxCollaborativeProject);
        ASSIGNMENT_FOR_TESTING.setIdAssignment(idAssignment);
        collaborativeProjectDAO.finalizeCollaborativeProject(auxCollaborativeProject);
        ASSIGNMENT_FOR_TESTING.setName("Rompehielos dos");
        try {
            ASSIGNMENT_DAO.updateAssignment(
                    ASSIGNMENT_FOR_TESTING, auxCollaborativeProject);
        } finally {
            deleteAssignment();
        }
    }
    @Test
    public void getAssignmentsSuccess() throws DAOException {
        ArrayList<Assignment> result = new ArrayList<>();

        initializeAssignmentes();
        result = ASSIGNMENT_DAO.getAssignmentsByIdProjectColaborative(
                auxCollaborativeProject.getIdCollaborativeProject());
        deleteAssignments();
        System.out.println(result);
        assertEquals(ASSGIGNMENTES_FOR_TESTING, result);
    }


    private void initializeAssignment() {
        ASSIGNMENT_FOR_TESTING.setIdAssignment(0);
        ASSIGNMENT_FOR_TESTING.setName("Rompehielos");
        ASSIGNMENT_FOR_TESTING.setDescription("Actividad donde se presentaron los alumnos de "
                + "Programación y los de Bases de Datos");
        ASSIGNMENT_FOR_TESTING.setPath("/files/id/Rompehielos.pdf");
    }

    private void initializeAssignmentes() throws DAOException {
        String names[] = {"Rompehielos", "Ejercicios", "Presentacion"};
        String descriptions[] = {"Actividad para presentacion", "Actividades sobre culturizacion",
            "Descripcion sobre la colaboracion"};
        String paths[] = {"/files/1/Rompehielos.pdf", "/files/2/ejercicios.pdf", "/files/3/presentacion.pdf"};
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        for (int i = 0; i < 3; i++) {
            Assignment assignment = new Assignment();
            assignment.setName(names[i]);
            assignment.setDescription(descriptions[i]);
            assignment.setPath(paths[i]);
            assignment.setIdAssignment(ASSIGNMENT_DAO.registerAssignment(assignment, auxCollaborativeProject));
            assignment.setDate(formattedDateTime);
            ASSGIGNMENTES_FOR_TESTING.add(assignment);
        }

    }


    private void deleteAssignments() throws DAOException {
        for (int i = 0; i < 3; i++) {
            ASSIGNMENT_DAO.deleteAssignment(ASSGIGNMENTES_FOR_TESTING.get(i).getIdAssignment(),
                    auxCollaborativeProject);
        }

    }
}
