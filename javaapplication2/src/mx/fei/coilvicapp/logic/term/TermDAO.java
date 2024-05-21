package mx.fei.coilvicapp.logic.term;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;

/*
 * @author d0ubl3_d
 */
public class TermDAO implements ITerm {
    
    @Override
    public int registerTerm(Term term) throws DAOException {
        int result = 0;
        if (!checkNameDuplicate(term)) {
            result = insertTerm(term);
        }
        return result;        
    }
    
    @Override
    public int updateTerm(Term term) throws DAOException {
        int result = 0;

        if (!checkNameDuplicate(term)) {
            result = updateTermPrivate(term);
        } else {
            throw new DAOException("El periodo ya esta registrado", Status.ERROR);
        }
        return result;          
    }    
    
    @Override
    public int deleteTerm(int idTerm) throws DAOException {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "DELETE FROM Periodo WHERE idPeriodo = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setInt(1, idTerm);
            
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(TermDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible eliminar el periodo", Status.ERROR);
        } finally {            
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Logger.getLogger(TermDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        } 
        return result;
    }
    
    @Override
    public Term getTermByName(String termName) throws DAOException {
        Term term = new Term();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;        
        ResultSet resultSet = null;        
        String statement = "SELECT * FROM Periodo WHERE nombre = ?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setString(1, termName);
            
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                term.setIdTerm(resultSet.getInt("idPeriodo"));
                term.setName(resultSet.getString("nombre"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(TermDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener el periodo", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(TermDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return term;
    }
    
    @Override
    public Term getTermByIdTerm(int idTerm) throws DAOException {
        Term term = new Term();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;        
        ResultSet resultSet = null;        
        String statement = "SELECT * FROM Periodo WHERE idPeriodo = ?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setInt(1, idTerm);
            
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                term.setIdTerm(resultSet.getInt("idPeriodo"));
                term.setName(resultSet.getString("nombre"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(TermDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener el periodo", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(TermDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return term;
    }    
    
    @Override
    public ArrayList<Term> getTerms() throws DAOException {
        ArrayList<Term> terms = new ArrayList<>();
        Term term = new Term();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;        
        ResultSet resultSet = null;        
        String statement = "SELECT * FROM Periodo";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                term.setIdTerm(resultSet.getInt("idPeriodo"));
                term.setName(resultSet.getString("nombre"));
                terms.add(term);
            }
        } catch (SQLException exception) {
            Logger.getLogger(TermDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener los periodos", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(TermDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return terms;
    }
    
    private boolean checkNameDuplicate(Term term) throws DAOException {
        Term auxTerm;
        int idTerm = 0;

        try {
            auxTerm = getTermByName(term.getName());
            idTerm = auxTerm.getIdTerm();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde", Status.ERROR);
        }
        if (idTerm != term.getIdTerm() && idTerm > 0) {
            throw new DAOException("El Periodo ya se encuentra registrado", Status.WARNING);
        }
        return false;
    }
    
    private int insertTerm(Term term) throws DAOException {
        int result = -1;
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "INSERT INTO Periodo (nombre) VALUES (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);
            
            preparedStatement.setString(1, term.getName());
            
            preparedStatement.executeUpdate();   
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Logger.getLogger(TermDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible registrar el periodo", Status.ERROR);
        } finally {
            try {
                if(resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Logger.getLogger(TermDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return result;
    }
    
    private int updateTermPrivate(Term term) throws DAOException {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE Periodo SET nombre = ? WHERE idPeriodo = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setString(1, term.getName());
            preparedStatement.setInt(2, term.getIdTerm());
            
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(TermDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible actualizar el periodo", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Logger.getLogger(TermDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return result;
    }
}
