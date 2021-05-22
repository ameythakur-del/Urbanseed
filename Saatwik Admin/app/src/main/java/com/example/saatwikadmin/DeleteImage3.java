package com.example.saatwikadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.saatwikadmin.Utils.Tag;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeleteImage3 extends AppCompatActivity {

    DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("padartha");
    RecyclerView recyclerView;
    static List<Tag> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_image3);

        ProgressDialog progressDialog = new ProgressDialog(DeleteImage3.this);
        progressDialog.setMessage("Loading Images");
        progressDialog.show();

        recyclerView = findViewById(R.id.image_recycle3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DeleteImage3.this));

        imageList = new ArrayList<Tag>();

        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    {
                        Tag tag = dataSnapshot1.getValue(Tag.class);
                        {
                            imageList.add(tag);
                        }
                    }
                }
                progressDialog.cancel();
                ImageRecyclerAdapter3 imageRecyclerAdapter = new ImageRecyclerAdapter3(DeleteImage3.this, imageList);
                recyclerView.setAdapter(imageRecyclerAdapter);
                imageRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}