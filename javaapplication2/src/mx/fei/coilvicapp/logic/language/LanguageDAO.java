package mx.fei.coilvicapp.logic.language;

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
public class LanguageDAO implements ILanguage {
    
    @Override
    public int registerLanguage(Language language) throws DAOException {
        int result = 0;
        if (!checkNameDuplicate(language)) {
            result = insertLanguage(language);
        }
        return result;        
    }
    
    @Override
    public int updateLanguage(Language language) throws DAOException {
        int result = 0;

        if (!checkNameDuplicate(language)) {
            result = updateLanguagePrivate(language);
        } else {
            throw new DAOException("El idioma ya esta registrado", Status.ERROR);
        }
        return result;          
    }    
    
    @Override
    public int deleteLanguage(int idLanguage) throws DAOException {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "DELETE FROM Idioma WHERE idIdioma = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setInt(1, idLanguage);
            
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(LanguageDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible eliminar el idioma", Status.ERROR);
        } finally {            
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Logger.getLogger(LanguageDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        } 
        return result;
    }
    
    @Override
    public Language getLanguageByName(String languageName) throws DAOException {
        Language language = new Language();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;        
        ResultSet resultSet = null;        
        String statement = "SELECT * FROM Idioma WHERE nombre = ?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setString(1, languageName);
            
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                language.setIdLanguage(resultSet.getInt("idIdioma"));
                language.setName(resultSet.getString("nombre"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(LanguageDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener el idioma", Status.ERROR);
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
                Logger.getLogger(LanguageDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return language;
    }
    
    @Override
    public Language getLanguageByIdLanguage(int idLanguage) throws DAOException {
        Language language = new Language();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;        
        ResultSet resultSet = null;        
        String statement = "SELECT * FROM Idioma WHERE idIdioma = ?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setInt(1, idLanguage);
            
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                language.setIdLanguage(resultSet.getInt("idIdioma"));
                language.setName(resultSet.getString("nombre"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(LanguageDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener el idioma", Status.ERROR);
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
                Logger.getLogger(LanguageDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return language;
    }    
    
    @Override
    public ArrayList<Language> getLanguages() throws DAOException {
        ArrayList<Language> languages = new ArrayList<>();        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;        
        ResultSet resultSet = null;        
        String statement = "SELECT * FROM Idioma";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Language language = new Language();
                language.setIdLanguage(resultSet.getInt("idIdioma"));
                language.setName(resultSet.getString("nombre"));                
                languages.add(language);
            }
        } catch (SQLException exception) {
            Logger.getLogger(LanguageDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener los idiomas", Status.ERROR);
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
                Logger.getLogger(LanguageDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return languages;
    }
    
    private boolean checkNameDuplicate(Language language) throws DAOException {
        Language auxLanguage;
        int idLanguage = 0;

        try {
            auxLanguage = getLanguageByName(language.getName());
            idLanguage = auxLanguage.getIdLanguage();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde", Status.ERROR);
        }
        if (idLanguage != language.getIdLanguage() && idLanguage > 0) {
            throw new DAOException("El idioma ya se encuentra registrado", Status.WARNING);
        }
        return false;
    }
    
    private int insertLanguage(Language language) throws DAOException {
        int result = -1;
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "INSERT INTO Idioma (nombre) VALUES (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);
            
            preparedStatement.setString(1, language.getName());
            
            preparedStatement.executeUpdate();   
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Logger.getLogger(LanguageDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible registrar el idioma", Status.ERROR);
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
                Logger.getLogger(LanguageDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return result;
    }
    
    private int updateLanguagePrivate(Language language) throws DAOException {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE Idioma SET nombre = ? WHERE idIdioma = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            
            preparedStatement.setString(1, language.getName());
            preparedStatement.setInt(2, language.getIdLanguage());
            
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(LanguageDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible actualizar el idioma", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Logger.getLogger(LanguageDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return result;
    }
}
