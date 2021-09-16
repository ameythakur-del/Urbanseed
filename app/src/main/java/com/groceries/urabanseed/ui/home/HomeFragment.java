package com.groceries.urabanseed.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.groceries.urabanseed.MyCart;
import com.groceries.urabanseed.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import model.Image;
import model.Item;
import ui.ItemRecyclerAdapter;
import ui.SliderAdapterExample;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView, recyclerview2;
    private EditText searchView;
    DatabaseReference reference,reference2, reference3;
    public List<Item> itemList;
    ChipGroup chipGroup;
    String tal;
    Timer timer;
    private Handler mHandler;
    public SliderView horizontal;
    public List<Image> snacksList;
    DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Stop");
    DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference().child("cart");
    float a, b, c;
    CardView cardView;
    TextView number, price;
    MaterialCardView linearLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        cardView = (CardView) root.findViewById(R.id.cart_card);
        mHandler = new Handler();
        searchView = root.findViewById(R.id.search_bar);

        price = root.findViewById(R.id.total_price);
        linearLayout = root.findViewById(R.id.view_cart);
        number = root.findViewById(R.id.number_items);

        reference2 = FirebaseDatabase.getInstance().getReference().child("Snacks");
        recyclerView = root.findViewById(R.id.view);
        reference = FirebaseDatabase.getInstance().getReference().child("items");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        chipGroup = root.findViewById(R.id.chipGroup);

        ProgressBar progressBar = root.findViewById(R.id.progress);
        CardView cardView2 = root.findViewById(R.id.cardView8);

        horizontal = root.findViewById(R.id.horizontal_view);

        reference3 = FirebaseDatabase.getInstance().getReference().child("Off Tag");
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);


        FloatingActionButton fab = root.findViewById(R.id.fab2);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String text = "Hey there, I want some help regarding the urbanseed app.";// Replace with your message.

                    String toNumber = "919403768656"; // Replace with mobile phone number without +Sign or leading zeros, but with country code
                    //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.


                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
                    startActivity(intent);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals("True")){
                    {
                        {
                            {
                                reference3.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        snacksList = new ArrayList<Image>();
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            {
                                                final Image image = dataSnapshot1.getValue(Image.class);
                                                {
                                                    snacksList.add(image);
                                                }
                                            }
                                        }
                                        SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(getActivity(), snacksList);
                                        horizontal.setSliderAdapter(sliderAdapterExample);
                                        sliderAdapterExample.notifyDataSetChanged();
                                        horizontal.startAutoCycle();
                                        horizontal.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
                                        horizontal.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
                                        horizontal.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        }
                    }
                }

                else{
                    {
                        {
                            {
                                reference2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        snacksList = new ArrayList<Image>();
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            {
                                                final Image image = dataSnapshot1.getValue(Image.class);
                                                {
                                                    snacksList.add(image);
                                                }
                                            }
                                        }
                                        SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(getActivity(), snacksList);
                                        horizontal.setSliderAdapter(sliderAdapterExample);
                                        sliderAdapterExample.notifyDataSetChanged();
                                        horizontal.startAutoCycle();
                                        horizontal.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
                                        horizontal.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
                                        horizontal.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList = new ArrayList<Item>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    {
                        Item item = dataSnapshot1.getValue(Item.class);
                        if(item.getVisibility().equals(true)) {
                            itemList.add(item);
                        }
                    }
                }
                ItemRecyclerAdapter hospitalRecyclerAdapter = new ItemRecyclerAdapter(getActivity(), itemList);
                progressBar.setVisibility(View.INVISIBLE);
                cardView2.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(hospitalRecyclerAdapter);
                hospitalRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {

                Chip chip = chipGroup.findViewById(i);

                if(chip != null){
                    tal = chip.getText().toString();

                    if(tal.equals("All")){
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                itemList = new ArrayList<Item>();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    {
                                        Item item = dataSnapshot1.getValue(Item.class);
                                        if(item.getVisibility().equals(true)) {
                                            itemList.add(item);
                                        }
                                    }
                                }
                                ItemRecyclerAdapter hospitalRecyclerAdapter = new ItemRecyclerAdapter(getActivity(), itemList);
                                progressBar.setVisibility(View.INVISIBLE);
                                cardView2.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(hospitalRecyclerAdapter);
                                hospitalRecyclerAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    else {
                        Query query = reference.orderByChild("category").equalTo(tal);

                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                itemList = new ArrayList<Item>();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    {
                                        Item item = dataSnapshot1.getValue(Item.class);
                                        if(item.getVisibility().equals(true)) {
                                            itemList.add(item);
                                        }
                                    }
                                }
                                ItemRecyclerAdapter itemRecyclerAdapter = new ItemRecyclerAdapter(getActivity(), itemList);
                                recyclerView.setAdapter(itemRecyclerAdapter);
                                itemRecyclerAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList = new ArrayList<Item>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    {
                        Item item = dataSnapshot1.getValue(Item.class);
                        if(item.getVisibility().equals(true)) {
                            itemList.add(item);
                        }
                    }
                }
                ItemRecyclerAdapter itemRecyclerAdapter = new ItemRecyclerAdapter(getActivity(), itemList);

                searchView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }



                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        itemRecyclerAdapter.getFilter().filter(s);
                                        recyclerView.setAdapter(itemRecyclerAdapter);
                                    }
                                });
                            }
                        }, 1000);
                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Query query = cartReference.orderByChild("userPhone");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int count = 0;
                    c=0;
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        {
                            if(dataSnapshot1.child("userPhone").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString())){
                                cardView.setVisibility(View.VISIBLE);
                                count++;
                                a = Float.parseFloat(dataSnapshot1.child("number").getValue().toString());
                                b = Float.parseFloat(dataSnapshot1.child("price").getValue().toString());
                                c = c + a*b;
                            }
                        }
                    }
                    if(String.valueOf(count).equals("0")){
                        cardView.setVisibility(View.INVISIBLE);
                        fab.setTranslationY(0);
                    }
                    else if(String.valueOf(count).equals("1")){
                        String a = String.valueOf(count);
                        number.setText(a + " item");
                        fab.setTranslationY(-150);
                    }
                    else {
                        fab.setTranslationY(-150);
                        String a = String.valueOf(count);
                        number.setText(a + " items");
                    }
                    String k = String.valueOf(Math.round(c));
                    price.setText("\u20B9"+k);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), MyCart.class));
                }
            });
        }

        return root;
    }

}