package net.atommobile.pemudakahfi.video;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.atommobile.pemudakahfi.R;

import java.util.ArrayList;

/**
 * Created by root on 27/04/16.
 */
public class VideoAdapter extends ArrayAdapter<VideoModel> {

    Context context;
    int layoutResourceId;
    FragmentManager fragmentManager;
    Fragment fragmentThis;

    private static final int RECOVERY_REQUEST = 10;
    ArrayList<VideoModel> data = new ArrayList<VideoModel>();

    public VideoAdapter(Context context, int layoutResourceId, ArrayList<VideoModel> data, FragmentManager fragmentManager, Fragment fragment) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.fragmentManager = fragmentManager;
        this.fragmentThis = fragment;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.webVideo = (WebView) row.findViewById(R.id.web_video);
            holder.youTubePreview = (ImageView) row.findViewById(R.id.youtube_view);
            holder.page_video_item = (LinearLayout) row.findViewById(R.id.page_video_item);
            holder.txtJudul = (TextView) row.findViewById(R.id.txt_video);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        //int dpHeight = (int)(displaymetrics.heightPixels / displaymetrics.density + 0.5);
        int width = (int)(displaymetrics.widthPixels / displaymetrics.density + 0.5);
        //int width = displaymetrics.widthPixels;

        int width1 = 560;
        int height1 = 315;

        int height = (width * height1) / width1;

        final VideoModel item = data.get(position);

        Log.e("Width", String.valueOf(width));
        Log.e("Height", String.valueOf(height));

        String frameVideo = "<iframe width='100%' height='300' src='" + item.getUrl_video() + "' frameborder='0' allowfullscreen></iframe>";
 
        String first_filter = item.getUrl_video().substring(item.getUrl_video().lastIndexOf('/') + 1);


        final String str_youtube_id = first_filter.substring(0, 11);
//        Log.e("Video Adapter", "youtube id"+str_youtube_id);

        String fileName = "https://img.youtube.com/vi/" + str_youtube_id + "/mqdefault.jpg";
//        Log.e("TAG", "getView:fileName "+fileName);
        ImageView image = holder.youTubePreview;
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(fileName, image);

        // holder.webVideo.getSettings().setJavaScriptEnabled(true);
        // holder.webVideo.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        holder.webVideo.getSettings().setLoadWithOverviewMode(true);
//        if (Build.VERSION.SDK_INT < 8) {
//            holder.webVideo.getSettings().setPluginsEnabled(true);
//        } else {
//            holder.webVideo.getSettings().setPluginState(WebSettings.PluginState.ON);
//        }
        // holder.webVideo.setWebChromeClient(new WebChromeClient() {});

        // holder.webVideo.loadData(frameVideo, "text/html", "utf-8");

        holder.txtJudul.setText(item.getJudul_video());

        holder.page_video_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                bundle.putString("judul", item.getJudul_video());
                bundle.putString("video_id", str_youtube_id);
//                Log.e("Video Adapter", "click youtube id"+str_youtube_id);

                Fragment fragment = new VideoDetail();

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

        return row;

    }

    static class RecordHolder {
        WebView webVideo;
        ImageView youTubePreview;
        TextView txtJudul;
        LinearLayout page_video_item;
    }

}
