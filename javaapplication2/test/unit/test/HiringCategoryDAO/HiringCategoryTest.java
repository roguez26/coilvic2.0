package unit.test.HiringCategoryDAO;

import java.util.ArrayList;
import log.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.hiringcategory.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class HiringCategoryTest {
    
    private static final HiringCategory TEST_HIRING_CATEGORY = new HiringCategory();
    private static final HiringCategoryDAO HIRING_CATEGORY_DAO = new HiringCategoryDAO();
    
    @Before
    public void setUp() {
        try {
            TEST_HIRING_CATEGORY.setName("Docente TC");
            int idHiringCategory = HIRING_CATEGORY_DAO.registerHiringCategory(TEST_HIRING_CATEGORY);
            TEST_HIRING_CATEGORY.setIdHiringCategory(idHiringCategory);
        } catch (DAOException exception) {
            Log.getLogger(HiringCategoryTest.class).error(exception.getMessage(), exception);
        }         
    }
    
    @After
    public void tearDown() {
        try {
            HIRING_CATEGORY_DAO.deleteHiringCategory(TEST_HIRING_CATEGORY.getIdHiringCategory());
        } catch (DAOException exception) {
            Log.getLogger(HiringCategoryTest.class).error(exception.getMessage(), exception);
        }
    }
    
    @Test
    public void TestIsThereAtLeastOneHiringCategorySuccess() {
        boolean result = false;
        try{
            result = HIRING_CATEGORY_DAO.isThereAtLeastOneHiringCategory();
        } catch(DAOException exception) {
            Log.getLogger(HiringCategoryTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(result);
    }
    
    @Test
    public void TestIsThereAtLeastOneHiringCategoryFailure() {
        boolean result = false;
        try{
            result = HIRING_CATEGORY_DAO.isThereAtLeastOneHiringCategory();
        } catch(DAOException exception) {
            Log.getLogger(HiringCategoryTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(!result);
    }     
    
    @Test
    public void testSuccessInsertHiringCategory() {
        HiringCategory hiringCategory = new HiringCategory();
        int idHiringCategory = 0;
        
        hiringCategory.setName("Investigador T40C");               
        try {            
            idHiringCategory = HIRING_CATEGORY_DAO.registerHiringCategory(hiringCategory);
            HIRING_CATEGORY_DAO.deleteHiringCategory(idHiringCategory);
        } catch (DAOException exception) {
            Log.getLogger(HiringCategoryTest.class).error(exception.getMessage(), exception);
        }    
        Assert.assertTrue(idHiringCategory > 0);
    } 
    
    @Test
    public void testFailureInsertHiringCategoryByNameAlreadyRegistered() {
        HiringCategory hiringCategory = new HiringCategory();
        hiringCategory.setName(TEST_HIRING_CATEGORY.getName());
        
        assertThrows(DAOException.class, () -> {
            HIRING_CATEGORY_DAO.registerHiringCategory(hiringCategory);
        });
    } 
    
    @Test
    public void testSuccessUpdateHiringCategory() {    
        int result = 0;
        HiringCategory hiringCategory = new HiringCategory();
        
        hiringCategory.setName("Tecnico Academico TC");
        hiringCategory.setIdHiringCategory(TEST_HIRING_CATEGORY.getIdHiringCategory());
        try {            
            result = HIRING_CATEGORY_DAO.updateHiringCategory(hiringCategory);
        } catch (DAOException exception) {
            Log.getLogger(HiringCategoryTest.class).error(exception.getMessage(), exception);
        } 
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testFailureUpdateHiringCategoryByAlreadyRegisteredName() {
        HiringCategory hiringCategory = new HiringCategory();
        hiringCategory.setName("Tecnico Academico TC");
        hiringCategory.setIdHiringCategory(TEST_HIRING_CATEGORY.getIdHiringCategory());
        HiringCategory auxHiringCategory = new HiringCategory();
        auxHiringCategory.setName("Tecnico Academico TC");
        int idTestHiringCategory = 0;
        
        try {
            idTestHiringCategory = HIRING_CATEGORY_DAO.registerHiringCategory(auxHiringCategory);
            assertThrows(DAOException.class, () -> {
                HIRING_CATEGORY_DAO.updateHiringCategory(hiringCategory);
            });
        } catch (DAOException exception) { 
            Log.getLogger(HiringCategoryTest.class).error(exception.getMessage(), exception);
        } finally {
            try {
                HIRING_CATEGORY_DAO.deleteHiringCategory(idTestHiringCategory);
            } catch (DAOException exception) {
                Log.getLogger(HiringCategoryTest.class).error(exception.getMessage(), exception);
            }
        }
    }
    
    @Test 
    public void testSuccessDeleteHiringCategory() {        
        int result = 0;
        
        try {            
           result = HIRING_CATEGORY_DAO.deleteHiringCategory(TEST_HIRING_CATEGORY.getIdHiringCategory());
        } catch (DAOException exception) {
            Log.getLogger(HiringCategoryTest.class).error(exception.getMessage(), exception);
        }      
        Assert.assertTrue(result > 0);
    }
    
    @Test 
    public void testFailureDeleteHiringCategoryByIdNotAvailable() {        
        assertThrows(DAOException.class, () -> {
           HIRING_CATEGORY_DAO.deleteHiringCategory(999);
        });
    }
    
    @Test
    public void testSuccessGetHiringCategoryByName() {
        HiringCategory hiringCategory = new HiringCategory();
        try {
            hiringCategory = HIRING_CATEGORY_DAO.getHiringCategoryByName(TEST_HIRING_CATEGORY.getName());
        } catch (DAOException exception) {
            Log.getLogger(HiringCategoryTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertEquals(TEST_HIRING_CATEGORY, hiringCategory);
    }
    
    @Test
    public void testFailureGetHiringCategoryByNameNotAvailable() {
        HiringCategory hiringCategory = new HiringCategory();
        try {
            hiringCategory = HIRING_CATEGORY_DAO.getHiringCategoryByName("Investigador T.C.");
        } catch (DAOException exception) {
            Log.getLogger(HiringCategoryTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertNotEquals(TEST_HIRING_CATEGORY, hiringCategory);
    }
    
    @Test
    public void testSuccessGetHiringCategoryById() {
        HiringCategory hiringCategory = new HiringCategory();
        try {
            hiringCategory = HIRING_CATEGORY_DAO.getHiringCategoryById(TEST_HIRING_CATEGORY.getIdHiringCategory());
        } catch (DAOException exception) {
            Log.getLogger(HiringCategoryTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertEquals(TEST_HIRING_CATEGORY, hiringCategory);
    }
    
    @Test
    public void testFailureGetHiringCategoryByIdNotAvailable() {
        HiringCategory hiringCategory = new HiringCategory();
        try {
            hiringCategory = HIRING_CATEGORY_DAO.getHiringCategoryById(9999);
        } catch (DAOException exception) {
            Log.getLogger(HiringCategoryTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertNotEquals(TEST_HIRING_CATEGORY, hiringCategory);
    }    
    
    @Test 
    public void testSuccessGetHiringCategories() {
        ArrayList<HiringCategory> expectedHiringCategories = new ArrayList<>();
        ArrayList<HiringCategory> actualHiringCategories = new ArrayList<>();
        
        expectedHiringCategories = initializeHiringCategoriesArray();
        try {
            actualHiringCategories = HIRING_CATEGORY_DAO.getHiringCategories();
        } catch (DAOException exception) {
            Log.getLogger(HiringCategoryTest.class).error(exception.getMessage(), exception);
        } finally {
            tearDownHiringCategoriesArray(expectedHiringCategories);
        }
        assertEquals(expectedHiringCategories, actualHiringCategories);
    }
    
    @Test 
    public void testFailureGetHiringCategories() {
        ArrayList<HiringCategory> expectedHiringCategories = new ArrayList<>();
        ArrayList<HiringCategory> actualHiringCategories = new ArrayList<>();
        
        expectedHiringCategories = initializeHiringCategoriesArray();
        try {
            tearDownHiringCategoriesArray(expectedHiringCategories);
            actualHiringCategories = HIRING_CATEGORY_DAO.getHiringCategories();
        } catch (DAOException exception) {
            Log.getLogger(HiringCategoryTest.class).error(exception.getMessage(), exception);
        } 
        Assert.assertNotEquals(expectedHiringCategories, actualHiringCategories);
    }    
    
    public ArrayList<HiringCategory> initializeHiringCategoriesArray() {
        ArrayList<HiringCategory> hiringCategories = new ArrayList<>();
        
        hiringCategories.add(TEST_HIRING_CATEGORY);
        HiringCategory hiringCategoryAux1 = new HiringCategory();
        hiringCategoryAux1.setName("Investigador TC");
        HiringCategory hiringCategoryAux2 = new HiringCategory();
        hiringCategoryAux2.setName("Tecnico Academico TC");
        try {
            int idHiringCategory = 0;
            idHiringCategory = HIRING_CATEGORY_DAO.registerHiringCategory(hiringCategoryAux1);
            hiringCategoryAux1.setIdHiringCategory(idHiringCategory);
            hiringCategories.add(hiringCategoryAux1);
            idHiringCategory = HIRING_CATEGORY_DAO.registerHiringCategory(hiringCategoryAux2);
            hiringCategoryAux2.setIdHiringCategory(idHiringCategory);
            hiringCategories.add(hiringCategoryAux2);
        } catch (DAOException exception) {
            Log.getLogger(HiringCategoryTest.class).error(exception.getMessage(), exception);
        }
        return hiringCategories;
    }
    
    public void tearDownHiringCategoriesArray(ArrayList<HiringCategory> hiringCategories) {
        try {
            HIRING_CATEGORY_DAO.deleteHiringCategory(hiringCategories.get(1).getIdHiringCategory());
            HIRING_CATEGORY_DAO.deleteHiringCategory(hiringCategories.get(2).getIdHiringCategory());
        } catch (DAOException exception) {
            Log.getLogger(HiringCategoryTest.class).error(exception.getMessage(), exception);
        }
    }
    
}
