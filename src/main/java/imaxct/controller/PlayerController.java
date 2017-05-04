package imaxct.controller;

import imaxct.Launcher;
import imaxct.bean.MediaInfo;
import imaxct.service.Mp3Player;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/***
 * Created by imaxct on 17-3-26.
 */
public class PlayerController implements Initializable {
    @FXML private Button playBtn;
    @FXML private Button pauseBtn;
    @FXML private Button stopBtn;
    @FXML private Button lastBtn;
    @FXML private Button nextBtn;
    @FXML private Button forwardBtn;
    @FXML private Button backwardBtn;
    @FXML private MenuItem menu_open;
    @FXML private MenuItem menu_open_dir;
    @FXML private MenuItem menu_exit;
    @FXML private MenuItem menu_play;
    @FXML private MenuItem menu_pause;
    @FXML private MenuItem menu_stop;
    @FXML private MenuItem menu_next;
    @FXML private MenuItem menu_last;
    @FXML private MenuItem menu_forward;
    @FXML private MenuItem menu_backward;
    @FXML private Label mediaTitle;
    @FXML private Label currentTime;
    @FXML private Label totalTime;
    @FXML private TableView<MediaInfo> mediaList;
    @FXML private TableColumn titleColumn;
    @FXML private TableColumn albumColumn;
    @FXML private TableColumn artistColumn;
    @FXML private MediaView mediaView;
    @FXML private Slider playSlider;
    @FXML private Slider volumeSlider;
    @FXML private AnchorPane firstPane;
    @FXML private AnchorPane secondPane;
    @FXML private SplitPane mainPane;

    private static int width;
    private static int height;

