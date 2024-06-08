package mx.fei.coilvicapp.logic.feedback;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.student.Student;

/**
 *
 * @author ivanr
 */
public interface IFeedback {

    public int registerQuestion(Question question) throws DAOException;

    public int deleteQuestion(Question question) throws DAOException;

    public int registerStudentResponses(ArrayList<Response> responses) throws DAOException;

    public int registerProfessorResponses(ArrayList<Response> responses) throws DAOException;

    public int updateQuestionTransaction(Question question) throws DAOException;

    public ArrayList<Question> getQuestionByType(String type) throws DAOException;

    public boolean areThereStudentQuestions() throws DAOException;

    public boolean areThereProfessorQuestions() throws DAOException;

    public boolean hasCompletedPreForm(Student student, CollaborativeProject collaborativeProject) throws DAOException;

    public boolean hasCompletedPostForm(Student student, CollaborativeProject collaborativeProject) throws DAOException;

    public boolean hasCompletedProfessorForm(Professor professor, CollaborativeProject collaborativeProject) throws DAOException;

    public ArrayList<Response> getResponsesByQuestionAndIdCollaborativeProject(Question question, int idCollaborativeProject) throws DAOException;
    
    public int deleteProfessorResponsesByIdAndIdCollaborativeProject(int idProfessor, int idCollaborativeProject) throws DAOException;
    
    public int deleteStudentResponsesByIdAndIdCollaborativeProject(int idProfessor, int idCollaborativeProject) throws DAOException;
}
