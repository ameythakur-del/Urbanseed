package com.example.saatwikadmin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saatwikadmin.Model.MyOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class GalleryFragment extends Fragment {

    public List<MyOrder> myCart;
    public RecyclerView recyclerView;
    public CartRecyclerAdapter cartRecyclerAdapter;
    DatabaseReference reference, databaseReference;
    public Integer cost = 0;
    public ProgressBar progressBar;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Button button, coupon, not_serving;
    MediaPlayer mediaPlayer;
    int c = 0;
    final String CHANNEL_ID = "personal notifications";
    final int NOTIFICATION_ID = 001;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        myCart = new ArrayList<MyOrder>();
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.t);

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        button = view.findViewById(R.id.history_button);
        coupon = view.findViewById(R.id.coupon_for_all);
        not_serving = view.findViewById(R.id.not_serving_now);
        not_serving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NotServing.class));
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OrderHistory.class));
            }
        });

        coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CouponForAll.class));
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.order_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        progressBar = view.findViewById(R.id.progressBar);
        {
            {

                {
                    progressBar.setVisibility(View.VISIBLE);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final CollectionReference collectionReference = db.collection("Registering users");

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            myCart = new ArrayList<MyOrder>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                MyOrder users = dataSnapshot1.getValue(MyOrder.class);
                                users.setUserId(dataSnapshot1.getKey());
                                myCart.add(users);
                            }

                            progressBar.setVisibility(view.INVISIBLE);
                            cartRecyclerAdapter = new CartRecyclerAdapter(getActivity(), myCart);
                            recyclerView.setAdapter(cartRecyclerAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        }

        return view;
    }
}
