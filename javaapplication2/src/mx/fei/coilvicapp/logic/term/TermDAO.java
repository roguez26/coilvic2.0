package mx.fei.coilvicapp.logic.term;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;

public class TermDAO implements ITerm {
    
    /**
     * Este método se utiliza para poder registrar periodos dentro de la base de datos
     * @param term Éste es el nuevo periodo que se desea registrar, term contiene 
     * todos los nuevos datos del periodo 
     * @return 0 en caso de que el registro no pueda ser realizado, de otro modo
     * retornará el id generado automáticamente en la base de datos
     * @throws DAOException Puede lanza DAOExcpetion en caso de que el nombre del 
     * periodo ya se encuentre registrado o en caso de que se presenta una excepción
     * del tipo SQL
     */
    
    @Override
    public int registerTerm(Term term) throws DAOException {
        int result = 0;
        if (!checkNameDuplicate(term)) {
            result = insertTerm(term);
        }
        return result;        
    }
    
    /**
     * Este método sirve para poder actualizar un periodo registrado en la base 
     * de datos
     * @param term Éste es el periodo que se desea registrar, term contiene los nuevos
     * datos que se desean serán los nuevos en la base de datos
     * @return 0 en caso de que la actualización no result, de otro modo retornará 
     * 1 que es el número de filas afectadas
     * @throws DAOException Puede lanza DAOExcpetion en caso de que el nombre del 
     * periodo ya se encuentre registrado o en caso de que se presenta una excepción
     * del tipo SQL
     */
    
    @Override
    public int updateTerm(Term term) throws DAOException {
        int result = 0;

        if (!checkNameDuplicate(term)) {
            result = updateTermPrivate(term);
        }
        return result;          
    }    
    
    /**
     * Este método se utiliza para eliminar periodos registrados dentro de la base de
     * datos
     * @param idTerm Éste es el id del periodo que se desea eliminar
     * @return -1 en caso de que no pueda ser eliminado, de otro modo retornará 1 
     * que es el número de filas afectado
     * @throws DAOException Puede lanzar una DAOException en caso que ocurra una 
     * excepción del tipo SQL
     */
    
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
            Log.getLogger(TermDAO.class).error(exception.getMessage(), exception);
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
                Log.getLogger(TermDAO.class).error(exception.getMessage(), exception);
            }
        } 
        return result;
    }
    
    /**
     * Este método se utiliza para recuperar periodos que se
     * encuentran registrados en la base de datos por el nombre 
     * @param termName Éste es el nombre por el que se desea buscar
     * @return Retornará un objeto Term con los datos obtenidos por la consulta,
     * de otro modo retornará el objeto con los datos vacíos
     * @throws DAOException Puede lanzar una DAOException en caso que ocurra una 
     * excepción del tipo SQL 
     */
    
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
            Log.getLogger(TermDAO.class).error(exception.getMessage(), exception);
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
                Log.getLogger(TermDAO.class).error(exception.getMessage(), exception);
            }
        }
        return term;
    }
    
    /**
     * Este método se utiliza para poder obtener periodos con base en el id del periodo
     * @param idTerm Éste es el id del periodo que se desea recuperar
     * @return Retornará un objeto Term con los datos del periodo con el id consultado, 
     * de otro modo retornará el objeto con los datos vacíos 
     * @throws DAOException Puede lanzar una DAOException en caso que ocurra una 
     * excepción del tipo SQL 
     */
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
            Log.getLogger(TermDAO.class).error(exception.getMessage(), exception);
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
                Log.getLogger(TermDAO.class).error(exception.getMessage(), exception);
            }
        }
        return term;
    }    
    
    /**
     * Este método se utiliza para poder recupera los periodos registrados
     * en la base de datos
     * @return Un arreglo de periodos con los datos de los periodos registrados en la
     * base de datos, de otro modo puede retornar un arreglo vacío en caso de no haber
     * periodos registrados
     * @throws DAOException Puede lanzar una DAOException en caso que ocurra una 
     * excepción del tipo SQL  
     */
    
    @Override
    public ArrayList<Term> getTerms() throws DAOException {
        ArrayList<Term> terms = new ArrayList<>();        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;        
        ResultSet resultSet = null;        
        String statement = "SELECT * FROM Periodo ORDER BY idPeriodo";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Term term = new Term();
                term.setIdTerm(resultSet.getInt("idPeriodo"));
                term.setName(resultSet.getString("nombre"));                
                terms.add(term);
            }
        } catch (SQLException exception) {
            Log.getLogger(TermDAO.class).error(exception.getMessage(), exception);
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
                Log.getLogger(TermDAO.class).error(exception.getMessage(), exception);
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
            throw new DAOException
            ("No fue posible realizar la validacion, intente registrar mas tarde", Status.WARNING);
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
            Log.getLogger(TermDAO.class).error(exception.getMessage(), exception);
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
                Log.getLogger(TermDAO.class).error(exception.getMessage(), exception);
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
            Log.getLogger(TermDAO.class).error(exception.getMessage(), exception);
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
                Log.getLogger(TermDAO.class).error(exception.getMessage(), exception);
            }
        }
        return result;
    }
}
