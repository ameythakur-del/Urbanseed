package com.example.saatwikadmin;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.saatwikadmin.Model.Item;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UpdateActivity  extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchView searchView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    public List<Item> itemList;
    public RecyclerView recyclerView;
    public ItemRecyclerAdapter itemRecyclerAdapter;
    private ProgressBar progressBar;
    private TextView apply;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String username;
    // Write a message to the database
    DatabaseReference reference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new FirstFragment(), "meal@door");
        adapter.AddFragment(new SecondFragment(), "Spices");
        adapter.AddFragment(new ThirdFragment(), "Mondkar's special");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        itemList = new ArrayList<>();




        recyclerView = findViewById(R.id.view_2);


        reference = FirebaseDatabase.getInstance().getReference().child("items");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        reference = FirebaseDatabase.getInstance().getReference();


        {
            {


                {

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            itemList = new ArrayList<Item>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                {
                                    Item ameya = dataSnapshot1.getValue(Item.class);
                                    itemList.add(ameya);
                                }
                            }

                            itemRecyclerAdapter = new ItemRecyclerAdapter(UpdateActivity.this, itemList);

                            searchView = (SearchView) findViewById(R.id.search_bar25);

                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    itemRecyclerAdapter.getFilter().filter(newText);
                                    recyclerView.setAdapter(itemRecyclerAdapter);
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
        }
    }
}

