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
        scene = new Scene(loadFXML("/mx/fei/coilvicapp/gui/views/UniversityManager"));
        stage.setScene(scene);
        stage.show();
    }
    
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    public static void changeView(String url, int width, int height) throws IOException {
        Stage currentStage = (Stage) scene.getWindow();
        configureStage(currentStage, width, height);
        setRoot(url);
        
    }
    
    private static void configureStage(Stage stage, int width, int height) {
        stage.setWidth(width);
        stage.setHeight(height);
        stage.centerOnScreen();
    }
    
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    public static void main(String[] args) {
        launch();
    }
}
