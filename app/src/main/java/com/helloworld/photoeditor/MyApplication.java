package com.helloworld.photoeditor;

import android.app.Application;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy.Builder;

import com.facebook.ads.AudienceNetworkAds;

public class MyApplication extends Application {
    static Context context;

    public void onCreate() {
        super.onCreate();

        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);

        context = getApplicationContext();
        if (VERSION.SDK_INT >= 24) {
            Builder builder = new Builder();
            builder.detectFileUriExposure();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    public static Context getContext() {
        return context;
    }
}
