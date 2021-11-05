package net.atommobile.pemudakahfi.transaksi.checkout.pengiriman;

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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.databinding.PagePengirimanBinding;
import net.atommobile.pemudakahfi.db.DatabaseHandler;
import net.atommobile.pemudakahfi.ext.GeneralModel;
import net.atommobile.pemudakahfi.member.MemberModel;
import net.atommobile.pemudakahfi.parser.JSONParser;
import net.atommobile.pemudakahfi.transaksi.checkout.pembayaran.PembayaranPage;

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
public class PengirimanPage extends Fragment {

    String api = "add_data_pengiriman/";
    String api_provinsi = "list_provinsi/";
    String api_kota = "list_kota/";
    String api_jasa = "list_kurir/";
    String api_layanan = "cek_ongkir/";

    PagePengirimanBinding binding;
    PengirimanModel model;

    private String id_transaksi;
    private String id_customer;
    private String nama;
    private String telepon;
    private String alamat;
    private String jasa;
    private String layanan = "";
    private String ongkir;
    private String id_provinsi;
    private String id_kota;
    private String kode_pos;
    private String total;

    private String total_pembayaran;
    private String total_berat;

    private String id_aplikasi;

    DatabaseHandler db;

    private ArrayList<GeneralModel> list_provinsi = new ArrayList<>();
    private ArrayList<GeneralModel> list_kota = new ArrayList<>();
    private ArrayList<GeneralModel> list_jasa = new ArrayList<>();
    private ArrayList<GeneralModel> list_layanan = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.page_pengiriman, container, false);
        View view = binding.getRoot();

        id_transaksi = this.getArguments().getString("id_transaksi");
        total_pembayaran = this.getArguments().getString("total_pembayaran");
        total_berat = this.getArguments().getString("total_berat");

        id_aplikasi = getString(R.string.app_id);

        db = new DatabaseHandler(getActivity());
        MemberModel member = db.getMember();

        id_customer = member.getId_member();

        model = new PengirimanModel();
        model.setDataMember(member.getNama(), member.getTelepon(), member.getAlamat(), member.getProvinsi(), member.getKota(),
                member.getKodepos(), "", "", total_berat, "0");

        model.setTotal(total_pembayaran);
        binding.setModel(model);

        new JSONParse_provinsi().execute();

        binding.btnLanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(layanan.equals("")){
                    Toast.makeText(getActivity(), "Lengkapi form dengan benar", Toast.LENGTH_SHORT).show();
                } else {
                    new JSONParse().execute();
                }
            }
        });

        return view;
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            nama = binding.txtNama.getText().toString();
            telepon = binding.txtTelepon.getText().toString();
            alamat = binding.txtAlamat.getText().toString();
            kode_pos = binding.txtKodepos.getText().toString();

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
            params.add(new BasicNameValuePair("id_customer", id_customer));
            params.add(new BasicNameValuePair("nama_penerima", nama));
            params.add(new BasicNameValuePair("telepon", telepon));
            params.add(new BasicNameValuePair("alamat_pengiriman", alamat));
            params.add(new BasicNameValuePair("jasa_kirim", jasa));
            params.add(new BasicNameValuePair("layanan", layanan));
            params.add(new BasicNameValuePair("ongkir", ongkir));
            params.add(new BasicNameValuePair("id_provinsi", id_provinsi));
            params.add(new BasicNameValuePair("id_kota", id_kota));
            params.add(new BasicNameValuePair("kode_pos", kode_pos));
            params.add(new BasicNameValuePair("total_pembayaran", total));

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

                    JSONArray c = json.getJSONArray("data");
                    JSONObject data = c.getJSONObject(0);

                    Bundle bundle = new Bundle();
                    bundle.putString("id_transaksi", id_transaksi);
                    bundle.putString("total_berat", total_berat);
                    bundle.putString("ongkir", ongkir);
                    bundle.putString("total_pembayaran", total_pembayaran);

                    Fragment fragment = new PembayaranPage();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment).addToBackStack(null).commit();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private class JSONParse_provinsi extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            String url = api_provinsi;

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

                        list_provinsi.clear();
                        ArrayList<String> provinsi_cap = new ArrayList<String>();

                        JSONArray c = json.getJSONArray("data");

                        //KOTA DEFAULT
                        JSONObject data_def = c.getJSONObject(0);
                        id_provinsi = data_def.getString("id");

                        for(int p = 0; p < c.length(); p++){
                            JSONObject data = c.getJSONObject(p);
                            list_provinsi.add(new GeneralModel(data.getString("id"), data.getString("provinsi")));
                            provinsi_cap.add(data.getString("provinsi"));
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.
                                simple_spinner_item, provinsi_cap);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.spinProvinsi.setAdapter(dataAdapter);

                        binding.spinProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                id_provinsi = list_provinsi.get(position).getParam1();
                                String provinsi = list_provinsi.get(position).getParam2();
                                Log.e("PROVINSI", id_provinsi + " - " + provinsi);

                                new JSONParse_kota().execute();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }

                        });

                    } else {

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private class JSONParse_kota extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_provinsi", id_provinsi));

            String url = api_kota;

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

                        list_kota.clear();
                        ArrayList<String> kota_cap = new ArrayList<String>();

                        JSONArray c = json.getJSONArray("data");

                        //DEFAULT KOTA
                        JSONObject data_def = c.getJSONObject(0);
                        id_kota = data_def.getString("id");
                        //new JSONParse_jasa().execute();

                        for(int p = 0; p < c.length(); p++){
                            JSONObject data = c.getJSONObject(p);
                            list_kota.add(new GeneralModel(data.getString("id"), data.getString("kota")));
                            kota_cap.add(data.getString("kota"));
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.
                                simple_spinner_item, kota_cap);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.spinKota.setAdapter(dataAdapter);

                        binding.spinKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                id_kota = list_kota.get(position).getParam1();
                                String kota = list_kota.get(position).getParam2();
                                Log.e("KOTA", id_kota + " - " + kota);

                                new JSONParse_jasa().execute();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }

                        });

                    } else {

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private class JSONParse_jasa extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_aplikasi", id_aplikasi));

            String url = api_jasa;

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

                        list_jasa.clear();
                        ArrayList<String> jasa_cap = new ArrayList<String>();

                        JSONObject c = json.getJSONObject("data");
                        JSONArray d = c.getJSONArray("list_kurir");

                        //DEFAULT JASA
                        jasa = d.getString(0);
                        ///new JSONParse_layanan().execute();

                        for(int p = 0; p < d.length(); p++){
                            list_jasa.add(new GeneralModel(String.valueOf(p), d.getString(p)));
                            jasa_cap.add(d.getString(p));
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.
                                simple_spinner_item, jasa_cap);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.spinJasa.setAdapter(dataAdapter);

                        binding.spinJasa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                jasa = list_jasa.get(position).getParam2();
                                new JSONParse_layanan().execute();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }

                        });

                    } else {

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private class JSONParse_layanan extends AsyncTask<String, String, JSONObject> {

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
            params.add(new BasicNameValuePair("id_kota_tujuan", id_kota));
            params.add(new BasicNameValuePair("berat", total_berat));
            params.add(new BasicNameValuePair("kurir", jasa));

            String url = api_layanan;

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

                        list_layanan.clear();
                        ArrayList<String> layanan_cap = new ArrayList<String>();

                        JSONObject c = json.getJSONObject("data");
                        JSONArray d = c.getJSONArray("costs");

                        if(d.length() > 0) {
                            //DEFAULT LAYANAN
                            JSONObject costs_def = d.getJSONObject(0);
                            layanan = costs_def.getString("service");
                            ongkir = costs_def.getString("cost");
                            total = String.valueOf(Integer.parseInt(total_pembayaran) + Integer.parseInt(ongkir));

                            for (int p = 0; p < d.length(); p++) {
                                JSONObject costs = d.getJSONObject(p);
                                list_layanan.add(new GeneralModel(costs.getString("service"), costs.getString("cost")));
                                layanan_cap.add(costs.getString("service"));
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.
                                    simple_spinner_item, layanan_cap);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.spinLayanan.setAdapter(dataAdapter);

                            binding.spinLayanan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    layanan = list_layanan.get(position).getParam1();
                                    ongkir = list_layanan.get(position).getParam2();
                                    Log.e("LAYANAN", layanan + " - " + ongkir);

                                    model.setOngkir(ongkir);
                                    total = String.valueOf(Integer.parseInt(total_pembayaran) + Integer.parseInt(ongkir));
                                    model.setTotal(total);
                                    binding.setModel(model);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }

                            });

                        } else {

                            Toast.makeText(getActivity(), "Layanan pengiriman tidak tersedia di kota Anda", Toast.LENGTH_SHORT).show();

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.
                                    simple_spinner_item, layanan_cap);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.spinLayanan.setAdapter(dataAdapter);

                            binding.txtOngkir.setText("");
                            binding.txtTotal.setText(total_pembayaran);

                            layanan = "";
                            ongkir = "";

                        }

                    } else {

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
