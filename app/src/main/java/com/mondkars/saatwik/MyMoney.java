package com.mondkars.saatwik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyMoney extends AppCompatActivity {

    TextView congratulations, availment, rs, desc, earn;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_money);

        congratulations = findViewById(R.id.congratulations);
        availment = findViewById(R.id.availment);
        button = findViewById(R.id.share);
        rs = findViewById(R.id.rs);
        desc = findViewById(R.id.desc);
        earn = findViewById(R.id.earn);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Marketers").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        DatabaseReference totalReference = FirebaseDatabase.getInstance().getReference().child("Total Money").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                   congratulations.setText(snapshot.child("money").getValue().toString());
                }
                else{
                    congratulations.setText("0");
                    availment.setText("Share the app and once the referred user orders, you will get the reward money of 10% of his order in your wallet");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        totalReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    earn.setText("Total earning: "+"\u20B9"+snapshot.getValue().toString());
                }
                else{
                    earn.setText("Total earning: "+"\u20B9"+"0");
                    availment.setText("Share the app and once the referred user orders, you will get the reward money of 10% of his order in your wallet");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                final String appPackageName = getPackageName();
                String shareBody = "https://play.google.com/store/apps/details?id="+appPackageName + "\n\nHere is the awesome app for you! Use my phone number as the referral code and earn 10% OFF on your first order.\n\nClick on the above link and Download now!";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }
}