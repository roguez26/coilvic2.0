package mx.fei.coilvicapp.logic.feedback;

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
public class FeedbackDAO implements IFeedback {

    public FeedbackDAO() {

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
        if (idQuestion != question.getIdQuestion() && idQuestion > 0 && instance.getQuestionType().equals(question.getQuestionType())) {
            throw new DAOException("La pregunta ya se encuentra registrada", Status.WARNING);
        }
        return false;
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
    
    //Este va a ser para agregar mas preguntas
    public int insertQuestionTransaction(Question question) throws DAOException {
        int result = -1;
        Connection connection;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "INSERT INTO pregunta (pregunta, tipo) VALUES (?, ?)";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, question.getQuestionText());
            preparedStatement.setString(2, question.getQuestionType());
            result = preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, exception);
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
                Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return result;
    }

    @Override
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
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible actualizar la pregunta", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return result;
    }
    
    public int deleteQuestionTransaction(Question question) throws DAOException {
        int result = -1;
        Connection connection;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = "DELETE FROM Pregunta WHERE idPregunta=?";

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, question.getIdQuestion());
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException("No fue posible eliminar la pregunta", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return result;
    }

    public int insertStudentResponsesTransaction(ArrayList<Response> responses) throws DAOException {
        int result = -1;
        Connection connection;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        String statement = configureInsertResponsesStatement(responses.size());

        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            int aux = 0;
            for (int i = 0; i < responses.size(); i++) {
                preparedStatement.setString(1 + aux, responses.get(i).getResponseText());
                preparedStatement.setInt(2 + aux, responses.get(i).getIdQuestion());
                preparedStatement.setInt(3 + aux, responses.get(i).getIdStudent());
                preparedStatement.setInt(4 + aux, responses.get(i).getIdCollaborativeProject());
                aux = aux + 4;
            }
            result = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, exception);
            throw new DAOException ("No fue posible registrar las respuestas", Status.ERROR);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
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
    //Esta se mostrara para que llenen el formulario
    @Override
    public ArrayList<Question> getQuestionByType(String type) throws DAOException {
        ArrayList<Question> questionsList = new ArrayList<>();
        Connection connection;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        ResultSet resultSet = null;
        String statement = "SELECT * FROM pregunta WHERE tipo=?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, type);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Question question = new Question();
                question.setIdQuestion(resultSet.getInt("idPregunta"));
                question.setQuestionText(resultSet.getString("pregunta"));
                question.setQuestionType(resultSet.getString("tipo"));
                questionsList.add(question);
            }
        } catch (SQLException exception) {
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return questionsList;
    }

    public ArrayList<Response> getResponsesByIdQuestionAndIdCollaborativeProject(int idQuestion, int idCollaborativeProject) throws DAOException {
        ArrayList<Response> responses = new ArrayList<>();
        Connection connection;
        PreparedStatement preparedStatement = null;
        DatabaseManager databaseManager = new DatabaseManager();
        ResultSet resultSet = null;
        String statement = "SELECT * FROM respuesta WHERE idpregunta=? and idproyectocolaborativo=?";
        
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, idQuestion);
            preparedStatement.setInt(2, idCollaborativeProject);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Response instance = new Response();
                instance.setIdResponse(resultSet.getInt("idRespuesta"));
                instance.setResponseText(resultSet.getString("respuesta"));
                instance.setIdQuestion(resultSet.getInt("idpregunta"));
                instance.setIdStudent(resultSet.getInt("idestudiante"));
                instance.setIdCollaborativeProject(resultSet.getInt("idProyectoColaborativo"));
                responses.add(instance);
            }
        } catch (SQLException exception) {
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return responses;
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
                question.setQuestionText(resultSet.getString("pregunta"));
                question.setQuestionType(resultSet.getString("tipo"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, exception);
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
                Logger.getLogger(FeedbackDAO.class.getName()).log(Level.SEVERE, null, exception);
            }
            databaseManager.closeConnection();
        }
        return question;
    }

}
