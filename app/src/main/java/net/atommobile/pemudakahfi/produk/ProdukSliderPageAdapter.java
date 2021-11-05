package net.atommobile.pemudakahfi.produk;

import android.app.Activity;
import android.content.Context;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.ext.TouchImageView;

import java.util.ArrayList;

/**
 * Created by faizlidwibrido on 9/17/16.
 */
public class ProdukSliderPageAdapter extends PagerAdapter {

    Context context;
    ArrayList<ProdukSliderModel> imageList = new ArrayList<ProdukSliderModel>();
    FragmentManager fragmentManager;
    Fragment fragmentThis;

    ProdukSliderModel item;

    public ProdukSliderPageAdapter(Context context, FragmentManager fragmentManager, Fragment fragment, ArrayList<ProdukSliderModel> imageList){
        this.context = context;
        this.imageList = imageList;
        this.fragmentManager = fragmentManager;
        this.fragmentThis = fragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View viewItem = inflater.inflate(R.layout.page_produk_slider_page_item, container, false);

        RecordHolder holder = null;
        holder = new RecordHolder();

        item = imageList.get(position);

        holder.imgView = (TouchImageView) viewItem.findViewById(R.id.img_view);
        //imageView.setImageResource(imageId[position]);
        if(!item.getImage().equals("") && !item.getImage().equals("0")){
            ImageView image = holder.imgView;
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(item.getImage(), image);
        }

        viewItem.setTag(holder);

        ((ViewPager)container).addView(viewItem);

        return viewItem;
    }

    static class RecordHolder {
        TouchImageView imgView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }

}