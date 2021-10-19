package com.groceries.urabanseed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.Coupon;
import ui.CouponRecyclerAdapter;

public class CouponActivity extends AppCompatActivity {


    private Button button3;
    private EditText editText;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private String username;
    TextView back;
    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Takers");
    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Donors");
    DatabaseReference couponReference = FirebaseDatabase.getInstance().getReference().child("Coupons");
    String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
    ArrayList<Coupon> coupons;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        recyclerView = findViewById(R.id.coupon_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        editText = findViewById(R.id.referral_number);

        if(getIntent().hasExtra("coupon")){
            editText.setText(getIntent().getStringExtra("coupon"));
        }

        button3 = findViewById(R.id.refferal_button3);
        back = findViewById(R.id.referral_back);

        coupons = new ArrayList<>();

        couponReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Coupon coupon = dataSnapshot.getValue(Coupon.class);
                    coupons.add(coupon);
                }
                CouponRecyclerAdapter couponRecyclerAdapter = new CouponRecyclerAdapter(CouponActivity.this, coupons);
                recyclerView.setAdapter(couponRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(CouponActivity.this);
                progressDialog.setMessage("Availing the discount...");
                progressDialog.show();
                username = editText.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    progressDialog.dismiss();
                    Toast.makeText(CouponActivity.this, "Please enter the mobile number", Toast.LENGTH_LONG).show();
                    return;
                }

                if (username.length() < 10){
                    progressDialog.dismiss();
                    Toast.makeText(CouponActivity.this, "Please enter the valid mobile number", Toast.LENGTH_LONG).show();
                    return;
                }

                if (username.length() > 10){
                    progressDialog.dismiss();
                    Toast.makeText(CouponActivity.this, "Please enter the mobile number without country code", Toast.LENGTH_LONG).show();
                    return;
                }
                if (("+91"+username).equals(phone)){
                    progressDialog.dismiss();
                    Toast.makeText(CouponActivity.this, "Referral number and the user number cannot be the same", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    databaseReference1.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                progressDialog.dismiss();
                                Toast.makeText(CouponActivity.this, "Referral discount is available only for the first order", Toast.LENGTH_LONG).show();
                            }
                            else{
                                databaseReference2.child("+91" + editText.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            progressDialog.dismiss();
                                            Toast.makeText(CouponActivity.this, "Availed", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(CouponActivity.this, MyCart.class);
                                            intent.putExtra("discount", 5);
                                            intent.putExtra("gift", "+91" + username);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            progressDialog.dismiss();
                                            Toast.makeText(CouponActivity.this, "User whith phone number +91" + editText.getText().toString() + " does not exist", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CouponActivity.this, MyCart.class));
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CouponActivity.this, MyCart.class));
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        startActivity(new Intent(CouponActivity.this, MyCart.class));
        finish();
        return true;
    }
}
