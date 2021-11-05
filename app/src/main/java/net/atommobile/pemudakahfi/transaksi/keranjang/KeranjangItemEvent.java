package net.atommobile.pemudakahfi.transaksi.keranjang;

import android.content.Context;
import android.content.DialogInterface;
//import android.support.v4.app.Fragment;
//import android.support.v7.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.atommobile.pemudakahfi.MainActivity;
import net.atommobile.pemudakahfi.db.DatabaseHandler;

/**
 * Created by faizlidwibrido on 9/8/16.
 */
public class KeranjangItemEvent {

    DatabaseHandler db;

    public void initDatabase(Context context){
        db = new DatabaseHandler(context);
    }

    public void increaseQty(View view, KeranjangItemModel item){

        int qty = Integer.parseInt(item.getQty()) + 1;
        Log.e("Increase QTY", String.valueOf(qty));
        updateQty(view, item, qty);

    }

    public void decreaseQty(View view, KeranjangItemModel item){

        int qty = Integer.parseInt(item.getQty()) - 1;
        updateQty(view, item, qty);

    }

    public void updateQty(View view, KeranjangItemModel item, int qty){

        Context context = view.getContext();
        initDatabase(context);

        int stok = Integer.parseInt(item.getStok());

        if (qty <= 0) {
            Toast.makeText(context, "Jumlah item tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (qty > stok) {
            Toast.makeText(context, "Stok tidak cukup", Toast.LENGTH_SHORT).show();
        } else {

            item.setQty(String.valueOf(qty));
            db.updateKeranjang(item);

            Fragment fragment = item.getFragment();
            ((KeranjangPage) fragment).updateData();
        }

    }

    public void deleteItem(View view, final KeranjangItemModel item){

        Context context = view.getContext();
        initDatabase(context);

        new AlertDialog.Builder(context)
                .setTitle("Hapus")
                .setMessage("Anda yakin menghapus produk ini?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        db.deleteKeranjang(item);

                        Fragment fragment = item.getFragment();
                        ((MainActivity) fragment.getActivity()).setNotifCount();

                        ((KeranjangPage) fragment).updateData();

                    }})

                .setNegativeButton("Tidak", null).show();

    }

}
