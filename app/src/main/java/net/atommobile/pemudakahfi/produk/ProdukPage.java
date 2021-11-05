package net.atommobile.pemudakahfi.produk;

import android.app.ProgressDialog;
//import android.databinding.DataBindingUtil;
import androidx.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import net.atommobile.pemudakahfi.MainActivity;
import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.databinding.PageProdukBinding;
import net.atommobile.pemudakahfi.parser.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 31/08/16.
 */
public class ProdukPage extends Fragment implements ProdukKategori.KategoriDialog, ProdukSort.SortDialog {

    private PageProdukBinding binding;
    private ProdukModel model;

    String api = "list_produk/";
    String api_banner = "list_banner/";
    //String id_menu;
    String id_aplikasi;

    String cari = "";
    String id_kategori = "";
    String nama_kategori = "";
    String sort = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.page_produk, container, false);
        View rootView = binding.getRoot();

        ((MainActivity)getActivity()).setBackground(rootView, binding.layoutProduk, getResources());

        //id_menu = this.getArguments().getString("idMenu");
        id_aplikasi = ((MainActivity) getActivity()).app_id;

        model = new ProdukModel();

        new JSONParse().execute();
        new JSONParse_banner().execute();

        binding.btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cari = binding.txtCari.getText().toString();
                new JSONParse().execute();
            }
        });

        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager manager = getActivity().getSupportFragmentManager();
                ProdukKategori dialog = new ProdukKategori();
                dialog.setKategoriDialog(ProdukPage.this);
                dialog.show(manager, "dialog_kategori");

            }
        });

        binding.sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager manager = getActivity().getSupportFragmentManager();
                ProdukSort dialog = new ProdukSort();
                dialog.setSortDialog(ProdukPage.this);
                dialog.show(manager, "dialog_sort");

            }
        });

        return rootView;
    }

    private class JSONParse_banner extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_aplikasi", id_aplikasi));

            String url = api_banner;

            JSONParser jParser = new JSONParser(getActivity()); // get JSON data from URL
            JSONObject json = jParser.makeHttpRequest(url, "POST", params);

            Log.e("json", json.toString());

            return json;

        }

        @Override
        protected void onPostExecute(JSONObject json) {

            try {

                if(json != null){

                    String status = json.getString("status");
                    String pesan = json.getString("pesan");

                    if(status.equals("true")) {

                        binding.layoutViewpager.setVisibility(View.VISIBLE);

                        model.getImages().clear();

                        JSONArray gambar_data = json.getJSONArray("data");
                        model.setIndicator(binding.indicator);

                        for (int j = 0; j < gambar_data.length(); j++) {
                            JSONObject gmb = gambar_data.getJSONObject(j);
                            String url_image = gmb.getString("banner");
                            model.AddSlide(gmb.getString("id_banner"), url_image);
                        }

                        ProdukSliderAdapter slideImage = new ProdukSliderAdapter(getActivity(), model.getImages(), null);
                        binding.slide.setAdapter(slideImage);
                        binding.indicator.setViewPager(binding.slide);

                    } else {

                    }

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
            params.add(new BasicNameValuePair("id_aplikasi", id_aplikasi));
            params.add(new BasicNameValuePair("cari", cari));
            params.add(new BasicNameValuePair("id_kategori", id_kategori));
            params.add(new BasicNameValuePair("sort", sort));

            String url = api;

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

                    model.getList().clear();

                    JSONArray c = json.getJSONArray("data");
                    for(int i = 0; i < c.length(); i++){
                        JSONObject data = c.getJSONObject(i);
                        String url_image = data.getString("gambar");
                        //Log.e("gambar", url_image);
                        String nama_produk = data.getString("nama_produk") + " " + data.getString("value");
                        model.AddData(data.getString("id_atribut"), nama_produk, data.getString("harga"), url_image);
                    }

                    ProdukAdapter adapter = new ProdukAdapter(getActivity(), model.getList());
                    binding.listItem.setAdapter(adapter);

                    //int n = a / b + (a % b == 0) ? 0 : 1;

                    binding.listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String id_attribut = model.getList().get(i).getId();
                            Bundle bundle = new Bundle();
                            bundle.putString("id_attribut", id_attribut);

                            Fragment fragment = new ProdukDetail();
                            fragment.setArguments(bundle);

                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment).addToBackStack(null).commit();

                        }
                    });

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void setKategori(String id, String nama) {
        id_kategori = id;
        nama_kategori = nama;
        binding.txtKategori.setText(nama_kategori);

        Log.e("ID_FRAGMENT", id_kategori);
        Log.e("NAMA_FRAGMENT", nama_kategori);

        new JSONParse().execute();
    }

    @Override
    public void setSort(String nama) {
        sort = nama;
        binding.txtUrutan.setText(sort);

        new JSONParse().execute();
    }

}
