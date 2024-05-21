package mx.fei.coilvicapp.logic.student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.academicarea.AcademicAreaDAO;
import mx.fei.coilvicapp.logic.region.RegionDAO;
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
    public int registerStudentUV(StudentUV studentUV) throws DAOException {
        int result = 0;

        if (!checkEmailDuplication(studentUV)) {
            result = insertStudentUVTransaction(studentUV);
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
    public int updateStudentUV(StudentUV newStudentUVInformation) throws DAOException {
        int result = 0;
        Student student = this.initializeStudenFromStudentUV(newStudentUVInformation);
        if (!checkEmailDuplication(student)) {
            result = updateStudentUVTransaction(newStudentUVInformation);
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
            Log.getLogger(StudentDAO.class).error("No fue posible eliminar al estudiante por ID", exception);            
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
                Log.getLogger(StudentDAO.class).error("No fue posible cerrar las conexiones", exception);
            }
        }       
        return result;
    }    
    
    @Override
    public int deleteStudentUVById(int idStudent) throws DAOException {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "DELETE FROM estudianteuv WHERE idEstudiante = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, idStudent);
            result = preparedStatement.executeUpdate();    
            deleteStudentById(idStudent);
        } catch (SQLException exception) {
            Log.getLogger(StudentDAO.class).error("No fue posible eliminar al estudiante uv", exception);           
            throw new DAOException("No fue posible eliminar al estudiante uv", Status.ERROR);
        } finally {            
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
               Log.getLogger(StudentDAO.class).error("No fue posible cerrar las conexiones", exception);
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
            Log.getLogger(StudentDAO.class).error("No fue posible obtener al estudiante", exception);     
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
                Log.getLogger(StudentDAO.class).error("No fue posible cerrar las conexiones", exception);
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
            Log.getLogger(StudentDAO.class).error("No fue posible eliminar al estudiante uv", exception);     
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
                Log.getLogger(StudentDAO.class).error("No fue posible cerrar las conexiones", exception);
            }
        }
        return student;          
    }
    
    @Override
    public StudentUV getStudentUVByEnrollment(String studentUVEnrollment) throws DAOException {
        StudentUV studentUV = new StudentUV();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM estudianteuv WHERE matricula = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, studentUVEnrollment);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                studentUV = initializeStudentUV(resultSet);
            }
        } catch (SQLException exception) {
            Log.getLogger(StudentDAO.class).error("No fue posible eliminar al estudiante uv", exception);     
            throw new DAOException("No fue posible obtener al estudiante uv", Status.ERROR);
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
               Log.getLogger(StudentDAO.class).error("No fue posible cerrar las conexiones", exception);
            }
        }
        return studentUV;          
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
            Log.getLogger(StudentDAO.class).error("No fue posible obtener a los estudiantes", exception);     
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
               Log.getLogger(StudentDAO.class).error("No fue posible cerrar las conexiones", exception);
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
            Log.getLogger(StudentDAO.class).error("No fue posible registrar al estudiante", exception);
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
                Log.getLogger(StudentDAO.class).error("No fue posible cerrar las conexiones", exception);
            }
        }
        return result;    
    }
    
