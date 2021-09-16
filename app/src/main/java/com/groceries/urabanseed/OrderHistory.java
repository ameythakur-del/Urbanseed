package com.groceries.urabanseed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.History;
import ui.HistoryRecyclerAdapter;

public class OrderHistory extends AppCompatActivity {

    List<History> histories;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Order History");
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(!FirebaseAuth.getInstance().getCurrentUser().equals(null))
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<History> histories = new ArrayList<>();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    History history = new History();
                    history.setDate(snapshot1.getKey());
                    histories.add(history);
                }
                Collections.reverse(histories);
                HistoryRecyclerAdapter historyRecyclerAdapter = new HistoryRecyclerAdapter(OrderHistory.this, histories);
                recyclerView.setAdapter(historyRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}