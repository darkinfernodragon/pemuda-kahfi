package net.atommobile.pemudakahfi.transaksi;

import android.app.ProgressDialog;
//import android.databinding.DataBindingUtil;
import androidx.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import androidx.annotation.Nullable;
import  androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.fragment.app.Fragment;

import net.atommobile.pemudakahfi.MainActivity;
import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.databinding.PageTransaksiHistoryBinding;
import net.atommobile.pemudakahfi.db.DatabaseHandler;
import net.atommobile.pemudakahfi.member.MemberLoginPage;
import net.atommobile.pemudakahfi.member.MemberModel;
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
public class TransaksiHistory extends Fragment {

    String api = "history_transaksi";

    PageTransaksiHistoryBinding binding;
    TransaksiHistoryModel model;

    String id_customer;

    DatabaseHandler db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.page_transaksi_history, container, false);
        View view = binding.getRoot();

        ((MainActivity)getActivity()).setBackground(view, binding.layoutHistory, getResources());

        model = new TransaksiHistoryModel();

        db = new DatabaseHandler(getActivity());
        Boolean login = db.cekLogin();
        if(login == true){

            MemberModel member = db.getMember();
            id_customer = member.getId_member();

            new JSONParse().execute();

        } else {

            getActivity().getSupportFragmentManager().popBackStack();
            Fragment fragment = new MemberLoginPage();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment).addToBackStack(null).commit();
            getActivity().setTitle("Member");

        }

        return view;

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
            params.add(new BasicNameValuePair("id_customer", id_customer));

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

                    String status = json.getString("status");
                    String pesan = json.getString("pesan");

                    if(status.equals("true")){

                        JSONArray c = json.getJSONArray("data");
                        for(int i = 0; i < c.length(); i++){

                            JSONObject data = c.getJSONObject(i);
                            model.AddHistory(data.getString("id_transaksi"), data.getString("no_transaksi"),
                                    data.getString("tanggal_transaksi"), data.getString("qty"), data.getString("total_pembayaran"),
                                    data.getString("jenis_pembayaran"), data.getString("status_pembayaran"));

                        }

                        TransaksiHistoryAdapter adapter = new TransaksiHistoryAdapter(getActivity(), model.getList());
                        binding.listHistory.setAdapter(adapter);

                        binding.setModel(model);

                        binding.listHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String id_transaksi = model.getList().get(i).getId();
                                Bundle bundle = new Bundle();
                                bundle.putString("id_transaksi", id_transaksi);

                                Fragment fragment = new TransaksiDetail();
                                fragment.setArguments(bundle);

                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment).addToBackStack(null).commit();

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
