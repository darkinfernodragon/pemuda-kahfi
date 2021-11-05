package net.atommobile.pemudakahfi.produk;

import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.DialogFragment;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.parser.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by faizlidwibrido on 9/15/16.
 */
public class ProdukKategori extends DialogFragment {

    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> id_items = new ArrayList<>();

    ListView listView;

    String id_aplikasi;
    String api = "list_kategori_produk";

    //Interface created for communicating this dialog fragment events to called fragment
    public interface KategoriDialog{
        void setKategori(String id, String nama);
    }

    KategoriDialog kategori;

    public void setKategoriDialog(KategoriDialog kategori){
        this.kategori = kategori;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_produk_kategori, container);

        listView = (ListView) view.findViewById(R.id.list_kategori);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        id_aplikasi = getString(R.string.app_id);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        new JSONParse().execute();

    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_aplikasi", id_aplikasi));

            String url = api;

            JSONParser jParser = new JSONParser(getActivity()); // get JSON data from URL
            JSONObject json = jParser.makeHttpRequest(url, "POST", params);

            Log.e("json", json.toString());

            return json;

        }

        @Override
        protected void onPostExecute(JSONObject json) {

            try {

                if(json != null){

                    JSONArray c = json.getJSONArray("data");
                    items.add("Semua Kategori");
                    id_items.add("");
                    for(int i = 0; i < c.length(); i++){
                        JSONObject data = c.getJSONObject(i);
                        items.add(data.getString("nama_kategori_produk"));
                        id_items.add(data.getString("id_kategori_produk"));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1, items);

                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            dismiss();

                            kategori.setKategori(id_items.get(i), items.get(i));

                        }
                    });

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
