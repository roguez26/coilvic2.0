/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.fei.coilvic.logic.institutionalRepresentative;
import mx.fei.coilvic.dataaccess.DatabaseManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 *
 * @author ivanr
 */
public class InstitutionalRepresentativeDAO implements IInstitutionalRepresentative {
    private final String INSERT_INSTITUTIONALREPRESENTATIVE = "INSERT INTO RepresentanteInstitucional(nombre, apellidoPaterno, apellidoMaterno, correo, telefono, iduniversidad) "
                + "values (?, ?, ?, ?, ?, ?)";
    private final String UPDATE_INSTITUTIONALREPRESENTATIVE = "UPDATE RepresentanteInstitucional SET nombre=?, apellidoPaterno=?, apellidoMaterno=?, correo=?, "
            + "telefono=?, iduniversidad=? WHERE idRepresentante=?";
    private final String DELETE_INSTITUTIONALREPRESENTATIVE = "DELETE FROM RepresentanteInstitucional WHERE idrepresentante=?";
    private final String GET_ALL_INSTITUTIONALREPRESENTATIVES = "SELECT * FROM RepresentanteInstitucional";
    
    
    @Override
    public int insertInstitutionalRepresentative(InstitutionalRepresentative institutionalRepresentative) {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = intializeStatement(connection, INSERT_INSTITUTIONALREPRESENTATIVE, institutionalRepresentative);         
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            databaseManager.closeConnection();
        }
        return result;
    } 
    
    @Override
    public int updateInstitutionalRepresentative(InstitutionalRepresentative institutionalRepresentative) {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = intializeStatement(connection, UPDATE_INSTITUTIONALREPRESENTATIVE, institutionalRepresentative);
            preparedStatement.setInt(7, institutionalRepresentative.getIdInstitutionalRepresentative());
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            databaseManager.closeConnection();
        }
        return result;
    }
    
    @Override
    public int deleteInstitutionalRepresentative(InstitutionalRepresentative institutionalRepresentative) {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(DELETE_INSTITUTIONALREPRESENTATIVE);
            preparedStatement.setInt(1, institutionalRepresentative.getIdInstitutionalRepresentative());
            return preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            databaseManager.closeConnection();
        }
        return result;
    }
    
    @Override
    public ArrayList<InstitutionalRepresentative> getAllInstitutionalRepresentatives() {
        ArrayList<InstitutionalRepresentative> representativesList = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(GET_ALL_INSTITUTIONALREPRESENTATIVES);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                InstitutionalRepresentative instance = new InstitutionalRepresentative();
                instance.setIdInstitutionalRepresentative(resultSet.getInt("IdRepresentante"));
                instance.setName(resultSet.getString("Nombre"));
                instance.setPaternalSurname(resultSet.getString("ApellidoPaterno"));
                instance.setMaternalSurname(resultSet.getString("ApellidoMaterno"));
                instance.setEmail(resultSet.getString("Correo"));
                instance.setPhoneNumber(resultSet.getString("Telefono"));
                instance.setIdUniversity(resultSet.getInt("IdUniversidad"));
                representativesList.add(instance);
            }
        } catch (SQLException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
           databaseManager.closeConnection();
        }
        return representativesList;
    }
    
    private PreparedStatement intializeStatement(Connection connection, String statement, InstitutionalRepresentative institutionalRepresentative) throws SQLException{
        PreparedStatement preparedStatement = null;
        
        preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, institutionalRepresentative.getName());
        preparedStatement.setString(2, institutionalRepresentative.getPaternalSurname());
        preparedStatement.setString(3, institutionalRepresentative.getMaternalSurname());
        preparedStatement.setString(4, institutionalRepresentative.getEmail());
        preparedStatement.setString(5, institutionalRepresentative.getPhoneNumber());
        preparedStatement.setInt(6, institutionalRepresentative.getIdUniversity()); 
        return preparedStatement;
    }
    
}
