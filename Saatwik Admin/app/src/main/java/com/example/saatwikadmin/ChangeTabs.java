package com.example.saatwikadmin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeTabs extends AppCompatActivity {

    EditText meal, spices, special;
    Button button;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tab names");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_tabs);

        meal = findViewById(R.id.mealdoor);
        spices = findViewById(R.id.spices);
        special = findViewById(R.id.mondkarspecial);

        button = findViewById(R.id.submit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Meal = meal.getText().toString().trim();
                String Spices = spices.getText().toString().trim();
                String Special = special.getText().toString().trim();

                if(!TextUtils.isEmpty(Meal)) {
                    reference.child("Meal").setValue(Meal);
                }
                if(!TextUtils.isEmpty(Spices)) {
                    reference.child("Spices").setValue(Spices);
                }
                if(!TextUtils.isEmpty(Special)) {
                    reference.child("Special").setValue(Special);
                }

                Toast.makeText(ChangeTabs.this, "Done", Toast.LENGTH_LONG).show();
            }
        });
    }
}
