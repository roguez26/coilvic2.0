package mx.fei.coilvicapp.logic.implementations;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author ivanr
 */
public class FileChooserV extends Application {
    
    //private

    private final Desktop desktop = Desktop.getDesktop();
    

    @Override
    public void start(final Stage stage) {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            openFile(file);
        }
    }
   
    public static void main(String[] args) {
        Application.launch(args);
    }

    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException exception) {
            Logger.getLogger( FileChooserV.class.getName()).log( Level.SEVERE, null, exception);
        }
    }
}
