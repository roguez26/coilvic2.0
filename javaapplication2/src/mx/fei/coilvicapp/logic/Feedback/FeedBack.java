package mx.fei.coilvicapp.logic.feedback;

import java.util.ArrayList;
import java.util.Objects;
/**
 *
 * @author ivanr
 */
public class Feedback {
    private int idFeedback = 0;
    private ArrayList<Question> questions;
    private ArrayList<Response> responses;

    public int getIdFeedback() {
        return idFeedback;
    }

    public void setIdFeedback(int idFeedback) {
        this.idFeedback = idFeedback;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public ArrayList<Response> getResponses() {
        return responses;
    }
 
    public void setResponses(ArrayList<Response> responses) {
        this.responses = responses;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Feedback feedBack = (Feedback) obj;
        return idFeedback == feedBack.idFeedback &&
                Objects.equals(questions, feedBack.questions) &&
                Objects.equals(responses, feedBack.responses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFeedback, questions, responses);
    }

    @Override
    public String toString() {
        return "FeedBack{" +
                "idFeedback=" + idFeedback +
                ", questions=" + questions +
                ", responses=" + responses +
                '}';
    }
}
