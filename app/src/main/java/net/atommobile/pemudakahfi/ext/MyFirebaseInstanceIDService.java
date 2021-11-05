package net.atommobile.pemudakahfi.ext;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Created by faizlidwibrido on 9/5/16.
 */
public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseInstanceIDService";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        String refreshToken = FirebaseMessaging.getInstance().getToken().toString();

        //Displaying token in Logcat
        Log.d("TAG", "Refreshed Token : " + refreshToken);

        sendRegistrationToServer(refreshToken);
    }

//    @Override
//    public void onTokenRefresh() {
//        //super.onTokenRefresh();
//
//        //getting registration token
//
//    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
        Log.e("Registration ID : ", token);
    }


}