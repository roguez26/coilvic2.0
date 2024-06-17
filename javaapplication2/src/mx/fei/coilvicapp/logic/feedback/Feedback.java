package mx.fei.coilvicapp.logic.feedback;

import java.util.ArrayList;
import java.util.Objects;

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
    public boolean equals(Object object) {
        boolean isEqual = false;
        if (this == object) {
            isEqual = true;
        } else if (object != null && getClass() == object.getClass()) {
            Feedback feedback = (Feedback) object;
            isEqual = idFeedback == feedback.idFeedback
                    && Objects.equals(questions, feedback.questions)
                    && Objects.equals(responses, feedback.responses);
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFeedback, questions, responses);
    }
}
