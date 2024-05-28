package unit.test.UserDAO;

import log.Log;
import mx.fei.coilvicapp.logic.user.User;
import mx.fei.coilvicapp.logic.user.UserDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.PasswordGenerator;
import mx.fei.coilvicapp.logic.professor.Professor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserRegistrationTest {

    private static final User TEST_USER = new User();
    private static final User AUX_TEST_USER = new User();
    private static final UserDAO USER_DAO = new UserDAO(); 
    private static final Professor TEST_PROFESSOR = new Professor();
    
    @Before
    public void setUp() {
        initializeTestUser();
        try {
            int idTestUser = USER_DAO.registerUser(TEST_USER);
            TEST_USER.setIdUser(idTestUser);            
        } catch (DAOException exception) {
            Log.getLogger(UserRegistrationTest.class).error(exception.getMessage(), exception);
        }
    }
    
    @After 
    public void tearDown(){
        int idUser = TEST_USER.getIdUser();
        
        try {
            USER_DAO.deleteUser(idUser);            
        } catch (DAOException exception) {
            Log.getLogger(UserRegistrationTest.class).error(exception.getMessage(), exception);
        }                
    }
    
    @Test
    public void testSuccessRegisterUser() {
        int idUser = 0;
        initializeAuxTestUser();
        try {          
            idUser = USER_DAO.registerUser(AUX_TEST_USER);
            USER_DAO.deleteUser(idUser);
        } catch (DAOException exception) {
            Log.getLogger(UserRegistrationTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(idUser > 0);
    }   
    
    @Test
    public void testFailureRegisterUserByInvalidPassword() {
        int idUser = 0;
        initializeAuxTestUser();
        AUX_TEST_USER.setPassword("Si");
        try {          
            idUser = USER_DAO.registerUser(AUX_TEST_USER);
            USER_DAO.deleteUser(idUser);
        } catch (DAOException exception) {
            Log.getLogger(UserRegistrationTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(idUser > 0);
    }       
        
    @Test
    public void testSuccessDeleteUser() {
        int rowsAffected = 0;
        
        initializeAuxTestUser();
        try {          
            AUX_TEST_USER.setIdUser(USER_DAO.registerUser(AUX_TEST_USER));
            rowsAffected = USER_DAO.deleteUser(AUX_TEST_USER.getIdUser());
        } catch (DAOException exception) {
            Log.getLogger(UserRegistrationTest.class).error(exception.getMessage(), exception);
        } 
        Assert.assertTrue(rowsAffected > 0);        
    }
    
    @Test 
    public void testFailureDeleteUserByIdNotAvailable() {
        int rowsAffected = 0;
        
        try {                      
            rowsAffected = USER_DAO.deleteUser(999);
        } catch (DAOException exception) {
            Log.getLogger(UserRegistrationTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(rowsAffected > 0);        
    }      
    
    @Test
    public void testSuccessUpdateUserPassword() {
        int rowsAffected = 0;
     
        TEST_USER.setPassword(PasswordGenerator.generatePassword());
        try {
            rowsAffected = USER_DAO.updateUserPassword(TEST_USER);
        } catch (DAOException exception) {
            Log.getLogger(UserRegistrationTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(rowsAffected > 0);   
    }   
    
    @Test
    public void testFailureUpdateUserPasswordByInvalidPassword() {
        int rowsAffected = 0;
     
        TEST_USER.setPassword("12345");
        try {
            rowsAffected = USER_DAO.updateUserPassword(TEST_USER);
        } catch (DAOException exception) {
            Log.getLogger(UserRegistrationTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(rowsAffected > 0);   
    }      
    
   @Test
    public void testFailureUpdateUserPasswordByNotFoundId() {
        int rowsAffected = 0;
     
        TEST_USER.setPassword(PasswordGenerator.generatePassword());
        TEST_USER.setIdUser(999);
        try {
            rowsAffected = USER_DAO.updateUserPassword(TEST_USER);
        } catch (DAOException exception) {
            Log.getLogger(UserRegistrationTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertTrue(rowsAffected > 0);   
    }     
    
    @Test
    public void testGetUserByIdSuccess() {
        User auxUser = new User();
        
        
        try {
            auxUser = USER_DAO.getUserById(TEST_USER.getIdUser());
        } catch (DAOException exception) {
            Log.getLogger(UserRegistrationTest.class).error(exception.getMessage(), exception);
        }
        Assert.assertEquals(auxUser, TEST_USER);
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
