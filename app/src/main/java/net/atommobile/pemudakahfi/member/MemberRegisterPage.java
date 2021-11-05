package net.atommobile.pemudakahfi.member;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.db.DatabaseHandler;
import net.atommobile.pemudakahfi.parser.JSONParser;
import net.atommobile.pemudakahfi.produk.ProdukPage;

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
public class MemberRegisterPage extends Fragment {

    private String api = "register_customer/";
    private String id_aplikasi;

    private EditText txtNama;
    private EditText txtEmail;
    private EditText txtPassword;

    private Button btnRegister;
    private TextView btnLogin;

    DatabaseHandler db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.page_member_register,container,false);

        id_aplikasi = getString(R.string.app_id);

        txtNama = (EditText) v.findViewById(R.id.txt_nama);
        txtEmail = (EditText) v.findViewById(R.id.txt_email);
        txtPassword = (EditText) v.findViewById(R.id.txt_password);

        btnLogin = (TextView) v.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MemberLoginPage();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment).addToBackStack(null).commit();

            }
        });

        btnRegister = (Button) v.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONParse().execute();
            }
        });

        db = new DatabaseHandler(getActivity());

        return v;

    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        private String nama;
        private String email;
        private String password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            nama = txtNama.getText().toString();
            email = txtEmail.getText().toString();
            password = txtPassword.getText().toString();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nama", nama));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("id_aplikasi", id_aplikasi));

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
                    // JSONArray c = json.getJSONArray("data");

                    if ((status.equals("1"))||(status.equals("true"))) {

                        // JSONObject data = c.getJSONObject(0);

                        // MemberModel member = new MemberModel(
                        //         data.getString("id_member"),
                        //         email,
                        //         "",
                        //         nama,
                        //         "",
                        //         "",
                        //         "",
                        //         "",
                        //         "",
                        //         "",
                        //         "",
                        //         "",
                        //         ""
                        // );

                        Toast.makeText(getActivity(), "Register Berhasil!", Toast.LENGTH_SHORT).show();

                        Fragment fragment = new MemberLoginPage();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment).addToBackStack(null).commit();


                        // db.addMember(member);

                        // Fragment fragment = new ProdukPage();
                        // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment).addToBackStack(null).commit();


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
}
