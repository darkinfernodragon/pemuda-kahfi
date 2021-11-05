package net.atommobile.pemudakahfi.transaksi.keranjang;

//import android.support.v4.app.Fragment;
import androidx.fragment.app.Fragment;

/**
 * Created by faizlidwibrido on 9/8/16.
 */
public class KeranjangItemModel {

    private String id_produk;
    private String id_attribut;
    private String id_ukuran;
    private String ukuran;
    private String nama;
    private String qty;
    private String stok;
    private String berat;
    private String harga;

    private String gambar;
    private Fragment fragment;

    private String subtotal;
    private String id;

    public KeranjangItemModel(){}

    public KeranjangItemModel(String id_produk, String id_attribut, String id_ukuran, String nama, String ukuran, String qty, String stok, String berat, String harga,
                              String gambar) {
        this.id_produk = id_produk;
        this.id_attribut = id_attribut;
        this.id_ukuran = id_ukuran;
        this.nama = nama;
        this.qty = qty;
        this.stok = stok;
        this.berat = berat;
        this.harga = String.format("%,d", Integer.parseInt(harga)).replace(",", ".");;
        this.gambar = gambar;
        this.ukuran = ukuran;

        this.subtotal = String.format("%,d", (Integer.parseInt(qty) * Integer.parseInt(harga))).replace(",", ".");
    }

    public KeranjangItemModel(String id_produk, String id_attribut, String id_ukuran, String nama, String ukuran, String qty, String stok, String berat, String harga,
                              String gambar, String id) {
        this.id_produk = id_produk;
        this.id_attribut = id_attribut;
        this.id_ukuran = id_ukuran;
        this.nama = nama;
        this.qty = qty;
        this.stok = stok;
        this.berat = berat;
        this.harga = String.format("%,d", Integer.parseInt(harga)).replace(",", ".");
        this.gambar = gambar;
        this.ukuran = ukuran;

        this.subtotal = String.format("%,d", (Integer.parseInt(qty) * Integer.parseInt(harga))).replace(",", ".");

        this.id = id;
    }

    public KeranjangItemModel(String id_produk, String id_attribut, String id_ukuran, String nama, String ukuran, String qty, String stok, String berat, String harga,
                              String gambar, String id, Fragment fragment) {
        this.id_produk = id_produk;
        this.id_attribut = id_attribut;
        this.id_ukuran = id_ukuran;
        this.nama = nama;
        this.qty = qty;
        this.stok = stok;
        this.berat = berat;
        this.harga = String.format("%,d", Integer.parseInt(harga)).replace(",", ".");
        this.gambar = gambar;
        this.ukuran = ukuran;

        this.fragment = fragment;

        this.subtotal = String.format("%,d", (Integer.parseInt(qty) * Integer.parseInt(harga))).replace(",", ".");

        this.id = id;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public String getStok() {
        return stok;
    }

    public String getUkuran() {
        return ukuran;
    }

    public String getBerat() {
        return berat;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = String.format("%,d", Integer.parseInt(subtotal)).replace(",", ".");
    }

    public void setHarga(String harga) {
        this.harga = String.format("%,d", Integer.parseInt(harga)).replace(",", ".");
    }

    public void setId_attribut(String id_attribut) {
        this.id_attribut = id_attribut;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public void setId_ukuran(String id_ukuran) {
        this.id_ukuran = id_ukuran;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHarga() {
        return harga;
    }

    public String getId_attribut() {
        return id_attribut;
    }

    public String getId_produk() {
        return id_produk;
    }

    public String getId_ukuran() {
        return id_ukuran;
    }

    public String getNama() {
        return nama;
    }

    public String getQty() {
        return qty;
    }

    public String getId() {
        return id;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public String getGambar() {
        return gambar;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }

    public void setUkuran(String ukuran) {
        this.ukuran = ukuran;
    }
}
