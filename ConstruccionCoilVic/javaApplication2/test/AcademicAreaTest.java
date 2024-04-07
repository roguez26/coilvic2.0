import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.academicarea.*;
import org.junit.Assert;
import org.junit.Test;
import mx.fei.coilvicapp.logic.implementations.DAOException;

public class AcademicAreaTest {
    
    @Test
    public void testSuccessInsertAcademicArea() {
        AcademicArea academicArea = new AcademicArea();
        academicArea.setName("Economico-Administrativo");
        AcademicAreaDAO academicAreaDAO = new AcademicAreaDAO();
                
        System.out.println("InsertAcademicArea Success");
        try {            
            Assert.assertEquals(1,academicAreaDAO.insertAcademicArea(academicArea));
        } catch (DAOException exception) {
            Logger.getLogger(AcademicAreaTest.class.getName()).log(Level.SEVERE, null, exception);
        }        
    } 
    
    @Test
    public void testLengthExceededInsertAcademicArea() {
        AcademicArea academicArea = new AcademicArea();
        academicArea.setName("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        AcademicAreaDAO academicAreaDAO = new AcademicAreaDAO();
        
        System.out.println("InsertAcademicArea LengthExceeded");
        try {            
            Assert.assertEquals(1,academicAreaDAO.insertAcademicArea(academicArea));
        } catch (DAOException exception) {
            Logger.getLogger(AcademicAreaTest.class.getName()).log(Level.SEVERE, null, exception);
        }        
    } 

    @Test
    public void testSuccesUpdateAcademicArea() {
        AcademicAreaDAO academicAreaDAO = new AcademicAreaDAO();
        String newAcademicAreaName = "Ciencias de la  salud";
        String academicAreaName = "Economico-Administrativo";
         
        System.out.println("UpdateAcademicArea Success");  
        try {            
            Assert.assertEquals(1,academicAreaDAO.updateAcademicArea(newAcademicAreaName, academicAreaName));
        } catch (DAOException exception) {
            Logger.getLogger(AcademicAreaTest.class.getName()).log(Level.SEVERE, null, exception);
        } 
    }
    
    @Test
    public void testNotAvailableUpdateAcademicArea() {
        AcademicAreaDAO academicAreaDAO = new AcademicAreaDAO();
        String newAcademicAreaName = "Economico-Administrativo";
        String academicAreaName = "Humanidades";
         
        System.out.println("UpdateAcademicArea NotAvailable");  
        try {            
            Assert.assertEquals(1,academicAreaDAO.updateAcademicArea(newAcademicAreaName, academicAreaName));
        } catch (DAOException exception) {
            Logger.getLogger(AcademicAreaTest.class.getName()).log(Level.SEVERE, null, exception);
        } 
    }
    
    @Test
    public void testSuccessDeleteAcademicArea() {
        AcademicAreaDAO academicAreaDAO = new AcademicAreaDAO();
        String academicAreaName = "Economico-Administrativo";
        
        System.out.println("deleteAcademicArea Success");
        try {            
            Assert.assertEquals(1,academicAreaDAO.deleteAcademicArea(academicAreaName));
        } catch (DAOException exception) {
            Logger.getLogger(AcademicAreaTest.class.getName()).log(Level.SEVERE, null, exception);
        } 
    }
    
    @Test
    public void testNotAvailableDeleteAcademicArea() {
        AcademicAreaDAO academicAreaDAO = new AcademicAreaDAO();
        String academicAreaName = "Ciencias de la  salud";
        
        System.out.println("deleteAcademicArea NotAvailable");
        try {            
            Assert.assertEquals(1,academicAreaDAO.deleteAcademicArea(academicAreaName));
        } catch (DAOException exception) {
            Logger.getLogger(AcademicAreaTest.class.getName()).log(Level.SEVERE, null, exception);
        } 
    }
    
    @Test
    public void testSuccessGetAcademicAreas() {
        AcademicAreaDAO academicAreaDAO = new AcademicAreaDAO();
        
        System.out.print("GetAcademicAreas Success");
        try {
            Assert.assertNotNull(academicAreaDAO.getAcademicAreas());
        } catch (DAOException exception) {
            
        }
       // Assert.assertNotNull(academicAreaDAO.getAcademicAreas());
    }
    
}
