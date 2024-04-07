import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.region.*;
import org.junit.Assert;
import org.junit.Test;
import mx.fei.coilvicapp.logic.implementations.DAOException;

public class RegionTest {
    
    @Test
    public void testSuccessInsertRegion() {
        Region region = new Region("Xalapa");
        RegionDAO regionDAO = new RegionDAO();
        
        System.out.print("InsertRegion Success");
        try {            
            Assert.assertEquals(1,regionDAO.insertRegion(region));
        } catch (DAOException exception) {
            Logger.getLogger(RegionTest.class.getName()).log(Level.SEVERE, null, exception);
        }        
    } 
    
    @Test
    public void testLengthExceededInsertRegion() {
        Region region = new Region("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        RegionDAO regionDAO = new RegionDAO();
        
        System.out.print("InsertRegion LengthExceeded");
        try {            
            Assert.assertEquals(1,regionDAO.insertRegion(region));
        } catch (DAOException exception) {
            Logger.getLogger(RegionTest.class.getName()).log(Level.SEVERE, null, exception);
        }        
    }  
    
    @Test
    public void testSuccesUpdateRegion() {
        RegionDAO regionDAO = new RegionDAO();
        String newRegion = "Veracruz";
        String regionName = "Xalapa";
         
        System.out.println("UpdateRegion Success");  
        try {            
            Assert.assertEquals(1,regionDAO.updateRegion(newRegion, regionName));
        } catch (DAOException exception) {
            Logger.getLogger(RegionTest.class.getName()).log(Level.SEVERE, null, exception);
        } 
    }
    
    @Test
    public void testNotAvailableUpdateRegion() {
        RegionDAO regionDAO = new RegionDAO();
        String newRegion = "Orizaba-Cordoba";
        String regionName = "Poza Rica-Tuxpan";
         
        System.out.println("UpdateRegion NotAvailable");  
        try {            
            Assert.assertEquals(1,regionDAO.updateRegion(newRegion, regionName));
        } catch (DAOException exception) {
            Logger.getLogger(RegionTest.class.getName()).log(Level.SEVERE, null, exception);
        } 
    }   
    
    @Test 
    public void testSuccessDeleteRegion() {
        RegionDAO regionDAO = new RegionDAO();
        String regionName = "Xalapa";
        
        System.out.println("DeleteRegion Success"); 
        try {            
            Assert.assertEquals(1,regionDAO.deleteRegion(regionName));
        } catch (DAOException exception) {
            Logger.getLogger(RegionTest.class.getName()).log(Level.SEVERE, null, exception);
        }        
    }
    
    @Test 
    public void testNotAvailableDeleteRegion() {
        RegionDAO regionDAO = new RegionDAO();
        String regionName = "Coatzacoalcos-Minatitlan";
        
        System.out.println("DeleteRegion NotAvailable"); 
        try {            
            Assert.assertEquals(1,regionDAO.deleteRegion(regionName));
        } catch (DAOException exception) {
            Logger.getLogger(RegionTest.class.getName()).log(Level.SEVERE, null, exception);
        }        
    }
    
    @Test
    public void testSuccessGetRegions() {
        RegionDAO regionDAO = new RegionDAO();
        
        System.out.print("GetRegions Success");
        try {            
        Assert.assertNotNull(regionDAO.getRegions());
        } catch (DAOException exception) {
            Logger.getLogger(RegionTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
    
}
