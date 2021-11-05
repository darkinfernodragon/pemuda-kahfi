package net.atommobile.pemudakahfi.transaksi.checkout.review;

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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import net.atommobile.pemudakahfi.MainActivity;
import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.databinding.PageReviewBinding;
import net.atommobile.pemudakahfi.db.DatabaseHandler;
import net.atommobile.pemudakahfi.parser.JSONParser;
import net.atommobile.pemudakahfi.produk.ProdukPage;
import net.atommobile.pemudakahfi.transaksi.checkout.selesai.SelesaiPage;

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
public class ReviewPage extends Fragment {

    String api = "save_transaksi/";
    String api_review = "preview_transaksi/";

    PageReviewBinding binding;
    ReviewModel model;

    private String id_transaksi;
    private String subtotal = "0";
    private String ongkir;
    private String total;
    private String alamat;
    private String alamat2;
    private String jasa;
    private String layanan;
    private String telepon;
    private String nama;
    private String pembayaran;

    DatabaseHandler db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.page_review, container, false);
        View view = binding.getRoot();

        id_transaksi = this.getArguments().getString("id_transaksi");

        model = new ReviewModel();
        db = new DatabaseHandler(getActivity());

        new JSONParse_review().execute();

        binding.btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONParse().execute();
            }
        });

        return view;

    }

    private class JSONParse_review extends AsyncTask<String, String, JSONObject> {

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

            String url = api_review;

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

                    if(status.equals("true")) {

                        JSONArray c = json.getJSONArray("data");
                        JSONObject data = c.getJSONObject(0);

                        JSONArray produks = data.getJSONArray("produk");
                        if(produks.length() > 0) {

                            nama = data.getString("nama_penerima");

                            alamat = data.getString("alamat_pengiriman");
                            alamat2 = data.getString("kota") + ", " + data.getString("provinsi") + ", " + data.getString("kode_pos");

                            jasa = data.getString("jasa_kirim");
                            layanan = data.getString("layanan");
                            telepon = data.getString("telepon");
                            pembayaran = data.getString("jenis_pembayaran");

                            int harga;
                            int qty;

                            //Log.e("PRODUKS", produks.toString());
                            for (int i = 0; i < produks.length(); i++) {

                                JSONObject prod = produks.getJSONObject(i);

                                qty = Integer.parseInt(prod.getString("qty"));
                                harga = Integer.parseInt(prod.getString("harga"));

                                String gambar_url = prod.getString("gambar");

                                model.AddList(
                                        prod.getString("id_produk"),
                                        prod.getString("id_atribut"),
                                        prod.getString("id_ukuran"),
                                        prod.getString("nama_produk"),
                                        prod.getString("ukuran"),
                                        prod.getString("qty"),
                                        "",
                                        prod.getString("berat"),
                                        prod.getString("harga"),
                                        gambar_url,
                                        prod.getString("id_detail"));

                                subtotal = String.valueOf(Integer.parseInt(subtotal) + (harga * qty));

                            }

                            ongkir = data.getString("ongkir");
                            total = String.valueOf(Integer.parseInt(subtotal) + Integer.parseInt(ongkir));

                            ReviewAdapter adapter = new ReviewAdapter(getActivity(), model.getList());
                            binding.listBarang.setAdapter(adapter);

                            ListAdapter listAdapter = binding.listBarang.getAdapter();
                            if (listAdapter != null) {

                                int totalHeight = 0;
                                int desiredWidth = View.MeasureSpec.makeMeasureSpec(binding.listBarang.getWidth(), View.MeasureSpec.AT_MOST);
                                for (int i = 0; i < listAdapter.getCount(); i++) {
                                    View listItem = listAdapter.getView(i, null, binding.listBarang);
                                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                                    totalHeight += listItem.getMeasuredHeight();
                                }

                                ViewGroup.LayoutParams params = binding.listBarang.getLayoutParams();
                                params.height = totalHeight + (binding.listBarang.getDividerHeight() * (listAdapter.getCount() - 1)) + 64;

                                binding.listBarang.setLayoutParams(params);
                                binding.listBarang.requestLayout();

                            }

                            model.setNama(nama);
                            model.setTotal(total);
                            model.setSubtotal(subtotal);
                            model.setOngkir(ongkir);
                            model.setAlamat(alamat);
                            model.setAlamat2(alamat2);
                            model.setJasa(jasa);
                            model.setLayanan(layanan);
                            model.setTelepon(telepon);
                            model.setPembayaran(pembayaran);

                            binding.setModel(model);

                        } else {

                            Toast.makeText(getActivity(), "Stok produk tidak cukup", Toast.LENGTH_SHORT).show();
                            Fragment fragment = new ProdukPage();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment).addToBackStack(null).commit();

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
                        JSONObject data = c.getJSONObject(0);

                        db.clearKeranjang();
                        ((MainActivity) getActivity()).setNotifCount();

                        Bundle bundle = new Bundle();
                        bundle.putString("id_transaksi", id_transaksi);

                        Fragment fragment = new SelesaiPage();
                        fragment.setArguments(bundle);

                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment).addToBackStack(null).commit();

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
