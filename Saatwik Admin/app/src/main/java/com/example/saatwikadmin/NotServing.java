package com.example.saatwikadmin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotServing extends AppCompatActivity implements View.OnClickListener {

    Button stop, start;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Stop");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_serving);

        stop = findViewById(R.id.stop_serving);
        start = findViewById(R.id.start_serving);
        stop.setOnClickListener(this);
        start.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == stop){
            reference.setValue("True");
            Toast.makeText(NotServing.this, "Done", Toast.LENGTH_LONG).show();
        }
        if (v == start){
            reference.setValue("False");
            Toast.makeText(NotServing.this, "Done", Toast.LENGTH_LONG).show();
        }
    }
}
