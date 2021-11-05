package net.atommobile.pemudakahfi.album;

import java.io.Serializable;

/**
 * Created by root on 01/06/16.
 */
public class AlbumModel implements Serializable {

    String id;
    String gambar;
    String judul;
    String waktu;
    String keterangan;

    public AlbumModel(String id, String gambar, String waktu, String judul, String keterangan){
        super();
        this.id = id;
        this.gambar = gambar;
        this.judul = judul;
        this.waktu = waktu;
        this.keterangan = keterangan;
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

    public String getKeterangan() {
        return keterangan;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getWaktu() {
        return waktu;
    }

}
