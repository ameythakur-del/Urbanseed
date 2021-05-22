package com.mondkars.saatwik;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutUsFragment  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_fragment);


        WebView view = (WebView) findViewById(R.id.textContent);
        String text;
        text = "<html><body><p align=\"justify\">";
        text+= "Urbanseed is an online grocery store. All kinds of groceries including spices to grains are available on this app. You just have to order and we will bring the products at your door.";
        text+= "</p></body></html>";
        view.loadData(text, "text/html", "utf-8");
    }
}
