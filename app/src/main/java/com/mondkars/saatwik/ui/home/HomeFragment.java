package com.mondkars.saatwik.ui.home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;
import com.mondkars.saatwik.MyCart;
import com.mondkars.saatwik.R;
import com.mondkars.saatwik.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import model.Item;
import ui.ItemRecyclerAdapter;

public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchView searchView;
    public List<Item> itemList;
    DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference().child("cart");
    public RecyclerView recyclerView;
    public ItemRecyclerAdapter itemRecyclerAdapter;
    private ProgressBar progressBar;
    private TextView notext, nowarning;
    private ImageView noimage;
    CardView cardView;
    LinearLayout linearLayout;
    TextView number, price;
    DatabaseReference reference, databaseReference;
    DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Stop");
    View v1, v2, v3, v4;
    int a, b, c;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = view.findViewById(R.id.progressBar);


        FloatingActionButton fab = view.findViewById(R.id.fab2);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String text = "Hey there, I want some help regarding the urbanseed app.";// Replace with your message.

                    String toNumber = "919158385531"; // Replace with mobile phone number without +Sign or leading zeros, but with country code
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

        searchView = (SearchView) view.findViewById(R.id.search_bar25);
        notext = view.findViewById(R.id.notext);
        price = view.findViewById(R.id.total_price);
        linearLayout = view.findViewById(R.id.view_cart);
        number = view.findViewById(R.id.number_items);
        nowarning = view.findViewById(R.id.nowarning);
        noimage = view.findViewById(R.id.noimage);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_id);
        cardView = (CardView) view.findViewById(R.id.cart_card);
        v1 = view.findViewById(R.id.vi1);
        v2 = view.findViewById(R.id.vi2);
        v3 = view.findViewById(R.id.vi3);
        v4 = view.findViewById(R.id.vi4);
        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.VISIBLE);
        v3.setVisibility(View.VISIBLE);
        v4.setVisibility(View.VISIBLE);
        {
               {
                    v1.setVisibility(View.VISIBLE);
                    v2.setVisibility(View.VISIBLE);
                    v3.setVisibility(View.VISIBLE);
                    v4.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.VISIBLE);
                    noimage.setVisibility(View.INVISIBLE);
                    notext.setVisibility(View.INVISIBLE);
                    nowarning.setVisibility(View.INVISIBLE);
                    searchView.setVisibility(View.VISIBLE);
                    final ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Tab names");
                    progressBar.setVisibility(View.VISIBLE);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String meal = dataSnapshot.child("Meal").getValue().toString();
                            String spices = dataSnapshot.child("Spices").getValue().toString();
                            String special = dataSnapshot.child("Special").getValue().toString();

                            adapter.AddFragment(new FirstFragment(), meal);
                            adapter.AddFragment(new SecondFragment(), spices);
                            adapter.AddFragment(new ThirdFragment(), special);

                            viewPager.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.INVISIBLE);
                            tabLayout.setupWithViewPager(viewPager);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    itemList = new ArrayList<>();

                    recyclerView = view.findViewById(R.id.view_2);

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

                                                itemList.add(ameya);
                                            }
                                        }
                                        itemRecyclerAdapter = new ItemRecyclerAdapter(getActivity(), itemList);

                                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                            @Override
                                            public boolean onQueryTextSubmit(String query) {
                                                return false;
                                            }
                                            @Override
                                            public boolean onQueryTextChange(String newText) {
                                                itemRecyclerAdapter.getFilter().filter(newText);
                                                recyclerView.setAdapter(itemRecyclerAdapter);
                                                itemRecyclerAdapter.notifyDataSetChanged();
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
                                    a = Integer.parseInt(dataSnapshot1.child("number").getValue().toString());
                                    b = Integer.parseInt(dataSnapshot1.child("price").getValue().toString());
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
                        String k = String.valueOf(c);
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
        return view;
    }
    }