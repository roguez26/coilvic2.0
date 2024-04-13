package mx.fei.coilvicapp.logic.Feedback;

import mx.fei.coilvicapp.dataaccess.DatabaseManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.implementations.Status;

/**
 *
 * @author ivanr
 */
public class FeedBackDAO implements IFeedBack {

    public FeedBackDAO() {

    }

    private boolean checkQuestionDuplication(Question question) throws DAOException {
        Question instance;
        int idQuestion = 0;

        try {
            instance = getQuestionByQuestionText(question.getQuestionText());
            idQuestion = instance.getIdQuestion();
        } catch (DAOException exception) {
            throw new DAOException("No fue posible realizar la validacion, intente registrar mas tarde", Status.ERROR);
        }
        if (idQuestion != question.getIdQuestion()) {
            throw new DAOException("La pregunta ya se encuentra registrada", Status.WARNING);
        }
        return true;
    }

    public int registerQuestion(Question question) throws DAOException {
        int result = 0;

        if (!checkQuestionDuplication(question)) {
            result = insertQuestionTransaction(question);
        }
        return result;
    }

    public int insertQuestionTransaction(Question quesiton) throws DAOException {
        int result = -1;
        Connection connection;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "INSERT INTO pregunta (pregunta, tipo) VALUES (?, ?)";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, quesiton.getQuestionText());
            preparedStatement.setString(2, quesiton.getQuestionType());
            result = preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Logger.getLogger(FeedBackDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible registrar la pregunta", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(FeedBackDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return result;
    }

    public int updateQuestionTransaction(Question question) throws DAOException {
        int result = -1;
        Connection connection;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "UPDATE Pregunta SET pregunta=?, tipo = ? WHERE idPregunta=?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, question.getQuestionText());
            preparedStatement.setString(2, question.getQuestionType());
            preparedStatement.setInt(3, question.getIdQuestion());
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(FeedBackDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible actualizar la pregunta", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(FeedBackDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return result;
    }

    public int insertResponseTransaction(ArrayList<Response> responses) throws DAOException {
        int result = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "INSERT INTO respuesta (respuesta, idRetroalimentacion, idPregunta) VALUES (?, ?, ?)";

        try {
            connection = databaseManager.getConnection();
            connection.setAutoCommit(false);
            for (Response response : responses) {
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setString(1, response.getResponseText());
                preparedStatement.setInt(2, response.getIdFeedback());
                preparedStatement.setInt(3, response.getIdQuestion());
                preparedStatement.addBatch();
            }
            int[] batchResults = preparedStatement.executeBatch();
            for (int batchResult : batchResults) {
                if (batchResult <= 0) {
                    throw new SQLException("Failed to insert response");
                }
            }
            connection.commit();
            result = batchResults.length;
        } catch (SQLException exception) {
            Logger.getLogger(FeedBackDAO.class.getName()).log(Level.SEVERE, null, exception);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    Logger.getLogger(FeedBackDAO.class.getName()).log(Level.SEVERE, null, rollbackException);
                }
            }
            throw new DAOException("No fue posible registrar las respuestas", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException closeException) {
                Logger.getLogger(FeedBackDAO.class.getName()).log(Level.SEVERE, null, closeException);
            }
            databaseManager.closeConnection();
        }
        return result;
    }

    public int insertResponsesTransaction(Question quesiton) throws DAOException {
        int result = -1;
        Connection connection;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "INSERT INTO pregunta (pregunta, tipo) VALUES (?, ?)";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, quesiton.getQuestionText());
            preparedStatement.setString(2, quesiton.getQuestionType());
            result = preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Logger.getLogger(FeedBackDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible registrar la pregunta", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(FeedBackDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return result;
    }

    public Question getQuestionById(int idQuestion) throws DAOException {
        Question question = new Question();
        Connection connection;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        ResultSet resultSet = null;
        String statement = "SELECT * FROM pregunta WHERE idpregunta=?";
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, idQuestion);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                question.setIdQuestion(resultSet.getInt("idPregunta"));
                question.setQuestionText(resultSet.getString("pregunta"));
                question.setQuestionType(resultSet.getString("tipo"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(FeedBackDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(FeedBackDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return question;
    }

    public Response getResponseById(int idQuestion) throws DAOException {
        Response response = new Response();
        Connection connection;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        ResultSet resultSet = null;
        String statement = "SELECT * FROM respuesta WHERE idrespuesta=?";
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, idQuestion);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                response.setIdResponse(resultSet.getInt("idRespuesta"));
                response.setResponseText(resultSet.getString("respuesta"));
                response.setIdFeedback(resultSet.getInt("idretroalimentacion"));
                response.setIdQuestion(resultSet.getInt("idpregunta"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(FeedBackDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(FeedBackDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return response;
    }

    public Question getQuestionByQuestionText(String questionText) throws DAOException {
        Question question = new Question();
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String statement = "SELECT * FROM pregunta WHERE pregunta=?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, questionText);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                question.setIdQuestion(resultSet.getInt("idPregunta"));
                question.setQuestionText(resultSet.getString("nombre"));
                question.setQuestionType(resultSet.getString("tipo"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(FeedBackDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible obtener la pregunta", Status.ERROR);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(FeedBackDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return question;
    }
    
//    public ArrayList<FeedBack> getAllFeedBackByColaborativeProjectId(int colaborativeProjectId) throws DAOException {
//        ArrayList<FeedBack> feedbacks = new ArrayList<>();
//        Connection connection;
//        PreparedStatement preparedStatement = null;
//        DatabaseManager databaseManager = new DatabaseManager();
//        ResultSet resultSet = null;
//        String statement = "SELECT * FROM Pais";
//
//        try {
//            connection = databaseManager.getConnection();
//            preparedStatement = connection.prepareStatement(statement);
//            resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                Country instance = new Country();
//                instance.setIdCountry(resultSet.getInt("IdPais"));
//                instance.setName(resultSet.getString("Nombre"));
//                countries.add(instance);
//            }
//
//        } catch (SQLException exception) {
//            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
//        } finally {
//            try {
//                if (preparedStatement != null) {
//                    preparedStatement.close();
//                }
//                if (resultSet != null) {
//                    resultSet.close();
//                }
//            } catch (SQLException exception) {
//                Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, exception);
//            }
//            databaseManager.closeConnection();
//        }
//        return countries;
//    }

}
