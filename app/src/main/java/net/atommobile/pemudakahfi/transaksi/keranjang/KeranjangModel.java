package net.atommobile.pemudakahfi.transaksi.keranjang;

//import android.databinding.ObservableArrayList;
//import android.support.v4.app.Fragment;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;

/**
 * Created by faizlidwibrido on 9/8/16.
 */
public class KeranjangModel {

    private ObservableArrayList<KeranjangItemModel> list = new ObservableArrayList<>();

    private String total;

    public KeranjangModel(){}

    public void AddList(String id_produk, String id_attribut, String id_ukuran, String nama, String ukuran, String qty, String stok, String berat, String harga, String gambar, String id){
        list.add(new KeranjangItemModel(id_produk, id_attribut, id_ukuran, nama, ukuran, qty, stok, berat, harga, gambar, id));
    }

    public void AddList(String id_produk, String id_attribut, String id_ukuran, String nama, String ukuran, String qty, String stok, String berat, String harga, String gambar, String id, Fragment fragment){
        list.add(new KeranjangItemModel(id_produk, id_attribut, id_ukuran, nama, ukuran, qty, stok, berat, harga, gambar, id, fragment));
    }

    public ObservableArrayList<KeranjangItemModel> getList() {
        return list;
    }

    public String getTotal() {
        return total;
    }

    public void setList(ObservableArrayList<KeranjangItemModel> list) {
        this.list = list;
    }

    public void setTotal(String total) {
        this.total = String.format("%,d", Integer.parseInt(total)).replace(",", ".");
    }
}
