package com.example.saatwikadmin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saatwikadmin.Model.History;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderHistory extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Order history");
    List <History> HistoryList;
    HistoryRecyclerAdapter historyRecyclerAdapter;
    ProgressDialog progressDialog;
    public Integer cost=0;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        HistoryList = new ArrayList<History>();
        recyclerView = findViewById(R.id.history_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        textView = findViewById(R.id.total);

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading history");
        progressDialog.show();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HistoryList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    History history = dataSnapshot1.getValue(History.class);
                    HistoryList.add(history);
                    int temp = Integer.parseInt(history.getPrice());
                    int temp2 = Integer.parseInt(history.getNumber());
                    cost = cost + temp*temp2;
                }
                textView.setText("Total : " + cost.toString());
                progressDialog.dismiss();
                historyRecyclerAdapter = new HistoryRecyclerAdapter(OrderHistory.this, HistoryList);
                recyclerView.setAdapter(historyRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
