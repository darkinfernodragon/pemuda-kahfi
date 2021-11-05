package net.atommobile.pemudakahfi.transaksi.checkout.review;

//import android.databinding.ObservableArrayList;
import androidx.databinding.ObservableArrayList;
import net.atommobile.pemudakahfi.transaksi.keranjang.KeranjangItemModel;

/**
 * Created by faizlidwibrido on 9/8/16.
 */
public class ReviewModel {

    private ObservableArrayList<KeranjangItemModel> list = new ObservableArrayList<>();

    private String subtotal;
    private String ongkir;
    private String total;
    private String alamat;
    private String alamat2;
    private String jasa;
    private String layanan;
    private String nama;
    private String telepon;
    private String pembayaran;
    private String status;

    private String nomor;
    private String tanggal;

    public ReviewModel(){}

    public void AddList(String id_produk, String id_attribut, String id_ukuran, String nama, String ukuran, String qty, String stok, String berat, String harga, String gambar, String id){
        list.add(new KeranjangItemModel(id_produk, id_attribut, id_ukuran, nama, ukuran, qty, stok, berat, harga, gambar, id));
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOngkir(String ongkir) {
        this.ongkir = String.format("%,d", Integer.parseInt(ongkir)).replace(",", ".");
    }

    public void setTotal(String total) {
        this.total = String.format("%,d", Integer.parseInt(total)).replace(",", ".");
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setAlamat2(String alamat2) {
        this.alamat2 = alamat2;
    }

    public void setJasa(String jasa) {
        this.jasa = jasa;
    }

    public void setLayanan(String layanan) {
        this.layanan = layanan;
    }

    public void setList(ObservableArrayList<KeranjangItemModel> list) {
        this.list = list;
    }

    public void setPembayaran(String pembayaran) {
        this.pembayaran = pembayaran;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = String.format("%,d", Integer.parseInt(subtotal)).replace(",", ".");
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getOngkir() {
        return ongkir;
    }

    public String getTotal() {
        return total;
    }

    public ObservableArrayList<KeranjangItemModel> getList() {
        return list;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getAlamat2() {
        return alamat2;
    }

    public String getJasa() {
        return jasa;
    }

    public String getLayanan() {
        return layanan;
    }

    public String getPembayaran() {
        return pembayaran;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public String getTelepon() {
        return telepon;
    }

    public String getStatus() {
        return status;
    }
}

