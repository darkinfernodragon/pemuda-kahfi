package net.atommobile.pemudakahfi.transaksi;

import android.app.ProgressDialog;
//import android.databinding.DataBindingUtil;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.Nullable;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.Toast;

import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.databinding.PageTransaksiDetailBinding;
import net.atommobile.pemudakahfi.parser.JSONParser;
import net.atommobile.pemudakahfi.transaksi.checkout.review.ReviewAdapter;
import net.atommobile.pemudakahfi.transaksi.checkout.review.ReviewModel;
import net.atommobile.pemudakahfi.transaksi.konfirmasi.KonfirmasiPage;

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
public class TransaksiDetail extends Fragment {

    String api = "detail_transaksi/";

    PageTransaksiDetailBinding binding;
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
    private String status_transaksi;

    private String nomor;
    private String tanggal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.page_transaksi_detail, container, false);
        View v = binding.getRoot();

        id_transaksi = this.getArguments().getString("id_transaksi");

        model = new ReviewModel();

        new JSONParse().execute();

        return v;
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

                if (json != null) {

                    String status = json.getString("status");
                    String pesan = json.getString("pesan");

                    if(status.equals("true")){

                        JSONArray c = json.getJSONArray("data");
                        JSONObject data = c.getJSONObject(0);

                        nama = data.getString("nama_penerima");

                        nomor = data.getString("no_transaksi");
                        tanggal = data.getString("tanggal_transaksi");

                        alamat = data.getString("alamat_pengiriman");
                        alamat2 = data.getString("kota") + ", " + data.getString("provinsi") + ", " + data.getString("kode_pos");
                        jasa = data.getString("jasa_kirim");
                        layanan = data.getString("layanan");
                        telepon = data.getString("telepon");
                        pembayaran = data.getString("jenis_pembayaran");
                        status_transaksi = data.getString("status_pembayaran");

                        int harga;
                        int qty;

                        JSONArray data_produk = data.getJSONArray("produk");
                        for (int i = 0; i < data_produk.length(); i++) {
                            JSONObject produk = data_produk.getJSONObject(i);

                            qty = Integer.parseInt(produk.getString("qty"));
                            harga = Integer.parseInt(produk.getString("harga"));

                            String gambar_url = produk.getString("gambar");

                            model.AddList(
                                    produk.getString("id_produk"),
                                    produk.getString("id_atribut"),
                                    produk.getString("id_ukuran"),
                                    produk.getString("nama_produk"),
                                    produk.getString("ukuran"),
                                    produk.getString("qty"),
                                    "",
                                    produk.getString("berat"),
                                    produk.getString("harga"),
                                    gambar_url,
                                    produk.getString("id_detail"));

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
                        model.setNomor(nomor);
                        model.setTanggal(tanggal);

                        model.setTotal(total);
                        model.setSubtotal(subtotal);
                        model.setOngkir(ongkir);
                        model.setAlamat(alamat);
                        model.setAlamat2(alamat2);
                        model.setJasa(jasa);
                        model.setLayanan(layanan);
                        model.setTelepon(telepon);
                        model.setPembayaran(pembayaran);
                        model.setStatus(status_transaksi);

                        if(status_transaksi.equals("Belum dibayar")){
                            binding.btnKonfirmasi.setVisibility(View.VISIBLE);

                            binding.btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id_transaksi", id_transaksi);
                                    bundle.putString("no_transaksi", nomor);
                                    bundle.putString("tanggal_transaksi", tanggal);
                                    bundle.putString("total_pembayaran", total);
                                    bundle.putString("jenis_pembayaran", pembayaran);

                                    Fragment fragment = new KonfirmasiPage();
                                    fragment.setArguments(bundle);

                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment).addToBackStack(null).commit();

                                    getActivity().setTitle("Konfirmasi Pembayaran");
                                }
                            });
                        }

                        binding.setModel(model);

                    } else {

                        Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(getActivity(), "Cannot connect to server", Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
