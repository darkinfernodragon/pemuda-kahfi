package net.atommobile.pemudakahfi.video;

/**
 * Created by root on 27/04/16.
 */
public class VideoModel {

    String id;
    String url_video;
    String judul_video;

    public VideoModel(String id, String url_video, String judul_video){
        this.id = id;
        this.url_video = url_video;
        this.judul_video = judul_video;
    }

    public String getId() {
        return id;
    }

    public String getUrl_video() {
        return url_video;
    }

    public String getJudul_video() {
        return judul_video;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl_video(String url_video) {
        this.url_video = url_video;
    }

    public void setJudul_video(String judul_video) {
        this.judul_video = judul_video;
    }
}
