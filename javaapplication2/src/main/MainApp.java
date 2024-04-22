package main;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 *
 * @author ivanr
 */
public class MainApp extends Application {
    private static Scene scene;
    //private static final Logger LOGGER = LogManager.getLogger(App.class);

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("/mx/fei/coilvicapp/gui/views/UniversityRegistrationFXML"));
        stage.setScene(scene);
        stage.show();
    }
    
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    public static void main(String[] args) {
        launch();
    }
}
