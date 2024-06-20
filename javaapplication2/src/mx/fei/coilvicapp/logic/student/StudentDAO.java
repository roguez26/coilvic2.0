package mx.fei.coilvicapp.logic.student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.academicarea.AcademicAreaDAO;
import mx.fei.coilvicapp.logic.region.RegionDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;

public class StudentDAO implements IStudent {

    /**
     * Registra un nuevo estudiante en la base de datos.
     * 
     * @param student El objeto Student que representa al estudiante a registrar
     * @return El número de registros afectados en la base de datos (debería ser 1)
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */    
    @Override
    public int registerStudent(Student student) throws DAOException {
        int result = 0;

        if (!checkEmailDuplication(student)) {
            result = insertStudentTransaction(student);
        }
        return result;
    }

    /**
     * Verifica si se cumplen todas las precondiciones necesarias en la base de datos.
     * 
     * @return true si se cumplen las precondiciones, false si no se cumplen
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public boolean checkPreconditions() throws DAOException {
        UniversityDAO universityDAO = new UniversityDAO();
        AcademicAreaDAO academicAreaDAO = new AcademicAreaDAO();
        RegionDAO regionDAO = new RegionDAO();

        return universityDAO.isThereAtLeastOneUniversity()
                && academicAreaDAO.isThereAtLeastOneAcademicArea()
                && regionDAO.isThereAtLeastOneRegion();
    }   

    /**
     * Registra un nuevo estudiante UV en la base de datos.
     * 
     * @param studentUV El objeto StudentUV que representa al estudiante UV a registrar
     * @return El número de registros afectados en la base de datos (debería ser 1)
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public int registerStudentUV(StudentUV studentUV) throws DAOException {
        int result = 0;

        if (!checkEmailDuplication(studentUV) && !checkEnrollmentDuplication(studentUV)) {
            result = insertStudentUVTransaction(studentUV);
        }
        return result;
    }

    /**
     * Actualiza la información de un estudiante en la base de datos.
     * 
     * @param newStudentInformation El objeto Student con la nueva información
     * @return El número de registros actualizados en la base de datos (debería ser 1)
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */    
    @Override
    public int updateStudent(Student newStudentInformation) throws DAOException {
        int result = 0;

        if (!checkEmailDuplication(newStudentInformation)) {
            result = updateStudentTransaction(newStudentInformation);
        }
        return result;
    }

    /**
     * Actualiza la información de un estudiante UV en la base de datos.
     * 
     * @param newStudentUVInformation El objeto StudentUV con la nueva información
     * @return El número de registros actualizados en la base de datos (debería ser 1)
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */    
    @Override
    public int updateStudentUV(StudentUV newStudentUVInformation) throws DAOException {
        int result = 0;
        Student student = this.initializeStudenFromStudentUV(newStudentUVInformation);
        if (!checkEmailDuplication(newStudentUVInformation) && !checkEnrollmentDuplication(newStudentUVInformation)) {
            result = updateStudentUVTransaction(newStudentUVInformation);
        }
        return result;
    }

