package com.helloworld.photoeditor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class PrivacyPolicyActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressDialog dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        dialog = new ProgressDialog(PrivacyPolicyActivity.this);
        this.webView = findViewById(R.id.wvPrivacyPolicy);
        WebSettings webSettings = this.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        this.webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(PrivacyPolicyActivity.this, description, Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setMessage("Loading...Please wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        this.webView.loadUrl(Glob.privacy_link);
    }

    public void onBackPressed() {
        finish();
    }
}
