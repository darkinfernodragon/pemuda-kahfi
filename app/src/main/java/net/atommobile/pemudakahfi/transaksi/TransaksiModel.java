package net.atommobile.pemudakahfi.transaksi;

/**
 * Created by faizlidwibrido on 9/9/16.
 */
public class TransaksiModel {

    private String id;
    private String nomor;
    private String tanggal;
    private String qty;
    private String total;
    private String pembayaran;
    private String status;

    public TransaksiModel(){}

    public TransaksiModel(String id, String nomor, String tanggal, String qty, String total, String pembayaran, String status){
        this.id = id;
        this.nomor = nomor;
        this.tanggal = tanggal;
        this.qty = qty;
        this.total = String.format("%,d", Integer.parseInt(total)).replace(",", ".");
        this.pembayaran = pembayaran;
        this.status = status;
    }

    public String getPembayaran() {
        return pembayaran;
    }

    public void setPembayaran(String pembayaran) {
        this.pembayaran = pembayaran;
    }

    public String getQty() {
        return qty;
    }

    public String getId() {
        return id;
    }

    public String getNomor() {
        return nomor;
    }

    public String getStatus() {
        return status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getTotal() {
        return total;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setTotal(String total) {
        this.total = String.format("%,d", Integer.parseInt(total)).replace(",", ".");
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
