package mx.fei.coilvicapp.logic.feedback;

import java.util.Objects;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;

public class Response {

    private int idResponse = 0;
    private String responseText;
    private Question question;
    private int idParticipant;
    private int idCollaborativeProject;

    public Response() {
        question = new Question();
    }

    public int getIdResponse() {
        return idResponse;
    }

    public String getResponseText() {
        return responseText;
    }

    public int getIdQuestion() {
        return question.getIdQuestion();
    }

    public int getIdParticipant() {
        return idParticipant;
    }

    public int getIdCollaborativeProject() {
        return idCollaborativeProject;
    }

    public void setIdResponse(int idResponse) {
        this.idResponse = idResponse;
    }

    public void setResponseText(String responseText) {
        FieldValidator fieldValidator = new FieldValidator();
        fieldValidator.checkLongRange(responseText);
        this.responseText = responseText;
    }

    public void setIdQuestion(int idQuestion) {
        this.question.setIdQuestion(idQuestion);
    }

    public void setIdParticipant(int idStudent) {
        this.idParticipant = idStudent;
    }

    public void setIdCollaborativeProject(int idCollaborativeProject) {
        this.idCollaborativeProject = idCollaborativeProject;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    @Override
    public boolean equals(Object object) {
        boolean isEqual = false;

        if (this == object) {
            isEqual = true;
        } else if (object != null && getClass() == object.getClass()) {
            Response toCompare = (Response) object;
            isEqual = Objects.equals(question, toCompare.getQuestion())
                    && Objects.equals(responseText, toCompare.getResponseText())
                    && Objects.equals(idParticipant, toCompare.getIdParticipant())
                    && Objects.equals(idCollaborativeProject, toCompare.getIdCollaborativeProject());
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.idResponse;
        hash = 59 * hash + Objects.hashCode(this.responseText);
        hash = 59 * hash + Objects.hashCode(this.question);
        hash = 59 * hash + this.idParticipant;
        hash = 59 * hash + this.idCollaborativeProject;
        return hash;
    }

    @Override
    public String toString() {
        return "Response{"
                + "idResponse=" + idResponse
                + ", responseText='" + responseText + '\''
                + ", question=" + question
                + ", idStudent=" + idParticipant
                + ", idCollaborativeProject=" + idCollaborativeProject
                + '}';
    }

}
