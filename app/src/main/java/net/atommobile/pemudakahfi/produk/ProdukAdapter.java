package net.atommobile.pemudakahfi.produk;

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
import net.atommobile.pemudakahfi.databinding.PageProdukItemBinding;

/**
 * Created by root on 31/08/16.
 */
public class ProdukAdapter extends BaseAdapter {

    Context context;
    ObservableArrayList<ProdukItemModel> data;
    LayoutInflater inflater;

    public ProdukAdapter(Context context, ObservableArrayList<ProdukItemModel> data){
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

        PageProdukItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.page_produk_item, viewGroup, false);
        binding.setModel(data.get(i));

        return binding.getRoot();
    }

}
