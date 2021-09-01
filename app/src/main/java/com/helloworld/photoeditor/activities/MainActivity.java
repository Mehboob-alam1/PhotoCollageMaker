package com.helloworld.photoeditor.activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.helloworld.photoeditor.BuildConfig;
import com.helloworld.photoeditor.Glob;
import com.helloworld.photoeditor.PrivacyPolicyActivity;
import com.helloworld.photoeditor.R;
import com.helloworld.photoeditor.bitmap.BitmapResizer;
import com.helloworld.photoeditor.collagelib.CollageActivity;
import com.helloworld.photoeditor.collagelib.CollageHelper;
import com.helloworld.photoeditor.gallerylib.GalleryFragment;
import com.helloworld.photoeditor.imagesavelib.ImageLoader;
import com.helloworld.photoeditor.imagesavelib.ImageLoader.ImageLoaded;
import com.helloworld.photoeditor.share.MyCreationActivity;
import com.helloworld.photoeditor.utils.Utility;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    String IMAGE_DIRECTORY_NAME = "PhotoCollageMaker";
    int MEDIA_TYPE_IMAGE = 1;
    int PERMISSION_CAMERA_EDITOR = 44;
    int PERMISSION_COLLAGE_EDITOR = 11;
    int PERMISSION_MIRROR_EDITOR = 55;
    int PERMISSION_SCRAPBOOK_EDITOR = 33;
    int PERMISSION_SINGLE_EDITOR = 22;
    int REQUEST_MIRROR = 3;
    Uri fileUri;
    GalleryFragment galleryFragment;
    ImageLoader imageLoader;
    InterstitialAd mInterstitialAd;
    RelativeLayout mMainLayout;
    LinearLayout mCameraLayout, mMirrorLayout;
    LinearLayout mCollegeLayout;
    LinearLayout mScrapbookLayout, mSingleEditorLayout;

   // android.app.AlertDialog alertDialog = null;
    private ProgressBar progressBarExitRefresh;
    private LinearLayout adExitChoicesContainer;
    private LinearLayout adExitView;
    public static final int RequestPermissionCode = 1;
    private LinearLayout nativeAdContainerExitAds;
    private long mBackPressed = 0;
    //private DrawerLayout mDrawerLayout;
    //private ListView mDrawerList;
    private int navigationPosition = 0;
    //ActionBarDrawerToggle mDrawerToggle;
    Toolbar toolbar;

    LinearLayout rate,share,about;
    private AdView adView;

    private final String TAG = MainActivity.class.getSimpleName();
    private NativeAd nativeAd;
    private NativeAdLayout nativeAdLayout;
    private LinearLayout ll_adView;
    private com.facebook.ads.InterstitialAd interstitialAd;

    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpNavigationDrawer();

        rate=findViewById(R.id.ll_rate);
        share=findViewById(R.id.ll_share);
        about=findViewById(R.id.ll_about);

        loadFbBanner();
        loadFbInterstitial();



        //getWindow().addFlags(1024);
        findViewbyIds();
        this.imageLoader = new ImageLoader(this);
        this.imageLoader.setListener(new ImageLoaded() {
            public void callFileSizeAlertDialogBuilder() {
                MainActivity.this.fileSizeAlertDialogBuilder();
            }
        });

        findViewById(R.id.ivmycreation).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent i = new Intent(getApplicationContext(), MyCreationActivity.class);

                //showInterstitialAd();
                    if(Glob.isFB){
                        if(interstitialAd.isAdLoaded() && !interstitialAd.isAdInvalidated()){
                            interstitialAd.show();
                            interstitialAd.setAdListener(new InterstitialAdListener() {
                                @Override
                                public void onInterstitialDisplayed(Ad ad) {
                                    // Interstitial ad displayed callback
                                    Log.e(TAG, "Interstitial ad displayed.");
                                }

                                @Override
                                public void onInterstitialDismissed(Ad ad) {
                                    // Interstitial dismissed callback
                                    Log.e(TAG, "Interstitial ad dismissed.");

                                    startActivity(i);
                                    loadFbInterstitial();
                                }

                                @Override
                                public void onError(Ad ad, AdError adError) {
                                    // Ad error callback
                                    Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                                }

                                @Override
                                public void onAdLoaded(Ad ad) {
                                    // Interstitial ad is loaded and ready to be displayed
                                    Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                                }

                                @Override
                                public void onAdClicked(Ad ad) {
                                    // Ad clicked callback
                                    Log.d(TAG, "Interstitial ad clicked!");
                                }

                                @Override
                                public void onLoggingImpression(Ad ad) {
                                    // Ad impression logged callback
                                    Log.d(TAG, "Interstitial ad impression logged!");
                                }
                            });
                        }else{
                            startActivity(i);
                        }

                    }else{
                        startActivity(i);
                }
            }
        });


        //Google
        if (Glob.isOnline(this)) {
            com.google.android.gms.ads.AdView mAdView = new com.google.android.gms.ads.AdView(this);
            mAdView.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
            mAdView.setAdUnitId(getString(R.string.banner_ad_id));
            ((RelativeLayout) findViewById(R.id.g_adView)).addView(mAdView);

            mAdView.loadAd(new com.google.android.gms.ads.AdRequest.Builder().build());
        }

        this.mInterstitialAd = new InterstitialAd(this);
        this.mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        if (Glob.isOnline(MainActivity.this)) {
            loadInterstitialAd();
        }




        rate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(MainActivity.this);
                intentBuilder.setText("Check this amazing app for making photo collages and photo editing: " + "http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())
                        .setType("text/plain")
                        .setSubject("Sharing " + getString(R.string.app_name) + " App");
                try {
                    startActivity(intentBuilder.getIntent());
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "No app available for sharing", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });

        about.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String appDescription = getString(R.string.app_description);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("App Description");
                    builder.setMessage(appDescription)
                            .setPositiveButton("OK", null)
                            .show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadFbBanner() {
        if(Glob.isFB){
            adView = new AdView(this, getResources().getString(R.string.fb_banner), AdSize.BANNER_HEIGHT_50);
            LinearLayout adContainer = findViewById(R.id.fb_banner_container);
            adContainer.addView(adView);
            adView.loadAd();
        }
    }

    private void showRatingDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.sc_dialog_rate_confirm);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        dialog.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Uri uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID);
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                    } catch (Exception e1) {
                        e1.printStackTrace();

                    }
                }
            }
        });

        dialog.show();

    }


  /*  void setupToolbar() {
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        setTitle("");
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
*/
    /*private class DrawerItemClickListener implements AdapterView.OnItemClickListener {
        private DrawerItemClickListener() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            MainActivity.this.selectItem(position);
        }
    }*/

    private void selectItem(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        this.navigationPosition = position;
        switch (position) {
            case 1:
            case 2:
                ShowAfterAdNavigationOption();
                break;
            case 3:
            case 4:
//                runOnUiThread(new C04644());
                MainActivity.this.ShowAfterAdNavigationOption();
                break;
        }
        //this.mDrawerLayout.closeDrawers();
    }


    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //this.mDrawerToggle.syncState();
    }




    private void ShowAfterAdNavigationOption() {
        switch (this.navigationPosition) {
            case 1:
                if (Glob.isOnline(MainActivity.this)) {
                    try {
                        Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                        startActivity(marketIntent);
                    } catch (ActivityNotFoundException e) {
                        Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                        startActivity(marketIntent);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Available", Toast.LENGTH_SHORT).show();
                }
                return;
            case 2:
                Intent shareIntent = new Intent("android.intent.action.SEND");
                shareIntent.setType("text/*");
                shareIntent.putExtra("android.intent.extra.TEXT", getResources().getString(R.string.app_name) + " Created By :" + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                startActivity(Intent.createChooser(shareIntent, "Share App"));
                return;
            /*case 3:
                if (!Glob.isOnline(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "No Internet Connection..", Toast.LENGTH_SHORT).show();
                    break;
                }
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(Glob.acc_link)));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "You don't have Google Play installed", Toast.LENGTH_SHORT).show();
                }
                return;*/
            case 4:
                if (!Glob.isOnline(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "No Internet Connection..", Toast.LENGTH_SHORT).show();
                    break;
                }
                startActivity(new Intent(getApplicationContext(), PrivacyPolicyActivity.class));
                break;
            default:
                return;
        }
    }


    private void findViewbyIds() {
        this.mMainLayout = findViewById(R.id.mainLayout);
        this.mSingleEditorLayout = findViewById(R.id.layout_single_editor);
        this.mCameraLayout = findViewById(R.id.layout_camera);
        this.mCollegeLayout = findViewById(R.id.layout_college);
        this.mMirrorLayout = findViewById(R.id.layout_mirror);
        this.mScrapbookLayout = findViewById(R.id.layout_scrapbook);
        this.mSingleEditorLayout.setOnClickListener(this);
        this.mCameraLayout.setOnClickListener(this);
        this.mCollegeLayout.setOnClickListener(this);
        this.mMirrorLayout.setOnClickListener(this);
        this.mScrapbookLayout.setOnClickListener(this);
    }

    private void fileSizeAlertDialogBuilder() {
        Point p = BitmapResizer.decodeFileSize(new File(this.imageLoader.selectedImagePath), Utility.maxSizeForDimension(this, 1, 1500.0f));
        if (p == null || p.x != -1) {
            startShaderActivity();
        } else {
            startShaderActivity();
        }
    }

    private void startShaderActivity() {
        Log.e("MainActivity.startShade", this.imageLoader.selectedImagePath);
        int maxSize = Utility.maxSizeForDimension(this, 1, 1500.0f);
        Intent shaderIntent = new Intent(getApplicationContext(), MirrorNewActivity.class);
        shaderIntent.putExtra("selectedImagePath", this.imageLoader.selectedImagePath);
        shaderIntent.putExtra("isSession", false);
        shaderIntent.putExtra("MAX_SIZE", maxSize);
        Utility.logFreeMemory(this);
        startActivity(shaderIntent);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == this.PERMISSION_COLLAGE_EDITOR) {
            if (ActivityCompat.checkSelfPermission(this, permissions[0]) == 0) {
                openCollage(false, false, false);
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        } else if (requestCode == this.PERMISSION_SINGLE_EDITOR) {
            if (ActivityCompat.checkSelfPermission(this, permissions[0]) == 0) {
                openCollage(true, false, false);
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        } else if (requestCode == this.PERMISSION_SCRAPBOOK_EDITOR) {
            if (ActivityCompat.checkSelfPermission(this, permissions[0]) == 0) {
                openCollage(false, true, false);
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        } else if (requestCode == this.PERMISSION_CAMERA_EDITOR) {
            if (ActivityCompat.checkSelfPermission(this, permissions[0]) == 0) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                this.fileUri = getOutputMediaFileUri(this.MEDIA_TYPE_IMAGE);
                intent.putExtra("output", this.fileUri);
                startActivityForResult(intent, this.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        } else if (requestCode != this.PERMISSION_MIRROR_EDITOR) {

        } else {
            if (ActivityCompat.checkSelfPermission(this, permissions[0]) == 0) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction("android.intent.action.GET_CONTENT");
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), this.REQUEST_MIRROR);
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", this.fileUri);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.fileUri = savedInstanceState.getParcelable("file_uri");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == this.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                if (resultCode == -1) {
                    Intent localIntent = new Intent(this, CollageActivity.class);
                    System.out.println("CAMERA IMAGE PATH" + this.fileUri.getPath());
                    localIntent.putExtra("selected_image_path", this.fileUri.getPath());
                    startActivity(localIntent);
                } else if (resultCode == 0) {
                    Toast.makeText(getApplicationContext(), "No Image Captured", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == -1 && requestCode == this.REQUEST_MIRROR) {
                try {
                    this.imageLoader.getImageFromIntent(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, BuildConfig.FLAVOR + getString(R.string.error_img_not_found), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException e2) {
            e2.printStackTrace();
        }
    }

    //Google
    void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        this.mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                loadInterstitialAd();
            }
        });
    }

    void showInterstitialAd() {
        if (mInterstitialAd != null && Glob.isOnline(MainActivity.this)) {
            if (mInterstitialAd.isLoaded()) {
                Random rn = new Random();
                int answer = rn.nextInt(70) + 30;
                if (answer < 60) {
                    mInterstitialAd.show();
                }

            }
        }
    }


    public void onClick(View v) {
        if (this.mCollegeLayout == v) {
            if (VERSION.SDK_INT < 19) {
                //showInterstitialAd();

                if(Glob.isFB){
                    if(interstitialAd.isAdLoaded() && !interstitialAd.isAdInvalidated()){
                        interstitialAd.show();
                        interstitialAd.setAdListener(new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(Ad ad) {
                                // Interstitial ad displayed callback
                                Log.e(TAG, "Interstitial ad displayed.");
                            }

                            @Override
                            public void onInterstitialDismissed(Ad ad) {
                                // Interstitial dismissed callback
                                Log.e(TAG, "Interstitial ad dismissed.");

                                openCollage(false, false, false);
                                loadFbInterstitial();
                            }

                            @Override
                            public void onError(Ad ad, AdError adError) {
                                // Ad error callback
                                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                            }

                            @Override
                            public void onAdLoaded(Ad ad) {
                                // Interstitial ad is loaded and ready to be displayed
                                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                            }

                            @Override
                            public void onAdClicked(Ad ad) {
                                // Ad clicked callback
                                Log.d(TAG, "Interstitial ad clicked!");
                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {
                                // Ad impression logged callback
                                Log.d(TAG, "Interstitial ad impression logged!");
                            }
                        });
                    }else{
                        openCollage(false, false, false);

                    }

                }else{
                    openCollage(false, false, false);

                }

            } else if (checkAndRequestCollagePermissions()) {
               // showInterstitialAd();

                if(Glob.isFB){
                    if(interstitialAd.isAdLoaded() && !interstitialAd.isAdInvalidated()){
                        interstitialAd.show();
                        interstitialAd.setAdListener(new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(Ad ad) {
                                // Interstitial ad displayed callback
                                Log.e(TAG, "Interstitial ad displayed.");
                            }

                            @Override
                            public void onInterstitialDismissed(Ad ad) {
                                // Interstitial dismissed callback
                                Log.e(TAG, "Interstitial ad dismissed.");

                                openCollage(false, false, false);
                                loadFbInterstitial();
                            }

                            @Override
                            public void onError(Ad ad, AdError adError) {
                                // Ad error callback
                                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                            }

                            @Override
                            public void onAdLoaded(Ad ad) {
                                // Interstitial ad is loaded and ready to be displayed
                                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                            }

                            @Override
                            public void onAdClicked(Ad ad) {
                                // Ad clicked callback
                                Log.d(TAG, "Interstitial ad clicked!");
                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {
                                // Ad impression logged callback
                                Log.d(TAG, "Interstitial ad impression logged!");
                            }
                        });
                    }else{
                        openCollage(false, false, false);

                    }

                }else{
                    openCollage(false, false, false);

                }
            }
        }
        if (this.mSingleEditorLayout == v) {
            if (VERSION.SDK_INT < 19) {
                openCollage(true, false, false);
            } else if (checkAndRequestSinglePermissions()) {
                openCollage(true, false, false);
            }
        }
        if (this.mScrapbookLayout == v) {
            if (VERSION.SDK_INT < 19) {
                //showInterstitialAd();
                if(Glob.isFB){
                    if(interstitialAd.isAdLoaded() && !interstitialAd.isAdInvalidated()){
                        interstitialAd.show();
                        interstitialAd.setAdListener(new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(Ad ad) {
                                // Interstitial ad displayed callback
                                Log.e(TAG, "Interstitial ad displayed.");
                            }

                            @Override
                            public void onInterstitialDismissed(Ad ad) {
                                // Interstitial dismissed callback
                                Log.e(TAG, "Interstitial ad dismissed.");

                                openCollage(false, true, false);
                                loadFbInterstitial();
                            }

                            @Override
                            public void onError(Ad ad, AdError adError) {
                                // Ad error callback
                                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                            }

                            @Override
                            public void onAdLoaded(Ad ad) {
                                // Interstitial ad is loaded and ready to be displayed
                                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                            }

                            @Override
                            public void onAdClicked(Ad ad) {
                                // Ad clicked callback
                                Log.d(TAG, "Interstitial ad clicked!");
                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {
                                // Ad impression logged callback
                                Log.d(TAG, "Interstitial ad impression logged!");
                            }
                        });
                    }else{
                        openCollage(false, true, false);

                    }

                }else{
                    openCollage(false, true, false);

                }
            } else if (checkAndRequestScrapbookPermissions()) {
                //showInterstitialAd();
                if(Glob.isFB){
                    if(interstitialAd.isAdLoaded() && !interstitialAd.isAdInvalidated()){
                        interstitialAd.show();
                        interstitialAd.setAdListener(new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(Ad ad) {
                                // Interstitial ad displayed callback
                                Log.e(TAG, "Interstitial ad displayed.");
                            }

                            @Override
                            public void onInterstitialDismissed(Ad ad) {
                                // Interstitial dismissed callback
                                Log.e(TAG, "Interstitial ad dismissed.");

                                openCollage(false, true, false);
                                loadFbInterstitial();
                            }

                            @Override
                            public void onError(Ad ad, AdError adError) {
                                // Ad error callback
                                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                            }

                            @Override
                            public void onAdLoaded(Ad ad) {
                                // Interstitial ad is loaded and ready to be displayed
                                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                            }

                            @Override
                            public void onAdClicked(Ad ad) {
                                // Ad clicked callback
                                Log.d(TAG, "Interstitial ad clicked!");
                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {
                                // Ad impression logged callback
                                Log.d(TAG, "Interstitial ad impression logged!");
                            }
                        });
                    }else{
                        openCollage(false, true, false);

                    }

                }else{
                    openCollage(false, true, false);

                }
            }
        }
        if (this.mCameraLayout == v) {
            Intent intent;
            if (VERSION.SDK_INT < 21) {
                intent = new Intent("android.media.action.IMAGE_CAPTURE");
                this.fileUri = getOutputMediaFileUri(this.MEDIA_TYPE_IMAGE);
                intent.putExtra("output", this.fileUri);
                startActivityForResult(intent, this.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            } else if (checkAndRequestCameraPermissions()) {
                intent = new Intent("android.media.action.IMAGE_CAPTURE");
                this.fileUri = getOutputMediaFileUri(this.MEDIA_TYPE_IMAGE);
                intent.putExtra("output", this.fileUri);
                startActivityForResult(intent, this.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            }
        }
        if (this.mMirrorLayout == v) {
            if (VERSION.SDK_INT < 19) {
                openCollage(true, true, false, true);
            } else if (checkAndRequestMirrorPermissions()) {
                openCollage(true, true, false, true);
            }
        }

    }

    public boolean isAvailable(Intent intent) {
        return getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    public void openCollage(boolean isblur, boolean isScrapBook, boolean isShape) {
        openCollage(isblur, isScrapBook, isShape, false);
    }

    public void openCollage(boolean isblur, boolean isScrapBook, boolean isShape, boolean isMirror) {
        this.galleryFragment = CollageHelper.addGalleryFragment(this, R.id.gallery_fragment_container, null, true, null);
        this.galleryFragment.setCollageSingleMode(isblur);
        this.galleryFragment.setIsMirrorSelector(isMirror);
        this.galleryFragment.setIsScrapbook(isScrapBook);
        this.galleryFragment.setIsShape(isShape);
        if (!isScrapBook) {
            this.galleryFragment.setLimitMax(GalleryFragment.MAX_COLLAGE);
        }
    }

//    public void showHomeScreenFacebookAds(View rootView) {
//        try {
//            this.nativeExitAd = new NativeAd(this, getResources().getString(R.string.fb_exit_native_id));
//            this.progressBarExitRefresh = (ProgressBar) rootView.findViewById(R.id.exit_progress);
//            this.nativeAdContainerExitAds = (LinearLayout) rootView.findViewById(R.id.exit_ad_container);
//            this.progressBarExitRefresh.setVisibility(View.VISIBLE);
//            this.nativeExitAd.setAdListener(new com.facebook.ads.AdListener() {
//                public void onError(Ad ad, AdError adError) {
//                    MainActivity.this.nativeAdContainerExitAds.setVisibility(View.INVISIBLE);
//                    MainActivity.this.progressBarExitRefresh.setVisibility(View.INVISIBLE);
//                }
//
//                public void onAdLoaded(Ad ad) {
//                    try {
//                        if (MainActivity.this.nativeExitAd != null) {
//                            MainActivity.this.nativeExitAd.unregisterView();
//                            MainActivity.this.adExitView = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.fb_native_app_box_, MainActivity.this.nativeAdContainerExitAds, false);
//                            MainActivity.this.nativeAdContainerExitAds.removeAllViews();
//                            MainActivity.this.nativeAdContainerExitAds.addView(MainActivity.this.adExitView);
//                            MainActivity.this.adExitChoicesContainer = (LinearLayout) MainActivity.this.adExitView.findViewById(R.id.lin_choices_container);
//                            ImageView nativeAdIcon = (ImageView) MainActivity.this.adExitView.findViewById(R.id.fb_native_ad_icon);
//                            TextView nativeAdTitle = (TextView) MainActivity.this.adExitView.findViewById(R.id.fb_native_ad_title);
//                            MediaView nativeAdMedia = (MediaView) MainActivity.this.adExitView.findViewById(R.id.fb_native_ad_mediaview);
//                            TextView nativeAdBody = (TextView) MainActivity.this.adExitView.findViewById(R.id.fb_native_ad_body);
//                            Button nativeAdCallToAction = (Button) MainActivity.this.adExitView.findViewById(R.id.fb_native_ad_call_to_action);
//                            nativeAdTitle.setText(MainActivity.this.nativeExitAd.getAdTitle());
//                            nativeAdBody.setText(MainActivity.this.nativeExitAd.getAdBody());
//                            nativeAdCallToAction.setText(MainActivity.this.nativeExitAd.getAdCallToAction());
//                            NativeAd.downloadAndDisplayImage(MainActivity.this.nativeExitAd.getAdIcon(), nativeAdIcon);
//                            nativeAdMedia.setNativeAd(MainActivity.this.nativeExitAd);
//                            MainActivity.this.adExitChoicesContainer.addView(new AdChoicesView(MainActivity.this, MainActivity.this.nativeExitAd, true));
//                            List<View> clickableViews = new ArrayList();
//                            clickableViews.add(nativeAdTitle);
//                            clickableViews.add(nativeAdCallToAction);
//                            MainActivity.this.nativeExitAd.registerViewForInteraction(MainActivity.this.nativeAdContainerExitAds, clickableViews);
//                            MainActivity.this.progressBarExitRefresh.setVisibility(View.GONE);
//                        }
//                    } catch (Exception e) {
//                        MainActivity.this.nativeAdContainerExitAds.setVisibility(View.INVISIBLE);
//                    }
//                }
//
//                public void onAdClicked(Ad ad) {
//                    MainActivity.this.progressBarExitRefresh.setVisibility(View.GONE);
//                }
//
//                public void onLoggingImpression(Ad ad) {
//                    MainActivity.this.progressBarExitRefresh.setVisibility(View.GONE);
//                }
//            });
//            this.nativeExitAd.loadAd();
//        } catch (Exception e) {
//        }
//    }


    public void showExitDialog() {
        try {
            View view = View.inflate(this, R.layout.dialog_exit_layout, null);
            //showHomeScreenFacebookAds(view);
            showExitBanner();
            /*android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setView(view);*/
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_exit_layout);

            ((TextView) view.findViewById(R.id.exit_app)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    MainActivity.this.finish();
                }
            });
            ((TextView) view.findViewById(R.id.cancel_app)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            //this.alertDialog = alertDialogBuilder.create();
           dialog.setCancelable(false);
            dialog.show();
        } catch (Exception e) {
        }
    }

    private void showExitBanner() {
        adView = new AdView(this, getString(R.string.fb_banner), AdSize.RECTANGLE_HEIGHT_250);
        LinearLayout adContainer = findViewById(R.id.exit_ad_container);
        adContainer.addView(adView);
        adView.loadAd();
    }


    public void onBackPressed() {
       /* GalleryFragment localGalleryFragment = CollageHelper.getGalleryFragment(this);
        if (localGalleryFragment == null || !localGalleryFragment.isVisible()) {
          if (!Glob.isOnline(this)) {
            if (this.mBackPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Snackbar.make(this.mMainLayout, getString(R.string.txt_press_again_to_exit), -1).show();
            }
            this.mBackPressed = System.currentTimeMillis();
            } else {
                showExitDialog();
            }
        } else {
            localGalleryFragment.onBackPressed();
        }*/

        GalleryFragment localGalleryFragment = CollageHelper.getGalleryFragment(this);
        if (localGalleryFragment == null || !localGalleryFragment.isVisible()) {


            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.adview_layout_exit);
            if(Glob.isFB){
                refreshAd(dialog);
            }

            ((Button) dialog.findViewById(R.id.btnno)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            ((Button) dialog.findViewById(R.id.btnrate)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    try {

                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));
                    } catch (ActivityNotFoundException unused) {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                }
            });
            ((Button) dialog.findViewById(R.id.btnyes)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    dialog.dismiss();
                    finish();
                    System.exit(1);


                }
            });
            dialog.show();


        }else{
            localGalleryFragment.onBackPressed();
        }

    }

    public Uri getOutputMediaFileUri(int type) {
        try {
            return Uri.fromFile(/*getOutputMediaFile*/createImageFile(type));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;  //DT return null
    }

    private File createImageFile(int type) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

//    public File getOutputMediaFile(int type) {
//
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), this.IMAGE_DIRECTORY_NAME);
//
//
//        if (!mediaStorageDir.exists()) {
//            mediaStorageDir.mkdir();
//        }
//
//        if (mediaStorageDir.exists()/* || mediaStorageDir.mkdirs()*/) {
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//            if (type == this.MEDIA_TYPE_IMAGE) {
//                return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
//            }
//            return null;
//        }
//        Log.d(this.IMAGE_DIRECTORY_NAME, "Oops! Failed create " + this.IMAGE_DIRECTORY_NAME + " directory");
//        return null;
//    }

    private boolean checkAndRequestCollagePermissions() {
        int permissionCAMERA = ContextCompat.checkSelfPermission(this, "android.permission.CAMERA");
        int storagePermission = ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE");
        int storagePermission1 = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        List<String> listPermissionsNeeded = new ArrayList();
        if (storagePermission != 0) {
            listPermissionsNeeded.add("android.permission.READ_EXTERNAL_STORAGE");
        }
        if (storagePermission1 != 0) {
            listPermissionsNeeded.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        if (permissionCAMERA != 0) {
            listPermissionsNeeded.add("android.permission.CAMERA");
        }
        if (listPermissionsNeeded.isEmpty()) {
            return true;
        }
        ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), this.PERMISSION_COLLAGE_EDITOR);
        return false;
    }

    private boolean checkAndRequestSinglePermissions() {
        int permissionCAMERA = ContextCompat.checkSelfPermission(this, "android.permission.CAMERA");
        int storagePermission = ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE");
        int storagePermission1 = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        List<String> listPermissionsNeeded = new ArrayList();
        if (storagePermission != 0) {
            listPermissionsNeeded.add("android.permission.READ_EXTERNAL_STORAGE");
        }
        if (storagePermission1 != 0) {
            listPermissionsNeeded.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        if (permissionCAMERA != 0) {
            listPermissionsNeeded.add("android.permission.CAMERA");
        }
        if (listPermissionsNeeded.isEmpty()) {
            return true;
        }
        ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), this.PERMISSION_SINGLE_EDITOR);
        return false;
    }

    private boolean checkAndRequestScrapbookPermissions() {
        int permissionCAMERA = ContextCompat.checkSelfPermission(this, "android.permission.CAMERA");
        int storagePermission = ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE");
        int storagePermission1 = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        List<String> listPermissionsNeeded = new ArrayList();
        if (storagePermission != 0) {
            listPermissionsNeeded.add("android.permission.READ_EXTERNAL_STORAGE");
        }
        if (storagePermission1 != 0) {
            listPermissionsNeeded.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        if (permissionCAMERA != 0) {
            listPermissionsNeeded.add("android.permission.CAMERA");
        }
        if (listPermissionsNeeded.isEmpty()) {
            return true;
        }
        ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), this.PERMISSION_SCRAPBOOK_EDITOR);
        return false;
    }

    private boolean checkAndRequestCameraPermissions() {
        int permissionCAMERA = ContextCompat.checkSelfPermission(this, "android.permission.CAMERA");
        int storagePermission = ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE");
        int storagePermission1 = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        List<String> listPermissionsNeeded = new ArrayList();
        if (storagePermission != 0) {
            listPermissionsNeeded.add("android.permission.READ_EXTERNAL_STORAGE");
        }
        if (storagePermission1 != 0) {
            listPermissionsNeeded.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        if (permissionCAMERA != 0) {
            listPermissionsNeeded.add("android.permission.CAMERA");
        }
        if (listPermissionsNeeded.isEmpty()) {
            return true;
        }
        ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), this.PERMISSION_CAMERA_EDITOR);
        return false;
    }

    private boolean checkAndRequestMirrorPermissions() {
        int permissionCAMERA = ContextCompat.checkSelfPermission(this, "android.permission.CAMERA");
        int storagePermission = ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE");
        int storagePermission1 = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        List<String> listPermissionsNeeded = new ArrayList();
        if (storagePermission != 0) {
            listPermissionsNeeded.add("android.permission.READ_EXTERNAL_STORAGE");
        }
        if (storagePermission1 != 0) {
            listPermissionsNeeded.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        if (permissionCAMERA != 0) {
            listPermissionsNeeded.add("android.permission.CAMERA");
        }
        if (listPermissionsNeeded.isEmpty()) {
            return true;
        }
        ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), this.PERMISSION_MIRROR_EDITOR);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main_drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_rate) {
            showRatingDialog();

        } else if (id == R.id.action_share) {
            ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(this);
            intentBuilder.setText("Check this amazing app for making photo collages and photo editing: " + "http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                    .setType("text/plain")
                    .setSubject("Sharing " + getString(R.string.app_name) + " App");
            try {
                startActivity(intentBuilder.getIntent());
            } catch (Exception e) {
                Toast.makeText(this, "No app available for sharing", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (id == R.id.action_privacy) {
            if (!Glob.isOnline(MainActivity.this)) {
                Toast.makeText(MainActivity.this, "No Internet Connection..", Toast.LENGTH_SHORT).show();
                return true;
            }
            startActivity(new Intent(getApplicationContext(), PrivacyPolicyActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }

    private void refreshAd(final  Dialog dialog) {
        nativeAd = new NativeAd(this, getResources().getString(R.string.fb_exit_native_id));

        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");


                    // Race condition, load() called again before last ad was displayed
                    if (nativeAd == null || nativeAd != ad) {
                        return;
                    }
                    // Inflate Native Ad into Container
                    inflateAd(nativeAd,dialog);
                    RelativeLayout rlNative = dialog.findViewById(R.id.admob_native_container);
                    rlNative.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
            }
        });

        // Request an ad
        nativeAd.loadAd();

    }

    private void inflateAd(NativeAd nativeAd,Dialog dialog) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeAdLayout = dialog.findViewById(R.id.native_ad_container);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        ll_adView = (LinearLayout) inflater.inflate(R.layout.ad_unified2, nativeAdLayout, false);
        nativeAdLayout.addView(ll_adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = ll_adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(MainActivity.this, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        AdIconView nativeAdIcon = ll_adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = ll_adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = ll_adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = ll_adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = ll_adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = ll_adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = ll_adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                ll_adView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feedback) {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Feedback")
                        .setMessage(getString(R.string.feedback_message))
                        .setPositiveButton("Play Store", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                } else {
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(MainActivity.this, "Play Store Unavailable", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Not Now", null)
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_share) {
            final String sharedMessage = String.format(getString(R.string.share_message), BuildConfig.APPLICATION_ID);
            ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(this);
            intentBuilder.setText(sharedMessage)
                    .setType("text/plain")
                    .setSubject("Sharing Collage Maker");
            startActivity(intentBuilder.getIntent());
        } else if (id == R.id.nav_about) {
            try {
                final String appDescription = getString(R.string.app_description);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("App Description");
                builder.setMessage(appDescription)
                        .setPositiveButton("OK", null)
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_privacy) {
            try {
                final String privacyUrl = getString(R.string.privacy_policy_url);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrl)));
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Browser Not Available", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setUpNavigationDrawer(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    public void loadFbInterstitial(){
        interstitialAd = new com.facebook.ads.InterstitialAd(this, getResources().getString(R.string.fb_interstitial));
        interstitialAd.loadAd();
    }

}