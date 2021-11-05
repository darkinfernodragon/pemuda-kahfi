package net.atommobile.pemudakahfi.transaksi.konfirmasi;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.databinding.PageKonfirmasiBinding;
import net.atommobile.pemudakahfi.ext.GeneralModel;
import net.atommobile.pemudakahfi.parser.JSONParser;
import net.atommobile.pemudakahfi.transaksi.TransaksiHistory;
import net.atommobile.pemudakahfi.transaksi.TransaksiModel;

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
public class KonfirmasiPage extends Fragment {

    String api = "konfirmasi_pembayaran/";
    String api_bank = "list_bank/";

    PageKonfirmasiBinding binding;
    TransaksiModel model;

    String id_transaksi;
    String no_transaksi;
    String tanggal_transaksi;
    String total_pembayaran;
    String jenis_pembayaran;
    String id_bank_tujuan;

    String id_aplikasi;

    private ArrayList<GeneralModel> list_bank = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.page_konfirmasi, container, false);
        View view = binding.getRoot();

        model = new TransaksiModel();
        id_transaksi = this.getArguments().getString("id_transaksi");
        no_transaksi = this.getArguments().getString("no_transaksi");
        tanggal_transaksi = this.getArguments().getString("tanggal_transaksi");
        total_pembayaran = this.getArguments().getString("total_pembayaran");
        jenis_pembayaran = this.getArguments().getString("jenis_pembayaran");

        id_aplikasi = getString(R.string.app_id);

        model.setTotal(total_pembayaran);
        model.setId(id_transaksi);
        model.setNomor(no_transaksi);
        model.setTanggal(tanggal_transaksi);
        model.setPembayaran(jenis_pembayaran);

        binding.setModel(model);

        new JSONParse_bank().execute();

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONParse().execute();
            }
        });

        return view;
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        private String pembayaran;
        private String bank_asal;
        private String atasnama;
        private String nominal;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pembayaran = binding.txtPembayaran.getText().toString();
            bank_asal = binding.txtBankAsal.getText().toString();
            atasnama = binding.txtAtasnama.getText().toString();
            nominal = binding.txtNominal.getText().toString();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_transaksi", id_transaksi));
            params.add(new BasicNameValuePair("atas_nama", atasnama));
            params.add(new BasicNameValuePair("nama_bank", bank_asal));
            params.add(new BasicNameValuePair("rekening_tujuan", id_bank_tujuan));
            params.add(new BasicNameValuePair("jumlah_bayar", nominal));
            params.add(new BasicNameValuePair("pembayaran", pembayaran));

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

                if (json != null) {

                    String status = json.getString("status");
                    String pesan = json.getString("pesan");

                    if (status.equals("true")) {

                        Fragment fragment = new TransaksiHistory();

                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container,
                                fragment).addToBackStack(null).commit();

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

                        ArrayList<String> bank_cap = new ArrayList<String>();

                        JSONArray c = json.getJSONArray("data");

                        //KOTA DEFAULT
                        JSONObject data_def = c.getJSONObject(0);
                        id_bank_tujuan = data_def.getString("id_bank");

                        for(int p = 0; p < c.length(); p++){
                            JSONObject data = c.getJSONObject(p);
                            list_bank.add(new GeneralModel(data.getString("id_bank"), data.getString("nama_bank")));
                            String caption = data.getString("nama_bank") + " " + data.getString("no_rekening") + " " + data.getString("atas_nama");
                            bank_cap.add(caption);
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.
                                simple_spinner_item, bank_cap);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.spinBankTujuan.setAdapter(dataAdapter);

                        binding.spinBankTujuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                id_bank_tujuan = list_bank.get(position).getParam1();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }

                        });


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
