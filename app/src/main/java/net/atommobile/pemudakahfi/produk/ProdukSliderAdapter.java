package net.atommobile.pemudakahfi.produk;

import android.content.Context;
//import android.databinding.DataBindingUtil;
//import android.databinding.ObservableArrayList;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import android.os.Bundle;
import android.os.Parcelable;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.PagerAdapter;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.databinding.PageProdukSliderItemBinding;

/**
 * Created by faizlidwibrido on 9/1/16.
 */
public class ProdukSliderAdapter extends PagerAdapter {

    private ObservableArrayList<ProdukSliderModel> items = new ObservableArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private Fragment fragment;

    public ProdukSliderAdapter(Context context, ObservableArrayList<ProdukSliderModel> items, Fragment fragment){
        this.context = context;
        this.fragment = fragment;
        this.items = items;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        if (inflater == null) {
            inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        // set binding on layout
        PageProdukSliderItemBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.page_produk_slider_item, view, false);
        // set model binding
        binding.setModel(items.get(position));
        // add item view to to view group
        view.addView(binding.getRoot(), 0);

        if(fragment != null) {
            binding.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();

                    bundle.putSerializable("items", items);
                    bundle.putInt("items_position", position);

                    Fragment fragment_to = new ProdukSliderPage();
                    fragment_to.setArguments(bundle);

                    ((ProdukDetail) fragment).getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.layout_container, fragment_to).addToBackStack(null).commit();

                }
            });
        }

        return binding.getRoot();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
