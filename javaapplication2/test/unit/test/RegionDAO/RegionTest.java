package unit.test.RegionDAO;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.region.*;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertEquals;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.assertNotEquals;

public class RegionTest {

    private static final Region TEST_REGION = new Region();
    private static final RegionDAO REGION_DAO = new RegionDAO();

    @Before
    public void setUp() throws DAOException {
        TEST_REGION.setName("Xalapa");
        int idTestRegion = REGION_DAO.registerRegion(TEST_REGION);
        TEST_REGION.setIdRegion(idTestRegion);
    }

    @After
    public void tearDown() throws DAOException {
        REGION_DAO.deleteRegion(TEST_REGION.getIdRegion());
    }

    @Test
    public void testSuccessIsThereAtLeastOneRegion() throws DAOException {
        boolean result = REGION_DAO.isThereAtLeastOneRegion();
        Assert.assertTrue(result);
    }

    @Test
    public void testFailureIsThereAtLeastOneRegion() throws DAOException {
        REGION_DAO.deleteRegion(TEST_REGION.getIdRegion());
        boolean result = REGION_DAO.isThereAtLeastOneRegion();
        Assert.assertFalse(result);
    }

    @Test
    public void testSuccessInsertRegion() throws DAOException {
        Region region = new Region();
        region.setName("Veracruz");
        int idTestRegion = REGION_DAO.registerRegion(region);
        REGION_DAO.deleteRegion(idTestRegion);
        Assert.assertTrue(idTestRegion > 0);
    }

    @Test(expected = DAOException.class)
    public void testFailureInsertRegionByNameAlreadyRegistered() throws DAOException {
        Region region = new Region();
        region.setName(TEST_REGION.getName());
        REGION_DAO.registerRegion(region);
    }

    @Test
    public void testSuccesUpdateRegion() throws DAOException {
        Region region = new Region();
        region.setName("Veracruz");
        region.setIdRegion(TEST_REGION.getIdRegion());
        int result = REGION_DAO.updateRegion(region);
        Assert.assertTrue(result > 0);
    }

    @Test(expected = DAOException.class)
    public void testFailureUpdateRegionByAlreadyRegisteredName() throws DAOException {
        Region region = new Region();
        region.setName("Veracruz");
        region.setIdRegion(TEST_REGION.getIdRegion());

        Region auxRegion = new Region();
        auxRegion.setName("Veracruz");
        int idTestRegion = REGION_DAO.registerRegion(auxRegion);
        try {
            REGION_DAO.updateRegion(region);
        } finally {
            REGION_DAO.deleteRegion(idTestRegion);
        }
    }

    @Test
    public void testSuccessDeleteRegion() throws DAOException {
        int result = REGION_DAO.deleteRegion(TEST_REGION.getIdRegion());
        Assert.assertTrue(result > 0);
    }

    @Test
    public void testFailureDeleteRegionByIdNotAvailable() throws DAOException {
        int result = REGION_DAO.deleteRegion(9999);
        Assert.assertTrue(result == 0);
    }

    @Test
    public void testSuccessGetRegionByName() throws DAOException {
        Region region = REGION_DAO.getRegionByName(TEST_REGION.getName());
        Assert.assertEquals(TEST_REGION, region);
    }

    @Test
    public void testFailureGetRegionByNameNotAvailable() throws DAOException {
        Region region = REGION_DAO.getRegionByName("Poza Rica Tuxpan");
        Assert.assertNotEquals(TEST_REGION, region);
    }

    @Test
    public void testSuccessGetRegionById() throws DAOException {
        Region region = REGION_DAO.getRegionById(TEST_REGION.getIdRegion());
        Assert.assertEquals(TEST_REGION, region);
    }

    @Test
    public void testFailureGetRegionByIdNotAvailable() throws DAOException {
        Region region = REGION_DAO.getRegionById(999);
        Assert.assertNotEquals(TEST_REGION, region);
    }

    @Test
    public void testGetRegionsSuccess() throws DAOException {
        ArrayList<Region> expectedRegions = initializeRegionsArray();
        ArrayList<Region> actualRegions = REGION_DAO.getRegions();
        assertEquals(expectedRegions, actualRegions);
        tearDownRegionsArray(expectedRegions);
    }
    
    @Test
    public void testGetRegionsFailure() throws DAOException {
        ArrayList<Region> expectedRegions = initializeRegionsArray();
        tearDownRegionsArray(expectedRegions);
        ArrayList<Region> actualRegions = REGION_DAO.getRegions();
        assertNotEquals(expectedRegions, actualRegions);
    }    

    private ArrayList<Region> initializeRegionsArray() throws DAOException {
        ArrayList<Region> regions = new ArrayList<>();
        regions.add(TEST_REGION);

        Region regionAux1 = new Region();
        regionAux1.setName("Poza Rica Tuxpan");
        Region regionAux2 = new Region();
        regionAux2.setName("Orizaba Cordoba");

        int idRegion = REGION_DAO.registerRegion(regionAux1);
        regionAux1.setIdRegion(idRegion);
        regions.add(regionAux1);

        idRegion = REGION_DAO.registerRegion(regionAux2);
        regionAux2.setIdRegion(idRegion);
        regions.add(regionAux2);

        return regions;
    }

    private void tearDownRegionsArray(ArrayList<Region> regions) throws DAOException {
        for (int i = 1; i < regions.size(); i++) {
            REGION_DAO.deleteRegion(regions.get(i).getIdRegion());
        }
    }
}
