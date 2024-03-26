/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.fei.coilvic.logic.university;
import java.sql.CallableStatement;
import mx.fei.coilvic.dataaccess.DatabaseManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ivanr
 */
public class UniversityDAO implements IUniversity {
    
    public UniversityDAO() {
        
    }
    @Override
    public int insertUniversity(University university) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "INSERT INTO Universidad(nombre, jurisdiccion, ciudad, idPais) "
                + "values (?, ?, ?, ?)";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, university.getName());
            preparedStatement.setString(2, university.getJurisdiction());
            preparedStatement.setString(3, university.getCity());
            preparedStatement.setInt(4, university.getIdCountry());
            return preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
            return -1;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }
    @Override
    public int updateUniversity(University university) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE Universidad SET nombre = ?, jurisdiccion = ?, ciudad = ?, idPais = ? WHERE idUniversidad = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, university.getName());
            preparedStatement.setString(2, university.getJurisdiction());
            preparedStatement.setString(3, university.getCity());
            preparedStatement.setInt(4, university.getIdCountry());
            preparedStatement.setInt(5, university.getIdUniversity());
            return preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
            return -1;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        
    }
    @Override
    public int deleteUniversity(University university) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "DELETE FROM Universidad where idUniversidad=?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, university.getIdUniversity());
            return preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
            return -1;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
    }
    
    @Override
    public ArrayList<University> getAllUniversities() {
        Connection connection = null;
        ArrayList<University> universitiesList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM Universidad";
        
        try {
            connection = new DatabaseManager().getConnection();
            preparedStatement = connection.prepareCall(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                University instance = new University();
                instance.setIdUniversity(resultSet.getInt("IdUniversidad"));
                instance.setName(resultSet.getString("Nombre"));
                instance.setJurisdiction(resultSet.getString("Jurisdiccion"));
                instance.setCity(resultSet.getString("Ciudad"));
                instance.setIdCountry(resultSet.getInt("IdPais"));
                universitiesList.add(instance);
            }
        } catch (SQLException exception) {
            Logger.getLogger(UniversityDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        return universitiesList;
    }
}
       
    