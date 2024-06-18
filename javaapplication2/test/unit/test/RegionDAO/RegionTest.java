package unit.test.RegionDAO;

import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.region.*;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertEquals;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

public class RegionTest {
    
    private static final Region TEST_REGION = new Region();
    private static final RegionDAO REGION_DAO = new RegionDAO();
    
    @Before
    public void setUp() {
        TEST_REGION.setName("Xalapa");
        try {
            int idTestRegion = REGION_DAO.registerRegion(TEST_REGION);
            TEST_REGION.setIdRegion(idTestRegion);
        } catch(DAOException exception) {
            Log.getLogger(RegionTest.class).error(exception.getMessage(), exception);
        }
    }
    
    @After
    public void tearDown() {
        try{
            REGION_DAO.deleteRegion(TEST_REGION.getIdRegion());
        } catch(DAOException exception) {
            Log.getLogger(RegionTest.class).error(exception.getMessage(), exception);
        }
    }
    
    @Test
    public void testSuccessIsThereAtLeastOneRegion() {
        boolean result = false;
        try{
            result = REGION_DAO.isThereAtLeastOneRegion();
        } catch(DAOException exception) {
            Log.getLogger(RegionTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(result);        
    }
    
    @Test
    public void testFailureIsThereAtLeastOneRegion() {
        boolean result = false;
        try{
            REGION_DAO.deleteRegion(TEST_REGION.getIdRegion());
            result = REGION_DAO.isThereAtLeastOneRegion();
        } catch(DAOException exception) {
            Log.getLogger(RegionTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(!result);        
    }    
    
    @Test
    public void testSuccessInsertRegion() {
        int idTestRegion = 0;
        Region region = new Region();
        
        region.setName("Veracruz");
        try {            
            idTestRegion = REGION_DAO.registerRegion(region);
            REGION_DAO.deleteRegion(idTestRegion);
        } catch (DAOException exception) {
            Log.getLogger(RegionTest.class).error(exception.getMessage(), exception);
        }    
        Assert.assertTrue(idTestRegion > 0);
    } 
        
    @Test
    public void testFailureInsertRegionByNameAlreadyRegistered() {
        int idTestRegion = 0;
        Region region = new Region();
        
        region.setName(TEST_REGION.getName());
        try {            
            idTestRegion = REGION_DAO.registerRegion(region);
            REGION_DAO.deleteRegion(idTestRegion);
        } catch (DAOException exception) {
            Log.getLogger(RegionTest.class).error(exception.getMessage(), exception);
        }    
        Assert.assertTrue(idTestRegion == 0);
    } 

    @Test
    public void testSuccesUpdateRegion() {
        int result = 0;        
        Region region = new Region();
        
        region.setName("Veracruz");
        region.setIdRegion(TEST_REGION.getIdRegion());
        try {            
            result = REGION_DAO.updateRegion(region);
        } catch (DAOException exception) {
            Log.getLogger(RegionTest.class).error(exception.getMessage(), exception);
        } 
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testFailureUpdateRegionByAlreadyRegisteredName() {
        Region region = new Region();
        region.setName("Veracruz");
        region.setIdRegion(TEST_REGION.getIdRegion());
        Region auxRegion = new Region();
        auxRegion.setName("Veracruz");        
        int idTestRegion = 0;
        
        try {
            idTestRegion = REGION_DAO.registerRegion(auxRegion);
            Assert.assertThrows(DAOException.class, () -> {
                REGION_DAO.updateRegion(region);
            });
        } catch (DAOException exception) {
            Log.getLogger(RegionTest.class).error(exception.getMessage(), exception);
        } finally {
            try {
                REGION_DAO.deleteRegion(idTestRegion);
            } catch (DAOException exception) {
                Log.getLogger(RegionTest.class).error(exception.getMessage(), exception);
            }
        }
    }
    
    @Test
    public void testSuccessDeleteRegion() {
        int result = 0;
        
        try {
            result = REGION_DAO.deleteRegion(TEST_REGION.getIdRegion());
        } catch (DAOException exception) {
            Log.getLogger(RegionTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(result > 0);
    }
    
    @Test
    public void testFailureDeleteRegionByIdNotAvailable() {
        int result = 0;
        
        try {
            result = REGION_DAO.deleteRegion(999);
        } catch (DAOException exception) {
            Log.getLogger(RegionTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(result == 0);
    }
    
    @Test
    public void testSuccessGetRegionByName() {
        Region region = new Region();
        
        try {
            region = REGION_DAO.getRegionByName(TEST_REGION.getName());            
        } catch (DAOException exception) {
            Log.getLogger(RegionTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertEquals(TEST_REGION, region);
    }
    
    @Test
    public void testFailureGetRegionByNameNotAvailable() {
        Region region = new Region();
        
        try {
            region = REGION_DAO.getRegionByName("Poza Rica Tuxpan");            
        } catch (DAOException exception) {
            Log.getLogger(RegionTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertNotEquals(TEST_REGION, region);
    }
    
    @Test
    public void testSuccessGetRegionById() {
        Region region = new Region();
        
        try {
            region = REGION_DAO.getRegionById(TEST_REGION.getIdRegion());            
        } catch (DAOException exception) {
            Log.getLogger(RegionTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertEquals(TEST_REGION, region);
    }
    
    @Test
    public void testFailureGetRegionByIdNotAvailable() {
        Region region = new Region();
        
        try {
            region = REGION_DAO.getRegionById(999);            
        } catch (DAOException exception) {
            Log.getLogger(RegionTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertNotEquals(TEST_REGION, region);
    }    
    
    @Test 
    public void testGetRegions() {
        ArrayList<Region> expectedRegions = new ArrayList<>();
        ArrayList<Region> actualRegions = new ArrayList<>();
        
        expectedRegions = initializeRegionsArray();
        try {
            actualRegions = REGION_DAO.getRegions();
        } catch (DAOException exception) {
            Log.getLogger(RegionTest.class).error(exception.getMessage(), exception);
        } finally {
            tearDownRegionsArray(expectedRegions);
        }
        assertEquals(expectedRegions, actualRegions);
    }
    
    public ArrayList<Region> initializeRegionsArray() {
        ArrayList<Region> regions = new ArrayList<>();
        
        regions.add(TEST_REGION);
        Region regionAux1 = new Region();
        regionAux1.setName("Poza Rica Tuxpan");
        Region regionAux2 = new Region();
        regionAux2.setName("Orizaba Cordoba");
        try {
            int idRegion = 0;
            idRegion = REGION_DAO.registerRegion(regionAux1);
            regionAux1.setIdRegion(idRegion);
            regions.add(regionAux1);
            idRegion = REGION_DAO.registerRegion(regionAux2);
            regionAux2.setIdRegion(idRegion);
            regions.add(regionAux2);
        } catch (DAOException exception) {
            Log.getLogger(RegionTest.class).error(exception.getMessage(), exception);
        }
        return regions;
    }
    
    public void tearDownRegionsArray(ArrayList<Region> regions) {
        try {
            REGION_DAO.deleteRegion(regions.get(1).getIdRegion());
            REGION_DAO.deleteRegion(regions.get(2).getIdRegion());
        } catch (DAOException exception) {
            Log.getLogger(RegionTest.class).error(exception.getMessage(), exception);
        }
    }
    
}
