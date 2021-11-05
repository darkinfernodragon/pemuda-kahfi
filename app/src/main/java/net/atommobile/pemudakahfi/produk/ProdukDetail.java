package net.atommobile.pemudakahfi.produk;

import android.app.ProgressDialog;
//import android.databinding.DataBindingUtil;
import androidx.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import net.atommobile.pemudakahfi.MainActivity;
import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.databinding.PageProdukDetailBinding;
import net.atommobile.pemudakahfi.db.DatabaseHandler;
import net.atommobile.pemudakahfi.parser.JSONParser;
import net.atommobile.pemudakahfi.transaksi.keranjang.KeranjangItemModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by faizlidwibrido on 9/1/16.
 */
public class ProdukDetail extends Fragment {

    private PageProdukDetailBinding binding;
    private ProdukItemModel model;

    private String id_produk;
    private String id_attribut;
    private String id_ukuran;
    private String ukuran;
    private String nama;
    private String qty;
    private String stok;
    private String berat;
    private String harga;
    private String gambar;

    private String id_aplikasi;
    private String api = "detail_produk";

    DatabaseHandler db;

    ArrayList<ProdukUkuranModel> ukuran_model;
    ArrayList<String> ukuran_list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.page_produk_detail, container, false);
        View rootView = binding.getRoot();

        model = new ProdukItemModel();

        model.setIndicator(binding.indicator);
        binding.setModel(model);

        id_attribut = this.getArguments().getString("id_attribut");
        id_aplikasi = getString(R.string.app_id);
        new JSONParse().execute();

        db = new DatabaseHandler(getActivity());

        return rootView;
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
            params.add(new BasicNameValuePair("id_atribut", id_attribut));
            params.add(new BasicNameValuePair("id_aplikasi", id_aplikasi));

            String url = api;

            JSONParser jParser = new JSONParser(getActivity()); // get JSON data from URL
            JSONObject json = jParser.makeHttpRequest(url, "POST", params);

            Log.e("json", json.toString());
            Log.e("id_attribut", id_attribut);
            Log.e("id_aplikasi", id_aplikasi);

            return json;

        }

        @Override
        protected void onPostExecute(JSONObject json) {

            pDialog.dismiss();

            try {

                if (json != null) {

                    JSONArray c = json.getJSONArray("data");
                    JSONObject data = c.getJSONObject(0);
                    String nama_produk = data.getString("nama_produk") + " " + data.getString("value");

                    nama = nama_produk;
                    id_produk = data.getString("id_produk");
                    qty = "1";
                    harga = data.getString("harga");

                    model.setId(id_produk);
                    model.setTitle(nama_produk);
                    model.setHarga(data.getString("harga"));
                    model.setKategori(data.getString("nama_kategori_produk"));
                    model.setDeskripsi(data.getString("deskripsi"));

                    model.setFragment(ProdukDetail.this);

                    String html = "<div style='padding: 0px; margin: 0px;'>" + model.getDeskripsi() + "</div>";
                    binding.webContainer.loadData(html, "text/html", null);

                    model.setIndicator(binding.indicator);

                    JSONArray gambar_data = data.getJSONArray("gambar");

                    JSONObject gmb_def = gambar_data.getJSONObject(0);
                    String url_image_def = gmb_def.getString("gambar_produk");
                    gambar = url_image_def;

                    for (int j = 0; j < gambar_data.length(); j++) {
                        JSONObject gmb = gambar_data.getJSONObject(j);
                        String url_image = gmb.getString("gambar_produk");
                        model.AddSlide(gmb.getString("id_gambar"), url_image);
                    }

                    ukuran_model = new ArrayList<ProdukUkuranModel>();
                    ukuran_list = new ArrayList<String>();
                    JSONArray ukuran_data = data.getJSONArray("ukuran");

                    //default ukuran
                    JSONObject ukuran_def = ukuran_data.getJSONObject(0);
                    id_ukuran = ukuran_def.getString("id_ukuran");
                    berat = ukuran_def.getString("berat");
                    stok = ukuran_def.getString("stock");
                    Log.e("ukuran_def", id_ukuran);

                    for (int k = 0; k < ukuran_data.length(); k++) {
                        JSONObject ukuran = ukuran_data.getJSONObject(k);
                        ukuran_model.add(new ProdukUkuranModel(ukuran.getString("id_ukuran"), ukuran.getString("ukuran"),
                                ukuran.getString("berat"), ukuran.getString("stock")));
                        ukuran_list.add(ukuran.getString("ukuran"));
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.
                            simple_spinner_item, ukuran_list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinUkuran.setAdapter(dataAdapter);

                    binding.spinUkuran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            id_ukuran = ukuran_model.get(position).getId_ukuran();
                            berat = ukuran_model.get(position).getBerat();
                            stok = ukuran_model.get(position).getStok();
                            ukuran = ukuran_model.get(position).getUkuran();
                         
                            SpannableString spanString;
//                            spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
//                            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
                            if (stok == "0"){
                                spanString = new SpannableString("sold out");
                                spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
                            }else {
                                spanString = new SpannableString(stok);
                            }
                            binding.textView81.setText(spanString);
                         
                            Log.e("ukuran", id_ukuran);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }

                    });

                    binding.setModel(model);

                    binding.btnBeli.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (Integer.parseInt(qty) > Integer.parseInt(stok)) {

                                Toast.makeText(getActivity(), "Stok habis", Toast.LENGTH_SHORT).show();

                            }else if(berat.equals("0")){
                            
                                Toast.makeText(getActivity(), "Mohon maaf, produk belum bisa dibeli", Toast.LENGTH_SHORT).show();
                            
                            } else {

                                int cekProduk = db.cekProdukKeranjang(id_ukuran);
                                if(cekProduk > 0){

                                    Toast.makeText(getActivity(), "Produk sudah masuk ke Keranjang", Toast.LENGTH_SHORT).show();

                                } else {

                                    db.addKeranjang(new KeranjangItemModel(
                                            id_produk,
                                            id_attribut,
                                            id_ukuran,
                                            nama,
                                            ukuran,
                                            qty,
                                            stok,
                                            berat,
                                            harga,
                                            gambar));

                                    ((MainActivity) getActivity()).setNotifCount();

                                    Toast.makeText(getActivity(), "Produk berhasil dimasukkan ke Keranjang", Toast.LENGTH_SHORT).show();

                                }

                            }

                        }
                    });

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
