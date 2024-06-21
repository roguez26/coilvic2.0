package unit.test.HiringCategoryDAO;

import java.util.ArrayList;
import log.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.hiringcategory.*;

public class HiringCategoryTest {
    
    private static final HiringCategory TEST_HIRING_CATEGORY = new HiringCategory();
    private static final HiringCategoryDAO HIRING_CATEGORY_DAO = new HiringCategoryDAO();
    
    @Before
    public void setUp() throws DAOException {
        TEST_HIRING_CATEGORY.setName("Docente TC");
        int idHiringCategory = HIRING_CATEGORY_DAO.registerHiringCategory(TEST_HIRING_CATEGORY);
        TEST_HIRING_CATEGORY.setIdHiringCategory(idHiringCategory);
    }
    
    @After
    public void tearDown() throws DAOException {
        HIRING_CATEGORY_DAO.deleteHiringCategory(TEST_HIRING_CATEGORY.getIdHiringCategory());
    }
    
    @Test
    public void TestIsThereAtLeastOneHiringCategorySuccess() throws DAOException {
        boolean result = HIRING_CATEGORY_DAO.isThereAtLeastOneHiringCategory();
        Assert.assertTrue(result);
    }
    
    @Test
    public void TestIsThereAtLeastOneHiringCategoryFailure() throws DAOException {
        HIRING_CATEGORY_DAO.deleteHiringCategory(TEST_HIRING_CATEGORY.getIdHiringCategory());
        boolean result = HIRING_CATEGORY_DAO.isThereAtLeastOneHiringCategory();
        Assert.assertTrue(!result);
    } 
    
    @Test
    public void testSuccessInsertHiringCategory() throws DAOException {
        HiringCategory hiringCategory = new HiringCategory();
        
        hiringCategory.setName("Investigador TC");               
        int idHiringCategory = HIRING_CATEGORY_DAO.registerHiringCategory(hiringCategory);
        HIRING_CATEGORY_DAO.deleteHiringCategory(idHiringCategory);
        Assert.assertTrue(idHiringCategory > 0);
    } 
    
    @Test(expected = DAOException.class)
    public void testFailureInsertHiringCategoryByNameAlreadyRegistered() throws DAOException {
        HiringCategory hiringCategory = new HiringCategory();
        
        hiringCategory.setName(TEST_HIRING_CATEGORY.getName());
        HIRING_CATEGORY_DAO.registerHiringCategory(hiringCategory);
    } 
    
    @Test
    public void testSuccessUpdateHiringCategory() throws DAOException {
        int result = 0;
        HiringCategory hiringCategory = new HiringCategory();
        
        hiringCategory.setName("Eventual");
        hiringCategory.setIdHiringCategory(TEST_HIRING_CATEGORY.getIdHiringCategory());
        result = HIRING_CATEGORY_DAO.updateHiringCategory(hiringCategory);
        Assert.assertTrue(result > 0);
    }
    
    @Test(expected = DAOException.class)
    public void testFailureUpdateHiringCategoryByAlreadyRegisteredName() throws DAOException {
        HiringCategory hiringCategory = new HiringCategory();
        
        hiringCategory.setName("Tecnico Academico TC");
        hiringCategory.setIdHiringCategory(TEST_HIRING_CATEGORY.getIdHiringCategory());
        HiringCategory auxHiringCategory = new HiringCategory();
        auxHiringCategory.setName("Tecnico Academico TC");
        
        int idTestHiringCategory = HIRING_CATEGORY_DAO.registerHiringCategory(auxHiringCategory);
        try { 
            HIRING_CATEGORY_DAO.updateHiringCategory(hiringCategory);
        } finally {
            HIRING_CATEGORY_DAO.deleteHiringCategory(idTestHiringCategory);
        }
    }
    
    @Test
    public void testSuccessDeleteHiringCategory() throws DAOException {
        int result = HIRING_CATEGORY_DAO.deleteHiringCategory(TEST_HIRING_CATEGORY.getIdHiringCategory());
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testFailureDeleteHiringCategoryByIdNotAvailable() throws DAOException {
        int result = HIRING_CATEGORY_DAO.deleteHiringCategory(9999);
        Assert.assertTrue(result == 0);
    }
    
    @Test
    public void testSuccessGetHiringCategoryByName() throws DAOException {
        HiringCategory hiringCategory = HIRING_CATEGORY_DAO.getHiringCategoryByName(TEST_HIRING_CATEGORY.getName());
        Assert.assertEquals(TEST_HIRING_CATEGORY, hiringCategory);
    }
    
    @Test
    public void testFailureGetHiringCategoryByNameNotAvailable() throws DAOException {
        HiringCategory hiringCategory = HIRING_CATEGORY_DAO.getHiringCategoryByName("Investigador T.C.");
        Assert.assertNotEquals(TEST_HIRING_CATEGORY, hiringCategory);
    }
    
    @Test
    public void testSuccessGetHiringCategoryById() throws DAOException {
        HiringCategory hiringCategory = HIRING_CATEGORY_DAO.getHiringCategoryById(TEST_HIRING_CATEGORY.getIdHiringCategory());
        Assert.assertEquals(TEST_HIRING_CATEGORY, hiringCategory);
    }
    
    @Test
    public void testFailureGetHiringCategoryByIdNotAvailable() throws DAOException {
        HiringCategory hiringCategory = HIRING_CATEGORY_DAO.getHiringCategoryById(9999);
        Assert.assertNotEquals(TEST_HIRING_CATEGORY, hiringCategory);
    }    
    
    @Test 
    public void testSuccessGetHiringCategories() throws DAOException {
        ArrayList<HiringCategory> expectedHiringCategories = initializeHiringCategoriesArray();
        ArrayList<HiringCategory> actualHiringCategories = HIRING_CATEGORY_DAO.getHiringCategories();
        tearDownHiringCategoriesArray(expectedHiringCategories);
        Assert.assertEquals(expectedHiringCategories, actualHiringCategories);
    }
    
    @Test 
    public void testFailureGetHiringCategories() throws DAOException {
        ArrayList<HiringCategory> expectedHiringCategories = initializeHiringCategoriesArray();
        tearDownHiringCategoriesArray(expectedHiringCategories);
        ArrayList<HiringCategory> actualHiringCategories = HIRING_CATEGORY_DAO.getHiringCategories();
        Assert.assertNotEquals(expectedHiringCategories, actualHiringCategories);
    }    
    
    public ArrayList<HiringCategory> initializeHiringCategoriesArray() {
        ArrayList<HiringCategory> hiringCategories = new ArrayList<>();
        
        hiringCategories.add(TEST_HIRING_CATEGORY);
        HiringCategory hiringCategoryAux1 = new HiringCategory();
        hiringCategoryAux1.setName("Investigador MC");
        HiringCategory hiringCategoryAux2 = new HiringCategory();
        hiringCategoryAux2.setName("Tecnico Academico MC");
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
