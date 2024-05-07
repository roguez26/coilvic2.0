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
import mx.fei.coilvicapp.logic.implementations.Status;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import main.MainApp;
import javafx.scene.layout.VBox;
import javax.mail.MessagingException;
import static mx.fei.coilvicapp.logic.implementations.Status.ERROR;
import static mx.fei.coilvicapp.logic.implementations.Status.FATAL;

/**
 *
 * @author ivanr
 */
public class NotifyProfessorController implements Initializable {

    @FXML
    private Button acceptButton;

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
    private final Professor professor = new Professor();
    private final EmailSender emailSender = new EmailSender();

    @Override
    public void initialize(URL URL, ResourceBundle resourceBundle) {

        professor.setEmail("ivanxspoti@gmail.com");
        professor.setIdProfessor(8);
        showEmail(professor.getEmail());
    }

    @FXML
    void acceptButtonIsPressed(ActionEvent event) {
        try {
            invokeSendEmail();
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
                wasSentConfirmation();
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
            MainApp.changeView("/mx/fei/coilvicapp/gui/views/main");
        }
    }

    private boolean wasSentConfirmation() {
        Optional<ButtonType> response = DialogController.getInformativeConfirmationDialog("", "El correo fue enviado con exito");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean confirmNotification() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar notificación", "¿Deseas notificar al profesor?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    private boolean confirmCancelation() {
        Optional<ButtonType> response = DialogController.getConfirmationDialog("Confirmar cancelacion", "¿Deseas cancelar la notificacion?");
        return (response.get() == DialogController.BUTTON_YES);
    }

    public void setProfessor(Professor professor) {
        //this.professor = professor;
        // showEmail(professor.getEmail());
    }

    public void showEmail(String email) {
        emailTextField.setText(email);
    }

    private void handleDAOException(DAOException exception) {
        try {
            DialogController.getDialog(new AlertMessage(exception.getMessage(), exception.getStatus()));
            switch (exception.getStatus()) {
                case ERROR ->
                    MainApp.changeView("/mx/fei/coilvicapp/gui/views/UniversityManager");
                case FATAL ->
                    MainApp.changeView("/main/MainApp");
            }
        } catch (IOException ioException) {

        }
    }

    private void handleValidationException(IllegalArgumentException exception) {
        DialogController.getInvalidDataDialog(exception.getMessage());
    }

    private void handleMessagingException(MessagingException exception) {
        DialogController.getNotSentMessageDialog(exception.getMessage());
    }

}
