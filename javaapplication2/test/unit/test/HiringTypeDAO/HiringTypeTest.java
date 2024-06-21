package unit.test.HiringTypeDAO;

import java.util.ArrayList;
import log.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.hiringtype.*;
import static org.junit.Assert.assertEquals;

public class HiringTypeTest {

    private static final HiringType TEST_HIRING_TYPE = new HiringType();
    private static final HiringTypeDAO HIRING_TYPE_DAO = new HiringTypeDAO();

    @Before
    public void setUp() throws DAOException {
        TEST_HIRING_TYPE.setName("Planta");
        int idHiringType = HIRING_TYPE_DAO.registerHiringType(TEST_HIRING_TYPE);
        TEST_HIRING_TYPE.setIdHiringType(idHiringType);
    }

    @After
    public void tearDown() throws DAOException {
        HIRING_TYPE_DAO.deleteHiringType(TEST_HIRING_TYPE.getIdHiringType());
    }

    @Test
    public void TestIsThereAtLeastOneHiringTypeSuccess() throws DAOException {
        boolean result = HIRING_TYPE_DAO.isThereAtLeastOneHiringType();
        Assert.assertTrue(result);
    }

    @Test
    public void TestIsThereAtLeastOneHiringTypeFailure() throws DAOException {
        HIRING_TYPE_DAO.deleteHiringType(TEST_HIRING_TYPE.getIdHiringType());
        boolean result = HIRING_TYPE_DAO.isThereAtLeastOneHiringType();
        Assert.assertFalse(result);
    }

    @Test
    public void testSuccessInsertHiringType() throws DAOException {
        HiringType hiringType = new HiringType();
        hiringType.setName("Interino por plaza");
        int idHiringType = HIRING_TYPE_DAO.registerHiringType(hiringType);
        HIRING_TYPE_DAO.deleteHiringType(idHiringType);
        Assert.assertTrue(idHiringType > 0);
    }

    @Test(expected = DAOException.class)
    public void testFailureInsertHiringTypeByNameAlreadyRegistered() throws DAOException {
        HiringType hiringType = new HiringType();
        hiringType.setName(TEST_HIRING_TYPE.getName());
        HIRING_TYPE_DAO.registerHiringType(hiringType);
    }

    @Test
    public void testSuccessUpdateHiringType() throws DAOException {
        HiringType hiringType = new HiringType();
        hiringType.setName("Interino por personas");
        hiringType.setIdHiringType(TEST_HIRING_TYPE.getIdHiringType());
        int result = HIRING_TYPE_DAO.updateHiringType(hiringType);
        Assert.assertTrue(result > 0);
    }

    @Test(expected = DAOException.class)
    public void testFailureUpdateHiringTypeByAlreadyRegisteredName() throws DAOException {
        HiringType hiringType = new HiringType();
        hiringType.setName("Apoyo");
        hiringType.setIdHiringType(TEST_HIRING_TYPE.getIdHiringType());

        HiringType auxHiringType = new HiringType();
        auxHiringType.setName("Apoyo");
        int idAuxHiringType = HIRING_TYPE_DAO.registerHiringType(auxHiringType);
        try {
            HIRING_TYPE_DAO.updateHiringType(hiringType);
        } finally {
            HIRING_TYPE_DAO.deleteHiringType(idAuxHiringType);
        }
    }

    @Test
    public void testSuccessDeleteHiringType() throws DAOException {
        int result = HIRING_TYPE_DAO.deleteHiringType(TEST_HIRING_TYPE.getIdHiringType());
        Assert.assertTrue(result > 0);
    }

    @Test
    public void testFailureDeleteHiringTypeByIdNotAvailable() throws DAOException {
        int result = HIRING_TYPE_DAO.deleteHiringType(9999);
        Assert.assertTrue(result == 0);
    }

    @Test
    public void testSuccessGetHiringTypeByName() throws DAOException {
        HiringType hiringType = HIRING_TYPE_DAO.getHiringTypeByName(TEST_HIRING_TYPE.getName());
        Assert.assertEquals(TEST_HIRING_TYPE, hiringType);
    }

    @Test
    public void testFailureGetHiringTypeByNameNotAvailable() throws DAOException {
        HiringType hiringType = HIRING_TYPE_DAO.getHiringTypeByName("Beca Trabajo");
        Assert.assertNotEquals(TEST_HIRING_TYPE, hiringType);
    }

    @Test
    public void testSuccessGetHiringTypeById() throws DAOException {
        HiringType hiringType = HIRING_TYPE_DAO.getHiringTypeById(TEST_HIRING_TYPE.getIdHiringType());
        Assert.assertEquals(TEST_HIRING_TYPE, hiringType);
    }

    @Test
    public void testFailureGetHiringTypeByIdNotAvailable() throws DAOException {
        HiringType hiringType = HIRING_TYPE_DAO.getHiringTypeById(9999);
        Assert.assertNotEquals(TEST_HIRING_TYPE, hiringType);
    }

    @Test
    public void testSuccessGetHiringTypes() throws DAOException {
        ArrayList<HiringType> expectedHiringTypes = initializeHiringTypesArray();
        ArrayList<HiringType> actualHiringTypes = HIRING_TYPE_DAO.getHiringTypes();
        tearDownHiringTypesArray(expectedHiringTypes);
        assertEquals(expectedHiringTypes, actualHiringTypes);
    }

    @Test
    public void testFailureGetHiringTypes() throws DAOException {
        ArrayList<HiringType> expectedHiringTypes = initializeHiringTypesArray();
        tearDownHiringTypesArray(expectedHiringTypes);
        ArrayList<HiringType> actualHiringTypes = HIRING_TYPE_DAO.getHiringTypes();
        Assert.assertNotEquals(expectedHiringTypes, actualHiringTypes);
    }

    public ArrayList<HiringType> initializeHiringTypesArray() {
        ArrayList<HiringType> hiringTypes = new ArrayList<>();
        hiringTypes.add(TEST_HIRING_TYPE);
        
        HiringType hiringTypeAux1 = new HiringType();
        hiringTypeAux1.setName("Interino por plaza");
        
        HiringType hiringTypeAux2 = new HiringType();
        hiringTypeAux2.setName("Apoyo");
        try {
            int idHiringType = HIRING_TYPE_DAO.registerHiringType(hiringTypeAux1);
            hiringTypeAux1.setIdHiringType(idHiringType);
            hiringTypes.add(hiringTypeAux1);
            idHiringType = HIRING_TYPE_DAO.registerHiringType(hiringTypeAux2);
            hiringTypeAux2.setIdHiringType(idHiringType);
            hiringTypes.add(hiringTypeAux2);
        } catch (DAOException exception) {
            Log.getLogger(HiringTypeTest.class).error(exception.getMessage(), exception);
        }
        return hiringTypes;
    }

    public void tearDownHiringTypesArray(ArrayList<HiringType> hiringTypes) {
        try {
            for (int i = 1; i < hiringTypes.size(); i++) {
                HIRING_TYPE_DAO.deleteHiringType(hiringTypes.get(i).getIdHiringType());
            }
        } catch (DAOException exception) {
            Log.getLogger(HiringTypeTest.class).error(exception.getMessage(), exception);
        }
    }
}
