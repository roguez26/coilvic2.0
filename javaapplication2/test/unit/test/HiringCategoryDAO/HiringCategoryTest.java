import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.hiringcategory.*;
import static org.junit.Assert.assertEquals;

public class HiringCategoryTest {
    
    private static final HiringCategory TEST_HIRING_CATEGORY = new HiringCategory();
    private static final HiringCategoryDAO HIRING_CATEGORY_DAO = new HiringCategoryDAO();
    
    @Before
    public void setUp() {
        try {
            TEST_HIRING_CATEGORY.setName("Docente TC");
            int idHiringCategory = HIRING_CATEGORY_DAO.registerHiringCategory(TEST_HIRING_CATEGORY);
            TEST_HIRING_CATEGORY.setIdHiringCategory(idHiringCategory);
            System.out.println(idHiringCategory);
        } catch (DAOException exception) {
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception);
        }         
    }
    
    @After
    public void tearDown() {
        try {
            HIRING_CATEGORY_DAO.deleteHiringCategory(TEST_HIRING_CATEGORY.getIdHiringCategory());
        } catch (DAOException exception) {
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception); 
        }
    }
    
    @Test
    public void testSuccessInsertHiringCategory() {
        HiringCategory hiringCategory = new HiringCategory();
        int idHiringCategory = 0;
        
        hiringCategory.setName("Investigador TC");               
        try {            
            idHiringCategory = HIRING_CATEGORY_DAO.registerHiringCategory(hiringCategory);
            HIRING_CATEGORY_DAO.deleteHiringCategory(idHiringCategory);
        } catch (DAOException exception) {
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception);
        }    
        Assert.assertTrue(idHiringCategory > 0);
    } 
    
    @Test
    public void testFailureInsertHiringCategoryByNameAlreadyRegistered() {
        HiringCategory hiringCategory = new HiringCategory();
        int idHiringCategory = 0;
        
        hiringCategory.setName(TEST_HIRING_CATEGORY.getName());        
        try {            
            idHiringCategory = HIRING_CATEGORY_DAO.registerHiringCategory(hiringCategory);
            HIRING_CATEGORY_DAO.deleteHiringCategory(idHiringCategory);
        } catch (DAOException exception) {
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception);
        }    
        Assert.assertTrue(idHiringCategory > 0);
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
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception);
        } 
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testFailureUpdateHiringCategoryByAlreadyRegisteredName() {
        int result = 0;
        int idHiringCategory = 0;        
        HiringCategory hiringCategory = new HiringCategory();
        
        hiringCategory.setName("Tecnico Academico TC");
        hiringCategory.setIdHiringCategory(TEST_HIRING_CATEGORY.getIdHiringCategory());
        try {            
            HiringCategory auxHiringCategory = new HiringCategory();
            auxHiringCategory.setName("Tecnico Academico TC");
            idHiringCategory = HIRING_CATEGORY_DAO.registerHiringCategory(auxHiringCategory);
            result = HIRING_CATEGORY_DAO.updateHiringCategory(hiringCategory);
        } catch (DAOException exception) {
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                HIRING_CATEGORY_DAO.deleteHiringCategory(idHiringCategory);
            } catch (DAOException exception) {
                Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        Assert.assertTrue(result > 0);
    }
    
    @Test 
    public void testSuccessDeleteHiringCategory() {        
        int result = 0;
        
        try {            
           result = HIRING_CATEGORY_DAO.deleteHiringCategory(TEST_HIRING_CATEGORY.getIdHiringCategory());
        } catch (DAOException exception) {
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception);
        }      
        Assert.assertTrue(result > 0);
    }
    
    @Test 
    public void testFailureDeleteHiringCategoryByIdNotAvailable() {        
        int result = 0;
        
        try {            
           result = HIRING_CATEGORY_DAO.deleteHiringCategory(1);
        } catch (DAOException exception) {
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception);
        }      
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testSuccessGetHiringCategoryByName() {
        HiringCategory hiringCategory = new HiringCategory();
        try {
            hiringCategory = HIRING_CATEGORY_DAO.getHiringCategoryByName(TEST_HIRING_CATEGORY.getName());
        } catch (DAOException exception) {
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertEquals(TEST_HIRING_CATEGORY.getName(), hiringCategory.getName());
    }
    
    @Test
    public void testFailureGetHiringCategoryByNameNotAvailable() {
        HiringCategory hiringCategory = new HiringCategory();
        try {
            hiringCategory = HIRING_CATEGORY_DAO.getHiringCategoryByName("Investigador T.C.");
        } catch (DAOException exception) {
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        Assert.assertEquals(TEST_HIRING_CATEGORY.getName(), hiringCategory.getName());
    }
    
    @Test 
    public void testGetHiringCategories() {
        ArrayList<HiringCategory> expectedHiringCategories = new ArrayList<>();
        ArrayList<HiringCategory> actualHiringCategories = new ArrayList<>();
        
        expectedHiringCategories = initializeHiringCategoriesArray();
        try {
            actualHiringCategories = HIRING_CATEGORY_DAO.getHiringCategories();
        } catch (DAOException exception) {
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception); 
        } finally {
            tearDownHiringCategoriesArray(expectedHiringCategories);
        }
        assertEquals(expectedHiringCategories, actualHiringCategories);
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
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception); 
        }
        return hiringCategories;
    }
    
    public void tearDownHiringCategoriesArray(ArrayList<HiringCategory> hiringCategories) {
        try {
            HIRING_CATEGORY_DAO.deleteHiringCategory(1);
            HIRING_CATEGORY_DAO.deleteHiringCategory(2);
        } catch (DAOException exception) {
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception); 
        }
    }
    
}
