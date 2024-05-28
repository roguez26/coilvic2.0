package mx.fei.coilvicapp.logic.user;

import mx.fei.coilvicapp.logic.implementations.DAOException;
/**
 *
 * @author ivanr
 */
public interface IUser {
    
    public int registerUser(User user) throws DAOException;    
    public boolean authenticateUser(String email, String password) throws DAOException;    
    public int deleteUser(int idProfessor) throws DAOException;
    public int updateUserPassword(User user) throws DAOException;
    public User getUserById(int idUser) throws DAOException;
    
}