    /**
     * Elimina un estudiante de la base de datos por su ID.
     * 
     * @param idStudent El ID del estudiante a eliminar
     * @return El número de registros eliminados en la base de datos (debería ser 1)
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public int deleteStudentById(int idStudent) throws DAOException {
        int result = -1;
        String statement = "DELETE FROM estudiante WHERE idEstudiante = ?";
        DatabaseManager databaseManager = new DatabaseManager();

        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, idStudent);
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(StudentDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar al estudiante", Status.ERROR);
        }
        return result;
    }

    /**
     * Elimina un estudiante universitario de la base de datos por su ID.
     * 
     * @param idStudent El ID del estudiante UV a eliminar
     * @return El número de registros eliminados en la base de datos (debería ser 1)
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public int deleteStudentUVById(int idStudent) throws DAOException {
        int result = -1;
        String deleteStudentQuery = "DELETE FROM estudianteuv WHERE idEstudiante = ?";
        DatabaseManager databaseManager = new DatabaseManager();

        try (Connection connection = databaseManager.getConnection(); PreparedStatement deleteStudentStatement = connection.prepareStatement(deleteStudentQuery)) {
            deleteStudentStatement.setInt(1, idStudent);
            result = deleteStudentStatement.executeUpdate();
            deleteStudentById(idStudent);
        } catch (SQLException exception) {
            Log.getLogger(StudentDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar al estudiante uv", Status.ERROR);
        }

        return result;
    }

     /**
     * Obtiene un estudiante por su ID desde la base de datos.
     * 
     * @param idStudent El ID del estudiante a buscar
     * @return El objeto Student si se encuentra, null si no se encuentra
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public Student getStudentById(int idStudent) throws DAOException {
        Student student = null;
        String selectQuery = "SELECT * FROM estudiante WHERE idEstudiante = ?";
        DatabaseManager databaseManager = new DatabaseManager();

        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, idStudent);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    student = initializeStudent(resultSet);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(StudentDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener al estudiante", Status.ERROR);
        }
        return student;
    }

    /**
     * Obtiene un estudiante por su dirección de correo electrónico desde la base de datos.
     * 
     * @param studentEmail La dirección de correo electrónico del estudiante a buscar
     * @return El objeto Student si se encuentra
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public Student getStudentByEmail(String studentEmail) throws DAOException {
        Student student = new Student();
        String selectQuery = "SELECT * FROM estudiante WHERE correo = ?";
        DatabaseManager databaseManager = new DatabaseManager();

        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, studentEmail);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    student = initializeStudent(resultSet);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(StudentDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener al estudiante", Status.ERROR);
        }
        return student;
    }

    /**
     * Obtiene un estudiante universitario por su matrícula desde la base de datos.
     * 
     * @param studentUVEnrollment La matrícula del estudiante UV a buscar
     * @return El objeto StudentUV si se encuentra
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public StudentUV getStudentUVByEnrollment(String studentUVEnrollment) throws DAOException {
        StudentUV studentUV = new StudentUV();
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT e.idEstudiante, e.nombre, e.apellidoPaterno, e.apellidoMaterno,"
                + " e.correo, e.genero, e.ascendencia, e.idUniversidad, eu.matricula, eu.idAreaAcademica,"
                + " eu.idRegion FROM estudiante e JOIN estudianteuv eu ON e.idEstudiante = eu.idEstudiante"
                + " WHERE eu.matricula = ?";
        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, studentUVEnrollment);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    studentUV = initializeStudentUV(resultSet);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(StudentDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener al estudiante uv", Status.ERROR);
        }
        return studentUV;
    }

    /**
     * Obtiene todos los estudiantes registrados en la base de datos.
     * 
     * @return Una lista de objetos Student con todos los estudiantes encontrados
     * @throws DAOException si ocurre un error durante el acceso a la base de datos
     */
    @Override
    public ArrayList<Student> getAllStudents() throws DAOException {
        ArrayList<Student> students = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT * FROM estudiante";
        try (Connection connection = databaseManager.getConnection(); 
                PreparedStatement preparedStatement = connection.prepareStatement(statement); 
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Student student = initializeStudent(resultSet);
                students.add(student);
            }
        } catch (SQLException exception) {
            Log.getLogger(StudentDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar a los estudiantes", Status.ERROR);
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

    private boolean checkEnrollmentDuplication(StudentUV studentUV) throws DAOException {
        StudentUV studentUVAux;
        int idStudentUV = 0;

        try {
            studentUVAux = getStudentUVByEnrollment(studentUV.getEnrollment());
            idStudentUV = studentUVAux.getIdStudent();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde.", Status.ERROR);
        }
        if (idStudentUV != studentUV.getIdStudent() && idStudentUV > 0) {
            throw new DAOException("La matricula ya se encuentra registrada", Status.WARNING);
        }
        return false;
    }

    private int insertStudentTransaction(Student student) throws DAOException {
        int result = -1;
        String statement = "INSERT INTO estudiante(nombre, apellidoPaterno, apellidoMaterno, correo,"
                + " genero, ascendencia, idUniversidad) VALUES(?, ?, ?, ?, ?, ?, ?)";
        DatabaseManager databaseManager = new DatabaseManager();

        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement,
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getPaternalSurname());
            preparedStatement.setString(3, student.getMaternalSurname());
            preparedStatement.setString(4, student.getEmail());
            preparedStatement.setString(5, student.getGender());
            preparedStatement.setString(6, student.getLineage());
            preparedStatement.setInt(7, student.getIdUniversity());
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(StudentDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible insertar al estudiante", Status.ERROR);
        }
        return result;
    }

    private int insertStudentUVTransaction(StudentUV studentUV) throws DAOException {
        int idEstudiante = -1;
        String statement = "{CALL registrar_estudiante_uv(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        DatabaseManager databaseManager = new DatabaseManager();

        try (Connection connection = databaseManager.getConnection(); CallableStatement callableStatement = connection.prepareCall(statement)) {
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
            callableStatement.execute();
            try (ResultSet resultSet = callableStatement.getResultSet()) {
                if (resultSet.next()) {
                    idEstudiante = resultSet.getInt(1);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(StudentDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible insertar al estudiante uv", Status.ERROR);
        }

        return idEstudiante;
    }

    private int updateStudentTransaction(Student newStudentInformation) throws DAOException {
        int rowsAffected = -1;
        String statement = "UPDATE estudiante SET nombre = ?, apellidoPaterno = ?,"
                + " apellidoMaterno = ?, correo = ?, genero = ?, ascendencia = ? WHERE idEstudiante = ?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

            preparedStatement.setString(1, newStudentInformation.getName());
            preparedStatement.setString(2, newStudentInformation.getPaternalSurname());
            preparedStatement.setString(3, newStudentInformation.getMaternalSurname());
            preparedStatement.setString(4, newStudentInformation.getEmail());
            preparedStatement.setString(5, newStudentInformation.getGender());
            preparedStatement.setString(6, newStudentInformation.getLineage());
            preparedStatement.setInt(7, newStudentInformation.getIdStudent());

            rowsAffected = preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            Log.getLogger(StudentDAO.class).error("No fue posible actualizar al estudiante", exception);
            throw new DAOException("No fue posible actualizar al estudiante", Status.ERROR);
        }

        return rowsAffected;
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
        AcademicAreaDAO academicAreaDAO = new AcademicAreaDAO();
        UniversityDAO universityDAO = new UniversityDAO();
        RegionDAO regionDAO = new RegionDAO();

        studentUV.setIdStudent(resultSet.getInt("idEstudiante"));
        studentUV.setName(resultSet.getString("nombre"));
        studentUV.setPaternalSurname(resultSet.getString("apellidoPaterno"));
        studentUV.setMaternalSurname(resultSet.getString("apellidoMaterno"));
        studentUV.setEmail(resultSet.getString("correo"));
        studentUV.setGender(resultSet.getString("genero"));
        studentUV.setLineage(resultSet.getString("ascendencia"));
        studentUV.setEnrollment(resultSet.getString("matricula"));

        try {
            studentUV.setUniversity(universityDAO.getUniversityById(resultSet.getInt("idAreaAcademica")));
            studentUV.setAcademicArea(academicAreaDAO.getAcademicAreaById(resultSet.getInt("idAreaAcademica")));
            studentUV.setRegion(regionDAO.getRegionById(resultSet.getInt("idRegion")));
        } catch (DAOException exception) {
            Log.getLogger(StudentDAO.class).error(exception.getMessage(), exception);
        }
        return studentUV;
    }

}