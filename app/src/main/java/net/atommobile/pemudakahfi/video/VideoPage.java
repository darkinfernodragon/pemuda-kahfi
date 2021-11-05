package net.atommobile.pemudakahfi.video;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import net.atommobile.pemudakahfi.MainActivity;
import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.parser.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 27/04/16.
 */
public class VideoPage extends Fragment {

    String api = "video_array/";
    ListView listVideo;
    String idMenu;

    ArrayList<VideoModel> mItems = new ArrayList<VideoModel>();
    VideoAdapter videoAdapter;

    Fragment menuFragment = this;
    int numItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.page_video,container,false);

        idMenu = this.getArguments().getString("idMenu");

        String awal = this.getArguments().getString("awal");
        if(awal.equals("")){

            LinearLayout layout_video = (LinearLayout) v.findViewById(R.id.layout_video);
            ((MainActivity)getActivity()).setBackground(v, layout_video, getResources());

            ImageView logo = (ImageView) v.findViewById(R.id.img_logo);
            logo.setVisibility(View.GONE);
        }

        listVideo = (ListView) v.findViewById(R.id.list_video);

        new JSONParse().execute();

        return v;

    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            String url = api + idMenu;

            JSONParser jParser = new JSONParser(getActivity()); // get JSON data from URL
            JSONObject json = jParser.makeHttpRequest(url, "POST", params);

            Log.e("json", json.toString());

            return json;

        }

        @Override
        protected void onPostExecute(JSONObject json) {

            pDialog.dismiss();

            try {

                if(json != null){

                    mItems.clear();

                    JSONArray c = json.getJSONArray("data_video");
                    for(int i = 0; i < c.length(); i++){
                        JSONObject data = c.getJSONObject(i);
                        mItems.add(new VideoModel(data.getString("id_video"), data.getString("youtube"), data.getString("judul_video")));
                    }

                    videoAdapter = new VideoAdapter(getActivity(), R.layout.page_video_item, mItems, getFragmentManager(), VideoPage.this);
                    listVideo.setAdapter(videoAdapter);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
