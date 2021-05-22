package com.example.saatwikadmin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saatwikadmin.Model.Gift;
import com.example.saatwikadmin.Model.MyOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CouponForAll extends AppCompatActivity {

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Marketers");
    public List<Gift> gifts;
    RecyclerView recyclerView;
    GiftRecyclerAdapter giftRecyclerAdapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_for_all);

        searchView = findViewById(R.id.search_bar);
        gifts = new ArrayList<Gift>();

        recyclerView = (RecyclerView) findViewById(R.id.view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gifts = new ArrayList<Gift>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Gift gift = new Gift();
                    gift.setMobile(dataSnapshot1.getKey());
                    if (dataSnapshot1.child("money").exists()) {
                        gift.setPrice(Integer.parseInt(dataSnapshot1.child("money").getValue().toString()));
                    }
                    gifts.add(gift);
                }
                giftRecyclerAdapter = new GiftRecyclerAdapter(CouponForAll.this, gifts);
                recyclerView.setAdapter(giftRecyclerAdapter);
                giftRecyclerAdapter.notifyDataSetChanged();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        giftRecyclerAdapter.getFilter().filter(newText);
                        recyclerView.setAdapter(giftRecyclerAdapter);
                        giftRecyclerAdapter.notifyDataSetChanged();
                        return true;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
