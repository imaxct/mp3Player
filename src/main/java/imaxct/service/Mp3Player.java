package imaxct.service;

import imaxct.bean.MediaInfo;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/***
 * Created by imaxct on 17-3-26.
 */
public class Mp3Player {

    private static MediaPlayer player;

    private static List<Media> medias;

    private static ObservableList<MediaInfo> mediaInfos;

    private static Mp3Player mp3Player;

    private static int currentMedia;

    private Mp3Player() {
        currentMedia = 0;
        medias = new ArrayList<Media>();
        mediaInfos = FXCollections.observableArrayList();
    }

    public static Mp3Player getInstance(){
        if (mp3Player == null){
            synchronized (Mp3Player.class){
                mp3Player = new Mp3Player();
            }
        }
        return mp3Player;
    }

    public MediaPlayer getPlayer(){
        return player;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public ObservableList<MediaInfo> getMediaInfos() {
        return mediaInfos;
    }

    public int getMediaNumber(){
        return medias == null ? 0 : medias.size();
    }

    public Media getCurrentMedia(){
        return medias ==null || medias.isEmpty() ? null : medias.get(currentMedia);
    }

    public MediaInfo getCurrentMediaInfo(){
        return mediaInfos == null || mediaInfos.isEmpty() ? null : mediaInfos.get(currentMedia);
    }

    public void play(URI uri) {
        addMedia(uri);
        currentMedia = medias.size() - 1;
        setMediaPosition(medias.size() - 1);
        play();
    }

    /**
     * 播放一个mp3
     * */
    public void setMediaPosition(int num) {
        if (num > medias.size() - 1 || num < 0){
            return;
        }
        currentMedia = num;
        System.out.println(num);
        synchronized (Mp3Player.class){
            player = new MediaPlayer(medias.get(num));
        }
    }

    /**
     * 继续播放
     * */
    public void play(){
        if (player != null) {
            player.play();
        }
    }

    /**
     * 暂停
     * */
    public void pause(){
        if (player != null) {
            player.pause();
        }
    }

    /**
     * 停止播放
     * */
    public void stop(){
        if (player != null) {
            player.stop();
        }
    }

    /**
     * 批量添加文件
     * */
    public void addMedia(URI[] uris){
        for (URI uri : uris){
            addMedia(uri);
        }
    }

    /**
     * 添加一个文件
     * */
    public void addMedia(URI uri){
        Media media = new Media(uri.toString());
        final MediaInfo info = new MediaInfo(uri.getPath());
        media.getMetadata().addListener(new MapChangeListener<String, Object>() {
            public void onChanged(Change<? extends String, ?> change) {
                if (change.wasAdded()){
                    MediaInfo.handleMediaInfo(info, change.getKey(), change.getValueAdded());
                }
            }
        });
        mediaInfos.add(info);
        medias.add(media);
    }

    public void playLast(){
        System.out.println(currentMedia);
        if (currentMedia - 1 >= 0){
            player.stop();
            setMediaPosition(currentMedia - 1);
            play();
        }
    }

    public void playNext(){
        System.out.println(currentMedia);
        if (currentMedia + 1 < medias.size()){
            player.stop();
            setMediaPosition(currentMedia+1);
            play();
        }
    }
    public boolean isPlaying(){
        return player != null && player.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public boolean isPaused(){
        return player != null && player.getStatus() == MediaPlayer.Status.PAUSED;
    }


    public int getCurrentNumber(){
        return currentMedia;
    }
}
