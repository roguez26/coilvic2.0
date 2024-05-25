import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.hiringtype.*;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertEquals;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

public class HiringTypeTest {
    
    private static final HiringType TEST_HIRING_TYPE = new HiringType();
    private static final HiringTypeDAO HIRING_TYPE_DAO = new HiringTypeDAO();

    @Before
    public void setUp() {
        TEST_HIRING_TYPE.setName("Planta");
        try {
            int idTestHiringType = HIRING_TYPE_DAO.registerHiringType(TEST_HIRING_TYPE);
            TEST_HIRING_TYPE.setIdHiringType(idTestHiringType);
            System.out.println(idTestHiringType);
        } catch(DAOException exception) {
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception);
        }        
    }
    
    @After
    public void tearDown() {
        try{
            HIRING_TYPE_DAO.deleteHiringType(TEST_HIRING_TYPE.getIdHiringType());
        } catch(DAOException exception) {
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception);
        }        
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
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception);
        }    
        Assert.assertTrue(idTestHiringType > 0);       
    } 
    
    @Test
    public void testFailureInsertHiringTypeByNameAlreadyRegistered() {
        int idTestHiringType = 0;
        HiringType hiringType = new HiringType();
        
        hiringType.setName(TEST_HIRING_TYPE.getName());
        try {            
            idTestHiringType = HIRING_TYPE_DAO.registerHiringType(hiringType);
            HIRING_TYPE_DAO.deleteHiringType(idTestHiringType);
        } catch (DAOException exception) {
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception);
        }    
        Assert.assertTrue(idTestHiringType > 0);       
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
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception);
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
            result = HIRING_TYPE_DAO.updateHiringType(hiringType);
        } catch (DAOException exception) {
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                HIRING_TYPE_DAO.deleteHiringType(idTestHiringType);
            } catch (DAOException exception) {
                Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        Assert.assertTrue(result > 0);
    }
    
    @Test 
    public void testSuccessDeleteHiringType() {
        int result = 0;
        
        try {
            result = HIRING_TYPE_DAO.deleteHiringType(TEST_HIRING_TYPE.getIdHiringType());
        } catch (DAOException exception) {
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(result > 0);      
    }

    @Test 
    public void testFailureDeleteHiringTypeByIdNotAvailable() {
        int result = 0;
        
        try {
            result = HIRING_TYPE_DAO.deleteHiringType(1);
        } catch (DAOException exception) {
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertTrue(result > 0);       
    }
    
    @Test
    public void testSuccessGetHiringTypeByName() {
        HiringType hiringType = new HiringType();
        
        try {
            hiringType = HIRING_TYPE_DAO.getHiringTypeByName(TEST_HIRING_TYPE.getName());
        } catch (DAOException exception) {
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertEquals(TEST_HIRING_TYPE.getName(), hiringType.getName());
    }
    
    @Test
    public void testSuccessGetHiringTypeByNameNotAvailable() {
        HiringType hiringType = new HiringType();
        
        try {
            hiringType = HIRING_TYPE_DAO.getHiringTypeByName("Beca Trabajo");
        } catch (DAOException exception) {
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertEquals(TEST_HIRING_TYPE.getName(), hiringType.getName());
    }
    
    @Test 
    public void testGetHiringTypes() {
        ArrayList<HiringType> expectedhiringTypes = new ArrayList<>();
        ArrayList<HiringType> actualhiringTypes = new ArrayList<>();
        
        expectedhiringTypes = initializeHiringTypesArray();
        try {
            actualhiringTypes = HIRING_TYPE_DAO.getHiringTypes();
        } catch (DAOException exception) {
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception); 
        } finally {
            tearDownHiringTypesArray(expectedhiringTypes);
        }
        assertEquals(expectedhiringTypes, actualhiringTypes);
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
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception); 
        }
        return hiringTypes;
    }
    
    public void tearDownHiringTypesArray(ArrayList<HiringType> hiringTypes) {
        try {
            HIRING_TYPE_DAO.deleteHiringType(hiringTypes.get(1).getIdHiringType());
            HIRING_TYPE_DAO.deleteHiringType(hiringTypes.get(2).getIdHiringType());
        } catch (DAOException exception) {
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception); 
        }
    }
    
}
