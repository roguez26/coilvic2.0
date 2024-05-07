package mx.fei.coilvicapp.logic.student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;

public class StudentDAO implements IStudent {
    
    @Override
    public int registerStudent(Student student) throws DAOException {
        int result = 0;

        if (!checkEmailDuplication(student)) {
            result = insertStudentTransaction(student);
        }
        return result;        
    }
    
    @Override    
    public int updateStudent(Student newStudentInformation) throws DAOException {   
        int result = 0;

        if (!checkEmailDuplication(newStudentInformation)) {
            result = updateStudentTransaction(newStudentInformation);
        }
        return result;          
    }
    
    @Override
    public int deleteStudentById(int idStudent) throws DAOException {     
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "DELETE FROM estudiante WHERE idEstudiante = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, idStudent);
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible eliminar al estudiante", Status.ERROR);
        } finally {            
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }       
        return result;
    }    
    
    @Override
    public Student getStudentById(int idStudent) throws DAOException {        
        Student student = new Student();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM estudiante WHERE idEstudiante = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, idStudent);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                student = initializeStudent(resultSet);
            }
        } catch (SQLException exception) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener al estudiante", Status.ERROR);
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
                Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return student;          
    }    
    
    @Override
    public Student getStudentByEmail(String studentEmail) throws DAOException {
        Student student = new Student();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM estudiante WHERE correo = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, studentEmail);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                student = initializeStudent(resultSet);
            }
        } catch (SQLException exception) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener al estudiante", Status.ERROR);
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
                Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return student;          
    }
    
    @Override
    public ArrayList<Student> getAllStudents() throws DAOException {
        ArrayList<Student> students = new ArrayList<>();
        Student student;
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM estudiante";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                student = initializeStudent(resultSet);
                students.add(student);
            }
        } catch (SQLException exception) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible recuperar a los estudiantes", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return students;
    }    
    
    private boolean checkEmailDuplication(Student student) throws DAOException {
        Student studentAux;
        int idStudent = 0;

        try {
            studentAux = getStudentByEmail(student.getEmail());
            idStudent = studentAux.getIdStudent();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde.", Status.ERROR);
        }
        if (idStudent != student.getIdStudent() && idStudent > 0) {
            throw new DAOException("El correo ya se encuentra registrado", Status.WARNING);
        }
        return false;
    }
    
    private int insertStudentTransaction(Student student) throws DAOException {
        int result = -1;
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "INSERT INTO estudiante(nombre, apellidoPaterno, apellidoMaterno, correo, genero, ascendencia, idUniversidad)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?);";
        DatabaseManager databaseManager = new DatabaseManager();

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareCall(statement);
            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getPaternalSurname());
            preparedStatement.setString(3, student.getMaternalSurname());
            preparedStatement.setString(4, student.getEmail());
            preparedStatement.setString(5, student.getGender());
            preparedStatement.setString(6, student.getLineage());
            preparedStatement.setInt(7, student.getIdUniversity());
            preparedStatement.executeUpdate();         
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
            
        } catch (SQLException exception) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible registrar al estudiante", Status.ERROR);
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
                Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return result;    
    }
    
    private int updateStudentTransaction(Student newStudentInformation) throws DAOException {   
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE estudiante SET nombre = ?, apellidoPaterno = ?,"
                + " apellidoMaterno = ?, correo = ?, genero = ?, ascendencia = ? WHERE idEstudiante = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, newStudentInformation.getName());
            preparedStatement.setString(2, newStudentInformation.getPaternalSurname());
            preparedStatement.setString(3, newStudentInformation.getMaternalSurname());
            preparedStatement.setString(4, newStudentInformation.getEmail());
            preparedStatement.setString(5, newStudentInformation.getGender());
            preparedStatement.setString(6, newStudentInformation.getLineage());
            preparedStatement.setInt(7, newStudentInformation.getIdStudent());
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible actualizar al estudiante", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }        
        return result;
    }
    
        private Student initializeStudent(ResultSet resultSet) throws SQLException {        
        Student student = new Student();
        UniversityDAO universityDAO = new UniversityDAO();
        
        student.setIdStudent(resultSet.getInt("idEstudiante"));
        student.setName(resultSet.getString("nombre"));
        student.setPaternalSurname(resultSet.getString("apellidoPaterno"));
        student.setMaternalSurname(resultSet.getString("apellidoMaterno"));
        student.setEmail(resultSet.getString("correo"));
        student.setGender(resultSet.getString("genero"));
        student.setLineage(resultSet.getString("ascendencia"));
        int idUniversity = resultSet.getInt("IdUniversidad");
        try {
            student.setUniversity(universityDAO.getUniversityById(idUniversity));
        } catch (DAOException exception) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, exception);
        }
        return student;  
    }
       
}
