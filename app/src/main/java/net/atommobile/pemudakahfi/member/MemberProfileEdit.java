package net.atommobile.pemudakahfi.member;

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

import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.databinding.PageMemberEditBinding;
import net.atommobile.pemudakahfi.db.DatabaseHandler;
import net.atommobile.pemudakahfi.ext.DatePicker;
import net.atommobile.pemudakahfi.ext.GeneralModel;
import net.atommobile.pemudakahfi.parser.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by faizlidwibrido on 9/9/16.
 */
public class MemberProfileEdit extends Fragment {

    private String api = "edit_profile/";
    String api_provinsi = "list_provinsi/";
    String api_kota = "list_kota/";

    PageMemberEditBinding binding;
    MemberModel model;

    private String id_member;

    DatabaseHandler db;

    private String id_provinsi;
    private String provinsi;
    private String id_kota;
    private String kota;
    private String gender;

    private ArrayList<GeneralModel> list_provinsi = new ArrayList<>();
    private ArrayList<GeneralModel> list_kota = new ArrayList<>();
    private ArrayList<GeneralModel> list_jasa = new ArrayList<>();

    DatePicker datePicker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.page_member_edit, container, false);
        View view = binding.getRoot();

        datePicker = new DatePicker(getActivity(), binding.txtTgllahir);

        db = new DatabaseHandler(getActivity());
        model = db.getMember();

        id_member = model.getId_member();

        if (model.getTelepon().toString().equals("0")) model.setTelepon("");
        if (model.getAlamat().toString().equals("0")) model.setAlamat("");
        if (model.getProvinsi().toString().equals("null")) model.setProvinsi("");
        if (model.getKota().toString().equals("null")) model.setKota("");
        if (model.getKodepos().toString().equals("0")) model.setKodepos("");
        if (model.getGender().toString().equals("0")) model.setGender("");
        if (model.getTgllahir().toString().equals("0000-00-00")) model.setTgllahir("");

        binding.setModel(model);
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONParse().execute();
            }
        });

        new JSONParse_provinsi().execute();

        final ArrayList<String> jasa_cap = new ArrayList<String>();
        jasa_cap.add("Laki-laki");
        jasa_cap.add("Perempuan");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.
                simple_spinner_item, jasa_cap);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinGender.setAdapter(dataAdapter);

        binding.spinGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = jasa_cap.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        return view;
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        private String nama;
        private String telepon;
        private String alamat;
        private String kodepos;
        private String tgl_lahir;
        private String email;
        private String password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            nama = binding.txtNama.getText().toString();
            telepon = binding.txtTelepon.getText().toString();
            alamat = binding.txtAlamat.getText().toString();
            kodepos = binding.txtKodepos.getText().toString();
            tgl_lahir = binding.txtTgllahir.getText().toString();

            email = binding.txtEmail.getText().toString();
            password = binding.txtPassword.getText().toString();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_customer", id_member));
            params.add(new BasicNameValuePair("nama", nama));
            params.add(new BasicNameValuePair("no_telp", telepon));
            params.add(new BasicNameValuePair("alamat", alamat));
            params.add(new BasicNameValuePair("provinsi", id_provinsi));
            params.add(new BasicNameValuePair("kota", id_kota));
            params.add(new BasicNameValuePair("kode_pos", kodepos));
            params.add(new BasicNameValuePair("gender", gender));
            params.add(new BasicNameValuePair("tgl_lahir", tgl_lahir));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));

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

                if(json != null) {

                    String status = json.getString("status");
                    String pesan = json.getString("pesan");
                    //JSONArray c = json.getJSONArray("data");

                    if (status.equals("true")) {

                        MemberModel member = new MemberModel(
                                id_member,
                                email,
                                "",
                                nama,
                                telepon,
                                alamat,
                                id_provinsi,
                                provinsi,
                                id_kota,
                                kota,
                                kodepos,
                                gender,
                                tgl_lahir
                        );

                        db.updateMember(member);

                        Fragment fragment = new MemberProfilePage();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment).addToBackStack(null).commit();


                    } else {

                        Toast.makeText(getActivity(), pesan, Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(getActivity(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();

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
                        provinsi = data_def.getString("provinsi");

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
                                provinsi = list_provinsi.get(position).getParam2();
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
                        kota = data_def.getString("kota");
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
                                kota = list_kota.get(position).getParam2();
                                Log.e("KOTA", id_kota + " - " + kota);
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

}
