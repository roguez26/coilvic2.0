import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.hiringtype.*;
import org.junit.Assert;
import org.junit.Test;
import mx.fei.coilvicapp.logic.implementations.DAOException;

public class HiringTypeTest {

    @Test
    public void testSuccessInsertHiringType() {
        HiringType hiringType = new HiringType("Planta");
        HiringTypeDAO hiringTypeDAO = new HiringTypeDAO();
        
        System.out.println("InsertHiringType Success");
        try {            
            Assert.assertEquals(1,hiringTypeDAO.insertHiringType(hiringType));
        } catch (DAOException exception) {
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception);
        }        
    } 
    
    @Test
    public void testLengthExceededInsertHiringType() {
        HiringType hiringType = new HiringType("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        HiringTypeDAO hiringTypeDAO = new HiringTypeDAO();
        
        System.out.print("InsertHiringType LengthExceeded");
        try {            
            Assert.assertEquals(1,hiringTypeDAO.insertHiringType(hiringType));
        } catch (DAOException exception) {
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception);
        }        
    } 
    
    @Test
    public void testSuccesUpdateHiringType() {
        HiringTypeDAO hiringTypeDAO = new HiringTypeDAO();
        String newHiringType = "Interino por plaza";
        String hiringTypeName = "Planta";
         
        System.out.println("UpdateHiringType Success");  
        try {            
            Assert.assertEquals(1,hiringTypeDAO.updateHiringType(newHiringType, hiringTypeName));
        } catch (DAOException exception) {
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception);
        } 
    }

    @Test
    public void testNotAvailableUpdateHiringType() {
        HiringTypeDAO hiringTypeDAO = new HiringTypeDAO();
        String newHiringType = "Interino por persona";
        String hiringTypeName = "Suplente";
         
        System.out.println("UpdateHiringType NotAvailable");  
        try {            
            Assert.assertEquals(1,hiringTypeDAO.updateHiringType(newHiringType, hiringTypeName));
        } catch (DAOException exception) {
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception);
        } 
    }
    
    @Test 
    public void testSuccessDeleteHiringType() {
        HiringTypeDAO hiringTypeDAO = new HiringTypeDAO();
        String hiringTypeName = "Planta";
        
        System.out.println("DeleteHiringType Success"); 
        try {            
            Assert.assertEquals(1,hiringTypeDAO.deleteHiringType(hiringTypeName));
        } catch (DAOException exception) {
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception);
        }        
    }

    @Test 
    public void testNotAvailableDeleteHiringType() {
        HiringTypeDAO hiringTypeDAO = new HiringTypeDAO();
        String hiringTypeName = "Interino por Obra Determinada";
        
        System.out.println("DeleteHiringType NotAvailable"); 
        try {            
            Assert.assertEquals(1,hiringTypeDAO.deleteHiringType(hiringTypeName));
        } catch (DAOException exception) {
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception);
        }        
    }
    
    @Test
    public void testSuccessGetHiringTypes() {
        HiringTypeDAO hiringTypeDAO = new HiringTypeDAO();
        
        System.out.print("GetHiringTypes Success");
        try {            
        Assert.assertNotNull(hiringTypeDAO.getHiringTypes());
        } catch (DAOException exception) {
            Logger.getLogger(HiringTypeTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
    
}
