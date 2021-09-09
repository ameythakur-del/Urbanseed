package com.example.saatwikadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddSlot extends AppCompatActivity {

    EditText openSelect, closeSelect, charges, amount;
    String open, close, Amount;
    Button add;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Delivery Slots");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_slot);
        
        openSelect = findViewById(R.id.open_select);
        closeSelect = findViewById(R.id.close_select);
        charges = findViewById(R.id.charges);
        add = findViewById(R.id.add);
        amount = findViewById(R.id.amount);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String charge = charges.getText().toString();
                open = openSelect.getText().toString();
                close = closeSelect.getText().toString();
                Amount = amount.getText().toString();

                databaseReference.child(open + " - " + close).child("starttime").setValue(open);
                databaseReference.child(open + " - " + close).child("closetime").setValue(close);
                databaseReference.child(open + " - " + close).child("charges").setValue(charge);
                databaseReference.child(open + " - " + close).child("amount").setValue(Amount);

                finish();
            }
        });
    }
}