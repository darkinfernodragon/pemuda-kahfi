package net.atommobile.pemudakahfi.news;

/**
 * Created by root on 24/05/16.
 */
public class KategoriModel {

    String id;
    String nama;

    public KategoriModel(String id, String nama){
        super();
        this.id = id;
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
