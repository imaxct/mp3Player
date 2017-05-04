package imaxct.controller;

import imaxct.Launcher;
import javafx.event.ActionEvent;
import javafx.util.Duration;

/**
 * Created by imaxct on 17-4-25.
 * mp3Player
 */
public class EventHandler {


    public static javafx.event.EventHandler<ActionEvent> player_pause_event = new javafx.event.EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) {
            Launcher.mp3Player.pause();
        }
    };


    public static javafx.event.EventHandler<ActionEvent> player_stop_event = new javafx.event.EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) {
            Launcher.mp3Player.stop();
        }
    };



    public static javafx.event.EventHandler<ActionEvent> player_forward_event = new javafx.event.EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) {
            Duration duration = Launcher.mp3Player.getPlayer().getCurrentTime();
            Duration total = Launcher.mp3Player.getPlayer().getTotalDuration();
            Duration pos = duration.add(Duration.seconds(5));
            if (!pos.greaterThan(total))
                Launcher.mp3Player.getPlayer().seek(pos);
        }
    };

    public static javafx.event.EventHandler<ActionEvent> player_backward_event = new javafx.event.EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) {
            Duration duration = Launcher.mp3Player.getPlayer().getCurrentTime();
            Duration pos = duration.subtract(Duration.seconds(5));
            if (!pos.lessThanOrEqualTo(Duration.ZERO))
                Launcher.mp3Player.getPlayer().seek(pos);
        }
    };
}
