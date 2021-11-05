package net.atommobile.pemudakahfi.produk;

import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.atommobile.pemudakahfi.R;

import java.util.ArrayList;

/**
 * Created by faizlidwibrido on 9/17/16.
 */
public class ProdukSliderPage extends Fragment {

    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.page_produk_slider_page, container, false);

        ArrayList<ProdukSliderModel> fileList =  (ArrayList<ProdukSliderModel>)this.getArguments().getSerializable("items");
        int posisiDefault = this.getArguments().getInt("items_position");

        viewPager = (ViewPager) v.findViewById(R.id.pager_view);
        PagerAdapter adapter = new ProdukSliderPageAdapter(getActivity(), getFragmentManager(), this, fileList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(posisiDefault);

        return v;
    }
}
