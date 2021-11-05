package net.atommobile.pemudakahfi.komentar;

/**
 * Created by root on 30/05/16.
 */
public class KomentarModel {

    String id;
    String nama;
    String waktu;
    String komentar;
    String status;

    public KomentarModel(String id, String nama, String waktu, String komentar, String status){
        super();
        this.id = id;
        this.nama = nama;
        this.waktu = waktu;
        this.komentar = komentar;
        this.status = status;
    }

    public String getNama() {
        return nama;
    }

    public String getId() {
        return id;
    }

    public String getKomentar() {
        return komentar;
    }

    public String getStatus() {
        return status;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
