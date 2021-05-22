package com.example.saatwikadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TagImageNavigation extends AppCompatActivity {

    Button button1, button2, button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_image_navigation);

        button1 = findViewById(R.id.first_tab);
        button2 = findViewById(R.id.second_tab);
        button3 = findViewById(R.id.third_tab);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TagImageNavigation.this, DeleteImage.class));
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TagImageNavigation.this, DeleteImage2.class));
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TagImageNavigation.this, DeleteImage3.class));
            }
        });
    }
}