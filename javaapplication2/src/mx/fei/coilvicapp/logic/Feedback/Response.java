package mx.fei.coilvicapp.logic.feedback;
import java.util.Objects;
/**
 *
 * @author ivanr
 */
public class Response {
    private int idResponse = 0;
    private String responseText;
    private int idQuestion;
    private int idStudent;
    private int idCollaborativeProject;

    public Response() {

    }

    public int getIdResponse() {
        return idResponse;
    }

    public String getResponseText() {
        return responseText;
    }

    public int getIdQuestion() {
        return idQuestion;
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
        this.responseText = responseText;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public void setIdCollaborativeProject(int idCollaborativeProject) {
        this.idCollaborativeProject = idCollaborativeProject;
    }

    
    @Override
    public boolean equals(Object toCompare) {
        //if (this == o) return true;
        //if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) toCompare;
        return idResponse == response.idResponse &&
               idQuestion == response.idQuestion &&
               idStudent == response.idStudent &&
               idCollaborativeProject == response.idCollaborativeProject &&
               Objects.equals(responseText, response.responseText);
    }

    
    @Override
    public String toString() {
        return "Response{" +
                "idResponse=" + idResponse +
                ", responseText='" + responseText + '\'' +
                ", idQuestion=" + idQuestion +
                ", idStudent=" + idStudent +
                ", idCollaborativeProject=" + idCollaborativeProject +
                '}';
    }

    
}
