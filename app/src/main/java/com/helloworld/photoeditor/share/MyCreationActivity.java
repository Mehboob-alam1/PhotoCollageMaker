package com.helloworld.photoeditor.share;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.media.session.PlaybackStateCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.helloworld.photoeditor.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.util.ArrayList;

public class MyCreationActivity extends AppCompatActivity {
    public static ArrayList<String> IMAGEALLARY = new ArrayList<>();
    public static int pos;
    private ImageView Iv_back_creation;
    MyCreationAdapter myCreationAdapter;
    private GridView grid_crea;

    private com.facebook.ads.AdView adView;

    class C14711 implements OnClickListener {
        C14711() {
        }

        public void onClick(View v) {
            MyCreationActivity.super.onBackPressed();
            finish();
//            showInterstitial();
//            MyCreationActivity.this.startActivity(new Intent(MyCreationActivity.this, MainActivity.class));
//            MyCreationActivity.this.finish();
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_creation);

        loadFbBanner();

        if (Glob.isOnline(this)) {

            AdView adview = new AdView(this);
            adview.setAdSize(AdSize.SMART_BANNER);
            adview.setAdUnitId(getString(R.string.banner_ad_id));
            ((RelativeLayout) findViewById(R.id.adContainer_creation)).addView(adview);

            adview.loadAd(new Builder().build());

            //loadFbBanner();

        }

        if (MyCreationAdapter.imagegallary.size() == 0) {
            findViewById(R.id.text_noimage).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.text_noimage).setVisibility(View.GONE);
        }


        this.grid_crea = findViewById(R.id.grid_crea);
        this.myCreationAdapter = new MyCreationAdapter(this, IMAGEALLARY);
        IMAGEALLARY.clear();
        listAllImages(new File(Environment.getExternalStorageDirectory().getPath() + "/" + getString(R.string.app_name)));
        this.grid_crea.setAdapter(this.myCreationAdapter);
        this.Iv_back_creation = findViewById(R.id.back_click_iv);
        this.Iv_back_creation.setOnClickListener(new C14711());
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }




    private void listAllImages(File filepath) {
        File[] files = filepath.listFiles();
        if (MyCreationAdapter.imagegallary.size() == 0) {
            findViewById(R.id.text_noimage).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.text_noimage).setVisibility(View.GONE);
        }
        if (files != null) {
            for (int j = files.length - 1; j >= 0; j--) {
                String ss = files[j].toString();
                File check = new File(ss);
                Log.d("" + check.length(), "" + check.length());
                if (check.length() <= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
                    Log.e("Invalid Image", "Delete Image");
                } else if (check.toString().contains(".jpg") || check.toString().contains(".png") || check.toString().contains(".jpeg")) {
                    IMAGEALLARY.add(ss);
                }
                System.out.println(ss);
            }
            return;
        }

        System.out.println("Empty Folder");
    }


    protected void onResume() {
        super.onResume();
        if (MyCreationAdapter.imagegallary.size() == 0) {
            findViewById(R.id.text_noimage).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.text_noimage).setVisibility(View.GONE);
        }
    }

    private void loadFbBanner() {
        if(com.helloworld.photoeditor.Glob.isFB) {
            adView = new com.facebook.ads.AdView(this, getResources().getString(R.string.fb_banner), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
            LinearLayout adContainer = findViewById(R.id.fb_banner_container);
            adContainer.addView(adView);
            adView.loadAd();
        }

    }
}