    public void initialize(URL location, ResourceBundle resources) {

        menu_open.setOnAction(menu_open_event);
        menu_exit.setOnAction(menu_exit_event);

        menu_play.setOnAction(player_play_event);
        menu_pause.setOnAction(imaxct.controller.EventHandler.player_pause_event);
        menu_stop.setOnAction(imaxct.controller.EventHandler.player_stop_event);
        menu_next.setOnAction(player_next_event);
        menu_last.setOnAction(player_last_event);

        playBtn.setOnAction(player_play_event);
        pauseBtn.setOnAction(imaxct.controller.EventHandler.player_pause_event);
        stopBtn.setOnAction(imaxct.controller.EventHandler.player_stop_event);
        lastBtn.setOnAction(player_last_event);
        nextBtn.setOnAction(player_next_event);
        forwardBtn.setOnAction(imaxct.controller.EventHandler.player_forward_event);
        backwardBtn.setOnAction(imaxct.controller.EventHandler.player_backward_event);
        menu_forward.setOnAction(imaxct.controller.EventHandler.player_forward_event);
        menu_backward.setOnAction(imaxct.controller.EventHandler.player_backward_event);
        menu_open_dir.setOnAction(menu_open_dir_event);

        titleColumn.setCellValueFactory(new PropertyValueFactory<MediaInfo, String>("title"));
        albumColumn.setCellValueFactory(new PropertyValueFactory<MediaInfo, String>("album"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<MediaInfo, String>("artist"));

        mediaList.setItems(Launcher.mp3Player.getMediaInfos());

        mediaList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)){
                    if (event.getClickCount() == 2){

                    }
                }
            }
        });

        playSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                if (playSlider.isValueChanging()){
                    Launcher.mp3Player.getPlayer().seek(Duration.seconds(playSlider.getValue()));
                }
            }
        });

        volumeSlider.valueChangingProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                if (volumeSlider.isValueChanging() && Launcher.mp3Player.isPlaying()) {
                    Launcher.mp3Player.getPlayer().setVolume(volumeSlider.getValue() / 100.00);
                }
            }
        });
    }

    private EventHandler<ActionEvent> menu_exit_event = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) {
            Launcher.mp3Player.stop();
            System.exit(0);
        }
    };

    private EventHandler<ActionEvent> menu_open_event = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) {
            FileChooser chooser = new FileChooser();
            chooser.setInitialDirectory(new File(System.getProperty("user.home")));
            chooser.setTitle("select media file");
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("media file",
                            "*.aif", "*.aiff", "*.fxm", "*.flv", "*.m3u8",
                            "*.mp3","*.mp4", "*.m4a", "*.m4v","*.wav")
            );
            List<File> files = chooser.showOpenMultipleDialog(null);
            for (File file : files){
                if (file.canRead()){
                    Launcher.mp3Player.addMedia(file.toURI());
                }
            }
            play(Launcher.mp3Player.getMediaNumber() - 1);
        }
    };

    private javafx.event.EventHandler<ActionEvent> player_play_event = new javafx.event.EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) {
            play();
        }
    };

    private void play(){
        if (Launcher.mp3Player.isPaused()){
            Launcher.mp3Player.play();
        }else {
            if (Launcher.mp3Player.isPlaying()) {
                Launcher.mp3Player.stop();
            }
            Launcher.mp3Player.setMediaPosition(Launcher.mp3Player.getMediaNumber() - 1);
            mediaView.setMediaPlayer(Launcher.mp3Player.getPlayer());
            initPlayer();
            Launcher.mp3Player.play();
        }
    }

    private void play(int num){
        if (Launcher.mp3Player.isPaused()){
            Launcher.mp3Player.play();
        }else {
            Launcher.mp3Player.setMediaPosition(num);
            mediaView.setMediaPlayer(Launcher.mp3Player.getPlayer());
            initPlayer();
            Launcher.mp3Player.play();
        }
    }

    private void play(File file){
        if (file != null){
            if (Launcher.mp3Player.isPlaying()){
                Launcher.mp3Player.stop();
            }
            Launcher.mp3Player.addMedia(file.toURI());
            Launcher.mp3Player.setMediaPosition(Launcher.mp3Player.getMediaNumber() - 1);

            mediaView.setMediaPlayer(Launcher.mp3Player.getPlayer());
            initPlayer();
            Launcher.mp3Player.play();

        }
    }


    private void initPlayer(){
        Launcher.mp3Player.getPlayer().setOnReady(new Runnable() {
            public void run() {
                Duration totalDuration = Launcher.mp3Player.getPlayer().getTotalDuration();
                playSlider.setMax(totalDuration.toSeconds());
                volumeSlider.setValue(Launcher.mp3Player.getPlayer().getVolume() * 100.0);
                int seconds = (int) totalDuration.toSeconds();
                int hours = seconds / (60 * 60);
                int minutes = (seconds - hours * 60 * 60) / 60;
                int second = seconds % 60;
                totalTime.setText(String.format("%02d:%02d:%02d", hours, minutes, second));

                //set mediaView
                width = Launcher.mp3Player.getCurrentMedia().getWidth();
                height = Launcher.mp3Player.getCurrentMedia().getHeight();
                System.out.println(width + "\t" + height);
                mediaView.setFitWidth(width);
                mediaView.setFitHeight(height);

                String title = Launcher.mp3Player.getMediaInfos().get(Launcher.mp3Player.getCurrentNumber()).getTitle();
                String fileName = Launcher.mp3Player.getCurrentMedia().getSource().substring("file:".length());

                if (title != null && title.equals(fileName)){
                    mediaTitle.setText(title.substring(title.lastIndexOf('/') + 1));
                }else {
                    mediaTitle.setText(title);
                }
            }
        });

        Launcher.mp3Player.getPlayer().currentTimeProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                updateValue();
            }
        });

        Launcher.mp3Player.getPlayer().setOnEndOfMedia(new Runnable() {
            public void run() {
                //mediaView.setFitWidth(0);
                //mediaView.setFitHeight(0);
            }
        });
    }

    protected void updateValue(){
        Platform.runLater(new Runnable() {
            public void run() {
                Duration duration = Launcher.mp3Player.getPlayer().getCurrentTime();
                playSlider.setDisable(duration.isUnknown());
                if (!playSlider.isDisabled() && !playSlider.isValueChanging()) {
                    playSlider.setValue(duration.toSeconds());
                }
                int seconds = (int) duration.toSeconds();
                int hours = seconds / (60 * 60);
                int minutes = (seconds - hours * 60 * 60) / 60;
                int second = seconds % 60;
                currentTime.setText(String.format("%02d:%02d:%02d", hours, minutes, second));
            }
        });
    }

    private javafx.event.EventHandler<ActionEvent> menu_open_dir_event = new javafx.event.EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) {
            DirectoryChooser chooser = new DirectoryChooser();
            File dir = chooser.showDialog(null);
            if (dir.isDirectory()){
                File[] files = dir.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        System.out.println(pathname);
                        return pathname.getAbsolutePath().toLowerCase()
                                .matches("^(.*?)(mp4|mp3|aif|aiff|fxm|flv|m3u8|m4a|m4v|wav)$");
                    }
                });
                for (File file : files){
                    Launcher.mp3Player.addMedia(file.toURI());
                }
                System.out.println(Launcher.mp3Player.getMediaNumber());
                play();
                System.out.println(Launcher.mp3Player.getCurrentNumber());
            }
        }
    };

    private javafx.event.EventHandler<ActionEvent> player_next_event = new javafx.event.EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) {
            Launcher.mp3Player.playNext();
            initPlayer();
        }
    };

    private javafx.event.EventHandler<ActionEvent> player_last_event = new javafx.event.EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) {
            Launcher.mp3Player.playLast();
            initPlayer();
        }
    };
}
