package net.atommobile.pemudakahfi.album;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.ext.TouchImageView;

import java.util.ArrayList;

/**
 * Created by root on 02/06/16.
 */
public class AlbumViewPagerAdapter extends PagerAdapter {

//    int[] imageId = {R.drawable.button1, R.drawable.button2, R.drawable.button3, R.drawable.button4, R.drawable.button5};
//    String[] imageTitle = {"Button 1", "Button 2", "Button 3", "Button 4", "Button 5"};

    Context context;
    ArrayList<AlbumModel> imageList = new ArrayList<AlbumModel>();
    FragmentManager fragmentManager;
    Fragment fragmentThis;

    AlbumModel item;

    public AlbumViewPagerAdapter(Context context, FragmentManager fragmentManager, Fragment fragment, ArrayList<AlbumModel> imageList){
        this.context = context;
        this.imageList = imageList;
        this.fragmentManager = fragmentManager;
        this.fragmentThis = fragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View viewItem = inflater.inflate(R.layout.page_album_view_item, container, false);

        RecordHolder holder = null;
        holder = new RecordHolder();

        item = imageList.get(position);

        holder.imgView = (TouchImageView) viewItem.findViewById(R.id.img_view);
        //imageView.setImageResource(imageId[position]);
        if(!item.getGambar().equals("") && !item.getGambar().equals("0")){
            ImageView image = holder.imgView;
            String gambar = item.getGambar();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(gambar, image);
        }

        holder.lblJudul = (TextView) viewItem.findViewById(R.id.lbl_judul);
        holder.lblJudul.setText(item.getJudul());

        holder.lblKeterangan = (TextView) viewItem.findViewById(R.id.lbl_keterangan);
        String keterangan = android.text.Html.fromHtml(item.getKeterangan()).toString();
        if(keterangan.equals("")){
            holder.lblKeterangan.setText("");
        } else {
            holder.lblKeterangan.setText(keterangan.substring(0, 10));
        }

        holder.btnDetail = (LinearLayout) viewItem.findViewById(R.id.btn_detail);
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "posisi : " + position);

                Bundle bundle = new Bundle();

                bundle.putSerializable("gridArray", imageList);
                bundle.putInt("gridPosition", position);

                Fragment fragment = new AlbumDetail();

                fragment.setArguments(bundle);

                if(fragment != null){
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.hide(fragmentThis);

                    fragmentTransaction.add(R.id.layout_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

            }
        });

        viewItem.setTag(holder);

        ((ViewPager)container).addView(viewItem);

        return viewItem;
    }

    static class RecordHolder {
        TextView lblJudul;
        TouchImageView imgView;
        TextView lblKeterangan;
        LinearLayout btnDetail;
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
