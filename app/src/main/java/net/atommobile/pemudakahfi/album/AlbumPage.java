package net.atommobile.pemudakahfi.album;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.annotation.Nullable;
import androidx.annotation.Nullable;
//import android.support.v4.app.Fragment;
import androidx.fragment.app.Fragment;
//import android.support.v4.app.FragmentManager;
import androidx.fragment.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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
 * Created by root on 01/06/16.
 */
public class AlbumPage extends Fragment {

    String api = "gallery_array/";
    String api_kategori = "kategori_array/";
    String idMenu;

    GridView gridAlbum;
    ArrayList<AlbumModel> gridArray;
    AlbumAdapter albumAdapter;

    private ArrayList<String> KategoriList;
    private ArrayList<KategoriModel> mItemsKategori;
    String id_kategori_selected = "";
    Spinner spin_kategori;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.page_album,container,false);

        idMenu = this.getArguments().getString("idMenu");

        String awal = this.getArguments().getString("awal");
        if(awal.equals("")){
            LinearLayout layout_menu = (LinearLayout) v.findViewById(R.id.layout_album);
            ((MainActivity)getActivity()).setBackground(v, layout_menu, getResources());

            ImageView logo = (ImageView) v.findViewById(R.id.img_logo);
            logo.setVisibility(View.GONE);
        }

        gridArray = new ArrayList<AlbumModel>();

        gridAlbum = (GridView) v.findViewById(R.id.grid_album);
        gridAlbum.setFocusable(false);

        spin_kategori = (Spinner) v.findViewById(R.id.spin_kategori);
        mItemsKategori = new ArrayList<KategoriModel>();

        new JSONParse_kategori().execute();

        return v;
    }

    private class JSONParse_kategori extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            String url = api_kategori + idMenu;
            Log.e("URL", url);

            JSONParser jParser = new JSONParser(getActivity()); // get JSON data from URL
            JSONObject json = jParser.makeHttpRequest(url, "POST", params);

            Log.e("json", json.toString());

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {

            try {

                if(json != null){

                    KategoriList = new ArrayList<String>();
                    KategoriList.add("Semua Kategori");
                    mItemsKategori.add(new KategoriModel("", ""));

                    JSONArray c = json.getJSONArray("data_kategori");
                    for(int i = 0; i < c.length(); i++){

                        JSONObject data = c.getJSONObject(i);
                        mItemsKategori.add(new KategoriModel(data.getString("id_kategori"), data.getString("nm_kategori")));
                        KategoriList.add(data.getString("nm_kategori"));

                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.
                            simple_spinner_item, KategoriList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // attaching data adapter to spinner
                    spin_kategori.setAdapter(dataAdapter);

                    spin_kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // On selecting a spinner item
                            id_kategori_selected = mItemsKategori.get(position).getId();
                            new JSONParse().execute();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }

                    });

                    new JSONParse().execute();

                } else {
                    Toast.makeText(getActivity(), "Kategori bermasalah", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
//            params.add(new BasicNameValuePair("kodemem", kodemem));

            String url = "";

            if(id_kategori_selected == ""){
                url = api + idMenu;
            } else {
                url = api + idMenu + "/" + id_kategori_selected;
            }

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

                    gridArray.clear();

                    JSONArray c = json.getJSONArray("data_gallery");
                    for(int i = 0; i < c.length(); i++){
                        JSONObject data = c.getJSONObject(i);
                        gridArray.add(new AlbumModel(data.getString("id_gallery"), data.getString("foto_gallery"), data.getString("tgl_input"), data.getString("judul_gallery"), data.getString("keterangan")));
                    }

                    albumAdapter = new AlbumAdapter(getActivity(), R.layout.page_album_item, gridArray);
                    gridAlbum.setAdapter(albumAdapter);
                    gridAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Bundle bundle = new Bundle();

                            bundle.putSerializable("gridArray", gridArray);
                            bundle.putInt("gridPosition", position);

                            Fragment fragment = new AlbumViewPager();

                            fragment.setArguments(bundle);

                            if(fragment != null){
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                fragmentTransaction.hide(AlbumPage.this);

                                fragmentTransaction.add(R.id.layout_container, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        }
                    });

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
