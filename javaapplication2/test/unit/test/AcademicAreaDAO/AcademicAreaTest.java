package unit.test.AcademicAreaDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.academicarea.*;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertEquals;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

public class AcademicAreaTest {
    
    private static final AcademicArea TEST_ACADEMIC_AREA = new AcademicArea();
    private static final AcademicAreaDAO ACADEMIC_AREA_DAO = new AcademicAreaDAO();
    
    @Before
    public void setUp() {
        TEST_ACADEMIC_AREA.setName("Economico Administrativo");
        try {
            int idTestAcademicArea = ACADEMIC_AREA_DAO.registerAcademicArea(TEST_ACADEMIC_AREA);
            TEST_ACADEMIC_AREA.setIdAreaAcademica(idTestAcademicArea);
            System.out.println(idTestAcademicArea);
        } catch(DAOException exception) {
            Logger.getLogger(AcademicAreaTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
    
    @After
    public void tearDown() {
        try{
            ACADEMIC_AREA_DAO.deleteAcademicArea(TEST_ACADEMIC_AREA.getIdAreaAcademica());
        } catch(DAOException exception) {
            Logger.getLogger(AcademicAreaTest.class.getName()).log(Level.SEVERE, null, exception);
        }
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
            Logger.getLogger(AcademicAreaTest.class.getName()).log(Level.SEVERE, null, exception);
        }    
        Assert.assertTrue(idTestAcademicArea > 0);
    } 
        
    @Test
    public void testFailureInsertAcademicAreaByNameAlreadyRegistered() {
        int idTestAcademicArea = 0;
        AcademicArea academicArea = new AcademicArea();
        
        academicArea.setName(TEST_ACADEMIC_AREA.getName());
        try {            
            idTestAcademicArea = ACADEMIC_AREA_DAO.registerAcademicArea(academicArea);
            ACADEMIC_AREA_DAO.deleteAcademicArea(idTestAcademicArea);
        } catch (DAOException exception) {
            Logger.getLogger(AcademicAreaTest.class.getName()).log(Level.SEVERE, null, exception);
        }    
        Assert.assertTrue(idTestAcademicArea > 0);
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
            Logger.getLogger(AcademicAreaTest.class.getName()).log(Level.SEVERE, null, exception);
        } 
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testFailureUpdateAcademicAreaByAlreadyRegisteredName() {
        int result = 0;
        int idTestAcademicArea = 0;        
        AcademicArea academicArea = new AcademicArea();
        
        academicArea.setName("Tecnica");
        academicArea.setIdAreaAcademica(TEST_ACADEMIC_AREA.getIdAreaAcademica());
        try {                        
            AcademicArea auxAcademicArea= new AcademicArea();
            auxAcademicArea.setName("Tecnica");            
            idTestAcademicArea = ACADEMIC_AREA_DAO.registerAcademicArea(auxAcademicArea);
            result = ACADEMIC_AREA_DAO.updateAcademicArea(academicArea);
        } catch (DAOException exception) {
            Logger.getLogger(AcademicAreaTest.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                ACADEMIC_AREA_DAO.deleteAcademicArea(idTestAcademicArea);
            } catch(DAOException exception) {
                Logger.getLogger(AcademicAreaTest.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testSuccessDeleteAcademicArea() {
        int result = 0;
        
        try {
            result = ACADEMIC_AREA_DAO.deleteAcademicArea(TEST_ACADEMIC_AREA.getIdAreaAcademica());
        } catch (DAOException exception) {
            Logger.getLogger(AcademicArea.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testFailureDeleteAcademicAreaByIdNotAvailable() {
        int result = 0;
        
        try {
            result = ACADEMIC_AREA_DAO.deleteAcademicArea(1);
        } catch (DAOException exception) {
            Logger.getLogger(AcademicArea.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testSuccessGetAcademicAreaByName() {
        AcademicArea academicArea = new AcademicArea();
        
        try {
            academicArea = ACADEMIC_AREA_DAO.getAcademicAreaByName(TEST_ACADEMIC_AREA.getName());            
        } catch (DAOException exception) {
            Logger.getLogger(AcademicArea.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertEquals(TEST_ACADEMIC_AREA.getName(), academicArea.getName());
    }
    
    @Test
    public void testFailureGetAcademicAreaByNameNotAvailable() {
        AcademicArea academicArea = new AcademicArea();
        
        try {
            academicArea = ACADEMIC_AREA_DAO.getAcademicAreaByName("Artes plasticas");            
        } catch (DAOException exception) {
            Logger.getLogger(AcademicArea.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertEquals(TEST_ACADEMIC_AREA.getName(), academicArea.getName());
    }
    
    @Test
    public void testSuccessGetAcademicAreaById() {
        AcademicArea academicArea = new AcademicArea();
        
        try {
            academicArea = ACADEMIC_AREA_DAO.getAcademicAreaById(TEST_ACADEMIC_AREA.getIdAreaAcademica());            
        } catch (DAOException exception) {
            Logger.getLogger(AcademicArea.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertEquals(TEST_ACADEMIC_AREA, academicArea);
    }
    
    @Test
    public void testFailureGetAcademicAreaByIdNotAvailable() {
        AcademicArea academicArea = new AcademicArea();
        
        try {
            academicArea = ACADEMIC_AREA_DAO.getAcademicAreaById(999);            
        } catch (DAOException exception) {
            Logger.getLogger(AcademicArea.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertEquals(TEST_ACADEMIC_AREA.getIdAreaAcademica(), academicArea.getIdAreaAcademica());
    }    
    
    @Test 
    public void testGetAcademicAreas() {
        ArrayList<AcademicArea> expectedAcademicAreas = new ArrayList<>();
        ArrayList<AcademicArea> actualAcademicAreas = new ArrayList<>();
        
        expectedAcademicAreas = initializeAcademicAreasArray();
        try {
            actualAcademicAreas = ACADEMIC_AREA_DAO.getAcademicAreas();
        } catch (DAOException exception) {
            Logger.getLogger(AcademicArea.class.getName()).log(Level.SEVERE, null, exception); 
        } finally {
            tearDownAcademicAreasArray(expectedAcademicAreas);
        }
        assertEquals(expectedAcademicAreas, actualAcademicAreas);
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
            Logger.getLogger(AcademicArea.class.getName()).log(Level.SEVERE, null, exception); 
        }
        return academicAreas;
    }
    
    public void tearDownAcademicAreasArray(ArrayList<AcademicArea> academicAreas) {
        try {
            ACADEMIC_AREA_DAO.deleteAcademicArea(academicAreas.get(1).getIdAreaAcademica());
            ACADEMIC_AREA_DAO.deleteAcademicArea(academicAreas.get(2).getIdAreaAcademica());
        } catch (DAOException exception) {
            Logger.getLogger(AcademicArea.class.getName()).log(Level.SEVERE, null, exception); 
        }
    }
    
}
