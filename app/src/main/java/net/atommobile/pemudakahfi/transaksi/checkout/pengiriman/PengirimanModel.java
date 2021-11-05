package net.atommobile.pemudakahfi.transaksi.checkout.pengiriman;

/**
 * Created by faizlidwibrido on 9/8/16.
 */
public class PengirimanModel {

    private String nama;
    private String telepon;
    private String alamat;
    private String provinsi;
    private String kota;
    private String kodepos;
    private String jasa;
    private String layanan;
    private String berat;
    private String ongkir;

    private String total;

    public PengirimanModel(){}

    public void setDataMember(String nama, String telepon, String alamat, String provinsi, String kota, String kodepos, String jasa, String layanan, String berat, String ongkir){
        this.nama = nama;
        this.telepon = telepon;
        this.alamat = alamat;
        this.provinsi = provinsi;
        this.kota = kota;
        this.kodepos = kodepos;
        this.jasa = jasa;
        this.layanan = layanan;
        this.berat = berat;
        this.ongkir = String.format("%,d", Integer.parseInt(ongkir)).replace(",", ".");
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTotal(String total) {
        this.total = String.format("%,d", Integer.parseInt(total)).replace(",", ".");
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }

    public void setJasa(String jasa) {
        this.jasa = jasa;
    }

    public void setKodepos(String kodepos) {
        this.kodepos = kodepos;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public void setLayanan(String layanan) {
        this.layanan = layanan;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setOngkir(String ongkir) {
        this.ongkir = String.format("%,d", Integer.parseInt(ongkir)).replace(",", ".");
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getBerat() {
        return berat;
    }

    public String getJasa() {
        return jasa;
    }

    public String getKodepos() {
        return kodepos;
    }

    public String getKota() {
        return kota;
    }

    public String getLayanan() {
        return layanan;
    }

    public String getNama() {
        return nama;
    }

    public String getOngkir() {
        return ongkir;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public String getTotal() {
        return total;
    }
}
