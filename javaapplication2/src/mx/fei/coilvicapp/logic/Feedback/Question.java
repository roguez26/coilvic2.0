package mx.fei.coilvicapp.logic.Feedback;
import java.util.Objects;
/**
 *
 * @author ivanr
 */
public class Question {

    private int idQuestion = 0;
    private String questionText;
    private String questionType;

    public Question() {
        
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Question toCompare = (Question) object;
        return idQuestion == toCompare.idQuestion &&
                Objects.equals(questionText, toCompare.questionText) &&
                Objects.equals(questionType, toCompare.questionType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idQuestion, questionText, questionType);
    }
}
