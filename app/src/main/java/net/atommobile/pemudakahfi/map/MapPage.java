package net.atommobile.pemudakahfi.map;

import android.app.ProgressDialog;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.atommobile.pemudakahfi.MainActivity;
import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.parser.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 19/05/16.
 */
public class MapPage extends Fragment implements OnMapReadyCallback {

    // Google Map
    private GoogleMap googleMap;

    String api = "lokasi/";
    String idMenu;

    private double latitude;
    private double longitude;
    private String title;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.page_map, container, false);
        idMenu = this.getArguments().getString("idMenu");

        String awal = this.getArguments().getString("awal");
        if (awal.equals("")) {
            LinearLayout layout_web = (LinearLayout) v.findViewById(R.id.layout_map);
            ((MainActivity) getActivity()).setBackground(v, layout_web, getResources());

            ImageView logo = (ImageView) v.findViewById(R.id.img_logo);
            logo.setVisibility(View.GONE);
        }

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

            String url = api + idMenu;

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

                    JSONObject c = json.getJSONObject("data_lokasi");

                    double latitude = c.getDouble("langx");
                    double longitude = c.getDouble("langy");
                    String title = c.getString("lokasi");
                    initilizeMap(latitude, longitude, title);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap == null) {
            Toast.makeText(getActivity(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        } else {
            double latitude = this.latitude;
            double longitude = this.longitude;
            String title = this.title;

            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(title);
            Marker locationMarker = googleMap.addMarker(marker);

            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(latitude, longitude)).zoom(17).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            Toast toast = Toast.makeText(getActivity(), "Click the marker for more information", Toast.LENGTH_SHORT);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            v.setBackgroundColor(Color.alpha(0));
            toast.show();

        }
    }

    private void initilizeMap(double latitude, double longitude, String title) {
        if (googleMap == null) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.title = title;

            SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
            mapFrag.getMapAsync(this);

            // check if map is created successfully or not

        }
    }

}
