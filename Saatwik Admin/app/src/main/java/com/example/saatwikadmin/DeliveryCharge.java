package com.example.saatwikadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeliveryCharge extends Fragment {
    private Button add;
    private EditText charge, min;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Delivery Slots");
    RecyclerView recyclerView;
    SlotRecyclerAdapter slotRecyclerAdapter;
    ArrayList<Slot> slots;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.delivery_charge, container, false);
        
        add = (Button) view.findViewById(R.id.add_slot);

        recyclerView = view.findViewById(R.id.recyclerview);

        slots = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Slot slot = dataSnapshot1.getValue(Slot.class);
                    slots.add(slot);
                }
                slotRecyclerAdapter = new SlotRecyclerAdapter(getActivity(), slots);
                recyclerView.setAdapter(slotRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddSlot.class));
            }
        });
        return view;
    }
}
