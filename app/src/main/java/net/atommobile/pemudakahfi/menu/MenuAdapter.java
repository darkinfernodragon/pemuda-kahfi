package net.atommobile.pemudakahfi.menu;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.atommobile.pemudakahfi.R;

import java.util.ArrayList;

/**
 * Created by root on 18/04/16.
 */
public class MenuAdapter extends ArrayAdapter<MenuModel> {

    Context context;
    int layoutResourceId;
    ArrayList<MenuModel> data = new ArrayList<MenuModel>();

    public MenuAdapter(Context context, int layoutResourceId, ArrayList<MenuModel> data) {
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
            holder.txtMenu = (TextView) row.findViewById(R.id.txt_menu);
            holder.imgMenu = (ImageView) row.findViewById(R.id.img_menu);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        MenuModel item = data.get(position);

        holder.txtMenu.setText(item.getCaption());

        ImageView image = holder.imgMenu;
        image.setImageResource(item.getImage());

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int widthPX = displaymetrics.widthPixels;
        int widthDP = pxToDp(widthPX, context);

        int imgSize = (widthDP / 2) - 48;
        if(imgSize > 100){
            imgSize = 100;
        }

        int imgSizePX = dpToPx(imgSize, context);

        image.getLayoutParams().width = imgSizePX;
        image.getLayoutParams().height = imgSizePX;

        return row;

    }

    static class RecordHolder {
        TextView txtMenu;
        ImageView imgMenu;

    }

    public int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public int pxToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

}
