package com.trysafe.trysafe;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import io.github.angebagui.mediumtextview.MediumTextView;

public class PostViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);



//        TextView textView = findViewById(R.id.text);
//        textView.setText(getIntent().getStringExtra("title"));

//        HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.html_text);
//
//// loads html from string and displays http://www.example.com/cat_pic.png from the Internet
//        htmlTextView.setHtml(getIntent().getStringExtra("content"),
//                new HtmlHttpImageGetter(htmlTextView));

        MediumTextView mediumTextView = (MediumTextView)findViewById(R.id.medium_text_view);
        mediumTextView.setText("<h1>hello</h1>");

    }


}
