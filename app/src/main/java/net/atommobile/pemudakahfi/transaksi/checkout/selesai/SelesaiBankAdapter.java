package net.atommobile.pemudakahfi.transaksi.checkout.selesai;

import android.content.Context;
//import android.databinding.DataBindingUtil;
//import android.databinding.ObservableArrayList;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.databinding.PageSelesaiBankItemBinding;

/**
 * Created by faizlidwibrido on 9/8/16.
 */
public class SelesaiBankAdapter extends BaseAdapter {

    Context context;
    ObservableArrayList<SelesaiBankModel> data;
    LayoutInflater inflater;

    public SelesaiBankAdapter(Context context, ObservableArrayList<SelesaiBankModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (inflater == null) {
            inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        PageSelesaiBankItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.page_selesai_bank_item, viewGroup, false);
        binding.setModel(data.get(i));

        return binding.getRoot();
    }

}