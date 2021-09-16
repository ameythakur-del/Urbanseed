package com.groceries.urabanseed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
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

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

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
                    availment.setText("Share the app and once the referred user orders, you will get the reward money of 5% of his order in your wallet");
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
                    availment.setText("Share the app and once the referred user orders, you will get the reward money of 5% of his order in your wallet");
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
                String shareBody = "सावंतवाडी शहरामध्ये घरपोच किराणा व भाजीपाला मिळण्यासाठी खालील अँप डाउनलोड करा. ऑर्डर करताना माझा नंबर टाकून तुमच्या पहिल्या ऑर्डर वर ५% सूट मिळवा. आत्ताच डाउनलोड करा." + "\n\nhttps://play.google.com/store/apps/details?id="+appPackageName;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }
}