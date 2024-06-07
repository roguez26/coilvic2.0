package mx.fei.coilvicapp.logic.professor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.mail.MessagingException;
import log.Log;
import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.academicarea.AcademicAreaDAO;
import mx.fei.coilvicapp.logic.emailSender.EmailSender;
import mx.fei.coilvicapp.logic.emailSender.EmailSenderDAO;
import mx.fei.coilvicapp.logic.hiringcategory.HiringCategoryDAO;
import mx.fei.coilvicapp.logic.hiringtype.HiringTypeDAO;
import mx.fei.coilvicapp.logic.region.RegionDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.PasswordGenerator;
import mx.fei.coilvicapp.logic.implementations.Status;
import mx.fei.coilvicapp.logic.user.User;
import mx.fei.coilvicapp.logic.user.UserDAO;

public class ProfessorDAO implements IProfessor {
           
    @Override
    public boolean checkPreconditions() throws DAOException {
        UniversityDAO universityDAO = new UniversityDAO();
        AcademicAreaDAO academicAreaDAO = new AcademicAreaDAO();
        HiringCategoryDAO hiringCategoryDAO = new HiringCategoryDAO();
        HiringTypeDAO hiringTypeDAO = new HiringTypeDAO();
        RegionDAO regionDAO = new RegionDAO();
        
        return universityDAO.isThereAtLeastOneUniversity() &&
                academicAreaDAO.isThereAtLeastOneAcademicArea() &&
                hiringCategoryDAO.isThereAtLeastOneHiringCategory() &&
                hiringTypeDAO.isThereAtLeastOneHiringType() &&
                regionDAO.isThereAtLeastOneRegion();
    }     
    
    private boolean checkEmailDuplication(Professor professor) throws DAOException {
        Professor professorAux;
        int idProfessor = 0;

        try {
            professorAux = getProfessorByEmail(professor.getEmail());
            idProfessor = professorAux.getIdProfessor();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde.", Status.ERROR);
        }
        if (idProfessor != professor.getIdProfessor() && idProfessor > 0) {
            throw new DAOException("El correo ya se encuentra registrado", Status.WARNING);
        }
        return false;
    }
    
    private boolean checkPersonalNumberDuplication(ProfessorUV professorUV) throws DAOException {
        ProfessorUV professorUVAux;
        int idProfessorUV = 0;

        try {
            professorUVAux = getProfessorUVByPersonalNumber(professorUV.getPersonalNumber());
            idProfessorUV = professorUVAux.getIdProfessor();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde.", Status.ERROR);
        }
        if (idProfessorUV != professorUV.getIdProfessor() && idProfessorUV > 0) {
            throw new DAOException("El numero de personal ya se encuentra registrado", Status.WARNING);
        }
        return false;
    }   
       
    
    @Override
    public int registerProfessor(Professor professor) throws DAOException {
        int result = 0;

        if (!checkEmailDuplication(professor)) {
            result = insertProfessorTransaction(professor);
        }
        return result;        
    }
    
    @Override
    public int registerProfessorUV(ProfessorUV professorUV) throws DAOException {
        int result = 0;

        if (!checkEmailDuplication(professorUV) && !checkPersonalNumberDuplication(professorUV)) {
            result = insertProfessorUVTransaction(professorUV);
        }
        return result;         
    }
    
