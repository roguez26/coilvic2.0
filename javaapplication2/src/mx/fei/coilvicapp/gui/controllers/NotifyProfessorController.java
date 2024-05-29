package mx.fei.coilvicapp.gui.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import mx.fei.coilvicapp.logic.professor.Professor;
import mx.fei.coilvicapp.logic.emailSender.EmailSenderDAO;
import mx.fei.coilvicapp.logic.emailSender.EmailSender;
import mx.fei.coilvicapp.logic.emailSender.IEmailSender;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import main.MainApp;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.mail.MessagingException;
import log.Log;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;

/**
 *
 * @author ivanr
 */
public class NotifyProfessorController implements Initializable {

    @FXML
    private Button sendButton;

    @FXML
    private VBox backgroundVBox;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextArea messageTextArea;

    @FXML
    private TextField subjectTextField;

    private final IEmailSender emailSenderDAO = new EmailSenderDAO();
    private Professor professor = new Professor();
    private final EmailSender emailSender = new EmailSender();
    private String lastView;

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {

    }

    @FXML
    void sendButtonIsPressed(ActionEvent event) {
        try {
            invokeSendEmail();
            closeWindow();
        } catch (IllegalArgumentException iaException) {
            handleValidationException(iaException);
        } catch (DAOException daoException) {
            handleDAOException(daoException);
        } catch (MessagingException mException) {
            handleMessagingException(mException);
        }
    }

    private void invokeSendEmail() throws DAOException, MessagingException {
        initializeEmailSender();
        if (confirmNotification()) {
            emailSender.createEmail();
            if (emailSender.sendEmail()) {
                DialogController.getInformativeConfirmationDialog("Enviado", "El correo fue enviado con éxito");
                emailSenderDAO.registerEmail(emailSender);
            }
        }
    }

    private void initializeEmailSender() {
        emailSender.setSubject(subjectTextField.getText());
        emailSender.setMessage(messageTextArea.getText());
        emailSender.setReceiver(professor);
    }

    @FXML
    void cancelButtonIsPressed(ActionEvent event) throws IOException {
        if (confirmCancelation()) {
            closeWindow();
        }
    }

    private boolean confirmNotification() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar notificación", "¿Deseas notificar al profesor?");
        return (response.isPresent() && response.get() == DialogController.BUTTON_YES);
    }

    private boolean confirmCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cancelacion", "Si cancelas el profesor no será notificado ¿Deseas cancelar la notificacion?");
        return (response.isPresent() && response.get() == DialogController.BUTTON_YES);
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
        showEmail(professor.getEmail());
    }

    public void showEmail(String email) {
        emailTextField.setText(email);
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    goBack();
                case FATAL ->
                    MainApp.changeView("/main/MainApp");
            }
        } catch (IOException ioException) {
            Log.getLogger(NotifyProfessorController.class).error(exception.getMessage(), exception);
        }
    }

    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getInvalidDataDialog(exception.getMessage());
    }

    private void handleMessagingException(MessagingException exception) {
        DialogController.getNotSentMessageDialog(exception.getMessage());
    }

    public void setLastView(String lastView) {
        this.lastView = lastView;
    }

    private void goBack() {
        try {
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/" + lastView);
        } catch (IOException exception) {
            Log.getLogger(NotifyProfessorController.class).error(exception.getMessage(), exception);
        }
    }
    
    private void closeWindow() {
        Stage stage = (Stage) sendButton.getScene().getWindow();
        stage.close();
    }

}
