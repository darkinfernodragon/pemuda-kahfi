package net.atommobile.pemudakahfi.transaksi.checkout.selesai;

import android.app.ProgressDialog;
//import android.databinding.DataBindingUtil;
import androidx.databinding.DataBindingUtil;
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
import android.widget.ListAdapter;

import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.databinding.PageSelesaiBinding;
import net.atommobile.pemudakahfi.parser.JSONParser;
import net.atommobile.pemudakahfi.transaksi.konfirmasi.KonfirmasiPage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by faizlidwibrido on 9/8/16.
 */
public class SelesaiPage extends Fragment {

    String api = "transaksi_res/";
    String api_bank = "list_bank/";

    String id_aplikasi;
    String id_transaksi;
    String no_transaksi;
    String tanggal_transaksi;
    String total_pembayaran;
    String jenis_pembayaran;

    PageSelesaiBinding binding;
    SelesaiModel model;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.page_selesai, container, false);
        View view = binding.getRoot();

        id_transaksi = this.getArguments().getString("id_transaksi");
        id_aplikasi = getString(R.string.app_id);

        model = new SelesaiModel();

        new JSONParse().execute();
        new JSONParse_bank().execute();

        binding.btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("id_transaksi", id_transaksi);
                bundle.putString("no_transaksi", no_transaksi);
                bundle.putString("tanggal_transaksi", tanggal_transaksi);
                bundle.putString("total_pembayaran", total_pembayaran);
                bundle.putString("jenis_pembayaran", jenis_pembayaran);

                Fragment fragment = new KonfirmasiPage();
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment).addToBackStack(null).commit();

                getActivity().setTitle("Konfirmasi Pembayaran");

            }
        });

        return view;

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
            params.add(new BasicNameValuePair("id_transaksi", id_transaksi));

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

                    String status = json.getString("status");
                    String pesan = json.getString("pesan");

                    if(status.equals("true")){
                        JSONArray c = json.getJSONArray("data");
                        JSONObject data = c.getJSONObject(0);

                        no_transaksi = data.getString("no_transaksi");
                        tanggal_transaksi = data.getString("tanggal_transaksi");
                        total_pembayaran = data.getString("total_pembayaran");
                        jenis_pembayaran = data.getString("jenis_pembayaran");

                        model.setNoorder(no_transaksi);
                        model.setTanggal(tanggal_transaksi);
                        model.setTotal(total_pembayaran);
                        model.setPembayaran(jenis_pembayaran);

                        binding.setModel(model);


                    } else {

                    }

                } else {

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private class JSONParse_bank extends AsyncTask<String, String, JSONObject> {

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

            String url = api_bank;

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

                    String status = json.getString("status");
                    String pesan = json.getString("pesan");

                    if(status.equals("true")){
                        JSONArray c = json.getJSONArray("data");
                        for(int i = 0; i < c.length(); i++){
                            JSONObject data = c.getJSONObject(i);
                            model.AddBank(data.getString("id_bank"), data.getString("nama_bank"), data.getString("no_rekening"), data.getString("atas_nama"));
                        }

                        SelesaiBankAdapter adapter = new SelesaiBankAdapter(getActivity(), model.getList_bank());
                        binding.listBank.setAdapter(adapter);

                        ListAdapter listAdapter = binding.listBank.getAdapter();
                        if (listAdapter != null) {

                            int totalHeight = 0;
                            int desiredWidth = View.MeasureSpec.makeMeasureSpec(binding.listBank.getWidth(), View.MeasureSpec.AT_MOST);
                            for (int i = 0; i < listAdapter.getCount(); i++) {
                                View listItem = listAdapter.getView(i, null, binding.listBank);
                                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                                totalHeight += listItem.getMeasuredHeight();
                            }

                            ViewGroup.LayoutParams params = binding.listBank.getLayoutParams();
                            params.height = totalHeight + (binding.listBank.getDividerHeight() * (listAdapter.getCount() - 1));

                            binding.listBank.setLayoutParams(params);
                            binding.listBank.requestLayout();

                        }


                    } else {

                    }

                } else {

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