private int insertStudentUVTransaction(StudentUV studentUV) throws DAOException {
    int idEstudiante = -1;
    Connection connection = null;
    CallableStatement callableStatement = null;
    DatabaseManager databaseManager = new DatabaseManager();
    String statement = "{CALL insert_student_and_uv(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    try {
        connection = databaseManager.getConnection();
        callableStatement = connection.prepareCall(statement);

        callableStatement.setString(1, studentUV.getName());
        callableStatement.setString(2, studentUV.getPaternalSurname());
        callableStatement.setString(3, studentUV.getMaternalSurname());
        callableStatement.setString(4, studentUV.getEmail());
        callableStatement.setString(5, studentUV.getGender());
        callableStatement.setString(6, studentUV.getLineage());
        callableStatement.setInt(7, studentUV.getIdUniversity()); 
        callableStatement.setString(8, studentUV.getEnrollment());
        callableStatement.setInt(9, studentUV.getIdAcademicArea()); 
        callableStatement.setInt(10, studentUV.getIdRegion()); 
        

        callableStatement.registerOutParameter(11, Types.INTEGER);
        callableStatement.execute();

        idEstudiante = callableStatement.getInt(11);  
    } catch (SQLException exception) {
        Log.getLogger(StudentDAO.class).error("No fue posible registrar al estudiante uv", exception);
        throw new DAOException("No fue posible registrar al estudiante uv", Status.ERROR);
    } finally {
        System.out.println(idEstudiante);
        try {
            if (callableStatement != null) {
                callableStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException exception) {
            Log.getLogger(StudentDAO.class).error("No fue posible cerrar las conexiones", exception);
        }
    }
    return idEstudiante;         
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
            Log.getLogger(StudentDAO.class).error("No fue posible actualizar al estudiante", exception);
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
                Log.getLogger(StudentDAO.class).error("No fue posible cerrar las conexiones", exception);
            }
        }        
        return result;
    }
    
    private int updateStudentUVTransaction(StudentUV newStudentUVInformation) throws DAOException {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE estudianteuv SET idAreaAcademica = ?, idRegion = ? WHERE idEstudiante = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {            
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            Student student = initializeStudenFromStudentUV(newStudentUVInformation);           
            preparedStatement.setInt(1, newStudentUVInformation.getIdAcademicArea());
            preparedStatement.setInt(2, newStudentUVInformation.getIdRegion());
            preparedStatement.setInt(2, newStudentUVInformation.getIdStudent());
            updateStudentTransaction(student);
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Log.getLogger(StudentDAO.class).error("No fue posible actualizar al estudiante uv", exception);
            throw new DAOException("No fue posible actualizar al estudiante uv", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
               Log.getLogger(StudentDAO.class).error("No fue posible cerrar las conexiones", exception);
            }
        }        
        return result;        
    }
    
    private Student initializeStudenFromStudentUV(StudentUV studentUV) {
        Student student = new Student();
        
        student.setName(studentUV.getName());
        student.setPaternalSurname(studentUV.getPaternalSurname());
        student.setMaternalSurname(studentUV.getMaternalSurname());
        student.setEmail(studentUV.getEmail());
        student.setGender(studentUV.getGender());
        student.setLineage(studentUV.getLineage());
        student.setUniversity(studentUV.getUniversity());
        return student;
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
            Log.getLogger(StudentDAO.class).error(exception.getMessage(), exception);
        }
        return student;  
    }
    
    private StudentUV initializeStudentUV(ResultSet resultSet) throws SQLException {
        StudentUV studentUV = new StudentUV();
        Student student = new Student();
        AcademicAreaDAO academicAreaDAO = new AcademicAreaDAO();
        RegionDAO regionDAO = new RegionDAO();
        
        studentUV.setEnrollment(resultSet.getString("matricula"));
        System.out.println(studentUV.getEnrollment());
        int idAcademicArea = resultSet.getInt("idAreaAcademica");
        int idRegion = resultSet.getInt("idRegion");
        int idEstudiante = resultSet.getInt("idEstudiante");
        try {
            studentUV.setAcademicArea(academicAreaDAO.getAcademicAreaById(idAcademicArea));
            studentUV.setRegion(regionDAO.getRegionById(idRegion));
            student = getStudentById(idEstudiante);
        } catch (DAOException exception) {
            Log.getLogger(StudentDAO.class).error(exception.getMessage(), exception);
        }
        studentUV.setName(student.getName());
        studentUV.setPaternalSurname(student.getPaternalSurname());
        studentUV.setMaternalSurname(student.getMaternalSurname());
        studentUV.setEmail(student.getEmail());
        studentUV.setGender(studentUV.getGender());
        studentUV.setLineage(student.getLineage());
        studentUV.setUniversity(student.getUniversity());
        return studentUV;        
    }
        
}
