package unit.test.UserDAO;

import mx.fei.coilvicapp.logic.user.User;
import mx.fei.coilvicapp.logic.user.UserDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.PasswordGenerator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserRegistrationTest {

    private final User TEST_USER = new User();
    private final User AUX_TEST_USER = new User();
    private final UserDAO USER_DAO = new UserDAO();

    @Before
    public void setUp() throws DAOException {
        initializeTestUser();
        int idTestUser = USER_DAO.registerUser(TEST_USER);
        TEST_USER.setIdUser(idTestUser);

    }

    @After
    public void tearDown() throws DAOException {
        int idUser = TEST_USER.getIdUser();

        USER_DAO.deleteUser(idUser);

    }

    @Test
    public void testSuccessRegisterUser() throws DAOException {
        int idUser = 0;
        initializeAuxTestUser();
        idUser = USER_DAO.registerUser(AUX_TEST_USER);
        USER_DAO.deleteUser(idUser);
        System.out.println(idUser);
        Assert.assertTrue(idUser > 0);
    }

    @Test
    public void testSuccessDeleteUser() throws DAOException {
        int rowsAffected = 0;

        initializeAuxTestUser();

        AUX_TEST_USER.setIdUser(USER_DAO.registerUser(AUX_TEST_USER));
        rowsAffected = USER_DAO.deleteUser(AUX_TEST_USER.getIdUser());

        System.out.println(rowsAffected);
        Assert.assertTrue(rowsAffected > 0);
    }

    @Test
    public void testFailureDeleteUserByIdNotAvailable() throws DAOException {
        int rowsAffected = 0;

        rowsAffected = USER_DAO.deleteUser(0);

        System.out.println(rowsAffected);
        Assert.assertTrue(rowsAffected == 0);
    }

    @Test
    public void testSuccessUpdateUserPassword() throws DAOException {
        int rowsAffected = 0;

        TEST_USER.setPassword(PasswordGenerator.generatePassword());
        rowsAffected = USER_DAO.updateUserPassword(TEST_USER);
        System.out.println(rowsAffected);
        Assert.assertTrue(rowsAffected > 0);
    }

    @Test
    public void testFailureUpdateUserPasswordByNotFoundId() throws DAOException {
        int rowsAffected = 0;

        TEST_USER.setPassword(PasswordGenerator.generatePassword());
        TEST_USER.setIdUser(0);

        rowsAffected = USER_DAO.updateUserPassword(TEST_USER);
        System.out.println(rowsAffected);
        Assert.assertTrue(rowsAffected == 0);
    }

    @Test
    public void testGetUserByIdSuccess() throws DAOException {
        User auxUser = new User();

        auxUser = USER_DAO.getUserById(TEST_USER.getIdUser());
        TEST_USER.setPassword(USER_DAO.encryptPassword(TEST_USER.getPassword()));

        System.out.println(auxUser);
        Assert.assertEquals(auxUser, TEST_USER);
    }

    @Test
    public void testAuthenticateAdministrativeUserSuccess() throws DAOException {
        USER_DAO.authenticateAdministrativeUser(TEST_USER.getIdUser(), TEST_USER.getPassword());

    }

    @Test(expected = DAOException.class)
    public void testAuthenticateAdministrativeUserFailByIncorrectPassword() throws DAOException {
        USER_DAO.authenticateAdministrativeUser(TEST_USER.getIdUser(), "contrasena incorrecta");
    }

    @Test(expected = DAOException.class)
    public void testAuthenticateAdministrativeUserFailByIncorrectId() throws DAOException {
        USER_DAO.authenticateAdministrativeUser(0, TEST_USER.getPassword());

    }

    private void initializeTestUser() {
        TEST_USER.setPassword(PasswordGenerator.generatePassword());
        TEST_USER.setType("P");
    }

    private void initializeAuxTestUser() {
        AUX_TEST_USER.setPassword(PasswordGenerator.generatePassword());
        AUX_TEST_USER.setType("P");
    }

}
