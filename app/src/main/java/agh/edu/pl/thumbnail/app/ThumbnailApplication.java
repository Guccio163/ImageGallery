package agh.edu.pl.thumbnail.app;

import agh.edu.pl.thumbnail.app.services.mqtt.MqttNotifierService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ThumbnailApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MqttNotifierService service = MqttNotifierService.getInstance();
        var controller = ThumbnailApplication.class.getResource("labguigallery.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(controller);
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        try{
            stage.show();
        }
        catch (Exception e)
        {
            System.out.println("exception in the main thread: " + e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}