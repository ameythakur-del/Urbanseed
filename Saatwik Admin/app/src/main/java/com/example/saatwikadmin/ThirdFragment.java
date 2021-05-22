package com.example.saatwikadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saatwikadmin.Model.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ThirdFragment extends Fragment {



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
    private CollectionReference collectionReference = db.collection("tab1");
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private String text;
    private SearchView searchView;

    // Write a message to the database
    DatabaseReference reference, reference2;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId;

    public RecyclerView horizontal;

    public ThirdFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_fragment, container, false);

        itemList = new ArrayList<>();

        progressBar = view.findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.VISIBLE);


        recyclerView = view.findViewById(R.id.view);

        reference = FirebaseDatabase.getInstance().getReference().child("items");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                                    if(dataSnapshot1.child("category").exists())  {
                                        if (ameya.getCategory().equals("Masalyache Padartha")) {
                                            itemList.add(ameya);
                                        }
                                    }
                                }
                            }
                            itemRecyclerAdapter = new ItemRecyclerAdapter(getActivity(), itemList);
                            progressBar.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(itemRecyclerAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            return view;
        }
    }
}





