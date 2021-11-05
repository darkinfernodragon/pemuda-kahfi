package net.atommobile.pemudakahfi.album;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.atommobile.pemudakahfi.MainActivity;
import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.komentar.KomentarAdapter;
import net.atommobile.pemudakahfi.komentar.KomentarModel;
import net.atommobile.pemudakahfi.parser.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 27/05/16.
 */
public class AlbumDetail extends Fragment {

    String api = "gallery_detail/";

    String api_send = "gallery_komentar_simpan";

    String id_album;
    String judul;
    String waktu;
    String gambar;
    String keterangan;

    TextView lbl_judul;
    TextView lbl_waktu;
    ImageView img_album;
    WebView web_album;

    String api_komentar = "gallery_komentar_array/";

    private ListView mKomenList;

    private ArrayList<KomentarModel> mItems;
    private KomentarAdapter adapter;

    Button btn_simpan;
    EditText txt_name;
    EditText txt_email;
    EditText txt_comment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.page_album_detail,container,false);

        LinearLayout layout_web = (LinearLayout) v.findViewById(R.id.layout_album_detail);
        ((MainActivity)getActivity()).setBackground(v, layout_web, getResources());

        ArrayList<AlbumModel> fileList =  (ArrayList<AlbumModel>)this.getArguments().getSerializable("gridArray");
        int posisiDefault = this.getArguments().getInt("gridPosition");
        AlbumModel item = fileList.get(posisiDefault);

        id_album = item.getId();
        judul = item.getJudul();
        waktu = item.getWaktu();
        gambar = item.getGambar();
        keterangan = item.getKeterangan();

        lbl_judul = (TextView) v.findViewById(R.id.lbl_judul);
        lbl_judul.setText(judul);

        lbl_waktu = (TextView) v.findViewById(R.id.lbl_waktu);
        lbl_waktu.setText(waktu);

        img_album = (ImageView) v.findViewById(R.id.img_album);
        ImageView image = img_album;
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(gambar, image);

        String html = keterangan;
        web_album = (WebView) v.findViewById(R.id.web_album);
        web_album.loadData(html, "text/html", null);

        mKomenList = (ListView) v.findViewById(R.id.list_komen);

        btn_simpan = (Button) v.findViewById(R.id.btn_send);
        txt_name = (EditText) v.findViewById(R.id.txt_name);
        txt_email = (EditText) v.findViewById(R.id.txt_email);
        txt_comment = (EditText) v.findViewById(R.id.txt_comment);

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONParse_send().execute();
            }
        });

        new JSONParse().execute();

        return v;
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("kodemem", kodemem));

            String url = api_komentar + id_album;

            JSONParser jParser = new JSONParser(getActivity()); // get JSON data from URL
            JSONObject json = jParser.makeHttpRequest(url, "POST", params);

            Log.e("json", json.toString());

            return json;

        }

        @Override
        protected void onPostExecute(JSONObject json) {

            try {

                if(json != null){

                    mItems = new ArrayList<KomentarModel>();

                    JSONArray c = json.getJSONArray("data_komentar");
                    for(int i = 0; i < c.length(); i++){

                        JSONObject data = c.getJSONObject(i);
                        mItems.add(new KomentarModel(data.getString("id_komen"), data.getString("nama"), data.getString("waktu"), data.getString("komentar"), data.getString("status")));

                    }

                    adapter = new KomentarAdapter(getActivity(), R.layout.page_komentar_item, mItems);
                    mKomenList.setAdapter(adapter);

                    ListAdapter listAdapter = mKomenList.getAdapter();
                    if (listAdapter != null) {

                        int totalHeight = 0;
                        int desiredWidth = View.MeasureSpec.makeMeasureSpec(mKomenList.getWidth(), View.MeasureSpec.AT_MOST);
                        for (int i = 0; i < listAdapter.getCount(); i++) {
                            View listItem = listAdapter.getView(i, null, mKomenList);
                            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                            totalHeight += listItem.getMeasuredHeight();
                        }

                        ViewGroup.LayoutParams params = mKomenList.getLayoutParams();
                        params.height = totalHeight + (mKomenList.getDividerHeight() * (listAdapter.getCount() - 1));

                        mKomenList.setLayoutParams(params);
                        mKomenList.requestLayout();

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private class JSONParse_send extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;
        String name;
        String email;
        String comment;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            name = txt_name.getText().toString();
            email = txt_email.getText().toString();
            comment = txt_comment.getText().toString();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Sending ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nama", name));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("komentar", comment));
            params.add(new BasicNameValuePair("idgallery", id_album));

            String url = api_send;

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

                    JSONObject data = json.getJSONObject("data_komentar");
                    String status = data.getString("berhasil");

                    if(status.equals("1")){
                        Toast.makeText(getActivity(), "Your comment has been sent", Toast.LENGTH_LONG).show();

                        txt_name.setText("");
                        txt_email.setText("");
                        txt_comment.setText("");

                    } else {
                        Toast.makeText(getActivity(), "Sorry, something went wrong, try again later", Toast.LENGTH_LONG).show();
                    }

                }

                new JSONParse().execute();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


}
