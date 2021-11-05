package net.atommobile.pemudakahfi.news;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.atommobile.pemudakahfi.R;

import java.util.ArrayList;

/**
 * Created by root on 23/05/16.
 */
public class NewsAdapter extends ArrayAdapter<NewsModel> {

    Context context;
    int layoutResourceId;
    ArrayList<NewsModel> data = new ArrayList<NewsModel>();

    public NewsAdapter(Context context, int layoutResourceId, ArrayList<NewsModel> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();

            holder.lblJudul = (TextView) row.findViewById(R.id.lbl_judul);
            holder.lblSubJudul = (TextView) row.findViewById(R.id.lbl_sub_judul);
            holder.imgNews = (ImageView) row.findViewById(R.id.img_news);

            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        NewsModel item = data.get(position);

        holder.lblJudul.setText(item.getJudul());
        holder.lblSubJudul.setText(item.getSub_judul());

        if(!item.getGambar().equals("") && !item.getGambar().equals("0")){
            ImageView image = holder.imgNews;
            String gambar = item.getGambar();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(gambar, image);
        }

        return row;

    }

    static class RecordHolder {
        TextView lblJudul;
        TextView lblSubJudul;
        ImageView imgNews;
    }
    
}
