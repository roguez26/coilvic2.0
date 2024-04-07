
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;
import mx.fei.coilvicapp.logic.implementations.DAOException;

public class DatabaseManagerTest {

    public DatabaseManagerTest() {

    }
    
    @Test
    public void testGetConnectionSuccess() {
        DatabaseManager instance = new DatabaseManager();
        Connection result = null;

        try {
            result = instance.getConnection();
        } catch (DAOException exception) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, exception);

        }
        Assert.assertNotNull(result);
    }

    @Test
    public void testCloseConnectionSuccess() throws DAOException {
        DatabaseManager instance = new DatabaseManager();
        Connection connection = instance.getConnection();
        boolean result = false;

        instance.closeConnection();
        result = instance.closeConnection();
        Assert.assertTrue(result);

    }
}
