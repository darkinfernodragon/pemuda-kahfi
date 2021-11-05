package net.atommobile.pemudakahfi.news;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import net.atommobile.pemudakahfi.MainActivity;
import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.parser.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 23/05/16.
 */
public class NewsPage extends Fragment {

    String api = "info_array/";
    String api_kategori = "kategori_array/";
    String idMenu;

    private ListView mPostsList;
    Spinner spin_kategori;

    private ArrayList<NewsModel> mItems;
    private NewsAdapter adapter;

    private ArrayList<String> KategoriList;
    private ArrayList<KategoriModel> mItemsKategori;
    String id_kategori_selected = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.page_news,container,false);

        idMenu = this.getArguments().getString("idMenu");

        String awal = this.getArguments().getString("awal");
        if(awal.equals("")){
            LinearLayout layout_web = (LinearLayout) v.findViewById(R.id.layout_news);
            ((MainActivity)getActivity()).setBackground(v, layout_web, getResources());

            ImageView logo = (ImageView) v.findViewById(R.id.img_logo);
            logo.setVisibility(View.GONE);
        }

        mItems = new ArrayList<NewsModel>();

        mPostsList = (ListView) v.findViewById(R.id.list_news);

        spin_kategori = (Spinner) v.findViewById(R.id.spin_kategori);
        mItemsKategori = new ArrayList<KategoriModel>();

        new JSONParse_kategori().execute();

        return v;
    }

    private class JSONParse_kategori extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            String url = api_kategori + idMenu;

            JSONParser jParser = new JSONParser(getActivity()); // get JSON data from URL
            JSONObject json = jParser.makeHttpRequest(url, "POST", params);

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {

            try {

                if(json != null){

                    KategoriList = new ArrayList<String>();
                    KategoriList.add("Semua Kategori");
                    mItemsKategori.add(new KategoriModel("", ""));

                    JSONArray c = json.getJSONArray("data_kategori");
                    for(int i = 0; i < c.length(); i++){

                        JSONObject data = c.getJSONObject(i);
                        mItemsKategori.add(new KategoriModel(data.getString("id_kategori"), data.getString("nm_kategori")));
                        KategoriList.add(data.getString("nm_kategori"));

                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.
                            simple_spinner_item, KategoriList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // attaching data adapter to spinner
                    spin_kategori.setAdapter(dataAdapter);

                    spin_kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // On selecting a spinner item
                            id_kategori_selected = mItemsKategori.get(position).getId();
                            new JSONParse().execute();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }

                    });

                    new JSONParse().execute();

                } else {
                    Toast.makeText(getActivity(), "Kategori bermasalah", Toast.LENGTH_SHORT).show();
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

            String url = "";

            if(id_kategori_selected == ""){
                url = api + idMenu;
            } else {
                url = api + idMenu + "/" + id_kategori_selected;
            }

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

                    mItems.clear();

                    JSONArray c = json.getJSONArray("data_info");
                    for(int i = 0; i < c.length(); i++){
                        JSONObject data = c.getJSONObject(i);
                        mItems.add(new NewsModel(data.getString("id_info"), data.getString("picture_t"), data.getString("judul"), data.getString("tgl_input")));
                    }

                    adapter = new NewsAdapter(getActivity(), R.layout.page_news_item, mItems);
                    mPostsList.setAdapter(adapter);

                    ListAdapter listAdapter = mPostsList.getAdapter();
                    if (listAdapter != null) {

                        int totalHeight = 0;
                        int desiredWidth = View.MeasureSpec.makeMeasureSpec(mPostsList.getWidth(), View.MeasureSpec.AT_MOST);
                        for (int i = 0; i < listAdapter.getCount(); i++) {
                            View listItem = listAdapter.getView(i, null, mPostsList);
                            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                            totalHeight += listItem.getMeasuredHeight();
                        }

                        ViewGroup.LayoutParams params = mPostsList.getLayoutParams();
                        params.height = totalHeight + (mPostsList.getDividerHeight() * (listAdapter.getCount() - 1));

                        mPostsList.setLayoutParams(params);
                        mPostsList.requestLayout();

                    }

                    mPostsList.setOnItemClickListener(new mItemClickListener());

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private class mItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            //displayView(position);

//            Intent i = new Intent(getActivity(), ThreadDetailActivity.class);
//            ThreadModel item = (ThreadModel) mPostsList.getAdapter().getItem(position);
//            i.putExtra("kode", item.getKode());
//            startActivity(i);

            Bundle bundle = new Bundle();
            bundle.putString("id_news", mItems.get(position).getId());

            Fragment fragment = new NewsDetail();
            fragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.add(R.id.layout_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }

}
