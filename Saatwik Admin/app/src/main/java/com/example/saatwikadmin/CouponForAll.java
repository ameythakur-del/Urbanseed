package com.example.saatwikadmin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class CouponForAll extends AppCompatActivity {

    EditText coupon, discount, condition;
    Button apply;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Coupons");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_for_all);

        coupon = findViewById(R.id.coupon);
        discount = findViewById(R.id.coupon_discount);
        apply = findViewById(R.id.apply);
        condition = findViewById(R.id.coupon_condition);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Coupon = coupon.getText().toString().trim();
                String Discount = discount.getText().toString().trim();
                String Condition = condition.getText().toString().trim();
                String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                reference.child(Coupon).child("code").setValue(Coupon);
                reference.child(Coupon).child("percent").setValue(Discount);
                reference.child(Coupon).child("condition").setValue(Condition);
                Toast.makeText(CouponForAll.this, "Done", Toast.LENGTH_LONG).show();
            }
        });
    }
}
