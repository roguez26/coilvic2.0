/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mx.fei.coilvicapp.logic.feedback;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;

/**
 *
 * @author ivanr
 */
public interface IFeedback {
    
    public int registerQuestion(Question question) throws DAOException;
    
    public int deleteQuestion(Question question) throws DAOException;
    
    public int registerStudentResponses(ArrayList<Response> responses) throws DAOException;
    
    public int updateQuestionTransaction(Question question) throws DAOException;
    
    public ArrayList<Question> getQuestionByType(String type) throws DAOException;
    
}
