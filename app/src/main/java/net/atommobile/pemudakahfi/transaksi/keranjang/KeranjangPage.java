package net.atommobile.pemudakahfi.transaksi.keranjang;

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

import androidx.fragment.app.Fragment;

import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.databinding.PageKeranjangBinding;
import net.atommobile.pemudakahfi.db.DatabaseHandler;
import net.atommobile.pemudakahfi.member.MemberLoginPage;
import net.atommobile.pemudakahfi.parser.JSONParser;
import net.atommobile.pemudakahfi.produk.ProdukPage;
import net.atommobile.pemudakahfi.transaksi.checkout.pengiriman.PengirimanPage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 24/08/16.
 */
public class KeranjangPage extends Fragment {

    private String api = "add_transaksi/";

    PageKeranjangBinding binding;
    KeranjangModel model;

    DatabaseHandler db;

    private String id_customer;
    private String id_aplikasi;
    private String total_pembayaran;
    private String total_berat;

    private String id_transaksi;
    Fragment fragment;

    List<NameValuePair> params;

    KeranjangAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.page_keranjang, container, false);
        View rootView = binding.getRoot();

        db = new DatabaseHandler(getActivity());

        id_aplikasi = getString(R.string.app_id);

        showData();

        binding.btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(db.cekLogin() == false){

                    Fragment fragment = new MemberLoginPage();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container,
                            fragment, "memberlogin").addToBackStack(null).commit();

                } else {

                    id_customer = db.getMember().getId_member();

                    new JSONParse().execute();

                }

            }
        });

        return rootView;
    }

    private void showData(){

        int jumlahKeranjang = db.getKeranjangCount();

        if(jumlahKeranjang > 0) {

            model = new KeranjangModel();
            fragment = this;
            params = new ArrayList<NameValuePair>();

            int total = 0;
            int harga_tmp = 0;
            int qty_tmp = 0;
            int berat_tmp = 0;

            List<KeranjangItemModel> DataKeranjang = db.getAllKeranjang();

            for (KeranjangItemModel keranjang : DataKeranjang) {

                qty_tmp = Integer.parseInt(keranjang.getQty());
                harga_tmp = Integer.parseInt(keranjang.getHarga().replace(".", ""));

                model.AddList(
                        keranjang.getId_produk(),
                        keranjang.getId_attribut(),
                        keranjang.getId_ukuran(),
                        keranjang.getNama(),
                        keranjang.getUkuran(),
                        keranjang.getQty(),
                        keranjang.getStok(),
                        keranjang.getBerat(),
                        keranjang.getHarga().replace(".", ""),
                        keranjang.getGambar(),
                        keranjang.getId(),
                        fragment);

                total = total + (harga_tmp * qty_tmp);

                int berat_tmp_2 = (Integer.parseInt(keranjang.getBerat()) * qty_tmp);
                berat_tmp = berat_tmp + berat_tmp_2;

                params.add(new BasicNameValuePair("id_produk[]", keranjang.getId_produk()));
                params.add(new BasicNameValuePair("id_atribut[]", keranjang.getId_attribut()));
                params.add(new BasicNameValuePair("id_ukuran[]", keranjang.getId_ukuran()));
                params.add(new BasicNameValuePair("qty[]", keranjang.getQty()));
                params.add(new BasicNameValuePair("harga[]", keranjang.getHarga().replace(".", "")));
                params.add(new BasicNameValuePair("berat[]", keranjang.getBerat()));
                params.add(new BasicNameValuePair("subtotal[]", String.valueOf((harga_tmp * qty_tmp))));

            }

            total_pembayaran = String.valueOf(total);
            total_berat = String.valueOf(berat_tmp);

            adapter = new KeranjangAdapter(getActivity(), model.getList());
            binding.listBarang.setAdapter(adapter);

            model.setTotal(String.valueOf(total));

            binding.setModel(model);

        } else {

            Fragment fragment = new ProdukPage();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container,
                    fragment).commit();

        }

    }

    public void updateData(){
        showData();
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

            params.add(new BasicNameValuePair("id_customer", id_customer));
            params.add(new BasicNameValuePair("id_aplikasi", id_aplikasi));
            params.add(new BasicNameValuePair("total_pembayaran", total_pembayaran));

            String transaksi = db.getTransaksi();
            if(!transaksi.equals("")){
                params.add(new BasicNameValuePair("id_transaksi", transaksi));
            }

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

                        id_transaksi = data.getString("id_transaksi");

                        db.setTransaksi(id_transaksi);

                        Bundle bundle = new Bundle();
                        bundle.putString("id_transaksi", id_transaksi);
                        bundle.putString("total_pembayaran", total_pembayaran);
                        bundle.putString("total_berat", total_berat);

                        Fragment fragment = new PengirimanPage();
                        fragment.setArguments(bundle);

                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container,
                                fragment, "pengiriman").addToBackStack(null).commit();

                        getActivity().setTitle("Check Out");

                    } else {

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
