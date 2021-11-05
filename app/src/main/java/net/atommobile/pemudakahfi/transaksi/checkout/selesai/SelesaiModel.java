package net.atommobile.pemudakahfi.transaksi.checkout.selesai;

//import android.databinding.ObservableArrayList;
import androidx.databinding.ObservableArrayList;
/**
 * Created by faizlidwibrido on 9/8/16.
 */
public class SelesaiModel {

    private ObservableArrayList<SelesaiBankModel> list_bank = new ObservableArrayList<>();

    private String noorder;
    private String tanggal;
    private String total;
    private String pembayaran;

    public SelesaiModel(){}

    public void AddBank(String id, String bank, String norek, String atasnama){
        list_bank.add(new SelesaiBankModel(id, bank, norek, atasnama));
    }

    public String getPembayaran() {
        return pembayaran;
    }

    public ObservableArrayList<SelesaiBankModel> getList_bank() {
        return list_bank;
    }

    public String getNoorder() {
        return noorder;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getTotal() {
        return total;
    }

    public void setList_bank(ObservableArrayList<SelesaiBankModel> list_bank) {
        this.list_bank = list_bank;
    }

    public void setNoorder(String noorder) {
        this.noorder = noorder;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setTotal(String total) {
        this.total = String.format("%,d", Integer.parseInt(total)).replace(",", ".");
    }

    public void setPembayaran(String pembayaran) {
        this.pembayaran = pembayaran;
    }
}
