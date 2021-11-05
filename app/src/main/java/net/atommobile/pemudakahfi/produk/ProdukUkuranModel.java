package net.atommobile.pemudakahfi.produk;

/**
 * Created by faizlidwibrido on 9/12/16.
 */
public class ProdukUkuranModel {

    private String id_ukuran;
    private String ukuran;
    private String berat;
    private String stok;

    public ProdukUkuranModel(String id_ukuran, String ukuran, String berat, String stok){
        this.id_ukuran = id_ukuran;
        this.ukuran = ukuran;
        this.berat = berat;
        this.stok = stok;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }

    public void setId_ukuran(String id_ukuran) {
        this.id_ukuran = id_ukuran;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public void setUkuran(String ukuran) {
        this.ukuran = ukuran;
    }

    public String getBerat() {
        return berat;
    }

    public String getId_ukuran() {
        return id_ukuran;
    }

    public String getStok() {
        return stok;
    }

    public String getUkuran() {
        return ukuran;
    }

}
