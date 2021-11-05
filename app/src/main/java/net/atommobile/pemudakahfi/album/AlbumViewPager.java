package net.atommobile.pemudakahfi.album;

import android.os.Bundle;
//import android.support.annotation.Nullable;
import androidx.annotation.Nullable;
//import android.support.v4.app.Fragment;
import androidx.fragment.app.Fragment;
//import android.support.v4.view.PagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
//import android.support.v4.view.ViewPager;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.atommobile.pemudakahfi.R;

import java.util.ArrayList;

/**
 * Created by root on 02/06/16.
 */
public class AlbumViewPager extends Fragment {

    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.page_album_view, container, false);

        ArrayList<AlbumModel> fileList =  (ArrayList<AlbumModel>)this.getArguments().getSerializable("gridArray");
        int posisiDefault = this.getArguments().getInt("gridPosition");

        viewPager = (ViewPager) v.findViewById(R.id.pager_view);
        PagerAdapter adapter = new AlbumViewPagerAdapter(getActivity(), getFragmentManager(), this, fileList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(posisiDefault);

        return v;
    }

}
