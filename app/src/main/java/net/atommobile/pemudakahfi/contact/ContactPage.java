package net.atommobile.pemudakahfi.contact;

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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.atommobile.pemudakahfi.MainActivity;
import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.parser.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 12/05/16.
 */
public class ContactPage extends Fragment {

    private final static String TAG = "TestImageGetter";

    WebView web_container;
    String api = "contact/";
    String api_send ="email/send/";
    String idMenu;

    TextView telepon;
    TextView email;
    TextView alamat;
    TextView more;
    TextView website;

    EditText email_dari;
    EditText pesan;
    Button kirim;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.page_contact,container,false);

        idMenu = this.getArguments().getString("idMenu");

        String awal = this.getArguments().getString("awal");
        if(awal == ""){
            LinearLayout layout_web = (LinearLayout) v.findViewById(R.id.layout_contact);
            ((MainActivity)getActivity()).setBackground(v, layout_web, getResources());

            ImageView logo = (ImageView) v.findViewById(R.id.img_logo);
            logo.setVisibility(View.GONE);
        }

        telepon = (TextView) v.findViewById(R.id.lbl_telepon);
        email = (TextView) v.findViewById(R.id.lbl_email);
        alamat = (TextView) v.findViewById(R.id.lbl_alamat);
        more = (TextView) v.findViewById(R.id.lbl_more);
        website = (TextView) v.findViewById(R.id.lbl_website);

        email_dari = (EditText) v.findViewById(R.id.txt_email);
        pesan = (EditText) v.findViewById(R.id.txt_pesan);

        kirim = (Button) v.findViewById(R.id.btn_kirim);
        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KirimPesan().execute();
            }
        });

        new JSONParse().execute();

        return v;
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            String url = api + idMenu;

            JSONParser jParser = new JSONParser(getActivity()); // get JSON data from URL
            JSONObject json = jParser.makeHttpRequest(url, "POST", params);

            Log.e("json", json.toString());

            return json;

        }

        @Override
        protected void onPostExecute(JSONObject json) {

            try {

                if(json != null){

                    JSONObject c = json.getJSONObject("data_contact");

                    telepon.setText(c.getString("phone"));
                    email.setText(c.getString("email"));
                    alamat.setText(c.getString("alamat"));
                    more.setText(c.getString("more"));
                    website.setText(c.getString("website"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class KirimPesan extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;
        String email_from;
        String message;
        String email_destination;
        String subject;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            email_from = email_dari.getText().toString();
            message = pesan.getText().toString();
            email_destination = email.getText().toString();
            subject = "Pesan dari Aplikasi " + getResources().getString(R.string.app_name);

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Mengirim ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email_from", email_from));
            params.add(new BasicNameValuePair("message", message));
            params.add(new BasicNameValuePair("email_destination", email_destination));
            params.add(new BasicNameValuePair("subject", subject));

            String url = api_send + idMenu;

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

                    Boolean c = json.getBoolean("data_contact");

                    if(c == true) {
                        email_dari.setText("");
                        pesan.setText("");

                        Toast.makeText(getContext(), "Pesan telah dikirim", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Silahkan coba lagi nanti", Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
