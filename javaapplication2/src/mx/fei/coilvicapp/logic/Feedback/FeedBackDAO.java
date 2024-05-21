package mx.fei.coilvicapp.logic.feedback;

import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import log.Log;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;

/**
 *
 * @author ivanr
 */
public class FeedbackDAO implements IFeedback {

    private boolean checkQuestionDuplication(Question question) throws DAOException {
        Question instance;
        int idQuestion = 0;

        try {
            instance = getQuestionByQuestionText(question.getQuestionText());
            idQuestion = instance.getIdQuestion();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde", Status.ERROR);
        }
        if (idQuestion != question.getIdQuestion() && idQuestion > 0 && instance.getQuestionType().equals(question.getQuestionType())) {
            throw new DAOException("La pregunta ya se encuentra registrada", Status.WARNING);
        }
        return false;
    }

    @Override
    public int updateQuestionTransaction(Question question) throws DAOException {
        int result = -1;
        String statement = "UPDATE Pregunta SET pregunta=?, tipo = ? WHERE idPregunta=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement);) {
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

    @Override
    public ArrayList<Question> getQuestionByType(String type) throws DAOException {
        ArrayList<Question> questionsList = new ArrayList<>();
        String statement;

        if (type.equals("Estudiante")) {
            statement = "SELECT * FROM pregunta WHERE tipo LIKE 'Estudiante-%'";
        } else {
            statement = "SELECT * FROM pregunta WHERE tipo=?";
        }

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
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
            throw new DAOException("No fue posible obtener las preguntas por tipo", Status.ERROR);
        }
        return questionsList;
    }

    @Override
    public int registerQuestion(Question question) throws DAOException {
        int result = 0;

        if (!checkQuestionDuplication(question)) {
            result = insertQuestionTransaction(question);
        }
        return result;
    }

    @Override
    public int deleteQuestion(Question question) throws DAOException {
        int result;

        result = deleteQuestionTransaction(question);
        return result;
    }

    @Override
    public int registerStudentResponses(ArrayList<Response> responses) throws DAOException {
        int result;

        result = insertStudentResponsesTransaction(responses);
        return result;
    }

    @Override
    public boolean areThereStudentQuestions() throws DAOException {
        boolean result = false;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "SELECT 1 FROM pregunta WHERE tipo LIKE 'Estudiante-%' LIMIT 1";

        try (Connection connection = databaseManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement); ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                result = 1 == resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Log.getLogger(FeedbackDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible validar si hay preguntas para el estudiante", Status.ERROR);
        }
        return result;
    }

    public int insertQuestionTransaction(Question question) throws DAOException {
        int result = -1;
        String statement = "INSERT INTO pregunta (pregunta, tipo) VALUES (?, ?)";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);) {
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

    public int deleteQuestionTransaction(Question question) throws DAOException {
        int result = -1;
        String statement = "DELETE FROM Pregunta WHERE idPregunta=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement);) {
            preparedStatement.setInt(1, question.getIdQuestion());
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(FeedbackDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible eliminar la pregunta", Status.ERROR);
        }
        return result;
    }

    public int insertStudentResponsesTransaction(ArrayList<Response> responses) throws DAOException {
        int result = -1;
        String statement = configureInsertResponsesStatement(responses.size());

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement);) {
            int aux = 0;
            for (int i = 0; i < responses.size(); i++) {
                preparedStatement.setString(1 + aux, responses.get(i).getResponseText());
                preparedStatement.setInt(2 + aux, responses.get(i).getIdQuestion());
                preparedStatement.setInt(3 + aux, responses.get(i).getIdStudent());
                preparedStatement.setInt(4 + aux, responses.get(i).getIdCollaborativeProject());
                aux += 4;
            }
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Log.getLogger(FeedbackDAO.class).error(exception.getMessage(), exception);
            throw new DAOException("No fue posible registrar las respuestas", Status.ERROR);
        }
        return result;
    }

    private String configureInsertResponsesStatement(int size) {
        String statement = "INSERT INTO respuestaestudiante (respuesta, idpregunta, idestudiante, idproyectocolaborativo) VALUES ";
        for (int i = 0; i < size; i++) {
            statement += "(?, ?, ?, ?)";
            if (i != size - 1) {
                statement += ",";
            }
        }
        return statement;
    }

    public ArrayList<Response> getResponsesByIdQuestionAndIdCollaborativeProject(int idQuestion, int idCollaborativeProject) throws DAOException {
        ArrayList<Response> responses = new ArrayList<>();
        String statement = "SELECT * FROM respuesta WHERE idpregunta=? and idproyectocolaborativo=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, idQuestion);
            preparedStatement.setInt(2, idCollaborativeProject);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Response response = new Response();
                    response.setIdResponse(resultSet.getInt("idRespuesta"));
                    response.setResponseText(resultSet.getString("respuesta"));
                    response.setIdQuestion(resultSet.getInt("idpregunta"));
                    response.setIdStudent(resultSet.getInt("idestudiante"));
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

    public Question getQuestionByQuestionText(String questionText) throws DAOException {
        Question question = new Question();
        String statement = "SELECT * FROM pregunta WHERE pregunta=?";

        try (Connection connection = new DatabaseManager().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(statement);) {
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
}
