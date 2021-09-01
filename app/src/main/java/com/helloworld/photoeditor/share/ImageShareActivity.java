package com.helloworld.photoeditor.share;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.helloworld.photoeditor.BuildConfig;
import com.helloworld.photoeditor.R;
import com.helloworld.photoeditor.activities.MainActivity;
import com.helloworld.photoeditor.bitmap.BitmapLoader;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.io.File;

import static androidx.core.content.FileProvider.getUriForFile;

public class ImageShareActivity extends AppCompatActivity implements OnClickListener {
    private ImageView back;
    private ImageView ivFacebook;
    private ImageView ivFinalImage;
    private ImageView ivInstagram;
    private ImageView ivShareMore;
    private ImageView ivWhatsApp;
    private Bundle bundle;
    private String imagePath;
    private com.facebook.ads.AdView adView;


    private class BitmapWorkerTask extends AsyncTask<Void, Void, Bitmap> {
        BitmapLoader bitmapLoader = new BitmapLoader();
        DisplayMetrics metrics;

        public BitmapWorkerTask() {
            File file = new File(ImageShareActivity.this.imagePath);
            this.metrics = ImageShareActivity.this.getResources().getDisplayMetrics();
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Bitmap doInBackground(Void... arg0) {
            try {
                return this.bitmapLoader.load(ImageShareActivity.this.getApplicationContext(), new int[]{this.metrics.widthPixels, this.metrics.heightPixels}, ImageShareActivity.this.imagePath);
            } catch (Exception e) {
                return null;
            }
        }

        @SuppressLint("WrongConstant")
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                ImageShareActivity.this.ivFinalImage.setImageBitmap(bitmap);
            } else {
                Toast.makeText(ImageShareActivity.this, ImageShareActivity.this.getString(R.string.error_img_not_found), 0).show();
            }
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_share);


        this.bundle = getIntent().getExtras();
        if (this.bundle != null) {
            this.imagePath = this.bundle.getString("imagePath");
        }

        loadFbBanner();

        getWindow().setFlags(1024, 1024);
        bindView();

        new BitmapWorkerTask().execute(new Void[0]);


        if (isOnline()) {

            AdView adview = new AdView(this);
            adview.setAdSize(AdSize.LARGE_BANNER);
            adview.setAdUnitId(getString(R.string.banner_ad_id));
            ((RelativeLayout) findViewById(R.id.adView)).addView(adview);

            adview.loadAd(new Builder().build());

        } else {

        }
    }

    private void bindView() {
        this.ivFinalImage = (ImageView) findViewById(R.id.ivFinalImage);
        this.ivFinalImage.setOnClickListener(this);
        this.ivWhatsApp = (ImageView) findViewById(R.id.iv_whatsapp);
        this.ivWhatsApp.setOnClickListener(this);
        this.ivFacebook = (ImageView) findViewById(R.id.iv_facebook);
        this.ivFacebook.setOnClickListener(this);
        this.ivInstagram = (ImageView) findViewById(R.id.iv_instagram);
        this.ivInstagram.setOnClickListener(this);
        this.ivShareMore = (ImageView) findViewById(R.id.iv_Share_More);
        this.ivShareMore.setOnClickListener(this);

        this.back = (ImageView) findViewById(R.id.back);
        this.back.setOnClickListener(this);
    }

    public boolean isOnline() {
        @SuppressLint("WrongConstant") NetworkInfo netInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }

    public void onClick(View view) {
        Uri contentUri = getUriForFile(this, BuildConfig.APPLICATION_ID, new File(imagePath));

        Intent shareIntent = new Intent("android.intent.action.SEND");
        shareIntent.setType("image/*");
        shareIntent.putExtra("android.intent.extra.TEXT", "Download this amazing app\n\n" + "Photo Collage Maker - Collage Photo Editor" + " Created By : " + Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
        shareIntent.putExtra("android.intent.extra.STREAM", contentUri);
        switch (view.getId()) {
            case R.id.iv_whatsapp:
                try {
                    shareIntent.setPackage("com.whatsapp");
                    startActivity(shareIntent);
                    return;
                } catch (Exception e) {
                    Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
                    return;
                }
            case R.id.iv_facebook:
                try {
                    shareIntent.setPackage("com.facebook.katana");
                    startActivity(shareIntent);
                    return;
                } catch (Exception e2) {
                    Toast.makeText(this, "Facebook not installed", Toast.LENGTH_SHORT).show();
                    return;
                }
            case R.id.iv_instagram:
                try {
                    shareIntent.setPackage("com.instagram.android");
                    startActivity(shareIntent);
                    return;
                } catch (Exception e3) {
                    Toast.makeText(this, "Instagram not installed", Toast.LENGTH_SHORT).show();
                    return;
                }
            case R.id.iv_Share_More:
                Intent sharingIntent = new Intent("android.intent.action.SEND");
                sharingIntent.setType("image/*");
                sharingIntent.putExtra("android.intent.extra.TEXT", "Download this amazing app\n\n" + "Photo Collage Maker - Collage Photo Editor" + " Create By : " + Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                sharingIntent.putExtra("android.intent.extra.STREAM", contentUri);
                startActivity(Intent.createChooser(sharingIntent, "Share Image using"));
                return;
            case R.id.back:
                super.onBackPressed();
                return;
            default:
                return;
        }
    }

    public void onBackPressed() {
        startActivity(new Intent(ImageShareActivity.this, MainActivity.class));
        finish();
        //super.onBackPressed();
    }

    protected void onResume() {
        super.onResume();
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
