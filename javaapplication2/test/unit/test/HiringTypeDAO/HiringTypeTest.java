package unit.test.HiringTypeDAO;

import java.util.ArrayList;
import log.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.hiringtype.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class HiringTypeTest {
    
    private static final HiringType TEST_HIRING_TYPE = new HiringType();
    private static final HiringTypeDAO HIRING_TYPE_DAO = new HiringTypeDAO();

    @Before
    public void setUp() {
        TEST_HIRING_TYPE.setName("Planta");
        try {
            int idTestHiringType = HIRING_TYPE_DAO.registerHiringType(TEST_HIRING_TYPE);
            TEST_HIRING_TYPE.setIdHiringType(idTestHiringType);
        } catch(DAOException exception) {
            Log.getLogger(HiringTypeTest.class).error(exception.getMessage(), exception);
        }        
    }
    
    @After
    public void tearDown() {
        try {
            HIRING_TYPE_DAO.deleteHiringType(TEST_HIRING_TYPE.getIdHiringType());
        } catch(DAOException exception) {
            Log.getLogger(HiringTypeTest.class).error(exception.getMessage(), exception);
        }        
    }
    
    @Test
    public void TestIsThereAtLeastOneHiringTypeSuccess() {
        boolean result = false;
        try{
            result = HIRING_TYPE_DAO.isThereAtLeastOneHiringType();
        } catch(DAOException exception) {
            Log.getLogger(HiringTypeTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(result);
    }
    
    @Test
    public void TestIsThereAtLeastOneHiringTypeFailure() {
        boolean result = false;
        try{
            result = HIRING_TYPE_DAO.isThereAtLeastOneHiringType();
        } catch(DAOException exception) {
            Log.getLogger(HiringTypeTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(!result);
    }   
    
    @Test
    public void testSuccessInsertHiringType() {
        int idTestHiringType = 0;
        HiringType hiringType = new HiringType();
        
        hiringType.setName("Interino por plaza");
        try {            
            idTestHiringType = HIRING_TYPE_DAO.registerHiringType(hiringType);
            HIRING_TYPE_DAO.deleteHiringType(idTestHiringType);
        } catch (DAOException exception) {
            Log.getLogger(HiringTypeTest.class).error(exception.getMessage(), exception);
        }    
        Assert.assertTrue(idTestHiringType > 0);       
    } 
    
    @Test
    public void testFailureInsertHiringTypeByNameAlreadyRegistered() {
        HiringType hiringType = new HiringType();
        
        hiringType.setName(TEST_HIRING_TYPE.getName());
        assertThrows(DAOException.class, () -> {
            HIRING_TYPE_DAO.registerHiringType(hiringType);
        });
    } 
    
    @Test
    public void testSuccesUpdateHiringType() {
        int result = 0;        
        HiringType hiringType = new HiringType();
        
        hiringType.setName("Interino por personas");
        hiringType.setIdHiringType(TEST_HIRING_TYPE.getIdHiringType());
        try {            
            result = HIRING_TYPE_DAO.updateHiringType(hiringType);
        } catch (DAOException exception) {
            Log.getLogger(HiringTypeTest.class).error(exception.getMessage(), exception);
        } 
        Assert.assertTrue(result > 0);
    }

    @Test
    public void testFailureUpdateHiringTypeByAlreadyRegisteredName() {
        int result = 0;        
        int idTestHiringType = 0;
        HiringType hiringType = new HiringType();
        
        hiringType.setName("Apoyo");
        hiringType.setIdHiringType(TEST_HIRING_TYPE.getIdHiringType());
        try {            
            HiringType auxHiringType = new HiringType();
            auxHiringType.setName("Apoyo");
            idTestHiringType = HIRING_TYPE_DAO.registerHiringType(auxHiringType);
            assertThrows(DAOException.class, () -> {
                HIRING_TYPE_DAO.updateHiringType(hiringType);
            });
        } catch (DAOException exception) {
            Log.getLogger(HiringTypeTest.class).error(exception.getMessage(), exception);
        } finally {
            try {
                HIRING_TYPE_DAO.deleteHiringType(idTestHiringType);
            } catch (DAOException exception) {
                Log.getLogger(HiringTypeTest.class).error(exception.getMessage(), exception);
            }
        }
    }
    
    @Test 
    public void testSuccessDeleteHiringType() {
        int result = 0;
        
        try {
            result = HIRING_TYPE_DAO.deleteHiringType(TEST_HIRING_TYPE.getIdHiringType());
        } catch (DAOException exception) {
            Log.getLogger(HiringTypeTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(result > 0);      
    }

    @Test 
    public void testFailureDeleteHiringTypeByIdNotAvailable() {
        assertThrows(DAOException.class, () -> {
            HIRING_TYPE_DAO.deleteHiringType(999);
        });
    }
    
    @Test
    public void testSuccessGetHiringTypeByName() {
        HiringType hiringType = new HiringType();
        
        try {
            hiringType = HIRING_TYPE_DAO.getHiringTypeByName(TEST_HIRING_TYPE.getName());
        } catch (DAOException exception) {
            Log.getLogger(HiringTypeTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertEquals(TEST_HIRING_TYPE.getName(), hiringType.getName());
    }
    
    @Test
    public void testFailureGetHiringTypeByNameNotAvailable() {
        HiringType hiringType = new HiringType();
        
        try {
            hiringType = HIRING_TYPE_DAO.getHiringTypeByName("Beca Trabajo");
        } catch (DAOException exception) {
            Log.getLogger(HiringTypeTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertNotEquals(TEST_HIRING_TYPE.getName(), hiringType.getName());
    }
    
    @Test 
    public void testSuccessGetHiringTypes() {
        ArrayList<HiringType> expectedHiringTypes = new ArrayList<>();
        ArrayList<HiringType> actualHiringTypes = new ArrayList<>();
        
        expectedHiringTypes = initializeHiringTypesArray();
        try {
            actualHiringTypes = HIRING_TYPE_DAO.getHiringTypes();
        } catch (DAOException exception) {
            Log.getLogger(HiringTypeTest.class).error(exception.getMessage(), exception);
        } finally {
            tearDownHiringTypesArray(expectedHiringTypes);
        }
        assertEquals(expectedHiringTypes, actualHiringTypes);
    }
    
    @Test 
    public void testFailureGetHiringTypes() {
        ArrayList<HiringType> expectedHiringTypes = new ArrayList<>();
        ArrayList<HiringType> actualHiringTypes = new ArrayList<>();
        
        expectedHiringTypes = initializeHiringTypesArray();
        try {
            tearDownHiringTypesArray(expectedHiringTypes);
            actualHiringTypes = HIRING_TYPE_DAO.getHiringTypes();
        } catch (DAOException exception) {
            Log.getLogger(HiringTypeTest.class).error(exception.getMessage(), exception);
        } 
        Assert.assertNotEquals(expectedHiringTypes, actualHiringTypes);
    }    
    
    public ArrayList<HiringType> initializeHiringTypesArray() {
        ArrayList<HiringType> hiringTypes = new ArrayList<>();
        
        hiringTypes.add(TEST_HIRING_TYPE);
        HiringType hiringTypeAux1 = new HiringType();
        hiringTypeAux1.setName("Interino por plaza");
        HiringType hiringTypeAux2 = new HiringType();
        hiringTypeAux2.setName("Apoyo");
        try {
            int idHiringType = 0;
            idHiringType = HIRING_TYPE_DAO.registerHiringType(hiringTypeAux1);
            hiringTypeAux1.setIdHiringType(idHiringType);
            hiringTypes.add(hiringTypeAux1);
            idHiringType = HIRING_TYPE_DAO.registerHiringType(hiringTypeAux2);
            hiringTypeAux2.setIdHiringType(idHiringType);
            hiringTypes.add(hiringTypeAux2);
        } catch (DAOException exception) {
            Log.getLogger(HiringTypeTest.class).error(exception.getMessage(), exception);
        }
        return hiringTypes;
    }
    
    public void tearDownHiringTypesArray(ArrayList<HiringType> hiringTypes) {
        try {
            if (hiringTypes.size() > 1) {
                HIRING_TYPE_DAO.deleteHiringType(hiringTypes.get(1).getIdHiringType());
            }
            if (hiringTypes.size() > 2) {
                HIRING_TYPE_DAO.deleteHiringType(hiringTypes.get(2).getIdHiringType());
            }
        } catch (DAOException exception) {
            Log.getLogger(HiringTypeTest.class).error(exception.getMessage(), exception);
        }
    }
}
