package imaxct;

import imaxct.service.Mp3Player;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Launcher extends Application {

    public static Mp3Player mp3Player = Mp3Player.getInstance();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(Launcher.class.getResource("/PlayerView.fxml"));
            primaryStage.setTitle("Media Player");
            primaryStage.setScene(new Scene(root));
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent event) {
                    if (mp3Player.isPlaying())
                        mp3Player.stop();
                }
            });
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
