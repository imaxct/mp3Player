package imaxct.bean;

import javafx.scene.image.Image;

import java.io.Serializable;

/***
 * Created by imaxct on 17-3-27.
 */
public class MediaInfo implements Serializable{
    private String album = "未知";
    private String artist = "未知";
    private String title;
    private String year;
    private Image image;

    public MediaInfo(){}

    public MediaInfo(String title){
        this.title = title;
    }
    public MediaInfo(String album, String artist, String title, String year, Image image){
        this.album = album;
        this.artist = artist;
        this.title = title;
        this.year = year;
        this.image = image;
    }

    public static void handleMediaInfo(MediaInfo info, String key, Object value){
            if ("album".equals(key)){
                info.setAlbum(value.toString());
            }else if ("artist".equals(key)){
                info.setArtist(value.toString());
            }else if ("title".equals(key)){
                info.setTitle(value.toString());
            }else if ("year".equals(key)){
                info.setYear(value.toString());
            }else if ("image".equals(key)){
                info.setImage((Image)value);
            }
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}
