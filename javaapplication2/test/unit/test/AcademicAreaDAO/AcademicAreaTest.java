package unit.test.AcademicAreaDAO;

import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.academicarea.*;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.assertThrows;

public class AcademicAreaTest {
    
    private static final AcademicArea TEST_ACADEMIC_AREA = new AcademicArea();
    private static final AcademicAreaDAO ACADEMIC_AREA_DAO = new AcademicAreaDAO();
    
    @Before
    public void setUp() {
        TEST_ACADEMIC_AREA.setName("Economico Administrativo");
        try {
            int idTestAcademicArea = ACADEMIC_AREA_DAO.registerAcademicArea(TEST_ACADEMIC_AREA);
            TEST_ACADEMIC_AREA.setIdAreaAcademica(idTestAcademicArea);
        } catch(DAOException exception) {
            Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
        }
    }
    
    @After
    public void tearDown() {
        try{
            ACADEMIC_AREA_DAO.deleteAcademicArea(TEST_ACADEMIC_AREA.getIdAreaAcademica());
        } catch(DAOException exception) {
            Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
        }
    }
    
    @Test
    public void TestSuccessIsThereAtLeastOneAcademicArea() {
        boolean result = false;
        try{
            result = ACADEMIC_AREA_DAO.isThereAtLeastOneAcademicArea();
        } catch(DAOException exception) {
            Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(result);
    }
    
    @Test
    public void testFailureIsThereAtLeastOneAcademicArea() {
        boolean result = false;
        try{
            ACADEMIC_AREA_DAO.deleteAcademicArea(TEST_ACADEMIC_AREA.getIdAreaAcademica());
            result = ACADEMIC_AREA_DAO.isThereAtLeastOneAcademicArea();
        } catch(DAOException exception) {
            Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(!result);
    }    
    
    @Test
    public void testSuccessInsertAcademicArea() {
        int idTestAcademicArea = 0;
        AcademicArea academicArea = new AcademicArea();
        
        academicArea.setName("Ciencias de la salud");
        try {            
            idTestAcademicArea = ACADEMIC_AREA_DAO.registerAcademicArea(academicArea);
            ACADEMIC_AREA_DAO.deleteAcademicArea(idTestAcademicArea);
        } catch (DAOException exception) {
            Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
        }    
        Assert.assertTrue(idTestAcademicArea > 0);
    } 
        
    @Test
    public void testFailureInsertAcademicAreaByNameAlreadyRegistered() {
        AcademicArea academicArea = new AcademicArea();
        academicArea.setName(TEST_ACADEMIC_AREA.getName());
        assertThrows(DAOException.class, () -> {
            ACADEMIC_AREA_DAO.registerAcademicArea(academicArea);
        });
    } 

    @Test
    public void testSuccesUpdateAcademicArea() {
        int result = 0;        
        AcademicArea academicArea = new AcademicArea();
        
        academicArea.setName("Artes plasticas");
        academicArea.setIdAreaAcademica(TEST_ACADEMIC_AREA.getIdAreaAcademica());
        try {            
            result = ACADEMIC_AREA_DAO.updateAcademicArea(academicArea);
        } catch (DAOException exception) {
            Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
        } 
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testFailureUpdateAcademicAreaByAlreadyRegisteredName() {
        AcademicArea academicArea = new AcademicArea();
        academicArea.setName("Tecnica");
        academicArea.setIdAreaAcademica(TEST_ACADEMIC_AREA.getIdAreaAcademica());
        AcademicArea auxAcademicArea = new AcademicArea();
        auxAcademicArea.setName("Tecnica");        
        int idTestAcademicArea = 0;
        
        try {
            idTestAcademicArea = ACADEMIC_AREA_DAO.registerAcademicArea(auxAcademicArea);
            DAOException exception = assertThrows(DAOException.class, () -> {
                ACADEMIC_AREA_DAO.updateAcademicArea(academicArea);
            });            
        } catch(DAOException exception) { 
            Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
        } finally {
            try {
                ACADEMIC_AREA_DAO.deleteAcademicArea(idTestAcademicArea);
            } catch (DAOException exception) {
                Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
            }
        }
    }
    
    @Test
    public void testSuccessDeleteAcademicArea() {
        int result = 0;
        
        try {
            result = ACADEMIC_AREA_DAO.deleteAcademicArea(TEST_ACADEMIC_AREA.getIdAreaAcademica());
        } catch (DAOException exception) {
            Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testFailureDeleteAcademicAreaByIdNotAvailable() {
        int result = 0;
        
        try {
            result = ACADEMIC_AREA_DAO.deleteAcademicArea(999);
        } catch (DAOException exception) {
            Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(result == 0);
    }
    
    @Test
    public void testSuccessGetAcademicAreaByName() {
        AcademicArea academicArea = new AcademicArea();
        
        try {
            academicArea = ACADEMIC_AREA_DAO.getAcademicAreaByName(TEST_ACADEMIC_AREA.getName());            
        } catch (DAOException exception) {
            Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertEquals(TEST_ACADEMIC_AREA, academicArea);
    }
    
    @Test
    public void testFailureGetAcademicAreaByNameNotAvailable() {
        AcademicArea academicArea = new AcademicArea();
        
        try {
            academicArea = ACADEMIC_AREA_DAO.getAcademicAreaByName("Artes plasticas");            
        } catch (DAOException exception) {
            Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertNotEquals(TEST_ACADEMIC_AREA, academicArea);
    }
    
    @Test
    public void testSuccessGetAcademicAreaById() {
        AcademicArea academicArea = new AcademicArea();
        
        try {
            academicArea = ACADEMIC_AREA_DAO.getAcademicAreaById(TEST_ACADEMIC_AREA.getIdAreaAcademica());            
        } catch (DAOException exception) {
            Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertEquals(TEST_ACADEMIC_AREA, academicArea);
    }
    
    @Test
    public void testFailureGetAcademicAreaByIdNotAvailable() {
        AcademicArea academicArea = new AcademicArea();
        
        try {
            academicArea = ACADEMIC_AREA_DAO.getAcademicAreaById(999);            
        } catch (DAOException exception) {
            Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertNotEquals(TEST_ACADEMIC_AREA, academicArea);
    }    
    
    @Test 
    public void testSuccessGetAcademicAreas() {
        ArrayList<AcademicArea> expectedAcademicAreas = new ArrayList<>();
        ArrayList<AcademicArea> actualAcademicAreas = new ArrayList<>();
        
        expectedAcademicAreas = initializeAcademicAreasArray();
        try {
            actualAcademicAreas = ACADEMIC_AREA_DAO.getAcademicAreas();
        } catch (DAOException exception) {
            Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
        } finally {
            tearDownAcademicAreasArray(expectedAcademicAreas);
        }
        Assert.assertEquals(expectedAcademicAreas, actualAcademicAreas);
    }
    
    @Test 
    public void testFailureGetAcademicAreas() {
        ArrayList<AcademicArea> expectedAcademicAreas = new ArrayList<>();
        ArrayList<AcademicArea> actualAcademicAreas = new ArrayList<>();
        
        expectedAcademicAreas = initializeAcademicAreasArray();
        try {
            tearDownAcademicAreasArray(expectedAcademicAreas);
            actualAcademicAreas = ACADEMIC_AREA_DAO.getAcademicAreas();
        } catch (DAOException exception) {
            Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertNotEquals(expectedAcademicAreas, actualAcademicAreas);
    }    
    
    public ArrayList<AcademicArea> initializeAcademicAreasArray() {
        ArrayList<AcademicArea> academicAreas = new ArrayList<>();
        
        academicAreas.add(TEST_ACADEMIC_AREA);
        AcademicArea academicAreaAux1 = new AcademicArea();
        academicAreaAux1.setName("Humanidades");
        AcademicArea academicAreaAux2 = new AcademicArea();
        academicAreaAux2.setName("Ciencias de la salud");
        try {
            int idAcademicArea = 0;
            idAcademicArea = ACADEMIC_AREA_DAO.registerAcademicArea(academicAreaAux1);
            academicAreaAux1.setIdAreaAcademica(idAcademicArea);
            academicAreas.add(academicAreaAux1);
            idAcademicArea = ACADEMIC_AREA_DAO.registerAcademicArea(academicAreaAux2);
            academicAreaAux2.setIdAreaAcademica(idAcademicArea);
            academicAreas.add(academicAreaAux2);
        } catch (DAOException exception) {
            Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
        }
        return academicAreas;
    }
    
    public void tearDownAcademicAreasArray(ArrayList<AcademicArea> academicAreas) {
        try {
            ACADEMIC_AREA_DAO.deleteAcademicArea(academicAreas.get(1).getIdAreaAcademica());
            ACADEMIC_AREA_DAO.deleteAcademicArea(academicAreas.get(2).getIdAreaAcademica());
        } catch (DAOException exception) {
            Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
        }
    }
    
}