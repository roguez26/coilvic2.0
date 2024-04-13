package mx.fei.coilvicapp.logic.Feedback;
import java.util.Objects;
/**
 *
 * @author ivanr
 */
public class Response {
    private int idResponse = 0;
    private String responseText;
    private int idFeedback;
    private int idQuestion;

    public Response() {

    }

    public int getIdResponse() {
        return idResponse;
    }

    public void setIdResponse(int idResponse) {
        this.idResponse = idResponse;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public int getIdFeedback() {
        return idFeedback;
    }

    public void setIdFeedback(int idFeedback) {
        this.idFeedback = idFeedback;
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Response response = (Response) obj;
        return idResponse == response.idResponse &&
                idFeedback == response.idFeedback &&
                idQuestion == response.idQuestion &&
                Objects.equals(responseText, response.responseText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idResponse, responseText, idFeedback, idQuestion);
    }

    @Override
    public String toString() {
        return "Response{" +
                "idResponse=" + idResponse +
                ", responseText='" + responseText + '\'' +
                ", idFeedback=" + idFeedback +
                ", idQuestion=" + idQuestion +
                '}';
    }
}
