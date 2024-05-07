package mx.fei.coilvicapp.logic.feedback;
import java.util.Objects;
import mx.fei.coilvicapp.logic.implementations.FieldValidator;
/**
 *
 * @author ivanr
 */
public class Response {
    private int idResponse = 0;
    private String responseText;
    private Question question;
    private int idStudent;
    private int idCollaborativeProject;
    private FieldValidator fieldValidator;

    public Response() {
        fieldValidator = new FieldValidator();
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

    public int getIdStudent() {
        return idStudent;
    }

    public int getIdCollaborativeProject() {
        return idCollaborativeProject;
    }

    
    public void setIdResponse(int idResponse) {
        this.idResponse = idResponse;
    }

    public void setResponseText(String responseText) {
        fieldValidator.checkLongRange(responseText);
        this.responseText = responseText;
    }

    public void setIdQuestion(int idQuestion) {
        this.question.setIdQuestion(idQuestion);
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
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
    public boolean equals(Object toCompare) {
        //if (this == o) return true;
        //if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) toCompare;
        return idResponse == response.idResponse &&
               question.equals(toCompare) &&
               idStudent == response.idStudent &&
               idCollaborativeProject == response.idCollaborativeProject &&
               Objects.equals(responseText, response.responseText);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.idResponse;
        hash = 59 * hash + Objects.hashCode(this.responseText);
        hash = 59 * hash + Objects.hashCode(this.question);
        hash = 59 * hash + this.idStudent;
        hash = 59 * hash + this.idCollaborativeProject;
        return hash;
    }

    
    @Override
    public String toString() {
        return "Response{" +
                "idResponse=" + idResponse +
                ", responseText='" + responseText + '\'' +
                ", idQuestion=" + question +
                ", idStudent=" + idStudent +
                ", idCollaborativeProject=" + idCollaborativeProject +
                '}';
    }

    
}
