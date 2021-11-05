package net.atommobile.pemudakahfi.news;

/**
 * Created by root on 23/05/16.
 */
public class NewsModel {

    String id;
    String gambar;
    String judul;
    String sub_judul;

    public NewsModel(String id, String gambar, String judul, String sub_judul){
        super();
        this.id = id;
        this.gambar = gambar;
        this.judul = judul;
        this.sub_judul = sub_judul;
    }

    public String getId() {
        return id;
    }

    public String getGambar() {
        return gambar;
    }

    public String getJudul() {
        return judul;
    }

    public String getSub_judul() {
        return sub_judul;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setSub_judul(String sub_judul) {
        this.sub_judul = sub_judul;
    }
}
