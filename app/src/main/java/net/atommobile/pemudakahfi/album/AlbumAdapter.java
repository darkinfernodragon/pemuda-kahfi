package net.atommobile.pemudakahfi.album;

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
 * Created by root on 01/06/16.
 */
public class AlbumAdapter extends ArrayAdapter<AlbumModel> {

    Context context;
    int layoutResourceId;
    ArrayList<AlbumModel> data = new ArrayList<AlbumModel>();

    public AlbumAdapter(Context context, int layoutResourceId, ArrayList<AlbumModel> data) {
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
            holder.txtJudul = (TextView) row.findViewById(R.id.txt_album);
            holder.imgAlbum = (ImageView) row.findViewById(R.id.img_album);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        AlbumModel item = data.get(position);

        holder.txtJudul.setText(item.getJudul());

        ImageView image = holder.imgAlbum;

        if(!item.getGambar().equals("") && !item.getGambar().equals("0")){
            String gambar = item.getGambar();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(gambar, image);
        }

        return row;

    }

    static class RecordHolder {
        TextView txtJudul;
        ImageView imgAlbum;

    }
    
}
