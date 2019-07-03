package com.example.joorebelo.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class WebHotelsNearActivity extends AppCompatActivity {

    String webLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_hotels_near);

        //get place given by intent
        Intent i = getIntent();
        webLink = i.getStringExtra("webLink");
        this.setTitle(i.getStringExtra("placeName"));


        WebView webHotels = findViewById(R.id.webHotels);

        webHotels.clearCache(true);
        webHotels.clearHistory();
        webHotels.getSettings().setJavaScriptEnabled(true);
        webHotels.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);



        //Toast.makeText(this,"web: " + webLink, Toast.LENGTH_LONG).show();
        webHotels.loadUrl("https://www.trivago." + webLink);
    }
}
