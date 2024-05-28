package mx.fei.coilvicapp.logic.feedback_;

import mx.fei.coilvicapp.logic.feedback_.Question;
import java.util.Objects;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;
/**
 *
 * @author ivanr
 */
public class Question {

    private int idQuestion = 0;
    private String questionText;
    private String questionType;
    private final FieldValidator fieldValidator;
    
    public Question() {
        fieldValidator = new FieldValidator();
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
        fieldValidator.checkLongRange(questionText);
        this.questionText = questionText;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        if (questionType == null) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de pregunta");
        }
        this.questionType = questionType;
    }
    
    @Override
    public boolean equals(Object object) {
        boolean isEqual = false;
        
        if(this == object) {
            isEqual = true;
        } else if (object != null && getClass() == object.getClass()) {
            Question toCompare = (Question) object;
            isEqual = idQuestion == toCompare.getIdQuestion()
                    && Objects.equals(questionText, toCompare.getQuestionText())
                    && Objects.equals(questionType, toCompare.getQuestionType());
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idQuestion, questionText, questionType);
    }
    
    @Override
    public String toString() {
        return questionText;
    }
}
