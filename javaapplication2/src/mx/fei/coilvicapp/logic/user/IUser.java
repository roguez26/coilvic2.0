package mx.fei.coilvicapp.logic.user;

import mx.fei.coilvicapp.logic.implementations.DAOException;
/**
 *
 * @author ivanr
 */
public interface IUser {
    
    public int registerUser(User user) throws DAOException;
    
    public User authenticateUser(String email, String password) throws DAOException;
    
    
}
