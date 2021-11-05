package net.atommobile.pemudakahfi.text;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.atommobile.pemudakahfi.MainActivity;
import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.parser.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 18/04/16.
 */
public class TextPage extends Fragment {

    WebView web_container;
    String idMenu;
    String api = "text/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.page_text,container,false);

        idMenu = this.getArguments().getString("idMenu");

        String awal = this.getArguments().getString("awal");
        if(awal.equals("")){
            LinearLayout layout_web = (LinearLayout) v.findViewById(R.id.layout_web);
            ((MainActivity)getActivity()).setBackground(v, layout_web, getResources());

            ImageView logo = (ImageView) v.findViewById(R.id.img_logo);
            logo.setVisibility(View.GONE);
        }

        web_container = (WebView) v.findViewById(R.id.web_container);
        String html = "<div style='padding: 16px'>Loading..</div>";
        web_container.loadData(html, "text/html", null);
        new JSONParse().execute();

        return v;
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            String url = api + idMenu;

            JSONParser jParser = new JSONParser(getActivity()); // get JSON data from URL
            JSONObject json = jParser.makeHttpRequest(url, "POST", params);

            return json;

        }

        @Override
        protected void onPostExecute(JSONObject json) {

            try {

                if(json != null){

                    JSONObject c = json.getJSONObject("data_text");

                    String html = "<div style='padding: 16px'>" + c.getString("isi_text") + "</div>";
                    web_container.loadData(html, "text/html", null);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