    private int insertProfessorTransaction(Professor professor) throws DAOException {
        int result = -1;
        String statement = "INSERT INTO profesor(nombre, apellidoPaterno, apellidoMaterno, correo, genero, telefono, idUniversidad) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?);";
        DatabaseManager databaseManager = new DatabaseManager();

        try (Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, professor.getName());
            preparedStatement.setString(2, professor.getPaternalSurname());
            preparedStatement.setString(3, professor.getMaternalSurname());
            preparedStatement.setString(4, professor.getEmail());
            preparedStatement.setString(5, professor.getGender());
            preparedStatement.setString(6, professor.getPhoneNumber());
            preparedStatement.setInt(7, professor.getIdUniversity());
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar al profesor", Status.ERROR);
        }
        return result;
    }
    
    private int insertProfessorUVTransaction(ProfessorUV professorUV) throws DAOException {
        int idProfessor = -1;
        String statement = "{CALL registrar_profesor_uv(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        DatabaseManager databaseManager = new DatabaseManager();
        try (Connection connection = databaseManager.getConnection();
                CallableStatement callableStatement = connection.prepareCall(statement)) {
            callableStatement.setString(1, professorUV.getName());
            callableStatement.setString(2, professorUV.getPaternalSurname());
            callableStatement.setString(3, professorUV.getMaternalSurname());
            callableStatement.setString(4, professorUV.getEmail());
            callableStatement.setString(5, professorUV.getGender());
            callableStatement.setString(6, professorUV.getPhoneNumber());
            callableStatement.setInt(7, professorUV.getIdUniversity()); 
            callableStatement.setInt(8, professorUV.getIdHiringCategory()); 
            callableStatement.setInt(9, professorUV.getIdHiringType()); 
            callableStatement.setInt(10, professorUV.getIdAcademicArea()); 
            callableStatement.setInt(11, professorUV.getIdRegion());
            callableStatement.setInt(12, professorUV.getPersonalNumber());
            callableStatement.execute();
            try (ResultSet resultSet = callableStatement.getResultSet()) {
                if (resultSet.next()) {
                    idProfessor = resultSet.getInt(1);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);     
            throw new DAOException("No fue posible insertar al profesor uv", Status.ERROR);
        } 
        return idProfessor;         
    }

    
    @Override
    public int updateProfessor(Professor newProfessorInformation) throws DAOException {   
        int result = 0;

        if (!checkEmailDuplication(newProfessorInformation)) {
            result = updateProfessorTransaction(newProfessorInformation);
        }
        return result;          
    }
    
    @Override
    public int updateProfessorUV(ProfessorUV newProfessorUVInformation) throws DAOException{   
        int result = 0;

        if (!checkEmailDuplication(newProfessorUVInformation)) {
            result = updateProfessorTransaction(newProfessorUVInformation);
        }
        return result;          
    }    
    
    public int updateProfessorTransaction(Professor newProfessorInformation) throws DAOException {   
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "UPDATE profesor SET nombre = ?, apellidoPaterno = ?,"
                + " apellidoMaterno = ?, correo = ?, genero = ?, telefono = ? WHERE idProfesor = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, newProfessorInformation.getName());
            preparedStatement.setString(2, newProfessorInformation.getPaternalSurname());
            preparedStatement.setString(3, newProfessorInformation.getMaternalSurname());
            preparedStatement.setString(4, newProfessorInformation.getEmail());
            preparedStatement.setString(5, newProfessorInformation.getGender());
            preparedStatement.setString(6, newProfessorInformation.getPhoneNumber());
            preparedStatement.setInt(7, newProfessorInformation.getIdProfessor());
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible actualizar al profesor", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            }
        }        
        return result;
    }

    @Override
    public int deleteProfessorByID(int idProfessor) throws DAOException {     
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "DELETE FROM profesor WHERE idProfesor = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, idProfessor);
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar al profesor", Status.ERROR);
        } finally {            
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            }
        }       
        return result;
    }
    
    @Override
    public int deleteProfessorUVByID(int idProfessor) throws DAOException {     
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String statement = "DELETE FROM profesoruv WHERE idProfesor = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, idProfessor);
            result = preparedStatement.executeUpdate();      
        } catch (SQLException exception) {
            Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar al profesor", Status.ERROR);
        } finally {            
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }               
            } catch (SQLException exception) {
                Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            }
        }       
        return result;
    }    

    @Override
    public Professor getProfessorById(int idProfessor) throws DAOException {        
        Professor professor = new Professor();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM profesor WHERE idProfesor = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, idProfessor);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                professor = initializeProfessor(resultSet);
            }
        } catch (SQLException exception) {
            Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener al profesor", Status.ERROR);
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
                Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            }
        }
        return professor;          
    }
    
    @Override
    public Professor getProfessorByEmail(String professorEmail) throws DAOException {
        Professor professor = new Professor();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM profesor WHERE correo = ?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, professorEmail);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                professor = initializeProfessor(resultSet);
            }
        } catch (SQLException exception) {
            Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener al profesor", Status.ERROR);
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
                Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            }
        }
        return professor;          
    }
    
    @Override
    public ProfessorUV getProfessorUVByPersonalNumber(int personalNumber) throws DAOException {
        ProfessorUV professorUV = new ProfessorUV();
        String statement = "SELECT p.idProfesor, p.nombre, p.apellidoPaterno, p.apellidoMaterno, "
                + "p.correo, p.genero, p.telefono, p.estado, p.idUniversidad, pu.noPersonal, "
                + "pu.idCategoriaContratación, pu.idTipoContratación, pu.idAreaAcademica, pu.idRegion "
                + "FROM profesor p "
                + "JOIN profesorUV pu ON p.idProfesor = pu.idProfesor "
                + "WHERE pu.noPersonal = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        try (Connection connection = databaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

            preparedStatement.setInt(1, personalNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    professorUV = initializeProfessorUV(resultSet);                    
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener al profesor", Status.ERROR);
        }
        return professorUV;
    }

    
    @Override
    public ArrayList<Professor> getAllProfessors() throws DAOException {
        ArrayList<Professor> professors = new ArrayList<>();
        Professor professor;
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM profesor";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                professor = initializeProfessor(resultSet);
                professors.add(professor);
            }
        } catch (SQLException exception) {
            Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar a los profesores", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            }
            databaseManager.closeConnection();
        }
        return professors;
    }
    
    @Override
    public ArrayList<Professor> getProfessorsByPendingStatus() throws DAOException {
        ArrayList<Professor> professors = new ArrayList<>();
        Professor professor;
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM profesor where estado = 'Pendiente'";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                professor = initializeProfessor(resultSet);
                professors.add(professor);
            }
        } catch (SQLException exception) {
            Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible recuperar a los profesores por su estado", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            }
            databaseManager.closeConnection();
        }
        return professors;        
    }
    
    @Override
    public int acceptProfessor(Professor professor) throws DAOException {
        int result = -1;
        User user = new User();
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "UPDATE profesor set estado = 'Aceptado', idUsuario = ? where idProfesor = ?;";
        
        try (Connection connection = databaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            user = assignUser(professor);
            preparedStatement.setInt(1, user.getIdUser());
            preparedStatement.setInt(2, professor.getIdProfessor());
            result = preparedStatement.executeUpdate(); 
            professor.setUser(user);
            if (result > 0) {
                invokeSendEmail(professor);
            }
        } catch (SQLException exception) {
            Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible aceptar al profesor", Status.ERROR);
        } catch (MessagingException exception) { 
            undoValidateProfessor(professor);
            throw new DAOException("No fue posible mandar el correo", Status.ERROR);
        }
        return result;
    }
    
    private int undoValidateProfessor(Professor professor) throws DAOException {
        int result = 0;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "UPDATE profesor set estado = 'Pendiente' where idProfesor = ?;";
        
        try (Connection connection = databaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, professor.getIdProfessor());
            result = preparedStatement.executeUpdate(); 
        } catch (SQLException exception) {
            Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible rechazar al profesor", Status.ERROR);
        } 
        return result;
    }
    
    @Override
    public int rejectProfessor(Professor professor) throws DAOException {
        int result;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "UPDATE profesor set estado = 'Rechazado' where idProfesor = ?;";
        
        try (Connection connection = databaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, professor.getIdProfessor());
            result = preparedStatement.executeUpdate(); 
        } catch (SQLException exception) {
            Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible rechazar al profesor", Status.ERROR);
        } 
        return result;        
    }
    
    private Professor initializeProfessor(ResultSet resultSet) throws SQLException {        
        Professor professor = new Professor();
        UniversityDAO universityDAO = new UniversityDAO();        
        UserDAO userDAO = new UserDAO();
        
        professor.setIdProfessor(resultSet.getInt("idProfesor"));
        professor.setName(resultSet.getString("nombre"));
        professor.setPaternalSurname(resultSet.getString("apellidoPaterno"));
        professor.setMaternalSurname(resultSet.getString("apellidoMaterno"));
        professor.setEmail(resultSet.getString("correo"));
        professor.setGender(resultSet.getString("genero"));
        professor.setPhoneNumber(resultSet.getString("telefono"));
        professor.setState(resultSet.getString("estado"));
        int idUniversity = resultSet.getInt("IdUniversidad");
        int idUser = resultSet.getInt("idUsuario");
        try {
            professor.setUser(userDAO.getUserById(idUser));
            professor.setUniversity(universityDAO.getUniversityById(idUniversity));
        } catch (DAOException exception) {
            Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
        }
        return professor;  
    }
    
    private ProfessorUV initializeProfessorUV(ResultSet resultSet) throws SQLException {
        ProfessorUV professorUV = new ProfessorUV();
        UniversityDAO universityDAO = new UniversityDAO();
        AcademicAreaDAO academicAreaDAO = new AcademicAreaDAO();
        HiringCategoryDAO hiringCategoryDAO = new HiringCategoryDAO();
        HiringTypeDAO hiringTypeDAO = new HiringTypeDAO();
        RegionDAO regionDAO = new RegionDAO();
        UserDAO userDAO = new UserDAO();
        
        professorUV.setIdProfessor(resultSet.getInt("idProfesor"));
        professorUV.setName(resultSet.getString("nombre"));
        professorUV.setPaternalSurname(resultSet.getString("apellidoPaterno"));
        professorUV.setMaternalSurname(resultSet.getString("apellidoMaterno"));
        professorUV.setEmail(resultSet.getString("correo"));
        professorUV.setGender(resultSet.getString("genero"));
        professorUV.setPhoneNumber(resultSet.getString("telefono"));
        professorUV.setState(resultSet.getString("estado"));
        professorUV.setPersonalNumber(resultSet.getInt("noPersonal"));
        
        try {              
            professorUV.setUniversity(universityDAO.getUniversityById(resultSet.getInt("idUniversidad")));
            professorUV.setUser(userDAO.getUserById(resultSet.getInt("idUsuario")));
            professorUV.setHiringCategory(hiringCategoryDAO.getHiringCategoryById(resultSet.getInt("idCategoriaContratación")));
            professorUV.setHiringType(hiringTypeDAO.getHiringTypeById(resultSet.getInt("idTipoContratación")));
            professorUV.setAcademicArea(academicAreaDAO.getAcademicAreaById(resultSet.getInt("idAreaAcademica")));
            professorUV.setRegion(regionDAO.getRegionById(resultSet.getInt("idRegion")));
        } catch (DAOException exception) {
            Log.getLogger(ProfessorDAO.class).error(exception.getMessage(), exception);
        } 
        return professorUV;
    }
    
    public User assignUser(Professor professor) throws DAOException {
        int result = 0;
        User user = new User();
        UserDAO userDAO = new UserDAO();
        
        user.setType("P");
        user.setPassword(PasswordGenerator.generatePassword());        
        result = userDAO.registerUser(user);
        user.setIdUser(result);
        return user;
    }
    
    public int deleteUser(Professor professor) throws DAOException {
        int result = 0;
        UserDAO userDAO = new UserDAO();
        
        userDAO.deleteUser(professor.getIdProfessor());
        
        return result;
    }
    
    private void invokeSendEmail(Professor professor) throws DAOException, MessagingException {
        EmailSender emailSender = initializeEmailSender(professor);
        if (emailSender.createEmail()) {
            emailSender.sendEmail();
        } else {
            throw new MessagingException("El correo no pudo ser creado");
        }
        
    }

    private EmailSender initializeEmailSender(Professor professor) {
        EmailSender emailSender = new EmailSender();
        emailSender.setSubject("Aceptacion en COIL VIC");
        emailSender.setMessage("Ha sido aceptado en el proyecto COIL VIC,"
                + " se le ha asignado un usuario y contraseña.\n Usuario: " + professor.getEmail() 
                + "\nContraseña: " + professor.getUser().getPassword());
        emailSender.setReceiver(professor);
        return emailSender;
    }   
    
}