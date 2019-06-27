package com.trysafe.trysafe;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {

    private WebView webview;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Toolbar toolbar = findViewById(R.id.appBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

        webview = findViewById(R.id.webView);
        progressBar = findViewById(R.id.webViewProgressBar);

        webview.setVisibility(View.INVISIBLE);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Toast.makeText(WebViewActivity.this, "Page Started Loading", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);
                Toast.makeText(WebViewActivity.this, "Sucess", Toast.LENGTH_SHORT).show();
                String javaScript ="javascript:(function() { var a= document.getElementsByTagName('header');a[0].hidden='true';})()";
                String javaScript1 ="javascript:(function() { var h = document.getElementById('header-ads');h.hidden='true';})()";
                webview.loadUrl(javaScript);
            }
        });

        webview.loadUrl(getIntent().getStringExtra("url"));
    }
}
