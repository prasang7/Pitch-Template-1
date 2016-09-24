package com.pitchtemplate;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebsiteOpener extends AppCompatActivity {

    WebView my_webview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        setContentView(R.layout.website_opener);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle extras = getIntent().getExtras();
        String websiteURL = extras.getString("KEY");
        Toast.makeText(WebsiteOpener.this, "Please wait, opening website: " + websiteURL, Toast.LENGTH_SHORT).show();
        getSupportActionBar().setTitle(websiteURL);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        my_webview = (WebView)findViewById(R.id.wv_websiteOpener);
        WebSettings webSettings = my_webview.getSettings();
        webSettings.getJavaScriptEnabled();
        my_webview.setWebViewClient(new customWebViewClient());
        my_webview.loadUrl("https://" + websiteURL);
    }

    private class customWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(WebsiteOpener.this, MainActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
