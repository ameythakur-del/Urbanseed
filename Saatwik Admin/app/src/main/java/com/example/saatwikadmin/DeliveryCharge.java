package com.example.saatwikadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeliveryCharge extends Fragment {
    private Button submit;
    private EditText charge, min;
    private DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.delivery_charge, container, false);
        charge = (EditText) view.findViewById(R.id.delivery_charge);
        min = (EditText) view.findViewById(R.id.minimum_amount);
        submit = (Button) view.findViewById(R.id.charge_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Ch = charge.getText().toString().trim();
                final String Min = min.getText().toString().trim();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("charge").child("delivery");
                databaseReference.setValue(Ch);
                databaseReference =  FirebaseDatabase.getInstance().getReference().child("charge").child("Min");
                databaseReference.setValue(Min);
            }
        });
        return view;
    }
}
