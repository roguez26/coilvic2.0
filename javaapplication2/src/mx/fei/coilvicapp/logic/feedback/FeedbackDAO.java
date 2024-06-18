package mx.fei.coilvicapp.logic.feedback;

import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.student.Student;

public class FeedbackDAO implements IFeedback {

    private boolean checkQuestionDuplication(Question question) throws DAOException {
        Question axuQuestion;
        int idQuestion = 0;

        try {
            axuQuestion = getQuestionByQuestionText(question.getQuestionText());
            idQuestion = axuQuestion.getIdQuestion();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde", 
                    Status.ERROR);
        }
        if (idQuestion != question.getIdQuestion() && idQuestion > 0 && 
                axuQuestion.getQuestionType().equals(question.getQuestionType())) {
            throw new DAOException("La pregunta ya se encuentra registrada", Status.WARNING);
        }
        return false;
    }

    /**
     * Este método sirve para revisar si un estudiante ha completado su retroalimentacion previa
     * @param student Éste es el estudiante del que se desea saber si ha completado
     * @param collaborativeProject Éste es el proyecto sobre el que se desea saber
     * @return true en caso de que el estudiante haya completado la retroalimentación, false
     * en caso de que no
     * @throws DAOException Puede lanzar una DAOException en caso que ocurra una excepción del tipo
     * SQL
     */
    
    @Override
    public boolean hasCompletedPreForm(Student student, CollaborativeProject collaborativeProject) 
            throws DAOException {
        return checkIsFormDone(student.getIdStudent(), 
                collaborativeProject.getIdCollaborativeProject(), "Estudiante-PRE");
    }
    
    /**
     * Este método sirve para revisar si un estudiante ha completado su retroalimentacion posterior
     * @param student Éste es el estudiante del que se desea saber si ha completado
     * @param collaborativeProject Éste es el proyecto sobre el que se desea saber
     * @return true en caso de que el estudiante haya completado la retroalimentación, false
     * en caso de que no
     * @throws DAOException Puede lanzar una DAOException en caso que ocurra una excepción del tipo
     * SQL
     */

    @Override
    public boolean hasCompletedPostForm(Student student, CollaborativeProject 
            collaborativeProject) throws DAOException {
        return checkIsFormDone(student.getIdStudent(), 
                collaborativeProject.getIdCollaborativeProject(), "Estudiante-POST");
    }
    
    /**
     * Este método sirve para revisar si un profesor ha completado su retroalimentacion 
     * @param professor Éste es el profesor del que se desea saber si ha completado
     * @param collaborativeProject Éste es el proyecto sobre el que se desea saber
     * @return true en caso de que el estudiante haya completado la retroalimentación, false
     * en caso de que no
     * @throws DAOException Puede lanzar una DAOException en caso que ocurra una excepción del tipo
     * SQL
     */

    @Override
    public boolean hasCompletedProfessorForm(Professor professor, CollaborativeProject 
            collaborativeProject) throws DAOException {
        return checkIsFormDone(professor.getIdProfessor(), 
                collaborativeProject.getIdCollaborativeProject(), "Profesor");
    }

    private boolean checkIsFormDone(int idParticipant, int idCollaborativeProject, String type) 
            throws DAOException {
        boolean result = false;
        String tableName = "RespuestaEstudiante";
        String idType = "idEstudiante";

        if (type.equals("Profesor")) {
            tableName = "RespuestaProfessor";
            idType = "idProfesor";
        }
        String statement = "SELECT 1 FROM " + tableName + " re "
                + "JOIN Pregunta p ON re.idPregunta = p.idPregunta "
                + "WHERE " + idType + " = ? "
                + "AND re.idProyectoColaborativo = ? "
                + "AND p.tipo = ? "
                + "LIMIT 1";
        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, idParticipant);
            preparedStatement.setInt(2, idCollaborativeProject);
            preparedStatement.setString(3, type);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                result = resultSet.next();
            }
        } catch (SQLException exception) {
            Log.getLogger(FeedbackDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible hacer la verificacion", Status.ERROR);
        }
        return result;
    }

    
    /**
     * Este método se utiliza para actualizar preguntas de la base de datos
     * @param question Ésta será la pregunta que contiene los nuevos datos de la pregunta
     * @return -1 en caso de que no se pueda actualizar, 1 en caso de que la actualización
     * result exitosa
     * @throws DAOException Puede lanzar una DAOException en caso que ocurra una excepción del tipo
     * SQL
     */
    
    @Override
    public int updateQuestionTransaction(Question question) throws DAOException {
        int result = -1;
        String statement = "UPDATE Pregunta SET pregunta=?, tipo = ? WHERE idPregunta=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement);) {
            preparedStatement.setString(1, question.getQuestionText());
            preparedStatement.setString(2, question.getQuestionType());
            preparedStatement.setInt(3, question.getIdQuestion());
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(FeedbackDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible actualizar la pregunta", Status.ERROR);
        }
        return result;
    }
    
    /**
     * Este método se utiliza para obtener preguntas con base en su tipo
     * @param type Éste es el tipo con el cual se desea realizar la consulta
     * @return Retornará un arreglo de preguntas con todas las preguntas que
     * coincidan
     * @throws DAOException Puede lanzar una DAOException en caso que ocurra una excepción del tipo
     * SQL 
     */

    @Override
    public ArrayList<Question> getQuestionByType(String type) throws DAOException {
        ArrayList<Question> questionsList = new ArrayList<>();
        String statement;

        if (type.equals("Estudiante")) {
            statement = "SELECT * FROM pregunta WHERE tipo LIKE 'Estudiante-%'";
        } else {
            statement = "SELECT * FROM pregunta WHERE tipo=?";
        }

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement)) {
            if (!type.equals("Estudiante")) {
                preparedStatement.setString(1, type);
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Question question = new Question();
                    question.setIdQuestion(resultSet.getInt("idPregunta"));
                    question.setQuestionText(resultSet.getString("pregunta"));
                    question.setQuestionType(resultSet.getString("tipo"));
                    questionsList.add(question);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(FeedbackDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener las preguntas por tipo", 
                    Status.ERROR);
        }
        return questionsList;
    }
    
    /**
     * Este método se utiliza para poder registrar nuevas preguntas en la base de datos
     * @param question Ésta es la nueva pregunta que será registrada, question contiene
     * los datos de la nueva pregunta
     * @return 0 en caso de que no pueda ser registrada, de otro modo retornará el id
     * generado automáticamente por la base de datos
     * @throws DAOException Puede lanzar una DAOException en caso de que la pregunta
     * ya se encuentre en la base de datos o en caso de que ocurra una excepción
     * del tipo SQL
     */

    @Override
    public int registerQuestion(Question question) throws DAOException {
        int result = 0;

        if (!checkQuestionDuplication(question)) {
            result = insertQuestionTransaction(question);
        }
        return result;
    }
    
    /**
     * Este método se utiliza para poder eliminar preguntas de la base de datos
     * @param question Ésta es la pregunta que se desea eliminar
     * @return 0 en caso de que no pueda ser eliminada, 1 en caso de que la 
     * eliminación resulte exitosa
     * @throws DAOException Puede lanzar una DAOException en caso de que la pregunta
     * ya aparezca en una retroalimentación o en caso de que ocurra una excepción
     * del tipo SQL
     */

    @Override
    public int deleteQuestion(Question question) throws DAOException {
        int result = 0;
        if (!hasBeenUsed(question)) {
            result = deleteQuestionTransaction(question);
        } else {
            throw new DAOException("No es posible eliminar la pregunta debido a que ya ha sido "
                    + "respondida por los participantes", Status.WARNING);
        }
        
        return result;
    }

    private boolean hasBeenUsed(Question question) throws DAOException {
        boolean result = false;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT EXISTS (SELECT 1 FROM respuestaestudiante WHERE idPregunta = ?)"
                + " OR EXISTS (SELECT 1 FROM respuestaprofessor WHERE idPregunta = ?) AS usada";

        try (Connection connection = databaseManager.getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement)) {

            preparedStatement.setInt(1, question.getIdQuestion());
            preparedStatement.setInt(2, question.getIdQuestion());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getBoolean("usada");
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(FeedbackDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No se pudo hacer la verificacion para ver si la pregunta "
                    + "fue usada", Status.ERROR);
        }
        return result;
    }

    /**
     * Este método se utiliza para poder registrar las preguntas del estudiante dentro
     * de la base de datos
     * @param responses Éstas son las preguntas que serán registradas y contienen todos
     * los datos a guardar
     * @return -1 en caso de que que no puedan ser registradas, de otro modo devolverá
     * el número de filas afectadas
     * @throws DAOException Pueden lanzar una DAOException en caso de que ocurra una
     * excepción del tipo SQL
     */
    
    @Override
    public int registerStudentResponses(ArrayList<Response> responses) throws DAOException {
        int result = 0;

        result = insertStudentResponsesTransaction(responses);
        return result;
    }

    /**
     * Este método se utiliza para poder registrar las preguntas del profesor dentro
     * de la base de datos
     * @param responses Éstas son las preguntas que serán registradas y contienen todos
     * los datos a guardar
     * @return -1 en caso de que que no puedan ser registradas, de otro modo devolverá
     * el número de filas afectadas
     * @throws DAOException Pueden lanzar una DAOException en caso de que ocurra una
     * excepción del tipo SQL 
     */
    @Override
    public int registerProfessorResponses(ArrayList<Response> responses) throws DAOException {
        int result = 0;

        result = insertProfessorResponsesTransaction(responses);
        return result;
    }

    /**
     * Este método se utiliza para revisar si existen preguntas para el estudiante dentro
     * de la base de datos
     * @return false en caso de que no haya, true en caso de que sí existan preguntas
     * @throws DAOException Pueden lanzar una DAOException en caso de que ocurra una
     * excepción del tipo SQL  
     */
    
    @Override
    public boolean areThereStudentQuestions() throws DAOException {
        boolean result = false;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT 1 FROM pregunta WHERE tipo LIKE 'Estudiante-%' LIMIT 1";

        try (Connection connection = databaseManager.getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement); ResultSet 
                        resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                result = 1 == resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Log.getLogger(FeedbackDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible validar si hay preguntas para el estudiante", 
                    Status.ERROR);
        }
        return result;
    }

    /**
     * Este método se utiliza para revisar si existen preguntas para el profesor dentro
     * de la base de datos
     * @return false en caso de que no haya, true en caso de que sí existan preguntas
     * @throws DAOException Pueden lanzar una DAOException en caso de que ocurra una
     * excepción del tipo SQL 
     */
    
    @Override
    public boolean areThereProfessorQuestions() throws DAOException {
        boolean result = false;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT 1 FROM pregunta WHERE tipo LIKE 'Profesor' LIMIT 1";

        try (Connection connection = databaseManager.getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement); ResultSet 
                        resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                result = 1 == resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Log.getLogger(FeedbackDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible validar si hay preguntas para el estudiante",
                    Status.ERROR);
        }
        return result;
    }

    private int insertQuestionTransaction(Question question) throws DAOException {
        int result = -1;
        String statement = "INSERT INTO pregunta (pregunta, tipo) VALUES (?, ?)";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement, 
                        Statement.RETURN_GENERATED_KEYS);) {
            preparedStatement.setString(1, question.getQuestionText());
            preparedStatement.setString(2, question.getQuestionType());
            result = preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(FeedbackDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar la pregunta", Status.ERROR);
        }
        return result;
    }

    private int deleteQuestionTransaction(Question question) throws DAOException {
        int result = -1;
        String statement = "DELETE FROM Pregunta WHERE idPregunta=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement);) {
            preparedStatement.setInt(1, question.getIdQuestion());
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(FeedbackDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar la pregunta", Status.ERROR);
        }
        return result;
    }
    
    /**
     * Este método se utiliza para eliminar respuestas del profesor de la base de datos
     * @param idProfessor Éste es el id del profesor del que se deseana eliminar sus
     * respuestas
     * @param idCollaborativeProject Éste es el id del proyecto del que se desea eliminar las preguntas
     * @return -1 en caso de que no se puedan eliminar, 1 en caso de que la eliminación
     * result exitosa
     * @throws DAOException Pueden lanzar una DAOException en caso de que ocurra una
     * excepción del tipo SQL  
     */
    
    @Override
    public int deleteProfessorResponsesByIdAndIdCollaborativeProject(int idProfessor, int
            idCollaborativeProject) throws DAOException {
        int result = -1;
        String statement = "DELETE FROM RespuestaProfessor WHERE idProfesor=? AND idProyectoColaborativo=?";
        
        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement
                preparedStatement = connection.prepareStatement(statement);) {
            preparedStatement.setInt(1, idProfessor);
            preparedStatement.setInt(2, idCollaborativeProject);
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(FeedbackDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar las respuestas", Status.ERROR);
        }
        return result;
    }
    
    /**
     * Este método se utiliza para eliminar respuestas del estudiante de la base de datos
     * @param idStudent Éste es el id del estudainte del que se deseana eliminar sus
     * respuestas
     * @param idCollaborativeProject Éste es el id del proyecto del que se desea eliminar las preguntas
     * @return -1 en caso de que no se puedan eliminar, 1 en caso de que la eliminación
     * result exitosa
     * @throws DAOException Pueden lanzar una DAOException en caso de que ocurra una
     * excepción del tipo SQL 
     */
    
    @Override
    public int deleteStudentResponsesByIdAndIdCollaborativeProject(int idStudent, int
            idCollaborativeProject) throws DAOException {
        int result = -1;
        String statement = "DELETE FROM RespuestaEstudiante WHERE idEstudiante = ? AND "
                + "idProyectoColaborativo = ?";
        
        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement
                preparedStatement = connection.prepareStatement(statement);) {
            preparedStatement.setInt(1, idStudent);
            preparedStatement.setInt(2, idCollaborativeProject);
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(FeedbackDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar las respuestas", Status.ERROR);
        }
        return result;
    }
    
    private int insertStudentResponsesTransaction(ArrayList<Response> responses) 
            throws DAOException {
        int result = -1;
        String statement = configureStudentInsertResponsesStatement(responses.size());

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement);) {
            int auxCounter = 0;
            for (int i = 0; i < responses.size(); i++) {
                preparedStatement.setString(1 + auxCounter, responses.get(i).getResponseText());
                preparedStatement.setInt(2 + auxCounter, responses.get(i).getIdQuestion());
                preparedStatement.setInt(3 + auxCounter, responses.get(i).getIdParticipant());
                preparedStatement.setInt(4 + auxCounter, 
                        responses.get(i).getIdCollaborativeProject());
                auxCounter += 4;
            }
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(FeedbackDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar las respuestas", Status.ERROR);
        }
        return result;
    }

    private int insertProfessorResponsesTransaction(ArrayList<Response> responses) 
            throws DAOException {
        int result = -1;
        String statement = configureProfessorInsertResponsesStatement(responses.size());

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement);) {
            int auxCounter = 0;
            for (int i = 0; i < responses.size(); i++) {
                preparedStatement.setString(1 + auxCounter, responses.get(i).getResponseText());
                preparedStatement.setInt(2 + auxCounter, responses.get(i).getIdQuestion());
                preparedStatement.setInt(3 + auxCounter, responses.get(i).getIdParticipant());
                preparedStatement.setInt(4 + auxCounter, 
                        responses.get(i).getIdCollaborativeProject());
                auxCounter += 4;
            }
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(FeedbackDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar las respuestas", Status.ERROR);
        }
        return result;
    }

    private String configureProfessorInsertResponsesStatement(int size) {
        String statement = "INSERT INTO respuestaProfessor (respuesta, idpregunta, "
                + "idprofesor, idproyectocolaborativo) VALUES ";
        for (int i = 0; i < size; i++) {
            statement += "(?, ?, ?, ?)";
            if (i != size - 1) {
                statement += ",";
            }
        }
        return statement;
    }

    private String configureStudentInsertResponsesStatement(int size) {
        String statement = "INSERT INTO respuestaestudiante (respuesta, idpregunta, "
                + "idestudiante, idproyectocolaborativo) VALUES ";
        for (int i = 0; i < size; i++) {
            statement += "(?, ?, ?, ?)";
            if (i != size - 1) {
                statement += ",";
            }
        }
        return statement;
    }

    /**
     * Este método se utiliza para obtener las respuestas sobre un proyecto colaborativo y sobre un 
     * tipo de pregunta
     * @param question Ésta es de la pregunta de la cual se desea consultar
     * @param idCollaborativeProject Éste es el id del proyecto sobre el cual se desea saber las 
     * preguntas
     * @return Retornará un arreglo de preguntas, en caso de no encontrar el arreglo
     * retornará vacío
     * @throws DAOException Pueden lanzar una DAOException en caso de que ocurra una
     * excepción del tipo SQL  
     */
    
    @Override
    public ArrayList<Response> getResponsesByQuestionAndIdCollaborativeProject(Question question, 
            int idCollaborativeProject) throws DAOException {
        ArrayList<Response> responses = new ArrayList<>();
        String statement;
        String idResponse = "IdRespuesta";
        String idParticipant = "idEstudiante";
        if (question.getQuestionType().equals("Profesor")) {
            statement = "SELECT * FROM respuestaprofessor WHERE idpregunta=? and "
                    + "idproyectocolaborativo=?";
            idResponse = "IdRespuestaProfessor";
            idParticipant = "idProfesor";
        } else {
            statement = "SELECT * FROM respuestaestudiante WHERE idpregunta=? and "
                    + "idproyectocolaborativo=?";
        }

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, question.getIdQuestion());
            preparedStatement.setInt(2, idCollaborativeProject);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    
                    Response response = new Response();
                    response.setIdResponse(resultSet.getInt(idResponse));
                    response.setResponseText(resultSet.getString("respuesta"));
                    response.setQuestion(getQuestionById(resultSet.getInt("idpregunta")));
                    response.setIdParticipant(resultSet.getInt(idParticipant));
                    response.setIdCollaborativeProject(resultSet.getInt("idProyectoColaborativo"));
                    responses.add(response);
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(FeedbackDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener las respuestas", Status.ERROR);
        }
        return responses;
    }

    private Question getQuestionByQuestionText(String questionText) throws DAOException {
        Question question = new Question();
        String statement = "SELECT * FROM pregunta WHERE pregunta=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement);) {
            preparedStatement.setString(1, questionText);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    question.setIdQuestion(resultSet.getInt("idPregunta"));
                    question.setQuestionText(resultSet.getString("pregunta"));
                    question.setQuestionType(resultSet.getString("tipo"));
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(FeedbackDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener la pregunta", Status.ERROR);
        }
        return question;
    }
    
    private Question getQuestionById(int idQuestion) throws DAOException {
        Question question = new Question();
        String statement = "SELECT * FROM pregunta WHERE idPregunta=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement 
                preparedStatement = connection.prepareStatement(statement);) {
            preparedStatement.setInt(1, idQuestion);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    question.setIdQuestion(resultSet.getInt("idPregunta"));
                    question.setQuestionText(resultSet.getString("pregunta"));
                    question.setQuestionType(resultSet.getString("tipo"));
                }
            }
        } catch (SQLException exception) {
            Log.getLogger(FeedbackDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible obtener la pregunta", Status.ERROR);
        }
        return question;
    }
    
}
