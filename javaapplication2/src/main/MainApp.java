package main;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.paint.Color;

/**
 *
 * @author ivanr
 */
public class MainApp extends Application {
    private static Scene scene;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 700;
    

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("/mx/fei/coilvicapp/gui/views/UniversityManager"));
        stage.setScene(scene);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        scene.setFill(Color.BLUE);
        stage.show();
    }
    
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    private static void configureStage(Stage stage) {
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.centerOnScreen();
    }
    
    public static void changeView(FXMLLoader loader) throws IOException {
        Stage currentStage = (Stage) scene.getWindow();
        scene.setRoot(loader.load());
        configureStage(currentStage);
    }
    
    public static void changeView(String URL) throws IOException {
        Stage currentStage = (Stage) scene.getWindow();
        configureStage(currentStage);
        MainApp.setRoot(URL);
    }
    
    private static Parent loadFXML(String FXML) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(FXML + ".fxml"));
        return fxmlLoader.load();
    }
    
    public static void main(String[] args) {
        launch();
    }
    
    
}
