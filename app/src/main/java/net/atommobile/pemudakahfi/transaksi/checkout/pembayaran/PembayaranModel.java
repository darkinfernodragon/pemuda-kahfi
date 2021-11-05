package net.atommobile.pemudakahfi.transaksi.checkout.pembayaran;

/**
 * Created by faizlidwibrido on 9/8/16.
 */
public class PembayaranModel {

    private String berat;
    private String ongkir;
    private String total;

    public PembayaranModel(String berat, String ongkir, String total){
        this.berat = berat;
        this.ongkir = String.format("%,d", Integer.parseInt(ongkir)).replace(",", ".");
        this.total = String.format("%,d", Integer.parseInt(total)).replace(",", ".");
    }

    public void setTotal(String total) {
        this.total = String.format("%,d", Integer.parseInt(total)).replace(",", ".");
    }

    public void setOngkir(String ongkir) {
        this.ongkir = String.format("%,d", Integer.parseInt(ongkir)).replace(",", ".");
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }

    public String getTotal() {
        return total;
    }

    public String getOngkir() {
        return ongkir;
    }

    public String getBerat() {
        return berat;
    }
}
