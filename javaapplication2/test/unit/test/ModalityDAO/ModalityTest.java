package unit.test.ModalityDAO;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.modality.Modality;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.modality.ModalityDAO;
import static org.junit.Assert.assertEquals;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.assertNotEquals;

public class ModalityTest {

    private static final Modality TEST_MODALITY = new Modality();
    private static final ModalityDAO MODALITY_DAO = new ModalityDAO();

    @Before
    public void setUp() throws DAOException {
        TEST_MODALITY.setName("Clase espejo");
        int idTestModality = MODALITY_DAO.registerModality(TEST_MODALITY);
        TEST_MODALITY.setIdModality(idTestModality);
    }

    @After
    public void tearDown() throws DAOException {
        MODALITY_DAO.deleteModality(TEST_MODALITY.getIdModality());
    }

    @Test
    public void testSuccessInsertModality() throws DAOException {
        Modality modality = new Modality();
        modality.setName("Coil vic");
        int idTestModality = MODALITY_DAO.registerModality(modality);
        MODALITY_DAO.deleteModality(idTestModality);
        Assert.assertTrue(idTestModality > 0);
    }

    @Test(expected = DAOException.class)
    public void testFailureInsertModalityByNameAlreadyRegistered() throws DAOException {
        Modality modality = new Modality();
        modality.setName(TEST_MODALITY.getName());
        MODALITY_DAO.registerModality(modality);
    }

    @Test
    public void testSuccesUpdateModality() throws DAOException {
        Modality modality = new Modality();
        modality.setName("Coil vic");
        modality.setIdModality(TEST_MODALITY.getIdModality());
        int result = MODALITY_DAO.updateModality(modality);
        Assert.assertTrue(result > 0);
    }

    @Test(expected = DAOException.class)
    public void testFailureUpdateModalityByAlreadyRegisteredName() throws DAOException {
        Modality modality = new Modality();
        modality.setName("Coil vic");
        modality.setIdModality(TEST_MODALITY.getIdModality());

        Modality auxModality = new Modality();
        auxModality.setName("Coil vic");
        int idTestModality = MODALITY_DAO.registerModality(auxModality);
        try {
            MODALITY_DAO.updateModality(modality);
        } finally {
            MODALITY_DAO.deleteModality(idTestModality);
        }
    }

    @Test
    public void testSuccessDeleteModality() throws DAOException {
        int result = MODALITY_DAO.deleteModality(TEST_MODALITY.getIdModality());
        Assert.assertTrue(result > 0);
    }

    @Test
    public void testFailureDeleteModalityByIdNotAvailable() throws DAOException {
        int result = MODALITY_DAO.deleteModality(999);
        Assert.assertTrue(result == 0);
    }

    @Test
    public void testSuccessGetModalityByName() throws DAOException {
        Modality modality = MODALITY_DAO.getModalityByName(TEST_MODALITY.getName());
        Assert.assertEquals(TEST_MODALITY, modality);
    }

    @Test
    public void testFailureGetModalityByNameNotAvailable() throws DAOException {
        Modality modality = MODALITY_DAO.getModalityByName("Otra Modalidad");
        Assert.assertNotEquals(TEST_MODALITY, modality);
    }

    @Test
    public void testSuccessGetModalityById() throws DAOException {
        Modality modality = MODALITY_DAO.getModalityByIdModality(TEST_MODALITY.getIdModality());
        Assert.assertEquals(TEST_MODALITY, modality);
    }

    @Test
    public void testFailureGetModalityByIdNotAvailable() throws DAOException {
        Modality modality = MODALITY_DAO.getModalityByIdModality(9999);
        Assert.assertNotEquals(TEST_MODALITY, modality);
    }

    @Test
    public void testGetModalitiesSuccess() throws DAOException {
        ArrayList<Modality> expectedModalities = initializeModalitiesArray();
        ArrayList<Modality> actualModalities = MODALITY_DAO.getModalities();
        assertEquals(expectedModalities, actualModalities);
        tearDownModalitiesArray(expectedModalities);
    }
    
    @Test
    public void testGetModalitiesFailure() throws DAOException {
        ArrayList<Modality> expectedModalities = initializeModalitiesArray();
        tearDownModalitiesArray(expectedModalities);
        ArrayList<Modality> actualModalities = MODALITY_DAO.getModalities();
        assertNotEquals(expectedModalities, actualModalities);
    }    

    public ArrayList<Modality> initializeModalitiesArray() throws DAOException {
        ArrayList<Modality> modalities = new ArrayList<>();
        modalities.add(TEST_MODALITY);

        Modality modalityAux1 = new Modality();
        modalityAux1.setName("Coil vic");
        int idModality1 = MODALITY_DAO.registerModality(modalityAux1);
        modalityAux1.setIdModality(idModality1);
        modalities.add(modalityAux1);

        return modalities;
    }

    private void tearDownModalitiesArray(ArrayList<Modality> modalities) throws DAOException {
        for (int i = 1; i < modalities.size(); i++) {
            MODALITY_DAO.deleteModality(modalities.get(i).getIdModality());
        }
    }
}
