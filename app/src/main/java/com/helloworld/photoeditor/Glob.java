package com.helloworld.photoeditor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Glob {


    public static boolean isFB = false;
    public static boolean isAdmob = true;

    //public static String acc_link = "";  //REPLACE ABCD with Developer Console name
    public static String privacy_link = "https://imcollage.blogspot.com/2020/01/privacy-policy.html";

    public static boolean isOnline(Context ctx) {
        NetworkInfo netInfo = ((ConnectivityManager) ctx.getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnected()) {
            return false;
        }
        return true;
    }
}
