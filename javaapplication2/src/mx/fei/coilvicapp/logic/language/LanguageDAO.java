package mx.fei.coilvicapp.logic.language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;

public class LanguageDAO implements ILanguage {
    
    /**
     * Este método se utiliza para poder registrar nuevos idiomas dentro de la 
     * base de datos
     * @param language Éste es el idioma que se desea registrar, language contiene
     * todos los datos que se desean aparecerán en la base de datos
     * @return 0 en caso de que no se registre, de otro modo retornará el id generado
     * por la base de datos
     * @throws DAOException Puede lanzar una DAOException en caso de que el nombre
     * que se desea registrar ya esté registrado o en caso de que ocurra una excepción
     * del tipo SQL
     */
    
    @Override
    public int registerLanguage(Language language) throws DAOException {
        int result = 0;
        if (!checkNameDuplicate(language)) {
            result = insertLanguage(language);
        }
        return result;        
    }
    
    /**
     * Este método se utiliza para poder actualizar un idioma registrado en la base
     * de datos
     * @param language Éste es el nuevo lenguaje que aparecerá en la base de datos,
     * language contiene los nuevos datos 
     * @return 0 en caso de que no se registre, de otro modo retornará 1 que será el 
     * número de filas afectadas
     * @throws DAOException Puede lanzar una DAOException en caso de que el nombre
     * que se desea registrar ya esté registrado o en caso de que ocurra una excepción
     * del tipo SQL 
     */
    
    @Override
    public int updateLanguage(Language language) throws DAOException {
        int result = 0;

        if (!checkNameDuplicate(language)) {
            result = updateLanguagePrivate(language);
        }
        return result;          
    } 
    
    /**
     * Este método se utiliza para poder eliminar idiomas de la base de datos
     * @param idLanguage Éste es el id del idioma que será eliminadd¿o
     * @return -1 en caso de que no se pueda eliminar, 1 en caso que la eliminación
     * result exitosa
     * @throws DAOException Puede lanzar una DAOException en caso de que 
     * ocurra una excepción del tipo SQL
     */
    
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
            Log.getLogger(LanguageDAO.class).error(exception.getMessage(), exception);
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
                Log.getLogger(LanguageDAO.class).error(exception.getMessage(), exception);
            }
        } 
        return result;
    }
    
    /**
     * Este método se utiliza para poder obtener un idioma registrado en 
     * la base de datos con base en el nombre
     * @param languageName Éste es el nombre por el cual se desea obtener el 
     * idioma solicitado
     * @return Retornará un objeto Language que contendrá los datos del idioma
     * solicitado, en caso de no encontrarlo, retornará el objeto con los datos vacíos
     * @throws DAOException Puede lanzar una DAOException en caso de que 
     * ocurra una excepción del tipo SQL 
     */
    
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
            Log.getLogger(LanguageDAO.class).error(exception.getMessage(), exception);
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
                Log.getLogger(LanguageDAO.class).error(exception.getMessage(), exception);
            }
        }
        return language;
    }
    
    /**
     * Este método se utiliza para poder obtener idiomas registrados en la base de datos
     * con base en su id
     * @param idLanguage Éste es el id del idioma que se desea recuperar de la base de 
     * datos
     * @return Retornará un objeto Language con los datos del idioma consultado, en caso de 
     * no encontrarlo retornará un objeto con los datos vacíos
     * @throws DAOException Puede lanzar una DAOException en caso de que 
     * ocurra una excepción del tipo SQL  
     */
    
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
            Log.getLogger(LanguageDAO.class).error(exception.getMessage(), exception);
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
                Log.getLogger(LanguageDAO.class).error(exception.getMessage(), exception);
            }
        }
        return language;
    } 
    
    /**
     * Este método se utiliza para obtener todos los idiomas registrados dentro de la
     * base de datos
     * @return Retornará un arreglo de idiomas con los datos de todos los idiomaas de
     * la base, de otro modo, en caso de que hayan registros retornará un arreglo vacío
     * @throws DAOException Puede lanzar una DAOException en caso de que 
     * ocurra una excepción del tipo SQL  
     */
    
    @Override
    public ArrayList<Language> getLanguages() throws DAOException {
        ArrayList<Language> languages = new ArrayList<>();        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;        
        ResultSet resultSet = null;        
        String statement = "SELECT * FROM Idioma ORDER BY idIdioma";
        
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
            Log.getLogger(LanguageDAO.class).error(exception.getMessage(), exception);
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
                Log.getLogger(LanguageDAO.class).error(exception.getMessage(), exception);
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
            throw new DAOException
            ("No fue posible realizar la validacion, intente registrar mas tarde", Status.WARNING);
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
            Log.getLogger(LanguageDAO.class).error(exception.getMessage(), exception);
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
                Log.getLogger(LanguageDAO.class).error(exception.getMessage(), exception);
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
            Log.getLogger(LanguageDAO.class).error(exception.getMessage(), exception);
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
                Log.getLogger(LanguageDAO.class).error(exception.getMessage(), exception);
            }
        }
        return result;
    }
}
