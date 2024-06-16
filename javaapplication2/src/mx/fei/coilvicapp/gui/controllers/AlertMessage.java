package mx.fei.coilvicapp.gui.controllers;
import mx.fei.coilvicapp.logic.implementations.Status;

/**
 *
 * @author ivanr
 */
public class AlertMessage {
 
    private String content = "La operación se ha realizado exitosamente";
    private Status alertType = Status.SUCCESS;

    public AlertMessage(String content, Status alertType) {
        if (content == null || alertType == null) {
            throw new IllegalArgumentException("Debes ingresar un mensaje y un estado en el mensaje de alerta");
        }
        this.content = content;
        this.alertType = alertType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Status getAlertType() {
        return alertType;
    }

    public void setAlertType(Status alertType) {
        this.alertType = alertType;
    }
}
