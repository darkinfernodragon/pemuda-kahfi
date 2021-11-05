package net.atommobile.pemudakahfi.transaksi.checkout.pembayaran;

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
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;

import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.databinding.PagePembayaranBinding;
import net.atommobile.pemudakahfi.parser.JSONParser;
import net.atommobile.pemudakahfi.transaksi.checkout.review.ReviewPage;

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
public class PembayaranPage extends Fragment {

    String api = "add_pembayaran/";

    PagePembayaranBinding binding;
    PembayaranModel model;

    private String id_transaksi;
    private String total_pembayaran;
    private String total_berat;
    private String ongkir;

    private String pembayaran = "Transfer Bank";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.page_pembayaran, container, false);
        View view = binding.getRoot();

        id_transaksi = this.getArguments().getString("id_transaksi");
        total_pembayaran = this.getArguments().getString("total_pembayaran");
        total_berat = this.getArguments().getString("total_berat");
        ongkir = this.getArguments().getString("ongkir");

        int total = Integer.parseInt(ongkir) + Integer.parseInt(total_pembayaran);

        model = new PembayaranModel(total_berat, ongkir, String.valueOf(total));
        binding.setModel(model);

        RadioButton radioPembayaran = (RadioButton) view.findViewById(binding.radioPembayaran.getCheckedRadioButtonId());
        pembayaran = radioPembayaran.getText().toString();

        binding.btnLanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONParse().execute();
            }
        });

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
            params.add(new BasicNameValuePair("id_transaksi", id_transaksi));
            params.add(new BasicNameValuePair("jenis_pembayaran", pembayaran));

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

                    if(data.getString("id_transaksi") != ""){
                        Bundle bundle = new Bundle();
                        bundle.putString("id_transaksi", id_transaksi);

                        Fragment fragment = new ReviewPage();
                        fragment.setArguments(bundle);

                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment).addToBackStack(null).commit();

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
