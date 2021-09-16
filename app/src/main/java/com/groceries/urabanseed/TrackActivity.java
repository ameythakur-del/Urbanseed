package com.groceries.urabanseed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrackActivity extends AppCompatActivity {

    View view_order_placed,view_order_confirmed,view_order_processed,view_order_pickup,con_divider,ready_divider,placed_divider;
    ImageView img_orderconfirmed,orderprocessed,orderpickup;
    TextView textorderpickup,text_confirmed,textorderprocessed, n, m;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getPhoneNumber());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        view_order_placed=findViewById(R.id.view_order_placed);
        view_order_confirmed=findViewById(R.id.view_order_confirmed);
        view_order_processed=findViewById(R.id.view_order_processed);
        view_order_pickup=findViewById(R.id.view_order_pickup);
        placed_divider=findViewById(R.id.placed_divider);
        con_divider=findViewById(R.id.con_divider);
        ready_divider=findViewById(R.id.ready_divider);

        textorderpickup=findViewById(R.id.textorderpickup);
        text_confirmed=findViewById(R.id.text_confirmed);
        textorderprocessed=findViewById(R.id.textorderprocessed);

        img_orderconfirmed=findViewById(R.id.img_orderconfirmed);
        orderprocessed=findViewById(R.id.orderprocessed);
        orderpickup=findViewById(R.id.orderpickup);
        n = findViewById(R.id.pickup_desc);
        m = findViewById(R.id.processed_desc);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Take Away").exists()){
                    textorderpickup.setText("Ready for pickup");
                    n.setText("You have pick up your order");
                    m.setText("Now, You can pick your order from Mondkar's food");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else if (!dataSnapshot.child("status").exists()) {
                    float alfa = (float) 0.5;
                    setStatus(alfa);
                }
                else if (dataSnapshot.child("status").exists()) {
                    if (dataSnapshot.child("status").getValue().toString().equals("Sorry ! We cant place your order.")){
                        finish();
                    }
                    else if (dataSnapshot.child("status").getValue().toString().equals("Your order is approved by our team, Waiting for Arrival !") || dataSnapshot.child("status").getValue().toString().equals("Your order is aproved by our team. We will let you know for pickup once its ready.")) {
                        float alfa = (float) 1;
                        setStatus1(alfa);
                    }
                    else if (dataSnapshot.child("status").getValue().toString().equals("We are Out for delivery. Coming soon !") || dataSnapshot.child("status").getValue().toString().equals("Your order is ready to pickup")) {
                        float alfa = (float) 1;
                        setStatus2(alfa);
                    }
                }
            }
            private void setStatus(float alfa) {
                float myf= (float) 0.5;
                view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
                view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
                orderprocessed.setAlpha(alfa);
                view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
                con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
                placed_divider.setAlpha(alfa);
                img_orderconfirmed.setAlpha(alfa);
                placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
                text_confirmed.setAlpha(alfa);
                textorderprocessed.setAlpha(alfa);
                view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
                ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
                orderpickup.setAlpha(alfa);

                textorderpickup.setAlpha(myf);




            }

            private void setStatus1(float alfa) {
                float myf= (float) 0.5;
                view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
                view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
                orderprocessed.setAlpha(myf);
                view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
                con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
                placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
                img_orderconfirmed.setAlpha(alfa);

                text_confirmed.setAlpha(alfa);
                textorderprocessed.setAlpha(myf);
                view_order_pickup.setAlpha(myf);
                ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
                orderpickup.setAlpha(myf);
                view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
                textorderpickup.setAlpha(myf);
            }

            private void setStatus2(float alfa) {
                float myf= (float) 0.5;
                view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
                view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
                orderprocessed.setAlpha(alfa);
                view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
                con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
                placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
                img_orderconfirmed.setAlpha(alfa);

                text_confirmed.setAlpha(alfa);
                textorderprocessed.setAlpha(alfa);
                view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
                ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
                textorderpickup.setAlpha(myf);
                orderpickup.setAlpha(myf);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
