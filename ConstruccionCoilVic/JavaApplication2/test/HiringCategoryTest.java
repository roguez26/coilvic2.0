import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.hiringcategory.*;
import org.junit.Assert;
import org.junit.Test;
import java.sql.SQLException;

public class HiringCategoryTest {
    
    @Test
    public void testSuccessInsertHiringCategory() {
        HiringCategory hiringCategory = new HiringCategory("Docente T.C.");
        HiringCategoryDAO hiringCategoryDAO = new HiringCategoryDAO();
        
        System.out.println("InsertHiringCategory Success");
        try {            
            Assert.assertEquals(1,hiringCategoryDAO.insertHiringCategory(hiringCategory));
        } catch (SQLException exception) {
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception);
        }        
    } 
    
    @Test
    public void testLengthExceededInsertHiringCategory() {
        HiringCategory hiringCategory = new HiringCategory("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        HiringCategoryDAO hiringCategoryDAO = new HiringCategoryDAO();
        
        System.out.print("InsertHiringCategory LengthExceeded");
        try {            
            Assert.assertEquals(1,hiringCategoryDAO.insertHiringCategory(hiringCategory));
        } catch (SQLException exception) {
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception);
        }        
    } 
    
    @Test
    public void testSuccesUpdateHiringCategory() {
        HiringCategoryDAO hiringCategoryDAO = new HiringCategoryDAO();
        String newHiringCategory = "Investigador T.C";
        String hiringCategoryName = "Docente T.C.";
         
        System.out.println("UpdateHiringCategory Success");  
        try {            
            Assert.assertEquals(1,hiringCategoryDAO.updateHiringCategory(newHiringCategory, hiringCategoryName));
        } catch (SQLException exception) {
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception);
        } 
    }

    @Test
    public void testNotAvailableUpdateHiringCategory() {
        HiringCategoryDAO hiringCategoryDAO = new HiringCategoryDAO();
        String newHiringCategory = "Investigador T.C";
        String hiringCategoryName = "Investigador Docente T.C.";
         
        System.out.println("UpdateHiringCategory NotAvailable");  
        try {            
            Assert.assertEquals(1,hiringCategoryDAO.updateHiringCategory(newHiringCategory, hiringCategoryName));
        } catch (SQLException exception) {
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception);
        } 
    }
    
    @Test 
    public void testSuccessDeleteHiringCategory() {
        HiringCategoryDAO hiringCategoryDAO = new HiringCategoryDAO();
        String hiringCategoryName = "Docente T.C.";
        
        System.out.println("DeleteHiringCategory Success"); 
        try {            
            Assert.assertEquals(1,hiringCategoryDAO.deleteHiringCategory(hiringCategoryName));
        } catch (SQLException exception) {
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception);
        }        
    }
    
    @Test 
    public void testNotAvailableDeleteHiringCategory() {
        HiringCategoryDAO hiringCategoryDAO = new HiringCategoryDAO();
        String hiringCategoryName = "Investigador Docente T.C.";
        
        System.out.println("DeleteHiringCategory NotAvailable"); 
        try {            
            Assert.assertEquals(1,hiringCategoryDAO.deleteHiringCategory(hiringCategoryName));
        } catch (SQLException exception) {
            Logger.getLogger(HiringCategoryTest.class.getName()).log(Level.SEVERE, null, exception);
        }        
    }
    
    @Test
    public void testSuccessGetHiringCategories() {
        HiringCategoryDAO hiringCategoryDAO = new HiringCategoryDAO();
        
        System.out.print("GetHiringCategories Success");
        Assert.assertNotNull(hiringCategoryDAO.getHiringCategories());
    }
    
}
