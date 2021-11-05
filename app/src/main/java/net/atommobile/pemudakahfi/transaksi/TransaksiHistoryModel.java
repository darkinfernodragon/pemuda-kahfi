package net.atommobile.pemudakahfi.transaksi;

//import android.databinding.ObservableArrayList;
import androidx.databinding.ObservableArrayList;

/**
 * Created by faizlidwibrido on 9/9/16.
 */
public class TransaksiHistoryModel {

    private ObservableArrayList<TransaksiModel> list = new ObservableArrayList<>();

    public TransaksiHistoryModel(){

    }

    public void AddHistory(String id, String nomor, String tanggal, String qty, String total, String pembayaran, String status){
        list.add(new TransaksiModel(id, nomor, tanggal, qty, total, pembayaran, status));
    }

    public ObservableArrayList<TransaksiModel> getList() {
        return list;
    }

}
