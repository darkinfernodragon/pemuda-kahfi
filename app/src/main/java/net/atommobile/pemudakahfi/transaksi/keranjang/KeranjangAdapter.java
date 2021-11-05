package net.atommobile.pemudakahfi.transaksi.keranjang;

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
import net.atommobile.pemudakahfi.databinding.PageKeranjangItemBinding;

/**
 * Created by root on 31/08/16.
 */
public class KeranjangAdapter extends BaseAdapter {

    Context context;
    ObservableArrayList<KeranjangItemModel> data;
    LayoutInflater inflater;

    public KeranjangAdapter(Context context, ObservableArrayList<KeranjangItemModel> data){
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

        if(inflater == null){
            inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        PageKeranjangItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.page_keranjang_item, viewGroup, false);
        binding.setModel(data.get(i));

        KeranjangItemEvent event = new KeranjangItemEvent();
        binding.setEvent(event);

        return binding.getRoot();
    }

}
