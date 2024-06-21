package unit.test.AcademicAreaDAO;

import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.academicarea.*;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

public class AcademicAreaTest {
    
    private static final AcademicArea TEST_ACADEMIC_AREA = new AcademicArea();
    private static final AcademicAreaDAO ACADEMIC_AREA_DAO = new AcademicAreaDAO();
    
    @Before
    public void setUp() throws DAOException {
        TEST_ACADEMIC_AREA.setName("Economico Administrativo");
        int idTestAcademicArea = ACADEMIC_AREA_DAO.registerAcademicArea(TEST_ACADEMIC_AREA);
        TEST_ACADEMIC_AREA.setIdAreaAcademica(idTestAcademicArea);
    }
    
    @After
    public void tearDown() throws DAOException {
        ACADEMIC_AREA_DAO.deleteAcademicArea(TEST_ACADEMIC_AREA.getIdAreaAcademica());
    }
    
    @Test
    public void TestSuccessIsThereAtLeastOneAcademicArea() throws DAOException {
        boolean result = false;
        result = ACADEMIC_AREA_DAO.isThereAtLeastOneAcademicArea();
        Assert.assertTrue(result);        
    }
    
    @Test
    public void testFailureIsThereAtLeastOneAcademicArea() throws DAOException {
        boolean result = false;
        ACADEMIC_AREA_DAO.deleteAcademicArea(TEST_ACADEMIC_AREA.getIdAreaAcademica());
        result = ACADEMIC_AREA_DAO.isThereAtLeastOneAcademicArea();
        Assert.assertTrue(!result);
    }    
    
    @Test
    public void testSuccessInsertAcademicArea() throws DAOException {
        AcademicArea academicArea = new AcademicArea();
        
        academicArea.setName("Ciencias de la salud");
        int idTestAcademicArea = ACADEMIC_AREA_DAO.registerAcademicArea(academicArea);
        ACADEMIC_AREA_DAO.deleteAcademicArea(idTestAcademicArea);
        Assert.assertTrue(idTestAcademicArea > 0);
    } 
        
    @Test(expected = DAOException.class)
    public void testFailureInsertAcademicAreaByNameAlreadyRegistered() throws DAOException {
        int idTestAcademicArea = 0;
        AcademicArea academicArea = new AcademicArea();
        
        academicArea.setName(TEST_ACADEMIC_AREA.getName());
        idTestAcademicArea = ACADEMIC_AREA_DAO.registerAcademicArea(academicArea);
        ACADEMIC_AREA_DAO.deleteAcademicArea(idTestAcademicArea);
        Assert.assertTrue(idTestAcademicArea > 0);
    } 

    @Test
    public void testSuccesUpdateAcademicArea() throws DAOException {
        int result = 0;        
        AcademicArea academicArea = new AcademicArea();
        
        academicArea.setName("Artes plasticas");
        academicArea.setIdAreaAcademica(TEST_ACADEMIC_AREA.getIdAreaAcademica());
        result = ACADEMIC_AREA_DAO.updateAcademicArea(academicArea);
        Assert.assertTrue(result > 0);
    }
    
    @Test(expected = DAOException.class)
    public void testFailureUpdateAcademicAreaByAlreadyRegisteredName() throws DAOException {
        AcademicArea academicArea = new AcademicArea();
        
        academicArea.setName("Tecnica");
        academicArea.setIdAreaAcademica(TEST_ACADEMIC_AREA.getIdAreaAcademica());
        AcademicArea auxAcademicArea = new AcademicArea();
        auxAcademicArea.setName("Tecnica");        
        int idTestAcademicArea = 0;
        
        idTestAcademicArea = ACADEMIC_AREA_DAO.registerAcademicArea(auxAcademicArea);
        try {
            ACADEMIC_AREA_DAO.updateAcademicArea(academicArea);
        } finally {
            ACADEMIC_AREA_DAO.deleteAcademicArea(idTestAcademicArea);
        }
    }
    
    @Test
    public void testSuccessDeleteAcademicArea() throws DAOException {
        int result = 0;
        result = ACADEMIC_AREA_DAO.deleteAcademicArea(TEST_ACADEMIC_AREA.getIdAreaAcademica());
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testFailureDeleteAcademicAreaByIdNotAvailable() throws DAOException {
        int result = ACADEMIC_AREA_DAO.deleteAcademicArea(9999);
        Assert.assertTrue(result == 0);
    }
    
    @Test
    public void testSuccessGetAcademicAreaByName() throws DAOException {
        AcademicArea academicArea = new AcademicArea();
        academicArea = ACADEMIC_AREA_DAO.getAcademicAreaByName(TEST_ACADEMIC_AREA.getName());            
        Assert.assertEquals(TEST_ACADEMIC_AREA, academicArea);
    }
    
    @Test
    public void testFailureGetAcademicAreaByNameNotAvailable() throws DAOException {
        AcademicArea academicArea = new AcademicArea();
        academicArea = ACADEMIC_AREA_DAO.getAcademicAreaByName("Artes plasticas");            
        Assert.assertNotEquals(TEST_ACADEMIC_AREA, academicArea);
    }
    
    @Test
    public void testSuccessGetAcademicAreaById() throws DAOException {
        AcademicArea academicArea = new AcademicArea();
        
        academicArea = ACADEMIC_AREA_DAO.getAcademicAreaById(TEST_ACADEMIC_AREA.getIdAreaAcademica());            
        Assert.assertEquals(TEST_ACADEMIC_AREA, academicArea);
    }
    
    @Test
    public void testFailureGetAcademicAreaByIdNotAvailable() throws DAOException {
        AcademicArea academicArea = new AcademicArea();
        
        academicArea = ACADEMIC_AREA_DAO.getAcademicAreaById(9999);    
        Assert.assertNotEquals(TEST_ACADEMIC_AREA, academicArea);
    }    
    
    @Test 
    public void testSuccessGetAcademicAreas() throws DAOException {
        ArrayList<AcademicArea> expectedAcademicAreas = new ArrayList<>();
        ArrayList<AcademicArea> actualAcademicAreas = new ArrayList<>();
        
        expectedAcademicAreas = initializeAcademicAreasArray();
        actualAcademicAreas = ACADEMIC_AREA_DAO.getAcademicAreas();
        tearDownAcademicAreasArray(expectedAcademicAreas);
        Assert.assertEquals(expectedAcademicAreas, actualAcademicAreas);
    }
    
    @Test 
    public void testFailureGetAcademicAreas() throws DAOException {
        ArrayList<AcademicArea> expectedAcademicAreas = new ArrayList<>();
        ArrayList<AcademicArea> actualAcademicAreas = new ArrayList<>();
        
        expectedAcademicAreas = initializeAcademicAreasArray();
        tearDownAcademicAreasArray(expectedAcademicAreas);
        actualAcademicAreas = ACADEMIC_AREA_DAO.getAcademicAreas();
        Assert.assertNotEquals(expectedAcademicAreas, actualAcademicAreas);
    }    
    
    private ArrayList<AcademicArea> initializeAcademicAreasArray() {
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
    
    private void tearDownAcademicAreasArray(ArrayList<AcademicArea> academicAreas) {
        try {
            ACADEMIC_AREA_DAO.deleteAcademicArea(academicAreas.get(1).getIdAreaAcademica());
            ACADEMIC_AREA_DAO.deleteAcademicArea(academicAreas.get(2).getIdAreaAcademica());
        } catch (DAOException exception) {
            Log.getLogger(AcademicAreaTest.class).error(exception.getMessage(), exception);
        }
    }
    
}