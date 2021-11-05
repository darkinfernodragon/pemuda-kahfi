package net.atommobile.pemudakahfi;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
//import android.support.v4.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
//import android.support.v4.app.FragmentManager;
import androidx.fragment.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
import androidx.fragment.app.FragmentTransaction;
//import android.support.v4.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.atommobile.pemudakahfi.R;
import net.atommobile.pemudakahfi.db.DatabaseHandler;
import net.atommobile.pemudakahfi.menu.MenuPage;
import net.atommobile.pemudakahfi.parser.JSONParser;
import net.atommobile.pemudakahfi.transaksi.keranjang.KeranjangPage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private static DisplayImageOptions options;

    public String base_server = "";
    public String app_id = "";
    public String app_id_user = "";
    public String api_path = "";
    public String app_path = "";
    public String api_key = "";
    public String background = "";

    private DatabaseHandler db;
    private int mNotifCount = 0;

    public String token;
    private final int interval = 5000; // 1 Second
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable(){
        public void run() {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if(!task.isSuccessful()) {
                        Log.w("error", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    String token = task.getResult();

                    if(token == null){
                        handler.postDelayed(runnable, interval);
                    } else {
                        Log.e("Token", token);
                        kirim(token);
                    }
                }
            });
        }
    };

    public void kirim(String token){
        this.token = token;
        new JSONParse().execute();
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("appid", app_id));
            params.add(new BasicNameValuePair("regid", token));
            params.add(new BasicNameValuePair("server", "firebase"));

            String url = "register";
            Log.e("url", url);

            JSONParser jParser = new JSONParser(MainActivity.this); // get JSON data from URL
            JSONObject json = jParser.makeHttpRequest(url, "POST", params);

            //Log.e("json", json.toString());

            return json;

        }

        @Override
        protected void onPostExecute(JSONObject json) {

            try {

                if(json != null){

                    JSONObject data = json.getJSONObject("status");
                    String status = data.getString("sukses");

                    db.addToken(token);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);

        int is_registered = db.getTokenAvailable();
        if(is_registered == 0){
            handler.postDelayed(runnable, interval);
        }

        base_server = getString(R.string.server);
        app_id = getString(R.string.app_id);
        app_id_user =getString(R.string.app_id_user);
        api_path = getString(R.string.api_path);
        app_path = "uploads/"; //getString(R.string.app_path) + "/" + getString(R.string.app_id_user) + "/" + getString(R.string.app_id) + "/";
        api_key = getString(R.string.api_key);
        background = getString(R.string.background);

        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        TextView headerTitle = (TextView) toolbar.findViewById(R.id.header_title);
        headerTitle.setText(getString(R.string.header_title));
        setSupportActionBar(toolbar);

        initImageLoader(getApplicationContext());

        FrameLayout layoutMain = (FrameLayout) findViewById(R.id.layout_container);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layoutMain.setBackgroundColor(getResources().getColor(R.color.ColorBackground, getTheme()));
        }else {
            layoutMain.setBackgroundColor(getResources().getColor(R.color.ColorBackground));
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new MenuPage();
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();


        setNotifCount();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        String app_jenis = getString(R.string.app_jenis);
        if(app_jenis.equals("ecommerce")) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);

            MenuItem item = menu.findItem(R.id.btn_keranjang);
            MenuItemCompat.setActionView(item, R.layout.badge);
            View view = MenuItemCompat.getActionView(item);
            TextView textView = (TextView) view.findViewById(R.id.hotlist_hot);
            textView.setText(String.valueOf(mNotifCount));

            if (mNotifCount == 0) {
                textView.setVisibility(View.GONE);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "Keranjang Kosong", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment fragment = new KeranjangPage();
                        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment, "KERANJANG").addToBackStack(null).commit();

                        setTitle("Keranjang");
                    }
                });
            }

        }

        return true;
    }

    public void setNotifCount(){
        mNotifCount = db.getKeranjangCount();
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btn_keranjang) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        TextView headerTitle = (TextView) toolbar.findViewById(R.id.header_title);
        headerTitle.setText(title.toString());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .delayBeforeLoading(0)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .build();

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        config.defaultDisplayImageOptions(options);

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

//    @Override
//    public void onBackPressed(){
//        FragmentManager fm = getSupportFragmentManager();
//        if (fm.getBackStackEntryCount() > 0) {
//            Log.i("MainActivity", "popping backstack");
//            fm.popBackStack();
//        } else {
//            Log.i("MainActivity", "nothing on backstack, calling super");
//            super.onBackPressed();
//        }
//    }

    public void setBackground(View v, LinearLayout layout, Resources resources){

        if(background.equals("pattern")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                layout.setBackground( resources.getDrawable(R.drawable.bg_pattern) );
            } else {
                layout.setBackgroundDrawable( resources.getDrawable(R.drawable.bg_pattern) );
            }

        } else if(background.equals("background")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                layout.setBackground( resources.getDrawable(R.drawable.background) );
            } else {
                layout.setBackgroundDrawable( resources.getDrawable(R.drawable.background) );
            }
        } else {
            layout.setBackgroundColor(resources.getColor(R.color.ColorBackground));
        }

    }

    public String getApiPath(){
        String ApiPath = base_server + api_path;
        return ApiPath;
    }

    public String getAppPath(){
        String AppPath = base_server + app_path;
        return AppPath;
    }

    public String getSharedkey(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(c.getTime());

        String toHash = app_id + "&" + app_id_user + "&" + api_key + "&" + strDate;

        String Hashed = bin2hex(getHash(toHash));
        return Hashed;
    }

    public byte[] getHash(String password) {
        MessageDigest digest=null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        digest.reset();
        return digest.digest(password.getBytes());
    }

    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
    }

    public int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public int pxToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

}
